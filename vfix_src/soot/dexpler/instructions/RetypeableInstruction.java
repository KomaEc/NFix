package soot.dexpler.instructions;

import soot.Body;
import soot.Type;
import soot.dexpler.DexBody;

public interface RetypeableInstruction {
   void setRealType(DexBody var1, Type var2);

   void retype(Body var1);
}
