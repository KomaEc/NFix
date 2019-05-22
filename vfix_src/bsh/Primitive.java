package bsh;

import java.io.Serializable;
import java.util.Hashtable;

public final class Primitive implements ParserConstants, Serializable {
   static Hashtable wrapperMap = new Hashtable();
   private Object value;
   public static final Primitive NULL;
   public static final Primitive VOID;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Character;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$bsh$Primitive;

   public Primitive(Object var1) {
      if (var1 == null) {
         throw new InterpreterError("Use Primitve.NULL instead of Primitive(null)");
      } else if (var1 != Primitive.Special.NULL_VALUE && var1 != Primitive.Special.VOID_TYPE && !isWrapperType(var1.getClass())) {
         throw new InterpreterError("Not a wrapper type: " + var1);
      } else {
         this.value = var1;
      }
   }

   public Primitive(boolean var1) {
      this(new Boolean(var1));
   }

   public Primitive(byte var1) {
      this(new Byte(var1));
   }

   public Primitive(short var1) {
      this(new Short(var1));
   }

   public Primitive(char var1) {
      this(new Character(var1));
   }

   public Primitive(int var1) {
      this(new Integer(var1));
   }

   public Primitive(long var1) {
      this(new Long(var1));
   }

   public Primitive(float var1) {
      this(new Float(var1));
   }

   public Primitive(double var1) {
      this(new Double(var1));
   }

   public Object getValue() {
      if (this.value == Primitive.Special.NULL_VALUE) {
         return null;
      } else if (this.value == Primitive.Special.VOID_TYPE) {
         throw new InterpreterError("attempt to unwrap void type");
      } else {
         return this.value;
      }
   }

   public String toString() {
      if (this.value == Primitive.Special.NULL_VALUE) {
         return "null";
      } else {
         return this.value == Primitive.Special.VOID_TYPE ? "void" : this.value.toString();
      }
   }

   public Class getType() {
      if (this == VOID) {
         return Void.TYPE;
      } else {
         return this == NULL ? null : unboxType(this.value.getClass());
      }
   }

   public static Object binaryOperation(Object var0, Object var1, int var2) throws UtilEvalError {
      if (var0 != NULL && var1 != NULL) {
         if (var0 != VOID && var1 != VOID) {
            Class var3 = var0.getClass();
            Class var4 = var1.getClass();
            if (var0 instanceof Primitive) {
               var0 = ((Primitive)var0).getValue();
            }

            if (var1 instanceof Primitive) {
               var1 = ((Primitive)var1).getValue();
            }

            Object[] var5 = promotePrimitives(var0, var1);
            Object var6 = var5[0];
            Object var7 = var5[1];
            if (var6.getClass() != var7.getClass()) {
               throw new UtilEvalError("Type mismatch in operator.  " + var6.getClass() + " cannot be used with " + var7.getClass());
            } else {
               Object var8;
               try {
                  var8 = binaryOperationImpl(var6, var7, var2);
               } catch (ArithmeticException var10) {
                  throw new UtilTargetError("Arithemetic Exception in binary op", var10);
               }

               return (var3 != (class$bsh$Primitive == null ? (class$bsh$Primitive = class$("bsh.Primitive")) : class$bsh$Primitive) || var4 != (class$bsh$Primitive == null ? (class$bsh$Primitive = class$("bsh.Primitive")) : class$bsh$Primitive)) && !(var8 instanceof Boolean) ? var8 : new Primitive(var8);
            }
         } else {
            throw new UtilEvalError("Undefined variable, class, or 'void' literal in binary operation");
         }
      } else {
         throw new UtilEvalError("Null value or 'null' literal in binary operation");
      }
   }

   static Object binaryOperationImpl(Object var0, Object var1, int var2) throws UtilEvalError {
      if (var0 instanceof Boolean) {
         return booleanBinaryOperation((Boolean)var0, (Boolean)var1, var2);
      } else if (var0 instanceof Integer) {
         return intBinaryOperation((Integer)var0, (Integer)var1, var2);
      } else if (var0 instanceof Long) {
         return longBinaryOperation((Long)var0, (Long)var1, var2);
      } else if (var0 instanceof Float) {
         return floatBinaryOperation((Float)var0, (Float)var1, var2);
      } else if (var0 instanceof Double) {
         return doubleBinaryOperation((Double)var0, (Double)var1, var2);
      } else {
         throw new UtilEvalError("Invalid types in binary operator");
      }
   }

