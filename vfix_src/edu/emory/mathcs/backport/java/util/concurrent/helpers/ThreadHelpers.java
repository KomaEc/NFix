package edu.emory.mathcs.backport.java.util.concurrent.helpers;

public class ThreadHelpers {
   private ThreadHelpers() {
   }

   public static Runnable assignExceptionHandler(final Runnable runnable, final ThreadHelpers.UncaughtExceptionHandler handler) {
      if (runnable != null && handler != null) {
         return new Runnable() {
            public void run() {
               try {
                  runnable.run();
               } catch (Throwable var4) {
                  Throwable error = var4;

                  try {
                     handler.uncaughtException(Thread.currentThread(), error);
                  } catch (Throwable var3) {
                  }
               }

            }
         };
      } else {
         throw new NullPointerException();
      }
   }

   public interface UncaughtExceptionHandler {
      void uncaughtException(Thread var1, Throwable var2);
   }
}
