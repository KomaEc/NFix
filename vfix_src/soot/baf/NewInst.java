package soot.baf;

import soot.RefType;

public interface NewInst extends Inst {
   RefType getBaseType();

   void setBaseType(RefType var1);
}
