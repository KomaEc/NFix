package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;

public class NativeMethodNotSupportedException extends RuntimeException {
   private String msg;

   public NativeMethodNotSupportedException(SootMethod method) {
      String message = "The following native method is not supported: \n  " + method.getSignature();
      this.msg = message;
   }

   public NativeMethodNotSupportedException(String message) {
      this.msg = message;
   }

   public NativeMethodNotSupportedException() {
   }

   public String toString() {
      return this.msg;
   }
}
