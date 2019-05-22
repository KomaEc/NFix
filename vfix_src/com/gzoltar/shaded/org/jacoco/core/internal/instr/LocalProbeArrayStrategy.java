package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.runtime.IExecutionDataAccessorGenerator;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class LocalProbeArrayStrategy implements IProbeArrayStrategy {
   private final String className;
   private final long classId;
   private final int probeCount;
   private final IExecutionDataAccessorGenerator accessorGenerator;

   LocalProbeArrayStrategy(String className, long classId, int probeCount, IExecutionDataAccessorGenerator accessorGenerator) {
      this.className = className;
      this.classId = classId;
      this.probeCount = probeCount;
      this.accessorGenerator = accessorGenerator;
   }

   public int storeInstance(MethodVisitor mv, boolean clinit, int variable) {
      int maxStack = this.accessorGenerator.generateDataAccessor(this.classId, this.className, this.probeCount, mv);
      mv.visitVarInsn(58, variable);
      return maxStack;
   }

   public void addMembers(ClassVisitor delegate, int probeCount) {
   }
}
