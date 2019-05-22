package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.runtime.IExecutionDataAccessorGenerator;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class InterfaceFieldProbeArrayStrategy implements IProbeArrayStrategy {
   private static final Object[] FRAME_STACK_ARRZ = new Object[]{"[Z"};
   private static final Object[] FRAME_LOCALS_EMPTY = new Object[0];
   private final String className;
   private final long classId;
   private final int probeCount;
   private final IExecutionDataAccessorGenerator accessorGenerator;
   private boolean seenClinit = false;

   InterfaceFieldProbeArrayStrategy(String className, long classId, int probeCount, IExecutionDataAccessorGenerator accessorGenerator) {
      this.className = className;
      this.classId = classId;
      this.probeCount = probeCount;
      this.accessorGenerator = accessorGenerator;
   }

   public int storeInstance(MethodVisitor mv, boolean clinit, int variable) {
      if (clinit) {
         int maxStack = this.accessorGenerator.generateDataAccessor(this.classId, this.className, this.probeCount, mv);
         mv.visitInsn(89);
         mv.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
         mv.visitVarInsn(58, variable);
         this.seenClinit = true;
         return Math.max(maxStack, 2);
      } else {
         mv.visitMethodInsn(184, this.className, "$jacocoInit", "()[Z", true);
         mv.visitVarInsn(58, variable);
         return 1;
      }
   }

   public void addMembers(ClassVisitor cv, int probeCount) {
      this.createDataField(cv);
      this.createInitMethod(cv, probeCount);
      if (!this.seenClinit) {
         this.createClinitMethod(cv, probeCount);
      }

   }

   private void createDataField(ClassVisitor cv) {
      cv.visitField(4121, "$jacocoData", "[Z", (String)null, (Object)null);
   }

   private void createInitMethod(ClassVisitor cv, int probeCount) {
      MethodVisitor mv = cv.visitMethod(4106, "$jacocoInit", "()[Z", (String)null, (String[])null);
      mv.visitCode();
      mv.visitFieldInsn(178, this.className, "$jacocoData", "[Z");
      mv.visitInsn(89);
      Label alreadyInitialized = new Label();
      mv.visitJumpInsn(199, alreadyInitialized);
      mv.visitInsn(87);
      int size = this.accessorGenerator.generateDataAccessor(this.classId, this.className, probeCount, mv);
      mv.visitFrame(-1, 0, FRAME_LOCALS_EMPTY, 1, FRAME_STACK_ARRZ);
      mv.visitLabel(alreadyInitialized);
      mv.visitInsn(176);
      mv.visitMaxs(Math.max(size, 2), 0);
      mv.visitEnd();
   }

   private void createClinitMethod(ClassVisitor cv, int probeCount) {
      MethodVisitor mv = cv.visitMethod(4104, "<clinit>", "()V", (String)null, (String[])null);
      mv.visitCode();
      int maxStack = this.accessorGenerator.generateDataAccessor(this.classId, this.className, probeCount, mv);
      mv.visitFieldInsn(179, this.className, "$jacocoData", "[Z");
      mv.visitInsn(177);
      mv.visitMaxs(maxStack, 0);
      mv.visitEnd();
   }
}
