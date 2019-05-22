package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Value;
import soot.ValueBox;

public abstract class AbstractOpStmt extends AbstractStmt {
   final ValueBox opBox;

   protected AbstractOpStmt(ValueBox opBox) {
      this.opBox = opBox;
   }

   public final Value getOp() {
      return this.opBox.getValue();
   }

   public final void setOp(Value op) {
      this.opBox.setValue(op);
   }

   public final ValueBox getOpBox() {
      return this.opBox;
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.opBox.getValue().getUseBoxes());
      list.add(this.opBox);
      return list;
   }
}
