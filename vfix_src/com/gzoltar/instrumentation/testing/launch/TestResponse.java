package com.gzoltar.instrumentation.testing.launch;

import com.gzoltar.instrumentation.testing.TestResult;
import java.io.Serializable;

public class TestResponse implements Serializable {
   private static final long serialVersionUID = -7090271272807736171L;
   private TestResult testResult = null;

   public TestResult getTestResult() {
      return this.testResult;
   }

   public void setTestResult(TestResult var1) {
      this.testResult = var1;
   }
}
