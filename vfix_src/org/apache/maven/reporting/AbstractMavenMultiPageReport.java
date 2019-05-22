package org.apache.maven.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.RendererException;
import org.apache.maven.reporting.sink.MultiPageSink;
import org.apache.maven.reporting.sink.SinkFactory;

public abstract class AbstractMavenMultiPageReport extends AbstractMavenReport {
   private SinkFactory factory;
   private List sinks = new ArrayList();

   public void setSinkFactory(SinkFactory factory) {
      this.factory = factory;
   }

   public SinkFactory getSinkFactory() {
      return this.factory;
   }

   public boolean useDefaultSiteDescriptor() {
      return true;
   }

   public abstract boolean usePageLinkBar();

   private Sink getSink(String outputName) throws RendererException, IOException {
      return this.factory.getSink(outputName);
   }

   public MultiPageSink startPage(String outputName) throws RendererException, IOException {
      return new MultiPageSink(outputName, this.getSink(outputName));
   }

   public void endPage(MultiPageSink sink) {
      if (this.usePageLinkBar()) {
         this.sinks.add(sink);
      } else {
         sink.closeSink();
      }

   }

   protected void closeReport() {
      if (!this.sinks.isEmpty()) {
         Iterator i = this.sinks.iterator();

         while(i.hasNext()) {
            MultiPageSink currentSink = (MultiPageSink)i.next();
            currentSink.paragraph();

            for(int counter = 1; counter <= this.sinks.size(); ++counter) {
               if (counter > 1) {
                  currentSink.text("&nbsp;");
               }

               MultiPageSink sink = (MultiPageSink)this.sinks.get(counter - 1);
               sink.link(sink.getOutputName() + ".html");
               sink.text(String.valueOf(counter));
               sink.link_();
            }

            currentSink.paragraph_();
            currentSink.closeSink();
         }
      }

      super.closeReport();
   }
}
