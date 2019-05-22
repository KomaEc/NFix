package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PumpStreamHandler implements ExecuteStreamHandler {
   private Thread outputThread;
   private Thread errorThread;
   private StreamPumper inputPump;
   private OutputStream out;
   private OutputStream err;
   private InputStream input;

   public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input) {
      this.out = out;
      this.err = err;
      this.input = input;
   }

   public PumpStreamHandler(OutputStream out, OutputStream err) {
      this(out, err, (InputStream)null);
   }

   public PumpStreamHandler(OutputStream outAndErr) {
      this(outAndErr, outAndErr);
   }

   public PumpStreamHandler() {
      this(System.out, System.err);
   }

   public void setProcessOutputStream(InputStream is) {
      this.createProcessOutputPump(is, this.out);
   }

   public void setProcessErrorStream(InputStream is) {
      if (this.err != null) {
         this.createProcessErrorPump(is, this.err);
      }

   }

   public void setProcessInputStream(OutputStream os) {
      if (this.input != null) {
         this.inputPump = this.createInputPump(this.input, os, true);
      } else {
         try {
            os.close();
         } catch (IOException var3) {
         }
      }

   }

   public void start() {
      this.outputThread.start();
      this.errorThread.start();
      if (this.inputPump != null) {
         Thread inputThread = new Thread(this.inputPump);
         inputThread.setDaemon(true);
         inputThread.start();
      }

   }

   public void stop() {
      try {
         this.outputThread.join();
      } catch (InterruptedException var5) {
      }

      try {
         this.errorThread.join();
      } catch (InterruptedException var4) {
      }

      if (this.inputPump != null) {
         this.inputPump.stop();
      }

      try {
         this.err.flush();
      } catch (IOException var3) {
      }

      try {
         this.out.flush();
      } catch (IOException var2) {
      }

   }

   protected OutputStream getErr() {
      return this.err;
   }

   protected OutputStream getOut() {
      return this.out;
   }

   protected void createProcessOutputPump(InputStream is, OutputStream os) {
      this.outputThread = this.createPump(is, os);
   }

   protected void createProcessErrorPump(InputStream is, OutputStream os) {
      this.errorThread = this.createPump(is, os);
   }

   protected Thread createPump(InputStream is, OutputStream os) {
      return this.createPump(is, os, false);
   }

   protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted) {
      Thread result = new Thread(new StreamPumper(is, os, closeWhenExhausted));
      result.setDaemon(true);
      return result;
   }

   StreamPumper createInputPump(InputStream is, OutputStream os, boolean closeWhenExhausted) {
      StreamPumper pumper = new StreamPumper(is, os, closeWhenExhausted);
      pumper.setAutoflush(true);
      return pumper;
   }
}
