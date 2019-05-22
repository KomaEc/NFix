package org.apache.maven.lifecycle;

import org.apache.maven.BuildFailureException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.monitor.event.EventDispatcher;

public interface LifecycleExecutor {
   String ROLE = LifecycleExecutor.class.getName();

   void execute(MavenSession var1, ReactorManager var2, EventDispatcher var3) throws LifecycleExecutionException, BuildFailureException;
}
