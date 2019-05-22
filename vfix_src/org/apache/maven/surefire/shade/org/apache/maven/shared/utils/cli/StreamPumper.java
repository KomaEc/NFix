package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.IOUtil;

public class StreamPumper extends AbstractStreamHandler {
   private final BufferedReader in;
   private final StreamConsumer consumer;
   private final PrintWriter out;
   private volatile Exception exception;
   private static final int SIZE = 1024;

   public StreamPumper(InputStream in, StreamConsumer consumer) {
      this(in, (PrintWriter)null, consumer);
   }

   private StreamPumper(InputStream in, PrintWriter writer, StreamConsumer consumer) {
      this.exception = null;
      this.in = new BufferedReader(new InputStreamReader(in), 1024);
      this.out = writer;
      this.consumer = consumer;
   }

   public void run() {
      boolean var14 = false;

      label126: {
         try {
            var14 = true;

            for(String line = this.in.readLine(); line != null; line = this.in.readLine()) {
               try {
                  if (this.exception == null) {
                     this.consumeLine(line);
                  }
               } catch (Exception var18) {
                  this.exception = var18;
               }

               if (this.out != null) {
                  this.out.println(line);
                  this.out.flush();
               }
            }

            var14 = false;
            break label126;
         } catch (IOException var19) {
            this.exception = var19;
            var14 = false;
         } finally {
            if (var14) {
               IOUtil.close((Reader)this.in);
               synchronized(this) {
                  this.setDone();
                  this.notifyAll();
               }
            }
         }

         IOUtil.close((Reader)this.in);
         synchronized(this) {
            this.setDone();
            this.notifyAll();
            return;
         }
      }

      IOUtil.close((Reader)this.in);
      synchronized(this) {
         this.setDone();
         this.notifyAll();
      }

   }

   public void flush() {
      if (this.out != null) {
         this.out.flush();
      }

   }

   public void close() {
      IOUtil.close((Writer)this.out);
   }

   public Exception getException() {
      return this.exception;
   }

   private void consumeLine(String line) {
      if (this.consumer != null && !this.isDisabled()) {
         this.consumer.consumeLine(line);
      }

   }
}