   static Boolean booleanBinaryOperation(Boolean var0, Boolean var1, int var2) {
      boolean var3 = var0;
      boolean var4 = var1;
      switch(var2) {
      case 90:
         return new Boolean(var3 == var4);
      case 91:
      case 92:
      case 93:
      case 94:
      default:
         throw new InterpreterError("unimplemented binary operator");
      case 95:
         return new Boolean(var3 != var4);
      case 96:
      case 97:
         return new Boolean(var3 || var4);
      case 98:
      case 99:
         return new Boolean(var3 && var4);
      }
   }

   static Object longBinaryOperation(Long var0, Long var1, int var2) {
      long var3 = var0;
      long var5 = var1;
      switch(var2) {
      case 82:
      case 83:
         return new Boolean(var3 > var5);
      case 84:
      case 85:
         return new Boolean(var3 < var5);
      case 86:
      case 87:
      case 88:
      case 89:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      default:
         throw new InterpreterError("Unimplemented binary long operator");
      case 90:
         return new Boolean(var3 == var5);
      case 91:
      case 92:
         return new Boolean(var3 <= var5);
      case 93:
      case 94:
         return new Boolean(var3 >= var5);
      case 95:
         return new Boolean(var3 != var5);
      case 102:
         return new Long(var3 + var5);
      case 103:
         return new Long(var3 - var5);
      case 104:
         return new Long(var3 * var5);
      case 105:
         return new Long(var3 / var5);
      case 106:
      case 107:
         return new Long(var3 & var5);
      case 108:
      case 109:
         return new Long(var3 | var5);
      case 110:
         return new Long(var3 ^ var5);
      case 111:
         return new Long(var3 % var5);
      case 112:
      case 113:
         return new Long(var3 << (int)var5);
      case 114:
      case 115:
         return new Long(var3 >> (int)var5);
      case 116:
      case 117:
         return new Long(var3 >>> (int)var5);
      }
   }

   static Object intBinaryOperation(Integer var0, Integer var1, int var2) {
      int var3 = var0;
      int var4 = var1;
      switch(var2) {
      case 82:
      case 83:
         return new Boolean(var3 > var4);
      case 84:
      case 85:
         return new Boolean(var3 < var4);
      case 86:
      case 87:
      case 88:
      case 89:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      default:
         throw new InterpreterError("Unimplemented binary integer operator");
      case 90:
         return new Boolean(var3 == var4);
      case 91:
      case 92:
         return new Boolean(var3 <= var4);
      case 93:
      case 94:
         return new Boolean(var3 >= var4);
      case 95:
         return new Boolean(var3 != var4);
      case 102:
         return new Integer(var3 + var4);
      case 103:
         return new Integer(var3 - var4);
      case 104:
         return new Integer(var3 * var4);
      case 105:
         return new Integer(var3 / var4);
      case 106:
      case 107:
         return new Integer(var3 & var4);
      case 108:
      case 109:
         return new Integer(var3 | var4);
      case 110:
         return new Integer(var3 ^ var4);
      case 111:
         return new Integer(var3 % var4);
      case 112:
      case 113:
         return new Integer(var3 << var4);
      case 114:
      case 115:
         return new Integer(var3 >> var4);
      case 116:
      case 117:
         return new Integer(var3 >>> var4);
      }
   }

   static Object doubleBinaryOperation(Double var0, Double var1, int var2) throws UtilEvalError {
      double var3 = var0;
      double var5 = var1;
      switch(var2) {
      case 82:
      case 83:
         return new Boolean(var3 > var5);
      case 84:
      case 85:
         return new Boolean(var3 < var5);
      case 86:
      case 87:
      case 88:
      case 89:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      default:
         throw new InterpreterError("Unimplemented binary double operator");
      case 90:
         return new Boolean(var3 == var5);
      case 91:
      case 92:
         return new Boolean(var3 <= var5);
      case 93:
      case 94:
         return new Boolean(var3 >= var5);
      case 95:
         return new Boolean(var3 != var5);
      case 102:
         return new Double(var3 + var5);
      case 103:
         return new Double(var3 - var5);
      case 104:
         return new Double(var3 * var5);
      case 105:
         return new Double(var3 / var5);
      case 111:
         return new Double(var3 % var5);
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
         throw new UtilEvalError("Can't shift doubles");
      }
   }

