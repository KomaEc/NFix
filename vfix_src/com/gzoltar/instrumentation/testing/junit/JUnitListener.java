package com.gzoltar.instrumentation.testing.junit;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestListener;
import com.gzoltar.instrumentation.testing.TestResult;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JUnitListener extends TestListener {
   private long start = 0L;
   private TestResult current = null;

   public void testRunStarted(Description var1) {
      Logger.getInstance().info("* Number of test cases to execute: " + var1.testCount());
      super.testRunStarted(var1.getClassName());
   }

   public void testRunFinished(Result var1) {
      Logger.getInstance().info("* Number of test cases executed: " + var1.getRunCount());
      super.testRunFinished(this.current);
   }

   public void testStarted(Description var1) {
      Logger.getInstance().info("* Started " + getName(var1));
      this.start = System.nanoTime();
      this.current = new TestResult(getName(var1));
      super.testStarted(getName(var1));
   }

   public void testFinished(Description var1) {
      Logger.getInstance().info("* Finished " + getName(var1));
      this.current.setRuntime(System.nanoTime() - this.start);
      super.testFinished(this.current);
   }

   public void testFailure(Failure var1) {
      Logger.getInstance().info("* Failure: " + var1.getMessage() + " | " + var1.getDescription());
      Logger.getInstance().info("--- " + var1.getDescription().getClassName() + "::" + var1.getDescription().getMethodName());
      Logger.getInstance().info(var1.getTrace());
      this.current.setSuccessful(false);
      this.current.setTrace(var1.getTrace());
      super.testFailure();
   }

   public void testIgnored(Description var1) throws Exception {
      Logger.getInstance().info("* Ignored: " + getName(var1));
      super.testIgnored();
   }

   private static String getName(Description var0) {
      return var0.getClassName() + "#" + var0.getMethodName();
   }
}
