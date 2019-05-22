package soot.baf;

import soot.jimple.Constant;

public interface PushInst extends Inst {
   Constant getConstant();

   void setConstant(Constant var1);
}
