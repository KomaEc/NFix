package soot.baf;

import soot.Local;
import soot.jimple.Constant;

public interface IncInst extends Inst {
   Constant getConstant();

   void setConstant(Constant var1);

   void setLocal(Local var1);

   Local getLocal();
}
