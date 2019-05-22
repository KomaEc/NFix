package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.MarshallingStrategy;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.DataHolder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public abstract class AbstractTreeMarshallingStrategy implements MarshallingStrategy {
   public Object unmarshal(Object root, HierarchicalStreamReader reader, DataHolder dataHolder, ConverterLookup converterLookup, Mapper mapper) {
      TreeUnmarshaller context = this.createUnmarshallingContext(root, reader, converterLookup, mapper);
      return context.start(dataHolder);
   }

   public void marshal(HierarchicalStreamWriter writer, Object obj, ConverterLookup converterLookup, Mapper mapper, DataHolder dataHolder) {
      TreeMarshaller context = this.createMarshallingContext(writer, converterLookup, mapper);
      context.start(obj, dataHolder);
   }

   protected abstract TreeUnmarshaller createUnmarshallingContext(Object var1, HierarchicalStreamReader var2, ConverterLookup var3, Mapper var4);

   protected abstract TreeMarshaller createMarshallingContext(HierarchicalStreamWriter var1, ConverterLookup var2, Mapper var3);
}
