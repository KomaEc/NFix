package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.MethodVisitor;

public class MethodCallerMultiAdapter {
   private MethodCaller[] methods;
   boolean skipSpreadSafeAndSafe;
   public static final int MAX_ARGS = 0;

   public static MethodCallerMultiAdapter newStatic(Class theClass, String baseName, boolean createNArgs, boolean skipSpreadSafeAndSafe) {
      MethodCallerMultiAdapter mcma = new MethodCallerMultiAdapter();
      mcma.skipSpreadSafeAndSafe = skipSpreadSafeAndSafe;
      if (createNArgs) {
         int numberOfBaseMethods = mcma.numberOfBaseMethods();
         mcma.methods = new MethodCaller[2 * numberOfBaseMethods];

         for(int i = 0; i <= 0; ++i) {
            mcma.methods[i * numberOfBaseMethods] = MethodCaller.newStatic(theClass, baseName + i);
            if (!skipSpreadSafeAndSafe) {
               mcma.methods[i * numberOfBaseMethods + 1] = MethodCaller.newStatic(theClass, baseName + i + "Safe");
               mcma.methods[i * numberOfBaseMethods + 2] = MethodCaller.newStatic(theClass, baseName + i + "SpreadSafe");
            }
         }

         mcma.methods[1 * numberOfBaseMethods] = MethodCaller.newStatic(theClass, baseName + "N");
         if (!skipSpreadSafeAndSafe) {
            mcma.methods[1 * numberOfBaseMethods + 1] = MethodCaller.newStatic(theClass, baseName + "N" + "Safe");
            mcma.methods[1 * numberOfBaseMethods + 2] = MethodCaller.newStatic(theClass, baseName + "N" + "SpreadSafe");
         }
      } else if (!skipSpreadSafeAndSafe) {
         mcma.methods = new MethodCaller[]{MethodCaller.newStatic(theClass, baseName), MethodCaller.newStatic(theClass, baseName + "Safe"), MethodCaller.newStatic(theClass, baseName + "SpreadSafe")};
      } else {
         mcma.methods = new MethodCaller[]{MethodCaller.newStatic(theClass, baseName)};
      }

      return mcma;
   }

   public void call(MethodVisitor methodVisitor, int numberOfArguments, boolean safe, boolean spreadSafe) {
      int offset = 0;
      if (safe && !this.skipSpreadSafeAndSafe) {
         offset = 1;
      }

      if (spreadSafe && !this.skipSpreadSafeAndSafe) {
         offset = 2;
      }

      int offset;
      if (numberOfArguments <= 0 && numberOfArguments >= 0) {
         offset = offset + numberOfArguments * this.numberOfBaseMethods();
      } else {
         offset = offset + 1 * this.numberOfBaseMethods();
      }

      this.methods[offset].call(methodVisitor);
   }

   private int numberOfBaseMethods() {
      return this.skipSpreadSafeAndSafe ? 1 : 3;
   }
}
