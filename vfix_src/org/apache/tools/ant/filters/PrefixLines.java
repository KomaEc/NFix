package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;

public final class PrefixLines extends BaseParamFilterReader implements ChainableReader {
   private static final String PREFIX_KEY = "prefix";
   private String prefix = null;
   private String queuedData = null;

   public PrefixLines() {
   }

   public PrefixLines(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch = true;
      if (this.queuedData != null && this.queuedData.length() == 0) {
         this.queuedData = null;
      }

      int ch;
      if (this.queuedData != null) {
         ch = this.queuedData.charAt(0);
         this.queuedData = this.queuedData.substring(1);
         if (this.queuedData.length() == 0) {
            this.queuedData = null;
         }
      } else {
         this.queuedData = this.readLine();
         if (this.queuedData != null) {
            if (this.prefix != null) {
               this.queuedData = this.prefix + this.queuedData;
            }

            return this.read();
         }

         ch = -1;
      }

      return ch;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   private String getPrefix() {
      return this.prefix;
   }

   public Reader chain(Reader rdr) {
      PrefixLines newFilter = new PrefixLines(rdr);
      newFilter.setPrefix(this.getPrefix());
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("prefix".equals(params[i].getName())) {
               this.prefix = params[i].getValue();
               break;
            }
         }
      }

   }
}
