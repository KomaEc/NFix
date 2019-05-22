package soot.jimple.spark.geom.heapinsE;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.IFigureManager;

public class HeapInsIntervalManager extends IFigureManager {
   public static int Divisions = 3;
   public static final int ALL_TO_ALL = -1;
   public static final int ALL_TO_MANY = 0;
   public static final int MANY_TO_ALL = 1;
   public static final int ONE_TO_ONE = 2;
   private int[] size = new int[]{0, 0, 0};
   private SegmentNode[] header = new SegmentNode[]{null, null, null};
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

      for(int i = 0; i < Divisions; ++i) {
         for(SegmentNode p = this.header[i]; p != null && p.is_new; p = p.next) {
            p.is_new = false;
         }
      }

   }

   public void clear() {
      for(int i = 0; i < Divisions; ++i) {
         this.size[i] = 0;
         this.header[i] = null;
      }

      this.hasNewFigure = false;
   }

   public SegmentNode addNewFigure(int code, RectangleNode pnew) {
      SegmentNode p;
      if (code == -1) {
         if (this.header[0] != null && this.header[0].I2 == 0L) {
            return null;
         }

         p = new SegmentNode();
         code = 0;
         p.I1 = p.I2 = 0L;
         p.L = 9223372036854775806L;

         for(int i = 0; i < Divisions; ++i) {
            this.size[i] = 0;
            this.header[i] = null;
         }
      } else {
         if (code == 0 || code == 2) {
            for(p = this.header[0]; p != null; p = p.next) {
               if (p.I2 <= pnew.I2 && p.I2 + p.L >= pnew.I2 + pnew.L) {
                  return null;
               }
            }
         }

         if (code == 1 || code == 2) {
            for(p = this.header[1]; p != null; p = p.next) {
               if (p.I1 <= pnew.I1 && p.I1 + p.L >= pnew.I1 + pnew.L) {
                  return null;
               }
            }
         }

         if (code == 2) {
            for(p = this.header[2]; p != null; p = p.next) {
               if (p.I1 - p.I2 == pnew.I1 - pnew.I2 && p.I1 <= pnew.I1 && p.I1 + p.L >= pnew.I1 + pnew.L) {
                  return null;
               }
            }
         }

         p = new SegmentNode(pnew);
         if (code == 0) {
            this.clean_garbage_all_to_many(p);
         } else if (code == 1) {
            this.clean_garbage_many_to_all(p);
         } else {
            this.clean_garbage_one_to_one(p);
         }
      }

      this.hasNewFigure = true;
      int var10002 = this.size[code]++;
      p.next = this.header[code];
      this.header[code] = p;
      return p;
   }

   public void mergeFigures(int upperSize) {
      if (this.hasNewFigure) {
         if (this.size[2] > upperSize && this.header[2].is_new) {
            SegmentNode p = this.generate_many_to_all(this.header[2]);
            this.clean_garbage_many_to_all(p);
            p.next = this.header[1];
            this.header[1] = p;
            this.header[2] = null;
            int var10002 = this.size[1]++;
            this.size[2] = 0;
         }

         if (this.size[1] > upperSize && this.header[1].is_new) {
            this.header[1] = this.generate_many_to_all(this.header[1]);
            this.size[1] = 1;
         }

         if (this.size[0] > upperSize && this.header[0].is_new) {
            this.header[0] = this.generate_all_to_many(this.header[0]);
            this.size[0] = 1;
         }

      }
   }

   public void removeUselessSegments() {
      SegmentNode p = this.header[2];
      this.size[2] = 0;

      SegmentNode q;
      SegmentNode temp;
      for(q = null; p != null; p = temp) {
         boolean contained = false;
         long L = p.L;

         for(int i = 0; i < 2; ++i) {
            for(temp = this.header[i]; temp != null; temp = temp.next) {
               if ((temp.I1 == 0L || temp.I1 <= p.I1 && temp.I1 + temp.L >= p.I1 + L) && (temp.I2 == 0L || temp.I2 <= p.I2 && temp.I2 + temp.L >= p.I2 + L)) {
                  contained = true;
                  break;
               }
            }
         }

         temp = p.next;
         if (!contained) {
            p.next = q;
            q = p;
            int var10002 = this.size[2]++;
         }
      }

      this.header[2] = q;
   }

   private SegmentNode generate_all_to_many(SegmentNode mp) {
      long left = mp.I2;
      long right = left + mp.L;

      for(SegmentNode p = mp.next; p != null; p = p.next) {
         if (p.I2 < left) {
            left = p.I2;
         }

         long t = p.I2 + p.L;
         if (t > right) {
            right = t;
         }
      }

      mp.I1 = 0L;
      mp.I2 = left;
      mp.L = right - left;
      mp.next = null;
      return mp;
   }

   private SegmentNode generate_many_to_all(SegmentNode mp) {
      long left = mp.I1;
      long right = left + mp.L;

      for(SegmentNode p = mp.next; p != null; p = p.next) {
         if (p.I1 < left) {
            left = p.I1;
         }

         long t = p.I1 + p.L;
         if (t > right) {
            right = t;
         }
      }

      mp.I1 = left;
      mp.I2 = 0L;
      mp.L = right - left;
      mp.next = null;
      return mp;
   }

   private void clean_garbage_many_to_all(SegmentNode predator) {
      SegmentNode list = this.header[1];
      SegmentNode q = null;
      SegmentNode p = null;
      int num = 0;
      long left = predator.I1;
      long right = left + predator.L;

      while(true) {
         while(list != null) {
            if (list.I1 >= left) {
               if (list.I1 <= right) {
                  if (list.I1 + list.L > right) {
                     right = list.I1 + list.L;
                  }

                  list = list.next;
                  continue;
               }
            } else if (list.I1 + list.L >= left) {
               left = list.I1;
               list = list.next;
               continue;
            }

            if (q == null) {
               q = list;
               p = list;
            } else {
               q.next = list;
               q = list;
            }

            ++num;
            list = list.next;
         }

         predator.I1 = left;
         predator.L = right - left;
         if (q != null) {
            q.next = null;
         }

         this.header[1] = p;
         this.size[1] = num;
         return;
      }
   }

   private void clean_garbage_all_to_many(SegmentNode predator) {
      SegmentNode list = this.header[0];
      SegmentNode q = null;
      SegmentNode p = null;
      int num = 0;
      long left = predator.I2;
      long right = predator.I2 + predator.L;

      while(true) {
         while(list != null) {
            if (list.I2 >= left) {
               if (list.I2 <= right) {
                  if (list.I2 + list.L > right) {
                     right = list.I2 + list.L;
                  }

                  list = list.next;
                  continue;
               }
            } else if (list.I2 + list.L >= left) {
               left = list.I2;
               list = list.next;
               continue;
            }

            if (q == null) {
               q = list;
               p = list;
            } else {
               q.next = list;
               q = list;
            }

            ++num;
            list = list.next;
         }

         predator.I2 = left;
         predator.L = right - left;
         if (q != null) {
            q.next = null;
         }

         this.header[0] = p;
         this.size[0] = num;
         return;
      }
   }

   private void clean_garbage_one_to_one(SegmentNode predator) {
      SegmentNode list = this.header[2];
      SegmentNode q = null;
      SegmentNode p = null;

      int num;
      for(num = 0; list != null; list = list.next) {
         long L = list.L;
         if (predator.I2 - predator.I1 != list.I2 - list.I1 || predator.I1 > list.I1 || predator.I1 + predator.L < list.I2 + L) {
            if (q == null) {
               q = list;
               p = list;
            } else {
               q.next = list;
               q = list;
            }

            ++num;
         }
      }

      if (q != null) {
         q.next = null;
      }

      this.header[2] = p;
      this.size[2] = num;
   }
}
