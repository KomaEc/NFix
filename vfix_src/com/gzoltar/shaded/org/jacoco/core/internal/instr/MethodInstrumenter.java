package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.internal.flow.IFrame;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.LabelInfo;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.MethodProbesVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class MethodInstrumenter extends MethodProbesVisitor {
   private final IProbeInserter probeInserter;

   public MethodInstrumenter(MethodVisitor mv, IProbeInserter probeInserter) {
      super(mv);
      this.probeInserter = probeInserter;
   }

   public void visitProbe(int probeId) {
      this.probeInserter.insertProbe(probeId);
   }

   public void visitInsnWithProbe(int opcode, int probeId) {
      this.probeInserter.insertProbe(probeId);
      this.mv.visitInsn(opcode);
   }

   public void visitMethodInsnWithProbe(int opcode, String owner, String name, String desc, boolean itf, int probeId) {
      this.probeInserter.insertProbe(probeId);
      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitJumpInsnWithProbe(int opcode, Label label, int probeId, IFrame frame) {
      if (opcode == 167) {
         this.probeInserter.insertProbe(probeId);
         this.mv.visitJumpInsn(167, label);
      } else {
         Label intermediate = new Label();
         this.mv.visitJumpInsn(this.getInverted(opcode), intermediate);
         this.probeInserter.insertProbe(probeId);
         this.mv.visitJumpInsn(167, label);
         this.mv.visitLabel(intermediate);
         frame.accept(this.mv);
      }

   }

   private int getInverted(int opcode) {
      switch(opcode) {
      case 153:
         return 154;
      case 154:
         return 153;
      case 155:
         return 156;
      case 156:
         return 155;
      case 157:
         return 158;
      case 158:
         return 157;
      case 159:
         return 160;
      case 160:
         return 159;
      case 161:
         return 162;
      case 162:
         return 161;
      case 163:
         return 164;
      case 164:
         return 163;
      case 165:
         return 166;
      case 166:
         return 165;
      case 167:
      case 168:
      case 169:
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
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
      case 191:
      case 192:
      case 193:
      case 194:
      case 195:
      case 196:
      case 197:
      default:
         throw new IllegalArgumentException();
      case 198:
         return 199;
      case 199:
         return 198;
      }
   }

   public void visitTableSwitchInsnWithProbes(int min, int max, Label dflt, Label[] labels, IFrame frame) {
      LabelInfo.resetDone(dflt);
      LabelInfo.resetDone(labels);
      Label newDflt = this.createIntermediate(dflt);
      Label[] newLabels = this.createIntermediates(labels);
      this.mv.visitTableSwitchInsn(min, max, newDflt, newLabels);
      this.insertIntermediateProbes(dflt, labels, frame);
   }

   public void visitLookupSwitchInsnWithProbes(Label dflt, int[] keys, Label[] labels, IFrame frame) {
      LabelInfo.resetDone(dflt);
      LabelInfo.resetDone(labels);
      Label newDflt = this.createIntermediate(dflt);
      Label[] newLabels = this.createIntermediates(labels);
      this.mv.visitLookupSwitchInsn(newDflt, keys, newLabels);
      this.insertIntermediateProbes(dflt, labels, frame);
   }

   private Label[] createIntermediates(Label[] labels) {
      Label[] intermediates = new Label[labels.length];

      for(int i = 0; i < labels.length; ++i) {
         intermediates[i] = this.createIntermediate(labels[i]);
      }

      return intermediates;
   }

   private Label createIntermediate(Label label) {
      Label intermediate;
      if (LabelInfo.getProbeId(label) == -1) {
         intermediate = label;
      } else if (LabelInfo.isDone(label)) {
         intermediate = LabelInfo.getIntermediateLabel(label);
      } else {
         intermediate = new Label();
         LabelInfo.setIntermediateLabel(label, intermediate);
         LabelInfo.setDone(label);
      }

      return intermediate;
   }

   private void insertIntermediateProbe(Label label, IFrame frame) {
      int probeId = LabelInfo.getProbeId(label);
      if (probeId != -1 && !LabelInfo.isDone(label)) {
         this.mv.visitLabel(LabelInfo.getIntermediateLabel(label));
         frame.accept(this.mv);
         this.probeInserter.insertProbe(probeId);
         this.mv.visitJumpInsn(167, label);
         LabelInfo.setDone(label);
      }

   }

   private void insertIntermediateProbes(Label dflt, Label[] labels, IFrame frame) {
      LabelInfo.resetDone(dflt);
      LabelInfo.resetDone(labels);
      this.insertIntermediateProbe(dflt, frame);
      Label[] arr$ = labels;
      int len$ = labels.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Label l = arr$[i$];
         this.insertIntermediateProbe(l, frame);
      }

   }
}
