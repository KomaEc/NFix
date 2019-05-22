package org.apache.commons.httpclient.util;

public final class TimeoutController {
   private TimeoutController() {
   }

   public static void execute(Thread task, long timeout) throws TimeoutController.TimeoutException {
      task.start();

      try {
         task.join(timeout);
      } catch (InterruptedException var4) {
      }

      if (task.isAlive()) {
         task.interrupt();
         throw new TimeoutController.TimeoutException();
      }
   }

   public static void execute(Runnable task, long timeout) throws TimeoutController.TimeoutException {
      Thread t = new Thread(task, "Timeout guard");
      t.setDaemon(true);
      execute(t, timeout);
   }

   public static class TimeoutException extends Exception {
   }
}
