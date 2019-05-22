package bsh;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

class BSHPrimarySuffix extends SimpleNode {
   public static final int CLASS = 0;
   public static final int INDEX = 1;
   public static final int NAME = 2;
   public static final int PROPERTY = 3;
   public int operation;
   Object index;
   public String field;

   BSHPrimarySuffix(int var1) {
      super(var1);
   }

   public Object doSuffix(Object var1, boolean var2, CallStack var3, Interpreter var4) throws EvalError {
      if (this.operation == 0) {
         if (var1 instanceof BSHType) {
            if (var2) {
               throw new EvalError("Can't assign .class", this, var3);
            } else {
               NameSpace var5 = var3.top();
               return ((BSHType)var1).getType(var3, var4);
            }
         } else {
            throw new EvalError("Attempt to use .class suffix on non class.", this, var3);
         }
      } else {
         if (var1 instanceof SimpleNode) {
            if (var1 instanceof BSHAmbiguousName) {
               var1 = ((BSHAmbiguousName)var1).toObject(var3, var4);
            } else {
               var1 = ((SimpleNode)var1).eval(var3, var4);
            }
         } else if (var1 instanceof LHS) {
            try {
               var1 = ((LHS)var1).getValue();
            } catch (UtilEvalError var9) {
               throw var9.toEvalError(this, var3);
            }
         }

         try {
            switch(this.operation) {
            case 1:
               return this.doIndex(var1, var2, var3, var4);
            case 2:
               return this.doName(var1, var2, var3, var4);
            case 3:
               return this.doProperty(var2, var1, var3, var4);
            default:
               throw new InterpreterError("Unknown suffix type");
            }
         } catch (ReflectError var7) {
            throw new EvalError("reflection error: " + var7, this, var3);
         } catch (InvocationTargetException var8) {
            throw new TargetError("target exception", var8.getTargetException(), this, var3, true);
         }
      }
   }

   private Object doName(Object var1, boolean var2, CallStack var3, Interpreter var4) throws EvalError, ReflectError, InvocationTargetException {
      try {
         if (this.field.equals("length") && var1.getClass().isArray()) {
            if (var2) {
               throw new EvalError("Can't assign array length", this, var3);
            } else {
               return new Primitive(Array.getLength(var1));
            }
         } else if (this.jjtGetNumChildren() == 0) {
            return var2 ? Reflect.getLHSObjectField(var1, this.field) : Reflect.getObjectFieldValue(var1, this.field);
         } else {
            Object[] var5 = ((BSHArguments)this.jjtGetChild(0)).getArguments(var3, var4);

            try {
               return Reflect.invokeObjectMethod(var1, this.field, var5, var4, var3, this);
            } catch (ReflectError var11) {
               throw new EvalError("Error in method invocation: " + var11.getMessage(), this, var3);
            } catch (InvocationTargetException var12) {
               String var8 = "Method Invocation " + this.field;
               Throwable var9 = var12.getTargetException();
               boolean var10 = true;
               if (var9 instanceof EvalError) {
                  if (var9 instanceof TargetError) {
                     var10 = ((TargetError)var9).inNativeCode();
                  } else {
                     var10 = false;
                  }
               }

               throw new TargetError(var8, var9, this, var3, var10);
            }
         }
      } catch (UtilEvalError var13) {
         throw var13.toEvalError(this, var3);
      }
   }

   static int getIndexAux(Object var0, CallStack var1, Interpreter var2, SimpleNode var3) throws EvalError {
      if (!var0.getClass().isArray()) {
         throw new EvalError("Not an array", var3, var1);
      } else {
         try {
            Object var4 = ((SimpleNode)var3.jjtGetChild(0)).eval(var1, var2);
            if (!(var4 instanceof Primitive)) {
               var4 = Types.castObject(var4, Integer.TYPE, 1);
            }

            int var5 = ((Primitive)var4).intValue();
            return var5;
         } catch (UtilEvalError var6) {
            Interpreter.debug("doIndex: " + var6);
            throw var6.toEvalError("Arrays may only be indexed by integer types.", var3, var1);
         }
      }
   }

   private Object doIndex(Object var1, boolean var2, CallStack var3, Interpreter var4) throws EvalError, ReflectError {
      int var5 = getIndexAux(var1, var3, var4, this);
      if (var2) {
         return new LHS(var1, var5);
      } else {
         try {
            return Reflect.getIndex(var1, var5);
         } catch (UtilEvalError var7) {
            throw var7.toEvalError(this, var3);
         }
      }
   }

   private Object doProperty(boolean var1, Object var2, CallStack var3, Interpreter var4) throws EvalError {
      if (var2 == Primitive.VOID) {
         throw new EvalError("Attempt to access property on undefined variable or class name", this, var3);
      } else if (var2 instanceof Primitive) {
         throw new EvalError("Attempt to access property on a primitive", this, var3);
      } else {
         Object var5 = ((SimpleNode)this.jjtGetChild(0)).eval(var3, var4);
         if (!(var5 instanceof String)) {
            throw new EvalError("Property expression must be a String or identifier.", this, var3);
         } else if (var1) {
            return new LHS(var2, (String)var5);
         } else {
            CollectionManager var6 = CollectionManager.getCollectionManager();
            if (var6.isMap(var2)) {
               Object var7 = var6.getFromMap(var2, var5);
               return var7 == null ? Primitive.NULL : var7;
            } else {
               try {
                  return Reflect.getObjectProperty(var2, (String)var5);
               } catch (UtilEvalError var9) {
                  throw var9.toEvalError("Property: " + var5, this, var3);
               } catch (ReflectError var10) {
                  throw new EvalError("No such property: " + var5, this, var3);
               }
            }
         }
      }
   }
}
