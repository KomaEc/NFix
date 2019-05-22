package soot.baf.internal;

import java.util.Iterator;
import soot.AbstractJasminClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPrinter;
import soot.VoidType;

abstract class AbstractInvokeInst extends AbstractInst {
   SootMethodRef methodRef;

   public SootMethodRef getMethodRef() {
      return this.methodRef;
   }

   public SootMethod getMethod() {
      return this.methodRef.resolve();
   }

   public Type getType() {
      return this.methodRef.returnType();
   }

   public String toString() {
      return this.getName() + this.getParameters();
   }

   public abstract String getName();

   String getParameters() {
      return " " + this.methodRef.getSignature();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      up.methodRef(this.methodRef);
   }

   public int getInCount() {
      return this.getMethodRef().parameterTypes().size();
   }

   public int getOutCount() {
      return this.getMethodRef().returnType() instanceof VoidType ? 0 : 1;
   }

   public int getInMachineCount() {
      int count = 0;

      for(Iterator it = this.getMethodRef().parameterTypes().iterator(); it.hasNext(); count += AbstractJasminClass.sizeOfType((Type)it.next())) {
      }

      return count;
   }

   public int getOutMachineCount() {
      return this.getMethodRef().returnType() instanceof VoidType ? 0 : AbstractJasminClass.sizeOfType(this.getMethodRef().returnType());
   }

   public boolean containsInvokeExpr() {
      return true;
   }
}
