package com.gzoltar.instrumentation.testing;

import com.gzoltar.instrumentation.runtime.JaCoCoWrapper;
import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;
import org.junit.runner.notification.RunListener;

public abstract class TestListener extends RunListener {
   private TestResult testResult = null;
   private IAgent agent = null;

   protected TestListener() {
   }

   public void testRunStarted(String var1) {
      this.agent = RT.getAgent();
      this.agent.setSessionId(var1);
   }

   public void testRunFinished(TestResult var1) {
      var1.setCoveredComponents(JaCoCoWrapper.registerCoverage((byte[])this.agent.getExecutionData(true).clone(), false).keySet());
      this.testResult = var1;
   }

   public void testStarted(String var1) {
   }

   public void testFinished(TestResult var1) {
   }

   public void testFailure() {
   }

   public void testIgnored() {
   }

   public TestResult getTestResult() {
      return this.testResult;
   }
}
