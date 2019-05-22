package org.codehaus.plexus.component.manager;

import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.lifecycle.LifecycleHandlerManager;
import org.codehaus.plexus.lifecycle.UndefinedLifecycleHandlerException;

public interface ComponentManagerManager {
   String ROLE = (null.class$org$codehaus$plexus$component$manager$ComponentManagerManager == null ? (null.class$org$codehaus$plexus$component$manager$ComponentManagerManager = null.class$("org.codehaus.plexus.component.manager.ComponentManagerManager")) : null.class$org$codehaus$plexus$component$manager$ComponentManagerManager).getName();

   void setLifecycleHandlerManager(LifecycleHandlerManager var1);

   ComponentManager findComponentManagerByComponentKey(String var1);

   ComponentManager findComponentManagerByComponentInstance(Object var1);

   ComponentManager createComponentManager(ComponentDescriptor var1, PlexusContainer var2) throws UndefinedComponentManagerException, UndefinedLifecycleHandlerException;

   Map getComponentManagers();

   void associateComponentWithComponentManager(Object var1, ComponentManager var2);

   void unassociateComponentWithComponentManager(Object var1);
}
