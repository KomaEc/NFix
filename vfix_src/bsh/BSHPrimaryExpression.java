package bsh;

class BSHPrimaryExpression extends SimpleNode {
   BSHPrimaryExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      return this.eval(false, var1, var2);
   }

   public LHS toLHS(CallStack var1, Interpreter var2) throws EvalError {
      Object var3 = this.eval(true, var1, var2);
      if (!(var3 instanceof LHS)) {
         throw new EvalError("Can't assign to:", this, var1);
      } else {
         return (LHS)var3;
      }
   }

   private Object eval(boolean var1, CallStack var2, Interpreter var3) throws EvalError {
      Object var4 = this.jjtGetChild(0);
      int var5 = this.jjtGetNumChildren();

      for(int var6 = 1; var6 < var5; ++var6) {
         var4 = ((BSHPrimarySuffix)this.jjtGetChild(var6)).doSuffix(var4, var1, var2, var3);
      }

      if (var4 instanceof SimpleNode) {
         if (var4 instanceof BSHAmbiguousName) {
            if (var1) {
               var4 = ((BSHAmbiguousName)var4).toLHS(var2, var3);
            } else {
               var4 = ((BSHAmbiguousName)var4).toObject(var2, var3);
            }
         } else {
            if (var1) {
               throw new EvalError("Can't assign to prefix.", this, var2);
            }

            var4 = ((SimpleNode)var4).eval(var2, var3);
         }
      }

      if (var4 instanceof LHS) {
         if (var1) {
            return var4;
         } else {
            try {
               return ((LHS)var4).getValue();
            } catch (UtilEvalError var8) {
               throw var8.toEvalError(this, var2);
            }
         }
      } else {
         return var4;
      }
   }
}
