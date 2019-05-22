package bsh;

import java.io.Serializable;
import java.lang.reflect.Field;

class LHS implements ParserConstants, Serializable {
   NameSpace nameSpace;
   boolean localVar;
   static final int VARIABLE = 0;
   static final int FIELD = 1;
   static final int PROPERTY = 2;
   static final int INDEX = 3;
   static final int METHOD_EVAL = 4;
   int type;
   String varName;
   String propName;
   Field field;
   Object object;
   int index;

   LHS(NameSpace var1, String var2) {
      throw new Error("namespace lhs");
   }

   LHS(NameSpace var1, String var2, boolean var3) {
      this.type = 0;
      this.localVar = var3;
      this.varName = var2;
      this.nameSpace = var1;
   }

   LHS(Field var1) {
      this.type = 1;
      this.object = null;
      this.field = var1;
   }

   LHS(Object var1, Field var2) {
      if (var1 == null) {
         throw new NullPointerException("constructed empty LHS");
      } else {
         this.type = 1;
         this.object = var1;
         this.field = var2;
      }
   }

   LHS(Object var1, String var2) {
      if (var1 == null) {
         throw new NullPointerException("constructed empty LHS");
      } else {
         this.type = 2;
         this.object = var1;
         this.propName = var2;
      }
   }

   LHS(Object var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("constructed empty LHS");
      } else {
         this.type = 3;
         this.object = var1;
         this.index = var2;
      }
   }

   public Object getValue() throws UtilEvalError {
      if (this.type == 0) {
         return this.nameSpace.getVariable(this.varName);
      } else if (this.type == 1) {
         try {
            Object var1 = this.field.get(this.object);
            return Primitive.wrap(var1, this.field.getType());
         } catch (IllegalAccessException var2) {
            throw new UtilEvalError("Can't read field: " + this.field);
         }
      } else if (this.type == 2) {
         try {
            return Reflect.getObjectProperty(this.object, this.propName);
         } catch (ReflectError var3) {
            Interpreter.debug(var3.getMessage());
            throw new UtilEvalError("No such property: " + this.propName);
         }
      } else if (this.type == 3) {
         try {
            return Reflect.getIndex(this.object, this.index);
         } catch (Exception var4) {
            throw new UtilEvalError("Array access: " + var4);
         }
      } else {
         throw new InterpreterError("LHS type");
      }
   }

   public Object assign(Object var1, boolean var2) throws UtilEvalError {
      if (this.type == 0) {
         if (this.localVar) {
            this.nameSpace.setLocalVariable(this.varName, var1, var2);
         } else {
            this.nameSpace.setVariable(this.varName, var1, var2);
         }
      } else {
         if (this.type == 1) {
            try {
               Object var13 = var1 instanceof Primitive ? ((Primitive)var1).getValue() : var1;
               ReflectManager.RMSetAccessible(this.field);
               this.field.set(this.object, var13);
               return var1;
            } catch (NullPointerException var10) {
               throw new UtilEvalError("LHS (" + this.field.getName() + ") not a static field.");
            } catch (IllegalAccessException var11) {
               throw new UtilEvalError("LHS (" + this.field.getName() + ") can't access field: " + var11);
            } catch (IllegalArgumentException var12) {
               String var6 = var1 instanceof Primitive ? ((Primitive)var1).getType().getName() : var1.getClass().getName();
               throw new UtilEvalError("Argument type mismatch. " + (var1 == null ? "null" : var6) + " not assignable to field " + this.field.getName());
            }
         }

         if (this.type == 2) {
            CollectionManager var3 = CollectionManager.getCollectionManager();
            if (var3.isMap(this.object)) {
               var3.putInMap(this.object, this.propName, var1);
            } else {
               try {
                  Reflect.setObjectProperty(this.object, this.propName, var1);
               } catch (ReflectError var9) {
                  Interpreter.debug("Assignment: " + var9.getMessage());
                  throw new UtilEvalError("No such property: " + this.propName);
               }
            }
         } else {
            if (this.type != 3) {
               throw new InterpreterError("unknown lhs");
            }

            try {
               Reflect.setIndex(this.object, this.index, var1);
            } catch (UtilTargetError var7) {
               throw var7;
            } catch (Exception var8) {
               throw new UtilEvalError("Assignment: " + var8.getMessage());
            }
         }
      }

      return var1;
   }

   public String toString() {
      return "LHS: " + (this.field != null ? "field = " + this.field.toString() : "") + (this.varName != null ? " varName = " + this.varName : "") + (this.nameSpace != null ? " nameSpace = " + this.nameSpace.toString() : "");
   }
}
