package org.codehaus.plexus.component.manager;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.lifecycle.LifecycleHandler;

public interface ComponentManager {
   String ROLE = (null.class$org$codehaus$plexus$component$manager$ComponentManager == null ? (null.class$org$codehaus$plexus$component$manager$ComponentManager = null.class$("org.codehaus.plexus.component.manager.ComponentManager")) : null.class$org$codehaus$plexus$component$manager$ComponentManager).getName();

   ComponentManager copy();

   String getId();

   void setup(PlexusContainer var1, LifecycleHandler var2, ComponentDescriptor var3);

   void initialize();

   int getConnections();

   LifecycleHandler getLifecycleHandler();

   void dispose() throws ComponentLifecycleException;

   void release(Object var1) throws ComponentLifecycleException;

   void suspend(Object var1) throws ComponentLifecycleException;

   void resume(Object var1) throws ComponentLifecycleException;

   Object getComponent() throws ComponentInstantiationException, ComponentLifecycleException;

   ComponentDescriptor getComponentDescriptor();

   PlexusContainer getContainer();
}
