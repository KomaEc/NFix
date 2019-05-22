package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaIoObjectOutputStreamNative extends NativeMethodClass {
   public JavaIoObjectOutputStreamNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.lang.Object getObjectFieldValue(java.lang.Object,long)")) {
         this.java_io_ObjectOutputStream_getObjectFieldValue(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_io_ObjectOutputStream_getObjectFieldValue(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }
}
