package bsh;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

class BSHAllocationExpression extends SimpleNode {
   private static int innerClassCount = 0;

   BSHAllocationExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      SimpleNode var3 = (SimpleNode)this.jjtGetChild(0);
      SimpleNode var4 = (SimpleNode)this.jjtGetChild(1);
      if (var3 instanceof BSHAmbiguousName) {
         BSHAmbiguousName var5 = (BSHAmbiguousName)var3;
         return var4 instanceof BSHArguments ? this.objectAllocation(var5, (BSHArguments)var4, var1, var2) : this.objectArrayAllocation(var5, (BSHArrayDimensions)var4, var1, var2);
      } else {
         return this.primitiveArrayAllocation((BSHPrimitiveType)var3, (BSHArrayDimensions)var4, var1, var2);
      }
   }

   private Object objectAllocation(BSHAmbiguousName var1, BSHArguments var2, CallStack var3, Interpreter var4) throws EvalError {
      NameSpace var5 = var3.top();
      Object[] var6 = var2.getArguments(var3, var4);
      if (var6 == null) {
         throw new EvalError("Null args in new.", this, var3);
      } else {
         var1.toObject(var3, var4, false);
         Object var7 = var1.toObject(var3, var4, true);
         Class var8 = null;
         if (var7 instanceof ClassIdentifier) {
            var8 = ((ClassIdentifier)var7).getTargetClass();
            boolean var9 = this.jjtGetNumChildren() > 2;
            if (var9) {
               BSHBlock var10 = (BSHBlock)this.jjtGetChild(2);
               return var8.isInterface() ? this.constructWithInterfaceBody(var8, var6, var10, var3, var4) : this.constructWithClassBody(var8, var6, var10, var3, var4);
            } else {
               return this.constructObject(var8, var6, var3);
            }
         } else {
            throw new EvalError("Unknown class: " + var1.text, this, var3);
         }
      }
   }

   private Object constructObject(Class var1, Object[] var2, CallStack var3) throws EvalError {
      Object var4;
      try {
         var4 = Reflect.constructObject(var1, var2);
      } catch (ReflectError var10) {
         throw new EvalError("Constructor error: " + var10.getMessage(), this, var3);
      } catch (InvocationTargetException var11) {
         Interpreter.debug("The constructor threw an exception:\n\t" + var11.getTargetException());
         throw new TargetError("Object constructor", var11.getTargetException(), this, var3, true);
      }

      String var5 = var1.getName();
      if (var5.indexOf("$") == -1) {
         return var4;
      } else {
         This var6 = var3.top().getThis((Interpreter)null);
         NameSpace var7 = Name.getClassNameSpace(var6.getNameSpace());
         if (var7 != null && var5.startsWith(var7.getName() + "$")) {
            try {
               ClassGenerator.getClassGenerator().setInstanceNameSpaceParent(var4, var5, var7);
            } catch (UtilEvalError var9) {
               throw var9.toEvalError(this, var3);
            }
         }

         return var4;
      }
   }

   private Object constructWithClassBody(Class var1, Object[] var2, BSHBlock var3, CallStack var4, Interpreter var5) throws EvalError {
      String var6 = var4.top().getName() + "$" + ++innerClassCount;
      Modifiers var7 = new Modifiers();
      var7.addModifier(0, "public");

      Class var8;
      try {
         var8 = ClassGenerator.getClassGenerator().generateClass(var6, var7, (Class[])null, var1, var3, false, var4, var5);
      } catch (UtilEvalError var10) {
         throw var10.toEvalError(this, var4);
      }

      try {
         return Reflect.constructObject(var8, var2);
      } catch (Exception var11) {
         Exception var9 = var11;
         if (var11 instanceof InvocationTargetException) {
            var9 = (Exception)((InvocationTargetException)var11).getTargetException();
         }

         throw new EvalError("Error constructing inner class instance: " + var9, this, var4);
      }
   }

   private Object constructWithInterfaceBody(Class var1, Object[] var2, BSHBlock var3, CallStack var4, Interpreter var5) throws EvalError {
      NameSpace var6 = var4.top();
      NameSpace var7 = new NameSpace(var6, "AnonymousBlock");
      var4.push(var7);
      var3.eval(var4, var5, true);
      var4.pop();
      var7.importStatic(var1);

      try {
         return var7.getThis(var5).getInterface(var1);
      } catch (UtilEvalError var9) {
         throw var9.toEvalError(this, var4);
      }
   }

   private Object objectArrayAllocation(BSHAmbiguousName var1, BSHArrayDimensions var2, CallStack var3, Interpreter var4) throws EvalError {
      NameSpace var5 = var3.top();
      Class var6 = var1.toClass(var3, var4);
      if (var6 == null) {
         throw new EvalError("Class " + var1.getName(var5) + " not found.", this, var3);
      } else {
         return this.arrayAllocation(var2, var6, var3, var4);
      }
   }

   private Object primitiveArrayAllocation(BSHPrimitiveType var1, BSHArrayDimensions var2, CallStack var3, Interpreter var4) throws EvalError {
      Class var5 = var1.getType();
      return this.arrayAllocation(var2, var5, var3, var4);
   }

   private Object arrayAllocation(BSHArrayDimensions var1, Class var2, CallStack var3, Interpreter var4) throws EvalError {
      Object var5 = var1.eval(var2, var3, var4);
      return var5 != Primitive.VOID ? var5 : this.arrayNewInstance(var2, var1, var3);
   }

   private Object arrayNewInstance(Class var1, BSHArrayDimensions var2, CallStack var3) throws EvalError {
      if (var2.numUndefinedDims > 0) {
         Object var4 = Array.newInstance(var1, new int[var2.numUndefinedDims]);
         var1 = var4.getClass();
      }

      try {
         return Array.newInstance(var1, var2.definedDimensions);
      } catch (NegativeArraySizeException var6) {
         throw new TargetError(var6, this, var3);
      } catch (Exception var7) {
         throw new EvalError("Can't construct primitive array: " + var7.getMessage(), this, var3);
      }
   }
}
