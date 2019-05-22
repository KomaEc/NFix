package soot.baf;

import soot.Local;
import soot.Type;

public interface LoadInst extends Inst {
   Type getOpType();

   void setOpType(Type var1);

   Local getLocal();

   void setLocal(Local var1);
}
