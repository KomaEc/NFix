package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class CollectionConverter extends AbstractCollectionConverter {
   private final Class type;

   public CollectionConverter(Mapper mapper) {
      this(mapper, (Class)null);
   }

   public CollectionConverter(Mapper mapper, Class type) {
      super(mapper);
      this.type = type;
      if (type != null && !Collection.class.isAssignableFrom(type)) {
         throw new IllegalArgumentException(type + " not of type " + Collection.class);
      }
   }

   public boolean canConvert(Class type) {
      if (this.type != null) {
         return type.equals(this.type);
      } else {
         return type.equals(ArrayList.class) || type.equals(HashSet.class) || type.equals(LinkedList.class) || type.equals(Vector.class) || JVM.is14() && type.getName().equals("java.util.LinkedHashSet");
      }
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Collection collection = (Collection)source;
      Iterator iterator = collection.iterator();

      while(iterator.hasNext()) {
         Object item = iterator.next();
         this.writeItem(item, context, writer);
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Collection collection = (Collection)this.createCollection(context.getRequiredType());
      this.populateCollection(reader, context, collection);
      return collection;
   }

   protected void populateCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection) {
      this.populateCollection(reader, context, collection, collection);
   }

   protected void populateCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection, Collection target) {
      while(reader.hasMoreChildren()) {
         reader.moveDown();
         this.addCurrentElementToCollection(reader, context, collection, target);
         reader.moveUp();
      }

   }

   protected void addCurrentElementToCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection, Collection target) {
      Object item = this.readItem(reader, context, collection);
      target.add(item);
   }

   protected Object createCollection(Class type) {
      return super.createCollection(this.type != null ? this.type : type);
   }
}
