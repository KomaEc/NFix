package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaIoFileSystemNative extends NativeMethodClass {
   public JavaIoFileSystemNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("java.io.FileSystem getFileSystem()")) {
         this.java_io_FileSystem_getFileSystem(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_io_FileSystem_getFileSystem(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getFileSystemObject());
   }
}