   static Object floatBinaryOperation(Float var0, Float var1, int var2) throws UtilEvalError {
      float var3 = var0;
      float var4 = var1;
      switch(var2) {
      case 82:
      case 83:
         return new Boolean(var3 > var4);
      case 84:
      case 85:
         return new Boolean(var3 < var4);
      case 86:
      case 87:
      case 88:
      case 89:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      default:
         throw new InterpreterError("Unimplemented binary float operator");
      case 90:
         return new Boolean(var3 == var4);
      case 91:
      case 92:
         return new Boolean(var3 <= var4);
      case 93:
      case 94:
         return new Boolean(var3 >= var4);
      case 95:
         return new Boolean(var3 != var4);
      case 102:
         return new Float(var3 + var4);
      case 103:
         return new Float(var3 - var4);
      case 104:
         return new Float(var3 * var4);
      case 105:
         return new Float(var3 / var4);
      case 111:
         return new Float(var3 % var4);
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
         throw new UtilEvalError("Can't shift floats ");
      }
   }

   static Object promoteToInteger(Object var0) {
      if (var0 instanceof Character) {
         return new Integer((Character)var0);
      } else {
         return !(var0 instanceof Byte) && !(var0 instanceof Short) ? var0 : new Integer(((Number)var0).intValue());
      }
   }

   static Object[] promotePrimitives(Object var0, Object var1) {
      var0 = promoteToInteger(var0);
      var1 = promoteToInteger(var1);
      if (var0 instanceof Number && var1 instanceof Number) {
         Number var2 = (Number)var0;
         Number var3 = (Number)var1;
         boolean var4;
         if (!(var4 = var2 instanceof Double) && !(var3 instanceof Double)) {
            if (!(var4 = var2 instanceof Float) && !(var3 instanceof Float)) {
               if ((var4 = var2 instanceof Long) || var3 instanceof Long) {
                  if (var4) {
                     var1 = new Long(var3.longValue());
                  } else {
                     var0 = new Long(var2.longValue());
                  }
               }
            } else if (var4) {
               var1 = new Float(var3.floatValue());
            } else {
               var0 = new Float(var2.floatValue());
            }
         } else if (var4) {
            var1 = new Double(var3.doubleValue());
         } else {
            var0 = new Double(var2.doubleValue());
         }
      }

      return new Object[]{var0, var1};
   }

   public static Primitive unaryOperation(Primitive var0, int var1) throws UtilEvalError {
      if (var0 == NULL) {
         throw new UtilEvalError("illegal use of null object or 'null' literal");
      } else if (var0 == VOID) {
         throw new UtilEvalError("illegal use of undefined object or 'void' literal");
      } else {
         Class var2 = var0.getType();
         Object var3 = promoteToInteger(var0.getValue());
         if (var3 instanceof Boolean) {
            return new Primitive(booleanUnaryOperation((Boolean)var3, var1));
         } else if (!(var3 instanceof Integer)) {
            if (var3 instanceof Long) {
               return new Primitive(longUnaryOperation((Long)var3, var1));
            } else if (var3 instanceof Float) {
               return new Primitive(floatUnaryOperation((Float)var3, var1));
            } else if (var3 instanceof Double) {
               return new Primitive(doubleUnaryOperation((Double)var3, var1));
            } else {
               throw new InterpreterError("An error occurred.  Please call technical support.");
            }
         } else {
            int var4 = intUnaryOperation((Integer)var3, var1);
            if (var1 == 100 || var1 == 101) {
               if (var2 == Byte.TYPE) {
                  return new Primitive((byte)var4);
               }

               if (var2 == Short.TYPE) {
                  return new Primitive((short)var4);
               }

               if (var2 == Character.TYPE) {
                  return new Primitive((char)var4);
               }
            }

            return new Primitive(var4);
         }
      }
   }

   static boolean booleanUnaryOperation(Boolean var0, int var1) throws UtilEvalError {
      boolean var2 = var0;
      switch(var1) {
      case 86:
         return !var2;
      default:
         throw new UtilEvalError("Operator inappropriate for boolean");
      }
   }

   static int intUnaryOperation(Integer var0, int var1) {
      int var2 = var0;
      switch(var1) {
      case 87:
         return ~var2;
      case 100:
         return var2 + 1;
      case 101:
         return var2 - 1;
      case 102:
         return var2;
      case 103:
         return -var2;
      default:
         throw new InterpreterError("bad integer unaryOperation");
      }
   }

