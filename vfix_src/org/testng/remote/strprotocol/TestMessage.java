package org.testng.remote.strprotocol;

import org.testng.ITestContext;

public class TestMessage implements IStringMessage {
   protected final boolean m_testStart;
   protected final String m_suiteName;
   protected final String m_testName;
   protected final int m_testMethodCount;
   protected final int m_passedTestCount;
   protected final int m_failedTestCount;
   protected final int m_skippedTestCount;
   protected final int m_successPercentageFailedTestCount;

   public TestMessage(boolean isTestStart, String suiteName, String testName, int methodCount, int passedCount, int failedCount, int skippedCount, int percentageCount) {
      this.m_testStart = isTestStart;
      this.m_suiteName = suiteName;
      this.m_testName = testName;
      this.m_testMethodCount = methodCount;
      this.m_passedTestCount = passedCount;
      this.m_failedTestCount = failedCount;
      this.m_skippedTestCount = skippedCount;
      this.m_successPercentageFailedTestCount = percentageCount;
   }

   public TestMessage(ITestContext testContext, boolean isTestStart) {
      this(isTestStart, testContext.getSuite().getName(), testContext.getCurrentXmlTest().getName(), testContext.getAllTestMethods().length, testContext.getPassedTests().size(), testContext.getFailedTests().size(), testContext.getSkippedTests().size(), testContext.getFailedButWithinSuccessPercentageTests().size());
   }

   public boolean isMessageOnStart() {
      return this.m_testStart;
   }

   public String getMessageAsString() {
      StringBuffer buf = new StringBuffer();
      buf.append(this.m_testStart ? 101 : 102).append('\u0001').append(this.m_suiteName).append('\u0001').append(this.m_testName).append('\u0001').append(this.m_testMethodCount).append('\u0001').append(this.m_passedTestCount).append('\u0001').append(this.m_failedTestCount).append('\u0001').append(this.m_skippedTestCount).append('\u0001').append(this.m_successPercentageFailedTestCount);
      return buf.toString();
   }

   public String getSuiteName() {
      return this.m_suiteName;
   }

   public String getTestName() {
      return this.m_testName;
   }

   public boolean isTestStart() {
      return this.m_testStart;
   }

   public int getTestMethodCount() {
      return this.m_testMethodCount;
   }

   public int getSuccessPercentageFailedTestCount() {
      return this.m_successPercentageFailedTestCount;
   }

   public int getFailedTestCount() {
      return this.m_failedTestCount;
   }

   public int getPassedTestCount() {
      return this.m_passedTestCount;
   }

   public int getSkippedTestCount() {
      return this.m_skippedTestCount;
   }

   public String toString() {
      return "[TestMessage suite:" + this.m_suiteName + " testName:" + this.m_testName + " passed:" + this.m_passedTestCount + " failed:" + this.m_failedTestCount + "]";
   }
}
