package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.objectweb.asm.Handle;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class DuplicateFrameEliminator extends MethodVisitor {
   private boolean instruction = true;

   public DuplicateFrameEliminator(MethodVisitor mv) {
      super(327680, mv);
   }

   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      if (this.instruction) {
         this.instruction = false;
         this.mv.visitFrame(type, nLocal, local, nStack, stack);
      }

   }

   public void visitInsn(int opcode) {
      this.instruction = true;
      this.mv.visitInsn(opcode);
   }

   public void visitIntInsn(int opcode, int operand) {
      this.instruction = true;
      this.mv.visitIntInsn(opcode, operand);
   }

   public void visitVarInsn(int opcode, int var) {
      this.instruction = true;
      this.mv.visitVarInsn(opcode, var);
   }

   public void visitTypeInsn(int opcode, String type) {
      this.instruction = true;
      this.mv.visitTypeInsn(opcode, type);
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.instruction = true;
      this.mv.visitFieldInsn(opcode, owner, name, desc);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.instruction = true;
      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.instruction = true;
      this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
   }

   public void visitJumpInsn(int opcode, Label label) {
      this.instruction = true;
      this.mv.visitJumpInsn(opcode, label);
   }

   public void visitLdcInsn(Object cst) {
      this.instruction = true;
      this.mv.visitLdcInsn(cst);
   }

   public void visitIincInsn(int var, int increment) {
      this.instruction = true;
      this.mv.visitIincInsn(var, increment);
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.instruction = true;
      this.mv.visitTableSwitchInsn(min, max, dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.instruction = true;
      this.mv.visitLookupSwitchInsn(dflt, keys, labels);
   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.instruction = true;
      this.mv.visitMultiANewArrayInsn(desc, dims);
   }
}
