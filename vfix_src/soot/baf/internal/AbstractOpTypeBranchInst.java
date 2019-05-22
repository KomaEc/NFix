package soot.baf.internal;

import soot.ArrayType;
import soot.NullType;
import soot.RefType;
import soot.Type;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;

public abstract class AbstractOpTypeBranchInst extends AbstractBranchInst {
   protected Type opType;

   AbstractOpTypeBranchInst(Type opType, UnitBox targetBox) {
      super(targetBox);
      if (opType instanceof NullType || opType instanceof ArrayType || opType instanceof RefType) {
         opType = RefType.v();
      }

      this.opType = (Type)opType;
   }

   public int getInCount() {
      return 2;
   }

   public int getOutCount() {
      return 0;
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
      return this.getName() + "." + Baf.bafDescriptorOf(this.opType) + " " + this.getTarget();
   }

   public void toString(UnitPrinter up) {
      up.literal(this.getName());
      up.literal(".");
      up.literal(Baf.bafDescriptorOf(this.opType));
      up.literal(" ");
      this.targetBox.toString(up);
   }
}
