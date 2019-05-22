package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.blocks.BlockCounter;

public interface MutationContext extends BlockCounter {
   void registerCurrentLine(int var1);

   ClassInfo getClassInfo();

   MutationIdentifier registerMutation(MethodMutatorFactory var1, String var2);

   boolean shouldMutate(MutationIdentifier var1);

   void disableMutations(String var1);

   void enableMutatations(String var1);
}
