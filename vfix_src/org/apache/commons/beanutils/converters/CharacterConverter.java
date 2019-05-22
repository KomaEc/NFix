package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class CharacterConverter implements Converter {
   private Object defaultValue = null;
   private boolean useDefault = true;

   public CharacterConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public CharacterConverter(Object defaultValue) {
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
      } else if (value instanceof Character) {
         return value;
      } else {
         try {
            return new Character(value.toString().charAt(0));
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
