package bsh;

import java.util.Vector;

class BSHTryStatement extends SimpleNode {
   BSHTryStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      BSHBlock var3 = (BSHBlock)this.jjtGetChild(0);
      Vector var4 = new Vector();
      Vector var5 = new Vector();
      int var6 = this.jjtGetNumChildren();
      Node var7 = null;

      int var8;
      for(var8 = 1; var8 < var6 && (var7 = this.jjtGetChild(var8++)) instanceof BSHFormalParameter; var7 = null) {
         var4.addElement(var7);
         var5.addElement(this.jjtGetChild(var8++));
      }

      BSHBlock var9 = null;
      if (var7 != null) {
         var9 = (BSHBlock)var7;
      }

      TargetError var10 = null;
      Throwable var11 = null;
      Object var12 = null;
      int var13 = var1.depth();

      try {
         var12 = var3.eval(var1, var2);
      } catch (TargetError var29) {
         var10 = var29;

         for(String var15 = "Bsh Stack: "; var1.depth() > var13; var15 = var15 + "\t" + var1.pop() + "\n") {
         }
      }

      if (var10 != null) {
         var11 = var10.getTarget();
      }

      if (var11 != null) {
         int var14 = var4.size();
         var8 = 0;

         while(var8 < var14) {
            BSHFormalParameter var30 = (BSHFormalParameter)var4.elementAt(var8);
            var30.eval(var1, var2);
            if (var30.type == null && var2.getStrictJava()) {
               throw new EvalError("(Strict Java) Untyped catch block", this, var1);
            }

            if (var30.type != null) {
               try {
                  var11 = (Throwable)Types.castObject(var11, var30.type, 1);
               } catch (UtilEvalError var28) {
                  ++var8;
                  continue;
               }
            }

            BSHBlock var16 = (BSHBlock)var5.elementAt(var8);
            NameSpace var17 = var1.top();
            BlockNameSpace var18 = new BlockNameSpace(var17);

            try {
               if (var30.type == BSHFormalParameter.UNTYPED) {
                  var18.setBlockVariable(var30.name, var11);
               } else {
                  new Modifiers();
                  var18.setTypedVariable(var30.name, var30.type, var11, new Modifiers());
               }
            } catch (UtilEvalError var27) {
               throw new InterpreterError("Unable to set var in catch block namespace.");
            }

            var1.swap(var18);

            try {
               var12 = var16.eval(var1, var2);
            } finally {
               var1.swap(var17);
            }

            var10 = null;
            break;
         }
      }

      if (var9 != null) {
         var12 = var9.eval(var1, var2);
      }

      if (var10 != null) {
         throw var10;
      } else {
         return var12 instanceof ReturnControl ? var12 : Primitive.VOID;
      }
   }
}
