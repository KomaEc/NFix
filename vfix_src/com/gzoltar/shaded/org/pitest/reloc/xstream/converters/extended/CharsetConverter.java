package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.nio.charset.Charset;

public class CharsetConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return Charset.class.isAssignableFrom(type);
   }

   public String toString(Object obj) {
      return obj == null ? null : ((Charset)obj).name();
   }

   public Object fromString(String str) {
      return Charset.forName(str);
   }
}
