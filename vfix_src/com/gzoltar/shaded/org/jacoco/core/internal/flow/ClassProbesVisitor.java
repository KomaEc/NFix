package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;

public abstract class ClassProbesVisitor extends ClassVisitor {
   public ClassProbesVisitor() {
      this((ClassVisitor)null);
   }

   public ClassProbesVisitor(ClassVisitor cv) {
      super(327680, cv);
   }

   public abstract MethodProbesVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5);

   public abstract void visitTotalProbeCount(int var1);
}
