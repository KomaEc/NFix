package org.apache.maven.doxia.siterenderer.sink;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.maven.doxia.module.xhtml.XhtmlSink;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;
import org.codehaus.doxia.sink.Sink;

public class SiteRendererSink extends XhtmlSink implements Sink {
   private String date;
   private String title;
   private List authors;
   private final Writer writer;

   public SiteRendererSink(RenderingContext renderingContext) {
      this(new StringWriter(), renderingContext);
   }

   private SiteRendererSink(StringWriter writer, RenderingContext renderingContext) {
      super(writer, renderingContext, (Map)null);
      this.date = "";
      this.title = "";
      this.authors = new ArrayList();
      this.writer = writer;
   }

   public void title_() {
      if (this.getBuffer().length() > 0) {
         this.title = this.getBuffer().toString();
      }

      this.resetBuffer();
   }

   public void title() {
   }

   public String getTitle() {
      return this.title;
   }

   public void author_() {
      if (this.getBuffer().length() > 0) {
         this.authors.add(this.getBuffer().toString());
      }

      this.resetBuffer();
   }

   public List getAuthors() {
      return this.authors;
   }

   public void date_() {
      if (this.getBuffer().length() > 0) {
         this.date = this.getBuffer().toString();
      }

      this.resetBuffer();
   }

   public String getDate() {
      return this.date;
   }

   public void body_() {
   }

   public void body() {
   }

   public String getBody() {
      return this.writer.toString();
   }

   public void head_() {
      this.setHeadFlag(false);
   }

   public void head() {
      this.setHeadFlag(true);
   }
}
