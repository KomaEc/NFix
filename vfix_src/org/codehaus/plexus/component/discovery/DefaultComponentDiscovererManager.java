package org.codehaus.plexus.component.discovery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultComponentDiscovererManager implements ComponentDiscovererManager {
   private List componentDiscoverers;
   private List componentDiscoveryListeners;
   private List listeners;

   public List getComponentDiscoverers() {
      return this.componentDiscoverers;
   }

   public void registerComponentDiscoveryListener(ComponentDiscoveryListener listener) {
      if (this.componentDiscoveryListeners == null) {
         this.componentDiscoveryListeners = new ArrayList();
      }

      this.componentDiscoveryListeners.add(listener);
   }

   public void removeComponentDiscoveryListener(ComponentDiscoveryListener listener) {
      if (this.componentDiscoveryListeners != null) {
         this.componentDiscoveryListeners.remove(listener);
      }

   }

   public void fireComponentDiscoveryEvent(ComponentDiscoveryEvent event) {
      if (this.componentDiscoveryListeners != null) {
         Iterator i = this.componentDiscoveryListeners.iterator();

         while(i.hasNext()) {
            ComponentDiscoveryListener listener = (ComponentDiscoveryListener)i.next();
            listener.componentDiscovered(event);
         }
      }

   }

   public List getListenerDescriptors() {
      return this.listeners;
   }

   public void initialize() {
      Iterator i = this.componentDiscoverers.iterator();

      while(i.hasNext()) {
         ComponentDiscoverer componentDiscoverer = (ComponentDiscoverer)i.next();
         componentDiscoverer.setManager(this);
      }

   }
}
