package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfCmpEqInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfCmpEqInst extends AbstractOpTypeBranchInst implements IfCmpEqInst {
   public BIfCmpEqInst(Type opType, Unit target) {
      super(opType, Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BIfCmpEqInst(this.getOpType(), this.getTarget());
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
      return "ifcmpeq";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfCmpEqInst(this);
   }
}
