package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.LineTokenizer;

public final class TailFilter extends BaseParamFilterReader implements ChainableReader {
   private static final String LINES_KEY = "lines";
   private static final String SKIP_KEY = "skip";
   private static final int DEFAULT_NUM_LINES = 10;
   private long lines = 10L;
   private long skip = 0L;
   private boolean completedReadAhead = false;
   private LineTokenizer lineTokenizer = null;
   private String line = null;
   private int linePos = 0;
   private LinkedList lineList = new LinkedList();

   public TailFilter() {
   }

   public TailFilter(Reader in) {
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
         this.line = this.tailFilter(this.line);
         if (this.line == null) {
            return -1;
         }

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
      TailFilter newFilter = new TailFilter(rdr);
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
               this.setLines(new Long(params[i].getValue()));
            } else if ("skip".equals(params[i].getName())) {
               this.skip = new Long(params[i].getValue());
            }
         }
      }

   }

   private String tailFilter(String line) {
      if (!this.completedReadAhead) {
         if (line != null) {
            this.lineList.add(line);
            if (this.lines == -1L) {
               if ((long)this.lineList.size() > this.skip) {
                  return (String)this.lineList.removeFirst();
               }
            } else {
               long linesToKeep = this.lines + (this.skip > 0L ? this.skip : 0L);
               if (linesToKeep < (long)this.lineList.size()) {
                  this.lineList.removeFirst();
               }
            }

            return "";
         }

         this.completedReadAhead = true;
         if (this.skip > 0L) {
            for(int i = 0; (long)i < this.skip; ++i) {
               this.lineList.removeLast();
            }
         }

         if (this.lines > -1L) {
            while((long)this.lineList.size() > this.lines) {
               this.lineList.removeFirst();
            }
         }
      }

      return this.lineList.size() > 0 ? (String)this.lineList.removeFirst() : null;
   }
}
