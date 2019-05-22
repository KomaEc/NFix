package org.apache.commons.beanutils.converters;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class URLConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public URLConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public URLConverter(Object defaultValue) {
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
      } else if (value instanceof URL) {
         return value;
      } else {
         try {
            return new URL(value.toString());
         } catch (MalformedURLException var4) {
            if (this.useDefault) {
               return this.defaultValue;
            } else {
               throw new ConversionException(var4);
            }
         }
      }
   }
}
