package org.apache.commons.beanutils.converters;

import java.sql.Date;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class SqlDateConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public SqlDateConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public SqlDateConverter(Object defaultValue) {
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
      } else if (value instanceof Date) {
         return value;
      } else {
         try {
            return Date.valueOf(value.toString());
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
