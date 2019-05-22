package com.gzoltar.shaded.org.pitest.reloc.xstream;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.DataHolder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public interface MarshallingStrategy {
   Object unmarshal(Object var1, HierarchicalStreamReader var2, DataHolder var3, ConverterLookup var4, Mapper var5);

   void marshal(HierarchicalStreamWriter var1, Object var2, ConverterLookup var3, Mapper var4, DataHolder var5);
}
