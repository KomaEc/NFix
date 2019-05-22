package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class BooleanConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public BooleanConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public BooleanConverter(Object defaultValue) {
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
      } else if (value instanceof Boolean) {
         return value;
      } else {
         try {
            String stringValue = value.toString();
            if (!stringValue.equalsIgnoreCase("yes") && !stringValue.equalsIgnoreCase("y") && !stringValue.equalsIgnoreCase("true") && !stringValue.equalsIgnoreCase("on") && !stringValue.equalsIgnoreCase("1")) {
               if (!stringValue.equalsIgnoreCase("no") && !stringValue.equalsIgnoreCase("n") && !stringValue.equalsIgnoreCase("false") && !stringValue.equalsIgnoreCase("off") && !stringValue.equalsIgnoreCase("0")) {
                  if (this.useDefault) {
                     return this.defaultValue;
                  } else {
                     throw new ConversionException(stringValue);
                  }
               } else {
                  return Boolean.FALSE;
               }
            } else {
               return Boolean.TRUE;
            }
         } catch (ClassCastException var4) {
            if (this.useDefault) {
               return this.defaultValue;
            } else {
               throw new ConversionException(var4);
            }
         }
      }
   }
}
