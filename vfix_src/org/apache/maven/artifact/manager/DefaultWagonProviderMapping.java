package org.apache.maven.artifact.manager;

import java.util.HashMap;
import java.util.Map;

public class DefaultWagonProviderMapping implements WagonProviderMapping {
   private Map<String, String> wagonProviders = new HashMap();

   public String getWagonProvider(String protocol) {
      return (String)this.wagonProviders.get(protocol);
   }

   public void setWagonProvider(String protocol, String provider) {
      this.wagonProviders.put(protocol, provider);
   }

   public void setWagonProviders(Map<String, String> wagonProviders) {
      this.wagonProviders = wagonProviders;
   }
}
