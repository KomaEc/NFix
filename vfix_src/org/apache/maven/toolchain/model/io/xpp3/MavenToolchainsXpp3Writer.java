package org.apache.maven.toolchain.model.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.toolchain.model.PersistedToolchains;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class MavenToolchainsXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, PersistedToolchains persistedToolchains) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(persistedToolchains.getModelEncoding(), (Boolean)null);
      this.writePersistedToolchains(persistedToolchains, "toolchains", serializer);
      serializer.endDocument();
   }

   private void writePersistedToolchains(PersistedToolchains persistedToolchains, String tagName, XmlSerializer serializer) throws IOException {
      if (persistedToolchains != null) {
         serializer.setPrefix("", "http://maven.apache.org/TOOLCHAINS/1.0.0");
         serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
         serializer.startTag(NAMESPACE, tagName);
         serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/TOOLCHAINS/1.0.0 http://maven.apache.org/xsd/toolchains-1.0.0.xsd");
         if (persistedToolchains.getToolchains() != null && persistedToolchains.getToolchains().size() > 0) {
            Iterator iter = persistedToolchains.getToolchains().iterator();

            while(iter.hasNext()) {
               ToolchainModel o = (ToolchainModel)iter.next();
               this.writeToolchainModel(o, "toolchain", serializer);
            }
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }

   private void writeToolchainModel(ToolchainModel toolchainModel, String tagName, XmlSerializer serializer) throws IOException {
      if (toolchainModel != null) {
         serializer.startTag(NAMESPACE, tagName);
         if (toolchainModel.getType() != null) {
            serializer.startTag(NAMESPACE, "type").text(toolchainModel.getType()).endTag(NAMESPACE, "type");
         }

         if (toolchainModel.getProvides() != null) {
            ((Xpp3Dom)toolchainModel.getProvides()).writeToSerializer(NAMESPACE, serializer);
         }

         if (toolchainModel.getConfiguration() != null) {
            ((Xpp3Dom)toolchainModel.getConfiguration()).writeToSerializer(NAMESPACE, serializer);
         }

         serializer.endTag(NAMESPACE, tagName);
      }

   }
}
