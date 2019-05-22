package soot.util;

import jasmin.Main;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JasminOutputStream extends ByteArrayOutputStream {
   private final OutputStream out;

   public JasminOutputStream(OutputStream out) {
      this.out = out;
   }

   public void flush() {
      ByteArrayInputStream bais = new ByteArrayInputStream(this.toByteArray());
      Main.assemble((InputStream)bais, (OutputStream)this.out, false);
   }

   public void close() throws IOException {
      this.out.close();
      super.close();
   }
}
