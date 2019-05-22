package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.sql.Time;

public class SqlTimeConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Time.class);
   }

   public Object fromString(String str) {
      return Time.valueOf(str);
   }
}
