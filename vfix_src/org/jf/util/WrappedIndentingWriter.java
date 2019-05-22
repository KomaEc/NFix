package org.jf.util;

import com.google.common.collect.Lists;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class WrappedIndentingWriter extends FilterWriter {
   private final int maxIndent;
   private final int maxWidth;
   private int currentIndent = 0;
   private final StringBuilder line = new StringBuilder();

   public WrappedIndentingWriter(Writer out, int maxIndent, int maxWidth) {
      super(out);
      this.maxIndent = maxIndent;
      this.maxWidth = maxWidth;
   }

   private void writeIndent() throws IOException {
      for(int i = 0; i < this.getIndent(); ++i) {
         this.write(32);
      }

   }

   private int getIndent() {
      if (this.currentIndent < 0) {
         return 0;
      } else {
         return this.currentIndent > this.maxIndent ? this.maxIndent : this.currentIndent;
      }
   }

   public void indent(int indent) {
      this.currentIndent += indent;
   }

   public void deindent(int indent) {
      this.currentIndent -= indent;
   }

   private void wrapLine() throws IOException {
      List<String> wrapped = Lists.newArrayList(StringWrapper.wrapStringOnBreaks(this.line.toString(), this.maxWidth));
      this.out.write((String)wrapped.get(0), 0, ((String)wrapped.get(0)).length());
      this.out.write(10);
      this.line.replace(0, this.line.length(), "");
      this.writeIndent();

      for(int i = 1; i < wrapped.size(); ++i) {
         if (i > 1) {
            this.write(10);
         }

         this.write((String)wrapped.get(i));
      }

   }

   public void write(int c) throws IOException {
      if (c == 10) {
         this.out.write(this.line.toString());
         this.out.write(c);
         this.line.replace(0, this.line.length(), "");
         this.writeIndent();
      } else {
         this.line.append((char)c);
         if (this.line.length() > this.maxWidth) {
            this.wrapLine();
         }
      }

   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         this.write(cbuf[i + off]);
      }

   }

   public void write(String str, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         this.write(str.charAt(i + off));
      }

   }

   public void flush() throws IOException {
      this.out.write(this.line.toString());
      this.line.replace(0, this.line.length(), "");
   }
}
