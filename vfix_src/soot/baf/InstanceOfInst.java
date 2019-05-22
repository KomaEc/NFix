package soot.baf;

import soot.Type;

public interface InstanceOfInst extends Inst {
   Type getCheckType();

   void setCheckType(Type var1);
}
