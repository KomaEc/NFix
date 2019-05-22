package bsh;

class BSHWhileStatement extends SimpleNode implements ParserConstants {
   public boolean isDoStatement;

   BSHWhileStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      int var3 = this.jjtGetNumChildren();
      SimpleNode var4 = null;
      SimpleNode var5;
      if (this.isDoStatement) {
         var5 = (SimpleNode)this.jjtGetChild(1);
         var4 = (SimpleNode)this.jjtGetChild(0);
      } else {
         var5 = (SimpleNode)this.jjtGetChild(0);
         if (var3 > 1) {
            var4 = (SimpleNode)this.jjtGetChild(1);
         }
      }

      boolean var6 = this.isDoStatement;

      while(var6 || BSHIfStatement.evaluateCondition(var5, var1, var2)) {
         if (var4 != null) {
            Object var7 = var4.eval(var1, var2);
            boolean var8 = false;
            if (var7 instanceof ReturnControl) {
               switch(((ReturnControl)var7).kind) {
               case 12:
                  var8 = true;
                  break;
               case 19:
                  continue;
               case 46:
                  return var7;
               }
            }

            if (var8) {
               break;
            }

            var6 = false;
         }
      }

      return Primitive.VOID;
   }
}
