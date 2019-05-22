package bsh;

class BSHMethodDeclaration extends SimpleNode {
   public String name;
   BSHReturnType returnTypeNode;
   BSHFormalParameters paramsNode;
   BSHBlock blockNode;
   int firstThrowsClause;
   public Modifiers modifiers;
   Class returnType;
   int numThrows = 0;

   BSHMethodDeclaration(int var1) {
      super(var1);
   }

   synchronized void insureNodesParsed() {
      if (this.paramsNode == null) {
         Node var1 = this.jjtGetChild(0);
         this.firstThrowsClause = 1;
         if (var1 instanceof BSHReturnType) {
            this.returnTypeNode = (BSHReturnType)var1;
            this.paramsNode = (BSHFormalParameters)this.jjtGetChild(1);
            if (this.jjtGetNumChildren() > 2 + this.numThrows) {
               this.blockNode = (BSHBlock)this.jjtGetChild(2 + this.numThrows);
            }

            ++this.firstThrowsClause;
         } else {
            this.paramsNode = (BSHFormalParameters)this.jjtGetChild(0);
            this.blockNode = (BSHBlock)this.jjtGetChild(1 + this.numThrows);
         }

      }
   }

   Class evalReturnType(CallStack var1, Interpreter var2) throws EvalError {
      this.insureNodesParsed();
      return this.returnTypeNode != null ? this.returnTypeNode.evalReturnType(var1, var2) : null;
   }

   String getReturnTypeDescriptor(CallStack var1, Interpreter var2, String var3) {
      this.insureNodesParsed();
      return this.returnTypeNode == null ? null : this.returnTypeNode.getTypeDescriptor(var1, var2, var3);
   }

   BSHReturnType getReturnTypeNode() {
      this.insureNodesParsed();
      return this.returnTypeNode;
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      this.returnType = this.evalReturnType(var1, var2);
      this.evalNodes(var1, var2);
      NameSpace var3 = var1.top();
      BshMethod var4 = new BshMethod(this, var3, this.modifiers);

      try {
         var3.setMethod(this.name, var4);
      } catch (UtilEvalError var6) {
         throw var6.toEvalError(this, var1);
      }

      return Primitive.VOID;
   }

   private void evalNodes(CallStack var1, Interpreter var2) throws EvalError {
      this.insureNodesParsed();

      for(int var3 = this.firstThrowsClause; var3 < this.numThrows + this.firstThrowsClause; ++var3) {
         ((BSHAmbiguousName)this.jjtGetChild(var3)).toClass(var1, var2);
      }

      this.paramsNode.eval(var1, var2);
      if (var2.getStrictJava()) {
         for(int var4 = 0; var4 < this.paramsNode.paramTypes.length; ++var4) {
            if (this.paramsNode.paramTypes[var4] == null) {
               throw new EvalError("(Strict Java Mode) Undeclared argument type, parameter: " + this.paramsNode.getParamNames()[var4] + " in method: " + this.name, this, (CallStack)null);
            }
         }

         if (this.returnType == null) {
            throw new EvalError("(Strict Java Mode) Undeclared return type for method: " + this.name, this, (CallStack)null);
         }
      }

   }

   public String toString() {
      return "MethodDeclaration: " + this.name;
   }
}
