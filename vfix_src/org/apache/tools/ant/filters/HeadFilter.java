package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.LineTokenizer;

public final class HeadFilter extends BaseParamFilterReader implements ChainableReader {
   private static final String LINES_KEY = "lines";
   private static final String SKIP_KEY = "skip";
   private long linesRead = 0L;
   private static final int DEFAULT_NUM_LINES = 10;
   private long lines = 10L;
   private long skip = 0L;
   private LineTokenizer lineTokenizer = null;
   private String line = null;
   private int linePos = 0;

   public HeadFilter() {
   }

   public HeadFilter(Reader in) {
      super(in);
      this.lineTokenizer = new LineTokenizer();
      this.lineTokenizer.setIncludeDelims(true);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      while(this.line == null || this.line.length() == 0) {
         this.line = this.lineTokenizer.getToken(this.in);
         if (this.line == null) {
            return -1;
         }

         this.line = this.headFilter(this.line);
         this.linePos = 0;
      }

      int ch = this.line.charAt(this.linePos);
      ++this.linePos;
      if (this.linePos == this.line.length()) {
         this.line = null;
      }

      return ch;
   }

   public void setLines(long lines) {
      this.lines = lines;
   }

   private long getLines() {
      return this.lines;
   }

   public void setSkip(long skip) {
      this.skip = skip;
   }

   private long getSkip() {
      return this.skip;
   }

   public Reader chain(Reader rdr) {
      HeadFilter newFilter = new HeadFilter(rdr);
      newFilter.setLines(this.getLines());
      newFilter.setSkip(this.getSkip());
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("lines".equals(params[i].getName())) {
               this.lines = new Long(params[i].getValue());
            } else if ("skip".equals(params[i].getName())) {
               this.skip = new Long(params[i].getValue());
            }
         }
      }

   }

   private String headFilter(String line) {
      ++this.linesRead;
      if (this.skip > 0L && this.linesRead - 1L < this.skip) {
         return null;
      } else {
         return this.lines > 0L && this.linesRead > this.lines + this.skip ? null : line;
      }
   }
}
