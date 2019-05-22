package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.EnumFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.IFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.IFilterOutput;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.LombokGeneratedFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.PrivateEmptyNoArgConstructorFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.SynchronizedFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.SyntheticFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.TryWithResourcesEcjFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter.TryWithResourcesJavacFilter;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.IFrame;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.Instruction;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.LabelInfo;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.MethodProbesVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Handle;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MethodAnalyzer extends MethodProbesVisitor implements IFilterOutput {
   private static final IFilter[] FILTERS = new IFilter[]{new EnumFilter(), new SyntheticFilter(), new SynchronizedFilter(), new TryWithResourcesJavacFilter(), new TryWithResourcesEcjFilter(), new PrivateEmptyNoArgConstructorFilter(), new LombokGeneratedFilter()};
   private final String className;
   private final String superClassName;
   private final boolean[] probes;
   private final MethodCoverageImpl coverage;
   private int currentLine = -1;
   private int firstLine = -1;
   private int lastLine = -1;
   private final List<Label> currentLabel = new ArrayList(2);
   private final List<Instruction> instructions = new ArrayList();
   private final List<Instruction> coveredProbes = new ArrayList();
   private final List<MethodAnalyzer.Jump> jumps = new ArrayList();
   private Instruction lastInsn;
   private final Set<AbstractInsnNode> ignored = new HashSet();
   private AbstractInsnNode currentNode;

   public MethodAnalyzer(String className, String superClassName, String name, String desc, String signature, boolean[] probes) {
      this.className = className;
      this.superClassName = superClassName;
      this.probes = probes;
      this.coverage = new MethodCoverageImpl(name, desc, signature);
   }

   public IMethodCoverage getCoverage() {
      return this.coverage;
   }

   public void accept(MethodNode methodNode, MethodVisitor methodVisitor) {
      this.ignored.clear();
      IFilter[] arr$ = FILTERS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IFilter filter = arr$[i$];
         filter.filter(this.className, this.superClassName, methodNode, this);
      }

      Iterator i$ = methodNode.tryCatchBlocks.iterator();

      while(i$.hasNext()) {
         TryCatchBlockNode n = (TryCatchBlockNode)i$.next();
         n.accept(methodVisitor);
      }

      for(this.currentNode = methodNode.instructions.getFirst(); this.currentNode != null; this.currentNode = this.currentNode.getNext()) {
         this.currentNode.accept(methodVisitor);
      }

      methodVisitor.visitEnd();
   }

   public void ignore(AbstractInsnNode fromInclusive, AbstractInsnNode toInclusive) {
      for(AbstractInsnNode i = fromInclusive; i != toInclusive; i = i.getNext()) {
         this.ignored.add(i);
      }

      this.ignored.add(toInclusive);
   }

   public void visitLabel(Label label) {
      this.currentLabel.add(label);
      if (!LabelInfo.isSuccessor(label)) {
         this.lastInsn = null;
      }

   }

   public void visitLineNumber(int line, Label start) {
      this.currentLine = line;
      if (this.firstLine > line || this.lastLine == -1) {
         this.firstLine = line;
      }

      if (this.lastLine < line) {
         this.lastLine = line;
      }

   }

   private void visitInsn() {
      Instruction insn = new Instruction(this.currentNode, this.currentLine);
      this.instructions.add(insn);
      if (this.lastInsn != null) {
         insn.setPredecessor(this.lastInsn);
      }

      int labelCount = this.currentLabel.size();
      if (labelCount > 0) {
         int i = labelCount;

         while(true) {
            --i;
            if (i < 0) {
               this.currentLabel.clear();
               break;
            }

            LabelInfo.setInstruction((Label)this.currentLabel.get(i), insn);
         }
      }

      this.lastInsn = insn;
   }

   public void visitInsn(int opcode) {
      this.visitInsn();
   }

   public void visitIntInsn(int opcode, int operand) {
      this.visitInsn();
   }

   public void visitVarInsn(int opcode, int var) {
      this.visitInsn();
   }

   public void visitTypeInsn(int opcode, String type) {
      this.visitInsn();
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.visitInsn();
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.visitInsn();
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.visitInsn();
   }

   public void visitJumpInsn(int opcode, Label label) {
      this.visitInsn();
      this.jumps.add(new MethodAnalyzer.Jump(this.lastInsn, label));
   }

   public void visitLdcInsn(Object cst) {
      this.visitInsn();
   }

   public void visitIincInsn(int var, int increment) {
      this.visitInsn();
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.visitSwitchInsn(dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.visitSwitchInsn(dflt, labels);
   }

   private void visitSwitchInsn(Label dflt, Label[] labels) {
      this.visitInsn();
      LabelInfo.resetDone(labels);
      this.jumps.add(new MethodAnalyzer.Jump(this.lastInsn, dflt));
      LabelInfo.setDone(dflt);
      Label[] arr$ = labels;
      int len$ = labels.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Label l = arr$[i$];
         if (!LabelInfo.isDone(l)) {
            this.jumps.add(new MethodAnalyzer.Jump(this.lastInsn, l));
            LabelInfo.setDone(l);
         }
      }

   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.visitInsn();
   }

   public void visitProbe(int probeId) {
      this.addProbeWithBranch(probeId);
      this.lastInsn = null;
   }

   public void visitMethodInsnWithProbe(int opcode, String owner, String name, String desc, boolean itf, int probeId) {
      this.visitInsn();
      this.addProbeWithoutBranch(probeId);
   }

   public void visitJumpInsnWithProbe(int opcode, Label label, int probeId, IFrame frame) {
      this.visitInsn();
      this.addProbeWithBranch(probeId);
   }

   public void visitInsnWithProbe(int opcode, int probeId) {
      this.visitInsn();
      this.addProbeWithBranch(probeId);
   }

   public void visitTableSwitchInsnWithProbes(int min, int max, Label dflt, Label[] labels, IFrame frame) {
      this.visitSwitchInsnWithProbes(dflt, labels);
   }

   public void visitLookupSwitchInsnWithProbes(Label dflt, int[] keys, Label[] labels, IFrame frame) {
      this.visitSwitchInsnWithProbes(dflt, labels);
   }

   private void visitSwitchInsnWithProbes(Label dflt, Label[] labels) {
      this.visitInsn();
      LabelInfo.resetDone(dflt);
      LabelInfo.resetDone(labels);
      this.visitSwitchTarget(dflt);
      Label[] arr$ = labels;
      int len$ = labels.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Label l = arr$[i$];
         this.visitSwitchTarget(l);
      }

   }

   private void visitSwitchTarget(Label label) {
      int id = LabelInfo.getProbeId(label);
      if (!LabelInfo.isDone(label)) {
         if (id == -1) {
            this.jumps.add(new MethodAnalyzer.Jump(this.lastInsn, label));
         } else {
            this.addProbeWithBranch(id);
         }

         LabelInfo.setDone(label);
      }

   }

   public void visitEnd() {
      Iterator i$ = this.jumps.iterator();

      while(i$.hasNext()) {
         MethodAnalyzer.Jump j = (MethodAnalyzer.Jump)i$.next();
         LabelInfo.getInstruction(j.target).setPredecessor(j.source);
      }

      i$ = this.coveredProbes.iterator();

      Instruction i;
      while(i$.hasNext()) {
         i = (Instruction)i$.next();
         i.setCovered();
      }

      this.coverage.ensureCapacity(this.firstLine, this.lastLine);
      i$ = this.instructions.iterator();

      while(i$.hasNext()) {
         i = (Instruction)i$.next();
         if (!this.ignored.contains(i.getNode())) {
            int total = i.getBranches();
            int covered = i.getCoveredBranches();
            ICounter instrCounter = covered == 0 ? CounterImpl.COUNTER_1_0 : CounterImpl.COUNTER_0_1;
            ICounter branchCounter = total > 1 ? CounterImpl.getInstance(total - covered, covered) : CounterImpl.COUNTER_0_0;
            this.coverage.increment(instrCounter, branchCounter, i.getLine());
         }
      }

      this.coverage.incrementMethodCounter();
   }

   private void addProbeWithBranch(int probeId) {
      this.addBranch();
      this.addProbeWithoutBranch(probeId);
   }

   private void addProbeWithoutBranch(int probeId) {
      if (this.probes != null && this.probes[probeId]) {
         this.coveredProbes.add(this.lastInsn);
      }

   }

   private void addBranch() {
      this.lastInsn.addBranch();
   }

   private static class Jump {
      final Instruction source;
      final Label target;

      Jump(Instruction source, Label target) {
         this.source = source;
         this.target = target;
      }
   }
}
