package soot.baf;

import soot.Type;

public interface InstanceCastInst extends Inst {
   Type getCastType();

   void setCastType(Type var1);
}
