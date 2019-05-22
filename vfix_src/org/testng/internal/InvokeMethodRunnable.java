package org.testng.internal;

import java.lang.reflect.Method;
import org.testng.IHookable;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class InvokeMethodRunnable implements Runnable {
   private ITestNGMethod m_method = null;
   private Object m_instance = null;
   private Object[] m_parameters = null;
   private final IHookable m_hookable;
   private final ITestResult m_testResult;

   public InvokeMethodRunnable(ITestNGMethod thisMethod, Object instance, Object[] parameters, IHookable hookable, ITestResult testResult) {
      this.m_method = thisMethod;
      this.m_instance = instance;
      this.m_parameters = parameters;
      this.m_hookable = hookable;
      this.m_testResult = testResult;
   }

   public void run() throws InvokeMethodRunnable.TestNGRuntimeException {
      if (this.m_method.getInvocationTimeOut() > 0L) {
         for(int i = 0; i < this.m_method.getInvocationCount(); ++i) {
            this.runOne();
         }
      } else {
         this.runOne();
      }

   }

   private void runOne() {
      try {
         InvokeMethodRunnable.TestNGRuntimeException t = null;

         try {
            Method m = this.m_method.getMethod();
            if (this.m_hookable == null) {
               MethodInvocationHelper.invokeMethod(m, this.m_instance, this.m_parameters);
            } else {
               MethodInvocationHelper.invokeHookable(this.m_instance, this.m_parameters, this.m_hookable, m, this.m_testResult);
            }
         } catch (Throwable var6) {
            t = new InvokeMethodRunnable.TestNGRuntimeException(var6.getCause());
         }

         if (null != t) {
            Thread.currentThread().interrupt();
            throw t;
         }
      } finally {
         this.m_method.incrementCurrentInvocationCount();
      }

   }

   public static class TestNGRuntimeException extends RuntimeException {
      private static final long serialVersionUID = -8619899270785596231L;

      public TestNGRuntimeException(Throwable rootCause) {
         super(rootCause);
      }
   }
}
