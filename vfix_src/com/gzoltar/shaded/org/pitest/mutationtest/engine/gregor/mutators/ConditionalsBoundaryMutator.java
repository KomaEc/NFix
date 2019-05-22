package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public enum ConditionalsBoundaryMutator implements MethodMutatorFactory {
   CONDITIONALS_BOUNDARY_MUTATOR;

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new ConditionalsBoundaryMethodVisitor(this, context, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String getName() {
      return this.name();
   }
}
