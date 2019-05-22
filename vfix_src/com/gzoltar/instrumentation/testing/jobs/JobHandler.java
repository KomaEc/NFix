package com.gzoltar.instrumentation.testing.jobs;

import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.testing.launch.LaunchTest;
import com.gzoltar.instrumentation.testing.launch.TestResponse;
import java.util.concurrent.Callable;

public class JobHandler implements Callable<TestResult> {
   private final JobDefinition job;

   public JobHandler(JobDefinition var1) {
      this.job = var1;
   }

   public JobDefinition getJobDefinition() {
      return this.job;
   }

   public TestResult call() throws Exception {
      TestResponse var1;
      if (this.job.getTestMethodName() == null) {
         var1 = LaunchTest.launch(this.job.getRunner(), this.job.getTestClassName(), (String)null, this.job.getExecutionParameters());
      } else {
         var1 = LaunchTest.launch(this.job.getRunner(), this.job.getTestClassName(), this.job.getTestMethodName(), this.job.getExecutionParameters());
      }

      return var1 != null ? var1.getTestResult() : null;
   }
}
