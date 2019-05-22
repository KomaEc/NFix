package soot.jimple.spark.sets;

import soot.Scene;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;

public final class HybridPointsToSet extends PointsToSetInternal {
   private Node[] nodes = new Node[16];
   private BitVector bits = null;
   private PAG pag;
   private boolean empty = true;

   public HybridPointsToSet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
   }

   public final boolean isEmpty() {
      return this.empty;
   }

   private boolean superAddAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      boolean ret = super.addAll(other, exclude);
      if (ret) {
         this.empty = false;
      }

      return ret;
   }

   private boolean nativeAddAll(HybridPointsToSet other, HybridPointsToSet exclude) {
      boolean ret = false;
      TypeManager typeManager = this.pag.getTypeManager();
      if (other.bits != null) {
         this.convertToBits();
         if (exclude != null) {
            exclude.convertToBits();
         }

         BitVector mask = null;
         if (!typeManager.castNeverFails(other.getType(), this.getType())) {
            mask = typeManager.get(this.getType());
         }

         BitVector ebits = exclude == null ? null : exclude.bits;
         ret = this.bits.orAndAndNot(other.bits, mask, ebits);
      } else {
         for(int i = 0; i < this.nodes.length && other.nodes[i] != null; ++i) {
            if (exclude == null || !exclude.contains(other.nodes[i])) {
               ret |= this.add(other.nodes[i]);
            }
         }
      }

      if (ret) {
         this.empty = false;
      }

      return ret;
   }

   public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      if (other != null && !(other instanceof HybridPointsToSet)) {
         return this.superAddAll(other, exclude);
      } else {
         return exclude != null && !(exclude instanceof HybridPointsToSet) ? this.superAddAll(other, exclude) : this.nativeAddAll((HybridPointsToSet)other, (HybridPointsToSet)exclude);
      }
   }

   public final boolean forall(P2SetVisitor v) {
      if (this.bits == null) {
         Node[] var2 = this.nodes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Node node = var2[var4];
            if (node == null) {
               return v.getReturnValue();
            }

            v.visit(node);
         }
      } else {
         BitSetIterator it = this.bits.iterator();

         while(it.hasNext()) {
            v.visit((Node)this.pag.getAllocNodeNumberer().get((long)it.next()));
         }
      }

      return v.getReturnValue();
   }

   public final boolean add(Node n) {
      return this.pag.getTypeManager().castNeverFails(n.getType(), this.type) ? this.fastAdd(n) : false;
   }

   public final boolean contains(Node n) {
      if (this.bits != null) {
         return this.bits.get(n.getNumber());
      } else {
         Node[] var2 = this.nodes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Node node = var2[var4];
            if (node == n) {
               return true;
            }

            if (node == null) {
               break;
            }
         }

         return false;
      }
   }

   public static P2SetFactory getFactory() {
      return new P2SetFactory() {
         public final PointsToSetInternal newSet(Type type, PAG pag) {
            return new HybridPointsToSet(type, pag);
         }
      };
   }

   protected final boolean fastAdd(Node n) {
      if (this.bits == null) {
         for(int i = 0; i < this.nodes.length; ++i) {
            if (this.nodes[i] == null) {
               this.empty = false;
               this.nodes[i] = n;
               return true;
            }

            if (this.nodes[i] == n) {
               return false;
            }
         }

         this.convertToBits();
      }

      boolean ret = this.bits.set(n.getNumber());
      if (ret) {
         this.empty = false;
      }

      return ret;
   }

   protected final void convertToBits() {
      if (this.bits == null) {
         this.bits = new BitVector(this.pag.getAllocNodeNumberer().size());
         Node[] var1 = this.nodes;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Node node = var1[var3];
            if (node != null) {
               this.fastAdd(node);
            }
         }

      }
   }

   public static HybridPointsToSet intersection(final HybridPointsToSet set1, final HybridPointsToSet set2, PAG pag) {
      final HybridPointsToSet ret = new HybridPointsToSet(Scene.v().getObjectType(), pag);
      BitVector s1Bits = set1.bits;
      BitVector s2Bits = set2.bits;
      if (s1Bits != null && s2Bits != null) {
         ret.bits = BitVector.and(s1Bits, s2Bits);
         ret.empty = false;
      } else if (s1Bits != null) {
         set2.forall(new P2SetVisitor() {
            public void visit(Node n) {
               if (set1.contains(n)) {
                  ret.add(n);
               }

            }
         });
      } else {
         set1.forall(new P2SetVisitor() {
            public void visit(Node n) {
               if (set2.contains(n)) {
                  ret.add(n);
               }

            }
         });
      }

      return ret;
   }
}
