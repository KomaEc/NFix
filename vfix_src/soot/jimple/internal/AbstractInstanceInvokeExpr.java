package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InstanceInvokeExpr;

public abstract class AbstractInstanceInvokeExpr extends AbstractInvokeExpr implements InstanceInvokeExpr {
   protected final ValueBox baseBox;

   protected AbstractInstanceInvokeExpr(SootMethodRef methodRef, ValueBox baseBox, ValueBox[] argBoxes) {
      super(methodRef, argBoxes);
      this.baseBox = baseBox;
   }

   public Value getBase() {
      return this.baseBox.getValue();
   }

   public ValueBox getBaseBox() {
      return this.baseBox;
   }

   public void setBase(Value base) {
      this.baseBox.setValue(base);
   }

   public List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      if (this.argBoxes != null) {
         Collections.addAll(list, this.argBoxes);
         ValueBox[] var2 = this.argBoxes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ValueBox element = var2[var4];
            list.addAll(element.getValue().getUseBoxes());
         }
      }

      list.addAll(this.baseBox.getValue().getUseBoxes());
      list.add(this.baseBox);
      return list;
   }
}
