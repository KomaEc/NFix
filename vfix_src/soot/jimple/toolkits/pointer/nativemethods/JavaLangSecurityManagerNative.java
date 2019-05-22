package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaLangSecurityManagerNative extends NativeMethodClass {
   public JavaLangSecurityManagerNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.lang.Class[] getClassContext()")) {
         this.java_lang_SecurityManager_getClassContext(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.ClassLoader currentClassLoader0()")) {
         this.java_lang_SecurityManager_currentClassLoader0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class currentLoadedClass0()")) {
         this.java_lang_SecurityManager_currentLoadedClass0(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_lang_SecurityManager_getClassContext(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getLeastArrayObject());
   }

   public void java_lang_SecurityManager_currentClassLoader0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassLoaderObject());
   }

   public void java_lang_SecurityManager_currentLoadedClass0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }
}
