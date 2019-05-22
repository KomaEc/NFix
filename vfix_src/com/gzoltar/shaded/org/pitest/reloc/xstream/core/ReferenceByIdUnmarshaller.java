package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class ReferenceByIdUnmarshaller extends AbstractReferenceUnmarshaller {
   public ReferenceByIdUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      super(root, reader, converterLookup, mapper);
   }

   protected Object getReferenceKey(String reference) {
      return reference;
   }

   protected Object getCurrentReferenceKey() {
      String attributeName = this.getMapper().aliasForSystemAttribute("id");
      return attributeName == null ? null : this.reader.getAttribute(attributeName);
   }
}
