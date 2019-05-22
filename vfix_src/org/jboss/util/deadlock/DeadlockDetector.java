package org.jboss.util.deadlock;

import java.util.HashMap;
import java.util.HashSet;

public class DeadlockDetector {
   public static DeadlockDetector singleton = new DeadlockDetector();
   protected HashMap waiting = new HashMap();

   public void deadlockDetection(Object holder, Resource resource) throws ApplicationDeadlockException {
      HashSet set = new HashSet();
      set.add(holder);
      Object checkHolder = resource.getResourceHolder();
      synchronized(this.waiting) {
         this.addWaiting(holder, resource);

         Object holding;
         for(; checkHolder != null; checkHolder = holding) {
            Resource waitingFor = (Resource)this.waiting.get(checkHolder);
            holding = null;
            if (waitingFor != null) {
               holding = waitingFor.getResourceHolder();
            }

            if (holding != null) {
               if (set.contains(holding)) {
                  String msg = "Application deadlock detected, resource=" + resource + ", holder=" + holder + ", waitingResource=" + waitingFor + ", waitingResourceHolder=" + holding;
                  throw new ApplicationDeadlockException(msg, true);
               }

               set.add(holding);
            }
         }

      }
   }

   public void addWaiting(Object holder, Resource resource) {
      synchronized(this.waiting) {
         this.waiting.put(holder, resource);
      }
   }

   public void removeWaiting(Object holder) {
      synchronized(this.waiting) {
         this.waiting.remove(holder);
      }
   }
}
