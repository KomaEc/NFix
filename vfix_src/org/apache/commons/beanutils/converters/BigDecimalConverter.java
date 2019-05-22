package org.apache.commons.beanutils.converters;

import java.math.BigDecimal;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class BigDecimalConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public BigDecimalConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public BigDecimalConverter(Object defaultValue) {
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
      } else if (value instanceof BigDecimal) {
         return value;
      } else {
         try {
            return new BigDecimal(value.toString());
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
