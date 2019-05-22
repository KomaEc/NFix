package com.gzoltar.instrumentation.testing.testng;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestListener;
import com.gzoltar.instrumentation.testing.TestResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener extends TestListener implements ITestListener {
   private long start = 0L;
   private TestResult current = null;

   public void onStart(ITestContext var1) {
      Logger.getInstance().info("* Number of test cases to execute: " + var1.getAllTestMethods().length);
   }

   public void onFinish(ITestContext var1) {
      Logger.getInstance().info("* Number of test cases executed: " + var1.getAllTestMethods().length);
   }

   public void onTestStart(ITestResult var1) {
      Logger.getInstance().info("* Started " + getName(var1));
      this.start = System.nanoTime();
      this.current = new TestResult(getName(var1));
      super.testStarted(getName(var1));
   }

   public void onTestSuccess(ITestResult var1) {
      Logger.getInstance().info("* Finished " + getName(var1));
      this.current.setRuntime(System.nanoTime() - this.start);
      super.testFinished(this.current);
   }

   public void onTestFailedButWithinSuccessPercentage(ITestResult var1) {
      Logger.getInstance().info("* Finished " + getName(var1));
      this.current.setRuntime(System.nanoTime() - this.start);
      this.current.setSuccessful(false);
      this.current.setTrace(var1.getThrowable().getStackTrace().toString());
      super.testFailure();
      super.testFinished(this.current);
   }

   public void onTestFailure(ITestResult var1) {
      Logger.getInstance().info("* Failure: " + var1.getThrowable().getMessage());
      this.current.setRuntime(System.nanoTime() - this.start);
      this.current.setSuccessful(false);
      this.current.setTrace(var1.getThrowable().getStackTrace().toString());
      super.testFailure();
      super.testFinished(this.current);
   }

   public void onTestSkipped(ITestResult var1) {
      Logger.getInstance().info("* Ignored: " + getName(var1));
      super.testIgnored();
   }

   private static String getName(ITestResult var0) {
      return var0.getTestClass().getName() + "#" + var0.getMethod().getMethodName();
   }
}
