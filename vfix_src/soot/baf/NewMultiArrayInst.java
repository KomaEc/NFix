package soot.baf;

import soot.ArrayType;

public interface NewMultiArrayInst extends Inst {
   ArrayType getBaseType();

   void setBaseType(ArrayType var1);

   int getDimensionCount();

   void setDimensionCount(int var1);
}
