package org.codehaus.plexus.component.discovery;

import java.util.List;

public interface ComponentDiscovererManager {
   List getComponentDiscoverers();

   void registerComponentDiscoveryListener(ComponentDiscoveryListener var1);

   void removeComponentDiscoveryListener(ComponentDiscoveryListener var1);

   void fireComponentDiscoveryEvent(ComponentDiscoveryEvent var1);

   void initialize();

   List getListenerDescriptors();
}
