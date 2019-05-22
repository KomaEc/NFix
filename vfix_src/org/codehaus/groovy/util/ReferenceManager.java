package org.codehaus.groovy.util;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ReferenceManager {
   private ReferenceQueue queue;
   private static final ReferenceBundle SOFT_BUNDLE;
   private static final ReferenceBundle WEAK_BUNDLE;

   public static ReferenceManager createThreadedManager(ReferenceQueue queue) {
      return new ReferenceManager.ThreadedReferenceManager(queue);
   }

   public static ReferenceManager createIdlingManager(ReferenceQueue queue) {
      return new ReferenceManager(queue);
   }

   public static ReferenceManager createCallBackedManager(ReferenceQueue queue) {
      return new ReferenceManager(queue) {
         public void removeStallEntries() {
            ReferenceQueue queue = this.getReferenceQueue();

            while(true) {
               java.lang.ref.Reference r = queue.poll();
               if (r == null) {
                  return;
               }

               if (r instanceof Reference) {
                  Reference ref = (Reference)r;
                  Finalizable holder = ref.getHandler();
                  if (holder != null) {
                     holder.finalizeReference();
                  }
               }

               r.clear();
               r = null;
            }
         }

         public void afterReferenceCreation(Reference r) {
            this.removeStallEntries();
         }

         public String toString() {
            return "ReferenceManager(callback)";
         }
      };
   }

   public static ReferenceManager createThresholdedIdlingManager(final ReferenceQueue queue, final ReferenceManager callback, final int threshold) {
      if (threshold < 0) {
         throw new IllegalArgumentException("threshold must not be below 0.");
      } else {
         return new ReferenceManager(queue) {
            private AtomicInteger refCnt = new AtomicInteger();
            private volatile ReferenceManager manager = createIdlingManager(queue);

            public void afterReferenceCreation(Reference r) {
               if (this.manager == callback) {
                  callback.afterReferenceCreation(r);
               } else {
                  int count = this.refCnt.incrementAndGet();
                  if (count > threshold || count < 0) {
                     this.manager = callback;
                  }

               }
            }

            public String toString() {
               return "ReferenceManager(thresholded, current manager=" + this.manager + ", threshold=" + this.refCnt.get() + "/" + threshold + ")";
            }
         };
      }
   }

   public ReferenceManager(ReferenceQueue queue) {
      this.queue = queue;
   }

   protected ReferenceQueue getReferenceQueue() {
      return this.queue;
   }

   public void afterReferenceCreation(Reference r) {
   }

   public void removeStallEntries() {
   }

   public void stopThread() {
   }

   public String toString() {
      return "ReferenceManager(idling)";
   }

   public static ReferenceBundle getDefaultSoftBundle() {
      return SOFT_BUNDLE;
   }

   public static ReferenceBundle getDefaultWeakBundle() {
      return WEAK_BUNDLE;
   }

   static {
      ReferenceQueue queue = new ReferenceQueue();
      ReferenceManager callBack = createCallBackedManager(queue);
      ReferenceManager manager = createThresholdedIdlingManager(queue, callBack, 500);
      SOFT_BUNDLE = new ReferenceBundle(manager, ReferenceType.SOFT);
      WEAK_BUNDLE = new ReferenceBundle(manager, ReferenceType.WEAK);
   }

   private static class ThreadedReferenceManager extends ReferenceManager {
      private final Thread thread = new Thread() {
         public void run() {
            ReferenceQueue queue = ThreadedReferenceManager.this.getReferenceQueue();
            java.lang.ref.Reference r = null;

            while(ThreadedReferenceManager.this.shouldRun) {
               try {
                  r = queue.remove(1000L);
               } catch (InterruptedException var5) {
                  break;
               }

               if (r != null) {
                  if (r instanceof Reference) {
                     Reference ref = (Reference)r;
                     Finalizable holder = ref.getHandler();
                     if (holder != null) {
                        holder.finalizeReference();
                     }
                  }

                  r.clear();
                  r = null;
               }
            }

         }
      };
      private volatile boolean shouldRun = true;

      public ThreadedReferenceManager(ReferenceQueue queue) {
         super(queue);
         this.thread.setContextClassLoader((ClassLoader)null);
         this.thread.setDaemon(true);
         this.thread.setName(ReferenceManager.ThreadedReferenceManager.class.getName());
         this.thread.start();
      }

      public void stopThread() {
         this.shouldRun = false;
         this.thread.interrupt();
      }

      public String toString() {
         return "ReferenceManager(threaded, thread=" + this.thread + ")";
      }
   }
}
