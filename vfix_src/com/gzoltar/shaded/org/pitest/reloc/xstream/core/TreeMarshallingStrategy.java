package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class TreeMarshallingStrategy extends AbstractTreeMarshallingStrategy {
   protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      return new TreeUnmarshaller(root, reader, converterLookup, mapper);
   }

   protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
      return new TreeMarshaller(writer, converterLookup, mapper);
   }
}
