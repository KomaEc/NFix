package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaLangReflectFieldNative extends NativeMethodClass {
   public JavaLangReflectFieldNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("void set(java.lang.Object,java.lang.Object)")) {
         this.java_lang_reflect_Field_set(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Object get(java.lang.Object)")) {
         this.java_lang_reflect_Field_get(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_lang_reflect_Field_set(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }

   public void java_lang_reflect_Field_get(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }
}
