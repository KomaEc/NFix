package bsh;

import java.lang.reflect.Array;

class BSHArrayDimensions extends SimpleNode {
   public Class baseType;
   public int numDefinedDims;
   public int numUndefinedDims;
   public int[] definedDimensions;

   BSHArrayDimensions(int var1) {
      super(var1);
   }

   public void addDefinedDimension() {
      ++this.numDefinedDims;
   }

   public void addUndefinedDimension() {
      ++this.numUndefinedDims;
   }

   public Object eval(Class var1, CallStack var2, Interpreter var3) throws EvalError {
      if (Interpreter.DEBUG) {
         Interpreter.debug("array base type = " + var1);
      }

      this.baseType = var1;
      return this.eval(var2, var3);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      SimpleNode var3 = (SimpleNode)this.jjtGetChild(0);
      if (var3 instanceof BSHArrayInitializer) {
         if (this.baseType == null) {
            throw new EvalError("Internal Array Eval err:  unknown base type", this, var1);
         } else {
            Object var10 = ((BSHArrayInitializer)var3).eval(this.baseType, this.numUndefinedDims, var1, var2);
            Class var11 = var10.getClass();
            int var6 = Reflect.getArrayDimensions(var11);
            this.definedDimensions = new int[var6];
            if (this.definedDimensions.length != this.numUndefinedDims) {
               throw new EvalError("Incompatible initializer. Allocation calls for a " + this.numUndefinedDims + " dimensional array, but initializer is a " + var6 + " dimensional array", this, var1);
            } else {
               Object var7 = var10;

               for(int var8 = 0; var8 < this.definedDimensions.length; ++var8) {
                  this.definedDimensions[var8] = Array.getLength(var7);
                  if (this.definedDimensions[var8] > 0) {
                     var7 = Array.get(var7, 0);
                  }
               }

               return var10;
            }
         }
      } else {
         this.definedDimensions = new int[this.numDefinedDims];

         for(int var4 = 0; var4 < this.numDefinedDims; ++var4) {
            try {
               Object var5 = ((SimpleNode)this.jjtGetChild(var4)).eval(var1, var2);
               this.definedDimensions[var4] = ((Primitive)var5).intValue();
            } catch (Exception var9) {
               throw new EvalError("Array index: " + var4 + " does not evaluate to an integer", this, var1);
            }
         }

         return Primitive.VOID;
      }
   }
}
