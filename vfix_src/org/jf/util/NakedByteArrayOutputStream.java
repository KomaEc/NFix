package org.jf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NakedByteArrayOutputStream extends ByteArrayOutputStream {
   public byte[] getBuffer() throws IOException {
      return this.buf;
   }
}
