package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.AbstractInsnMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.InsnSubstitution;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.HashMap;
import java.util.Map;

class InvertNegsMethodVisitor extends AbstractInsnMutator {
   private static final String MESSAGE = "removed negation";
   private static final Map<Integer, ZeroOperandMutation> MUTATIONS = new HashMap();

   public InvertNegsMethodVisitor(MethodMutatorFactory factory, MethodInfo methodInfo, MutationContext context, MethodVisitor writer) {
      super(factory, methodInfo, context, writer);
   }

   protected Map<Integer, ZeroOperandMutation> getMutations() {
      return MUTATIONS;
   }

   static {
      MUTATIONS.put(116, new InsnSubstitution(0, "removed negation"));
      MUTATIONS.put(119, new InsnSubstitution(0, "removed negation"));
      MUTATIONS.put(118, new InsnSubstitution(0, "removed negation"));
      MUTATIONS.put(117, new InsnSubstitution(0, "removed negation"));
   }
}
