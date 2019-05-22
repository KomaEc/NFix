package soot.jimple.spark.geom.geomPA;

public class PQ_Worklist implements IWorklist {
   private IVarAbstraction[] heap = null;
   int cur_tail = 0;

   public void initialize(int size) {
      this.heap = new IVarAbstraction[size];
      this.cur_tail = 1;
   }

   public boolean has_job() {
      return this.cur_tail > 1;
   }

   public IVarAbstraction next() {
      IVarAbstraction ret = this.heap[1];
      --this.cur_tail;
      if (this.cur_tail > 1) {
         IVarAbstraction e = this.heap[this.cur_tail];

         int k;
         int kk;
         for(k = 1; k * 2 < this.cur_tail; k = kk) {
            kk = k * 2;
            if (kk + 1 < this.cur_tail && this.heap[kk + 1].lessThan(this.heap[kk])) {
               ++kk;
            }

            if (e.lessThan(this.heap[kk])) {
               break;
            }

            this.heap[k] = this.heap[kk];
            this.heap[k].Qpos = k;
         }

         e.Qpos = k;
         this.heap[k] = e;
      }

      ret.Qpos = 0;
      return ret;
   }

   public void push(IVarAbstraction e) {
      ++e.lrf_value;
      int k;
      int kk;
      if (e.Qpos == 0) {
         for(k = this.cur_tail; k > 1; k /= 2) {
            kk = k / 2;
            if (this.heap[kk].lessThan(e)) {
               break;
            }

            this.heap[k] = this.heap[kk];
            this.heap[k].Qpos = k;
         }

         e.Qpos = k;
         this.heap[k] = e;
         ++this.cur_tail;
      } else {
         for(k = e.Qpos; k * 2 < this.cur_tail; k = kk) {
            kk = k * 2;
            if (kk + 1 < this.cur_tail && this.heap[kk + 1].lessThan(this.heap[kk])) {
               ++kk;
            }

            if (e.lessThan(this.heap[kk])) {
               break;
            }

            this.heap[k] = this.heap[kk];
            this.heap[kk].Qpos = k;
         }

         e.Qpos = k;
         this.heap[k] = e;
      }

   }

   public int size() {
      return this.cur_tail - 1;
   }

   public void clear() {
      this.cur_tail = 1;
   }
}
