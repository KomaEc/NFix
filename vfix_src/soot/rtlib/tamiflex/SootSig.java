package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SootSig {
   private static final Logger logger = LoggerFactory.getLogger(SootSig.class);
   private static Map<Constructor<?>, String> constrCache = new ConcurrentHashMap();
   private static Map<Method, String> methodCache = new ConcurrentHashMap();

   public static String sootSignature(Constructor<?> c) {
      String res = (String)constrCache.get(c);
      if (res == null) {
         String[] paramTypes = classesToTypeNames(c.getParameterTypes());
         res = sootSignature(c.getDeclaringClass().getName(), "void", "<init>", paramTypes);
         constrCache.put(c, res);
      }

      return res;
   }

   public static String sootSignature(Object receiver, Method m) {
      Class receiverClass = Modifier.isStatic(m.getModifiers()) ? m.getDeclaringClass() : receiver.getClass();

      try {
         Method resolved = null;
         Class c = receiverClass;

         do {
            try {
               resolved = c.getDeclaredMethod(m.getName(), m.getParameterTypes());
            } catch (NoSuchMethodException var7) {
               c = c.getSuperclass();
            }
         } while(resolved == null && c != null);

         if (resolved == null) {
            Error error = new Error("Method not found : " + m + " in class " + receiverClass + " and super classes.");
            logger.error((String)error.getMessage(), (Throwable)error);
         }

         String res = (String)methodCache.get(resolved);
         if (res == null) {
            String[] paramTypes = classesToTypeNames(resolved.getParameterTypes());
            res = sootSignature(resolved.getDeclaringClass().getName(), getTypeName(resolved.getReturnType()), resolved.getName(), paramTypes);
            methodCache.put(resolved, res);
         }

         return res;
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }

   private static String[] classesToTypeNames(Class<?>[] params) {
      String[] paramTypes = new String[params.length];
      int i = 0;
      Class[] var3 = params;
      int var4 = params.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class<?> type = var3[var5];
         paramTypes[i] = getTypeName(type);
         ++i;
      }

      return paramTypes;
   }

   private static String getTypeName(Class<?> type) {
      if (type.isArray()) {
         try {
            Class<?> cl = type;

            int dimensions;
            for(dimensions = 0; cl.isArray(); cl = cl.getComponentType()) {
               ++dimensions;
            }

            StringBuffer sb = new StringBuffer();
            sb.append(cl.getName());

            for(int i = 0; i < dimensions; ++i) {
               sb.append("[]");
            }

            return sb.toString();
         } catch (Throwable var5) {
         }
      }

      return type.getName();
   }

   private static String sootSignature(String declaringClass, String returnType, String name, String... paramTypes) {
      StringBuilder b = new StringBuilder();
      b.append("<");
      b.append(declaringClass);
      b.append(": ");
      b.append(returnType);
      b.append(" ");
      b.append(name);
      b.append("(");
      int i = 0;
      String[] var6 = paramTypes;
      int var7 = paramTypes.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String type = var6[var8];
         ++i;
         b.append(type);
         if (i < paramTypes.length) {
            b.append(",");
         }
      }

      b.append(")>");
      return b.toString();
   }

   public static String sootSignature(Field f) {
      StringBuilder b = new StringBuilder();
      b.append("<");
      b.append(getTypeName(f.getDeclaringClass()));
      b.append(": ");
      b.append(getTypeName(f.getType()));
      b.append(" ");
      b.append(f.getName());
      b.append(">");
      return b.toString();
   }
}
