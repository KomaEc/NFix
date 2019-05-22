package bsh;

class BSHImportDeclaration extends SimpleNode {
   public boolean importPackage;
   public boolean staticImport;
   public boolean superImport;

   BSHImportDeclaration(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      NameSpace var3 = var1.top();
      if (this.superImport) {
         try {
            var3.doSuperImport();
         } catch (UtilEvalError var5) {
            throw var5.toEvalError(this, var1);
         }
      } else if (this.staticImport) {
         if (!this.importPackage) {
            throw new EvalError("static field imports not supported yet", this, var1);
         }

         Class var4 = ((BSHAmbiguousName)this.jjtGetChild(0)).toClass(var1, var2);
         var3.importStatic(var4);
      } else {
         String var6 = ((BSHAmbiguousName)this.jjtGetChild(0)).text;
         if (this.importPackage) {
            var3.importPackage(var6);
         } else {
            var3.importClass(var6);
         }
      }

      return Primitive.VOID;
   }
}
