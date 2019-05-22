package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;

public class IntervalContextVar extends ContextVar implements Comparable<IntervalContextVar> {
   public long L = 0L;
   public long R = 0L;

   public IntervalContextVar() {
   }

   public IntervalContextVar(long l, long r, Node v) {
      assert l < r;

      this.L = l;
      this.R = r;
      this.var = v;
   }

   public IntervalContextVar(IntervalContextVar o) {
      this.L = o.L;
      this.R = o.R;
      this.var = o.var;
   }

   public String toString() {
      return "<" + this.var.toString() + ", " + this.L + ", " + this.R + ">";
   }

   public boolean equals(Object o) {
      IntervalContextVar other = (IntervalContextVar)o;
      return other.L == this.L && other.R == this.R && other.var == this.var;
   }

   public int hashCode() {
      int ch = (int)((this.L + this.R) % 2147483647L);
      int ans = this.var.hashCode() + ch;
      if (ans < 0) {
         ans = this.var.hashCode();
      }

      return ans;
   }

   public int compareTo(IntervalContextVar o) {
      if (this.L == o.L) {
         return this.R < o.R ? -1 : 1;
      } else {
         return this.L < o.L ? -1 : 1;
      }
   }

   public boolean contains(ContextVar cv) {
      IntervalContextVar icv = (IntervalContextVar)cv;
      return this.L <= icv.L && this.R >= icv.R;
   }

   public boolean merge(ContextVar cv) {
      IntervalContextVar icv = (IntervalContextVar)cv;
      if (icv.L < this.L) {
         if (this.L <= icv.R) {
            this.L = icv.L;
            if (this.R < icv.R) {
               this.R = icv.R;
            }

            return true;
         }
      } else if (icv.L <= this.R) {
         if (this.R < icv.R) {
            this.R = icv.R;
         }

         return true;
      }

      return false;
   }

   public boolean intersect(ContextVar cv) {
      IntervalContextVar icv = (IntervalContextVar)cv;
      if (this.L <= icv.L && icv.L < this.R) {
         return true;
      } else {
         return icv.L <= this.L && this.L < icv.R;
      }
   }
}
