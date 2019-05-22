package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.AbstractJumpMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.HashMap;
import java.util.Map;

class ConditionalsBoundaryMethodVisitor extends AbstractJumpMutator {
   private static final String DESCRIPTION = "changed conditional boundary";
   private static final Map<Integer, AbstractJumpMutator.Substitution> MUTATIONS = new HashMap();

   public ConditionalsBoundaryMethodVisitor(MethodMutatorFactory factory, MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(factory, context, delegateMethodVisitor);
   }

   protected Map<Integer, AbstractJumpMutator.Substitution> getMutations() {
      return MUTATIONS;
   }

   static {
      MUTATIONS.put(158, new AbstractJumpMutator.Substitution(155, "changed conditional boundary"));
      MUTATIONS.put(156, new AbstractJumpMutator.Substitution(157, "changed conditional boundary"));
      MUTATIONS.put(157, new AbstractJumpMutator.Substitution(156, "changed conditional boundary"));
      MUTATIONS.put(155, new AbstractJumpMutator.Substitution(158, "changed conditional boundary"));
      MUTATIONS.put(164, new AbstractJumpMutator.Substitution(161, "changed conditional boundary"));
      MUTATIONS.put(162, new AbstractJumpMutator.Substitution(163, "changed conditional boundary"));
      MUTATIONS.put(163, new AbstractJumpMutator.Substitution(162, "changed conditional boundary"));
      MUTATIONS.put(161, new AbstractJumpMutator.Substitution(164, "changed conditional boundary"));
   }
}
