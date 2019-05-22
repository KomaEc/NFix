package org.apache.maven.scm.providers.cvslib.settings.io.xpp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.scm.providers.cvslib.settings.Settings;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class CvsXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "cvs-settings", serializer);
      serializer.endDocument();
   }

   public void write(OutputStream stream, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(stream, settings.getModelEncoding());
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "cvs-settings", serializer);
      serializer.endDocument();
   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      serializer.setPrefix("", "http://maven.apache.org/SCM/CVS/1.0.0");
      serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      serializer.startTag(NAMESPACE, tagName);
      serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/SCM/CVS/1.0.0 http://maven.apache.org/xsd/scm-cvs-1.0.0.xsd");
      if (settings.getChangeLogCommandDateFormat() != null && !settings.getChangeLogCommandDateFormat().equals("yyyy-MM-dd HH:mm:ssZ")) {
         serializer.startTag(NAMESPACE, "changeLogCommandDateFormat").text(settings.getChangeLogCommandDateFormat()).endTag(NAMESPACE, "changeLogCommandDateFormat");
      }

      if (settings.isUseCvsrc()) {
         serializer.startTag(NAMESPACE, "useCvsrc").text(String.valueOf(settings.isUseCvsrc())).endTag(NAMESPACE, "useCvsrc");
      }

      if (settings.getCompressionLevel() != 3) {
         serializer.startTag(NAMESPACE, "compressionLevel").text(String.valueOf(settings.getCompressionLevel())).endTag(NAMESPACE, "compressionLevel");
      }

      if (settings.isTraceCvsCommand()) {
         serializer.startTag(NAMESPACE, "traceCvsCommand").text(String.valueOf(settings.isTraceCvsCommand())).endTag(NAMESPACE, "traceCvsCommand");
      }

      if (settings.getTemporaryFilesDirectory() != null) {
         serializer.startTag(NAMESPACE, "temporaryFilesDirectory").text(settings.getTemporaryFilesDirectory()).endTag(NAMESPACE, "temporaryFilesDirectory");
      }

      if (settings.getCvsVariables() != null && settings.getCvsVariables().size() > 0) {
         serializer.startTag(NAMESPACE, "cvsVariables");
         Iterator iter = settings.getCvsVariables().keySet().iterator();

         while(iter.hasNext()) {
            String key = (String)iter.next();
            String value = (String)settings.getCvsVariables().get(key);
            serializer.startTag(NAMESPACE, "" + key + "").text(value).endTag(NAMESPACE, "" + key + "");
         }

         serializer.endTag(NAMESPACE, "cvsVariables");
      }

      if (!settings.isUseForceTag()) {
         serializer.startTag(NAMESPACE, "useForceTag").text(String.valueOf(settings.isUseForceTag())).endTag(NAMESPACE, "useForceTag");
      }

      serializer.endTag(NAMESPACE, tagName);
   }
}
