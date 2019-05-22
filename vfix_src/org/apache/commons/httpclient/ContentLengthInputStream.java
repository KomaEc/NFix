package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;

public class ContentLengthInputStream extends InputStream {
   private int contentLength;
   private int pos = 0;
   private boolean closed = false;
   private InputStream wrappedStream;

   public ContentLengthInputStream(InputStream in, int contentLength) {
      this.wrappedStream = in;
      this.contentLength = contentLength;
   }

   public void close() throws IOException {
      if (!this.closed) {
         try {
            ChunkedInputStream.exhaustInputStream(this);
         } finally {
            this.closed = true;
         }
      }

   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.pos >= this.contentLength) {
         return -1;
      } else {
         ++this.pos;
         return this.wrappedStream.read();
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.pos >= this.contentLength) {
         return -1;
      } else {
         if (this.pos + len > this.contentLength) {
            len = this.contentLength - this.pos;
         }

         int count = this.wrappedStream.read(b, off, len);
         this.pos += count;
         return count;
      }
   }

   public int read(byte[] b) throws IOException {
      return this.read(b, 0, b.length);
   }

   public long skip(long n) throws IOException {
      long length = Math.min(n, (long)(this.contentLength - this.pos));
      length = this.wrappedStream.skip(length);
      if (length > 0L) {
         this.pos = (int)((long)this.pos + length);
      }

      return length;
   }
}
