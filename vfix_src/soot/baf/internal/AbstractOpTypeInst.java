package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.ArrayType;
import soot.NullType;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.baf.Baf;

public abstract class AbstractOpTypeInst extends AbstractInst {
   protected Type opType;

   protected AbstractOpTypeInst(Type opType) {
      if (opType instanceof NullType || opType instanceof ArrayType || opType instanceof RefType) {
         opType = RefType.v();
      }

      this.opType = (Type)opType;
   }

   public Type getOpType() {
      return this.opType;
   }

   public void setOpType(Type t) {
      this.opType = t;
      if (this.opType instanceof NullType || this.opType instanceof ArrayType || this.opType instanceof RefType) {
         this.opType = RefType.v();
      }

   }

   public String toString() {
      return this.getName() + "." + Baf.bafDescriptorOf(this.opType) + this.getParameters();
   }

   public void toString(UnitPrinter up) {
      up.literal(this.getName());
      up.literal(".");
      up.literal(Baf.bafDescriptorOf(this.opType));
      this.getParameters(up);
   }

   public int getOutMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType());
   }
}
