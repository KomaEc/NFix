package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public interface IProbeArrayStrategy {
   int storeInstance(MethodVisitor var1, boolean var2, int var3);

   void addMembers(ClassVisitor var1, int var2);
}
