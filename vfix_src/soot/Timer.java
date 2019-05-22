package soot;

import java.util.Iterator;
import soot.options.Options;

public class Timer {
   private long duration;
   private long startTime;
   private boolean hasStarted;
   private String name;

   public Timer(String name) {
      this.name = name;
      this.duration = 0L;
   }

   public Timer() {
      this("unnamed");
   }

   static void doGarbageCollecting() {
      G g = G.v();
      if (!g.Timer_isGarbageCollecting) {
         if (Options.v().subtract_gc()) {
            if (g.Timer_count++ % 4 == 0) {
               g.Timer_isGarbageCollecting = true;
               g.Timer_forcedGarbageCollectionTimer.start();
               Iterator var1 = g.Timer_outstandingTimers.iterator();

               Timer t;
               while(var1.hasNext()) {
                  t = (Timer)var1.next();
                  t.end();
               }

               System.gc();
               var1 = g.Timer_outstandingTimers.iterator();

               while(var1.hasNext()) {
                  t = (Timer)var1.next();
                  t.start();
               }

               g.Timer_forcedGarbageCollectionTimer.end();
               g.Timer_isGarbageCollecting = false;
            }
         }
      }
   }

   public void start() {
      doGarbageCollecting();
      this.startTime = System.nanoTime();
      if (this.hasStarted) {
         throw new RuntimeException("timer " + this.name + " has already been started!");
      } else {
         this.hasStarted = true;
         if (!G.v().Timer_isGarbageCollecting) {
            synchronized(G.v().Timer_outstandingTimers) {
               G.v().Timer_outstandingTimers.add(this);
            }
         }

      }
   }

   public String toString() {
      return this.name;
   }

   public void end() {
      if (!this.hasStarted) {
         throw new RuntimeException("timer " + this.name + " has not been started!");
      } else {
         this.hasStarted = false;
         this.duration += System.nanoTime() - this.startTime;
         if (!G.v().Timer_isGarbageCollecting) {
            synchronized(G.v().Timer_outstandingTimers) {
               G.v().Timer_outstandingTimers.remove(this);
            }
         }

      }
   }

   public long getTime() {
      return this.duration / 1000000L;
   }
}