   static long longUnaryOperation(Long var0, int var1) {
      long var2 = var0;
      switch(var1) {
      case 87:
         return ~var2;
      case 100:
         return var2 + 1L;
      case 101:
         return var2 - 1L;
      case 102:
         return var2;
      case 103:
         return -var2;
      default:
         throw new InterpreterError("bad long unaryOperation");
      }
   }

   static float floatUnaryOperation(Float var0, int var1) {
      float var2 = var0;
      switch(var1) {
      case 102:
         return var2;
      case 103:
         return -var2;
      default:
         throw new InterpreterError("bad float unaryOperation");
      }
   }

   static double doubleUnaryOperation(Double var0, int var1) {
      double var2 = var0;
      switch(var1) {
      case 102:
         return var2;
      case 103:
         return -var2;
      default:
         throw new InterpreterError("bad double unaryOperation");
      }
   }

   public int intValue() throws UtilEvalError {
      if (this.value instanceof Number) {
         return ((Number)this.value).intValue();
      } else {
         throw new UtilEvalError("Primitive not a number");
      }
   }

   public boolean booleanValue() throws UtilEvalError {
      if (this.value instanceof Boolean) {
         return (Boolean)this.value;
      } else {
         throw new UtilEvalError("Primitive not a boolean");
      }
   }

   public boolean isNumber() {
      return !(this.value instanceof Boolean) && this != NULL && this != VOID;
   }

   public Number numberValue() throws UtilEvalError {
      Object var1 = this.value;
      if (var1 instanceof Character) {
         var1 = new Integer((Character)var1);
      }

      if (var1 instanceof Number) {
         return (Number)var1;
      } else {
         throw new UtilEvalError("Primitive not a number");
      }
   }

   public boolean equals(Object var1) {
      return var1 instanceof Primitive ? ((Primitive)var1).value.equals(this.value) : false;
   }

   public int hashCode() {
      return this.value.hashCode() * 21;
   }

   public static Object unwrap(Object var0) {
      if (var0 == VOID) {
         return null;
      } else {
         return var0 instanceof Primitive ? ((Primitive)var0).getValue() : var0;
      }
   }

   public static Object[] unwrap(Object[] var0) {
      Object[] var1 = new Object[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = unwrap(var0[var2]);
      }

      return var1;
   }

   public static Object[] wrap(Object[] var0, Class[] var1) {
      if (var0 == null) {
         return null;
      } else {
         Object[] var2 = new Object[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2[var3] = wrap(var0[var3], var1[var3]);
         }

         return var2;
      }
   }

   public static Object wrap(Object var0, Class var1) {
      if (var1 == Void.TYPE) {
         return VOID;
      } else if (var0 == null) {
         return NULL;
      } else {
         return var1.isPrimitive() ? new Primitive(var0) : var0;
      }
   }

