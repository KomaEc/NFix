package bsh;

public class BSHPackageDeclaration extends SimpleNode {
   public BSHPackageDeclaration(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      BSHAmbiguousName var3 = (BSHAmbiguousName)this.jjtGetChild(0);
      NameSpace var4 = var1.top();
      var4.setPackage(var3.text);
      var4.importPackage(var3.text);
      return Primitive.VOID;
   }
}
