package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class ByteConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Byte.TYPE) || type.equals(Byte.class);
   }

   public Object fromString(String str) {
      int value = Integer.decode(str);
      if (value >= -128 && value <= 255) {
         return new Byte((byte)value);
      } else {
         throw new NumberFormatException("For input string: \"" + str + '"');
      }
   }
}
