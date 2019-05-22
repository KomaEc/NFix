package soot.jimple.spark.geom.dataRep;

public class SegmentNode implements Comparable<SegmentNode> {
   public long I1;
   public long I2;
   public long L;
   public SegmentNode next = null;
   public boolean is_new = true;

   public SegmentNode() {
   }

   public SegmentNode(SegmentNode other) {
      this.copySegment(other);
   }

   public void copySegment(SegmentNode other) {
      this.I1 = other.I1;
      this.I2 = other.I2;
      this.L = other.L;
   }

   public SegmentNode(long i1, long i2, long l) {
      this.I1 = i1;
      this.I2 = i2;
      this.L = l;
   }

   public boolean equals(SegmentNode other) {
      if (other instanceof RectangleNode) {
         return false;
      } else {
         return this.I1 == other.I1 && this.I2 == other.I2 && this.L == other.L;
      }
   }

   public int compareTo(SegmentNode o) {
      long d = this.I1 - o.I1;
      if (d != 0L) {
         return d < 0L ? -1 : 1;
      } else {
         d = this.I2 - o.I2;
         if (d != 0L) {
            return d < 0L ? -1 : 1;
         } else {
            d = this.L - o.L;
            if (d != 0L) {
               return d < 0L ? -1 : 1;
            } else {
               if (this instanceof RectangleNode && o instanceof RectangleNode) {
                  d = ((RectangleNode)this).L_prime - ((RectangleNode)o).L_prime;
                  if (d != 0L) {
                     return d < 0L ? -1 : 1;
                  }
               }

               return 0;
            }
         }
      }
   }

   public long xEnd() {
      return this.I1 + this.L;
   }

   public long yEnd() {
      return this.I2 + this.L;
   }

   public boolean intersect(SegmentNode q) {
      if (q instanceof RectangleNode) {
         return q.intersect(this);
      } else if (this.I2 - this.I1 == q.I2 - q.I1) {
         if (this.I1 <= q.I1) {
            return q.I1 < this.I1 + this.L;
         } else {
            return this.I1 < q.I1 + q.L;
         }
      } else {
         return false;
      }
   }

   public boolean projYIntersect(SegmentNode q) {
      long py1 = this.I2;
      long py2 = this.yEnd();
      long qy1 = q.I2;
      long qy2 = q.yEnd();
      if (py1 <= qy1) {
         return qy1 < py2;
      } else {
         return py1 < qy2;
      }
   }
}
