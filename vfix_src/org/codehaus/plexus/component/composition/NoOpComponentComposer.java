package org.codehaus.plexus.component.composition;

import java.util.Collections;
import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public class NoOpComponentComposer extends AbstractComponentComposer {
   public String getId() {
      return null;
   }

   public List assembleComponent(Object component, ComponentDescriptor componentDescriptor, PlexusContainer container) {
      return Collections.EMPTY_LIST;
   }
}
