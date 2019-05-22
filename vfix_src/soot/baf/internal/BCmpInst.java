package soot.baf.internal;

import soot.Type;
import soot.baf.CmpInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BCmpInst extends AbstractOpTypeInst implements CmpInst {
   public BCmpInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public int getInMachineCount() {
      return 4;
   }

   public Object clone() {
      return new BCmpInst(this.getOpType());
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "cmp";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseCmpInst(this);
   }
}
