package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SerializationMembers implements Caching {
   private static final Method NO_METHOD = (new Object() {
      private void noMethod() {
      }
   }).getClass().getDeclaredMethods()[0];
   private static final Object[] EMPTY_ARGS = new Object[0];
   private static final Class[] EMPTY_CLASSES = new Class[0];
   private static final Map NO_FIELDS;
   private static final int PERSISTENT_FIELDS_MODIFIER = 26;
   private static final FastField[] OBJECT_TYPE_FIELDS;
   private Map declaredCache = Collections.synchronizedMap(new HashMap());
   private Map resRepCache = Collections.synchronizedMap(new HashMap());
   private final Map fieldCache = Collections.synchronizedMap(new HashMap());

   public SerializationMembers() {
      int i;
      for(i = 0; i < OBJECT_TYPE_FIELDS.length; ++i) {
         this.declaredCache.put(OBJECT_TYPE_FIELDS[i], NO_METHOD);
      }

      for(i = 0; i < 2; ++i) {
         this.resRepCache.put(OBJECT_TYPE_FIELDS[i], NO_METHOD);
      }

   }

   public Object callReadResolve(Object result) {
      if (result == null) {
         return null;
      } else {
         Class resultType = result.getClass();
         Method readResolveMethod = this.getRRMethod(resultType, "readResolve");
         if (readResolveMethod != null) {
            try {
               return readResolveMethod.invoke(result, EMPTY_ARGS);
            } catch (IllegalAccessException var5) {
               throw new ObjectAccessException("Could not call " + resultType.getName() + ".readResolve()", var5);
            } catch (InvocationTargetException var6) {
               throw new ObjectAccessException("Could not call " + resultType.getName() + ".readResolve()", var6.getTargetException());
            }
         } else {
            return result;
         }
      }
   }

   public Object callWriteReplace(Object object) {
      if (object == null) {
         return null;
      } else {
         Class objectType = object.getClass();
         Method writeReplaceMethod = this.getRRMethod(objectType, "writeReplace");
         if (writeReplaceMethod != null) {
            try {
               return writeReplaceMethod.invoke(object, EMPTY_ARGS);
            } catch (IllegalAccessException var5) {
               throw new ObjectAccessException("Could not call " + objectType.getName() + ".writeReplace()", var5);
            } catch (InvocationTargetException var6) {
               throw new ObjectAccessException("Could not call " + objectType.getName() + ".writeReplace()", var6.getTargetException());
            }
         } else {
            return object;
         }
      }
   }

   public boolean supportsReadObject(Class type, boolean includeBaseClasses) {
      return this.getMethod(type, "readObject", new Class[]{ObjectInputStream.class}, includeBaseClasses) != null;
   }

   public void callReadObject(Class type, Object object, ObjectInputStream stream) {
      try {
         Method readObjectMethod = this.getMethod(type, "readObject", new Class[]{ObjectInputStream.class}, false);
         readObjectMethod.invoke(object, stream);
      } catch (IllegalAccessException var5) {
         throw new ConversionException("Could not call " + object.getClass().getName() + ".readObject()", var5);
      } catch (InvocationTargetException var6) {
         throw new ConversionException("Could not call " + object.getClass().getName() + ".readObject()", var6.getTargetException());
      }
   }

   public boolean supportsWriteObject(Class type, boolean includeBaseClasses) {
      return this.getMethod(type, "writeObject", new Class[]{ObjectOutputStream.class}, includeBaseClasses) != null;
   }

   public void callWriteObject(Class type, Object instance, ObjectOutputStream stream) {
      try {
         Method readObjectMethod = this.getMethod(type, "writeObject", new Class[]{ObjectOutputStream.class}, false);
         readObjectMethod.invoke(instance, stream);
      } catch (IllegalAccessException var5) {
         throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", var5);
      } catch (InvocationTargetException var6) {
         throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", var6.getTargetException());
      }
   }

   private Method getMethod(Class type, String name, Class[] parameterTypes, boolean includeBaseclasses) {
      Method method = this.getMethod(type, name, parameterTypes);
      return method != NO_METHOD && (includeBaseclasses || method.getDeclaringClass().equals(type)) ? method : null;
   }

   private Method getMethod(Class type, String name, Class[] parameterTypes) {
      if (type == null) {
         return null;
      } else {
         FastField method = new FastField(type, name);
         Method result = (Method)this.declaredCache.get(method);
         if (result == null) {
            try {
               result = type.getDeclaredMethod(name, parameterTypes);
               if (!result.isAccessible()) {
                  result.setAccessible(true);
               }
            } catch (NoSuchMethodException var7) {
               result = this.getMethod(type.getSuperclass(), name, parameterTypes);
            }

            this.declaredCache.put(method, result);
         }

         return result;
      }
   }

   private Method getRRMethod(Class type, String name) {
      FastField method = new FastField(type, name);
      Method result = (Method)this.resRepCache.get(method);
      if (result == null) {
         result = this.getMethod(type, name, EMPTY_CLASSES, true);
         if (result != null && result.getDeclaringClass() != type) {
            if ((result.getModifiers() & 5) == 0 && ((result.getModifiers() & 2) > 0 || type.getPackage() != result.getDeclaringClass().getPackage())) {
               result = NO_METHOD;
            }
         } else if (result == null) {
            result = NO_METHOD;
         }

         this.resRepCache.put(method, result);
      }

      return result == NO_METHOD ? null : result;
   }

   public Map getSerializablePersistentFields(Class type) {
      if (type == null) {
         return null;
      } else {
         Map result = (Map)this.fieldCache.get(type.getName());
         if (result == null) {
            try {
               Field field = type.getDeclaredField("serialPersistentFields");
               if ((field.getModifiers() & 26) == 26) {
                  field.setAccessible(true);
                  ObjectStreamField[] fields = (ObjectStreamField[])((ObjectStreamField[])field.get((Object)null));
                  if (fields != null) {
                     result = new HashMap();

                     for(int i = 0; i < fields.length; ++i) {
                        ((Map)result).put(fields[i].getName(), fields[i]);
                     }
                  }
               }
            } catch (NoSuchFieldException var6) {
            } catch (IllegalAccessException var7) {
               throw new ObjectAccessException("Cannot get " + type.getName() + ".serialPersistentFields.", var7);
            } catch (ClassCastException var8) {
               throw new ObjectAccessException("Cannot get " + type.getName() + ".serialPersistentFields.", var8);
            }

            if (result == null) {
               result = NO_FIELDS;
            }

            this.fieldCache.put(type.getName(), result);
         }

         return (Map)(result == NO_FIELDS ? null : result);
      }
   }

   public void flushCache() {
      this.declaredCache.keySet().retainAll(Arrays.asList(OBJECT_TYPE_FIELDS));
      this.resRepCache.keySet().retainAll(Arrays.asList(OBJECT_TYPE_FIELDS));
   }

   static {
      NO_FIELDS = Collections.EMPTY_MAP;
      OBJECT_TYPE_FIELDS = new FastField[]{new FastField(Object.class, "readResolve"), new FastField(Object.class, "writeReplace"), new FastField(Object.class, "readObject"), new FastField(Object.class, "writeObject")};
   }
}
