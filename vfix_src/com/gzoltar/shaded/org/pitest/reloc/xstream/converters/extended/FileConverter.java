package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.io.File;

public class FileConverter extends AbstractSingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(File.class);
   }

   public Object fromString(String str) {
      return new File(str);
   }

   public String toString(Object obj) {
      return ((File)obj).getPath();
   }
}
