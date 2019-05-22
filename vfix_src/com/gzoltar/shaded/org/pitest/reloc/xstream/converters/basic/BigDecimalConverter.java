package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import java.math.BigDecimal;

public class BigDecimalConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(BigDecimal.class);
   }

   public Object fromString(String str) {
      return new BigDecimal(str);
   }
}
