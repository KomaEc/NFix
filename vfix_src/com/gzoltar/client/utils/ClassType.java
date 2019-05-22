package com.gzoltar.client.utils;

import com.gzoltar.instrumentation.testing.TestClassUtils;
import java.io.Serializable;

public class ClassType {
   private String name;
   private ClassType.Type type;

   public ClassType(String var1, ClassType.Type var2) {
      this.name = var1;
      this.type = var2;
   }

   public String getName() {
      return this.name;
   }

   public ClassType.Type getType() {
      return this.type;
   }

   public static ClassType.Type whatType(Class<?> var0) {
      if (TestClassUtils.isJUnitTest(var0)) {
         return ClassType.Type.JUNIT;
      } else {
         return TestClassUtils.isTestNGTest(var0) ? ClassType.Type.TESTNG : ClassType.Type.CLASS;
      }
   }

   public static enum Type implements Serializable {
      JUNIT,
      TESTNG,
      CLASS;
   }
}
