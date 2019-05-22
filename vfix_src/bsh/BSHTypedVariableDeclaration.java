package bsh;

class BSHTypedVariableDeclaration extends SimpleNode {
   public Modifiers modifiers;

   BSHTypedVariableDeclaration(int var1) {
      super(var1);
   }

   private BSHType getTypeNode() {
      return (BSHType)this.jjtGetChild(0);
   }

   Class evalType(CallStack var1, Interpreter var2) throws EvalError {
      BSHType var3 = this.getTypeNode();
      return var3.getType(var1, var2);
   }

   BSHVariableDeclarator[] getDeclarators() {
      int var1 = this.jjtGetNumChildren();
      byte var2 = 1;
      BSHVariableDeclarator[] var3 = new BSHVariableDeclarator[var1 - var2];

      for(int var4 = var2; var4 < var1; ++var4) {
         var3[var4 - var2] = (BSHVariableDeclarator)this.jjtGetChild(var4);
      }

      return var3;
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      try {
         NameSpace var3 = var1.top();
         BSHType var4 = this.getTypeNode();
         Class var5 = var4.getType(var1, var2);
         BSHVariableDeclarator[] var6 = this.getDeclarators();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            BSHVariableDeclarator var8 = var6[var7];
            Object var9 = var8.eval(var4, var1, var2);

            try {
               var3.setTypedVariable(var8.name, var5, var9, this.modifiers);
            } catch (UtilEvalError var11) {
               throw var11.toEvalError(this, var1);
            }
         }
      } catch (EvalError var12) {
         var12.reThrow("Typed variable declaration");
      }

      return Primitive.VOID;
   }

   public String getTypeDescriptor(CallStack var1, Interpreter var2, String var3) {
      return this.getTypeNode().getTypeDescriptor(var1, var2, var3);
   }
}
