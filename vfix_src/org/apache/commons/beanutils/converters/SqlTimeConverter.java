package org.apache.commons.beanutils.converters;

import java.sql.Time;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class SqlTimeConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public SqlTimeConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public SqlTimeConverter(Object defaultValue) {
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
      } else if (value instanceof Time) {
         return value;
      } else {
         try {
            return Time.valueOf(value.toString());
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
