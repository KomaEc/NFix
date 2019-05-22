package org.codehaus.plexus.component.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.lifecycle.LifecycleHandler;
import org.codehaus.plexus.lifecycle.LifecycleHandlerManager;
import org.codehaus.plexus.lifecycle.UndefinedLifecycleHandlerException;

public class DefaultComponentManagerManager implements ComponentManagerManager {
   private Map activeComponentManagers = new HashMap();
   private List componentManagers = null;
   private String defaultComponentManagerId = null;
   private LifecycleHandlerManager lifecycleHandlerManager;
   private Map componentManagersByComponentHashCode = Collections.synchronizedMap(new HashMap());

   public void setLifecycleHandlerManager(LifecycleHandlerManager lifecycleHandlerManager) {
      this.lifecycleHandlerManager = lifecycleHandlerManager;
   }

   private ComponentManager copyComponentManager(String id) throws UndefinedComponentManagerException {
      ComponentManager componentManager = null;
      Iterator iterator = this.componentManagers.iterator();

      do {
         if (!iterator.hasNext()) {
            throw new UndefinedComponentManagerException("Specified component manager cannot be found: " + id);
         }

         componentManager = (ComponentManager)iterator.next();
      } while(!id.equals(componentManager.getId()));

      return componentManager.copy();
   }

   public ComponentManager createComponentManager(ComponentDescriptor descriptor, PlexusContainer container) throws UndefinedComponentManagerException, UndefinedLifecycleHandlerException {
      String componentManagerId = descriptor.getInstantiationStrategy();
      if (componentManagerId == null) {
         componentManagerId = this.defaultComponentManagerId;
      }

      ComponentManager componentManager = this.copyComponentManager(componentManagerId);
      componentManager.setup(container, this.findLifecycleHandler(descriptor), descriptor);
      componentManager.initialize();
      this.activeComponentManagers.put(descriptor.getComponentKey(), componentManager);
      return componentManager;
   }

   public ComponentManager findComponentManagerByComponentInstance(Object component) {
      return (ComponentManager)this.componentManagersByComponentHashCode.get(new Integer(component.hashCode()));
   }

   public ComponentManager findComponentManagerByComponentKey(String componentKey) {
      ComponentManager componentManager = (ComponentManager)this.activeComponentManagers.get(componentKey);
      return componentManager;
   }

   private LifecycleHandler findLifecycleHandler(ComponentDescriptor descriptor) throws UndefinedLifecycleHandlerException {
      String lifecycleHandlerId = descriptor.getLifecycleHandler();
      LifecycleHandler lifecycleHandler;
      if (lifecycleHandlerId == null) {
         lifecycleHandler = this.lifecycleHandlerManager.getDefaultLifecycleHandler();
      } else {
         lifecycleHandler = this.lifecycleHandlerManager.getLifecycleHandler(lifecycleHandlerId);
      }

      return lifecycleHandler;
   }

   public Map getComponentManagers() {
      return this.activeComponentManagers;
   }

   public void associateComponentWithComponentManager(Object component, ComponentManager componentManager) {
      this.componentManagersByComponentHashCode.put(new Integer(component.hashCode()), componentManager);
   }

   public void unassociateComponentWithComponentManager(Object component) {
      this.componentManagersByComponentHashCode.remove(new Integer(component.hashCode()));
   }
}
