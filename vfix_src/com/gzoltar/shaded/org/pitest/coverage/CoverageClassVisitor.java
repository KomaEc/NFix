package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.BridgeMethodFilter;
import com.gzoltar.shaded.org.pitest.classinfo.MethodFilteringAdapter;
import com.gzoltar.shaded.org.pitest.coverage.analysis.CoverageAnalyser;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassWriter;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import sun.pitest.CodeCoverageStore;

public class CoverageClassVisitor extends MethodFilteringAdapter {
   private final int classId;
   private int probeCount = 0;

   public CoverageClassVisitor(int classId, ClassWriter writer) {
      super(writer, BridgeMethodFilter.INSTANCE);
      this.classId = classId;
   }

   public void registerProbes(int number) {
      this.probeCount += number;
   }

   public MethodVisitor visitMethodIfRequired(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor methodVisitor) {
      return new CoverageAnalyser(this, this.classId, this.probeCount, methodVisitor, access, name, desc, signature, exceptions);
   }

   public void visitEnd() {
      CodeCoverageStore.registerClassProbes(this.classId, this.probeCount);
   }
}
