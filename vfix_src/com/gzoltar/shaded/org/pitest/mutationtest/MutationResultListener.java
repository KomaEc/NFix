package com.gzoltar.shaded.org.pitest.mutationtest;

public interface MutationResultListener {
   void runStart();

   void handleMutationResult(ClassMutationResults var1);

   void runEnd();
}
