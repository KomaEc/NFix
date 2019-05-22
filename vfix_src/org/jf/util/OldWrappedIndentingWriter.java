package org.jf.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public final class OldWrappedIndentingWriter extends FilterWriter {
   private final String prefix;
   private final int width;
   private final int maxIndent;
   private int column;
   private boolean collectingIndent;
   private int indent;

   public OldWrappedIndentingWriter(Writer out, int width, String prefix) {
      super(out);
      if (out == null) {
         throw new NullPointerException("out == null");
      } else if (width < 0) {
         throw new IllegalArgumentException("width < 0");
      } else if (prefix == null) {
         throw new NullPointerException("prefix == null");
      } else {
         this.width = width != 0 ? width : Integer.MAX_VALUE;
         this.maxIndent = width >> 1;
         this.prefix = prefix.length() == 0 ? null : prefix;
         this.bol();
      }
   }

   public OldWrappedIndentingWriter(Writer out, int width) {
      this(out, width, "");
   }

   public void write(int c) throws IOException {
      synchronized(this.lock) {
         if (this.collectingIndent) {
            if (c == 32) {
               ++this.indent;
               if (this.indent >= this.maxIndent) {
                  this.indent = this.maxIndent;
                  this.collectingIndent = false;
               }
            } else {
               this.collectingIndent = false;
            }
         }

         if (this.column == this.width && c != 10) {
            this.out.write(10);
            this.column = 0;
         }

         if (this.column == 0) {
            if (this.prefix != null) {
               this.out.write(this.prefix);
            }

            if (!this.collectingIndent) {
               for(int i = 0; i < this.indent; ++i) {
                  this.out.write(32);
               }

               this.column = this.indent;
            }
         }

         this.out.write(c);
         if (c == 10) {
            this.bol();
         } else {
            ++this.column;
         }

      }
   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      synchronized(this.lock) {
         while(len > 0) {
            this.write(cbuf[off]);
            ++off;
            --len;
         }

      }
   }

   public void write(String str, int off, int len) throws IOException {
      synchronized(this.lock) {
         while(len > 0) {
            this.write(str.charAt(off));
            ++off;
            --len;
         }

      }
   }

   private void bol() {
      this.column = 0;
      this.collectingIndent = this.maxIndent != 0;
      this.indent = 0;
   }
}
