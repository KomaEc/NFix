package soot.dava.internal.javaRep;

import java.util.HashSet;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.internal.GInstanceFieldRef;

public class DInstanceFieldRef extends GInstanceFieldRef {
   private HashSet<Object> thisLocals;

   public DInstanceFieldRef(Value base, SootFieldRef fieldRef, HashSet<Object> thisLocals) {
      super(base, fieldRef);
      this.thisLocals = thisLocals;
   }

   public void toString(UnitPrinter up) {
      if (this.thisLocals.contains(this.getBase())) {
         up.fieldRef(this.fieldRef);
      } else {
         super.toString(up);
      }

   }

   public String toString() {
      return this.thisLocals.contains(this.getBase()) ? this.fieldRef.name() : super.toString();
   }

   public Object clone() {
      return new DInstanceFieldRef(this.getBase(), this.fieldRef, this.thisLocals);
   }
}
