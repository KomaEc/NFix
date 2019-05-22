package org.codehaus.plexus.lifecycle;

import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.PhaseExecutionException;

public interface LifecycleHandler {
   String getId();

   void start(Object var1, ComponentManager var2) throws PhaseExecutionException;

   void suspend(Object var1, ComponentManager var2) throws PhaseExecutionException;

   void resume(Object var1, ComponentManager var2) throws PhaseExecutionException;

   void end(Object var1, ComponentManager var2) throws PhaseExecutionException;

   void initialize();
}
