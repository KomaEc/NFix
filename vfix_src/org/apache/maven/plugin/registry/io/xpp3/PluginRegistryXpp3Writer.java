package org.apache.maven.plugin.registry.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.plugin.registry.Plugin;
import org.apache.maven.plugin.registry.PluginRegistry;
import org.apache.maven.plugin.registry.TrackableBase;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class PluginRegistryXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, PluginRegistry pluginRegistry) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(pluginRegistry.getModelEncoding(), (Boolean)null);
      this.writePluginRegistry(pluginRegistry, "pluginRegistry", serializer);
      serializer.endDocument();
   }

   private void writePlugin(Plugin plugin, String tagName, XmlSerializer serializer) throws IOException {
      if (plugin != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (plugin.getGroupId() != null) {
            serializer.startTag(NAMESPACE, "groupId").text(plugin.getGroupId()).endTag(NAMESPACE, "groupId");
         }

         if (plugin.getArtifactId() != null) {
            serializer.startTag(NAMESPACE, "artifactId").text(plugin.getArtifactId()).endTag(NAMESPACE, "artifactId");
         }

         if (plugin.getLastChecked() != null) {
            serializer.startTag(NAMESPACE, "lastChecked").text(plugin.getLastChecked()).endTag(NAMESPACE, "lastChecked");
         }

         if (plugin.getUseVersion() != null) {
            serializer.startTag(NAMESPACE, "useVersion").text(plugin.getUseVersion()).endTag(NAMESPACE, "useVersion");
         }

         if (plugin.getRejectedVersions() != null && plugin.getRejectedVersions().size() > 0) {
            serializer.startTag(NAMESPACE, "rejectedVersions");
            Iterator iter = plugin.getRejectedVersions().iterator();

            while(iter.hasNext()) {
               String rejectedVersion = (String)iter.next();
               serializer.startTag(NAMESPACE, "rejectedVersion").text(rejectedVersion).endTag(NAMESPACE, "rejectedVersion");
            }

            serializer.endTag(NAMESPACE, "rejectedVersions");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writePluginRegistry(PluginRegistry pluginRegistry, String tagName, XmlSerializer serializer) throws IOException {
      if (pluginRegistry != null) {
         serializer.setPrefix("", "http://maven.apache.org/PLUGIN_REGISTRY/1.0.0");
         serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
         serializer.startTag(NAMESPACE, tagName);
         serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/PLUGIN_REGISTRY/1.0.0 http://maven.apache.org/xsd/plugin-registry-1.0.0.xsd");
         if (pluginRegistry.getUpdateInterval() != null && !pluginRegistry.getUpdateInterval().equals("never")) {
            serializer.startTag(NAMESPACE, "updateInterval").text(pluginRegistry.getUpdateInterval()).endTag(NAMESPACE, "updateInterval");
         }

         if (pluginRegistry.getAutoUpdate() != null) {
            serializer.startTag(NAMESPACE, "autoUpdate").text(pluginRegistry.getAutoUpdate()).endTag(NAMESPACE, "autoUpdate");
         }

         if (pluginRegistry.getCheckLatest() != null) {
            serializer.startTag(NAMESPACE, "checkLatest").text(pluginRegistry.getCheckLatest()).endTag(NAMESPACE, "checkLatest");
         }

         if (pluginRegistry.getPlugins() != null && pluginRegistry.getPlugins().size() > 0) {
            serializer.startTag(NAMESPACE, "plugins");
            Iterator iter = pluginRegistry.getPlugins().iterator();

            while(iter.hasNext()) {
               Plugin o = (Plugin)iter.next();
               this.writePlugin(o, "plugin", serializer);
            }

            serializer.endTag(NAMESPACE, "plugins");
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeTrackableBase(TrackableBase trackableBase, String tagName, XmlSerializer serializer) throws IOException {
      if (trackableBase != null) {
         serializer.startTag(NAMESPACE, tagName);
         serializer.endTag(NAMESPACE, tagName);
      }

   }
}
