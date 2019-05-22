package org.apache.maven.plugin.surefire.booterclient.output;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.StreamConsumer;

public class ThreadedStreamConsumer implements StreamConsumer {
   private final BlockingQueue<String> items = new LinkedBlockingQueue();
   private static final String poison = "Pioson";
   private final Thread thread;
   private final ThreadedStreamConsumer.Pumper pumper;

   public ThreadedStreamConsumer(StreamConsumer target) {
      this.pumper = new ThreadedStreamConsumer.Pumper(this.items, target);
      this.thread = new Thread(this.pumper, "ThreadedStreamConsumer");
      this.thread.start();
   }

   public void consumeLine(String s) {
      this.items.add(s);
      if (this.items.size() > 10000) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
         }
      }

   }

   public void close() {
      try {
         this.items.add("Pioson");
         this.thread.join();
      } catch (InterruptedException var2) {
         throw new RuntimeException(var2);
      }

      if (this.pumper.getThrowable() != null) {
         throw new RuntimeException(this.pumper.getThrowable());
      }
   }

   static class Pumper implements Runnable {
      private final BlockingQueue<String> queue;
      private final StreamConsumer target;
      private volatile Throwable throwable;

      Pumper(BlockingQueue<String> queue, StreamConsumer target) {
         this.queue = queue;
         this.target = target;
      }

      public void run() {
         try {
            for(String item = (String)this.queue.take(); item != "Pioson"; item = (String)this.queue.take()) {
               this.target.consumeLine(item);
            }
         } catch (Throwable var2) {
            this.throwable = var2;
         }

      }

      public Throwable getThrowable() {
         return this.throwable;
      }
   }
}
