package bsh;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

class Reflect {
   public static Object invokeObjectMethod(Object var0, String var1, Object[] var2, Interpreter var3, CallStack var4, SimpleNode var5) throws ReflectError, EvalError, InvocationTargetException {
      if (var0 instanceof This && !This.isExposedThisMethod(var1)) {
         return ((This)var0).invokeMethod(var1, var2, var3, var4, var5, false);
      } else {
         try {
            BshClassManager var6 = var3 == null ? null : var3.getClassManager();
            Class var7 = var0.getClass();
            Method var8 = resolveExpectedJavaMethod(var6, var7, var0, var1, var2, false);
            return invokeMethod(var8, var0, var2);
         } catch (UtilEvalError var9) {
            throw var9.toEvalError(var5, var4);
         }
      }
   }

   public static Object invokeStaticMethod(BshClassManager var0, Class var1, String var2, Object[] var3) throws ReflectError, UtilEvalError, InvocationTargetException {
      Interpreter.debug("invoke static Method");
      Method var4 = resolveExpectedJavaMethod(var0, var1, (Object)null, var2, var3, true);
      return invokeMethod(var4, (Object)null, var3);
   }

   static Object invokeMethod(Method var0, Object var1, Object[] var2) throws ReflectError, InvocationTargetException {
      if (var2 == null) {
         var2 = new Object[0];
      }

      logInvokeMethod("Invoking method (entry): ", var0, var2);
      Object[] var3 = new Object[var2.length];
      Class[] var4 = var0.getParameterTypes();

      try {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            var3[var5] = Types.castObject(var2[var5], var4[var5], 1);
         }
      } catch (UtilEvalError var8) {
         throw new InterpreterError("illegal argument type in method invocation: " + var8);
      }

      var3 = Primitive.unwrap(var3);
      logInvokeMethod("Invoking method (after massaging values): ", var0, var3);

