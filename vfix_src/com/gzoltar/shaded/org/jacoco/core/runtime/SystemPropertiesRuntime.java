package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public class SystemPropertiesRuntime extends AbstractRuntime {
   private static final String KEYPREFIX = "jacoco-";
   private final String key = "jacoco-" + Integer.toHexString(this.hashCode());

   public int generateDataAccessor(long classid, String classname, int probecount, MethodVisitor mv) {
      mv.visitMethodInsn(184, "java/lang/System", "getProperties", "()Ljava/util/Properties;", false);
      mv.visitLdcInsn(this.key);
      mv.visitMethodInsn(182, "java/util/Properties", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
      RuntimeData.generateAccessCall(classid, classname, probecount, mv);
      return 6;
   }

   public void startup(RuntimeData data) throws Exception {
      super.startup(data);
      System.getProperties().put(this.key, data);
   }

   public void shutdown() {
      System.getProperties().remove(this.key);
   }
}