   public static Primitive getDefaultValue(Class var0) {
      if (var0 != null && var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return new Primitive(false);
         } else {
            try {
               return (new Primitive(0)).castToType(var0, 0);
            } catch (UtilEvalError var2) {
               throw new InterpreterError("bad cast");
            }
         }
      } else {
         return NULL;
      }
   }

   public static Class boxType(Class var0) {
      Class var1 = (Class)wrapperMap.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         throw new InterpreterError("Not a primitive type: " + var0);
      }
   }

   public static Class unboxType(Class var0) {
      Class var1 = (Class)wrapperMap.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         throw new InterpreterError("Not a primitive wrapper type: " + var0);
      }
   }

   public Primitive castToType(Class var1, int var2) throws UtilEvalError {
      return castPrimitive(var1, this.getType(), this, false, var2);
   }

   static Primitive castPrimitive(Class var0, Class var1, Primitive var2, boolean var3, int var4) throws UtilEvalError {
      if (var3 && var2 != null) {
         throw new InterpreterError("bad cast param 1");
      } else if (!var3 && var2 == null) {
         throw new InterpreterError("bad cast param 2");
      } else if (var1 != null && !var1.isPrimitive()) {
         throw new InterpreterError("bad fromType:" + var1);
      } else if (var2 == NULL && var1 != null) {
         throw new InterpreterError("inconsistent args 1");
      } else if (var2 == VOID && var1 != Void.TYPE) {
         throw new InterpreterError("inconsistent args 2");
      } else if (var1 == Void.TYPE) {
         if (var3) {
            return Types.INVALID_CAST;
         } else {
            throw Types.castError(Reflect.normalizeClassName(var0), "void value", var4);
         }
      } else {
         Object var5 = null;
         if (var2 != null) {
            var5 = var2.getValue();
         }

         if (var0.isPrimitive()) {
            if (var1 == null) {
               if (var3) {
                  return Types.INVALID_CAST;
               } else {
                  throw Types.castError("primitive type:" + var0, "Null value", var4);
               }
            } else if (var1 == Boolean.TYPE) {
               if (var0 != Boolean.TYPE) {
                  if (var3) {
                     return Types.INVALID_CAST;
                  } else {
                     throw Types.castError(var0, var1, var4);
                  }
               } else {
                  return var3 ? Types.VALID_CAST : var2;
               }
            } else if (var4 == 1 && !Types.isJavaAssignable(var0, var1)) {
               if (var3) {
                  return Types.INVALID_CAST;
               } else {
                  throw Types.castError(var0, var1, var4);
               }
            } else {
               return var3 ? Types.VALID_CAST : new Primitive(castWrapper(var0, var5));
            }
         } else if (var1 == null) {
            return var3 ? Types.VALID_CAST : NULL;
         } else if (var3) {
            return Types.INVALID_CAST;
         } else {
            throw Types.castError("object type:" + var0, "primitive value", var4);
         }
      }
   }

   public static boolean isWrapperType(Class var0) {
      return wrapperMap.get(var0) != null && !var0.isPrimitive();
   }

   static Object castWrapper(Class var0, Object var1) {
      if (!var0.isPrimitive()) {
         throw new InterpreterError("invalid type in castWrapper: " + var0);
      } else if (var1 == null) {
         throw new InterpreterError("null value in castWrapper, guard");
      } else if (var1 instanceof Boolean) {
         if (var0 != Boolean.TYPE) {
            throw new InterpreterError("bad wrapper cast of boolean");
         } else {
            return var1;
         }
      } else {
         if (var1 instanceof Character) {
            var1 = new Integer((Character)var1);
         }

         if (!(var1 instanceof Number)) {
            throw new InterpreterError("bad type in cast");
         } else {
            Number var2 = (Number)var1;
            if (var0 == Byte.TYPE) {
               return new Byte(var2.byteValue());
            } else if (var0 == Short.TYPE) {
               return new Short(var2.shortValue());
            } else if (var0 == Character.TYPE) {
               return new Character((char)var2.intValue());
            } else if (var0 == Integer.TYPE) {
               return new Integer(var2.intValue());
            } else if (var0 == Long.TYPE) {
               return new Long(var2.longValue());
            } else if (var0 == Float.TYPE) {
               return new Float(var2.floatValue());
            } else if (var0 == Double.TYPE) {
               return new Double(var2.doubleValue());
            } else {
               throw new InterpreterError("error in wrapper cast");
            }
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      wrapperMap.put(Boolean.TYPE, class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean);
      wrapperMap.put(Byte.TYPE, class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte);
      wrapperMap.put(Short.TYPE, class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short);
      wrapperMap.put(Character.TYPE, class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character);
      wrapperMap.put(Integer.TYPE, class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer);
      wrapperMap.put(Long.TYPE, class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long);
      wrapperMap.put(Float.TYPE, class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float);
      wrapperMap.put(Double.TYPE, class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double);
      wrapperMap.put(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean, Boolean.TYPE);
      wrapperMap.put(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte, Byte.TYPE);
      wrapperMap.put(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short, Short.TYPE);
      wrapperMap.put(class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character, Character.TYPE);
      wrapperMap.put(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer, Integer.TYPE);
      wrapperMap.put(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long, Long.TYPE);
      wrapperMap.put(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float, Float.TYPE);
      wrapperMap.put(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, Double.TYPE);
      NULL = new Primitive(Primitive.Special.NULL_VALUE);
      VOID = new Primitive(Primitive.Special.VOID_TYPE);
   }

   private static class Special implements Serializable {
      public static final Primitive.Special NULL_VALUE = new Primitive.Special();
      public static final Primitive.Special VOID_TYPE = new Primitive.Special();
   }
}
