package org.apache.maven.surefire.providerapi;

public abstract class AbstractProvider implements SurefireProvider {
   private final Thread creatingThread = Thread.currentThread();

   public void cancel() {
      synchronized(this.creatingThread) {
         if (this.creatingThread.isAlive()) {
            this.creatingThread.interrupt();
         }

      }
   }
}
