package bsh;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

class Name implements Serializable {
   public NameSpace namespace;
   String value = null;
   private String evalName;
   private String lastEvalName;
   private static String FINISHED = null;
   private Object evalBaseObject;
   private int callstackDepth;
   Class asClass;
   Class classOfStaticMethod;

   private void reset() {
      this.evalName = this.value;
      this.evalBaseObject = null;
      this.callstackDepth = 0;
   }

   Name(NameSpace var1, String var2) {
      this.namespace = var1;
      this.value = var2;
   }

   public Object toObject(CallStack var1, Interpreter var2) throws UtilEvalError {
      return this.toObject(var1, var2, false);
   }

   public synchronized Object toObject(CallStack var1, Interpreter var2, boolean var3) throws UtilEvalError {
      this.reset();

      Object var4;
      for(var4 = null; this.evalName != null; var4 = this.consumeNextObjectField(var1, var2, var3, false)) {
      }

      if (var4 == null) {
         throw new InterpreterError("null value in toObject()");
      } else {
         return var4;
      }
   }

   private Object completeRound(String var1, String var2, Object var3) {
      if (var3 == null) {
         throw new InterpreterError("lastEvalName = " + var1);
      } else {
         this.lastEvalName = var1;
         this.evalName = var2;
         this.evalBaseObject = var3;
         return var3;
      }
   }

   private Object consumeNextObjectField(CallStack var1, Interpreter var2, boolean var3, boolean var4) throws UtilEvalError {
      if (this.evalBaseObject == null && !isCompound(this.evalName) && !var3) {
         Object var5 = this.resolveThisFieldReference(var1, this.namespace, var2, this.evalName, false);
         if (var5 != Primitive.VOID) {
            return this.completeRound(this.evalName, FINISHED, var5);
         }
      }

      String var13 = prefix(this.evalName, 1);
      if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !var3) {
         if (Interpreter.DEBUG) {
            Interpreter.debug("trying to resolve variable: " + var13);
         }

         Object var6;
         if (this.evalBaseObject == null) {
            var6 = this.resolveThisFieldReference(var1, this.namespace, var2, var13, false);
         } else {
            var6 = this.resolveThisFieldReference(var1, ((This)this.evalBaseObject).namespace, var2, var13, true);
         }

         if (var6 != Primitive.VOID) {
            if (Interpreter.DEBUG) {
               Interpreter.debug("resolved variable: " + var13 + " in namespace: " + this.namespace);
            }

            return this.completeRound(var13, suffix(this.evalName), var6);
         }
      }

      Class var14;
      if (this.evalBaseObject == null) {
         if (Interpreter.DEBUG) {
            Interpreter.debug("trying class: " + this.evalName);
         }

         var14 = null;
         int var7 = 1;

         String var8;
         for(var8 = null; var7 <= countParts(this.evalName); ++var7) {
            var8 = prefix(this.evalName, var7);
            if ((var14 = this.namespace.getClass(var8)) != null) {
               break;
            }
         }

         if (var14 != null) {
            return this.completeRound(var8, suffix(this.evalName, countParts(this.evalName) - var7), new ClassIdentifier(var14));
         }

         if (Interpreter.DEBUG) {
            Interpreter.debug("not a class, trying var prefix " + this.evalName);
         }
      }

