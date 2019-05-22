package soot.baf.internal;

import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.NewArrayInst;
import soot.util.Switch;

public class BNewArrayInst extends AbstractInst implements NewArrayInst {
   protected Type baseType;

   public BNewArrayInst(Type opType) {
      this.baseType = opType;
   }

   public int getInCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public Object clone() {
      return new BNewArrayInst(this.baseType);
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "newarray";
   }

   public Type getBaseType() {
      return this.baseType;
   }

   public void setBaseType(Type type) {
      this.baseType = type;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseNewArrayInst(this);
   }

   public boolean containsNewExpr() {
      return true;
   }
}
