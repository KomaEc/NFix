package bsh;

class BSHVariableDeclarator extends SimpleNode {
   public String name;

   BSHVariableDeclarator(int var1) {
      super(var1);
   }

   public Object eval(BSHType var1, CallStack var2, Interpreter var3) throws EvalError {
      Object var4 = null;
      if (this.jjtGetNumChildren() > 0) {
         SimpleNode var5 = (SimpleNode)this.jjtGetChild(0);
         if (var1 != null && var5 instanceof BSHArrayInitializer) {
            var4 = ((BSHArrayInitializer)var5).eval(var1.getBaseType(), var1.getArrayDims(), var2, var3);
         } else {
            var4 = var5.eval(var2, var3);
         }
      }

      if (var4 == Primitive.VOID) {
         throw new EvalError("Void initializer.", this, var2);
      } else {
         return var4;
      }
   }

   public String toString() {
      return "BSHVariableDeclarator " + this.name;
   }
}
