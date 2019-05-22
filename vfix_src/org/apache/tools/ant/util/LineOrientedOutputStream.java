package org.apache.tools.ant.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class LineOrientedOutputStream extends OutputStream {
   private static final int INTIAL_SIZE = 132;
   private static final int CR = 13;
   private static final int LF = 10;
   private ByteArrayOutputStream buffer = new ByteArrayOutputStream(132);
   private boolean skip = false;

   public final void write(int cc) throws IOException {
      byte c = (byte)cc;
      if (c != 10 && c != 13) {
         this.buffer.write(cc);
      } else if (!this.skip) {
         this.processBuffer();
      }

      this.skip = c == 13;
   }

   public final void flush() throws IOException {
      if (this.buffer.size() > 0) {
         this.processBuffer();
      }

   }

   protected void processBuffer() throws IOException {
      try {
         this.processLine(this.buffer.toString());
      } finally {
         this.buffer.reset();
      }

   }

   protected abstract void processLine(String var1) throws IOException;

   public final void close() throws IOException {
      if (this.buffer.size() > 0) {
         this.processBuffer();
      }

      super.close();
   }

   public final void write(byte[] b, int off, int len) throws IOException {
      int offset = off;
      int blockStartOffset = off;

      for(int remaining = len; remaining > 0; blockStartOffset = offset) {
         while(remaining > 0 && b[offset] != 10 && b[offset] != 13) {
            ++offset;
            --remaining;
         }

         int blockLength = offset - blockStartOffset;
         if (blockLength > 0) {
            this.buffer.write(b, blockStartOffset, blockLength);
         }

         while(remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
            this.write(b[offset]);
            ++offset;
            --remaining;
         }
      }

   }
}
