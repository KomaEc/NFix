package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis;

import com.gzoltar.shaded.org.pitest.reloc.asm.Handle;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class InstructionTrackingMethodVisitor extends MethodVisitor {
   private final InstructionCounter count;

   public InstructionTrackingMethodVisitor(MethodVisitor mv, InstructionCounter count) {
      super(327680, mv);
      this.count = count;
   }

   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      this.count.increment();
      super.visitFrame(type, nLocal, local, nStack, stack);
   }

   public void visitInsn(int opcode) {
      this.count.increment();
      super.visitInsn(opcode);
   }

   public void visitIntInsn(int opcode, int operand) {
      this.count.increment();
      super.visitIntInsn(opcode, operand);
   }

   public void visitVarInsn(int opcode, int var) {
      this.count.increment();
      super.visitVarInsn(opcode, var);
   }

   public void visitTypeInsn(int opcode, String type) {
      this.count.increment();
      super.visitTypeInsn(opcode, type);
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.count.increment();
      super.visitFieldInsn(opcode, owner, name, desc);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.count.increment();
      super.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.count.increment();
      super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
   }

   public void visitJumpInsn(int opcode, Label label) {
      this.count.increment();
      super.visitJumpInsn(opcode, label);
   }

   public void visitLabel(Label label) {
      this.count.increment();
      super.visitLabel(label);
   }

   public void visitLdcInsn(Object cst) {
      this.count.increment();
      super.visitLdcInsn(cst);
   }

   public void visitIincInsn(int var, int increment) {
      this.count.increment();
      super.visitIincInsn(var, increment);
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.count.increment();
      super.visitTableSwitchInsn(min, max, dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.count.increment();
      super.visitLookupSwitchInsn(dflt, keys, labels);
   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.count.increment();
      super.visitMultiANewArrayInsn(desc, dims);
   }

   public void visitLineNumber(int line, Label start) {
      this.count.increment();
      super.visitLineNumber(line, start);
   }
}
