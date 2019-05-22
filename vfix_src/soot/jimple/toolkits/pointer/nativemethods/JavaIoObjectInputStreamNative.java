package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaIoObjectInputStreamNative extends NativeMethodClass {
   public JavaIoObjectInputStreamNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.lang.ClassLoader latestUserDefinedLoader()")) {
         this.java_io_ObjectInputStream_latestUserDefinedLoader(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Object allocateNewObject(java.lang.Class,java.lang.Class)")) {
         this.java_io_ObjectInputStream_allocateNewObject(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Object allocateNewArray(java.lang.Class,int)")) {
         this.java_io_ObjectInputStream_allocateNewArray(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_io_ObjectInputStream_latestUserDefinedLoader(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassLoaderObject());
   }

   public void java_io_ObjectInputStream_allocateNewObject(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }

   public void java_io_ObjectInputStream_allocateNewArray(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      throw new NativeMethodNotSupportedException(method);
   }
}
