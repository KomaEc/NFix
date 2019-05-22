package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import java.util.EnumSet;

public class EnumConverter<T extends Enum<T>> implements IStringConverter<T> {
   private final String optionName;
   private final Class<T> clazz;

   public EnumConverter(String var1, Class<T> var2) {
      this.optionName = var1;
      this.clazz = var2;
   }

   public T convert(String var1) {
      try {
         try {
            return Enum.valueOf(this.clazz, var1);
         } catch (IllegalArgumentException var3) {
            return Enum.valueOf(this.clazz, var1.toUpperCase());
         }
      } catch (Exception var4) {
         throw new ParameterException("Invalid value for " + this.optionName + " parameter. Allowed values:" + EnumSet.allOf(this.clazz));
      }
   }
}
