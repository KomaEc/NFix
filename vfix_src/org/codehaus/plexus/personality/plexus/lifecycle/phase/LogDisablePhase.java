package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.LoggerManager;

public class LogDisablePhase extends AbstractPhase {
   public void execute(Object object, ComponentManager componentManager) throws PhaseExecutionException {
      if (object instanceof LogEnabled) {
         LoggerManager loggerManager;
         try {
            loggerManager = (LoggerManager)componentManager.getContainer().lookup(LoggerManager.ROLE);
         } catch (ComponentLookupException var6) {
            throw new PhaseExecutionException("Unable to locate logger manager", var6);
         }

         ComponentDescriptor descriptor = componentManager.getComponentDescriptor();
         loggerManager.returnComponentLogger(descriptor.getRole(), descriptor.getRoleHint());
      }

   }
}
