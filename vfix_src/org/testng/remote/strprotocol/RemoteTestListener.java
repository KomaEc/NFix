package org.testng.remote.strprotocol;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;
import org.testng.xml.XmlTest;

public class RemoteTestListener implements IResultListener2 {
   private final MessageHub m_sender;
   private ISuite m_suite;
   private XmlTest m_xmlTest;
   private ITestContext m_currentTestContext;

   public RemoteTestListener(ISuite suite, XmlTest test, MessageHub msh) {
      this.m_sender = msh;
      this.m_suite = suite;
      this.m_xmlTest = test;
   }

   public void onStart(ITestContext testCtx) {
      this.m_currentTestContext = testCtx;
      this.m_sender.sendMessage(new TestMessage(testCtx, true));
   }

   public void onFinish(ITestContext testCtx) {
      this.m_sender.sendMessage(new TestMessage(testCtx, false));
      this.m_currentTestContext = null;
   }

   public void onTestStart(ITestResult testResult) {
      TestResultMessage trm = null;
      if (null == this.m_currentTestContext) {
         trm = new TestResultMessage(this.m_suite.getName(), this.m_xmlTest.getName(), testResult);
      } else {
         trm = new TestResultMessage(this.m_currentTestContext, testResult);
      }

      this.m_sender.sendMessage(trm);
   }

   public void beforeConfiguration(ITestResult tr) {
   }

   public void onTestFailedButWithinSuccessPercentage(ITestResult testResult) {
      if (null == this.m_currentTestContext) {
         this.m_sender.sendMessage(new TestResultMessage(this.m_suite.getName(), this.m_xmlTest.getName(), testResult));
      } else {
         this.m_sender.sendMessage(new TestResultMessage(this.m_currentTestContext, testResult));
      }

   }

   public void onTestFailure(ITestResult testResult) {
      if (null == this.m_currentTestContext) {
         this.m_sender.sendMessage(new TestResultMessage(this.m_suite.getName(), this.m_xmlTest.getName(), testResult));
      } else {
         this.m_sender.sendMessage(new TestResultMessage(this.m_currentTestContext, testResult));
      }

   }

   public void onTestSkipped(ITestResult testResult) {
      if (null == this.m_currentTestContext) {
         this.m_sender.sendMessage(new TestResultMessage(this.m_suite.getName(), this.m_xmlTest.getName(), testResult));
      } else {
         this.m_sender.sendMessage(new TestResultMessage(this.m_currentTestContext, testResult));
      }

   }

   public void onTestSuccess(ITestResult testResult) {
      if (null == this.m_currentTestContext) {
         this.m_sender.sendMessage(new TestResultMessage(this.m_suite.getName(), this.m_xmlTest.getName(), testResult));
      } else {
         this.m_sender.sendMessage(new TestResultMessage(this.m_currentTestContext, testResult));
      }

   }

   public void onConfigurationFailure(ITestResult itr) {
      this.onTestFailure(itr);
   }

   public void onConfigurationSkip(ITestResult itr) {
      this.onTestSkipped(itr);
   }

   public void onConfigurationSuccess(ITestResult itr) {
   }
}
