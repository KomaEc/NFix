package org.apache.maven.scm.providers.clearcase.settings.io.xpp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class ClearcaseXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "clearcase-settings", serializer);
      serializer.endDocument();
   }

   public void write(OutputStream stream, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(stream, settings.getModelEncoding());
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "clearcase-settings", serializer);
      serializer.endDocument();
   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      serializer.setPrefix("", "http://maven.apache.org/SCM/CLEARCASE/1.1.0");
      serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      serializer.startTag(NAMESPACE, tagName);
      serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/SCM/CLEARCASE/1.1.0 http://maven.apache.org/xsd/scm-clearcase-1.1.0.xsd");
      if (settings.getViewstore() != null) {
         serializer.startTag(NAMESPACE, "viewstore").text(settings.getViewstore()).endTag(NAMESPACE, "viewstore");
      }

      if (!settings.isUseVWSParameter()) {
         serializer.startTag(NAMESPACE, "useVWSParameter").text(String.valueOf(settings.isUseVWSParameter())).endTag(NAMESPACE, "useVWSParameter");
      }

      if (settings.getClearcaseType() != null) {
         serializer.startTag(NAMESPACE, "clearcaseType").text(settings.getClearcaseType()).endTag(NAMESPACE, "clearcaseType");
      }

      if (settings.getChangelogUserFormat() != null) {
         serializer.startTag(NAMESPACE, "changelogUserFormat").text(settings.getChangelogUserFormat()).endTag(NAMESPACE, "changelogUserFormat");
      }

      serializer.endTag(NAMESPACE, tagName);
   }
}
