package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;

public abstract class AbstractInvokeExpr implements InvokeExpr {
   protected SootMethodRef methodRef;
   protected final ValueBox[] argBoxes;

   protected AbstractInvokeExpr(SootMethodRef methodRef, ValueBox[] argBoxes) {
      this.methodRef = methodRef;
      this.argBoxes = argBoxes.length == 0 ? null : argBoxes;
   }

   public void setMethodRef(SootMethodRef methodRef) {
      this.methodRef = methodRef;
   }

   public SootMethodRef getMethodRef() {
      return this.methodRef;
   }

   public SootMethod getMethod() {
      return this.methodRef.resolve();
   }

   public abstract Object clone();

   public Value getArg(int index) {
      return this.argBoxes[index].getValue();
   }

   public List<Value> getArgs() {
      List<Value> l = new ArrayList();
      if (this.argBoxes != null) {
         ValueBox[] var2 = this.argBoxes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ValueBox element = var2[var4];
            l.add(element.getValue());
         }
      }

      return l;
   }

   public int getArgCount() {
      return this.argBoxes == null ? 0 : this.argBoxes.length;
   }

   public void setArg(int index, Value arg) {
      this.argBoxes[index].setValue(arg);
   }

   public ValueBox getArgBox(int index) {
      return this.argBoxes[index];
   }

   public Type getType() {
      return this.methodRef.returnType();
   }

   public List<ValueBox> getUseBoxes() {
      if (this.argBoxes == null) {
         return Collections.emptyList();
      } else {
         List<ValueBox> list = new ArrayList();
         Collections.addAll(list, this.argBoxes);
         ValueBox[] var2 = this.argBoxes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ValueBox element = var2[var4];
            list.addAll(element.getValue().getUseBoxes());
         }

         return list;
      }
   }
}
