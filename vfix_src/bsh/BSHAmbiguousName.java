package bsh;

class BSHAmbiguousName extends SimpleNode {
   public String text;

   BSHAmbiguousName(int var1) {
      super(var1);
   }

   public Name getName(NameSpace var1) {
      return var1.getNameResolver(this.text);
   }

   public Object toObject(CallStack var1, Interpreter var2) throws EvalError {
      return this.toObject(var1, var2, false);
   }

   Object toObject(CallStack var1, Interpreter var2, boolean var3) throws EvalError {
      try {
         return this.getName(var1.top()).toObject(var1, var2, var3);
      } catch (UtilEvalError var5) {
         throw var5.toEvalError(this, var1);
      }
   }

   public Class toClass(CallStack var1, Interpreter var2) throws EvalError {
      try {
         return this.getName(var1.top()).toClass();
      } catch (ClassNotFoundException var5) {
         throw new EvalError(var5.getMessage(), this, var1);
      } catch (UtilEvalError var6) {
         throw var6.toEvalError(this, var1);
      }
   }

   public LHS toLHS(CallStack var1, Interpreter var2) throws EvalError {
      try {
         return this.getName(var1.top()).toLHS(var1, var2);
      } catch (UtilEvalError var4) {
         throw var4.toEvalError(this, var1);
      }
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      throw new InterpreterError("Don't know how to eval an ambiguous name!  Use toObject() if you want an object.");
   }

   public String toString() {
      return "AmbigousName: " + this.text;
   }
}
