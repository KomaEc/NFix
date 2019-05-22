package soot.jimple.toolkits.pointer.nativemethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public abstract class NativeMethodClass {
   private static final Logger logger = LoggerFactory.getLogger(NativeMethodClass.class);
   private static final boolean DEBUG = false;
   protected NativeHelper helper;

   public NativeMethodClass(NativeHelper helper) {
      this.helper = helper;
   }

   public static void defaultMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
   }

   public abstract void simulateMethod(SootMethod var1, ReferenceVariable var2, ReferenceVariable var3, ReferenceVariable[] var4);
}
