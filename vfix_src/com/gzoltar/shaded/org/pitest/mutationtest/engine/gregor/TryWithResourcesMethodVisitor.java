package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TryWithResourcesMethodVisitor extends MethodVisitor {
   private static final List<Integer> JAVAC_CLASS_INS_SEQUENCE = Arrays.asList(58, 25, 198, 25, 198, 25, 182, 167, 58, 25, 25, 182, 167, 25, 182, 25, 191);
   private static final List<Integer> JAVAC_INTERFACE_INS_SEQUENCE = Arrays.asList(58, 25, 198, 25, 198, 25, 185, 167, 58, 25, 25, 182, 167, 25, 185, 25, 191);
   private static final List<Integer> ECJ_INS_SEQUENCE = Arrays.asList(58, 25, 199, 25, 58, 167, 25, 25, 165, 25, 25, 182, 25, 191);
   private final PremutationClassInfo context;
   private final List<Integer> opcodesStack = new ArrayList();
   private int currentLineNumber;

   public TryWithResourcesMethodVisitor(PremutationClassInfo context) {
      super(327680);
      this.context = context;
   }

   public void visitLineNumber(int line, Label start) {
      this.prepareToStartTracking();
      this.currentLineNumber = line;
      super.visitLineNumber(line, start);
   }

   public void visitVarInsn(int opcode, int var) {
      this.opcodesStack.add(opcode);
      super.visitVarInsn(opcode, var);
   }

   public void visitJumpInsn(int opcode, Label label) {
      this.opcodesStack.add(opcode);
      super.visitJumpInsn(opcode, label);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.opcodesStack.add(opcode);
      super.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitInsn(int opcode) {
      if (opcode == 191) {
         this.opcodesStack.add(opcode);
         this.finishTracking();
      }

      super.visitInsn(opcode);
   }

   private void finishTracking() {
      if (JAVAC_CLASS_INS_SEQUENCE.equals(this.opcodesStack) || JAVAC_INTERFACE_INS_SEQUENCE.equals(this.opcodesStack) || ECJ_INS_SEQUENCE.equals(this.opcodesStack)) {
         this.context.registerLineToAvoid(this.currentLineNumber);
      }

      this.prepareToStartTracking();
   }

   private void prepareToStartTracking() {
      if (!this.opcodesStack.isEmpty()) {
         this.opcodesStack.clear();
      }

   }
}
