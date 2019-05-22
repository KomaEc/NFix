package org.codehaus.plexus.component.repository;

import org.codehaus.plexus.component.composition.ComponentComposer;
import org.codehaus.plexus.component.factory.ComponentFactory;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.lifecycle.LifecycleHandler;

public class ComponentProfile {
   private ComponentFactory componentFactory;
   private LifecycleHandler lifecycleHandler;
   private ComponentManager componentManager;
   private ComponentComposer componentComposer;

   public ComponentFactory getComponentFactory() {
      return this.componentFactory;
   }

   public void setComponentFactory(ComponentFactory componentFactory) {
      this.componentFactory = componentFactory;
   }

   public LifecycleHandler getLifecycleHandler() {
      return this.lifecycleHandler;
   }

   public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
      this.lifecycleHandler = lifecycleHandler;
   }

   public ComponentManager getComponentManager() {
      return this.componentManager;
   }

   public void setComponentManager(ComponentManager componentManager) {
      this.componentManager = componentManager;
   }

   public ComponentComposer getComponentComposer() {
      return this.componentComposer;
   }

   public void setComponentComposer(ComponentComposer componentComposer) {
      this.componentComposer = componentComposer;
   }
}
