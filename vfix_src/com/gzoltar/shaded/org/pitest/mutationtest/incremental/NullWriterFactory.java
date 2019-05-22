package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class NullWriterFactory implements WriterFactory {
   private final PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.nullOutputStream()));

   private OutputStream nullOutputStream() {
      return new OutputStream() {
         public void write(int b) throws IOException {
         }
      };
   }

   public PrintWriter create() {
      return this.pw;
   }

   public void close() {
      this.pw.close();
   }
}
