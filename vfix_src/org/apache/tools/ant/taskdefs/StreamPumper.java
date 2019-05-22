package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPumper implements Runnable {
   private InputStream is;
   private OutputStream os;
   private volatile boolean finish;
   private volatile boolean finished;
   private boolean closeWhenExhausted;
   private boolean autoflush;
   private Exception exception;
   private int bufferSize;
   private boolean started;

   public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted) {
      this.autoflush = false;
      this.exception = null;
      this.bufferSize = 128;
      this.started = false;
      this.is = is;
      this.os = os;
      this.closeWhenExhausted = closeWhenExhausted;
   }

   public StreamPumper(InputStream is, OutputStream os) {
      this(is, os, false);
   }

   void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   public void run() {
      synchronized(this) {
         this.started = true;
      }

      this.finished = false;
      this.finish = false;
      byte[] buf = new byte[this.bufferSize];

      while(true) {
         boolean var20 = false;

         label190: {
            try {
               var20 = true;
               int length;
               if ((length = this.is.read(buf)) > 0 && !this.finish) {
                  this.os.write(buf, 0, length);
                  if (this.autoflush) {
                     this.os.flush();
                  }
                  continue;
               }

               this.os.flush();
               var20 = false;
               break label190;
            } catch (Exception var29) {
               Exception e = var29;
               synchronized(this) {
                  this.exception = e;
                  var20 = false;
               }
            } finally {
               if (var20) {
                  if (this.closeWhenExhausted) {
                     try {
                        this.os.close();
                     } catch (IOException var22) {
                     }
                  }

                  this.finished = true;
                  synchronized(this) {
                     this.notifyAll();
                  }
               }
            }

            if (this.closeWhenExhausted) {
               try {
                  this.os.close();
               } catch (IOException var24) {
               }
            }

            this.finished = true;
            synchronized(this) {
               this.notifyAll();
               break;
            }
         }

         if (this.closeWhenExhausted) {
            try {
               this.os.close();
            } catch (IOException var27) {
            }
         }

         this.finished = true;
         synchronized(this) {
            this.notifyAll();
            break;
         }
      }

   }

   public boolean isFinished() {
      return this.finished;
   }

   public synchronized void waitFor() throws InterruptedException {
      while(!this.isFinished()) {
         this.wait();
      }

   }

   public synchronized void setBufferSize(int bufferSize) {
      if (this.started) {
         throw new IllegalStateException("Cannot set buffer size on a running StreamPumper");
      } else {
         this.bufferSize = bufferSize;
      }
   }

   public synchronized int getBufferSize() {
      return this.bufferSize;
   }

   public synchronized Exception getException() {
      return this.exception;
   }

   synchronized void stop() {
      this.finish = true;
      this.notifyAll();
   }
}
