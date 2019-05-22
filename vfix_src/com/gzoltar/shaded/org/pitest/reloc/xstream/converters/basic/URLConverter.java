package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(URL.class);
   }

   public Object fromString(String str) {
      try {
         return new URL(str);
      } catch (MalformedURLException var3) {
         throw new ConversionException(var3);
      }
   }
}
