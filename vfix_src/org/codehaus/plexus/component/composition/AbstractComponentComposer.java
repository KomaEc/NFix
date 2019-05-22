package org.codehaus.plexus.component.composition;

import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public abstract class AbstractComponentComposer extends AbstractLogEnabled implements ComponentComposer {
   private String id;

   public String getId() {
      return this.id;
   }

   // $FF: synthetic method
   public abstract List assembleComponent(Object var1, ComponentDescriptor var2, PlexusContainer var3) throws CompositionException, UndefinedComponentComposerException;
}
