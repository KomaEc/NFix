package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.commons.AnalyzerAdapter;

public class ClassProbesAdapter extends ClassVisitor implements IProbeIdGenerator {
   private static final MethodProbesVisitor EMPTY_METHOD_PROBES_VISITOR = new MethodProbesVisitor() {
   };
   private final ClassProbesVisitor cv;
   private final boolean trackFrames;
   private int counter = 0;
   private String name;

   public ClassProbesAdapter(ClassProbesVisitor cv, boolean trackFrames) {
      super(327680, cv);
      this.cv = cv;
      this.trackFrames = trackFrames;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.name = name;
      super.visit(version, access, name, signature, superName, interfaces);
   }

   public final MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodProbesVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
      final MethodProbesVisitor methodProbes;
      if (mv == null) {
         methodProbes = EMPTY_METHOD_PROBES_VISITOR;
      } else {
         methodProbes = mv;
      }

      return new MethodSanitizer((MethodVisitor)null, access, name, desc, signature, exceptions) {
         public void visitEnd() {
            super.visitEnd();
            LabelFlowAnalyzer.markLabels(this);
            MethodProbesAdapter probesAdapter = new MethodProbesAdapter(methodProbes, ClassProbesAdapter.this);
            if (ClassProbesAdapter.this.trackFrames) {
               AnalyzerAdapter analyzer = new AnalyzerAdapter(ClassProbesAdapter.this.name, this.access, this.name, this.desc, probesAdapter);
               probesAdapter.setAnalyzer(analyzer);
               methodProbes.accept(this, analyzer);
            } else {
               methodProbes.accept(this, probesAdapter);
            }

         }
      };
   }

   public void visitEnd() {
      this.cv.visitTotalProbeCount(this.counter);
      super.visitEnd();
   }

   public int nextId() {
      return this.counter++;
   }
}
