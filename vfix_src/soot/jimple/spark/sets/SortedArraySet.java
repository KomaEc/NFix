package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitVector;

public final class SortedArraySet extends PointsToSetInternal {
   private Node[] nodes = null;
   private int size = 0;
   private PAG pag = null;

   public SortedArraySet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
   }

   public final boolean isEmpty() {
      return this.size == 0;
   }

   public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      boolean ret = false;
      BitVector typeMask = this.pag.getTypeManager().get(this.type);
      if (!(other instanceof SortedArraySet)) {
         return super.addAll(other, exclude);
      } else {
         SortedArraySet o = (SortedArraySet)other;
         Node[] mya = this.nodes;
         Node[] oa = o.nodes;
         int osize = o.size;
         Node[] newa = new Node[this.size + osize];
         int myi = 0;
         int oi = 0;
         int newi = 0;

         while(true) {
            while(true) {
               while(true) {
                  int myhc;
                  for(; myi >= this.size; ++oi) {
                     if (oi >= osize) {
                        this.nodes = newa;
                        this.size = newi;
                        return ret;
                     }

                     myhc = oa[oi].getNumber();
                     if ((this.type == null || typeMask == null || typeMask.get(myhc)) && (exclude == null || !exclude.contains(oa[oi]))) {
                        newa[newi++] = oa[oi];
                        ret = true;
                     }
                  }

                  if (oi < osize) {
                     myhc = mya[myi].getNumber();
                     int ohc = oa[oi].getNumber();
                     if (myhc < ohc) {
                        newa[newi++] = mya[myi++];
                     } else if (myhc <= ohc) {
                        newa[newi++] = mya[myi++];
                        ++oi;
                     } else {
                        if ((this.type == null || typeMask == null || typeMask.get(ohc)) && (exclude == null || !exclude.contains(oa[oi]))) {
                           newa[newi++] = oa[oi];
                           ret = true;
                        }

                        ++oi;
                     }
                  } else {
                     newa[newi++] = mya[myi++];
                  }
               }
            }
         }
      }
   }

   public final boolean forall(P2SetVisitor v) {
      for(int i = 0; i < this.size; ++i) {
         v.visit(this.nodes[i]);
      }

      return v.getReturnValue();
   }

   public final boolean add(Node n) {
      if (!this.pag.getTypeManager().castNeverFails(n.getType(), this.type)) {
         return false;
      } else if (this.contains(n)) {
         return false;
      } else {
         int left = 0;
         int right = this.size;
         int hc = n.getNumber();

         while(left < right) {
            int mid = (left + right) / 2;
            int midhc = this.nodes[mid].getNumber();
            if (midhc < hc) {
               left = mid + 1;
            } else {
               if (midhc <= hc) {
                  break;
               }

               right = mid;
            }
         }

         if (this.nodes == null) {
            this.nodes = new Node[this.size + 4];
         } else if (this.size == this.nodes.length) {
            Node[] newNodes = new Node[this.size + 4];
            System.arraycopy(this.nodes, 0, newNodes, 0, this.nodes.length);
            this.nodes = newNodes;
         }

         System.arraycopy(this.nodes, left, this.nodes, left + 1, this.size - left);
         this.nodes[left] = n;
         ++this.size;
         return true;
      }
   }

   public final boolean contains(Node n) {
      int left = 0;
      int right = this.size;
      int hc = n.getNumber();

      while(left < right) {
         int mid = (left + right) / 2;
         int midhc = this.nodes[mid].getNumber();
         if (midhc < hc) {
            left = mid + 1;
         } else {
            if (midhc <= hc) {
               return true;
            }

            right = mid;
         }
      }

      return false;
   }

   public static final P2SetFactory getFactory() {
      return new P2SetFactory() {
         public final PointsToSetInternal newSet(Type type, PAG pag) {
            return new SortedArraySet(type, pag);
         }
      };
   }
}
