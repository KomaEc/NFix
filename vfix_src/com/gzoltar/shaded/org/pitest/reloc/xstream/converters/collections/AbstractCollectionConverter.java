package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public abstract class AbstractCollectionConverter implements Converter {
   private final Mapper mapper;

   public abstract boolean canConvert(Class var1);

   public AbstractCollectionConverter(Mapper mapper) {
      this.mapper = mapper;
   }

   protected Mapper mapper() {
      return this.mapper;
   }

   public abstract void marshal(Object var1, HierarchicalStreamWriter var2, MarshallingContext var3);

   public abstract Object unmarshal(HierarchicalStreamReader var1, UnmarshallingContext var2);

   protected void writeItem(Object item, MarshallingContext context, HierarchicalStreamWriter writer) {
      String name;
      if (item == null) {
         name = this.mapper().serializedClass((Class)null);
         ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, Mapper.Null.class);
         writer.endNode();
      } else {
         name = this.mapper().serializedClass(item.getClass());
         ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, item.getClass());
         context.convertAnother(item);
         writer.endNode();
      }

   }

   protected Object readItem(HierarchicalStreamReader reader, UnmarshallingContext context, Object current) {
      Class type = HierarchicalStreams.readClassType(reader, this.mapper());
      return context.convertAnother(current, type);
   }

   protected Object createCollection(Class type) {
      Class defaultType = this.mapper().defaultImplementationOf(type);

      try {
         return defaultType.newInstance();
      } catch (InstantiationException var4) {
         throw new ConversionException("Cannot instantiate " + defaultType.getName(), var4);
      } catch (IllegalAccessException var5) {
         throw new ConversionException("Cannot instantiate " + defaultType.getName(), var5);
      }
   }
}
