package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;
import org.codehaus.plexus.util.StringUtils;

public class AutoConfigurePhase extends AbstractPhase {
   public static final String DEFAULT_CONFIGURATOR_ID = "basic";

   public void execute(Object object, ComponentManager manager) throws PhaseExecutionException {
      try {
         ComponentDescriptor descriptor = manager.getComponentDescriptor();
         String configuratorId = descriptor.getComponentConfigurator();
         if (StringUtils.isEmpty(configuratorId)) {
            configuratorId = "basic";
         }

         ComponentConfigurator componentConfigurator = (ComponentConfigurator)manager.getContainer().lookup(ComponentConfigurator.ROLE, configuratorId);
         if (manager.getComponentDescriptor().hasConfiguration()) {
            componentConfigurator.configureComponent(object, manager.getComponentDescriptor().getConfiguration(), manager.getContainer().getContainerRealm());
         }

      } catch (ComponentLookupException var6) {
         throw new PhaseExecutionException("Unable to auto-configure component as its configurator could not be found", var6);
      } catch (ComponentConfigurationException var7) {
         throw new PhaseExecutionException("Unable to auto-configure component", var7);
      }
   }
}
