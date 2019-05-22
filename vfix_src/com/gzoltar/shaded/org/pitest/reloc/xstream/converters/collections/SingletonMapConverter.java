package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.Collections;

public class SingletonMapConverter extends MapConverter {
   private static final Class MAP;

   public SingletonMapConverter(Mapper mapper) {
      super(mapper);
   }

   public boolean canConvert(Class type) {
      return MAP == type;
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      reader.moveDown();
      reader.moveDown();
      Object key = this.readItem(reader, context, (Object)null);
      reader.moveUp();
      reader.moveDown();
      Object value = this.readItem(reader, context, (Object)null);
      reader.moveUp();
      reader.moveUp();
      return Collections.singletonMap(key, value);
   }

   static {
      MAP = Collections.singletonMap(Boolean.TRUE, (Object)null).getClass();
   }
}
