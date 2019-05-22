package soot.javaToJimple;

import soot.SootClass;

public class InnerClassInfo {
   public static final int NESTED = 0;
   public static final int STATIC = 1;
   public static final int LOCAL = 2;
   public static final int ANON = 3;
   private SootClass outerClass;
   private String simpleName;
   private int innerType;

   public SootClass getOuterClass() {
      return this.outerClass;
   }

   public String getSimpleName() {
      return this.simpleName;
   }

   public int getInnerType() {
      return this.innerType;
   }

   public InnerClassInfo(SootClass outerClass, String simpleName, int innerType) {
      this.outerClass = outerClass;
      this.simpleName = simpleName;
      this.innerType = innerType;
   }
}
