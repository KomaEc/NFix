package bsh;

class BSHFormalParameter extends SimpleNode {
   public static final Class UNTYPED = null;
   public String name;
   public Class type;

   BSHFormalParameter(int var1) {
      super(var1);
   }

   public String getTypeDescriptor(CallStack var1, Interpreter var2, String var3) {
      return this.jjtGetNumChildren() > 0 ? ((BSHType)this.jjtGetChild(0)).getTypeDescriptor(var1, var2, var3) : "Ljava/lang/Object;";
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      if (this.jjtGetNumChildren() > 0) {
         this.type = ((BSHType)this.jjtGetChild(0)).getType(var1, var2);
      } else {
         this.type = UNTYPED;
      }

      return this.type;
   }
}
