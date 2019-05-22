package bsh;

class BSHClassDeclaration extends SimpleNode {
   static final String CLASSINITNAME = "_bshClassInit";
   String name;
   Modifiers modifiers;
   int numInterfaces;
   boolean extend;
   boolean isInterface;

   BSHClassDeclaration(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      int var3 = 0;
      Class var4 = null;
      if (this.extend) {
         BSHAmbiguousName var5 = (BSHAmbiguousName)this.jjtGetChild(var3++);
         var4 = var5.toClass(var1, var2);
      }

      Class[] var10 = new Class[this.numInterfaces];

      for(int var6 = 0; var6 < this.numInterfaces; ++var6) {
         BSHAmbiguousName var7 = (BSHAmbiguousName)this.jjtGetChild(var3++);
         var10[var6] = var7.toClass(var1, var2);
         if (!var10[var6].isInterface()) {
            throw new EvalError("Type: " + var7.text + " is not an interface!", this, var1);
         }
      }

      BSHBlock var11;
      if (var3 < this.jjtGetNumChildren()) {
         var11 = (BSHBlock)this.jjtGetChild(var3);
      } else {
         var11 = new BSHBlock(25);
      }

      try {
         return ClassGenerator.getClassGenerator().generateClass(this.name, this.modifiers, var10, var4, var11, this.isInterface, var1, var2);
      } catch (UtilEvalError var9) {
         throw var9.toEvalError(this, var1);
      }
   }

   public String toString() {
      return "ClassDeclaration: " + this.name;
   }
}
