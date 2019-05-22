package org.codehaus.plexus.component.manager;

import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;

public class PerLookupComponentManager extends AbstractComponentManager {
   public void dispose() {
   }

   public Object getComponent() throws ComponentInstantiationException, ComponentLifecycleException {
      Object component = this.createComponentInstance();
      return component;
   }

   public void release(Object component) throws ComponentLifecycleException {
      this.endComponentLifecycle(component);
   }
}
