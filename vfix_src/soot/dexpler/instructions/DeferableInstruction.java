package soot.dexpler.instructions;

import soot.dexpler.DexBody;

public interface DeferableInstruction {
   void deferredJimplify(DexBody var1);
}
