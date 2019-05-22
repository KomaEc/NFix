package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class IncrementsMethodVisitor extends MethodVisitor {
   private final MethodMutatorFactory factory;
   private final MutationContext context;

   public IncrementsMethodVisitor(MethodMutatorFactory factory, MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.factory = factory;
      this.context = context;
   }

   public void visitIincInsn(int var, int increment) {
      MutationIdentifier newId = this.context.registerMutation(this.factory, "Changed increment from " + increment + " to " + -increment);
      if (this.context.shouldMutate(newId)) {
         this.mv.visitIincInsn(var, -increment);
      } else {
         this.mv.visitIincInsn(var, increment);
      }

   }
}
