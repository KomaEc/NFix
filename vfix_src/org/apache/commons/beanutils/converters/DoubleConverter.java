package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class DoubleConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public DoubleConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public DoubleConverter(Object defaultValue) {
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
      } else if (value instanceof Double) {
         return value;
      } else if (value instanceof Number) {
         return new Double(((Number)value).doubleValue());
      } else {
         try {
            return new Double(value.toString());
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
