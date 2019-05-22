package soot.jimple.internal;

import soot.IntType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.LengthExpr;
import soot.util.Switch;

public abstract class AbstractLengthExpr extends AbstractUnopExpr implements LengthExpr {
   protected AbstractLengthExpr(ValueBox opBox) {
      super(opBox);
   }

   public boolean equivTo(Object o) {
      return o instanceof AbstractLengthExpr ? this.opBox.getValue().equivTo(((AbstractLengthExpr)o).opBox.getValue()) : false;
   }

   public int equivHashCode() {
      return this.opBox.getValue().equivHashCode();
   }

   public abstract Object clone();

   public String toString() {
      return "lengthof " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("lengthof");
      up.literal(" ");
      this.opBox.toString(up);
   }

   public Type getType() {
      return IntType.v();
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseLengthExpr(this);
   }
}
