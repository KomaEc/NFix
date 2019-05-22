package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;

public class MemberVariableMutator implements MethodMutatorFactory {
   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new MemberVariableMutator.MemberVariableVisitor(context, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String toString() {
      return "EXPERIMENTAL_MEMBER_VARIABLE_MUTATOR";
   }

   public String getName() {
      return this.toString();
   }

   private final class MemberVariableVisitor extends MethodVisitor {
      private final MutationContext context;

      public MemberVariableVisitor(MutationContext context, MethodVisitor delegateVisitor) {
         super(327680, delegateVisitor);
         this.context = context;
      }

      public void visitFieldInsn(int opcode, String owner, String name, String desc) {
         if (181 == opcode && this.shouldMutate(name)) {
            if (Type.getType(desc).getSize() == 2) {
               super.visitInsn(88);
               super.visitInsn(87);
            } else {
               super.visitInsn(87);
               super.visitInsn(87);
            }
         } else {
            super.visitFieldInsn(opcode, owner, name, desc);
         }

      }

      public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
         super.visitMethodInsn(opcode, owner, name, desc, itf);
      }

      private boolean shouldMutate(String fieldName) {
         MutationIdentifier mutationId = this.context.registerMutation(MemberVariableMutator.this, "Removed assignment to member variable " + fieldName);
         return this.context.shouldMutate(mutationId);
      }
   }
}
