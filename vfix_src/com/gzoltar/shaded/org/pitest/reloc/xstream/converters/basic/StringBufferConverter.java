package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class StringBufferConverter extends AbstractSingleValueConverter {
   public Object fromString(String str) {
      return new StringBuffer(str);
   }

   public boolean canConvert(Class type) {
      return type.equals(StringBuffer.class);
   }
}
