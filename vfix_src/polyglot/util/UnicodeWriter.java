package polyglot.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class UnicodeWriter extends FilterWriter {
   public UnicodeWriter(Writer out) {
      super(out);
   }

   public void write(int c) throws IOException {
      if (c <= 255) {
         super.write(c);
      } else {
         String s = String.valueOf(Integer.toHexString(c));
         super.write(92);
         super.write(117);

         for(int i = s.length(); i < 4; ++i) {
            super.write(48);
         }

         this.write(s);
      }

   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         this.write(cbuf[i + off]);
      }

   }

   public void write(String str, int off, int len) throws IOException {
      this.write(str.toCharArray(), off, len);
   }
}
