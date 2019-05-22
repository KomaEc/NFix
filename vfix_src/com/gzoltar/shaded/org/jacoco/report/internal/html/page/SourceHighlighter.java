package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ILine;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceNode;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;

final class SourceHighlighter {
   private final Locale locale;
   private String lang;

   public SourceHighlighter(Locale locale) {
      this.locale = locale;
      this.lang = "java";
   }

   public void setLanguage(String lang) {
      this.lang = lang;
   }

   public void render(HTMLElement parent, ISourceNode source, Reader contents) throws IOException {
      HTMLElement pre = parent.pre("source lang-" + this.lang + " linenums");
      BufferedReader lineBuffer = new BufferedReader(contents);
      int nr = 0;

      String line;
      while((line = lineBuffer.readLine()) != null) {
         ++nr;
         this.renderCodeLine(pre, line, source.getLine(nr), nr);
      }

   }

   private void renderCodeLine(HTMLElement pre, String linesrc, ILine line, int lineNr) throws IOException {
      this.highlight(pre, line, lineNr).text(linesrc);
      pre.text("\n");
   }

   HTMLElement highlight(HTMLElement pre, ILine line, int lineNr) throws IOException {
      String style;
      switch(line.getStatus()) {
      case 1:
         style = "nc";
         break;
      case 2:
         style = "fc";
         break;
      case 3:
         style = "pc";
         break;
      default:
         return pre;
      }

      String lineId = "L" + Integer.toString(lineNr);
      ICounter branches = line.getBranchCounter();
      switch(branches.getStatus()) {
      case 1:
         return this.span(pre, lineId, style, "bnc", "All %2$d branches missed.", branches);
      case 2:
         return this.span(pre, lineId, style, "bfc", "All %2$d branches covered.", branches);
      case 3:
         return this.span(pre, lineId, style, "bpc", "%1$d of %2$d branches missed.", branches);
      default:
         return pre.span(style, lineId);
      }
   }

   private HTMLElement span(HTMLElement parent, String id, String style1, String style2, String title, ICounter branches) throws IOException {
      HTMLElement span = parent.span(style1 + " " + style2, id);
      Integer missed = branches.getMissedCount();
      Integer total = branches.getTotalCount();
      span.attr("title", String.format(this.locale, title, missed, total));
      return span;
   }
}
