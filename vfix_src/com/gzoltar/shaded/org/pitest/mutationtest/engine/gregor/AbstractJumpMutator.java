package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Map;

public abstract class AbstractJumpMutator extends MethodVisitor {
   private final MethodMutatorFactory factory;
   private final MutationContext context;

   public AbstractJumpMutator(MethodMutatorFactory factory, MutationContext context, MethodVisitor writer) {
      super(327680, writer);
      this.factory = factory;
      this.context = context;
   }

   protected abstract Map<Integer, AbstractJumpMutator.Substitution> getMutations();

   public void visitJumpInsn(int opcode, Label label) {
      if (this.canMutate(opcode)) {
         this.createMutationForJumpInsn(opcode, label);
      } else {
         this.mv.visitJumpInsn(opcode, label);
      }

   }

   private boolean canMutate(int opcode) {
      return this.getMutations().containsKey(opcode);
   }

   private void createMutationForJumpInsn(int opcode, Label label) {
      AbstractJumpMutator.Substitution substitution = (AbstractJumpMutator.Substitution)this.getMutations().get(opcode);
      MutationIdentifier newId = this.context.registerMutation(this.factory, substitution.description);
      if (this.context.shouldMutate(newId)) {
         this.mv.visitJumpInsn(substitution.newCode, label);
      } else {
         this.mv.visitJumpInsn(opcode, label);
      }

   }

   public static class Substitution {
      private final int newCode;
      private final String description;

      public Substitution(int newCode, String description) {
         this.newCode = newCode;
         this.description = description;
      }
   }
}
