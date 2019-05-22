package org.codehaus.plexus.util.cli;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import org.codehaus.plexus.util.IOUtil;

public class StreamPumper extends Thread {
   private BufferedReader in;
   private StreamConsumer consumer;
   private PrintWriter out;
   private static final int SIZE = 1024;
   boolean done;

   public StreamPumper(InputStream in) {
      this.consumer = null;
      this.out = null;
      this.in = new BufferedReader(new InputStreamReader(in), 1024);
   }

   public StreamPumper(InputStream in, StreamConsumer consumer) {
      this(in);
      this.consumer = consumer;
   }

   public StreamPumper(InputStream in, PrintWriter writer) {
      this(in);
      this.out = writer;
   }

   public StreamPumper(InputStream in, PrintWriter writer, StreamConsumer consumer) {
      this(in);
      this.out = writer;
      this.consumer = consumer;
   }

   public void run() {
      boolean var12 = false;

      label112: {
         try {
            var12 = true;

            for(String s = this.in.readLine(); s != null; s = this.in.readLine()) {
               this.consumeLine(s);
               if (this.out != null) {
                  this.out.println(s);
                  this.out.flush();
               }
            }

            var12 = false;
            break label112;
         } catch (Throwable var16) {
            var12 = false;
         } finally {
            if (var12) {
               IOUtil.close((Reader)this.in);
               this.done = true;
               synchronized(this) {
                  this.notifyAll();
               }
            }
         }

         IOUtil.close((Reader)this.in);
         this.done = true;
         synchronized(this) {
            this.notifyAll();
            return;
         }
      }

      IOUtil.close((Reader)this.in);
      this.done = true;
      synchronized(this) {
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

   public boolean isDone() {
      return this.done;
   }

   private void consumeLine(String line) {
      if (this.consumer != null) {
         this.consumer.consumeLine(line);
      }

   }
}
