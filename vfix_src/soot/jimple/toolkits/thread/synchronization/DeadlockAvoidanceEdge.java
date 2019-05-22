package soot.jimple.toolkits.thread.synchronization;

import soot.SootClass;

public class DeadlockAvoidanceEdge extends NewStaticLock {
   public DeadlockAvoidanceEdge(SootClass sc) {
      super(sc);
   }

   public Object clone() {
      return new DeadlockAvoidanceEdge(this.sc);
   }

   public boolean equals(Object c) {
      if (c instanceof DeadlockAvoidanceEdge) {
         return ((DeadlockAvoidanceEdge)c).idnum == this.idnum;
      } else {
         return false;
      }
   }

   public String toString() {
      return "dae<" + this.sc.toString() + ">";
   }
}
