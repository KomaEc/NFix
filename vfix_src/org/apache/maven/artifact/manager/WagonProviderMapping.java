package org.apache.maven.artifact.manager;

public interface WagonProviderMapping {
   String ROLE = WagonProviderMapping.class.getName();

   void setWagonProvider(String var1, String var2);

   String getWagonProvider(String var1);
}
