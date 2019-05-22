package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.NewExpr;
import soot.util.Switch;

public abstract class AbstractNewExpr implements NewExpr {
   RefType type;

   public boolean equivTo(Object o) {
      if (o instanceof AbstractNewExpr) {
         AbstractNewExpr ae = (AbstractNewExpr)o;
         return this.type.equals(ae.type);
      } else {
         return false;
      }
   }

   public int equivHashCode() {
      return this.type.hashCode();
   }

   public abstract Object clone();

   public String toString() {
      return "new " + this.type.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("new");
      up.literal(" ");
      up.type(this.type);
   }

   public RefType getBaseType() {
      return this.type;
   }

   public void setBaseType(RefType type) {
      this.type = type;
   }

   public Type getType() {
      return this.type;
   }

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNewExpr(this);
   }
}
