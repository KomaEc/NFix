package org.apache.maven.scm.providers.svn.settings.io.xpp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.maven.scm.providers.svn.settings.Settings;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class SvnXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "svn-settings", serializer);
      serializer.endDocument();
   }

   public void write(OutputStream stream, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(stream, settings.getModelEncoding());
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "svn-settings", serializer);
      serializer.endDocument();
   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      serializer.setPrefix("", "http://maven.apache.org/SCM/SVN/1.1.0");
      serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      serializer.startTag(NAMESPACE, tagName);
      serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/SCM/SVN/1.1.0 http://maven.apache.org/xsd/svn-settings-1.1.0.xsd");
      if (settings.getConfigDirectory() != null) {
         serializer.startTag(NAMESPACE, "configDirectory").text(settings.getConfigDirectory()).endTag(NAMESPACE, "configDirectory");
      }

      if (settings.isUseCygwinPath()) {
         serializer.startTag(NAMESPACE, "useCygwinPath").text(String.valueOf(settings.isUseCygwinPath())).endTag(NAMESPACE, "useCygwinPath");
      }

      if (settings.getCygwinMountPath() != null && !settings.getCygwinMountPath().equals("/cygwin")) {
         serializer.startTag(NAMESPACE, "cygwinMountPath").text(settings.getCygwinMountPath()).endTag(NAMESPACE, "cygwinMountPath");
      }

      if (!settings.isUseNonInteractive()) {
         serializer.startTag(NAMESPACE, "useNonInteractive").text(String.valueOf(settings.isUseNonInteractive())).endTag(NAMESPACE, "useNonInteractive");
      }

      if (settings.isUseAuthCache()) {
         serializer.startTag(NAMESPACE, "useAuthCache").text(String.valueOf(settings.isUseAuthCache())).endTag(NAMESPACE, "useAuthCache");
      }

      if (settings.isTrustServerCert()) {
         serializer.startTag(NAMESPACE, "trustServerCert").text(String.valueOf(settings.isTrustServerCert())).endTag(NAMESPACE, "trustServerCert");
      }

      serializer.endTag(NAMESPACE, tagName);
   }
}
