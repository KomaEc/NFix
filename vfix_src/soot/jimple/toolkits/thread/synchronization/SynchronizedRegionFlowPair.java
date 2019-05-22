package soot.jimple.toolkits.thread.synchronization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SynchronizedRegionFlowPair {
   private static final Logger logger = LoggerFactory.getLogger(SynchronizedRegionFlowPair.class);
   public CriticalSection tn;
   public boolean inside;

   SynchronizedRegionFlowPair(CriticalSection tn, boolean inside) {
      this.tn = tn;
      this.inside = inside;
   }

   SynchronizedRegionFlowPair(SynchronizedRegionFlowPair tfp) {
      this.tn = tfp.tn;
      this.inside = tfp.inside;
   }

   public void copy(SynchronizedRegionFlowPair tfp) {
      tfp.tn = this.tn;
      tfp.inside = this.inside;
   }

   public SynchronizedRegionFlowPair clone() {
      return new SynchronizedRegionFlowPair(this.tn, this.inside);
   }

   public boolean equals(Object other) {
      if (other instanceof SynchronizedRegionFlowPair) {
         SynchronizedRegionFlowPair tfp = (SynchronizedRegionFlowPair)other;
         if (this.tn.IDNum == tfp.tn.IDNum) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      return "[" + (this.inside ? "in," : "out,") + this.tn.toString() + "]";
   }
}
