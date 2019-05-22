package org.codehaus.plexus.configuration;

public interface PlexusConfiguration {
   String getName();

   String getValue() throws PlexusConfigurationException;

   String getValue(String var1);

   String[] getAttributeNames();

   String getAttribute(String var1) throws PlexusConfigurationException;

   String getAttribute(String var1, String var2);

   PlexusConfiguration getChild(String var1);

   PlexusConfiguration getChild(int var1);

   PlexusConfiguration getChild(String var1, boolean var2);

   PlexusConfiguration[] getChildren();

   PlexusConfiguration[] getChildren(String var1);

   void addChild(PlexusConfiguration var1);

   int getChildCount();
}
