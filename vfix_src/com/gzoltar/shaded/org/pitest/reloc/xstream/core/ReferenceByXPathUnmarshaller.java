package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.path.Path;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.path.PathTracker;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.path.PathTrackingReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class ReferenceByXPathUnmarshaller extends AbstractReferenceUnmarshaller {
   private PathTracker pathTracker = new PathTracker();
   protected boolean isNameEncoding;

   public ReferenceByXPathUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      super(root, reader, converterLookup, mapper);
      this.reader = new PathTrackingReader(reader, this.pathTracker);
      this.isNameEncoding = reader.underlyingReader() instanceof AbstractReader;
   }

   protected Object getReferenceKey(String reference) {
      Path path = new Path(this.isNameEncoding ? ((AbstractReader)this.reader.underlyingReader()).decodeNode(reference) : reference);
      return reference.charAt(0) != '/' ? this.pathTracker.getPath().apply(path) : path;
   }

   protected Object getCurrentReferenceKey() {
      return this.pathTracker.getPath();
   }
}
