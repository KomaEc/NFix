package org.testng.remote;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.testng.CommandLineArgs;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestRunnerFactory;
import org.testng.TestNG;
import org.testng.TestNGException;
import org.testng.TestRunner;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.remote.strprotocol.GenericMessage;
import org.testng.remote.strprotocol.IMessageSender;
import org.testng.remote.strprotocol.MessageHub;
import org.testng.remote.strprotocol.RemoteTestListener;
import org.testng.remote.strprotocol.SerializedMessageSender;
import org.testng.remote.strprotocol.StringMessageSender;
import org.testng.remote.strprotocol.SuiteMessage;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.reporters.TestHTMLReporter;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RemoteTestNG extends TestNG {
   private static final String LOCALHOST = "localhost";
   public static final String DEBUG_PORT = "12345";
   public static final String DEBUG_SUITE_FILE = "testng-customsuite.xml";
   public static final String DEBUG_SUITE_DIRECTORY = System.getProperty("java.io.tmpdir");
   public static final String PROPERTY_DEBUG = "testng.eclipse.debug";
   public static final String PROPERTY_VERBOSE = "testng.eclipse.verbose";
   private ITestRunnerFactory m_customTestRunnerFactory;
   private String m_host;
   private Integer m_port = null;
   private static Integer m_serPort = null;
   private static boolean m_debug;
   private static boolean m_dontExit;
   private static boolean m_ack;

   public void setHost(String host) {
      this.m_host = Utils.defaultIfStringEmpty(host, "localhost");
   }

   private void calculateAllSuites(List<XmlSuite> suites, List<XmlSuite> outSuites) {
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         XmlSuite s = (XmlSuite)i$.next();
         outSuites.add(s);
      }

   }

   public void run() {
      IMessageSender sender = m_serPort != null ? new SerializedMessageSender(this.m_host, m_serPort, m_ack) : new StringMessageSender(this.m_host, this.m_port);
      MessageHub msh = new MessageHub((IMessageSender)sender);
      msh.setDebug(isDebug());

      try {
         msh.connect();
         this.initializeSuitesAndJarFile();
         List<XmlSuite> suites = Lists.newArrayList();
         this.calculateAllSuites(this.m_suites, suites);
         if (suites.size() > 0) {
            int testCount = 0;

            XmlSuite suite;
            for(Iterator i$ = suites.iterator(); i$.hasNext(); testCount += suite.getTests().size()) {
               suite = (XmlSuite)i$.next();
            }

            GenericMessage gm = new GenericMessage(1);
            gm.setSuiteCount(suites.size());
            gm.setTestCount(testCount);
            msh.sendMessage(gm);
            this.addListener(new RemoteTestNG.RemoteSuiteListener(msh));
            this.setTestRunnerFactory(new RemoteTestNG.DelegatingTestRunnerFactory(this.buildTestRunnerFactory(), msh));
            super.run();
         } else {
            System.err.println("No test suite found. Nothing to run");
         }
      } catch (Throwable var10) {
         var10.printStackTrace(System.err);
      } finally {
         msh.shutDown();
         if (!m_debug && !m_dontExit) {
            System.exit(0);
         }

      }

   }

   protected ITestRunnerFactory buildTestRunnerFactory() {
      if (null == this.m_customTestRunnerFactory) {
         this.m_customTestRunnerFactory = new ITestRunnerFactory() {
            public TestRunner newTestRunner(ISuite suite, XmlTest xmlTest, List<IInvokedMethodListener> listeners) {
               TestRunner runner = new TestRunner(RemoteTestNG.this.getConfiguration(), suite, xmlTest, false, listeners);
               if (RemoteTestNG.this.m_useDefaultListeners) {
                  runner.addListener(new TestHTMLReporter());
                  runner.addListener(new JUnitXMLReporter());
               }

               return runner;
            }
         };
      }

      return this.m_customTestRunnerFactory;
   }

   public static void main(String[] args) throws ParameterException {
      CommandLineArgs cla = new CommandLineArgs();
      RemoteArgs ra = new RemoteArgs();
      new JCommander(Arrays.asList(cla, ra), args);
      m_dontExit = ra.dontExit;
      if (cla.port != null && ra.serPort != null) {
         throw new TestNGException("Can only specify one of -port and -serport");
      } else {
         m_debug = cla.debug;
         m_ack = ra.ack;
         if (m_debug) {
            initAndRun(args, cla, ra);
         } else {
            initAndRun(args, cla, ra);
         }

      }
   }

   private static void initAndRun(String[] args, CommandLineArgs cla, RemoteArgs ra) {
      RemoteTestNG remoteTestNg = new RemoteTestNG();
      if (m_debug) {
         cla.port = Integer.parseInt("12345");
         ra.serPort = cla.port;
         cla.suiteFiles = Arrays.asList(DEBUG_SUITE_DIRECTORY + "testng-customsuite.xml");
      }

      remoteTestNg.configure(cla);
      remoteTestNg.setHost(cla.host);
      m_serPort = ra.serPort;
      remoteTestNg.m_port = cla.port;
      if (isVerbose()) {
         StringBuilder sb = new StringBuilder("Invoked with ");
         String[] arr$ = args;
         int len$ = args.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            sb.append(s).append(" ");
         }

         p(sb.toString());
      }

      validateCommandLineParameters(cla);
      remoteTestNg.run();
   }

   private static void p(String s) {
      if (isVerbose()) {
         System.out.println("[RemoteTestNG] " + s);
      }

   }

   public static boolean isVerbose() {
      boolean result = System.getProperty("testng.eclipse.verbose") != null || isDebug();
      return result;
   }

   public static boolean isDebug() {
      return m_debug || System.getProperty("testng.eclipse.debug") != null;
   }

   private String getHost() {
      return this.m_host;
   }

   private int getPort() {
      return this.m_port;
   }

   private static class DelegatingTestRunnerFactory implements ITestRunnerFactory {
      private final ITestRunnerFactory m_delegateFactory;
      private final MessageHub m_messageSender;

      DelegatingTestRunnerFactory(ITestRunnerFactory trf, MessageHub smsh) {
         this.m_delegateFactory = trf;
         this.m_messageSender = smsh;
      }

      public TestRunner newTestRunner(ISuite suite, XmlTest test, List<IInvokedMethodListener> listeners) {
         TestRunner tr = this.m_delegateFactory.newTestRunner(suite, test, listeners);
         tr.addListener(new RemoteTestListener(suite, test, this.m_messageSender));
         return tr;
      }
   }

   private static class RemoteSuiteListener implements ISuiteListener {
      private final MessageHub m_messageSender;

      RemoteSuiteListener(MessageHub smsh) {
         this.m_messageSender = smsh;
      }

      public void onFinish(ISuite suite) {
         this.m_messageSender.sendMessage(new SuiteMessage(suite, false));
      }

      public void onStart(ISuite suite) {
         this.m_messageSender.sendMessage(new SuiteMessage(suite, true));
      }
   }
}
