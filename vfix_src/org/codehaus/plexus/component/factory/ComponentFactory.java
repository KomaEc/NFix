package org.codehaus.plexus.component.factory;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public interface ComponentFactory {
   String ROLE = (null.class$org$codehaus$plexus$component$factory$ComponentFactory == null ? (null.class$org$codehaus$plexus$component$factory$ComponentFactory = null.class$("org.codehaus.plexus.component.factory.ComponentFactory")) : null.class$org$codehaus$plexus$component$factory$ComponentFactory).getName();

   String getId();

   Object newInstance(ComponentDescriptor var1, ClassRealm var2, PlexusContainer var3) throws ComponentInstantiationException;
}
