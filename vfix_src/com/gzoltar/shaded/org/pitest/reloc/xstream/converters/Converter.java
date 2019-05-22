package com.gzoltar.shaded.org.pitest.reloc.xstream.converters;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;

public interface Converter extends ConverterMatcher {
   void marshal(Object var1, HierarchicalStreamWriter var2, MarshallingContext var3);

   Object unmarshal(HierarchicalStreamReader var1, UnmarshallingContext var2);
}
