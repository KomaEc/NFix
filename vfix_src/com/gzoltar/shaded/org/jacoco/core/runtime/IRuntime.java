package com.gzoltar.shaded.org.jacoco.core.runtime;

public interface IRuntime extends IExecutionDataAccessorGenerator {
   void startup(RuntimeData var1) throws Exception;

   void shutdown();
}
