package soot.baf.internal;

import soot.RefType;
import soot.Type;
import soot.UnitPrinter;

public abstract class AbstractRefTypeInst extends AbstractInst {
   Type opType;

   protected AbstractRefTypeInst(RefType opType) {
      this.opType = opType;
   }

   public Type getOpType() {
      return this.opType;
   }

   public void setOpType(Type t) {
      this.opType = t;
   }

   public RefType getBaseType() {
      return (RefType)this.opType;
   }

   public void setBaseType(RefType type) {
      this.opType = type;
   }

   String getParameters() {
      return " " + this.opType.toString();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      up.type(this.opType);
   }
}
