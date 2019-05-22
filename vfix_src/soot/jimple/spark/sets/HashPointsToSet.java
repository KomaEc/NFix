package soot.jimple.spark.sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;

public final class HashPointsToSet extends PointsToSetInternal {
   private final HashSet<Node> s = new HashSet(4);
   private PAG pag = null;

   public HashPointsToSet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
   }

   public final boolean isEmpty() {
      return this.s.isEmpty();
   }

   public final boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      return !(other instanceof HashPointsToSet) || exclude != null || this.pag.getTypeManager().getFastHierarchy() != null && this.type != null && !this.type.equals(other.type) ? super.addAll(other, exclude) : this.s.addAll(((HashPointsToSet)other).s);
   }

   public final boolean forall(P2SetVisitor v) {
      Iterator it = (new ArrayList(this.s)).iterator();

      while(it.hasNext()) {
         v.visit((Node)it.next());
      }

      return v.getReturnValue();
   }

   public final boolean add(Node n) {
      return this.pag.getTypeManager().castNeverFails(n.getType(), this.type) ? this.s.add(n) : false;
   }

   public final boolean contains(Node n) {
      return this.s.contains(n);
   }

   public static P2SetFactory getFactory() {
      return new P2SetFactory() {
         public PointsToSetInternal newSet(Type type, PAG pag) {
            return new HashPointsToSet(type, pag);
         }
      };
   }
}
