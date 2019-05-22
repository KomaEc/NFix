package org.apache.commons.beanutils.converters;

import java.sql.Timestamp;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class SqlTimestampConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public SqlTimestampConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public SqlTimestampConverter(Object defaultValue) {
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
      } else if (value instanceof Timestamp) {
         return value;
      } else {
         try {
            return Timestamp.valueOf(value.toString());
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
