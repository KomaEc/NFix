package bsh;

class BSHReturnType extends SimpleNode {
   public boolean isVoid;

   BSHReturnType(int var1) {
      super(var1);
   }

   BSHType getTypeNode() {
      return (BSHType)this.jjtGetChild(0);
   }

   public String getTypeDescriptor(CallStack var1, Interpreter var2, String var3) {
      return this.isVoid ? "V" : this.getTypeNode().getTypeDescriptor(var1, var2, var3);
   }

   public Class evalReturnType(CallStack var1, Interpreter var2) throws EvalError {
      return this.isVoid ? Void.TYPE : this.getTypeNode().getType(var1, var2);
   }
}
