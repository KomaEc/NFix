package org.jboss.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Classes {
   public static final String PACKAGE_SEPARATOR = ".";
   public static final char PACKAGE_SEPARATOR_CHAR = '.';
   public static final String DEFAULT_PACKAGE_NAME = "<default>";
   private static final Map PRIMITIVE_NAME_TYPE_MAP = new HashMap();
   private static final Class[] PRIMITIVE_WRAPPER_MAP;

   public static void displayClassInfo(Class clazz, StringBuffer results) {
      ClassLoader cl = clazz.getClassLoader();
      results.append("\n");
      results.append(clazz.getName());
      results.append("(");
      results.append(Integer.toHexString(clazz.hashCode()));
      results.append(").ClassLoader=");
      results.append(cl);
      ClassLoader parent = cl;

      int i;
      while(parent != null) {
         results.append("\n..");
         results.append(parent);
         URL[] urls = getClassLoaderURLs(parent);
         int length = urls != null ? urls.length : 0;

         for(i = 0; i < length; ++i) {
            results.append("\n....");
            results.append(urls[i]);
         }

         if (parent != null) {
            parent = parent.getParent();
         }
      }

      CodeSource clazzCS = clazz.getProtectionDomain().getCodeSource();
      if (clazzCS != null) {
         results.append("\n++++CodeSource: ");
         results.append(clazzCS);
      } else {
         results.append("\n++++Null CodeSource");
      }

      results.append("\nImplemented Interfaces:");
      Class[] ifaces = clazz.getInterfaces();

      for(i = 0; i < ifaces.length; ++i) {
         Class iface = ifaces[i];
         results.append("\n++");
         results.append(iface);
         results.append("(");
         results.append(Integer.toHexString(iface.hashCode()));
         results.append(")");
         ClassLoader loader = ifaces[i].getClassLoader();
         results.append("\n++++ClassLoader: ");
         results.append(loader);
         ProtectionDomain pd = ifaces[i].getProtectionDomain();
         CodeSource cs = pd.getCodeSource();
         if (cs != null) {
            results.append("\n++++CodeSource: ");
            results.append(cs);
         } else {
            results.append("\n++++Null CodeSource");
         }
      }

   }

   public static URL[] getClassLoaderURLs(ClassLoader cl) {
      URL[] urls = new URL[0];

      try {
         Class returnType = urls.getClass();
         Class[] parameterTypes = new Class[0];
         Class clClass = cl.getClass();
         Method getURLs = clClass.getMethod("getURLs", parameterTypes);
         if (returnType.isAssignableFrom(getURLs.getReturnType())) {
            Object[] args = new Object[0];
            urls = (URL[])((URL[])getURLs.invoke(cl, args));
         }

         if (urls == null || urls.length == 0) {
            Method getCp = clClass.getMethod("getClasspath", parameterTypes);
            if (returnType.isAssignableFrom(getCp.getReturnType())) {
               Object[] args = new Object[0];
               urls = (URL[])((URL[])getCp.invoke(cl, args));
            }
         }
      } catch (Exception var8) {
      }

      return urls;
   }

   public static String getDescription(Object object) {
      StringBuffer buffer = new StringBuffer();
      describe(buffer, object);
      return buffer.toString();
   }

   public static void describe(StringBuffer buffer, Object object) {
      if (object == null) {
         buffer.append("**null**");
      } else {
         describe(buffer, object.getClass());
      }

   }

   public static void describe(StringBuffer buffer, Class clazz) {
      if (clazz == null) {
         buffer.append("**null**");
      } else {
         buffer.append("{class=").append(clazz.getName());
         Class[] intfs = clazz.getInterfaces();
         if (intfs.length > 0) {
            buffer.append(" intfs=");

            for(int i = 0; i < intfs.length; ++i) {
               buffer.append(intfs[i].getName());
               if (i < intfs.length - 1) {
                  buffer.append(", ");
               }
            }
         }

         buffer.append("}");
      }

   }

   public static String stripPackageName(String classname) {
      int idx = classname.lastIndexOf(".");
      return idx != -1 ? classname.substring(idx + 1, classname.length()) : classname;
   }

   public static String stripPackageName(Class type) {
      return stripPackageName(type.getName());
   }

   public static String getPackageName(String classname) {
      if (classname.length() == 0) {
         throw new EmptyStringException();
      } else {
         int index = classname.lastIndexOf(".");
         return index != -1 ? classname.substring(0, index) : "";
      }
   }

   public static String getPackageName(Class type) {
      return getPackageName(type.getName());
   }

   public static void forceLoad(Class type) {
      if (type == null) {
         throw new NullArgumentException("type");
      } else if (!type.isPrimitive()) {
         String packageName = getPackageName(type);
         if (!packageName.startsWith("java.") && !packageName.startsWith("javax.")) {
            try {
               Method[] methods = type.getDeclaredMethods();
               Method method = null;

               for(int i = 0; i < methods.length; ++i) {
                  int modifiers = methods[i].getModifiers();
                  if (Modifier.isStatic(modifiers)) {
                     method = methods[i];
                     break;
                  }
               }

               if (method != null) {
                  method.invoke((Object)null, (Object[])null);
               } else {
                  type.newInstance();
               }
            } catch (Exception var6) {
               ThrowableHandler.add(var6);
            }

         }
      }
   }

   public static Class getPrimitiveTypeForName(String name) {
      return (Class)PRIMITIVE_NAME_TYPE_MAP.get(name);
   }

   public static Class getPrimitiveWrapper(Class type) {
      if (!type.isPrimitive()) {
         throw new IllegalArgumentException("type is not a primitive class");
      } else {
         for(int i = 0; i < PRIMITIVE_WRAPPER_MAP.length; i += 2) {
            if (type.equals(PRIMITIVE_WRAPPER_MAP[i])) {
               return PRIMITIVE_WRAPPER_MAP[i + 1];
            }
         }

         throw new UnreachableStatementException();
      }
   }

   public static void getAllInterfaces(List allIfaces, Class c) {
      while(c != null) {
         Class[] ifaces = c.getInterfaces();

         for(int n = 0; n < ifaces.length; ++n) {
            allIfaces.add(ifaces[n]);
         }

         c = c.getSuperclass();
      }

   }

   public static Class[] getAllUniqueInterfaces(Class c) {
      HashSet uniqueIfaces;
      for(uniqueIfaces = new HashSet(); c != null; c = c.getSuperclass()) {
         Class[] ifaces = c.getInterfaces();

         for(int n = 0; n < ifaces.length; ++n) {
            uniqueIfaces.add(ifaces[n]);
         }
      }

      return (Class[])((Class[])uniqueIfaces.toArray(new Class[uniqueIfaces.size()]));
   }

   public static boolean isPrimitiveWrapper(Class type) {
      for(int i = 0; i < PRIMITIVE_WRAPPER_MAP.length; i += 2) {
         if (type.equals(PRIMITIVE_WRAPPER_MAP[i + 1])) {
            return true;
         }
      }

      return false;
   }

   public static boolean isPrimitive(Class type) {
      return type.isPrimitive() || isPrimitiveWrapper(type);
   }

   public static boolean isPrimitive(String type) {
      return PRIMITIVE_NAME_TYPE_MAP.containsKey(type);
   }

   public static Class getPrimitive(Class wrapper) {
      Class primitive;
      if (Integer.class == wrapper) {
         primitive = Integer.TYPE;
      } else if (Long.class == wrapper) {
         primitive = Long.TYPE;
      } else if (Double.class == wrapper) {
         primitive = Double.TYPE;
      } else if (Boolean.class == wrapper) {
         primitive = Boolean.TYPE;
      } else if (Short.class == wrapper) {
         primitive = Short.TYPE;
      } else if (Float.class == wrapper) {
         primitive = Float.TYPE;
      } else if (Byte.class == wrapper) {
         primitive = Byte.TYPE;
      } else {
         if (Character.class != wrapper) {
            throw new IllegalArgumentException("The class is not a primitive wrapper type: " + wrapper);
         }

         primitive = Character.TYPE;
      }

      return primitive;
   }

   public static Object instantiate(Class expected, String property, String defaultClassName) {
      String className = getProperty(property, defaultClassName);
      Class clazz = null;

      try {
         clazz = loadClass(className);
      } catch (ClassNotFoundException var9) {
         throw new NestedRuntimeException("Cannot load class " + className, var9);
      }

      Object result = null;

      try {
         result = clazz.newInstance();
      } catch (InstantiationException var7) {
         throw new NestedRuntimeException("Error instantiating " + className, var7);
      } catch (IllegalAccessException var8) {
         throw new NestedRuntimeException("Error instantiating " + className, var8);
      }

      if (!expected.isAssignableFrom(clazz)) {
         throw new NestedRuntimeException("Class " + className + " from classloader " + clazz.getClassLoader() + " is not of the expected class " + expected + " loaded from " + expected.getClassLoader());
      } else {
         return result;
      }
   }

   public static Class loadClass(String className) throws ClassNotFoundException {
      return loadClass(className, Thread.currentThread().getContextClassLoader());
   }

   public static Class loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
      if (className.length() == 1) {
         char type = className.charAt(0);
         if (type == 'B') {
            return Byte.TYPE;
         } else if (type == 'C') {
            return Character.TYPE;
         } else if (type == 'D') {
            return Double.TYPE;
         } else if (type == 'F') {
            return Float.TYPE;
         } else if (type == 'I') {
            return Integer.TYPE;
         } else if (type == 'J') {
            return Long.TYPE;
         } else if (type == 'S') {
            return Short.TYPE;
         } else if (type == 'Z') {
            return Boolean.TYPE;
         } else if (type == 'V') {
            return Void.TYPE;
         } else {
            throw new ClassNotFoundException(className);
         }
      } else if (isPrimitive(className)) {
         return (Class)PRIMITIVE_NAME_TYPE_MAP.get(className);
      } else if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
         return classLoader.loadClass(className.substring(1, className.length() - 1));
      } else {
         try {
            return classLoader.loadClass(className);
         } catch (ClassNotFoundException var4) {
            if (className.charAt(0) != '[') {
               throw var4;
            } else {
               int arrayDimension;
               for(arrayDimension = 0; className.charAt(arrayDimension) == '['; ++arrayDimension) {
               }

               Class componentType = loadClass(className.substring(arrayDimension), classLoader);
               return Array.newInstance(componentType, new int[arrayDimension]).getClass();
            }
         }
      }
   }

   public static final Class<?>[] convertToJavaClasses(Iterator<String> it, ClassLoader cl) throws ClassNotFoundException {
      ArrayList classes = new ArrayList();

      while(it.hasNext()) {
         classes.add(convertToJavaClass((String)it.next(), cl));
      }

      return (Class[])classes.toArray(new Class[classes.size()]);
   }

   public static final Method getAttributeGetter(Class cls, String attr) throws java.lang.NoSuchMethodException {
      StringBuffer buf = new StringBuffer(attr.length() + 3);
      buf.append("get");
      if (Character.isLowerCase(attr.charAt(0))) {
         buf.append(Character.toUpperCase(attr.charAt(0))).append(attr.substring(1));
      } else {
         buf.append(attr);
      }

      try {
         return cls.getMethod(buf.toString(), (Class[])null);
      } catch (java.lang.NoSuchMethodException var4) {
         buf.replace(0, 3, "is");
         return cls.getMethod(buf.toString(), (Class[])null);
      }
   }

   public static final Method getAttributeSetter(Class cls, String attr, Class type) throws java.lang.NoSuchMethodException {
      StringBuffer buf = new StringBuffer(attr.length() + 3);
      buf.append("set");
      if (Character.isLowerCase(attr.charAt(0))) {
         buf.append(Character.toUpperCase(attr.charAt(0))).append(attr.substring(1));
      } else {
         buf.append(attr);
      }

      return cls.getMethod(buf.toString(), type);
   }

   private static final Class convertToJavaClass(String name, ClassLoader cl) throws ClassNotFoundException {
      int arraySize;
      for(arraySize = 0; name.endsWith("[]"); ++arraySize) {
         name = name.substring(0, name.length() - 2);
      }

      Class c = (Class)PRIMITIVE_NAME_TYPE_MAP.get(name);
      if (c == null) {
         try {
            c = cl.loadClass(name);
         } catch (ClassNotFoundException var6) {
            throw new ClassNotFoundException("Parameter class not found: " + name);
         }
      }

      if (arraySize > 0) {
         int[] dims = new int[arraySize];

         for(int i = 0; i < arraySize; ++i) {
            dims[i] = 1;
         }

         c = Array.newInstance(c, dims).getClass();
      }

      return c;
   }

   private static String getProperty(final String name, final String defaultValue) {
      return (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return System.getProperty(name, defaultValue);
         }
      });
   }

   static {
      PRIMITIVE_NAME_TYPE_MAP.put("boolean", Boolean.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("byte", Byte.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("char", Character.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("short", Short.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("int", Integer.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("long", Long.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("float", Float.TYPE);
      PRIMITIVE_NAME_TYPE_MAP.put("double", Double.TYPE);
      PRIMITIVE_WRAPPER_MAP = new Class[]{Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class, Character.TYPE, Character.class, Double.TYPE, Double.class, Float.TYPE, Float.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class};
   }
}
