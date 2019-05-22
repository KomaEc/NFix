package bsh;

import java.lang.reflect.InvocationTargetException;

class BSHMethodInvocation extends SimpleNode {
   BSHMethodInvocation(int var1) {
      super(var1);
   }

   BSHAmbiguousName getNameNode() {
      return (BSHAmbiguousName)this.jjtGetChild(0);
   }

   BSHArguments getArgsNode() {
      return (BSHArguments)this.jjtGetChild(1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      NameSpace var3 = var1.top();
      BSHAmbiguousName var4 = this.getNameNode();
      if (var3.getParent() == null || !var3.getParent().isClass || !var4.text.equals("super") && !var4.text.equals("this")) {
         Name var5 = var4.getName(var3);
         Object[] var6 = this.getArgsNode().getArguments(var1, var2);

         try {
            return var5.invokeMethod(var2, var6, var1, this);
         } catch (ReflectError var12) {
            throw new EvalError("Error in method invocation: " + var12.getMessage(), this, var1);
         } catch (InvocationTargetException var13) {
            String var9 = "Method Invocation " + var5;
            Throwable var10 = var13.getTargetException();
            boolean var11 = true;
            if (var10 instanceof EvalError) {
               if (var10 instanceof TargetError) {
                  var11 = ((TargetError)var10).inNativeCode();
               } else {
                  var11 = false;
               }
            }

            throw new TargetError(var9, var10, this, var1, var11);
         } catch (UtilEvalError var14) {
            throw var14.toEvalError(this, var1);
         }
      } else {
         return Primitive.VOID;
      }
   }
}
