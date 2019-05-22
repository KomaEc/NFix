package org.apache.maven.scm.providers.vss.settings.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import org.apache.maven.scm.providers.vss.settings.Settings;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class VssXpp3Writer {
   private String NAMESPACE;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "vss-settings", serializer);
      serializer.endDocument();
   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      if (settings != null) {
         serializer.startTag(this.NAMESPACE, tagName);
         if (settings.getVssDirectory() != null) {
            serializer.startTag(this.NAMESPACE, "vssDirectory").text(settings.getVssDirectory()).endTag(this.NAMESPACE, "vssDirectory");
         }

         serializer.endTag(this.NAMESPACE, tagName);
      }

   }
}
