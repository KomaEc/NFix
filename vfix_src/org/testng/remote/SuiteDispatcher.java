package org.testng.remote;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SuiteRunner;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.internal.IConfiguration;
import org.testng.internal.Invoker;
import org.testng.internal.PropertiesFile;
import org.testng.remote.adapter.DefaultMastertAdapter;
import org.testng.remote.adapter.IMasterAdapter;
import org.testng.remote.adapter.RemoteResultListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteDispatcher {
   public static final String MASTER_STRATEGY = "testng.master.strategy";
   public static final String VERBOSE = "testng.verbose";
   public static final String MASTER_ADPATER = "testng.master.adpter";
   public static final String STRATEGY_TEST = "test";
   public static final String STRATEGY_SUITE = "suite";
   private final int m_verbose;
   private final boolean m_isStrategyTest;
   private final IMasterAdapter m_masterAdpter;

   public SuiteDispatcher(String propertiesFile) throws TestNGException {
      try {
         PropertiesFile file = new PropertiesFile(propertiesFile);
         Properties properties = file.getProperties();
         this.m_verbose = Integer.parseInt(properties.getProperty("testng.verbose", "1"));
         String strategy = properties.getProperty("testng.master.strategy", "suite");
         this.m_isStrategyTest = "test".equalsIgnoreCase(strategy);
         String adapter = properties.getProperty("testng.master.adpter");
         if (adapter == null) {
            this.m_masterAdpter = new DefaultMastertAdapter();
         } else {
            Class clazz = Class.forName(adapter);
            this.m_masterAdpter = (IMasterAdapter)clazz.newInstance();
         }

         this.m_masterAdpter.init(properties);
      } catch (Exception var7) {
         throw new TestNGException("Fail to initialize master mode", var7);
      }
   }

   public List<ISuite> dispatch(IConfiguration configuration, List<XmlSuite> suites, String outputDir, List<ITestListener> testListeners) {
      List result = Lists.newArrayList();

      try {
         Iterator i$;
         SuiteRunner suiteRunner;
         for(i$ = suites.iterator(); i$.hasNext(); result.add(suiteRunner)) {
            XmlSuite suite = (XmlSuite)i$.next();
            suite.setVerbose(this.m_verbose);
            suiteRunner = new SuiteRunner(configuration, suite, outputDir);
            RemoteResultListener listener = new RemoteResultListener(suiteRunner);
            if (this.m_isStrategyTest) {
               Iterator i$ = suite.getTests().iterator();

               while(i$.hasNext()) {
                  XmlTest test = (XmlTest)i$.next();
                  XmlSuite tmpSuite = new XmlSuite();
                  tmpSuite.setXmlPackages(suite.getXmlPackages());
                  tmpSuite.setJUnit(suite.isJUnit());
                  tmpSuite.setSkipFailedInvocationCounts(suite.skipFailedInvocationCounts());
                  tmpSuite.setName("Temporary suite for " + test.getName());
                  tmpSuite.setParallel(suite.getParallel());
                  tmpSuite.setParentModule(suite.getParentModule());
                  tmpSuite.setGuiceStage(suite.getGuiceStage());
                  tmpSuite.setParameters(suite.getParameters());
                  tmpSuite.setThreadCount(suite.getThreadCount());
                  tmpSuite.setDataProviderThreadCount(suite.getDataProviderThreadCount());
                  tmpSuite.setVerbose(suite.getVerbose());
                  tmpSuite.setObjectFactory(suite.getObjectFactory());
                  XmlTest tmpTest = new XmlTest(tmpSuite);
                  tmpTest.setBeanShellExpression(test.getExpression());
                  tmpTest.setXmlClasses(test.getXmlClasses());
                  tmpTest.setExcludedGroups(test.getExcludedGroups());
                  tmpTest.setIncludedGroups(test.getIncludedGroups());
                  tmpTest.setJUnit(test.isJUnit());
                  tmpTest.setMethodSelectors(test.getMethodSelectors());
                  tmpTest.setName(test.getName());
                  tmpTest.setParallel(test.getParallel());
                  tmpTest.setParameters(test.getLocalParameters());
                  tmpTest.setVerbose(test.getVerbose());
                  tmpTest.setXmlClasses(test.getXmlClasses());
                  tmpTest.setXmlPackages(test.getXmlPackages());
                  this.m_masterAdpter.runSuitesRemotely(tmpSuite, listener);
               }
            } else {
               this.m_masterAdpter.runSuitesRemotely(suite, listener);
            }
         }

         this.m_masterAdpter.awaitTermination(100000L);
         i$ = result.iterator();

         while(i$.hasNext()) {
            ISuite suite = (ISuite)i$.next();
            Iterator i$ = suite.getResults().values().iterator();

            while(i$.hasNext()) {
               ISuiteResult suiteResult = (ISuiteResult)i$.next();
               Collection<ITestResult>[] allTests = new Collection[]{suiteResult.getTestContext().getPassedTests().getAllResults(), suiteResult.getTestContext().getFailedTests().getAllResults(), suiteResult.getTestContext().getSkippedTests().getAllResults(), suiteResult.getTestContext().getFailedButWithinSuccessPercentageTests().getAllResults()};
               Collection[] arr$ = allTests;
               int len$ = allTests.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Collection<ITestResult> all = arr$[i$];
                  Iterator i$ = all.iterator();

                  while(i$.hasNext()) {
                     ITestResult tr = (ITestResult)i$.next();
                     Invoker.runTestListeners(tr, testListeners);
                  }
               }
            }
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      }

      return result;
   }
}
