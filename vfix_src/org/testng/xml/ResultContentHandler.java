package org.testng.xml;

import java.util.List;
import org.testng.collections.Lists;
import org.testng.remote.strprotocol.GenericMessage;
import org.testng.remote.strprotocol.IRemoteSuiteListener;
import org.testng.remote.strprotocol.IRemoteTestListener;
import org.testng.remote.strprotocol.SuiteMessage;
import org.testng.remote.strprotocol.TestMessage;
import org.testng.remote.strprotocol.TestResultMessage;
import org.testng.reporters.XMLReporterConfig;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ResultContentHandler extends DefaultHandler {
   private int m_suiteMethodCount = 0;
   private int m_testMethodCount = 0;
   private SuiteMessage m_currentSuite;
   private TestMessage m_currentTest;
   private String m_className;
   private int m_passed;
   private int m_failed;
   private int m_skipped;
   private int m_invocationCount;
   private int m_currentInvocationCount;
   private TestResultMessage m_currentTestResult;
   private IRemoteSuiteListener m_suiteListener;
   private IRemoteTestListener m_testListener;
   private List<String> m_params = null;

   public ResultContentHandler(IRemoteSuiteListener suiteListener, IRemoteTestListener testListener, boolean resolveClasses) {
      this.m_suiteListener = suiteListener;
      this.m_testListener = testListener;
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) {
      p("Start " + qName);
      if ("suite".equals(qName)) {
         this.m_suiteListener.onInitialization(new GenericMessage(1));
         this.m_suiteMethodCount = 0;
         this.m_currentSuite = new SuiteMessage(attributes.getValue("name"), true, this.m_suiteMethodCount);
         this.m_suiteListener.onStart(this.m_currentSuite);
      } else if ("test".equals(qName)) {
         this.m_passed = this.m_failed = this.m_skipped = 0;
         this.m_currentTest = new TestMessage(true, this.m_currentSuite.getSuiteName(), attributes.getValue("name"), this.m_testMethodCount, this.m_passed, this.m_failed, this.m_skipped, 0);
         this.m_testListener.onStart(this.m_currentTest);
      } else if ("class".equals(qName)) {
         this.m_className = attributes.getValue("name");
      } else if ("test-method".equals(qName)) {
         Integer status = XMLReporterConfig.getStatus(attributes.getValue("status"));
         this.m_currentTestResult = new TestResultMessage(status, this.m_currentSuite.getSuiteName(), this.m_currentTest.getTestName(), this.m_className, attributes.getValue("name"), attributes.getValue("description"), attributes.getValue("description"), new String[0], 0L, Long.parseLong(attributes.getValue("duration-ms")), "", this.m_invocationCount, this.m_currentInvocationCount);
         ++this.m_suiteMethodCount;
         ++this.m_testMethodCount;
         if (status == 1) {
            ++this.m_passed;
         } else if (status == 2) {
            ++this.m_failed;
         } else if (status == 3) {
            ++this.m_skipped;
         }
      } else if ("params".equals(qName)) {
         this.m_params = Lists.newArrayList();
      }

   }

   public void characters(char[] ch, int start, int length) {
      if (this.m_params != null) {
         String string = new String(ch, start, length);
         if (string.trim().length() != 0) {
            this.m_params.add(string);
         }
      }

   }

   public void endElement(String uri, String localName, String qName) {
      if ("suite".equals(qName)) {
         this.m_suiteListener.onFinish(new SuiteMessage((String)null, false, this.m_suiteMethodCount));
         this.m_currentSuite = null;
      } else if ("test".equals(qName)) {
         this.m_currentTest = new TestMessage(false, this.m_currentSuite.getSuiteName(), (String)null, this.m_testMethodCount, this.m_passed, this.m_failed, this.m_skipped, 0);
         this.m_testMethodCount = 0;
         this.m_testListener.onFinish(this.m_currentTest);
      } else if ("class".equals(qName)) {
         this.m_className = null;
      } else if ("test-method".equals(qName)) {
         switch(this.m_currentTestResult.getResult()) {
         case 1:
            this.m_testListener.onTestSuccess(this.m_currentTestResult);
            break;
         case 2:
            this.m_testListener.onTestFailure(this.m_currentTestResult);
            break;
         case 3:
            this.m_testListener.onTestSkipped(this.m_currentTestResult);
            break;
         default:
            p("Ignoring test status:" + this.m_currentTestResult.getResult());
         }
      } else if ("params".equals(qName)) {
         String[] params = new String[this.m_params.size()];

         for(int i = 0; i < this.m_params.size(); ++i) {
            params[i] = "@:" + (String)this.m_params.get(i);
         }

         this.m_currentTestResult.setParameters(params);
         this.m_params = null;
      }

   }

   private static void p(String string) {
   }
}
