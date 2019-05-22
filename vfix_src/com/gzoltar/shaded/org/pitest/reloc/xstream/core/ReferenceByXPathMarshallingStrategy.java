package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class ReferenceByXPathMarshallingStrategy extends AbstractTreeMarshallingStrategy {
   public static int RELATIVE = 0;
   public static int ABSOLUTE = 1;
   public static int SINGLE_NODE = 2;
   private final int mode;

   public ReferenceByXPathMarshallingStrategy(int mode) {
      this.mode = mode;
   }

   protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      return new ReferenceByXPathUnmarshaller(root, reader, converterLookup, mapper);
   }

   protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
      return new ReferenceByXPathMarshaller(writer, converterLookup, mapper, this.mode);
   }
}
