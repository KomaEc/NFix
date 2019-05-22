package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;

public class EnumSingleValueConverter extends AbstractSingleValueConverter {
   private final Class<? extends Enum> enumType;

   public EnumSingleValueConverter(Class<? extends Enum> type) {
      if (!Enum.class.isAssignableFrom(type) && type != Enum.class) {
         throw new IllegalArgumentException("Converter can only handle defined enums");
      } else {
         this.enumType = type;
      }
   }

   public boolean canConvert(Class type) {
      return this.enumType.isAssignableFrom(type);
   }

   public String toString(Object obj) {
      return ((Enum)Enum.class.cast(obj)).name();
   }

   public Object fromString(String str) {
      Enum result = Enum.valueOf(this.enumType, str);
      return result;
   }
}
