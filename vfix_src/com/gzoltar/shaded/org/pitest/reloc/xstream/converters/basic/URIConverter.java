package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import java.net.URI;
import java.net.URISyntaxException;

public class URIConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(URI.class);
   }

   public Object fromString(String str) {
      try {
         return new URI(str);
      } catch (URISyntaxException var3) {
         throw new ConversionException(var3);
      }
   }
}
