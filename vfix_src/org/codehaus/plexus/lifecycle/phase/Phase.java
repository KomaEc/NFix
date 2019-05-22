package org.codehaus.plexus.lifecycle.phase;

import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.PhaseExecutionException;

public interface Phase {
   void execute(Object var1, ComponentManager var2) throws PhaseExecutionException;
}
