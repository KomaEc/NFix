package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.jacoco.core.JaCoCo;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.InstrSupport;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public class OfflineInstrumentationAccessGenerator implements IExecutionDataAccessorGenerator {
   private final String runtimeClassName;

   public OfflineInstrumentationAccessGenerator() {
      this(JaCoCo.RUNTIMEPACKAGE.replace('.', '/') + "/Offline");
   }

   OfflineInstrumentationAccessGenerator(String runtimeClassName) {
      this.runtimeClassName = runtimeClassName;
   }

   public int generateDataAccessor(long classid, String classname, int probecount, MethodVisitor mv) {
      mv.visitLdcInsn(classid);
      mv.visitLdcInsn(classname);
      InstrSupport.push(mv, probecount);
      mv.visitMethodInsn(184, this.runtimeClassName, "getProbes", "(JLjava/lang/String;I)[Z", false);
      return 4;
   }
}
