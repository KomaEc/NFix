package soot.baf.internal;

import soot.RefType;
import soot.baf.InstSwitch;
import soot.baf.NewInst;
import soot.util.Switch;

public class BNewInst extends AbstractRefTypeInst implements NewInst {
   public BNewInst(RefType opType) {
      super(opType);
   }

   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BNewInst(this.getBaseType());
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "new";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseNewInst(this);
   }

   public boolean containsNewExpr() {
      return true;
   }
}
