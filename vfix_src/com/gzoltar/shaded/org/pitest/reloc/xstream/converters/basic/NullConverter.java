package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class NullConverter implements Converter {
   public boolean canConvert(Class type) {
      return type == null || Mapper.Null.class.isAssignableFrom(type);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      ExtendedHierarchicalStreamWriterHelper.startNode(writer, "null", Mapper.Null.class);
      writer.endNode();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return null;
   }
}
