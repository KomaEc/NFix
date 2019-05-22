package org.apache.maven.reporting.sink;

import java.io.File;
import java.io.IOException;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.siterenderer.RendererException;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;

public class SinkFactory {
   private String siteDirectory;
   private Renderer siteRenderer;

   public void setSiteRenderer(Renderer siteRenderer) {
      this.siteRenderer = siteRenderer;
   }

   public void setSiteDirectory(String siteDirectory) {
      this.siteDirectory = siteDirectory;
   }

   public Sink getSink(String outputFileName) throws RendererException, IOException {
      return createSink(new File(this.siteDirectory), outputFileName);
   }

   public static SiteRendererSink createSink(File basedir, String document) {
      return new SiteRendererSink(new RenderingContext(basedir, document));
   }
}
