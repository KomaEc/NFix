package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public final class InstrSupport {
   public static final int ASM_API_VERSION = 327680;
   public static final String DATAFIELD_NAME = "$jacocoData";
   public static final int DATAFIELD_ACC = 4234;
   public static final int DATAFIELD_INTF_ACC = 4121;
   public static final String DATAFIELD_DESC = "[Z";
   public static final String INITMETHOD_NAME = "$jacocoInit";
   public static final String INITMETHOD_DESC = "()[Z";
   public static final int INITMETHOD_ACC = 4106;
   static final String CLINIT_NAME = "<clinit>";
   static final String CLINIT_DESC = "()V";
   static final int CLINIT_ACC = 4104;

   private InstrSupport() {
   }

   public static void assertNotInstrumented(String member, String owner) throws IllegalStateException {
      if (member.equals("$jacocoData") || member.equals("$jacocoInit")) {
         throw new IllegalStateException(String.format("Class %s is already instrumented.", owner));
      }
   }

   public static void push(MethodVisitor mv, int value) {
      if (value >= -1 && value <= 5) {
         mv.visitInsn(3 + value);
      } else if (value >= -128 && value <= 127) {
         mv.visitIntInsn(16, value);
      } else if (value >= -32768 && value <= 32767) {
         mv.visitIntInsn(17, value);
      } else {
         mv.visitLdcInsn(value);
      }

   }
}
