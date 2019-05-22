package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class ShortConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Short.TYPE) || type.equals(Short.class);
   }

   public Object fromString(String str) {
      int value = Integer.decode(str);
      if (value >= -32768 && value <= 65535) {
         return new Short((short)value);
      } else {
         throw new NumberFormatException("For input string: \"" + str + '"');
      }
   }
}
