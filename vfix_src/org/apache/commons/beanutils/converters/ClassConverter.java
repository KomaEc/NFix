package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class ClassConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$converters$ClassConverter;

   public ClassConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public ClassConverter(Object defaultValue) {
      this.defaultValue = defaultValue;
      this.useDefault = true;
   }

   public Object convert(Class type, Object value) {
      if (value == null) {
         if (this.useDefault) {
            return this.defaultValue;
         } else {
            throw new ConversionException("No value specified");
         }
      } else if (value instanceof Class) {
         return value;
      } else {
         try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
               classLoader = (class$org$apache$commons$beanutils$converters$ClassConverter == null ? (class$org$apache$commons$beanutils$converters$ClassConverter = class$("org.apache.commons.beanutils.converters.ClassConverter")) : class$org$apache$commons$beanutils$converters$ClassConverter).getClassLoader();
            }

            return classLoader.loadClass(value.toString());
         } catch (Exception var4) {
            if (this.useDefault) {
               return this.defaultValue;
            } else {
               throw new ConversionException(var4);
            }
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
