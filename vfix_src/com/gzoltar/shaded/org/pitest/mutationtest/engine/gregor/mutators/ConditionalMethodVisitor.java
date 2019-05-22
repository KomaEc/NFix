package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.AbstractJumpMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.HashMap;
import java.util.Map;

class ConditionalMethodVisitor extends AbstractJumpMutator {
   private static final String DESCRIPTION = "negated conditional";
   private static final Map<Integer, AbstractJumpMutator.Substitution> MUTATIONS = new HashMap();

   public ConditionalMethodVisitor(MethodMutatorFactory factory, MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(factory, context, delegateMethodVisitor);
   }

   protected Map<Integer, AbstractJumpMutator.Substitution> getMutations() {
      return MUTATIONS;
   }

   static {
      MUTATIONS.put(153, new AbstractJumpMutator.Substitution(154, "negated conditional"));
      MUTATIONS.put(154, new AbstractJumpMutator.Substitution(153, "negated conditional"));
      MUTATIONS.put(158, new AbstractJumpMutator.Substitution(157, "negated conditional"));
      MUTATIONS.put(156, new AbstractJumpMutator.Substitution(155, "negated conditional"));
      MUTATIONS.put(157, new AbstractJumpMutator.Substitution(158, "negated conditional"));
      MUTATIONS.put(155, new AbstractJumpMutator.Substitution(156, "negated conditional"));
      MUTATIONS.put(198, new AbstractJumpMutator.Substitution(199, "negated conditional"));
      MUTATIONS.put(199, new AbstractJumpMutator.Substitution(198, "negated conditional"));
      MUTATIONS.put(160, new AbstractJumpMutator.Substitution(159, "negated conditional"));
      MUTATIONS.put(159, new AbstractJumpMutator.Substitution(160, "negated conditional"));
      MUTATIONS.put(164, new AbstractJumpMutator.Substitution(163, "negated conditional"));
      MUTATIONS.put(162, new AbstractJumpMutator.Substitution(161, "negated conditional"));
      MUTATIONS.put(163, new AbstractJumpMutator.Substitution(164, "negated conditional"));
      MUTATIONS.put(161, new AbstractJumpMutator.Substitution(162, "negated conditional"));
      MUTATIONS.put(165, new AbstractJumpMutator.Substitution(166, "negated conditional"));
      MUTATIONS.put(166, new AbstractJumpMutator.Substitution(165, "negated conditional"));
   }
}
