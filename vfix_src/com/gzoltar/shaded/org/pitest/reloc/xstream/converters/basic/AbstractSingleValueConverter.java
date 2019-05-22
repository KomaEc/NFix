package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;

public abstract class AbstractSingleValueConverter implements SingleValueConverter {
   public abstract boolean canConvert(Class var1);

   public String toString(Object obj) {
      return obj == null ? null : obj.toString();
   }

   public abstract Object fromString(String var1);
}
