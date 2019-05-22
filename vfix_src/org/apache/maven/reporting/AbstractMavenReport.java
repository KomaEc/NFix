package org.apache.maven.reporting;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.siterenderer.RendererException;
import org.apache.maven.doxia.siterenderer.SiteRenderingContext;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.sink.SinkFactory;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XmlStreamWriter;

public abstract class AbstractMavenReport extends AbstractMojo implements MavenReport {
   private Sink sink;
   private Locale locale;
   private File reportOutputDirectory;

   public AbstractMavenReport() {
      this.locale = Locale.ENGLISH;
   }

   protected abstract Renderer getSiteRenderer();

   protected abstract String getOutputDirectory();

   protected abstract MavenProject getProject();

   public void execute() throws MojoExecutionException {
      SiteRendererSink sink;
      try {
         String outputDirectory = this.getOutputDirectory();
         sink = SinkFactory.createSink(new File(outputDirectory), this.getOutputName() + ".html");
         this.generate(sink, Locale.getDefault());
      } catch (MavenReportException var13) {
         throw new MojoExecutionException("An error has occurred in " + this.getName(this.locale) + " report generation.", var13);
      }

      File outputHtml = new File(this.getOutputDirectory(), this.getOutputName() + ".html");
      outputHtml.getParentFile().mkdirs();
      XmlStreamWriter writer = null;

      try {
         SiteRenderingContext context = new SiteRenderingContext();
         context.setDecoration(new DecorationModel());
         context.setTemplateName("org/apache/maven/doxia/siterenderer/resources/default-site.vm");
         context.setLocale(this.locale);
         writer = WriterFactory.newXmlWriter(outputHtml);
         this.getSiteRenderer().generateDocument(writer, sink, context);
      } catch (RendererException var10) {
         throw new MojoExecutionException("An error has occurred in " + this.getName(Locale.ENGLISH) + " report generation.", var10);
      } catch (IOException var11) {
         throw new MojoExecutionException("An error has occurred in " + this.getName(Locale.ENGLISH) + " report generation.", var11);
      } finally {
         IOUtil.close((Writer)writer);
      }

   }

   public void generate(org.codehaus.doxia.sink.Sink sink, Locale locale) throws MavenReportException {
      if (sink == null) {
         throw new MavenReportException("You must specify a sink.");
      } else {
         this.sink = sink;
         this.executeReport(locale);
         this.closeReport();
      }
   }

   protected abstract void executeReport(Locale var1) throws MavenReportException;

   protected void closeReport() {
      this.getSink().close();
   }

   public String getCategoryName() {
      return "Project Reports";
   }

   public File getReportOutputDirectory() {
      if (this.reportOutputDirectory == null) {
         this.reportOutputDirectory = new File(this.getOutputDirectory());
      }

      return this.reportOutputDirectory;
   }

   public void setReportOutputDirectory(File reportOutputDirectory) {
      this.reportOutputDirectory = reportOutputDirectory;
   }

   public Sink getSink() {
      return this.sink;
   }

   public boolean isExternalReport() {
      return false;
   }

   public boolean canGenerateReport() {
      return true;
   }
}
