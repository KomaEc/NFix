package org.jf.dexlib2.writer.io;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class MemoryDeferredOutputStream extends DeferredOutputStream {
   private static final int DEFAULT_BUFFER_SIZE = 16384;
   private final List<byte[]> buffers;
   private byte[] currentBuffer;
   private int currentPosition;

   public MemoryDeferredOutputStream() {
      this(16384);
   }

   public MemoryDeferredOutputStream(int bufferSize) {
      this.buffers = Lists.newArrayList();
      this.currentBuffer = new byte[bufferSize];
   }

   public void writeTo(OutputStream output) throws IOException {
      Iterator var2 = this.buffers.iterator();

      while(var2.hasNext()) {
         byte[] buffer = (byte[])var2.next();
         output.write(buffer);
      }

      if (this.currentPosition > 0) {
         output.write(this.currentBuffer, 0, this.currentPosition);
      }

      this.buffers.clear();
      this.currentPosition = 0;
   }

   public void write(int i) throws IOException {
      if (this.remaining() == 0) {
         this.buffers.add(this.currentBuffer);
         this.currentBuffer = new byte[this.currentBuffer.length];
         this.currentPosition = 0;
      }

      this.currentBuffer[this.currentPosition++] = (byte)i;
   }

   public void write(byte[] bytes) throws IOException {
      this.write(bytes, 0, bytes.length);
   }

   public void write(byte[] bytes, int offset, int length) throws IOException {
      int remaining = this.remaining();
      int written = 0;

      while(length - written > 0) {
         int toWrite = Math.min(remaining, length - written);
         System.arraycopy(bytes, offset + written, this.currentBuffer, this.currentPosition, toWrite);
         written += toWrite;
         this.currentPosition += toWrite;
         remaining = this.remaining();
         if (remaining == 0) {
            this.buffers.add(this.currentBuffer);
            this.currentBuffer = new byte[this.currentBuffer.length];
            this.currentPosition = 0;
            remaining = this.currentBuffer.length;
         }
      }

   }

   private int remaining() {
      return this.currentBuffer.length - this.currentPosition;
   }

   @Nonnull
   public static DeferredOutputStreamFactory getFactory() {
      return getFactory(16384);
   }

   @Nonnull
   public static DeferredOutputStreamFactory getFactory(final int bufferSize) {
      return new DeferredOutputStreamFactory() {
         public DeferredOutputStream makeDeferredOutputStream() {
            return new MemoryDeferredOutputStream(bufferSize);
         }
      };
   }
}
