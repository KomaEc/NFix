package bsh;

class BSHCastExpression extends SimpleNode {
   public BSHCastExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      NameSpace var3 = var1.top();
      Class var4 = ((BSHType)this.jjtGetChild(0)).getType(var1, var2);
      SimpleNode var5 = (SimpleNode)this.jjtGetChild(1);
      Object var6 = var5.eval(var1, var2);
      Class var7 = var6.getClass();

      try {
         return Types.castObject(var6, var4, 0);
      } catch (UtilEvalError var9) {
         throw var9.toEvalError(this, var1);
      }
   }
}
