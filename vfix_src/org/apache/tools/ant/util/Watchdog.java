package org.apache.tools.ant.util;

import java.util.Enumeration;
import java.util.Vector;

public class Watchdog implements Runnable {
   private Vector observers = new Vector(1);
   private long timeout = -1L;
   private volatile boolean stopped = false;
   public static final String ERROR_INVALID_TIMEOUT = "timeout less than 1.";

   public Watchdog(long timeout) {
      if (timeout < 1L) {
         throw new IllegalArgumentException("timeout less than 1.");
      } else {
         this.timeout = timeout;
      }
   }

   public void addTimeoutObserver(TimeoutObserver to) {
      this.observers.addElement(to);
   }

   public void removeTimeoutObserver(TimeoutObserver to) {
      this.observers.removeElement(to);
   }

   protected final void fireTimeoutOccured() {
      Enumeration e = this.observers.elements();

      while(e.hasMoreElements()) {
         ((TimeoutObserver)e.nextElement()).timeoutOccured(this);
      }

   }

   public synchronized void start() {
      this.stopped = false;
      Thread t = new Thread(this, "WATCHDOG");
      t.setDaemon(true);
      t.start();
   }

   public synchronized void stop() {
      this.stopped = true;
      this.notifyAll();
   }

   public synchronized void run() {
      long until = System.currentTimeMillis() + this.timeout;

      long now;
      while(!this.stopped && until > (now = System.currentTimeMillis())) {
         try {
            this.wait(until - now);
         } catch (InterruptedException var6) {
         }
      }

      if (!this.stopped) {
         this.fireTimeoutOccured();
      }

   }
}
