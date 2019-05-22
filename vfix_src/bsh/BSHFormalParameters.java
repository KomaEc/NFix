package bsh;

class BSHFormalParameters extends SimpleNode {
   private String[] paramNames;
   Class[] paramTypes;
   int numArgs;
   String[] typeDescriptors;

   BSHFormalParameters(int var1) {
      super(var1);
   }

   void insureParsed() {
      if (this.paramNames == null) {
         this.numArgs = this.jjtGetNumChildren();
         String[] var1 = new String[this.numArgs];

         for(int var2 = 0; var2 < this.numArgs; ++var2) {
            BSHFormalParameter var3 = (BSHFormalParameter)this.jjtGetChild(var2);
            var1[var2] = var3.name;
         }

         this.paramNames = var1;
      }
   }

   public String[] getParamNames() {
      this.insureParsed();
      return this.paramNames;
   }

   public String[] getTypeDescriptors(CallStack var1, Interpreter var2, String var3) {
      if (this.typeDescriptors != null) {
         return this.typeDescriptors;
      } else {
         this.insureParsed();
         String[] var4 = new String[this.numArgs];

         for(int var5 = 0; var5 < this.numArgs; ++var5) {
            BSHFormalParameter var6 = (BSHFormalParameter)this.jjtGetChild(var5);
            var4[var5] = var6.getTypeDescriptor(var1, var2, var3);
         }

         this.typeDescriptors = var4;
         return var4;
      }
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      if (this.paramTypes != null) {
         return this.paramTypes;
      } else {
         this.insureParsed();
         Class[] var3 = new Class[this.numArgs];

         for(int var4 = 0; var4 < this.numArgs; ++var4) {
            BSHFormalParameter var5 = (BSHFormalParameter)this.jjtGetChild(var4);
            var3[var4] = (Class)var5.eval(var1, var2);
         }

         this.paramTypes = var3;
         return var3;
      }
   }
}
