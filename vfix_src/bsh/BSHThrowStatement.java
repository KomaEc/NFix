package bsh;

class BSHThrowStatement extends SimpleNode {
   BSHThrowStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      Object var3 = ((SimpleNode)this.jjtGetChild(0)).eval(var1, var2);
      if (!(var3 instanceof Exception)) {
         throw new EvalError("Expression in 'throw' must be Exception type", this, var1);
      } else {
         throw new TargetError((Exception)var3, this, var1);
      }
   }
}
