package bsh;

public class DelayedEvalBshMethod extends BshMethod {
   String returnTypeDescriptor;
   BSHReturnType returnTypeNode;
   String[] paramTypeDescriptors;
   BSHFormalParameters paramTypesNode;
   transient CallStack callstack;
   transient Interpreter interpreter;

   DelayedEvalBshMethod(String var1, String var2, BSHReturnType var3, String[] var4, String[] var5, BSHFormalParameters var6, BSHBlock var7, NameSpace var8, Modifiers var9, CallStack var10, Interpreter var11) {
      super(var1, (Class)null, var4, (Class[])null, var7, var8, var9);
      this.returnTypeDescriptor = var2;
      this.returnTypeNode = var3;
      this.paramTypeDescriptors = var5;
      this.paramTypesNode = var6;
      this.callstack = var10;
      this.interpreter = var11;
   }

   public String getReturnTypeDescriptor() {
      return this.returnTypeDescriptor;
   }

   public Class getReturnType() {
      if (this.returnTypeNode == null) {
         return null;
      } else {
         try {
            return this.returnTypeNode.evalReturnType(this.callstack, this.interpreter);
         } catch (EvalError var2) {
            throw new InterpreterError("can't eval return type: " + var2);
         }
      }
   }

   public String[] getParamTypeDescriptors() {
      return this.paramTypeDescriptors;
   }

   public Class[] getParameterTypes() {
      try {
         return (Class[])this.paramTypesNode.eval(this.callstack, this.interpreter);
      } catch (EvalError var2) {
         throw new InterpreterError("can't eval param types: " + var2);
      }
   }
}
