package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;

public class CharConverter implements Converter, SingleValueConverter {
   public boolean canConvert(Class type) {
      return type.equals(Character.TYPE) || type.equals(Character.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      writer.setValue(this.toString(source));
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String nullAttribute = reader.getAttribute("null");
      return nullAttribute != null && nullAttribute.equals("true") ? new Character('\u0000') : this.fromString(reader.getValue());
   }

   public Object fromString(String str) {
      return str.length() == 0 ? new Character('\u0000') : new Character(str.charAt(0));
   }

   public String toString(Object obj) {
      char ch = (Character)obj;
      return ch == 0 ? "" : obj.toString();
   }
}
