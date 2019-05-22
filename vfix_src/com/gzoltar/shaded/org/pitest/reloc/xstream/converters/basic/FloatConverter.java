package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class FloatConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Float.TYPE) || type.equals(Float.class);
   }

   public Object fromString(String str) {
      return Float.valueOf(str);
   }
}
