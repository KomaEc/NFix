package bsh;

public class UtilTargetError extends UtilEvalError {
   public Throwable t;

   public UtilTargetError(String var1, Throwable var2) {
      super(var1);
      this.t = var2;
   }

   public UtilTargetError(Throwable var1) {
      this((String)null, var1);
   }

   public EvalError toEvalError(String var1, SimpleNode var2, CallStack var3) {
      if (var1 == null) {
         var1 = this.getMessage();
      } else {
         var1 = var1 + ": " + this.getMessage();
      }

      return new TargetError(var1, this.t, var2, var3, false);
   }
}
