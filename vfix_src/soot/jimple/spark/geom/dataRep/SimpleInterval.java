package soot.jimple.spark.geom.dataRep;

public class SimpleInterval implements Comparable<SimpleInterval> {
   public long L;
   public long R;

   public SimpleInterval() {
      this.L = 0L;
      this.R = 1L;
   }

   public SimpleInterval(long l, long r) {
      this.L = l;
      this.R = r;
   }

   public SimpleInterval(SimpleInterval o) {
      this.L = o.L;
      this.R = o.R;
   }

   public String toString() {
      return "[" + this.L + ", " + this.R + ")";
   }

   public boolean equals(Object o) {
      SimpleInterval other = (SimpleInterval)o;
      return other.L == this.L && other.R == this.R;
   }

   public int hashCode() {
      int ans = (int)((this.L + this.R) % 2147483647L);
      if (ans < 0) {
         ans = -ans;
      }

      return ans;
   }

   public int compareTo(SimpleInterval o) {
      if (this.L == o.L) {
         return this.R < o.R ? -1 : 1;
      } else {
         return this.L < o.L ? -1 : 1;
      }
   }

   public boolean contains(SimpleInterval o) {
      return this.L <= o.L && this.R >= o.R;
   }

   public boolean merge(SimpleInterval o) {
      if (o.L < this.L) {
         if (this.L <= o.R) {
            this.L = o.L;
            if (this.R < o.R) {
               this.R = o.R;
            }

            return true;
         }
      } else if (o.L <= this.R) {
         if (this.R < o.R) {
            this.R = o.R;
         }

         return true;
      }

      return false;
   }

   public boolean intersect(SimpleInterval o) {
      if (this.L <= o.L && o.L < this.R) {
         return true;
      } else {
         return o.L <= this.L && this.L < o.R;
      }
   }
}
