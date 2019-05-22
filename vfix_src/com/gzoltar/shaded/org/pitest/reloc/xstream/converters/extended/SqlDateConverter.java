package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.sql.Date;

public class SqlDateConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Date.class);
   }

   public Object fromString(String str) {
      return Date.valueOf(str);
   }
}
