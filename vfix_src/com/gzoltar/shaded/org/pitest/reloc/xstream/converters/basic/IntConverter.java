package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class IntConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Integer.TYPE) || type.equals(Integer.class);
   }

   public Object fromString(String str) {
      long value = Long.decode(str);
      if (value >= -2147483648L && value <= 4294967295L) {
         return new Integer((int)value);
      } else {
         throw new NumberFormatException("For input string: \"" + str + '"');
      }
   }
}
