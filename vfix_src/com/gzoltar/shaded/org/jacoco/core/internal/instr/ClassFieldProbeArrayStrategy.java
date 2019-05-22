package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.runtime.IExecutionDataAccessorGenerator;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class ClassFieldProbeArrayStrategy implements IProbeArrayStrategy {
   private static final Object[] FRAME_STACK_ARRZ = new Object[]{"[Z"};
   private static final Object[] FRAME_LOCALS_EMPTY = new Object[0];
   private final String className;
   private final long classId;
   private final boolean withFrames;
   private final IExecutionDataAccessorGenerator accessorGenerator;

   ClassFieldProbeArrayStrategy(String className, long classId, boolean withFrames, IExecutionDataAccessorGenerator accessorGenerator) {
      this.className = className;
      this.classId = classId;
      this.withFrames = withFrames;
      this.accessorGenerator = accessorGenerator;
   }

   public int storeInstance(MethodVisitor mv, boolean clinit, int variable) {
      mv.visitMethodInsn(184, this.className, "$jacocoInit", "()[Z", false);
      mv.visitVarInsn(58, variable);
      return 1;
   }

   public void addMembers(ClassVisitor cv, int probeCount) {
      this.createDataField(cv);
      this.createInitMethod(cv, probeCount);
   }

   private void createDataField(ClassVisitor cv) {
      cv.visitField(4234, "$jacocoData", "[Z", (String)null, (Object)null);
   }

   private void createInitMethod(ClassVisitor cv, int probeCount) {
      MethodVisitor mv = cv.visitMethod(4106, "$jacocoInit", "()[Z", (String)null, (String[])null);
      mv.visitCode();
      mv.visitFieldInsn(178, this.className, "$jacocoData", "[Z");
      mv.visitInsn(89);
      Label alreadyInitialized = new Label();
      mv.visitJumpInsn(199, alreadyInitialized);
      mv.visitInsn(87);
      int size = this.genInitializeDataField(mv, probeCount);
      if (this.withFrames) {
         mv.visitFrame(-1, 0, FRAME_LOCALS_EMPTY, 1, FRAME_STACK_ARRZ);
      }

      mv.visitLabel(alreadyInitialized);
      mv.visitInsn(176);
      mv.visitMaxs(Math.max(size, 2), 0);
      mv.visitEnd();
   }

   private int genInitializeDataField(MethodVisitor mv, int probeCount) {
      int size = this.accessorGenerator.generateDataAccessor(this.classId, this.className, probeCount, mv);
      mv.visitInsn(89);
      mv.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
      return Math.max(size, 2);
   }
}
