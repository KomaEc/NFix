package com.gzoltar.shaded.org.pitest.maven.report;

import com.gzoltar.shaded.org.pitest.maven.report.generator.ReportGenerationContext;
import com.gzoltar.shaded.org.pitest.maven.report.generator.ReportGenerationManager;
import com.gzoltar.shaded.org.pitest.util.PitError;
import java.io.File;
import java.util.List;
import java.util.Locale;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

public class PitReportMojo extends AbstractMavenReport {
   private Renderer siteRenderer;
   private MavenProject project;
   private boolean skip;
   private File reportsDirectory;
   private List<String> sourceDataFormats;
   private String siteReportName;
   private String siteReportDescription;
   private String siteReportDirectory;
   private ReportGenerationManager reportGenerationManager = new ReportGenerationManager();

   public String getOutputName() {
      return this.siteReportDirectory + File.separator + "index";
   }

   public String getName(Locale locale) {
      return this.siteReportName;
   }

   public String getDescription(Locale locale) {
      return this.siteReportDescription;
   }

   protected Renderer getSiteRenderer() {
      return this.siteRenderer;
   }

   protected String getOutputDirectory() {
      return this.reportsDirectory.getAbsolutePath();
   }

   protected MavenProject getProject() {
      return this.project;
   }

   protected void executeReport(Locale locale) throws MavenReportException {
      this.getLog().debug((CharSequence)"PitReportMojo - starting");
      if (!this.reportsDirectory.exists()) {
         throw new PitError("could not find reports directory [" + this.reportsDirectory + "]");
      } else if (!this.reportsDirectory.canRead()) {
         throw new PitError("reports directory [" + this.reportsDirectory + "] not readable");
      } else if (!this.reportsDirectory.isDirectory()) {
         throw new PitError("reports directory [" + this.reportsDirectory + "] is actually a file, it must be a directory");
      } else {
         this.reportGenerationManager.generateSiteReport(this.buildReportGenerationContext(locale));
         this.getLog().debug((CharSequence)"PitReportMojo - ending");
      }
   }

   public boolean canGenerateReport() {
      return !this.skip;
   }

   public boolean isExternalReport() {
      return true;
   }

   public boolean isSkip() {
      return this.skip;
   }

   public File getReportsDirectory() {
      return this.reportsDirectory;
   }

   public List<String> getSourceDataFormats() {
      return this.sourceDataFormats;
   }

   private ReportGenerationContext buildReportGenerationContext(Locale locale) {
      return new ReportGenerationContext(locale, this.getSink(), this.reportsDirectory, new File(this.getReportOutputDirectory().getAbsolutePath() + File.separator + this.siteReportDirectory), this.getLog(), this.getSourceDataFormats());
   }
}
