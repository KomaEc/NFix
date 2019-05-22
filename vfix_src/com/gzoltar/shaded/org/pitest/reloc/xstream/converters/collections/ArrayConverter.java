package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayConverter extends AbstractCollectionConverter {
   public ArrayConverter(Mapper mapper) {
      super(mapper);
   }

   public boolean canConvert(Class type) {
      return type.isArray();
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      int length = Array.getLength(source);

      for(int i = 0; i < length; ++i) {
         Object item = Array.get(source, i);
         this.writeItem(item, context, writer);
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      ArrayList items = new ArrayList();

      Object array;
      while(reader.hasMoreChildren()) {
         reader.moveDown();
         array = this.readItem(reader, context, (Object)null);
         items.add(array);
         reader.moveUp();
      }

      array = Array.newInstance(context.getRequiredType().getComponentType(), items.size());
      int i = 0;
      Iterator iterator = items.iterator();

      while(iterator.hasNext()) {
         Array.set(array, i++, iterator.next());
      }

      return array;
   }
}
