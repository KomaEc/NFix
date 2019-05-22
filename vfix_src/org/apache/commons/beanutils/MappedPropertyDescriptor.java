package org.apache.commons.beanutils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

public class MappedPropertyDescriptor extends PropertyDescriptor {
   private Class mappedPropertyType;
   private Method mappedReadMethod;
   private Method mappedWriteMethod;
   private static final Class[] stringClassArray;
   private static Hashtable declaredMethodCache;
   // $FF: synthetic field
   static Class class$java$lang$String;

   public MappedPropertyDescriptor(String propertyName, Class beanClass) throws IntrospectionException {
      super(propertyName, (Method)null, (Method)null);
      if (propertyName != null && propertyName.length() != 0) {
         this.setName(propertyName);
         String base = capitalizePropertyName(propertyName);

         try {
            this.mappedReadMethod = findMethod(beanClass, "get" + base, 1, stringClassArray);
            Class[] params = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, this.mappedReadMethod.getReturnType()};
            this.mappedWriteMethod = findMethod(beanClass, "set" + base, 2, params);
         } catch (IntrospectionException var5) {
         }

         if (this.mappedReadMethod == null) {
            this.mappedWriteMethod = findMethod(beanClass, "set" + base, 2);
         }

         if (this.mappedReadMethod == null && this.mappedWriteMethod == null) {
            throw new IntrospectionException("Property '" + propertyName + "' not found on " + beanClass.getName());
         } else {
            this.findMappedPropertyType();
         }
      } else {
         throw new IntrospectionException("bad property name: " + propertyName + " on class: " + beanClass.getClass().getName());
      }
   }

   public MappedPropertyDescriptor(String propertyName, Class beanClass, String mappedGetterName, String mappedSetterName) throws IntrospectionException {
      super(propertyName, (Method)null, (Method)null);
      if (propertyName != null && propertyName.length() != 0) {
         this.setName(propertyName);
         this.mappedReadMethod = findMethod(beanClass, mappedGetterName, 1, stringClassArray);
         if (this.mappedReadMethod != null) {
            Class[] params = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, this.mappedReadMethod.getReturnType()};
            this.mappedWriteMethod = findMethod(beanClass, mappedSetterName, 2, params);
         } else {
            this.mappedWriteMethod = findMethod(beanClass, mappedSetterName, 2);
         }

         this.findMappedPropertyType();
      } else {
         throw new IntrospectionException("bad property name: " + propertyName);
      }
   }

   public MappedPropertyDescriptor(String propertyName, Method mappedGetter, Method mappedSetter) throws IntrospectionException {
      super(propertyName, mappedGetter, mappedSetter);
      if (propertyName != null && propertyName.length() != 0) {
         this.setName(propertyName);
         this.mappedReadMethod = mappedGetter;
         this.mappedWriteMethod = mappedSetter;
         this.findMappedPropertyType();
      } else {
         throw new IntrospectionException("bad property name: " + propertyName);
      }
   }

   public Class getMappedPropertyType() {
      return this.mappedPropertyType;
   }

   public Method getMappedReadMethod() {
      return this.mappedReadMethod;
   }

   public void setMappedReadMethod(Method mappedGetter) throws IntrospectionException {
      this.mappedReadMethod = mappedGetter;
      this.findMappedPropertyType();
   }

   public Method getMappedWriteMethod() {
      return this.mappedWriteMethod;
   }

   public void setMappedWriteMethod(Method mappedSetter) throws IntrospectionException {
      this.mappedWriteMethod = mappedSetter;
      this.findMappedPropertyType();
   }

   private void findMappedPropertyType() throws IntrospectionException {
      try {
         this.mappedPropertyType = null;
         if (this.mappedReadMethod != null) {
            if (this.mappedReadMethod.getParameterTypes().length != 1) {
               throw new IntrospectionException("bad mapped read method arg count");
            }

            this.mappedPropertyType = this.mappedReadMethod.getReturnType();
            if (this.mappedPropertyType == Void.TYPE) {
               throw new IntrospectionException("mapped read method " + this.mappedReadMethod.getName() + " returns void");
            }
         }

         if (this.mappedWriteMethod != null) {
            Class[] params = this.mappedWriteMethod.getParameterTypes();
            if (params.length != 2) {
               throw new IntrospectionException("bad mapped write method arg count");
            }

            if (this.mappedPropertyType != null && this.mappedPropertyType != params[1]) {
               throw new IntrospectionException("type mismatch between mapped read and write methods");
            }

            this.mappedPropertyType = params[1];
         }

      } catch (IntrospectionException var2) {
         throw var2;
      }
   }

   private static String capitalizePropertyName(String s) {
      if (s.length() == 0) {
         return s;
      } else {
         char[] chars = s.toCharArray();
         chars[0] = Character.toUpperCase(chars[0]);
         return new String(chars);
      }
   }

   private static synchronized Method[] getPublicDeclaredMethods(final Class clz) {
      Method[] result = (Method[])declaredMethodCache.get(clz);
      if (result != null) {
         return result;
      } else {
         result = (Method[])AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               try {
                  return clz.getDeclaredMethods();
               } catch (SecurityException var6) {
                  Method[] methods = clz.getMethods();
                  int i = 0;

                  for(int size = methods.length; i < size; ++i) {
                     Method method = methods[i];
                     if (!clz.equals(method.getDeclaringClass())) {
                        methods[i] = null;
                     }
                  }

                  return methods;
               }
            }
         });

         for(int i = 0; i < result.length; ++i) {
            Method method = result[i];
            if (method != null) {
               int mods = method.getModifiers();
               if (!Modifier.isPublic(mods)) {
                  result[i] = null;
               }
            }
         }

         declaredMethodCache.put(clz, result);
         return result;
      }
   }

   private static Method internalFindMethod(Class start, String methodName, int argCount) {
      int i;
      Method method;
      for(Class cl = start; cl != null; cl = cl.getSuperclass()) {
         Method[] methods = getPublicDeclaredMethods(cl);

         for(i = 0; i < methods.length; ++i) {
            method = methods[i];
            if (method != null) {
               int mods = method.getModifiers();
               if (!Modifier.isStatic(mods) && method.getName().equals(methodName) && method.getParameterTypes().length == argCount) {
                  return method;
               }
            }
         }
      }

      Class[] ifcs = start.getInterfaces();

      for(i = 0; i < ifcs.length; ++i) {
         method = internalFindMethod(ifcs[i], methodName, argCount);
         if (method != null) {
            return method;
         }
      }

      return null;
   }

   private static Method internalFindMethod(Class start, String methodName, int argCount, Class[] args) {
      int i;
      Method method;
      for(Class cl = start; cl != null; cl = cl.getSuperclass()) {
         Method[] methods = getPublicDeclaredMethods(cl);

         for(i = 0; i < methods.length; ++i) {
            method = methods[i];
            if (method != null) {
               int mods = method.getModifiers();
               if (!Modifier.isStatic(mods)) {
                  Class[] params = method.getParameterTypes();
                  if (method.getName().equals(methodName) && params.length == argCount) {
                     boolean different = false;
                     if (argCount <= 0) {
                        return method;
                     }

                     for(int j = 0; j < argCount; ++j) {
                        if (params[j] != args[j]) {
                           different = true;
                        }
                     }

                     if (!different) {
                        return method;
                     }
                  }
               }
            }
         }
      }

      Class[] ifcs = start.getInterfaces();

      for(i = 0; i < ifcs.length; ++i) {
         method = internalFindMethod(ifcs[i], methodName, argCount);
         if (method != null) {
            return method;
         }
      }

      return null;
   }

   static Method findMethod(Class cls, String methodName, int argCount) throws IntrospectionException {
      if (methodName == null) {
         return null;
      } else {
         Method m = internalFindMethod(cls, methodName, argCount);
         if (m != null) {
            return m;
         } else {
            throw new IntrospectionException("No method \"" + methodName + "\" with " + argCount + " arg(s)");
         }
      }
   }

   static Method findMethod(Class cls, String methodName, int argCount, Class[] args) throws IntrospectionException {
      if (methodName == null) {
         return null;
      } else {
         Method m = internalFindMethod(cls, methodName, argCount, args);
         if (m != null) {
            return m;
         } else {
            throw new IntrospectionException("No method \"" + methodName + "\" with " + argCount + " arg(s) of matching types.");
         }
      }
   }

   static boolean isSubclass(Class a, Class b) {
      if (a == b) {
         return true;
      } else if (a != null && b != null) {
         for(Class x = a; x != null; x = x.getSuperclass()) {
            if (x == b) {
               return true;
            }

            if (b.isInterface()) {
               Class[] interfaces = x.getInterfaces();

               for(int i = 0; i < interfaces.length; ++i) {
                  if (isSubclass(interfaces[i], b)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean throwsException(Method method, Class exception) {
      Class[] exs = method.getExceptionTypes();

      for(int i = 0; i < exs.length; ++i) {
         if (exs[i] == exception) {
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      stringClassArray = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
      declaredMethodCache = new Hashtable();
   }
}
