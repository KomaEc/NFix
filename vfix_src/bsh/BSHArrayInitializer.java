package bsh;

import java.lang.reflect.Array;

class BSHArrayInitializer extends SimpleNode {
   BSHArrayInitializer(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      throw new EvalError("Array initializer has no base type.", this, var1);
   }

   public Object eval(Class var1, int var2, CallStack var3, Interpreter var4) throws EvalError {
      int var5 = this.jjtGetNumChildren();
      int[] var6 = new int[var2];
      var6[0] = var5;
      Object var7 = Array.newInstance(var1, var6);

      for(int var8 = 0; var8 < var5; ++var8) {
         SimpleNode var9 = (SimpleNode)this.jjtGetChild(var8);
         Object var10;
         if (var9 instanceof BSHArrayInitializer) {
            if (var2 < 2) {
               throw new EvalError("Invalid Location for Intializer, position: " + var8, this, var3);
            }

            var10 = ((BSHArrayInitializer)var9).eval(var1, var2 - 1, var3, var4);
         } else {
            var10 = var9.eval(var3, var4);
         }

         if (var10 == Primitive.VOID) {
            throw new EvalError("Void in array initializer, position" + var8, this, var3);
         }

         Object var11 = var10;
         if (var2 == 1) {
            try {
               var11 = Types.castObject(var10, var1, 0);
            } catch (UtilEvalError var14) {
               throw var14.toEvalError("Error in array initializer", this, var3);
            }

            var11 = Primitive.unwrap(var11);
         }

         try {
            Array.set(var7, var8, var11);
         } catch (IllegalArgumentException var15) {
            Interpreter.debug("illegal arg" + var15);
            this.throwTypeError(var1, var10, var8, var3);
         } catch (ArrayStoreException var16) {
            Interpreter.debug("arraystore" + var16);
            this.throwTypeError(var1, var10, var8, var3);
         }
      }

      return var7;
   }

   private void throwTypeError(Class var1, Object var2, int var3, CallStack var4) throws EvalError {
      String var5;
      if (var2 instanceof Primitive) {
         var5 = ((Primitive)var2).getType().getName();
      } else {
         var5 = Reflect.normalizeClassName(var2.getClass());
      }

      throw new EvalError("Incompatible type: " + var5 + " in initializer of array type: " + var1 + " at position: " + var3, this, var4);
   }
}
