package soot.jbco.util;

import soot.Scene;
import soot.SootClass;

public class ThrowSet {
   private static SootClass[] throwable = null;

   public static SootClass getRandomThrowable() {
      if (throwable == null) {
         initThrowables();
      }

      return throwable[Rand.getInt(throwable.length)];
   }

   private static void initThrowables() {
      Scene sc = Scene.v();
      throwable = new SootClass[10];
      throwable[0] = sc.getRefType("java.lang.RuntimeException").getSootClass();
      throwable[1] = sc.getRefType("java.lang.ArithmeticException").getSootClass();
      throwable[2] = sc.getRefType("java.lang.ArrayStoreException").getSootClass();
      throwable[3] = sc.getRefType("java.lang.ClassCastException").getSootClass();
      throwable[4] = sc.getRefType("java.lang.IllegalMonitorStateException").getSootClass();
      throwable[5] = sc.getRefType("java.lang.IndexOutOfBoundsException").getSootClass();
      throwable[6] = sc.getRefType("java.lang.ArrayIndexOutOfBoundsException").getSootClass();
      throwable[7] = sc.getRefType("java.lang.NegativeArraySizeException").getSootClass();
      throwable[8] = sc.getRefType("java.lang.NullPointerException").getSootClass();
      throwable[9] = sc.getRefType("java.lang.Throwable").getSootClass();
   }
}
