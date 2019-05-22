package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;

public class CharArrayConverter implements Converter {
   public boolean canConvert(Class type) {
      return type.isArray() && type.getComponentType().equals(Character.TYPE);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      char[] chars = (char[])((char[])source);
      writer.setValue(new String(chars));
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return reader.getValue().toCharArray();
   }
}
