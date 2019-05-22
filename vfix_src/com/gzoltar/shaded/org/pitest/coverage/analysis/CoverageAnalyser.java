package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.coverage.CoverageClassVisitor;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.DefaultInstructionCounter;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionTrackingMethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.MethodNode;
import java.util.List;
import sun.pitest.CodeCoverageStore;

public class CoverageAnalyser extends MethodNode {
   private static final int MAX_SUPPORTED_LOCAL_PROBES = 15;
   private final CoverageClassVisitor parent;
   private final int classId;
   private final MethodVisitor mv;
   private final int probeOffset;

   public CoverageAnalyser(CoverageClassVisitor parent, int classId, int probeOffset, MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
      super(327680, access, name, desc, signature, exceptions);
      this.mv = mv;
      this.parent = parent;
      this.classId = classId;
      this.probeOffset = probeOffset;
   }

   public void visitEnd() {
      List<Block> blocks = this.findRequriedProbeLocations();
      this.parent.registerProbes(blocks.size());
      int blockCount = blocks.size();
      CodeCoverageStore.registerMethod(this.classId, this.name, this.desc, this.probeOffset, this.probeOffset + blocks.size() - 1);
      DefaultInstructionCounter counter = new DefaultInstructionCounter();
      if (blockCount != 1 && !this.name.equals("<init>")) {
         if (blockCount <= 15 && blockCount >= 1) {
            this.accept(new InstructionTrackingMethodVisitor(new LocalVariableCoverageMethodVisitor(blocks, counter, this.classId, this.mv, this.access, this.name, this.desc, this.probeOffset), counter));
         } else {
            this.accept(new InstructionTrackingMethodVisitor(new ArrayProbeCoverageMethodVisitor(blocks, counter, this.classId, this.mv, this.access, this.name, this.desc, this.probeOffset), counter));
         }
      } else {
         this.accept(new InstructionTrackingMethodVisitor(new SimpleBlockCoverageVisitor(blocks, counter, this.classId, this.mv, this.access, this.name, this.desc, this.probeOffset), counter));
      }

   }

   private List<Block> findRequriedProbeLocations() {
      ControlFlowAnalyser cfa = new ControlFlowAnalyser();
      return cfa.analyze(this);
   }
}
