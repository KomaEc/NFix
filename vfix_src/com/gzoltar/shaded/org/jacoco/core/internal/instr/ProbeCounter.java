package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesVisitor;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.MethodProbesVisitor;

class ProbeCounter extends ClassProbesVisitor {
   private int count = 0;
   private boolean methods = false;

   public MethodProbesVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      if (!"<clinit>".equals(name) && (access & 1024) == 0) {
         this.methods = true;
      }

      return null;
   }

   public void visitTotalProbeCount(int count) {
      this.count = count;
   }

   int getCount() {
      return this.count;
   }

   boolean hasMethods() {
      return this.methods;
   }
}
