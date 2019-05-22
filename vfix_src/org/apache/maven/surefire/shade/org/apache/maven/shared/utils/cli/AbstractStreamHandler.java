package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

class AbstractStreamHandler extends Thread {
   private boolean done;
   private volatile boolean disabled;

   boolean isDone() {
      return this.done;
   }

   public synchronized void waitUntilDone() throws InterruptedException {
      while(!this.isDone()) {
         this.wait();
      }

   }

   boolean isDisabled() {
      return this.disabled;
   }

   public void disable() {
      this.disabled = true;
   }

   protected void setDone() {
      this.done = true;
   }
}
