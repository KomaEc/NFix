package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import java.util.UUID;

public class UUIDConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(UUID.class);
   }

   public Object fromString(String str) {
      try {
         return UUID.fromString(str);
      } catch (IllegalArgumentException var3) {
         throw new ConversionException("Cannot create UUID instance", var3);
      }
   }
}
