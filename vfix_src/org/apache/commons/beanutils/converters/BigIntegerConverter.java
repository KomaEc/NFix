package org.apache.commons.beanutils.converters;

import java.math.BigInteger;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class BigIntegerConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public BigIntegerConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public BigIntegerConverter(Object defaultValue) {
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
      } else if (value instanceof BigInteger) {
         return value;
      } else {
         try {
            return new BigInteger(value.toString());
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
