package ch.ethz.ssh2.util;

import ch.ethz.ssh2.log.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;

public class TimeoutService {
   private static final Logger log;
   private static final LinkedList todolist;
   private static Thread timeoutThread;
   // $FF: synthetic field
   static Class class$0;

   static {
      Class var10000 = class$0;
      if (var10000 == null) {
         try {
            var10000 = Class.forName("ch.ethz.ssh2.util.TimeoutService");
         } catch (ClassNotFoundException var0) {
            throw new NoClassDefFoundError(var0.getMessage());
         }

         class$0 = var10000;
      }

      log = Logger.getLogger(var10000);
      todolist = new LinkedList();
      timeoutThread = null;
   }

   public static final TimeoutService.TimeoutToken addTimeoutHandler(long runTime, Runnable handler) {
      TimeoutService.TimeoutToken token = new TimeoutService.TimeoutToken(runTime, handler, (TimeoutService.TimeoutToken)null);
      synchronized(todolist) {
         todolist.add(token);
         Collections.sort(todolist);
         if (timeoutThread != null) {
            timeoutThread.interrupt();
         } else {
            timeoutThread = new TimeoutService.TimeoutThread((TimeoutService.TimeoutThread)null);
            timeoutThread.setDaemon(true);
            timeoutThread.start();
         }

         return token;
      }
   }

   public static final void cancelTimeoutHandler(TimeoutService.TimeoutToken token) {
      synchronized(todolist) {
         todolist.remove(token);
         if (timeoutThread != null) {
            timeoutThread.interrupt();
         }

      }
   }

   public static class TimeoutToken implements Comparable {
      private long runTime;
      private Runnable handler;

      private TimeoutToken(long runTime, Runnable handler) {
         this.runTime = runTime;
         this.handler = handler;
      }

      public int compareTo(Object o) {
         TimeoutService.TimeoutToken t = (TimeoutService.TimeoutToken)o;
         if (this.runTime > t.runTime) {
            return 1;
         } else {
            return this.runTime == t.runTime ? 0 : -1;
         }
      }

      // $FF: synthetic method
      TimeoutToken(long var1, Runnable var3, TimeoutService.TimeoutToken var4) {
         this(var1, var3);
      }
   }

   private static class TimeoutThread extends Thread {
      private TimeoutThread() {
      }

      public void run() {
         synchronized(TimeoutService.todolist) {
            while(TimeoutService.todolist.size() != 0) {
               long now = System.currentTimeMillis();
               TimeoutService.TimeoutToken tt = (TimeoutService.TimeoutToken)TimeoutService.todolist.getFirst();
               if (tt.runTime > now) {
                  try {
                     TimeoutService.todolist.wait(tt.runTime - now);
                  } catch (InterruptedException var7) {
                  }
               } else {
                  TimeoutService.todolist.removeFirst();

                  try {
                     tt.handler.run();
                  } catch (Exception var8) {
                     StringWriter sw = new StringWriter();
                     var8.printStackTrace(new PrintWriter(sw));
                     TimeoutService.log.log(20, "Exeception in Timeout handler:" + var8.getMessage() + "(" + sw.toString() + ")");
                  }
               }
            }

            TimeoutService.timeoutThread = null;
         }
      }

      // $FF: synthetic method
      TimeoutThread(TimeoutService.TimeoutThread var1) {
         this();
      }
   }
}
