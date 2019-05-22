package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaUtilResourceBundleNative extends NativeMethodClass {
   public JavaUtilResourceBundleNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.lang.Class[] getClassContext()")) {
         this.java_util_ResourceBundle_getClassContext(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_util_ResourceBundle_getClassContext(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }
}