      if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !var3 && var4) {
         NameSpace var19 = this.evalBaseObject == null ? this.namespace : ((This)this.evalBaseObject).namespace;
         This var20 = (new NameSpace(var19, "auto: " + var13)).getThis(var2);
         var19.setVariable(var13, var20, false);
         return this.completeRound(var13, suffix(this.evalName), var20);
      } else if (this.evalBaseObject == null) {
         if (!isCompound(this.evalName)) {
            return this.completeRound(this.evalName, FINISHED, Primitive.VOID);
         } else {
            throw new UtilEvalError("Class or variable not found: " + this.evalName);
         }
      } else if (this.evalBaseObject == Primitive.NULL) {
         throw new UtilTargetError(new NullPointerException("Null Pointer while evaluating: " + this.value));
      } else if (this.evalBaseObject == Primitive.VOID) {
         throw new UtilEvalError("Undefined variable or class name while evaluating: " + this.value);
      } else if (this.evalBaseObject instanceof Primitive) {
         throw new UtilEvalError("Can't treat primitive like an object. Error while evaluating: " + this.value);
      } else if (!(this.evalBaseObject instanceof ClassIdentifier)) {
         if (var3) {
            throw new UtilEvalError(this.value + " does not resolve to a class name.");
         } else {
            String var17 = prefix(this.evalName, 1);
            if (var17.equals("length") && this.evalBaseObject.getClass().isArray()) {
               Primitive var18 = new Primitive(Array.getLength(this.evalBaseObject));
               return this.completeRound(var17, suffix(this.evalName), var18);
            } else {
               try {
                  Object var16 = Reflect.getObjectFieldValue(this.evalBaseObject, var17);
                  return this.completeRound(var17, suffix(this.evalName), var16);
               } catch (ReflectError var11) {
                  throw new UtilEvalError("Cannot access field: " + var17 + ", on object: " + this.evalBaseObject);
               }
            }
         }
      } else {
         var14 = ((ClassIdentifier)this.evalBaseObject).getTargetClass();
         String var15 = prefix(this.evalName, 1);
         if (var15.equals("this")) {
            for(NameSpace var22 = this.namespace; var22 != null; var22 = var22.getParent()) {
               if (var22.classInstance != null && var22.classInstance.getClass() == var14) {
                  return this.completeRound(var15, suffix(this.evalName), var22.classInstance);
               }
            }

            throw new UtilEvalError("Can't find enclosing 'this' instance of class: " + var14);
         } else {
            Object var21 = null;

            try {
               if (Interpreter.DEBUG) {
                  Interpreter.debug("Name call to getStaticFieldValue, class: " + var14 + ", field:" + var15);
               }

               var21 = Reflect.getStaticFieldValue(var14, var15);
            } catch (ReflectError var12) {
               if (Interpreter.DEBUG) {
                  Interpreter.debug("field reflect error: " + var12);
               }
            }

            if (var21 == null) {
               String var9 = var14.getName() + "$" + var15;
               Class var10 = this.namespace.getClass(var9);
               if (var10 != null) {
                  var21 = new ClassIdentifier(var10);
               }
            }

            if (var21 == null) {
               throw new UtilEvalError("No static field or inner class: " + var15 + " of " + var14);
            } else {
               return this.completeRound(var15, suffix(this.evalName), var21);
            }
         }
      }
   }

   Object resolveThisFieldReference(CallStack var1, NameSpace var2, Interpreter var3, String var4, boolean var5) throws UtilEvalError {
      This var9;
      if (var4.equals("this")) {
         if (var5) {
            throw new UtilEvalError("Redundant to call .this on This type");
         } else {
            var9 = var2.getThis(var3);
            var2 = var9.getNameSpace();
            Object var7 = var9;
            NameSpace var8 = getClassNameSpace(var2);
            if (var8 != null) {
               if (isCompound(this.evalName)) {
                  var7 = var8.getThis(var3);
               } else {
                  var7 = var8.getClassInstance();
               }
            }

            return var7;
         }
      } else if (var4.equals("super")) {
         var9 = var2.getSuper(var3);
         var2 = var9.getNameSpace();
         if (var2.getParent() != null && var2.getParent().isClass) {
            var9 = var2.getParent().getThis(var3);
         }

         return var9;
      } else {
         Object var6 = null;
         if (var4.equals("global")) {
            var6 = var2.getGlobal(var3);
         }

         if (var6 == null && var5) {
            if (var4.equals("namespace")) {
               var6 = var2;
            } else if (var4.equals("variables")) {
               var6 = var2.getVariableNames();
            } else if (var4.equals("methods")) {
               var6 = var2.getMethodNames();
            } else if (var4.equals("interpreter")) {
               if (!this.lastEvalName.equals("this")) {
                  throw new UtilEvalError("Can only call .interpreter on literal 'this'");
               }

               var6 = var3;
            }
         }

         if (var6 == null && var5 && var4.equals("caller")) {
            if (!this.lastEvalName.equals("this") && !this.lastEvalName.equals("caller")) {
               throw new UtilEvalError("Can only call .caller on literal 'this' or literal '.caller'");
            } else if (var1 == null) {
               throw new InterpreterError("no callstack");
            } else {
               var9 = var1.get(++this.callstackDepth).getThis(var3);
               return var9;
            }
         } else {
            if (var6 == null && var5 && var4.equals("callstack")) {
               if (!this.lastEvalName.equals("this")) {
                  throw new UtilEvalError("Can only call .callstack on literal 'this'");
               }

               if (var1 == null) {
                  throw new InterpreterError("no callstack");
               }

               var6 = var1;
            }

            if (var6 == null) {
               var6 = var2.getVariable(var4);
            }

            if (var6 == null) {
               throw new InterpreterError("null this field ref:" + var4);
            } else {
               return var6;
            }
         }
      }
   }

   static NameSpace getClassNameSpace(NameSpace var0) {
      if (var0.isClass) {
         return var0;
      } else {
         return var0.isMethod && var0.getParent() != null && var0.getParent().isClass ? var0.getParent() : null;
      }
   }

   public synchronized Class toClass() throws ClassNotFoundException, UtilEvalError {
      if (this.asClass != null) {
         return this.asClass;
      } else {
         this.reset();
         if (this.evalName.equals("var")) {
            return this.asClass = null;
         } else {
            Class var1 = this.namespace.getClass(this.evalName);
            if (var1 == null) {
               Object var2 = null;

               try {
                  var2 = this.toObject((CallStack)null, (Interpreter)null, true);
               } catch (UtilEvalError var4) {
               }

               if (var2 instanceof ClassIdentifier) {
                  var1 = ((ClassIdentifier)var2).getTargetClass();
               }
            }

            if (var1 == null) {
               throw new ClassNotFoundException("Class: " + this.value + " not found in namespace");
            } else {
               this.asClass = var1;
               return this.asClass;
            }
         }
      }
   }

   public synchronized LHS toLHS(CallStack var1, Interpreter var2) throws UtilEvalError {
      this.reset();
      LHS var3;
      if (!isCompound(this.evalName)) {
         if (this.evalName.equals("this")) {
            throw new UtilEvalError("Can't assign to 'this'.");
         } else {
            var3 = new LHS(this.namespace, this.evalName, false);
            return var3;
         }
      } else {
         Object var4 = null;

         try {
            while(this.evalName != null && isCompound(this.evalName)) {
               var4 = this.consumeNextObjectField(var1, var2, false, true);
            }
         } catch (UtilEvalError var7) {
            throw new UtilEvalError("LHS evaluation: " + var7.getMessage());
         }

         if (this.evalName == null && var4 instanceof ClassIdentifier) {
            throw new UtilEvalError("Can't assign to class: " + this.value);
         } else if (var4 == null) {
            throw new UtilEvalError("Error in LHS: " + this.value);
         } else if (var4 instanceof This) {
            if (!this.evalName.equals("namespace") && !this.evalName.equals("variables") && !this.evalName.equals("methods") && !this.evalName.equals("caller")) {
               Interpreter.debug("found This reference evaluating LHS");
               boolean var8 = !this.lastEvalName.equals("super");
               return new LHS(((This)var4).namespace, this.evalName, var8);
            } else {
               throw new UtilEvalError("Can't assign to special variable: " + this.evalName);
            }
         } else if (this.evalName != null) {
            try {
               if (var4 instanceof ClassIdentifier) {
                  Class var5 = ((ClassIdentifier)var4).getTargetClass();
                  var3 = Reflect.getLHSStaticField(var5, this.evalName);
                  return var3;
               } else {
                  var3 = Reflect.getLHSObjectField(var4, this.evalName);
                  return var3;
               }
            } catch (ReflectError var6) {
               throw new UtilEvalError("Field access: " + var6);
            }
         } else {
            throw new InterpreterError("Internal error in lhs...");
         }
      }
   }

   public Object invokeMethod(Interpreter var1, Object[] var2, CallStack var3, SimpleNode var4) throws UtilEvalError, EvalError, ReflectError, InvocationTargetException {
      String var5 = suffix(this.value, 1);
      BshClassManager var6 = var1.getClassManager();
      NameSpace var7 = var3.top();
      if (this.classOfStaticMethod != null) {
         return Reflect.invokeStaticMethod(var6, this.classOfStaticMethod, var5, var2);
      } else if (!isCompound(this.value)) {
         return this.invokeLocalMethod(var1, var2, var3, var4);
      } else {
         String var8 = prefix(this.value);
         if (var8.equals("super") && countParts(this.value) == 2) {
            This var9 = var7.getThis(var1);
            NameSpace var10 = var9.getNameSpace();
            NameSpace var11 = getClassNameSpace(var10);
            if (var11 != null) {
               Object var12 = var11.getClassInstance();
               return ClassGenerator.getClassGenerator().invokeSuperclassMethod(var6, var12, var5, var2);
            }
         }

         Name var15 = var7.getNameResolver(var8);
         Object var13 = var15.toObject(var3, var1);
         if (var13 == Primitive.VOID) {
            throw new UtilEvalError("Attempt to resolve method: " + var5 + "() on undefined variable or class name: " + var15);
         } else if (!(var13 instanceof ClassIdentifier)) {
            if (var13 instanceof Primitive) {
               if (var13 == Primitive.NULL) {
                  throw new UtilTargetError(new NullPointerException("Null Pointer in Method Invocation"));
               }

               if (Interpreter.DEBUG) {
                  Interpreter.debug("Attempt to access method on primitive... allowing bsh.Primitive to peek through for debugging");
               }
            }

            return Reflect.invokeObjectMethod(var13, var5, var2, var1, var3, var4);
         } else {
            if (Interpreter.DEBUG) {
               Interpreter.debug("invokeMethod: trying static - " + var15);
            }

            Class var14 = ((ClassIdentifier)var13).getTargetClass();
            this.classOfStaticMethod = var14;
            if (var14 != null) {
               return Reflect.invokeStaticMethod(var6, var14, var5, var2);
            } else {
               throw new UtilEvalError("invokeMethod: unknown target: " + var15);
            }
         }
      }
   }

   private Object invokeLocalMethod(Interpreter var1, Object[] var2, CallStack var3, SimpleNode var4) throws EvalError {
      if (Interpreter.DEBUG) {
         Interpreter.debug("invokeLocalMethod: " + this.value);
      }

      if (var1 == null) {
         throw new InterpreterError("invokeLocalMethod: interpreter = null");
      } else {
         String var5 = this.value;
         Class[] var6 = Types.getTypes(var2);
         BshMethod var7 = null;

         try {
            var7 = this.namespace.getMethod(var5, var6);
         } catch (UtilEvalError var15) {
            throw var15.toEvalError("Local method invocation", var4, var3);
         }

         if (var7 != null) {
            return var7.invoke(var2, var1, var3, var4);
         } else {
            BshClassManager var8 = var1.getClassManager();

            Object var9;
            try {
               var9 = this.namespace.getCommand(var5, var6, var1);
            } catch (UtilEvalError var14) {
               throw var14.toEvalError("Error loading command: ", var4, var3);
            }

            if (var9 == null) {
               BshMethod var10 = null;

               try {
                  var10 = this.namespace.getMethod("invoke", new Class[]{null, null});
               } catch (UtilEvalError var12) {
                  throw var12.toEvalError("Local method invocation", var4, var3);
               }

               if (var10 != null) {
                  return var10.invoke(new Object[]{var5, var2}, var1, var3, var4);
               } else {
                  throw new EvalError("Command not found: " + StringUtil.methodString(var5, var6), var4, var3);
               }
            } else if (var9 instanceof BshMethod) {
               return ((BshMethod)var9).invoke(var2, var1, var3, var4);
            } else if (var9 instanceof Class) {
               try {
                  return Reflect.invokeCompiledCommand((Class)var9, var2, var1, var3);
               } catch (UtilEvalError var13) {
                  throw var13.toEvalError("Error invoking compiled command: ", var4, var3);
               }
            } else {
               throw new InterpreterError("invalid command type");
            }
         }
      }
   }

   public static boolean isCompound(String var0) {
      return var0.indexOf(46) != -1;
   }

   static int countParts(String var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = -1; (var2 = var0.indexOf(46, var2 + 1)) != -1; ++var1) {
         }

         return var1 + 1;
      }
   }

   static String prefix(String var0) {
      return !isCompound(var0) ? null : prefix(var0, countParts(var0) - 1);
   }

   static String prefix(String var0, int var1) {
      if (var1 < 1) {
         return null;
      } else {
         int var2 = 0;
         int var3 = -1;

         while((var3 = var0.indexOf(46, var3 + 1)) != -1) {
            ++var2;
            if (var2 >= var1) {
               break;
            }
         }

         return var3 == -1 ? var0 : var0.substring(0, var3);
      }
   }

   static String suffix(String var0) {
      return !isCompound(var0) ? null : suffix(var0, countParts(var0) - 1);
   }

   public static String suffix(String var0, int var1) {
      if (var1 < 1) {
         return null;
      } else {
         int var2 = 0;
         int var3 = var0.length() + 1;

         while((var3 = var0.lastIndexOf(46, var3 - 1)) != -1) {
            ++var2;
            if (var2 >= var1) {
               break;
            }
         }

         return var3 == -1 ? var0 : var0.substring(var3 + 1);
      }
   }

   public String toString() {
      return this.value;
   }
}
