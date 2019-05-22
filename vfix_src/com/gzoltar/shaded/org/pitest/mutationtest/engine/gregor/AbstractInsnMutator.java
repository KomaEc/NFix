package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Map;

public abstract class AbstractInsnMutator extends MethodVisitor {
   private final MethodMutatorFactory factory;
   private final MutationContext context;
   private final MethodInfo methodInfo;

   public AbstractInsnMutator(MethodMutatorFactory factory, MethodInfo methodInfo, MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.factory = factory;
      this.methodInfo = methodInfo;
      this.context = context;
   }

   protected abstract Map<Integer, ZeroOperandMutation> getMutations();

   public void visitInsn(int opcode) {
      if (this.canMutate(opcode)) {
         this.createMutationForInsnOpcode(opcode);
      } else {
         this.mv.visitInsn(opcode);
      }

   }

   private boolean canMutate(int opcode) {
      return this.getMutations().containsKey(opcode);
   }

   private void createMutationForInsnOpcode(int opcode) {
      ZeroOperandMutation mutation = (ZeroOperandMutation)this.getMutations().get(opcode);
      MutationIdentifier newId = this.context.registerMutation(this.factory, mutation.decribe(opcode, this.methodInfo));
      if (this.context.shouldMutate(newId)) {
         mutation.apply(opcode, this.mv);
      } else {
         this.applyUnmutatedInstruction(opcode);
      }

   }

   private void applyUnmutatedInstruction(int opcode) {
      this.mv.visitInsn(opcode);
   }
}
