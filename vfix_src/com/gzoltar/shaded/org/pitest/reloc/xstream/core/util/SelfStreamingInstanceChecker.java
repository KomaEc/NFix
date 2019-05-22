package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;

public class SelfStreamingInstanceChecker implements Converter {
   private final Object self;
   private Converter defaultConverter;
   private final ConverterLookup lookup;

   public SelfStreamingInstanceChecker(ConverterLookup lookup, Object xstream) {
      this.lookup = lookup;
      this.self = xstream;
   }

   /** @deprecated */
   public SelfStreamingInstanceChecker(Converter defaultConverter, Object xstream) {
      this.defaultConverter = defaultConverter;
      this.self = xstream;
      this.lookup = null;
   }

   public boolean canConvert(Class type) {
      return type == this.self.getClass();
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      if (source == this.self) {
         throw new ConversionException("Cannot marshal the XStream instance in action");
      } else {
         this.getConverter().marshal(source, writer, context);
      }
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return this.getConverter().unmarshal(reader, context);
   }

   private Converter getConverter() {
      return this.defaultConverter != null ? this.defaultConverter : this.lookup.lookupConverterForType(Object.class);
   }
}
