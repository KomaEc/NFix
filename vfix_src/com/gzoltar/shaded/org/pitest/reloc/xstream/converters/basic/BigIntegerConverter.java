package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import java.math.BigInteger;

public class BigIntegerConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(BigInteger.class);
   }

   public Object fromString(String str) {
      return new BigInteger(str);
   }
}
