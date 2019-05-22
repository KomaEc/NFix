package soot.jimple.spark.geom.geomE;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.IFigureManager;

public class GeometricManager extends IFigureManager {
   public static final int Divisions = 2;
   public static final int ONE_TO_ONE = 0;
   public static final int MANY_TO_MANY = 1;
   public static final int Undefined_Mapping = -1;
   private SegmentNode[] header = new SegmentNode[]{null, null};
   private int[] size = new int[]{0, 0};
   private boolean hasNewFigure = false;

   public SegmentNode[] getFigures() {
      return this.header;
   }

   public int[] getSizes() {
      return this.size;
   }

   public boolean isThereUnprocessedFigures() {
      return this.hasNewFigure;
   }

   public void flush() {
      this.hasNewFigure = false;

      for(int i = 0; i < 2; ++i) {
         for(SegmentNode p = this.header[i]; p != null && p.is_new; p = p.next) {
            p.is_new = false;
         }
      }

   }

   public SegmentNode addNewFigure(int code, RectangleNode pnew) {
      if (this.checkRedundancy(code, pnew)) {
         return null;
      } else {
         this.filterOutDuplicates(code, pnew);
         Object p;
         if (code == 0) {
            p = getSegmentNode();
            ((SegmentNode)p).copySegment(pnew);
         } else {
            p = getRectangleNode();
            ((RectangleNode)p).copyRectangle(pnew);
         }

         this.hasNewFigure = true;
         ((SegmentNode)p).next = this.header[code];
         this.header[code] = (SegmentNode)p;
         int var10002 = this.size[code]++;
         return (SegmentNode)p;
      }
   }

   public void mergeFigures(int buget_size) {
      if (this.hasNewFigure) {
         for(int i = 0; i < 2; ++i) {
            RectangleNode p = null;
            if (this.size[i] > buget_size && this.header[i].is_new) {
               switch(i) {
               case 0:
                  p = this.mergeOneToOne();
                  break;
               case 1:
                  p = this.mergeManyToMany();
               }
            }

            if (p != null) {
               if (i == 0) {
                  if (this.checkRedundancy(1, p)) {
                     continue;
                  }

                  this.filterOutDuplicates(1, p);
               }

               p.next = this.header[1];
               this.header[1] = p;
               int var10002 = this.size[1]++;
            }
         }

      }
   }

   public void removeUselessSegments() {
      SegmentNode p = this.header[0];
      SegmentNode q = null;

      int countAll;
      SegmentNode temp;
      for(countAll = 0; p != null; p = temp) {
         temp = p.next;
         if (!this.isContainedInRectangles(p)) {
            p.next = q;
            q = p;
            ++countAll;
         } else {
            reclaimSegmentNode(p);
         }
      }

      this.size[0] = countAll;
      this.header[0] = q;
   }

   private boolean isContainedInRectangles(SegmentNode pnew) {
      for(SegmentNode p = this.header[1]; p != null; p = p.next) {
         if (pnew.I1 >= p.I1 && pnew.I2 >= p.I2 && pnew.I1 + pnew.L <= p.I1 + p.L && pnew.I2 + pnew.L <= p.I2 + ((RectangleNode)p).L_prime) {
            return true;
         }
      }

      return false;
   }

   private boolean checkRedundancy(int code, RectangleNode pnew) {
      if (code == 0) {
         pnew.L_prime = pnew.L;
      }

      for(int i = code; i <= 1; ++i) {
         for(SegmentNode p = this.header[i]; p != null; p = p.next) {
            switch(i) {
            case 0:
               if (p.I2 - p.I1 == pnew.I2 - pnew.I1 && pnew.I1 >= p.I1 && pnew.I1 + pnew.L <= p.I1 + p.L) {
                  return true;
               }
               break;
            case 1:
               if (pnew.I1 >= p.I1 && pnew.I2 >= p.I2 && pnew.I1 + pnew.L <= p.I1 + p.L && pnew.I2 + pnew.L_prime <= p.I2 + ((RectangleNode)p).L_prime) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void filterOutDuplicates(int code, SegmentNode p) {
      for(int i = code; i > -1; --i) {
         SegmentNode pold = this.header[i];
         SegmentNode q_head = null;
         SegmentNode q_tail = null;
         int countAll = 0;

         while(pold != null) {
            boolean flag = false;
            switch(i) {
            case 0:
               if (code == 1) {
                  if (pold.I1 >= p.I1 && pold.I2 >= p.I2 && pold.I1 + pold.L <= p.I1 + p.L && pold.I2 + pold.L <= p.I2 + ((RectangleNode)p).L_prime) {
                     flag = true;
                  }
               } else if (p.I2 - p.I1 == pold.I2 - pold.I1 && pold.I1 >= p.I1 && pold.I1 + pold.L <= p.I1 + p.L) {
                  flag = true;
               }
               break;
            case 1:
               if (pold.I1 >= p.I1 && pold.I2 >= p.I2 && pold.I1 + pold.L <= p.I1 + p.L && pold.I2 + ((RectangleNode)pold).L_prime <= p.I2 + ((RectangleNode)p).L_prime) {
                  flag = true;
               }
            }

            if (!flag) {
               if (q_head == null) {
                  q_head = pold;
               } else {
                  q_tail.next = pold;
               }

               q_tail = pold;
               ++countAll;
               pold = pold.next;
            } else if (i == 0) {
               pold = reclaimSegmentNode(pold);
            } else {
               pold = reclaimRectangleNode(pold);
            }
         }

         if (q_tail != null) {
            q_tail.next = null;
         }

         this.header[i] = q_head;
         this.size[i] = countAll;
      }

   }

   private RectangleNode mergeManyToMany() {
      long x_min = Long.MAX_VALUE;
      long y_min = Long.MAX_VALUE;
      long x_max = Long.MIN_VALUE;
      long y_max = Long.MIN_VALUE;
      RectangleNode p = (RectangleNode)this.header[1];
      this.header[1] = null;

      for(this.size[1] = 0; p != null; p = (RectangleNode)reclaimRectangleNode(p)) {
         if (p.I1 < x_min) {
            x_min = p.I1;
         }

         if (p.I2 < y_min) {
            y_min = p.I2;
         }

         if (p.I1 + p.L > x_max) {
            x_max = p.I1 + p.L;
         }

         if (p.I2 + p.L_prime > y_max) {
            y_max = p.I2 + p.L_prime;
         }
      }

      p = getRectangleNode();
      p.I1 = x_min;
      p.I2 = y_min;
      p.L = x_max - x_min;
      p.L_prime = y_max - y_min;
      p.next = null;
      return p;
   }

   private RectangleNode mergeOneToOne() {
      long x_min = Long.MAX_VALUE;
      long y_min = Long.MAX_VALUE;
      long x_max = Long.MIN_VALUE;
      long y_max = Long.MIN_VALUE;
      SegmentNode p = this.header[0];
      this.header[0] = null;

      for(this.size[0] = 0; p != null; p = reclaimSegmentNode(p)) {
         if (p.I1 < x_min) {
            x_min = p.I1;
         }

         if (p.I2 < y_min) {
            y_min = p.I2;
         }

         if (p.I1 + p.L > x_max) {
            x_max = p.I1 + p.L;
         }

         if (p.I2 + p.L > y_max) {
            y_max = p.I2 + p.L;
         }
      }

      RectangleNode q = getRectangleNode();
      q.I1 = x_min;
      q.I2 = y_min;
      q.L = x_max - x_min;
      q.L_prime = y_max - y_min;
      return q;
   }
}
