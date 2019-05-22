package com.gzoltar.instrumentation.transformer;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.utils.WildcardMatcher;
import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.CtClass;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {
   public static final String[] IGNORED = new String[]{"javax.", "java.", "sun.", "com.sun.", "jdk.internal.", "apple.", "com.apple.", "junit.", "org.junit.", "org.hamcrest.", "org.testng.", "major.mutation.", "org.evosuite.runtime.instrumentation.InstrumentedClass", "org.evosuite.runtime.EvoRunner", "com.gzoltar."};
   private final String[] targetPackages;
   private final String[] targetClasses;
   private final String[] testPackages;
   private final String[] testClasses;
   private final boolean isInstrumentTestClasses;
   private final boolean isInstrumentDeprecatedClasses;
   private final boolean isInstrumentDeprecatedMethods;
   private final boolean uniqueLineNumbers;

   public ClassTransformer(boolean var1, boolean var2, boolean var3, String[] var4, String[] var5, String[] var6, String[] var7, boolean var8) {
      this.targetPackages = var4;
      this.targetClasses = var5;
      this.testPackages = var6;
      this.testClasses = var7;
      this.isInstrumentTestClasses = var1;
      this.isInstrumentDeprecatedClasses = var2;
      this.isInstrumentDeprecatedMethods = var3;
      this.uniqueLineNumbers = var8;
   }

   public byte[] transform(ClassLoader var1, String var2, Class<?> var3, ProtectionDomain var4, byte[] var5) throws IllegalClassFormatException {
      if (var3 != null) {
         return null;
      } else if (matchesIgnored(var2 = var2.replace('/', '.'))) {
         return null;
      } else {
         if (this.isInstrumentTestClasses) {
            if (!isItAllowed(this.testPackages, this.testClasses, var2)) {
               return null;
            }
         } else if (!isItAllowed(this.targetPackages, this.targetClasses, var2)) {
            return null;
         }

         byte[] var7;
         try {
            ClassPool var8;
            CtClass var9 = (var8 = ClassPool.getDefault()).makeClass((InputStream)(new ByteArrayInputStream(var5)));
            if (!this.isInstrumentDeprecatedClasses && var9.hasAnnotation(Deprecated.class)) {
               Logger.getInstance().debug("Excluding deprecated class " + var2);
               return null;
            }

            Logger.getInstance().debug("Transforming " + var2 + " with " + var1.getClass().getName());
            Logger.getInstance().debug("  PACKAGE: " + (var9.getPackageName() == null ? "<null>" : var9.getPackageName()));
            Logger.getInstance().debug("    FILE: " + var9.getClassFile().getSourceFile());
            Logger.getInstance().debug("      CLASS: " + var9.getName());
            (new InstrumentationPass()).transform(var8, var9, this.isInstrumentDeprecatedMethods, this.uniqueLineNumbers);
            var7 = var9.toBytecode();
            var9.defrost();
         } catch (Exception var6) {
            Logger.getInstance().err("Trying to instrument class " + var2, var6);
            return null;
         }

         Logger.getInstance().debug("Transformation of " + var2 + " went well");
         return var7;
      }
   }

   boolean hasSourceLocation(ProtectionDomain var1) {
      if (var1 == null) {
         return false;
      } else {
         CodeSource var2;
         if ((var2 = var1.getCodeSource()) == null) {
            return false;
         } else {
            return var2.getLocation() != null;
         }
      }
   }

   public static boolean matchesIgnored(String var0) {
      String[] var1;
      int var2 = (var1 = IGNORED).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (var0.startsWith(var4)) {
            return true;
         }
      }

      return false;
   }

   public static boolean isItAllowed(String[] var0, String[] var1, String var2) {
      String var3 = var2.contains(".") ? var2.substring(0, var2.lastIndexOf(".")) : null;
      int var4;
      String var5;
      int var6;
      if (var0 != null && var3 != null) {
         var6 = (var0 = var0).length;

         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var0[var4];
            if ((new WildcardMatcher(var5)).matches(var3)) {
               return true;
            }
         }
      } else if (var1 != null) {
         var0 = var1;
         var6 = var1.length;

         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var0[var4];
            if ((new WildcardMatcher(var5)).matches(var2)) {
               return true;
            }

            if ((new WildcardMatcher(var5 + "$*")).matches(var2)) {
               return true;
            }
         }
      }

      return false;
   }
}
