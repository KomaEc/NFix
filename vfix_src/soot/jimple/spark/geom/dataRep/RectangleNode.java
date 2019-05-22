package soot.jimple.spark.geom.dataRep;

public class RectangleNode extends SegmentNode {
   public long L_prime;

   public RectangleNode() {
   }

   public RectangleNode(RectangleNode other) {
      this.copyRectangle(other);
   }

   public void copyRectangle(RectangleNode other) {
      this.I1 = other.I1;
      this.I2 = other.I2;
      this.L = other.L;
      this.L_prime = other.L_prime;
   }

   public RectangleNode(long I1, long I2, long L, long LL) {
      super(I1, I2, L);
      this.L_prime = LL;
   }

   public boolean equals(RectangleNode other) {
      return this.I1 == other.I1 && this.I2 == other.I2 && this.L == other.L && this.L_prime == other.L_prime;
   }

   public long yEnd() {
      return this.I2 + this.L_prime;
   }

   public boolean intersect(SegmentNode q) {
      if (q instanceof SegmentNode) {
         if (!this.point_within_rectangle(q.I1, q.I2, this) && !this.point_within_rectangle(q.I1 + q.L - 1L, q.I2 + q.L - 1L, this)) {
            return this.diagonal_line_intersect_horizontal(q, this.I1, this.I2, this.L) || this.diagonal_line_intersect_horizontal(q, this.I1, this.I2 + this.L_prime - 1L, this.L) || this.diagonal_line_intersect_vertical(q, this.I1, this.I2, this.L_prime) || this.diagonal_line_intersect_vertical(q, this.I1 + this.L - 1L, this.I2, this.L_prime);
         } else {
            return true;
         }
      } else {
         RectangleNode rect_q = (RectangleNode)q;
         if (this.I2 >= rect_q.I2 + rect_q.L_prime) {
            return false;
         } else if (this.I2 + this.L_prime <= rect_q.I2) {
            return false;
         } else if (this.I1 + this.L <= rect_q.I1) {
            return false;
         } else {
            return this.I1 < rect_q.I1 + rect_q.L;
         }
      }
   }

   private boolean point_within_rectangle(long x, long y, RectangleNode rect) {
      return x >= rect.I1 && x < rect.I1 + rect.L && y >= rect.I2 && y < rect.I2 + rect.L_prime;
   }

   private boolean diagonal_line_intersect_vertical(SegmentNode p, long x, long y, long L) {
      if (x >= p.I1 && x < p.I1 + p.L) {
         long y_cross = x - p.I1 + p.I2;
         if (y_cross >= y && y_cross < y + L) {
            return true;
         }
      }

      return false;
   }

   private boolean diagonal_line_intersect_horizontal(SegmentNode p, long x, long y, long L) {
      if (y >= p.I2 && y < p.I2 + p.L) {
         long x_cross = y - p.I2 + p.I1;
         if (x_cross >= x && x_cross < x + L) {
            return true;
         }
      }

      return false;
   }
}
