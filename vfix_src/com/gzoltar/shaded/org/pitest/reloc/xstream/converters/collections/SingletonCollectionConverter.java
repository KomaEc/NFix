package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.Collections;

public class SingletonCollectionConverter extends CollectionConverter {
   private static final Class LIST;
   private static final Class SET;

   public SingletonCollectionConverter(Mapper mapper) {
      super(mapper);
   }

   public boolean canConvert(Class type) {
      return LIST == type || SET == type;
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      reader.moveDown();
      Object item = this.readItem(reader, context, (Object)null);
      reader.moveUp();
      return context.getRequiredType() == LIST ? Collections.singletonList(item) : Collections.singleton(item);
   }

   static {
      LIST = Collections.singletonList(Boolean.TRUE).getClass();
      SET = Collections.singleton(Boolean.TRUE).getClass();
   }
}
