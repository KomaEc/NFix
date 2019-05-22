package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;

public final class StripLineBreaks extends BaseParamFilterReader implements ChainableReader {
   private static final String DEFAULT_LINE_BREAKS = "\r\n";
   private static final String LINE_BREAKS_KEY = "linebreaks";
   private String lineBreaks = "\r\n";

   public StripLineBreaks() {
   }

   public StripLineBreaks(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch;
      for(ch = this.in.read(); ch != -1 && this.lineBreaks.indexOf(ch) != -1; ch = this.in.read()) {
      }

      return ch;
   }

   public void setLineBreaks(String lineBreaks) {
      this.lineBreaks = lineBreaks;
   }

   private String getLineBreaks() {
      return this.lineBreaks;
   }

   public Reader chain(Reader rdr) {
      StripLineBreaks newFilter = new StripLineBreaks(rdr);
      newFilter.setLineBreaks(this.getLineBreaks());
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
      String userDefinedLineBreaks = null;
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("linebreaks".equals(params[i].getName())) {
               userDefinedLineBreaks = params[i].getValue();
               break;
            }
         }
      }

      if (userDefinedLineBreaks != null) {
         this.lineBreaks = userDefinedLineBreaks;
      }

   }
}
