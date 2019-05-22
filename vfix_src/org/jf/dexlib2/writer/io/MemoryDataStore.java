package org.jf.dexlib2.writer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import javax.annotation.Nonnull;

public class MemoryDataStore implements DexDataStore {
   private byte[] buf;

   public MemoryDataStore() {
      this(1048576);
   }

   public MemoryDataStore(int initialCapacity) {
      this.buf = new byte[initialCapacity];
   }

   public byte[] getData() {
      return this.buf;
   }

   @Nonnull
   public OutputStream outputAt(final int offset) {
      return new OutputStream() {
         private int position = offset;

         public void write(int b) throws IOException {
            MemoryDataStore.this.growBufferIfNeeded(this.position);
            MemoryDataStore.this.buf[this.position++] = (byte)b;
         }

         public void write(byte[] b) throws IOException {
            MemoryDataStore.this.growBufferIfNeeded(this.position + b.length);
            System.arraycopy(b, 0, MemoryDataStore.this.buf, this.position, b.length);
            this.position += b.length;
         }

         public void write(byte[] b, int off, int len) throws IOException {
            MemoryDataStore.this.growBufferIfNeeded(this.position + len);
            System.arraycopy(b, off, MemoryDataStore.this.buf, this.position, len);
            this.position += len;
         }
      };
   }

   private void growBufferIfNeeded(int index) {
      if (index >= this.buf.length) {
         this.buf = Arrays.copyOf(this.buf, (int)((double)(index + 1) * 1.2D));
      }
   }

   @Nonnull
   public InputStream readAt(final int offset) {
      return new InputStream() {
         private int position = offset;

         public int read() throws IOException {
            return this.position >= MemoryDataStore.this.buf.length ? -1 : MemoryDataStore.this.buf[this.position++];
         }

         public int read(byte[] b) throws IOException {
            int readLength = Math.min(b.length, MemoryDataStore.this.buf.length - this.position);
            if (readLength <= 0) {
               return this.position >= MemoryDataStore.this.buf.length ? -1 : 0;
            } else {
               System.arraycopy(MemoryDataStore.this.buf, this.position, b, 0, readLength);
               this.position += readLength;
               return readLength;
            }
         }

         public int read(byte[] b, int off, int len) throws IOException {
            int readLength = Math.min(len, MemoryDataStore.this.buf.length - this.position);
            if (readLength <= 0) {
               return this.position >= MemoryDataStore.this.buf.length ? -1 : 0;
            } else {
               System.arraycopy(MemoryDataStore.this.buf, this.position, b, 0, readLength);
               this.position += readLength;
               return readLength;
            }
         }

         public long skip(long n) throws IOException {
            int skipLength = (int)Math.min(n, (long)(MemoryDataStore.this.buf.length - this.position));
            this.position += skipLength;
            return (long)skipLength;
         }

         public int available() throws IOException {
            return MemoryDataStore.this.buf.length - this.position;
         }
      };
   }

   public void close() throws IOException {
   }
}
