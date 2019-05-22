package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Cloneables {
   public static Object clone(Object o) {
      if (!(o instanceof Cloneable)) {
         return null;
      } else if (!o.getClass().isArray()) {
         try {
            Method clone = o.getClass().getMethod("clone", (Class[])null);
            return clone.invoke(o, (Object[])null);
         } catch (NoSuchMethodException var4) {
            throw new ObjectAccessException("Cloneable type has no clone method", var4);
         } catch (IllegalAccessException var5) {
            throw new ObjectAccessException("Cannot clone Cloneable type", var5);
         } catch (InvocationTargetException var6) {
            throw new ObjectAccessException("Exception cloning Cloneable type", var6.getCause());
         }
      } else {
         Class componentType = o.getClass().getComponentType();
         if (!componentType.isPrimitive()) {
            return ((Object[])((Object[])o)).clone();
         } else {
            int length = Array.getLength(o);
            Object clone = Array.newInstance(componentType, length);

            while(length-- > 0) {
               Array.set(clone, length, Array.get(o, length));
            }

            return clone;
         }
      }
   }

   public static Object cloneIfPossible(Object o) {
      Object clone = clone(o);
      return clone == null ? o : clone;
   }
}
