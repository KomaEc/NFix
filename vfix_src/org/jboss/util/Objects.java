package org.jboss.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import org.jboss.util.stream.Streams;

public final class Objects {
   public static Constructor getCompatibleConstructor(Class type, Class valueType) {
      try {
         return type.getConstructor(valueType);
      } catch (Exception var7) {
         Class[] types = type.getClasses();
         int i = 0;

         while(i < types.length) {
            try {
               return type.getConstructor(types[i]);
            } catch (Exception var6) {
               ++i;
            }
         }

         return null;
      }
   }

   public static Object copy(Serializable obj) throws IOException, ClassNotFoundException {
      ObjectOutputStream out = null;
      ObjectInputStream in = null;
      Object copy = null;

      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         out = new ObjectOutputStream(baos);
         out.writeObject(obj);
         out.flush();
         byte[] data = baos.toByteArray();
         ByteArrayInputStream bais = new ByteArrayInputStream(data);
         in = new ObjectInputStream(bais);
         copy = in.readObject();
      } finally {
         Streams.close((OutputStream)out);
         Streams.close((InputStream)in);
      }

      return copy;
   }

   public static Object deref(Object obj) {
      if (obj != null && obj instanceof Reference) {
         Reference ref = (Reference)obj;
         return ref.get();
      } else {
         return obj;
      }
   }

   public static <T> T deref(Object obj, Class<T> expected) {
      Object result = deref(obj);
      return result == null ? null : expected.cast(result);
   }

   public static boolean isArray(Object obj) {
      return obj != null ? obj.getClass().isArray() : false;
   }

   public static Object[] toArray(Object obj) {
      if (obj instanceof Object[]) {
         return (Object[])((Object[])obj);
      } else {
         Class type = obj.getClass();
         Object array;
         if (type.isArray()) {
            int length = Array.getLength(obj);
            Class componentType = type.getComponentType();
            array = Array.newInstance(componentType, length);

            for(int i = 0; i < length; ++i) {
               Array.set(array, i, Array.get(obj, i));
            }
         } else {
            array = Array.newInstance(type, 1);
            Array.set(array, 0, obj);
         }

         return (Object[])((Object[])array);
      }
   }

   public static boolean equals(Object[] a, Object[] b, boolean deep) {
      if (a == b) {
         return true;
      } else if (a != null && b != null) {
         if (a.length != b.length) {
            return false;
         } else {
            for(int i = 0; i < a.length; ++i) {
               Object x = a[i];
               Object y = b[i];
               if (x != y) {
                  return false;
               }

               if (x == null || y == null) {
                  return false;
               }

               if (deep) {
                  if (!(x instanceof Object[]) || !(y instanceof Object[])) {
                     return false;
                  }

                  if (!equals((Object[])((Object[])x), (Object[])((Object[])y), true)) {
                     return false;
                  }
               }

               if (!x.equals(y)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean equals(Object[] a, Object[] b) {
      return equals(a, b, true);
   }

   public static boolean equals(Object first, Object second) {
      if (isArray(first)) {
         if (!isArray(second)) {
            return false;
         } else {
            int lenght = Array.getLength(first);
            if (lenght != Array.getLength(second)) {
               return false;
            } else {
               for(int i = 0; i < lenght; ++i) {
                  if (!equals(Array.get(first, i), Array.get(second, i))) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return JBossObject.equals(first, second);
      }
   }
}
