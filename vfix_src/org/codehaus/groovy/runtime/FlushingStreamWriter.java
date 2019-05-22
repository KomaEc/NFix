package org.codehaus.groovy.runtime;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FlushingStreamWriter extends OutputStreamWriter {
   public FlushingStreamWriter(OutputStream out) {
      super(out);
   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      super.write(cbuf, off, len);
      this.flush();
   }

   public void write(int c) throws IOException {
      super.write(c);
      this.flush();
   }

   public void write(String str, int off, int len) throws IOException {
      super.write(str, off, len);
      this.flush();
   }
}
