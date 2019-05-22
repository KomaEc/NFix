package groovy.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class PlatformLineWriter extends Writer {
   private BufferedWriter writer;

   public PlatformLineWriter(Writer out) {
      this.writer = new BufferedWriter(out);
   }

   public PlatformLineWriter(Writer out, int sz) {
      this.writer = new BufferedWriter(out, sz);
   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      for(; len > 0; --len) {
         char c = cbuf[off++];
         if (c == '\n') {
            this.writer.newLine();
         } else if (c != '\r') {
            this.writer.write(c);
         }
      }

   }

   public void flush() throws IOException {
      this.writer.flush();
   }

   public void close() throws IOException {
      this.writer.close();
   }
}
