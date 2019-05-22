package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesVisitor;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.MethodProbesVisitor;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.InstrSupport;
import com.gzoltar.shaded.org.objectweb.asm.FieldVisitor;

public class ClassAnalyzer extends ClassProbesVisitor {
   private final ClassCoverageImpl coverage;
   private final boolean[] probes;
   private final StringPool stringPool;

   public ClassAnalyzer(ClassCoverageImpl coverage, boolean[] probes, StringPool stringPool) {
      this.coverage = coverage;
      this.probes = probes;
      this.stringPool = stringPool;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.coverage.setSignature(this.stringPool.get(signature));
      this.coverage.setSuperName(this.stringPool.get(superName));
      this.coverage.setInterfaces(this.stringPool.get(interfaces));
   }

   public void visitSource(String source, String debug) {
      this.coverage.setSourceFileName(this.stringPool.get(source));
   }

   public MethodProbesVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      InstrSupport.assertNotInstrumented(name, this.coverage.getName());
      return new MethodAnalyzer(this.coverage.getName(), this.coverage.getSuperName(), this.stringPool.get(name), this.stringPool.get(desc), this.stringPool.get(signature), this.probes) {
         public void visitEnd() {
            super.visitEnd();
            IMethodCoverage methodCoverage = this.getCoverage();
            if (methodCoverage.getInstructionCounter().getTotalCount() > 0) {
               ClassAnalyzer.this.coverage.addMethod(methodCoverage);
            }

         }
      };
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      InstrSupport.assertNotInstrumented(name, this.coverage.getName());
      return super.visitField(access, name, desc, signature, value);
   }

   public void visitTotalProbeCount(int count) {
   }
}
