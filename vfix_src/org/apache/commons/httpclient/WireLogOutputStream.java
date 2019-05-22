package org.apache.commons.httpclient;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class WireLogOutputStream extends FilterOutputStream {
   private OutputStream out;
   private Wire wire;

   public WireLogOutputStream(OutputStream out, Wire wire) {
      super(out);
      this.out = out;
      this.wire = wire;
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.out.write(b, off, len);
      this.wire.output(b, off, len);
   }

   public void write(int b) throws IOException {
      this.out.write(b);
      this.wire.output(b);
   }

   public void write(byte[] b) throws IOException {
      this.out.write(b);
      this.wire.output(b);
   }
}
