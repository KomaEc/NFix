package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.composition.CompositionException;
import org.codehaus.plexus.component.composition.UndefinedComponentComposerException;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.lifecycle.phase.AbstractPhase;

public class CompositionPhase extends AbstractPhase {
   public void execute(Object object, ComponentManager manager) throws PhaseExecutionException {
      PlexusContainer container = manager.getContainer();
      ComponentDescriptor descriptor = manager.getComponentDescriptor();

      try {
         container.composeComponent(object, descriptor);
      } catch (CompositionException var6) {
         throw new PhaseExecutionException("Error composing component", var6);
      } catch (UndefinedComponentComposerException var7) {
         throw new PhaseExecutionException("Error composing component", var7);
      }
   }
}
