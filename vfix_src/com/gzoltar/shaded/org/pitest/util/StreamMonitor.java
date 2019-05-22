package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class StreamMonitor extends Thread implements Monitor {
   private static final Logger LOG = Log.getLogger();
   private final byte[] buf = new byte[256];
   private final InputStream in;
   private final SideEffect1<String> inputHandler;

   public StreamMonitor(InputStream in, SideEffect1<String> inputHandler) {
      super("PIT Stream Monitor");
      this.in = in;
      this.inputHandler = inputHandler;
      this.setDaemon(true);
   }

   public void requestStart() {
      this.start();
   }

   public void run() {
      while(!this.isInterrupted()) {
         this.readFromStream();
      }

   }

   private void readFromStream() {
      try {
         if (this.in.available() == 0) {
            Thread.sleep(100L);
            return;
         }

         int i;
         while((i = this.in.read(this.buf, 0, this.buf.length)) != -1) {
            String output = new String(this.buf, 0, i);
            this.inputHandler.apply(output);
         }
      } catch (IOException var3) {
         this.requestStop();
         LOG.fine("No longer able to read stream.");
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
      }

   }

   public void requestStop() {
      this.interrupt();
   }
}
