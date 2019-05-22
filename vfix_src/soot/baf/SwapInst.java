package soot.baf;

import soot.Type;

public interface SwapInst extends Inst {
   Type getFromType();

   void setFromType(Type var1);

   Type getToType();

   void setToType(Type var1);
}
