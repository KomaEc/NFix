package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;

public class LogEnablePhase extends AbstractPhase {
   public void execute(Object object, ComponentManager componentManager) throws PhaseExecutionException {
      if (object instanceof LogEnabled) {
         LoggerManager loggerManager = componentManager.getContainer().getLoggerManager();
         ComponentDescriptor descriptor = componentManager.getComponentDescriptor();
         Logger logger = loggerManager.getLoggerForComponent(descriptor.getRole(), descriptor.getRoleHint());
         ((LogEnabled)object).enableLogging(logger);
      }

   }
}
