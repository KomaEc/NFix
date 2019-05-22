package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

public class StringBuilderConverter extends AbstractSingleValueConverter {
   public Object fromString(String str) {
      return new StringBuilder(str);
   }

   public boolean canConvert(Class type) {
      return type.equals(StringBuilder.class);
   }
}
