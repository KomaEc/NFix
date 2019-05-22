package org.jboss.util.stream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NotifyingBufferedOutputStream extends BufferedOutputStream {
   int chunkSize;
   int chunk = 0;
   StreamListener listener;

   public NotifyingBufferedOutputStream(OutputStream os, int size, int chunkSize, StreamListener listener) {
      super(os, size);
      if (chunkSize <= size) {
         throw new IllegalArgumentException("chunkSize must be bigger than the buffer");
      } else {
         this.chunkSize = chunkSize;
         this.listener = listener;
      }
   }

   public void setStreamListener(StreamListener listener) {
      this.listener = listener;
   }

   public void write(int b) throws IOException {
      super.write(b);
      this.checkNotification(1);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      super.write(b, off, len);
      this.checkNotification(len);
   }

   public void checkNotification(int result) {
      this.chunk += result;
      if (this.chunk >= this.chunkSize) {
         if (this.listener != null) {
            this.listener.onStreamNotification(this, this.chunk);
         }

         this.chunk = 0;
      }

   }
}
