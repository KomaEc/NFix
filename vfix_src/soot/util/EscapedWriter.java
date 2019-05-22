package soot.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class EscapedWriter extends FilterWriter {
   public final String lineSeparator = System.getProperty("line.separator");
   private final int cr;
   private final int lf;
   private final StringBuffer mini;

   public EscapedWriter(Writer fos) {
      super(fos);
      this.cr = this.lineSeparator.charAt(0);
      this.lf = this.lineSeparator.length() == 2 ? this.lineSeparator.charAt(1) : -1;
      this.mini = new StringBuffer();
   }

   public void print(int ch) throws IOException {
      this.write(ch);
      throw new RuntimeException();
   }

   public void write(String s, int off, int len) throws IOException {
      for(int i = off; i < off + len; ++i) {
         this.write(s.charAt(i));
      }

   }

   public void write(int ch) throws IOException {
      if ((ch < 32 || ch > 126) && ch != this.cr && ch != this.lf && ch != 32) {
         this.mini.setLength(0);
         this.mini.append(Integer.toHexString(ch));

         while(this.mini.length() < 4) {
            this.mini.insert(0, "0");
         }

         this.mini.insert(0, "\\u");

         for(int i = 0; i < this.mini.length(); ++i) {
            super.write(this.mini.charAt(i));
         }

      } else {
         super.write(ch);
      }
   }
}
