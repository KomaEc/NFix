package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

class NoneProbeArrayStrategy implements IProbeArrayStrategy {
   public int storeInstance(MethodVisitor mv, boolean clinit, int variable) {
      throw new UnsupportedOperationException();
   }

   public void addMembers(ClassVisitor delegate, int probeCount) {
   }
}
