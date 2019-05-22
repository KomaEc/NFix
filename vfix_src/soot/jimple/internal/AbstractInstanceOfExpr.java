package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.BooleanType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.InstanceOfExpr;
import soot.util.Switch;

public abstract class AbstractInstanceOfExpr implements InstanceOfExpr {
   final ValueBox opBox;
   Type checkType;

   protected AbstractInstanceOfExpr(ValueBox opBox, Type checkType) {
      this.opBox = opBox;
      this.checkType = checkType;
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractInstanceOfExpr)) {
         return false;
      } else {
         AbstractInstanceOfExpr aie = (AbstractInstanceOfExpr)o;
         return this.opBox.getValue().equivTo(aie.opBox.getValue()) && this.checkType.equals(aie.checkType);
      }
   }

   public int equivHashCode() {
      return this.opBox.getValue().equivHashCode() * 101 + this.checkType.hashCode() * 17;
   }

   public abstract Object clone();

   public String toString() {
      return this.opBox.getValue().toString() + " " + "instanceof" + " " + this.checkType.toString();
   }

   public void toString(UnitPrinter up) {
      this.opBox.toString(up);
      up.literal(" ");
      up.literal("instanceof");
      up.literal(" ");
      up.type(this.checkType);
   }

   public Value getOp() {
      return this.opBox.getValue();
   }

   public void setOp(Value op) {
      this.opBox.setValue(op);
   }

   public ValueBox getOpBox() {
      return this.opBox;
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.opBox.getValue().getUseBoxes());
      list.add(this.opBox);
      return list;
   }

   public Type getType() {
      return BooleanType.v();
   }

   public Type getCheckType() {
      return this.checkType;
   }

   public void setCheckType(Type checkType) {
      this.checkType = checkType;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseInstanceOfExpr(this);
   }
}
