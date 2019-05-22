package org.testng.internal.annotations;

import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

public class TestAnnotation extends TestOrConfiguration implements ITestAnnotation {
   private long m_invocationTimeOut = 0L;
   private int m_invocationCount = 1;
   private int m_threadPoolSize = 0;
   private int m_successPercentage = 100;
   private String m_dataProvider = "";
   private boolean m_alwaysRun = false;
   private Class<?>[] m_expectedExceptions = new Class[0];
   private String m_expectedExceptionsMessageRegExp = ".*";
   private String m_suiteName = "";
   private String m_testName = "";
   private boolean m_singleThreaded = false;
   private boolean m_sequential = false;
   private Class<?> m_dataProviderClass = null;
   private IRetryAnalyzer m_retryAnalyzer = null;
   private boolean m_skipFailedInvocations = false;
   private boolean m_ignoreMissingDependencies = false;

   public Class<?>[] getExpectedExceptions() {
      return this.m_expectedExceptions;
   }

   public void setExpectedExceptions(Class<?>[] expectedExceptions) {
      this.m_expectedExceptions = expectedExceptions;
   }

   public String getExpectedExceptionsMessageRegExp() {
      return this.m_expectedExceptionsMessageRegExp;
   }

   public void setExpectedExceptionsMessageRegExp(String expectedExceptionsMessageRegExp) {
      this.m_expectedExceptionsMessageRegExp = expectedExceptionsMessageRegExp;
   }

   public void setAlwaysRun(boolean alwaysRun) {
      this.m_alwaysRun = alwaysRun;
   }

   public void setDataProvider(String dataProvider) {
      this.m_dataProvider = dataProvider;
   }

   public Class<?> getDataProviderClass() {
      return this.m_dataProviderClass;
   }

   public void setDataProviderClass(Class<?> dataProviderClass) {
      this.m_dataProviderClass = dataProviderClass;
   }

   public void setInvocationCount(int invocationCount) {
      this.m_invocationCount = invocationCount;
   }

   public void setSuccessPercentage(int successPercentage) {
      this.m_successPercentage = successPercentage;
   }

   public int getInvocationCount() {
      return this.m_invocationCount;
   }

   public long invocationTimeOut() {
      return this.m_invocationTimeOut;
   }

   public void setInvocationTimeOut(long timeOut) {
      this.m_invocationTimeOut = timeOut;
   }

   public int getSuccessPercentage() {
      return this.m_successPercentage;
   }

   public String getDataProvider() {
      return this.m_dataProvider;
   }

   public boolean getAlwaysRun() {
      return this.m_alwaysRun;
   }

   public int getThreadPoolSize() {
      return this.m_threadPoolSize;
   }

   public void setThreadPoolSize(int threadPoolSize) {
      this.m_threadPoolSize = threadPoolSize;
   }

   public String getSuiteName() {
      return this.m_suiteName;
   }

   public void setSuiteName(String xmlSuite) {
      this.m_suiteName = xmlSuite;
   }

   public String getTestName() {
      return this.m_testName;
   }

   public void setTestName(String xmlTest) {
      this.m_testName = xmlTest;
   }

   public boolean getSingleThreaded() {
      return this.m_singleThreaded;
   }

   public void setSingleThreaded(boolean singleThreaded) {
      this.m_singleThreaded = singleThreaded;
   }

   public boolean getSequential() {
      return this.m_sequential;
   }

   public void setSequential(boolean sequential) {
      this.m_sequential = sequential;
   }

   public IRetryAnalyzer getRetryAnalyzer() {
      return this.m_retryAnalyzer;
   }

   public void setRetryAnalyzer(Class<?> c) {
      this.m_retryAnalyzer = null;
      if (c != null && IRetryAnalyzer.class.isAssignableFrom(c)) {
         try {
            this.m_retryAnalyzer = (IRetryAnalyzer)c.newInstance();
         } catch (IllegalAccessException | InstantiationException var3) {
         }
      }

   }

   public void setSkipFailedInvocations(boolean skip) {
      this.m_skipFailedInvocations = skip;
   }

   public boolean skipFailedInvocations() {
      return this.m_skipFailedInvocations;
   }

   public void setIgnoreMissingDependencies(boolean ignore) {
      this.m_ignoreMissingDependencies = ignore;
   }

   public boolean ignoreMissingDependencies() {
      return this.m_ignoreMissingDependencies;
   }
}
