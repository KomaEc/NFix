package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class InsnSubstitution implements ZeroOperandMutation {
   private final int replacementOpcode;
   private final String message;

   public InsnSubstitution(int replacementOpcode, String message) {
      this.replacementOpcode = replacementOpcode;
      this.message = message;
   }

   public void apply(int opCode, MethodVisitor mv) {
      mv.visitInsn(this.replacementOpcode);
   }

   public String decribe(int opCode, MethodInfo methodInfo) {
      return this.message;
   }
}
