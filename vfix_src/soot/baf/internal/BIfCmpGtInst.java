package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfCmpGtInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfCmpGtInst extends AbstractOpTypeBranchInst implements IfCmpGtInst {
   public BIfCmpGtInst(Type opType, Unit target) {
      super(opType, Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BIfCmpGtInst(this.getOpType(), this.getTarget());
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public String getName() {
      return "ifcmpgt";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfCmpGtInst(this);
   }
}
