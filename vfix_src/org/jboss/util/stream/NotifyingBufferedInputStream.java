package org.jboss.util.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NotifyingBufferedInputStream extends BufferedInputStream {
   int chunkSize;
   int chunk = 0;
   StreamListener listener;

   public NotifyingBufferedInputStream(InputStream is, int size, int chunkSize, StreamListener listener) {
      super(is, size);
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

   public int read() throws IOException {
      int result = super.read();
      if (result == -1) {
         return result;
      } else {
         this.checkNotification(result);
         return result;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      int result = super.read(b, off, len);
      if (result == -1) {
         return result;
      } else {
         this.checkNotification(result);
         return result;
      }
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
