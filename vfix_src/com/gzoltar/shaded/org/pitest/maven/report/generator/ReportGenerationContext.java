package com.gzoltar.shaded.org.pitest.maven.report.generator;

import java.io.File;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;

public class ReportGenerationContext {
   private Locale locale;
   private Sink sink;
   private File reportsDataDirectory;
   private File siteDirectory;
   private Log logger;
   private List<String> sourceDataFormats;

   public ReportGenerationContext() {
   }

   public ReportGenerationContext(Locale locale, Sink sink, File reportsDataDirectory, File siteDirectory, Log logger, List<String> sourceDataFormats) {
      this.locale = locale;
      this.sink = sink;
      this.reportsDataDirectory = reportsDataDirectory;
      this.siteDirectory = siteDirectory;
      this.logger = logger;
      this.sourceDataFormats = sourceDataFormats;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   public Sink getSink() {
      return this.sink;
   }

   public void setSink(Sink sink) {
      this.sink = sink;
   }

   public File getReportsDataDirectory() {
      return this.reportsDataDirectory;
   }

   public void setReportsDataDirectory(File reportsDataDirectory) {
      this.reportsDataDirectory = reportsDataDirectory;
   }

   public File getSiteDirectory() {
      return this.siteDirectory;
   }

   public void setSiteDirectory(File siteDirectory) {
      this.siteDirectory = siteDirectory;
   }

   public Log getLogger() {
      return this.logger;
   }

   public void setLogger(Log logger) {
      this.logger = logger;
   }

   public List<String> getSourceDataFormats() {
      return this.sourceDataFormats;
   }

   public void setSourceDataFormats(List<String> sourceDataFormats) {
      this.sourceDataFormats = sourceDataFormats;
   }

   public String toString() {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
   }
}
