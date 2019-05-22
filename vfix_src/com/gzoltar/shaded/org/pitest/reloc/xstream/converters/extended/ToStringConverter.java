package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ToStringConverter extends AbstractSingleValueConverter {
   private final Class clazz;
   private final Constructor ctor;

   public ToStringConverter(Class clazz) throws NoSuchMethodException {
      this.clazz = clazz;
      this.ctor = clazz.getConstructor(String.class);
   }

   public boolean canConvert(Class type) {
      return type.equals(this.clazz);
   }

   public String toString(Object obj) {
      return obj == null ? null : obj.toString();
   }

   public Object fromString(String str) {
      try {
         return this.ctor.newInstance(str);
      } catch (InstantiationException var3) {
         throw new ConversionException("Unable to instantiate single String param constructor", var3);
      } catch (IllegalAccessException var4) {
         throw new ConversionException("Unable to access single String param constructor", var4);
      } catch (InvocationTargetException var5) {
         throw new ConversionException("Unable to target single String param constructor", var5.getTargetException());
      }
   }
}
