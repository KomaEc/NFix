package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.Currency;

public class CurrencyConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Currency.class);
   }

   public Object fromString(String str) {
      return Currency.getInstance(str);
   }
}
