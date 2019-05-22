package bsh;

class BSHSwitchLabel extends SimpleNode {
   boolean isDefault;

   public BSHSwitchLabel(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      if (this.isDefault) {
         return null;
      } else {
         SimpleNode var3 = (SimpleNode)this.jjtGetChild(0);
         return var3.eval(var1, var2);
      }
   }
}
