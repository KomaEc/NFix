package bsh;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BshMethod implements Serializable {
   NameSpace declaringNameSpace;
   Modifiers modifiers;
   private String name;
   private Class creturnType;
   private String[] paramNames;
   private int numArgs;
   private Class[] cparamTypes;
   BSHBlock methodBody;
   private Method javaMethod;
   private Object javaObject;

   BshMethod(BSHMethodDeclaration var1, NameSpace var2, Modifiers var3) {
      this(var1.name, var1.returnType, var1.paramsNode.getParamNames(), var1.paramsNode.paramTypes, var1.blockNode, var2, var3);
   }

   BshMethod(String var1, Class var2, String[] var3, Class[] var4, BSHBlock var5, NameSpace var6, Modifiers var7) {
      this.name = var1;
      this.creturnType = var2;
      this.paramNames = var3;
      if (var3 != null) {
         this.numArgs = var3.length;
      }

      this.cparamTypes = var4;
      this.methodBody = var5;
      this.declaringNameSpace = var6;
      this.modifiers = var7;
   }

   BshMethod(Method var1, Object var2) {
      this(var1.getName(), var1.getReturnType(), (String[])null, var1.getParameterTypes(), (BSHBlock)null, (NameSpace)null, (Modifiers)null);
      this.javaMethod = var1;
      this.javaObject = var2;
   }

   public Class[] getParameterTypes() {
      return this.cparamTypes;
   }

   public String[] getParameterNames() {
      return this.paramNames;
   }

   public Class getReturnType() {
      return this.creturnType;
   }

   public Modifiers getModifiers() {
      return this.modifiers;
   }

   public String getName() {
      return this.name;
   }

   public Object invoke(Object[] var1, Interpreter var2) throws EvalError {
      return this.invoke(var1, var2, (CallStack)null, (SimpleNode)null, false);
   }

   public Object invoke(Object[] var1, Interpreter var2, CallStack var3, SimpleNode var4) throws EvalError {
      return this.invoke(var1, var2, var3, var4, false);
   }

   Object invoke(Object[] var1, Interpreter var2, CallStack var3, SimpleNode var4, boolean var5) throws EvalError {
      if (var1 != null) {
         for(int var6 = 0; var6 < var1.length; ++var6) {
            if (var1[var6] == null) {
               throw new Error("HERE!");
            }
         }
      }

      if (this.javaMethod != null) {
         try {
            return Reflect.invokeMethod(this.javaMethod, this.javaObject, var1);
         } catch (ReflectError var10) {
            throw new EvalError("Error invoking Java method: " + var10, var4, var3);
         } catch (InvocationTargetException var11) {
            throw new TargetError("Exception invoking imported object method.", var11, var4, var3, true);
         }
      } else if (this.modifiers != null && this.modifiers.hasModifier("synchronized")) {
         Object var7;
         if (this.declaringNameSpace.isClass) {
            try {
               var7 = this.declaringNameSpace.getClassInstance();
            } catch (UtilEvalError var13) {
               throw new InterpreterError("Can't get class instance for synchronized method.");
            }
         } else {
            var7 = this.declaringNameSpace.getThis(var2);
         }

         synchronized(var7) {
            return this.invokeImpl(var1, var2, var3, var4, var5);
         }
      } else {
         return this.invokeImpl(var1, var2, var3, var4, var5);
      }
   }

   private Object invokeImpl(Object[] var1, Interpreter var2, CallStack var3, SimpleNode var4, boolean var5) throws EvalError {
      Class var6 = this.getReturnType();
      Class[] var7 = this.getParameterTypes();
      if (var3 == null) {
         var3 = new CallStack(this.declaringNameSpace);
      }

      if (var1 == null) {
         var1 = new Object[0];
      }

      if (var1.length != this.numArgs) {
         throw new EvalError("Wrong number of arguments for local method: " + this.name, var4, var3);
      } else {
         NameSpace var8;
         if (var5) {
            var8 = var3.top();
         } else {
            var8 = new NameSpace(this.declaringNameSpace, this.name);
            var8.isMethod = true;
         }

         var8.setNode(var4);

         for(int var9 = 0; var9 < this.numArgs; ++var9) {
            if (var7[var9] != null) {
               try {
                  var1[var9] = Types.castObject(var1[var9], var7[var9], 1);
               } catch (UtilEvalError var17) {
                  throw new EvalError("Invalid argument: `" + this.paramNames[var9] + "'" + " for method: " + this.name + " : " + var17.getMessage(), var4, var3);
               }

               try {
                  var8.setTypedVariable(this.paramNames[var9], var7[var9], var1[var9], (Modifiers)null);
               } catch (UtilEvalError var16) {
                  throw var16.toEvalError("Typed method parameter assignment", var4, var3);
               }
            } else {
               if (var1[var9] == Primitive.VOID) {
                  throw new EvalError("Undefined variable or class name, parameter: " + this.paramNames[var9] + " to method: " + this.name, var4, var3);
               }

               try {
                  var8.setLocalVariable(this.paramNames[var9], var1[var9], var2.getStrictJava());
               } catch (UtilEvalError var15) {
                  throw var15.toEvalError(var4, var3);
               }
            }
         }

         if (!var5) {
            var3.push(var8);
         }

         Object var10 = this.methodBody.eval(var3, var2, true);
         CallStack var11 = var3.copy();
         if (!var5) {
            var3.pop();
         }

         ReturnControl var12 = null;
         if (var10 instanceof ReturnControl) {
            var12 = (ReturnControl)var10;
            if (var12.kind != 46) {
               throw new EvalError("'continue' or 'break' in method body", var12.returnPoint, var11);
            }

            var10 = ((ReturnControl)var10).value;
            if (var6 == Void.TYPE && var10 != Primitive.VOID) {
               throw new EvalError("Cannot return value from void method", var12.returnPoint, var11);
            }
         }

         if (var6 != null) {
            if (var6 == Void.TYPE) {
               return Primitive.VOID;
            }

            try {
               var10 = Types.castObject(var10, var6, 1);
            } catch (UtilEvalError var18) {
               SimpleNode var14 = var4;
               if (var12 != null) {
                  var14 = var12.returnPoint;
               }

               throw var18.toEvalError("Incorrect type returned from method: " + this.name + var18.getMessage(), var14, var3);
            }
         }

         return var10;
      }
   }

   public boolean hasModifier(String var1) {
      return this.modifiers != null && this.modifiers.hasModifier(var1);
   }

   public String toString() {
      return "Scripted Method: " + StringUtil.methodString(this.name, this.getParameterTypes());
   }
}