      try {
         Object var9 = var0.invoke(var1, var3);
         if (var9 == null) {
            var9 = Primitive.NULL;
         }

         Class var6 = var0.getReturnType();
         return Primitive.wrap(var9, var6);
      } catch (IllegalAccessException var7) {
         throw new ReflectError("Cannot access method " + StringUtil.methodString(var0.getName(), var0.getParameterTypes()) + " in '" + var0.getDeclaringClass() + "' :" + var7);
      }
   }

   public static Object getIndex(Object var0, int var1) throws ReflectError, UtilTargetError {
      if (Interpreter.DEBUG) {
         Interpreter.debug("getIndex: " + var0 + ", index=" + var1);
      }

      try {
         Object var2 = Array.get(var0, var1);
         return Primitive.wrap(var2, var0.getClass().getComponentType());
      } catch (ArrayIndexOutOfBoundsException var4) {
         throw new UtilTargetError(var4);
      } catch (Exception var5) {
         throw new ReflectError("Array access:" + var5);
      }
   }

   public static void setIndex(Object var0, int var1, Object var2) throws ReflectError, UtilTargetError {
      try {
         var2 = Primitive.unwrap(var2);
         Array.set(var0, var1, var2);
      } catch (ArrayStoreException var6) {
         throw new UtilTargetError(var6);
      } catch (IllegalArgumentException var7) {
         throw new UtilTargetError(new ArrayStoreException(var7.toString()));
      } catch (Exception var8) {
         throw new ReflectError("Array access:" + var8);
      }
   }

   public static Object getStaticFieldValue(Class var0, String var1) throws UtilEvalError, ReflectError {
      return getFieldValue(var0, (Object)null, var1, true);
   }

   public static Object getObjectFieldValue(Object var0, String var1) throws UtilEvalError, ReflectError {
      if (var0 instanceof This) {
         return ((This)var0).namespace.getVariable(var1);
      } else {
         try {
            return getFieldValue(var0.getClass(), var0, var1, false);
         } catch (ReflectError var3) {
            if (hasObjectPropertyGetter(var0.getClass(), var1)) {
               return getObjectProperty(var0, var1);
            } else {
               throw var3;
            }
         }
      }
   }

   static LHS getLHSStaticField(Class var0, String var1) throws UtilEvalError, ReflectError {
      Field var2 = resolveExpectedJavaField(var0, var1, true);
      return new LHS(var2);
   }

   static LHS getLHSObjectField(Object var0, String var1) throws UtilEvalError, ReflectError {
      if (var0 instanceof This) {
         boolean var4 = false;
         return new LHS(((This)var0).namespace, var1, var4);
      } else {
         try {
            Field var2 = resolveExpectedJavaField(var0.getClass(), var1, false);
            return new LHS(var0, var2);
         } catch (ReflectError var3) {
            if (hasObjectPropertySetter(var0.getClass(), var1)) {
               return new LHS(var0, var1);
            } else {
               throw var3;
            }
         }
      }
   }

   private static Object getFieldValue(Class var0, Object var1, String var2, boolean var3) throws UtilEvalError, ReflectError {
      try {
         Field var4 = resolveExpectedJavaField(var0, var2, var3);
         Object var5 = var4.get(var1);
         Class var6 = var4.getType();
         return Primitive.wrap(var5, var6);
      } catch (NullPointerException var7) {
         throw new ReflectError("???" + var2 + " is not a static field.");
      } catch (IllegalAccessException var8) {
         throw new ReflectError("Can't access field: " + var2);
      }
   }

   protected static Field resolveJavaField(Class var0, String var1, boolean var2) throws UtilEvalError {
      try {
         return resolveExpectedJavaField(var0, var1, var2);
      } catch (ReflectError var4) {
         return null;
      }
   }

   protected static Field resolveExpectedJavaField(Class var0, String var1, boolean var2) throws UtilEvalError, ReflectError {
      Field var3;
      try {
         if (Capabilities.haveAccessibility()) {
            var3 = findAccessibleField(var0, var1);
         } else {
            var3 = var0.getField(var1);
         }
      } catch (NoSuchFieldException var6) {
         throw new ReflectError("No such field: " + var1);
      } catch (SecurityException var7) {
         throw new UtilTargetError("Security Exception while searching fields of: " + var0, var7);
      }

      if (var2 && !Modifier.isStatic(var3.getModifiers())) {
         throw new UtilEvalError("Can't reach instance field: " + var1 + " from static context: " + var0.getName());
      } else {
         return var3;
      }
   }

   private static Field findAccessibleField(Class var0, String var1) throws UtilEvalError, NoSuchFieldException {
      Field var2;
      try {
         var2 = var0.getField(var1);
         ReflectManager.RMSetAccessible(var2);
         return var2;
      } catch (NoSuchFieldException var5) {
         while(var0 != null) {
            try {
               var2 = var0.getDeclaredField(var1);
               ReflectManager.RMSetAccessible(var2);
               return var2;
            } catch (NoSuchFieldException var4) {
               var0 = var0.getSuperclass();
            }
         }

         throw new NoSuchFieldException(var1);
      }
   }

   protected static Method resolveExpectedJavaMethod(BshClassManager var0, Class var1, Object var2, String var3, Object[] var4, boolean var5) throws ReflectError, UtilEvalError {
      if (var2 == Primitive.NULL) {
         throw new UtilTargetError(new NullPointerException("Attempt to invoke method " + var3 + " on null value"));
      } else {
         Class[] var6 = Types.getTypes(var4);
         Method var7 = resolveJavaMethod(var0, var1, var3, var6, var5);
         if (var7 == null) {
            throw new ReflectError((var5 ? "Static method " : "Method ") + StringUtil.methodString(var3, var6) + " not found in class'" + var1.getName() + "'");
         } else {
            return var7;
         }
      }
   }

   protected static Method resolveJavaMethod(BshClassManager var0, Class var1, String var2, Class[] var3, boolean var4) throws UtilEvalError {
      if (var1 == null) {
         throw new InterpreterError("null class");
      } else {
         Method var5 = null;
         if (var0 == null) {
            Interpreter.debug("resolveJavaMethod UNOPTIMIZED lookup");
         } else {
            var5 = var0.getResolvedMethod(var1, var2, var3, var4);
         }

         if (var5 == null) {
            boolean var6 = !Capabilities.haveAccessibility();

            try {
               var5 = findOverloadedMethod(var1, var2, var3, var6);
            } catch (SecurityException var9) {
               throw new UtilTargetError("Security Exception while searching methods of: " + var1, var9);
            }

            checkFoundStaticMethod(var5, var4, var1);
            if (var5 != null && !var6) {
               try {
                  ReflectManager.RMSetAccessible(var5);
               } catch (UtilEvalError var8) {
               }
            }

            if (var5 != null && var0 != null) {
               var0.cacheResolvedMethod(var1, var3, var5);
            }
         }

         return var5;
      }
   }

   private static Method findOverloadedMethod(Class var0, String var1, Class[] var2, boolean var3) {
      if (Interpreter.DEBUG) {
         Interpreter.debug("Searching for method: " + StringUtil.methodString(var1, var2) + " in '" + var0.getName() + "'");
      }

      Method[] var4 = getCandidateMethods(var0, var1, var2.length, var3);
      if (Interpreter.DEBUG) {
         Interpreter.debug("Looking for most specific method: " + var1);
      }

      Method var5 = findMostSpecificMethod(var2, var4);
      return var5;
   }

   static Method[] getCandidateMethods(Class var0, String var1, int var2, boolean var3) {
      Vector var4 = gatherMethodsRecursive(var0, var1, var2, var3, (Vector)null);
      Method[] var5 = new Method[var4.size()];
      var4.copyInto(var5);
      return var5;
   }

   private static Vector gatherMethodsRecursive(Class var0, String var1, int var2, boolean var3, Vector var4) {
      if (var4 == null) {
         var4 = new Vector();
      }

      if (var3) {
         if (isPublic(var0)) {
            addCandidates(var0.getMethods(), var1, var2, var3, var4);
         }
      } else {
         addCandidates(var0.getDeclaredMethods(), var1, var2, var3, var4);
      }

      Class[] var5 = var0.getInterfaces();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         gatherMethodsRecursive(var5[var6], var1, var2, var3, var4);
      }

      Class var7 = var0.getSuperclass();
      if (var7 != null) {
         gatherMethodsRecursive(var7, var1, var2, var3, var4);
      }

      return var4;
   }

   private static Vector addCandidates(Method[] var0, String var1, int var2, boolean var3, Vector var4) {
      for(int var5 = 0; var5 < var0.length; ++var5) {
         Method var6 = var0[var5];
         if (var6.getName().equals(var1) && var6.getParameterTypes().length == var2 && (!var3 || isPublic(var6))) {
            var4.add(var6);
         }
      }

      return var4;
   }

   static Object constructObject(Class var0, Object[] var1) throws ReflectError, InvocationTargetException {
      if (var0.isInterface()) {
         throw new ReflectError("Can't create instance of an interface: " + var0);
      } else {
         Object var2 = null;
         Class[] var3 = Types.getTypes(var1);
         Constructor var4 = null;
         Constructor[] var5 = Capabilities.haveAccessibility() ? var0.getDeclaredConstructors() : var0.getConstructors();
         if (Interpreter.DEBUG) {
            Interpreter.debug("Looking for most specific constructor: " + var0);
         }

         var4 = findMostSpecificConstructor(var3, var5);
         if (var4 == null) {
            throw cantFindConstructor(var0, var3);
         } else {
            if (!isPublic(var4)) {
               try {
                  ReflectManager.RMSetAccessible(var4);
               } catch (UtilEvalError var12) {
               }
            }

            var1 = Primitive.unwrap(var1);

            try {
               var2 = var4.newInstance(var1);
            } catch (InstantiationException var9) {
               throw new ReflectError("The class " + var0 + " is abstract ");
            } catch (IllegalAccessException var10) {
               throw new ReflectError("We don't have permission to create an instance.Use setAccessibility(true) to enable access.");
            } catch (IllegalArgumentException var11) {
               throw new ReflectError("The number of arguments was wrong");
            }

            if (var2 == null) {
               throw new ReflectError("Couldn't construct the object");
            } else {
               return var2;
            }
         }
      }
   }

   static Constructor findMostSpecificConstructor(Class[] var0, Constructor[] var1) {
      int var2 = findMostSpecificConstructorIndex(var0, var1);
      return var2 == -1 ? null : var1[var2];
   }

   static int findMostSpecificConstructorIndex(Class[] var0, Constructor[] var1) {
      Class[][] var2 = new Class[var1.length][];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1[var3].getParameterTypes();
      }

      return findMostSpecificSignature(var0, var2);
   }

   static Method findMostSpecificMethod(Class[] var0, Method[] var1) {
      Class[][] var2 = new Class[var1.length][];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3].getParameterTypes();
      }

      int var4 = findMostSpecificSignature(var0, var2);
      return var4 == -1 ? null : var1[var4];
   }

   static int findMostSpecificSignature(Class[] var0, Class[][] var1) {
      for(int var2 = 1; var2 <= 4; ++var2) {
         Class[] var3 = null;
         int var4 = -1;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            Class[] var6 = var1[var5];
            if (Types.isSignatureAssignable(var0, var6, var2) && (var3 == null || Types.isSignatureAssignable(var6, var3, 1))) {
               var3 = var6;
               var4 = var5;
            }
         }

         if (var3 != null) {
            return var4;
         }
      }

      return -1;
   }

   private static String accessorName(String var0, String var1) {
      return var0 + String.valueOf(Character.toUpperCase(var1.charAt(0))) + var1.substring(1);
   }

   public static boolean hasObjectPropertyGetter(Class var0, String var1) {
      String var2 = accessorName("get", var1);

      try {
         var0.getMethod(var2);
         return true;
      } catch (NoSuchMethodException var5) {
         var2 = accessorName("is", var1);

         try {
            Method var3 = var0.getMethod(var2);
            return var3.getReturnType() == Boolean.TYPE;
         } catch (NoSuchMethodException var4) {
            return false;
         }
      }
   }

   public static boolean hasObjectPropertySetter(Class var0, String var1) {
      String var2 = accessorName("set", var1);
      Method[] var3 = var0.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getName().equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public static Object getObjectProperty(Object var0, String var1) throws UtilEvalError, ReflectError {
      Object[] var2 = new Object[0];
      Interpreter.debug("property access: ");
      Method var3 = null;
      Exception var4 = null;
      Exception var5 = null;

      String var6;
      try {
         var6 = accessorName("get", var1);
         var3 = resolveExpectedJavaMethod((BshClassManager)null, var0.getClass(), var0, var6, var2, false);
      } catch (Exception var9) {
         var4 = var9;
      }

      if (var3 == null) {
         try {
            var6 = accessorName("is", var1);
            var3 = resolveExpectedJavaMethod((BshClassManager)null, var0.getClass(), var0, var6, var2, false);
            if (var3.getReturnType() != Boolean.TYPE) {
               var3 = null;
            }
         } catch (Exception var8) {
            var5 = var8;
         }
      }

      if (var3 == null) {
         throw new ReflectError("Error in property getter: " + var4 + (var5 != null ? " : " + var5 : ""));
      } else {
         try {
            return invokeMethod(var3, var0, var2);
         } catch (InvocationTargetException var7) {
            throw new UtilEvalError("Property accessor threw exception: " + var7.getTargetException());
         }
      }
   }

   public static void setObjectProperty(Object var0, String var1, Object var2) throws ReflectError, UtilEvalError {
      String var3 = accessorName("set", var1);
      Object[] var4 = new Object[]{var2};
      Interpreter.debug("property access: ");

      try {
         Method var5 = resolveExpectedJavaMethod((BshClassManager)null, var0.getClass(), var0, var3, var4, false);
         invokeMethod(var5, var0, var4);
      } catch (InvocationTargetException var6) {
         throw new UtilEvalError("Property accessor threw exception: " + var6.getTargetException());
      }
   }

   public static String normalizeClassName(Class var0) {
      if (!var0.isArray()) {
         return var0.getName();
      } else {
         StringBuffer var1 = new StringBuffer();

         try {
            var1.append(getArrayBaseType(var0).getName() + " ");

            for(int var2 = 0; var2 < getArrayDimensions(var0); ++var2) {
               var1.append("[]");
            }
         } catch (ReflectError var3) {
         }

         return var1.toString();
      }
   }

   public static int getArrayDimensions(Class var0) {
      return !var0.isArray() ? 0 : var0.getName().lastIndexOf(91) + 1;
   }

   public static Class getArrayBaseType(Class var0) throws ReflectError {
      if (!var0.isArray()) {
         throw new ReflectError("The class is not an array.");
      } else {
         return var0.getComponentType();
      }
   }

   public static Object invokeCompiledCommand(Class var0, Object[] var1, Interpreter var2, CallStack var3) throws UtilEvalError {
      Object[] var4 = new Object[var1.length + 2];
      var4[0] = var2;
      var4[1] = var3;
      System.arraycopy(var1, 0, var4, 2, var1.length);
      BshClassManager var5 = var2.getClassManager();

      try {
         return invokeStaticMethod(var5, var0, "invoke", var4);
      } catch (InvocationTargetException var8) {
         throw new UtilEvalError("Error in compiled command: " + var8.getTargetException());
      } catch (ReflectError var9) {
         throw new UtilEvalError("Error invoking compiled command: " + var9);
      }
   }

   private static void logInvokeMethod(String var0, Method var1, Object[] var2) {
      if (Interpreter.DEBUG) {
         Interpreter.debug(var0 + var1 + " with args:");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Interpreter.debug("args[" + var3 + "] = " + var2[var3] + " type = " + var2[var3].getClass());
         }
      }

   }

   private static void checkFoundStaticMethod(Method var0, boolean var1, Class var2) throws UtilEvalError {
      if (var0 != null && var1 && !isStatic(var0)) {
         throw new UtilEvalError("Cannot reach instance method: " + StringUtil.methodString(var0.getName(), var0.getParameterTypes()) + " from static context: " + var2.getName());
      }
   }

   private static ReflectError cantFindConstructor(Class var0, Class[] var1) {
      return var1.length == 0 ? new ReflectError("Can't find default constructor for: " + var0) : new ReflectError("Can't find constructor: " + StringUtil.methodString(var0.getName(), var1) + " in class: " + var0.getName());
   }

   private static boolean isPublic(Class var0) {
      return Modifier.isPublic(var0.getModifiers());
   }

   private static boolean isPublic(Method var0) {
      return Modifier.isPublic(var0.getModifiers());
   }

   private static boolean isPublic(Constructor var0) {
      return Modifier.isPublic(var0.getModifiers());
   }

   private static boolean isStatic(Method var0) {
      return Modifier.isStatic(var0.getModifiers());
   }
}
