package org.codehaus.plexus.component.manager;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.lifecycle.LifecycleHandler;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.PhaseExecutionException;

public abstract class AbstractComponentManager implements ComponentManager, Cloneable {
   private PlexusContainer container;
   private ComponentDescriptor componentDescriptor;
   private LifecycleHandler lifecycleHandler;
   private int connections;
   private String id = null;

   public ComponentManager copy() {
      try {
         ComponentManager componentManager = (ComponentManager)this.clone();
         return componentManager;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public ComponentDescriptor getComponentDescriptor() {
      return this.componentDescriptor;
   }

   public String getId() {
      return this.id;
   }

   public LifecycleHandler getLifecycleHandler() {
      return this.lifecycleHandler;
   }

   protected void incrementConnectionCount() {
      ++this.connections;
   }

   protected void decrementConnectionCount() {
      --this.connections;
   }

   protected boolean connected() {
      return this.connections > 0;
   }

   public int getConnections() {
      return this.connections;
   }

   public void setup(PlexusContainer container, LifecycleHandler lifecycleHandler, ComponentDescriptor componentDescriptor) {
      this.container = container;
      this.lifecycleHandler = lifecycleHandler;
      this.componentDescriptor = componentDescriptor;
   }

   public void initialize() {
   }

   protected Object createComponentInstance() throws ComponentInstantiationException, ComponentLifecycleException {
      Object component = this.container.createComponentInstance(this.componentDescriptor);
      this.startComponentLifecycle(component);
      return component;
   }

   protected void startComponentLifecycle(Object component) throws ComponentLifecycleException {
      try {
         this.getLifecycleHandler().start(component, this);
      } catch (PhaseExecutionException var3) {
         throw new ComponentLifecycleException("Error starting component", var3);
      }
   }

   public void suspend(Object component) throws ComponentLifecycleException {
      try {
         this.getLifecycleHandler().suspend(component, this);
      } catch (PhaseExecutionException var3) {
         throw new ComponentLifecycleException("Error suspending component", var3);
      }
   }

   public void resume(Object component) throws ComponentLifecycleException {
      try {
         this.getLifecycleHandler().resume(component, this);
      } catch (PhaseExecutionException var3) {
         throw new ComponentLifecycleException("Error suspending component", var3);
      }
   }

   protected void endComponentLifecycle(Object component) throws ComponentLifecycleException {
      try {
         this.getLifecycleHandler().end(component, this);
      } catch (PhaseExecutionException var3) {
         throw new ComponentLifecycleException("Error ending component lifecycle", var3);
      }
   }

   public PlexusContainer getContainer() {
      return this.container;
   }

   public Logger getLogger() {
      return this.container.getLogger();
   }

   // $FF: synthetic method
   public abstract Object getComponent() throws ComponentInstantiationException, ComponentLifecycleException;

   // $FF: synthetic method
   public abstract void release(Object var1) throws ComponentLifecycleException;

   // $FF: synthetic method
   public abstract void dispose() throws ComponentLifecycleException;
}
