package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesVisitor;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.MethodProbesVisitor;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.FieldVisitor;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public class ClassInstrumenter extends ClassProbesVisitor {
   private final IProbeArrayStrategy probeArrayStrategy;
   private String className;

   public ClassInstrumenter(IProbeArrayStrategy probeArrayStrategy, ClassVisitor cv) {
      super(cv);
      this.probeArrayStrategy = probeArrayStrategy;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.className = name;
      super.visit(version, access, name, signature, superName, interfaces);
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      InstrSupport.assertNotInstrumented(name, this.className);
      return super.visitField(access, name, desc, signature, value);
   }

   public MethodProbesVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      InstrSupport.assertNotInstrumented(name, this.className);
      MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
      if (mv == null) {
         return null;
      } else {
         MethodVisitor frameEliminator = new DuplicateFrameEliminator(mv);
         ProbeInserter probeVariableInserter = new ProbeInserter(access, name, desc, frameEliminator, this.probeArrayStrategy);
         return new MethodInstrumenter(probeVariableInserter, probeVariableInserter);
      }
   }

   public void visitTotalProbeCount(int count) {
      this.probeArrayStrategy.addMembers(this.cv, count);
   }
}
