package org.codehaus.plexus.component.composition;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public interface ComponentComposerManager {
   String ROLE = (null.class$org$codehaus$plexus$component$composition$ComponentComposerManager == null ? (null.class$org$codehaus$plexus$component$composition$ComponentComposerManager = null.class$("org.codehaus.plexus.component.composition.ComponentComposerManager")) : null.class$org$codehaus$plexus$component$composition$ComponentComposerManager).getName();

   void assembleComponent(Object var1, ComponentDescriptor var2, PlexusContainer var3) throws CompositionException, UndefinedComponentComposerException;
}
