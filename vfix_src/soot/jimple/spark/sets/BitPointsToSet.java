package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;

public final class BitPointsToSet extends PointsToSetInternal {
   private BitVector bits = null;
   private boolean empty = true;
   private PAG pag = null;

   public BitPointsToSet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
      this.bits = new BitVector(pag.getAllocNodeNumberer().size());
   }

   public final boolean isEmpty() {
      return this.empty;
   }

   private final boolean superAddAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      boolean ret = super.addAll(other, exclude);
      if (ret) {
         this.empty = false;
      }

      return ret;
   }

   private final boolean nativeAddAll(BitPointsToSet other, BitPointsToSet exclude) {
      BitVector mask = null;
      TypeManager typeManager = this.pag.getTypeManager();
      if (!typeManager.castNeverFails(other.getType(), this.getType())) {
         mask = typeManager.get(this.getType());
      }

      BitVector obits = other == null ? null : other.bits;
      BitVector ebits = exclude == null ? null : exclude.bits;
      boolean ret = this.bits.orAndAndNot(obits, mask, ebits);
      if (ret) {
         this.empty = false;
      }

      return ret;
   }

   public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      if (other != null && !(other instanceof BitPointsToSet)) {
         return this.superAddAll(other, exclude);
      } else {
         return exclude != null && !(exclude instanceof BitPointsToSet) ? this.superAddAll(other, exclude) : this.nativeAddAll((BitPointsToSet)other, (BitPointsToSet)exclude);
      }
   }

   public final boolean forall(P2SetVisitor v) {
      BitSetIterator it = this.bits.iterator();

      while(it.hasNext()) {
         v.visit((Node)this.pag.getAllocNodeNumberer().get((long)it.next()));
      }

      return v.getReturnValue();
   }

   public final boolean add(Node n) {
      return this.pag.getTypeManager().castNeverFails(n.getType(), this.type) ? this.fastAdd(n) : false;
   }

   public final boolean contains(Node n) {
      return this.bits.get(n.getNumber());
   }

   public static P2SetFactory getFactory() {
      return new P2SetFactory() {
         public PointsToSetInternal newSet(Type type, PAG pag) {
            return new BitPointsToSet(type, pag);
         }
      };
   }

   private boolean fastAdd(Node n) {
      boolean ret = this.bits.set(n.getNumber());
      if (ret) {
         this.empty = false;
      }

      return ret;
   }
}
