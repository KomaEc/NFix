package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class StreamFeeder extends AbstractStreamHandler {
   private InputStream input;
   private OutputStream output;

   public StreamFeeder(InputStream input, OutputStream output) {
      this.input = input;
      this.output = output;
   }

   public void run() {
      boolean var12 = false;

      label87: {
         try {
            var12 = true;
            this.feed();
            var12 = false;
            break label87;
         } catch (Throwable var16) {
            var12 = false;
         } finally {
            if (var12) {
               this.close();
               synchronized(this) {
                  this.setDone();
                  this.notifyAll();
               }
            }
         }

         this.close();
         synchronized(this) {
            this.setDone();
            this.notifyAll();
            return;
         }
      }

      this.close();
      synchronized(this) {
         this.setDone();
         this.notifyAll();
      }

   }

   public void close() {
      if (this.input != null) {
         synchronized(this.input) {
            try {
               this.input.close();
            } catch (IOException var7) {
            }

            this.input = null;
         }
      }

      if (this.output != null) {
         synchronized(this.output) {
            try {
               this.output.close();
            } catch (IOException var5) {
            }

            this.output = null;
         }
      }

   }

   private void feed() throws IOException {
      int data = this.input.read();

      while(!this.isDone() && data != -1) {
         synchronized(this.output) {
            if (!this.isDisabled()) {
               this.output.write(data);
            }

            data = this.input.read();
         }
      }

   }
}
