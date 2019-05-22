package org.jf.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;

public class RandomAccessFileInputStream extends InputStream {
   private int filePosition;
   @Nonnull
   private final RandomAccessFile raf;

   public RandomAccessFileInputStream(@Nonnull RandomAccessFile raf, int filePosition) {
      this.filePosition = filePosition;
      this.raf = raf;
   }

   public int read() throws IOException {
      this.raf.seek((long)this.filePosition);
      ++this.filePosition;
      return this.raf.read();
   }

   public int read(byte[] bytes) throws IOException {
      this.raf.seek((long)this.filePosition);
      int bytesRead = this.raf.read(bytes);
      this.filePosition += bytesRead;
      return bytesRead;
   }

   public int read(byte[] bytes, int offset, int length) throws IOException {
      this.raf.seek((long)this.filePosition);
      int bytesRead = this.raf.read(bytes, offset, length);
      this.filePosition += bytesRead;
      return bytesRead;
   }

   public long skip(long l) throws IOException {
      int skipBytes = Math.min((int)l, this.available());
      this.filePosition += skipBytes;
      return (long)skipBytes;
   }

   public int available() throws IOException {
      return (int)this.raf.length() - this.filePosition;
   }

   public boolean markSupported() {
      return false;
   }
}
