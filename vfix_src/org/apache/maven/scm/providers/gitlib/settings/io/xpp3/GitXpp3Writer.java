package org.apache.maven.scm.providers.gitlib.settings.io.xpp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.maven.scm.providers.gitlib.settings.Settings;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class GitXpp3Writer {
   private static final String NAMESPACE = null;

   public void write(Writer writer, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(writer);
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "git-settings", serializer);
      serializer.endDocument();
   }

   public void write(OutputStream stream, Settings settings) throws IOException {
      XmlSerializer serializer = new MXSerializer();
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      serializer.setOutput(stream, settings.getModelEncoding());
      serializer.startDocument(settings.getModelEncoding(), (Boolean)null);
      this.writeSettings(settings, "git-settings", serializer);
      serializer.endDocument();
   }

   private void writeSettings(Settings settings, String tagName, XmlSerializer serializer) throws IOException {
      serializer.setPrefix("", "http://maven.apache.org/SCM/GIT/1.0.0");
      serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      serializer.startTag(NAMESPACE, tagName);
      serializer.attribute("", "xsi:schemaLocation", "http://maven.apache.org/SCM/GIT/1.0.0 http://maven.apache.org/xsd/scm-git-1.0.0.xsd");
      if (settings.getRevParseDateFormat() != null && !settings.getRevParseDateFormat().equals("yyyy-MM-dd HH:mm:ss")) {
         serializer.startTag(NAMESPACE, "revParseDateFormat").text(settings.getRevParseDateFormat()).endTag(NAMESPACE, "revParseDateFormat");
      }

      if (settings.getTraceGitCommand() != null && !settings.getTraceGitCommand().equals("")) {
         serializer.startTag(NAMESPACE, "traceGitCommand").text(settings.getTraceGitCommand()).endTag(NAMESPACE, "traceGitCommand");
      }

      if (settings.isCommitNoVerify()) {
         serializer.startTag(NAMESPACE, "commitNoVerify").text(String.valueOf(settings.isCommitNoVerify())).endTag(NAMESPACE, "commitNoVerify");
      }

      serializer.endTag(NAMESPACE, tagName);
   }
}
