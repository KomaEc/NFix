package com.gzoltar.instrumentation.testing.jobs;

import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;

public class JobDefinition {
   private final String runner;
   private final String testClassName;
   private final String testMethodName;
   private final ExecutionParameters executionParameters;

   public JobDefinition(String var1, String var2, String var3, ExecutionParameters var4) {
      this.runner = var1;
      this.testClassName = var2;
      this.testMethodName = var3;
      this.executionParameters = var4;
   }

   public String getRunner() {
      return this.runner;
   }

   public String getTestClassName() {
      return this.testClassName;
   }

   public String getTestMethodName() {
      return this.testMethodName;
   }

   public ExecutionParameters getExecutionParameters() {
      return this.executionParameters;
   }
}
