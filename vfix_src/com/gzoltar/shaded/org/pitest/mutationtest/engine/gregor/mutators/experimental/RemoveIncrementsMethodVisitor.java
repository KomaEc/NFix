package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class RemoveIncrementsMethodVisitor extends MethodVisitor {
   private final MethodMutatorFactory factory;
   private final MutationContext context;

   public RemoveIncrementsMethodVisitor(MethodMutatorFactory factory, MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.factory = factory;
      this.context = context;
   }

   public void visitIincInsn(int var, int increment) {
      MutationIdentifier newId = this.context.registerMutation(this.factory, "Removed increment " + increment);
      if (this.context.shouldMutate(newId)) {
         this.mv.visitInsn(0);
      } else {
         this.mv.visitIincInsn(var, increment);
      }

   }
}
