package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Value;
import soot.ValueBox;
import soot.jimple.UnopExpr;

public abstract class AbstractUnopExpr implements UnopExpr {
   final ValueBox opBox;

   protected AbstractUnopExpr(ValueBox opBox) {
      this.opBox = opBox;
   }

   public abstract Object clone();

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
}
