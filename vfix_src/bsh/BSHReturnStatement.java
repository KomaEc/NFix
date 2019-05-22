package bsh;

class BSHReturnStatement extends SimpleNode implements ParserConstants {
   public int kind;

   BSHReturnStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      Object var3;
      if (this.jjtGetNumChildren() > 0) {
         var3 = ((SimpleNode)this.jjtGetChild(0)).eval(var1, var2);
      } else {
         var3 = Primitive.VOID;
      }

      return new ReturnControl(this.kind, var3, this);
   }
}
