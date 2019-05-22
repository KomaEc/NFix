package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;

public class ParameterRef implements IdentityRef {
   int n;
   Type paramType;

   public ParameterRef(Type paramType, int number) {
      this.n = number;
      this.paramType = paramType;
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof ParameterRef)) {
         return false;
      } else {
         return this.n == ((ParameterRef)o).n && this.paramType.equals(((ParameterRef)o).paramType);
      }
   }

   public int equivHashCode() {
      return this.n * 101 + this.paramType.hashCode() * 17;
   }

   public Object clone() {
      return new ParameterRef(this.paramType, this.n);
   }

   public String toString() {
      return "@parameter" + this.n + ": " + this.paramType;
   }

   public void toString(UnitPrinter up) {
      up.identityRef(this);
   }

   public int getIndex() {
      return this.n;
   }

   public void setIndex(int index) {
      this.n = index;
   }

   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Type getType() {
      return this.paramType;
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseParameterRef(this);
   }
}
