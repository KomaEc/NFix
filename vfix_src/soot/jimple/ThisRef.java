package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;

public class ThisRef implements IdentityRef {
   RefType thisType;

   public ThisRef(RefType thisType) {
      this.thisType = thisType;
   }

   public boolean equivTo(Object o) {
      return o instanceof ThisRef ? this.thisType.equals(((ThisRef)o).thisType) : false;
   }

   public int equivHashCode() {
      return this.thisType.hashCode();
   }

   public String toString() {
      return "@this: " + this.thisType;
   }

   public void toString(UnitPrinter up) {
      up.identityRef(this);
   }

   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Type getType() {
      return this.thisType;
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseThisRef(this);
   }

   public Object clone() {
      return new ThisRef(this.thisType);
   }
}
