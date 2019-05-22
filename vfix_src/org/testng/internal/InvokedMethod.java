package org.testng.internal;

import java.io.Serializable;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class InvokedMethod implements Serializable, IInvokedMethod {
   private static final long serialVersionUID = 2126127194102819222L;
   private transient Object m_instance;
   private ITestNGMethod m_testMethod;
   private Object[] m_parameters;
   private long m_date = System.currentTimeMillis();
   private ITestResult m_testResult;

   public InvokedMethod(Object instance, ITestNGMethod method, Object[] parameters, long date, ITestResult testResult) {
      this.m_instance = instance;
      this.m_testMethod = method;
      this.m_parameters = parameters;
      this.m_date = date;
      this.m_testResult = testResult;
   }

   public boolean isTestMethod() {
      return this.m_testMethod.isTest();
   }

   public String toString() {
      StringBuffer result = new StringBuffer(this.m_testMethod.toString());
      Object[] arr$ = this.m_parameters;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object p = arr$[i$];
         result.append(p).append(" ");
      }

      result.append(" ").append(this.m_instance != null ? this.m_instance.hashCode() : " <static>");
      return result.toString();
   }

   public boolean isConfigurationMethod() {
      return this.m_testMethod.isBeforeMethodConfiguration() || this.m_testMethod.isAfterMethodConfiguration() || this.m_testMethod.isBeforeTestConfiguration() || this.m_testMethod.isAfterTestConfiguration() || this.m_testMethod.isBeforeClassConfiguration() || this.m_testMethod.isAfterClassConfiguration() || this.m_testMethod.isBeforeSuiteConfiguration() || this.m_testMethod.isAfterSuiteConfiguration();
   }

   public ITestNGMethod getTestMethod() {
      return this.m_testMethod;
   }

   public long getDate() {
      return this.m_date;
   }

   public ITestResult getTestResult() {
      return this.m_testResult;
   }
}
