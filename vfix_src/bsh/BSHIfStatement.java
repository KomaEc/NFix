package bsh;

class BSHIfStatement extends SimpleNode {
   BSHIfStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      Object var3 = null;
      if (evaluateCondition((SimpleNode)this.jjtGetChild(0), var1, var2)) {
         var3 = ((SimpleNode)this.jjtGetChild(1)).eval(var1, var2);
      } else if (this.jjtGetNumChildren() > 2) {
         var3 = ((SimpleNode)this.jjtGetChild(2)).eval(var1, var2);
      }

      return var3 instanceof ReturnControl ? var3 : Primitive.VOID;
   }

   public static boolean evaluateCondition(SimpleNode var0, CallStack var1, Interpreter var2) throws EvalError {
      Object var3 = var0.eval(var1, var2);
      if (var3 instanceof Primitive) {
         if (var3 == Primitive.VOID) {
            throw new EvalError("Condition evaluates to void type", var0, var1);
         }

         var3 = ((Primitive)var3).getValue();
      }

      if (var3 instanceof Boolean) {
         return (Boolean)var3;
      } else {
         throw new EvalError("Condition must evaluate to a Boolean or boolean.", var0, var1);
      }
   }
}
