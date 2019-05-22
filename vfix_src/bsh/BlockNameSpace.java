package bsh;

class BlockNameSpace extends NameSpace {
   public BlockNameSpace(NameSpace var1) throws EvalError {
      super(var1, var1.getName() + "/BlockNameSpace");
   }

   public void setVariable(String var1, Object var2, boolean var3, boolean var4) throws UtilEvalError {
      if (this.weHaveVar(var1)) {
         super.setVariable(var1, var2, var3, false);
      } else {
         this.getParent().setVariable(var1, var2, var3, var4);
      }

   }

   public void setBlockVariable(String var1, Object var2) throws UtilEvalError {
      super.setVariable(var1, var2, false, false);
   }

   private boolean weHaveVar(String var1) {
      try {
         return super.getVariableImpl(var1, false) != null;
      } catch (UtilEvalError var3) {
         return false;
      }
   }

   private NameSpace getNonBlockParent() {
      NameSpace var1 = super.getParent();
      return var1 instanceof BlockNameSpace ? ((BlockNameSpace)var1).getNonBlockParent() : var1;
   }

   This getThis(Interpreter var1) {
      return this.getNonBlockParent().getThis(var1);
   }

   public This getSuper(Interpreter var1) {
      return this.getNonBlockParent().getSuper(var1);
   }

   public void importClass(String var1) {
      this.getParent().importClass(var1);
   }

   public void importPackage(String var1) {
      this.getParent().importPackage(var1);
   }

   public void setMethod(String var1, BshMethod var2) throws UtilEvalError {
      this.getParent().setMethod(var1, var2);
   }
}
