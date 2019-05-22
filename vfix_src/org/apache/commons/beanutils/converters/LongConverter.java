package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class LongConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public LongConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public LongConverter(Object defaultValue) {
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
      } else if (value instanceof Long) {
         return value;
      } else if (value instanceof Number) {
         return new Long(((Number)value).longValue());
      } else {
         try {
            return new Long(value.toString());
         } catch (Exception var4) {
            if (this.useDefault) {
               return this.defaultValue;
            } else {
               throw new ConversionException(var4);
            }
         }
      }
   }
}
