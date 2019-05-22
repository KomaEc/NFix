package soot.jimple.spark.geom.geomPA;

import java.util.Deque;
import java.util.LinkedList;

public class FIFO_Worklist implements IWorklist {
   Deque<IVarAbstraction> Q = null;

   public void initialize(int size) {
      this.Q = new LinkedList();
   }

   public boolean has_job() {
      return this.Q.size() != 0;
   }

   public IVarAbstraction next() {
      IVarAbstraction t = (IVarAbstraction)this.Q.getFirst();
      this.Q.removeFirst();
      t.Qpos = 0;
      return t;
   }

   public void push(IVarAbstraction pv) {
      if (pv.Qpos == 0) {
         this.Q.addLast(pv);
         pv.Qpos = 1;
      }

   }

   public int size() {
      return this.Q.size();
   }

   public void clear() {
      this.Q = null;
   }
}
