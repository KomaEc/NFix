package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import java.util.List;
import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public class PlexusContainerLocator implements ServiceLocator {
   private PlexusContainer container;

   public PlexusContainerLocator(PlexusContainer container) {
      this.container = container;
   }

   public Object lookup(String componentKey) throws ComponentLookupException {
      return this.container.lookup(componentKey);
   }

   public Object lookup(String role, String roleHint) throws ComponentLookupException {
      return this.container.lookup(role, roleHint);
   }

   public Map lookupMap(String role) throws ComponentLookupException {
      return this.container.lookupMap(role);
   }

   public List lookupList(String role) throws ComponentLookupException {
      return this.container.lookupList(role);
   }

   public void release(Object component) throws ComponentLifecycleException {
      this.container.release(component);
   }

   public void releaseAll(Map components) throws ComponentLifecycleException {
      this.container.releaseAll(components);
   }

   public void releaseAll(List components) throws ComponentLifecycleException {
      this.container.releaseAll(components);
   }

   public boolean hasComponent(String componentKey) {
      return this.container.hasComponent(componentKey);
   }

   public boolean hasComponent(String role, String roleHint) {
      return this.container.hasComponent(role, roleHint);
   }
}
