package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.path.Path;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class ReferenceByXPathMarshaller extends AbstractReferenceMarshaller {
   private final int mode;

   public ReferenceByXPathMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper, int mode) {
      super(writer, converterLookup, mapper);
      this.mode = mode;
   }

   protected String createReference(Path currentPath, Object existingReferenceKey) {
      Path existingPath = (Path)existingReferenceKey;
      Path referencePath = (this.mode & ReferenceByXPathMarshallingStrategy.ABSOLUTE) > 0 ? existingPath : currentPath.relativeTo(existingPath);
      return (this.mode & ReferenceByXPathMarshallingStrategy.SINGLE_NODE) > 0 ? referencePath.explicit() : referencePath.toString();
   }

   protected Object createReferenceKey(Path currentPath, Object item) {
      return currentPath;
   }

   protected void fireValidReference(Object referenceKey) {
   }
}
