package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public interface MethodMutatorFactory {
   MethodVisitor create(MutationContext var1, MethodInfo var2, MethodVisitor var3);

   String getGloballyUniqueId();

   String getName();
}
