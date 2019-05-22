package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class DoubleConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Double.TYPE) || type.equals(Double.class);
   }

   public Object fromString(String str) {
      return Double.valueOf(str);
   }
}
