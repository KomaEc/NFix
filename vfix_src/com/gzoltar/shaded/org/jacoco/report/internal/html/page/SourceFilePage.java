package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import java.io.IOException;
import java.io.Reader;

public class SourceFilePage extends NodePage<ISourceNode> {
   private final Reader sourceReader;
   private final int tabWidth;

   public SourceFilePage(ISourceNode sourceFileNode, Reader sourceReader, int tabWidth, ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      super(sourceFileNode, parent, folder, context);
      this.sourceReader = sourceReader;
      this.tabWidth = tabWidth;
   }

   protected void content(HTMLElement body) throws IOException {
      SourceHighlighter hl = new SourceHighlighter(this.context.getLocale());
      hl.render(body, (ISourceNode)this.getNode(), this.sourceReader);
      this.sourceReader.close();
   }

   protected void head(HTMLElement head) throws IOException {
      super.head(head);
      head.link("stylesheet", this.context.getResources().getLink(this.folder, "prettify.css"), "text/css");
      head.script("text/javascript", this.context.getResources().getLink(this.folder, "prettify.js"));
   }

   protected String getOnload() {
      return String.format("window['PR_TAB_WIDTH']=%d;prettyPrint()", this.tabWidth);
   }

   protected String getFileName() {
      return ((ISourceNode)this.getNode()).getName() + ".html";
   }
}
