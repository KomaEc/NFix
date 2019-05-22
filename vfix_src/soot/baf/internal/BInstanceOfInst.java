package soot.baf.internal;

import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.InstanceOfInst;
import soot.util.Switch;

public class BInstanceOfInst extends AbstractInst implements InstanceOfInst {
   protected Type checkType;

   public BInstanceOfInst(Type opType) {
      if (!(opType instanceof RefType) && !(opType instanceof ArrayType)) {
         throw new RuntimeException("invalid InstanceOfInst: " + opType);
      } else {
         this.checkType = opType;
      }
   }

   public int getInCount() {
      return 1;
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "instanceof";
   }

   public Type getCheckType() {
      return this.checkType;
   }

   public void setCheckType(Type t) {
      this.checkType = t;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseInstanceOfInst(this);
   }

   public Object clone() {
      return new BInstanceOfInst(this.checkType);
   }
}
