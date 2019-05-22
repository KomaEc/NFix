package org.codehaus.plexus.component.composition;

import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public interface ComponentComposer {
   String ROLE = (null.class$org$codehaus$plexus$component$composition$ComponentComposer == null ? (null.class$org$codehaus$plexus$component$composition$ComponentComposer = null.class$("org.codehaus.plexus.component.composition.ComponentComposer")) : null.class$org$codehaus$plexus$component$composition$ComponentComposer).getName();

   String getId();

   List assembleComponent(Object var1, ComponentDescriptor var2, PlexusContainer var3) throws CompositionException, UndefinedComponentComposerException;
}
