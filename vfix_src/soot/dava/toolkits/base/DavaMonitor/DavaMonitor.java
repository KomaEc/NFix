package soot.dava.toolkits.base.DavaMonitor;

import java.util.HashMap;

public class DavaMonitor {
   private static final DavaMonitor instance = new DavaMonitor();
   private final HashMap<Object, Lock> ref = new HashMap(1, 0.7F);
   private final HashMap<Object, Lock> lockTable = new HashMap(1, 0.7F);

   private DavaMonitor() {
   }

   public static DavaMonitor v() {
      return instance;
   }

   public synchronized void enter(Object o) throws NullPointerException {
      Thread currentThread = Thread.currentThread();
      if (o == null) {
         throw new NullPointerException();
      } else {
         Lock lock = (Lock)this.ref.get(o);
         if (lock == null) {
            lock = new Lock();
            this.ref.put(o, lock);
         }

         if (lock.level == 0) {
            lock.level = 1;
            lock.owner = currentThread;
         } else if (lock.owner == currentThread) {
            ++lock.level;
         } else {
            this.lockTable.put(currentThread, lock);
            lock.enQ(currentThread);

            while(lock.level > 0 || lock.nextThread() != currentThread) {
               try {
                  this.wait();
               } catch (InterruptedException var5) {
                  throw new RuntimeException(var5);
               }

               currentThread = Thread.currentThread();
               lock = (Lock)this.lockTable.get(currentThread);
            }

            lock.deQ(currentThread);
            lock.level = 1;
            lock.owner = currentThread;
         }
      }
   }

   public synchronized void exit(Object o) throws NullPointerException, IllegalMonitorStateException {
      if (o == null) {
         throw new NullPointerException();
      } else {
         Lock lock = (Lock)this.ref.get(o);
         if (lock != null && lock.level != 0 && lock.owner == Thread.currentThread()) {
            --lock.level;
            if (lock.level == 0) {
               this.notifyAll();
            }

         } else {
            throw new IllegalMonitorStateException();
         }
      }
   }
}
