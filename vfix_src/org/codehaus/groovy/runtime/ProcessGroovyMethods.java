package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.GroovyRuntimeException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;

public class ProcessGroovyMethods extends DefaultGroovyMethodsSupport {
   public static InputStream getIn(Process self) {
      return self.getInputStream();
   }

   public static String getText(Process self) throws IOException {
      return DefaultGroovyMethods.getText(new BufferedReader(new InputStreamReader(self.getInputStream())));
   }

   public static InputStream getErr(Process self) {
      return self.getErrorStream();
   }

   public static OutputStream getOut(Process self) {
      return self.getOutputStream();
   }

   public static Writer leftShift(Process self, Object value) throws IOException {
      return DefaultGroovyMethods.leftShift(self.getOutputStream(), value);
   }

   public static OutputStream leftShift(Process self, byte[] value) throws IOException {
      return DefaultGroovyMethods.leftShift(self.getOutputStream(), value);
   }

   public static void waitForOrKill(Process self, long numberOfMillis) {
      ProcessGroovyMethods.ProcessRunner runnable = new ProcessGroovyMethods.ProcessRunner(self);
      Thread thread = new Thread(runnable);
      thread.start();
      runnable.waitForOrKill(numberOfMillis);
   }

   public static void consumeProcessOutput(Process self) {
      consumeProcessOutput(self, (OutputStream)null, (OutputStream)null);
   }

   public static void consumeProcessOutput(Process self, Appendable output, Appendable error) {
      consumeProcessOutputStream(self, output);
      consumeProcessErrorStream(self, error);
   }

   public static void consumeProcessOutput(Process self, OutputStream output, OutputStream error) {
      consumeProcessOutputStream(self, output);
      consumeProcessErrorStream(self, error);
   }

   public static void waitForProcessOutput(Process self) {
      waitForProcessOutput(self, (OutputStream)null, (OutputStream)null);
   }

   public static void waitForProcessOutput(Process self, Appendable output, Appendable error) {
      Thread tout = consumeProcessOutputStream(self, output);
      Thread terr = consumeProcessErrorStream(self, error);

      try {
         tout.join();
      } catch (InterruptedException var8) {
      }

      try {
         terr.join();
      } catch (InterruptedException var7) {
      }

      try {
         self.waitFor();
      } catch (InterruptedException var6) {
      }

   }

   public static void waitForProcessOutput(Process self, OutputStream output, OutputStream error) {
      Thread tout = consumeProcessOutputStream(self, output);
      Thread terr = consumeProcessErrorStream(self, error);

      try {
         tout.join();
      } catch (InterruptedException var8) {
      }

      try {
         terr.join();
      } catch (InterruptedException var7) {
      }

      try {
         self.waitFor();
      } catch (InterruptedException var6) {
      }

   }

   public static Thread consumeProcessErrorStream(Process self, OutputStream err) {
      Thread thread = new Thread(new ProcessGroovyMethods.ByteDumper(self.getErrorStream(), err));
      thread.start();
      return thread;
   }

   public static Thread consumeProcessErrorStream(Process self, Appendable error) {
      Thread thread = new Thread(new ProcessGroovyMethods.TextDumper(self.getErrorStream(), error));
      thread.start();
      return thread;
   }

   public static Thread consumeProcessOutputStream(Process self, Appendable output) {
      Thread thread = new Thread(new ProcessGroovyMethods.TextDumper(self.getInputStream(), output));
      thread.start();
      return thread;
   }

   public static Thread consumeProcessOutputStream(Process self, OutputStream output) {
      Thread thread = new Thread(new ProcessGroovyMethods.ByteDumper(self.getInputStream(), output));
      thread.start();
      return thread;
   }

   public static void withWriter(final Process self, final Closure closure) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               DefaultGroovyMethods.withWriter((OutputStream)(new BufferedOutputStream(ProcessGroovyMethods.getOut(self))), closure);
            } catch (IOException var2) {
               throw new GroovyRuntimeException("exception while reading process stream", var2);
            }
         }
      })).start();
   }

   public static void withOutputStream(final Process self, final Closure closure) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               DefaultGroovyMethods.withStream((OutputStream)(new BufferedOutputStream(ProcessGroovyMethods.getOut(self))), closure);
            } catch (IOException var2) {
               throw new GroovyRuntimeException("exception while reading process stream", var2);
            }
         }
      })).start();
   }

   public static Process pipeTo(final Process left, final Process right) throws IOException {
      (new Thread(new Runnable() {
         public void run() {
            InputStream in = new BufferedInputStream(ProcessGroovyMethods.getIn(left));
            OutputStream out = new BufferedOutputStream(ProcessGroovyMethods.getOut(right));
            byte[] buf = new byte[8192];

            try {
               int next;
               try {
                  while((next = in.read(buf)) != -1) {
                     out.write(buf, 0, next);
                  }
               } catch (IOException var9) {
                  throw new GroovyRuntimeException("exception while reading process stream", var9);
               }
            } finally {
               DefaultGroovyMethodsSupport.closeWithWarning(out);
            }

         }
      })).start();
      return right;
   }

   public static Process or(Process left, Process right) throws IOException {
      return pipeTo(left, right);
   }

   private static class ByteDumper implements Runnable {
      InputStream in;
      OutputStream out;

      public ByteDumper(InputStream in, OutputStream out) {
         this.in = new BufferedInputStream(in);
         this.out = out;
      }

      public void run() {
         byte[] buf = new byte[8192];

         try {
            int next;
            while((next = this.in.read(buf)) != -1) {
               if (this.out != null) {
                  this.out.write(buf, 0, next);
               }
            }

         } catch (IOException var4) {
            throw new GroovyRuntimeException("exception while dumping process stream", var4);
         }
      }
   }

   private static class TextDumper implements Runnable {
      InputStream in;
      Appendable app;

      public TextDumper(InputStream in, Appendable app) {
         this.in = in;
         this.app = app;
      }

      public void run() {
         InputStreamReader isr = new InputStreamReader(this.in);
         BufferedReader br = new BufferedReader(isr);

         try {
            String next;
            while((next = br.readLine()) != null) {
               if (this.app != null) {
                  this.app.append(next);
                  this.app.append("\n");
               }
            }

         } catch (IOException var5) {
            throw new GroovyRuntimeException("exception while reading process stream", var5);
         }
      }
   }

   protected static class ProcessRunner implements Runnable {
      Process process;
      private boolean finished;

      public ProcessRunner(Process process) {
         this.process = process;
      }

      private void doProcessWait() {
         try {
            this.process.waitFor();
         } catch (InterruptedException var2) {
         }

      }

      public void run() {
         this.doProcessWait();
         synchronized(this) {
            this.notifyAll();
            this.finished = true;
         }
      }

      public synchronized void waitForOrKill(long millis) {
         if (!this.finished) {
            try {
               this.wait(millis);
            } catch (InterruptedException var4) {
            }

            if (!this.finished) {
               this.process.destroy();
               this.doProcessWait();
            }
         }

      }
   }
}
