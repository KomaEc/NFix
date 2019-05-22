package bsh;

class BSHTernaryExpression extends SimpleNode {
   BSHTernaryExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      SimpleNode var3 = (SimpleNode)this.jjtGetChild(0);
      SimpleNode var4 = (SimpleNode)this.jjtGetChild(1);
      SimpleNode var5 = (SimpleNode)this.jjtGetChild(2);
      return BSHIfStatement.evaluateCondition(var3, var1, var2) ? var4.eval(var1, var2) : var5.eval(var1, var2);
   }
}
