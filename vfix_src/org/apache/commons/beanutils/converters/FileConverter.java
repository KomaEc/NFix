package org.apache.commons.beanutils.converters;

import java.io.File;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class FileConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public FileConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public FileConverter(Object defaultValue) {
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
      } else {
         return value instanceof File ? value : new File(value.toString());
      }
   }
}
