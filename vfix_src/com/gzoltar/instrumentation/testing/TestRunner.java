package com.gzoltar.instrumentation.testing;

import com.gzoltar.instrumentation.testing.jobs.JobExecutor;
import com.gzoltar.instrumentation.testing.jobs.JobHandler;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import java.util.List;

public abstract class TestRunner {
   protected final ExecutionParameters executionData;

   protected TestRunner(ExecutionParameters var1) {
      this.executionData = var1;
   }

   public abstract List<JobHandler> createJobs(String var1);

   public static List<TestResult> run(List<JobHandler> var0, int var1) {
      return (new JobExecutor(var0, var1)).runJobs();
   }
}
