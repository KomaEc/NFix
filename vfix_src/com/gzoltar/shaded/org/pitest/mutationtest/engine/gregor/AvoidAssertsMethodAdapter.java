package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class AvoidAssertsMethodAdapter extends MethodVisitor {
   private static final String DISABLE_REASON = "ASSERTS";
   private final MutationContext context;
   private boolean assertBlockStarted;
   private Label destination;

   public AvoidAssertsMethodAdapter(MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.context = context;
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (opcode == 182 && "java/lang/Class".equals(owner) && "desiredAssertionStatus".equals(name)) {
         this.context.disableMutations("ASSERTS");
      }

      super.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      if ("$assertionsDisabled".equals(name)) {
         if (opcode == 178) {
            this.context.disableMutations("ASSERTS");
            this.assertBlockStarted = true;
         } else if (opcode == 179) {
            this.context.enableMutatations("ASSERTS");
         }
      }

      super.visitFieldInsn(opcode, owner, name, desc);
   }

   public void visitJumpInsn(int opcode, Label destination) {
      if (opcode == 154 && this.assertBlockStarted) {
         this.destination = destination;
         this.assertBlockStarted = false;
      }

      super.visitJumpInsn(opcode, destination);
   }

   public void visitLabel(Label label) {
      super.visitLabel(label);
      if (this.destination == label) {
         this.context.enableMutatations("ASSERTS");
         this.destination = null;
      }

   }
}
