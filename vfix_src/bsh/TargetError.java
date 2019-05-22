package bsh;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

public class TargetError extends EvalError {
   Throwable target;
   boolean inNativeCode;

   public TargetError(String var1, Throwable var2, SimpleNode var3, CallStack var4, boolean var5) {
      super(var1, var3, var4);
      this.target = var2;
      this.inNativeCode = var5;
   }

   public TargetError(Throwable var1, SimpleNode var2, CallStack var3) {
      this("TargetError", var1, var2, var3, false);
   }

   public Throwable getTarget() {
      return this.target instanceof InvocationTargetException ? ((InvocationTargetException)this.target).getTargetException() : this.target;
   }

   public String toString() {
      return super.toString() + "\nTarget exception: " + this.printTargetError(this.target);
   }

   public void printStackTrace() {
      this.printStackTrace(false, System.err);
   }

   public void printStackTrace(PrintStream var1) {
      this.printStackTrace(false, var1);
   }

   public void printStackTrace(boolean var1, PrintStream var2) {
      if (var1) {
         super.printStackTrace(var2);
         var2.println("--- Target Stack Trace ---");
      }

      this.target.printStackTrace(var2);
   }

   public String printTargetError(Throwable var1) {
      String var2 = this.target.toString();
      if (Capabilities.canGenerateInterfaces()) {
         var2 = var2 + "\n" + this.xPrintTargetError(var1);
      }

      return var2;
   }

   public String xPrintTargetError(Throwable var1) {
      String var2 = "import java.lang.reflect.UndeclaredThrowableException;String result=\"\";while ( target instanceof UndeclaredThrowableException ) {\ttarget=target.getUndeclaredThrowable(); \tresult+=\"Nested: \"+target.toString();}return result;";
      Interpreter var3 = new Interpreter();

      try {
         var3.set("target", var1);
         return (String)var3.eval(var2);
      } catch (EvalError var5) {
         throw new InterpreterError("xprintarget: " + var5.toString());
      }
   }

   public boolean inNativeCode() {
      return this.inNativeCode;
   }
}
