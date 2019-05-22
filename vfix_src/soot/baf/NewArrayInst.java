package soot.baf;

import soot.Type;

public interface NewArrayInst extends Inst {
   Type getBaseType();

   void setBaseType(Type var1);
}
