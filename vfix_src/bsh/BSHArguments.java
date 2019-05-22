package bsh;

class BSHArguments extends SimpleNode {
   BSHArguments(int var1) {
      super(var1);
   }

   public Object[] getArguments(CallStack var1, Interpreter var2) throws EvalError {
      Object[] var3 = new Object[this.jjtGetNumChildren()];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = ((SimpleNode)this.jjtGetChild(var4)).eval(var1, var2);
         if (var3[var4] == Primitive.VOID) {
            throw new EvalError("Undefined argument: " + ((SimpleNode)this.jjtGetChild(var4)).getText(), this, var1);
         }
      }

      return var3;
   }
}
