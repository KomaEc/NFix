package bsh;

public class UtilEvalError extends Exception {
   protected UtilEvalError() {
   }

   public UtilEvalError(String var1) {
      super(var1);
   }

   public EvalError toEvalError(String var1, SimpleNode var2, CallStack var3) {
      if (Interpreter.DEBUG) {
         this.printStackTrace();
      }

      if (var1 == null) {
         var1 = "";
      } else {
         var1 = var1 + ": ";
      }

      return new EvalError(var1 + this.getMessage(), var2, var3);
   }

   public EvalError toEvalError(SimpleNode var1, CallStack var2) {
      return this.toEvalError((String)null, var1, var2);
   }
}
