package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.Handle;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;

public final class LabelFlowAnalyzer extends MethodVisitor {
   boolean successor = false;
   boolean first = true;
   Label lineStart = null;

   public static void markLabels(MethodNode method) {
      MethodVisitor lfa = new LabelFlowAnalyzer();
      int i = method.tryCatchBlocks.size();

      while(true) {
         --i;
         if (i < 0) {
            method.instructions.accept(lfa);
            return;
         }

         ((TryCatchBlockNode)method.tryCatchBlocks.get(i)).accept(lfa);
      }
   }

   public LabelFlowAnalyzer() {
      super(327680);
   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      LabelInfo.setTarget(start);
      LabelInfo.setTarget(handler);
   }

   public void visitJumpInsn(int opcode, Label label) {
      LabelInfo.setTarget(label);
      if (opcode == 168) {
         throw new AssertionError("Subroutines not supported.");
      } else {
         this.successor = opcode != 167;
         this.first = false;
      }
   }

   public void visitLabel(Label label) {
      if (this.first) {
         LabelInfo.setTarget(label);
      }

      if (this.successor) {
         LabelInfo.setSuccessor(label);
      }

   }

   public void visitLineNumber(int line, Label start) {
      this.lineStart = start;
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.visitSwitchInsn(dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.visitSwitchInsn(dflt, labels);
   }

   private void visitSwitchInsn(Label dflt, Label[] labels) {
      LabelInfo.resetDone(dflt);
      LabelInfo.resetDone(labels);
      setTargetIfNotDone(dflt);
      Label[] arr$ = labels;
      int len$ = labels.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Label l = arr$[i$];
         setTargetIfNotDone(l);
      }

      this.successor = false;
      this.first = false;
   }

   private static void setTargetIfNotDone(Label label) {
      if (!LabelInfo.isDone(label)) {
         LabelInfo.setTarget(label);
         LabelInfo.setDone(label);
      }

   }

   public void visitInsn(int opcode) {
      switch(opcode) {
      case 169:
         throw new AssertionError("Subroutines not supported.");
      case 170:
      case 171:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
      case 184:
      case 185:
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      default:
         this.successor = true;
         break;
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
      case 191:
         this.successor = false;
      }

      this.first = false;
   }

   public void visitIntInsn(int opcode, int operand) {
      this.successor = true;
      this.first = false;
   }

   public void visitVarInsn(int opcode, int var) {
      this.successor = true;
      this.first = false;
   }

   public void visitTypeInsn(int opcode, String type) {
      this.successor = true;
      this.first = false;
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.successor = true;
      this.first = false;
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.successor = true;
      this.first = false;
      this.markMethodInvocationLine();
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.successor = true;
      this.first = false;
      this.markMethodInvocationLine();
   }

   private void markMethodInvocationLine() {
      if (this.lineStart != null) {
         LabelInfo.setMethodInvocationLine(this.lineStart);
      }

   }

   public void visitLdcInsn(Object cst) {
      this.successor = true;
      this.first = false;
   }

   public void visitIincInsn(int var, int increment) {
      this.successor = true;
      this.first = false;
   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.successor = true;
      this.first = false;
   }
}
