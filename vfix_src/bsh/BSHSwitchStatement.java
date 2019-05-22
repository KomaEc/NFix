package bsh;

class BSHSwitchStatement extends SimpleNode implements ParserConstants {
   public BSHSwitchStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      int var3 = this.jjtGetNumChildren();
      byte var4 = 0;
      int var11 = var4 + 1;
      SimpleNode var5 = (SimpleNode)this.jjtGetChild(var4);
      Object var6 = var5.eval(var1, var2);
      ReturnControl var7 = null;
      if (var11 >= var3) {
         throw new EvalError("Empty switch statement.", this, var1);
      } else {
         BSHSwitchLabel var8 = (BSHSwitchLabel)this.jjtGetChild(var11++);

         while(var11 < var3 && var7 == null) {
            Node var9;
            if (!var8.isDefault && !this.primitiveEquals(var6, var8.eval(var1, var2), var1, var5)) {
               while(var11 < var3) {
                  var9 = this.jjtGetChild(var11++);
                  if (var9 instanceof BSHSwitchLabel) {
                     var8 = (BSHSwitchLabel)var9;
                     break;
                  }
               }
            } else {
               while(var11 < var3) {
                  var9 = this.jjtGetChild(var11++);
                  if (!(var9 instanceof BSHSwitchLabel)) {
                     Object var10 = ((SimpleNode)var9).eval(var1, var2);
                     if (var10 instanceof ReturnControl) {
                        var7 = (ReturnControl)var10;
                        break;
                     }
                  }
               }
            }
         }

         return var7 != null && var7.kind == 46 ? var7 : Primitive.VOID;
      }
   }

   private boolean primitiveEquals(Object var1, Object var2, CallStack var3, SimpleNode var4) throws EvalError {
      if (!(var1 instanceof Primitive) && !(var2 instanceof Primitive)) {
         return var1.equals(var2);
      } else {
         try {
            Object var5 = Primitive.binaryOperation(var1, var2, 90);
            var5 = Primitive.unwrap(var5);
            return var5.equals(Boolean.TRUE);
         } catch (UtilEvalError var6) {
            throw var6.toEvalError("Switch value: " + var4.getText() + ": ", this, var3);
         }
      }
   }
}
