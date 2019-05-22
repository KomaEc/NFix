package org.jf.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;

public class RandomAccessFileOutputStream extends OutputStream {
   private int filePosition;
   @Nonnull
   private final RandomAccessFile raf;

   public RandomAccessFileOutputStream(@Nonnull RandomAccessFile raf, int startFilePosition) {
      this.filePosition = startFilePosition;
      this.raf = raf;
   }

   public void write(int b) throws IOException {
      this.raf.seek((long)this.filePosition);
      ++this.filePosition;
      this.raf.write(b);
   }

   public void write(byte[] b) throws IOException {
      this.raf.seek((long)this.filePosition);
      this.filePosition += b.length;
      this.raf.write(b);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.raf.seek((long)this.filePosition);
      this.filePosition += len;
      this.raf.write(b, off, len);
   }
}
