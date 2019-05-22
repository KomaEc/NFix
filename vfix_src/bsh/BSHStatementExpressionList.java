package bsh;

class BSHStatementExpressionList extends SimpleNode {
   BSHStatementExpressionList(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      int var3 = this.jjtGetNumChildren();

      for(int var4 = 0; var4 < var3; ++var4) {
         SimpleNode var5 = (SimpleNode)this.jjtGetChild(var4);
         var5.eval(var1, var2);
      }

      return Primitive.VOID;
   }
}
