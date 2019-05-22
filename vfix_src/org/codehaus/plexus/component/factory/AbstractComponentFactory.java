package org.codehaus.plexus.component.factory;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public abstract class AbstractComponentFactory implements ComponentFactory {
   protected String id;

   public String getId() {
      return this.id;
   }

   // $FF: synthetic method
   public abstract Object newInstance(ComponentDescriptor var1, ClassRealm var2, PlexusContainer var3) throws ComponentInstantiationException;
}
