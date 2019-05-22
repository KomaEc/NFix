package bsh;

class Types {
   static final int CAST = 0;
   static final int ASSIGNMENT = 1;
   static final int JAVA_BASE_ASSIGNABLE = 1;
   static final int JAVA_BOX_TYPES_ASSIGABLE = 2;
   static final int JAVA_VARARGS_ASSIGNABLE = 3;
   static final int BSH_ASSIGNABLE = 4;
   static final int FIRST_ROUND_ASSIGNABLE = 1;
   static final int LAST_ROUND_ASSIGNABLE = 4;
   static Primitive VALID_CAST = new Primitive(1);
   static Primitive INVALID_CAST = new Primitive(-1);
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$Number;
   // $FF: synthetic field
   static Class class$bsh$Primitive;
   // $FF: synthetic field
   static Class class$bsh$This;

   public static Class[] getTypes(Object[] var0) {
      if (var0 == null) {
         return new Class[0];
      } else {
         Class[] var1 = new Class[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == null) {
               var1[var2] = null;
            } else if (var0[var2] instanceof Primitive) {
               var1[var2] = ((Primitive)var0[var2]).getType();
            } else {
               var1[var2] = var0[var2].getClass();
            }
         }

         return var1;
      }
   }

   static boolean isSignatureAssignable(Class[] var0, Class[] var1, int var2) {
      if (var2 != 3 && var0.length != var1.length) {
         return false;
      } else {
         switch(var2) {
         case 1:
            for(int var3 = 0; var3 < var0.length; ++var3) {
               if (!isJavaBaseAssignable(var1[var3], var0[var3])) {
                  return false;
               }
            }

            return true;
         case 2:
            for(int var4 = 0; var4 < var0.length; ++var4) {
               if (!isJavaBoxTypesAssignable(var1[var4], var0[var4])) {
                  return false;
               }
            }

            return true;
         case 3:
            return isSignatureVarargsAssignable(var0, var1);
         case 4:
            for(int var5 = 0; var5 < var0.length; ++var5) {
               if (!isBshAssignable(var1[var5], var0[var5])) {
                  return false;
               }
            }

            return true;
         default:
            throw new InterpreterError("bad case");
         }
      }
   }

   private static boolean isSignatureVarargsAssignable(Class[] var0, Class[] var1) {
      return false;
   }

   static boolean isJavaAssignable(Class var0, Class var1) {
      return isJavaBaseAssignable(var0, var1) || isJavaBoxTypesAssignable(var0, var1);
   }

   static boolean isJavaBaseAssignable(Class var0, Class var1) {
      if (var0 == null) {
         return false;
      } else if (var1 == null) {
         return !var0.isPrimitive();
      } else {
         if (var0.isPrimitive() && var1.isPrimitive()) {
            if (var0 == var1) {
               return true;
            }

            if (var1 == Byte.TYPE && (var0 == Short.TYPE || var0 == Integer.TYPE || var0 == Long.TYPE || var0 == Float.TYPE || var0 == Double.TYPE)) {
               return true;
            }

            if (var1 == Short.TYPE && (var0 == Integer.TYPE || var0 == Long.TYPE || var0 == Float.TYPE || var0 == Double.TYPE)) {
               return true;
            }

            if (var1 == Character.TYPE && (var0 == Integer.TYPE || var0 == Long.TYPE || var0 == Float.TYPE || var0 == Double.TYPE)) {
               return true;
            }

            if (var1 == Integer.TYPE && (var0 == Long.TYPE || var0 == Float.TYPE || var0 == Double.TYPE)) {
               return true;
            }

            if (var1 == Long.TYPE && (var0 == Float.TYPE || var0 == Double.TYPE)) {
               return true;
            }

            if (var1 == Float.TYPE && var0 == Double.TYPE) {
               return true;
            }
         } else if (var0.isAssignableFrom(var1)) {
            return true;
         }

         return false;
      }
   }

   static boolean isJavaBoxTypesAssignable(Class var0, Class var1) {
      if (var0 == null) {
         return false;
      } else if (var0 == (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
         return true;
      } else if (var0 == (class$java$lang$Number == null ? (class$java$lang$Number = class$("java.lang.Number")) : class$java$lang$Number) && var1 != Character.TYPE && var1 != Boolean.TYPE) {
         return true;
      } else {
         return Primitive.wrapperMap.get(var0) == var1;
      }
   }

   static boolean isBshAssignable(Class var0, Class var1) {
      try {
         return castObject(var0, var1, (Object)null, 1, true) == VALID_CAST;
      } catch (UtilEvalError var3) {
         throw new InterpreterError("err in cast check: " + var3);
      }
   }

   public static Object castObject(Object var0, Class var1, int var2) throws UtilEvalError {
      if (var0 == null) {
         throw new InterpreterError("null fromValue");
      } else {
         Class var3 = var0 instanceof Primitive ? ((Primitive)var0).getType() : var0.getClass();
         return castObject(var1, var3, var0, var2, false);
      }
   }

   private static Object castObject(Class var0, Class var1, Object var2, int var3, boolean var4) throws UtilEvalError {
      if (var4 && var2 != null) {
         throw new InterpreterError("bad cast params 1");
      } else if (!var4 && var2 == null) {
         throw new InterpreterError("bad cast params 2");
      } else if (var1 == (class$bsh$Primitive == null ? (class$bsh$Primitive = class$("bsh.Primitive")) : class$bsh$Primitive)) {
         throw new InterpreterError("bad from Type, need to unwrap");
      } else if (var2 == Primitive.NULL && var1 != null) {
         throw new InterpreterError("inconsistent args 1");
      } else if (var2 == Primitive.VOID && var1 != Void.TYPE) {
         throw new InterpreterError("inconsistent args 2");
      } else if (var0 == Void.TYPE) {
         throw new InterpreterError("loose toType should be null");
      } else if (var0 != null && var0 != var1) {
         if (var0.isPrimitive()) {
            if (var1 != Void.TYPE && var1 != null && !var1.isPrimitive()) {
               if (Primitive.isWrapperType(var1)) {
                  Class var5 = Primitive.unboxType(var1);
                  Primitive var6;
                  if (var4) {
                     var6 = null;
                  } else {
                     var6 = (Primitive)Primitive.wrap(var2, var5);
                  }

                  return Primitive.castPrimitive(var0, var5, var6, var4, var3);
               } else if (var4) {
                  return INVALID_CAST;
               } else {
                  throw castError(var0, var1, var3);
               }
            } else {
               return Primitive.castPrimitive(var0, var1, (Primitive)var2, var4, var3);
            }
         } else if (var1 != Void.TYPE && var1 != null && !var1.isPrimitive()) {
            if (var0.isAssignableFrom(var1)) {
               return var4 ? VALID_CAST : var2;
            } else if (var0.isInterface() && (class$bsh$This == null ? (class$bsh$This = class$("bsh.This")) : class$bsh$This).isAssignableFrom(var1) && Capabilities.canGenerateInterfaces()) {
               return var4 ? VALID_CAST : ((This)var2).getInterface(var0);
            } else if (Primitive.isWrapperType(var0) && Primitive.isWrapperType(var1)) {
               return var4 ? VALID_CAST : Primitive.castWrapper(var0, var2);
            } else if (var4) {
               return INVALID_CAST;
            } else {
               throw castError(var0, var1, var3);
            }
         } else if (Primitive.isWrapperType(var0) && var1 != Void.TYPE && var1 != null) {
            return var4 ? VALID_CAST : Primitive.castWrapper(Primitive.unboxType(var0), ((Primitive)var2).getValue());
         } else if (var0 == (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object) && var1 != Void.TYPE && var1 != null) {
            return var4 ? VALID_CAST : ((Primitive)var2).getValue();
         } else {
            return Primitive.castPrimitive(var0, var1, (Primitive)var2, var4, var3);
         }
      } else {
         return var4 ? VALID_CAST : var2;
      }
   }

   static UtilEvalError castError(Class var0, Class var1, int var2) {
      return castError(Reflect.normalizeClassName(var0), Reflect.normalizeClassName(var1), var2);
   }

   static UtilEvalError castError(String var0, String var1, int var2) {
      if (var2 == 1) {
         return new UtilEvalError("Can't assign " + var1 + " to " + var0);
      } else {
         ClassCastException var3 = new ClassCastException("Cannot cast " + var1 + " to " + var0);
         return new UtilTargetError(var3);
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
}
