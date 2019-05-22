package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;

public abstract class MethodProbesVisitor extends MethodVisitor {
   public MethodProbesVisitor() {
      this((MethodVisitor)null);
   }

   public MethodProbesVisitor(MethodVisitor mv) {
      super(327680, mv);
   }

   public void visitProbe(int probeId) {
   }

   public void visitJumpInsnWithProbe(int opcode, Label label, int probeId, IFrame frame) {
   }

   public void visitMethodInsnWithProbe(int opcode, String owner, String name, String desc, boolean itf, int probeId) {
   }

   public void visitInsnWithProbe(int opcode, int probeId) {
   }

   public void visitTableSwitchInsnWithProbes(int min, int max, Label dflt, Label[] labels, IFrame frame) {
   }

   public void visitLookupSwitchInsnWithProbes(Label dflt, int[] keys, Label[] labels, IFrame frame) {
   }

   public void accept(MethodNode methodNode, MethodVisitor methodVisitor) {
      methodNode.accept(methodVisitor);
   }
}
