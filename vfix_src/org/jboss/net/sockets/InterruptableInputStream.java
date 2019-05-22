package org.jboss.net.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

public class InterruptableInputStream extends InputStream {
   private InputStream is;

   public InterruptableInputStream(InputStream is) {
      this.is = is;
   }

   public int read() throws IOException {
      byte[] b = new byte[0];
      int count = this.internalRead(b, 0, 1);
      return count > 0 ? b[0] : -1;
   }

   public int read(byte[] b) throws IOException {
      return this.internalRead(b, 0, b.length);
   }

   public int read(byte[] b, int off, int len) throws IOException {
      return this.internalRead(b, off, len);
   }

   public long skip(long n) throws IOException {
      return this.is.skip(n);
   }

   public int available() throws IOException {
      return this.is.available();
   }

   public void close() throws IOException {
      this.is.close();
   }

   public synchronized void mark(int readlimit) {
      this.is.mark(readlimit);
   }

   public synchronized void reset() throws IOException {
      this.is.reset();
   }

   public boolean markSupported() {
      return this.is.markSupported();
   }

   private int internalRead(byte[] b, int off, int len) throws IOException {
      boolean var4 = true;

      while(true) {
         try {
            int n = this.is.read(b, off, len);
            return n;
         } catch (SocketTimeoutException var6) {
            if (Thread.interrupted()) {
               throw var6;
            }
         }
      }
   }
}
