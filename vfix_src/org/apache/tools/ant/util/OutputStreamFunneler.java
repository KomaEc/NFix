package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamFunneler {
   public static final long DEFAULT_TIMEOUT_MILLIS = 1000L;
   private OutputStream out;
   private int count;
   private boolean closed;
   private long timeoutMillis;

   public OutputStreamFunneler(OutputStream out) {
      this(out, 1000L);
   }

   public OutputStreamFunneler(OutputStream out, long timeoutMillis) {
      this.count = 0;
      if (out == null) {
         throw new IllegalArgumentException("OutputStreamFunneler.<init>:  out == null");
      } else {
         this.out = out;
         this.closed = false;
         this.setTimeout(timeoutMillis);
      }
   }

   public synchronized void setTimeout(long timeoutMillis) {
      this.timeoutMillis = timeoutMillis;
   }

   public synchronized OutputStream getFunnelInstance() throws IOException {
      this.dieIfClosed();

      OutputStreamFunneler.Funnel var1;
      try {
         var1 = new OutputStreamFunneler.Funnel();
      } finally {
         this.notifyAll();
      }

      return var1;
   }

   private synchronized void release(OutputStreamFunneler.Funnel funnel) throws IOException {
      if (!funnel.closed) {
         try {
            if (this.timeoutMillis > 0L) {
               try {
                  this.wait(this.timeoutMillis);
               } catch (InterruptedException var6) {
               }
            }

            if (--this.count == 0) {
               this.close();
            }
         } finally {
            funnel.closed = true;
         }
      }

   }

   private synchronized void close() throws IOException {
      try {
         this.dieIfClosed();
         this.out.close();
      } finally {
         this.closed = true;
      }

   }

   private synchronized void dieIfClosed() throws IOException {
      if (this.closed) {
         throw new IOException("The funneled OutputStream has been closed.");
      }
   }

   private final class Funnel extends OutputStream {
      private boolean closed;

      private Funnel() {
         this.closed = false;
         synchronized(OutputStreamFunneler.this) {
            ++OutputStreamFunneler.this.count;
         }
      }

      public void flush() throws IOException {
         synchronized(OutputStreamFunneler.this) {
            OutputStreamFunneler.this.dieIfClosed();
            OutputStreamFunneler.this.out.flush();
         }
      }

      public void write(int b) throws IOException {
         synchronized(OutputStreamFunneler.this) {
            OutputStreamFunneler.this.dieIfClosed();
            OutputStreamFunneler.this.out.write(b);
         }
      }

      public void write(byte[] b) throws IOException {
         synchronized(OutputStreamFunneler.this) {
            OutputStreamFunneler.this.dieIfClosed();
            OutputStreamFunneler.this.out.write(b);
         }
      }

      public void write(byte[] b, int off, int len) throws IOException {
         synchronized(OutputStreamFunneler.this) {
            OutputStreamFunneler.this.dieIfClosed();
            OutputStreamFunneler.this.out.write(b, off, len);
         }
      }

      public void close() throws IOException {
         OutputStreamFunneler.this.release(this);
      }

      // $FF: synthetic method
      Funnel(Object x1) {
         this();
      }
   }
}
