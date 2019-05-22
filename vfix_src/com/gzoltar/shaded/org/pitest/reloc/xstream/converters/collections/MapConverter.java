package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapConverter extends AbstractCollectionConverter {
   private final Class type;

   public MapConverter(Mapper mapper) {
      this(mapper, (Class)null);
   }

   public MapConverter(Mapper mapper, Class type) {
      super(mapper);
      this.type = type;
      if (type != null && !Map.class.isAssignableFrom(type)) {
         throw new IllegalArgumentException(type + " not of type " + Map.class);
      }
   }

   public boolean canConvert(Class type) {
      if (this.type != null) {
         return type.equals(this.type);
      } else {
         return type.equals(HashMap.class) || type.equals(Hashtable.class) || type.getName().equals("java.util.LinkedHashMap") || type.getName().equals("java.util.concurrent.ConcurrentHashMap") || type.getName().equals("sun.font.AttributeMap");
      }
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Map map = (Map)source;
      String entryName = this.mapper().serializedClass(Entry.class);
      Iterator iterator = map.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry entry = (Entry)iterator.next();
         ExtendedHierarchicalStreamWriterHelper.startNode(writer, entryName, entry.getClass());
         this.writeItem(entry.getKey(), context, writer);
         this.writeItem(entry.getValue(), context, writer);
         writer.endNode();
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Map map = (Map)this.createCollection(context.getRequiredType());
      this.populateMap(reader, context, map);
      return map;
   }

   protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {
      this.populateMap(reader, context, map, map);
   }

   protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
      while(reader.hasMoreChildren()) {
         reader.moveDown();
         this.putCurrentEntryIntoMap(reader, context, map, target);
         reader.moveUp();
      }

   }

   protected void putCurrentEntryIntoMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
      reader.moveDown();
      Object key = this.readItem(reader, context, map);
      reader.moveUp();
      reader.moveDown();
      Object value = this.readItem(reader, context, map);
      reader.moveUp();
      target.put(key, value);
   }

   protected Object createCollection(Class type) {
      return super.createCollection(this.type != null ? this.type : type);
   }
}
