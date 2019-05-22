package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.PresortedSet;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeSetConverter extends CollectionConverter {
   private transient TreeMapConverter treeMapConverter;
   private static final Field sortedMapField;
   private static final Object constantValue;

   public TreeSetConverter(Mapper mapper) {
      super(mapper, TreeSet.class);
      this.readResolve();
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      SortedSet sortedSet = (SortedSet)source;
      this.treeMapConverter.marshalComparator(sortedSet.comparator(), writer, context);
      super.marshal(source, writer, context);
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      TreeSet result = null;
      Comparator unmarshalledComparator = this.treeMapConverter.unmarshalComparator(reader, context, (TreeMap)null);
      boolean inFirstElement = unmarshalledComparator instanceof Mapper.Null;
      Comparator comparator = inFirstElement ? null : unmarshalledComparator;
      TreeMap treeMap;
      if (sortedMapField != null) {
         TreeSet possibleResult = comparator == null ? new TreeSet() : new TreeSet(comparator);
         Object backingMap = null;

         try {
            backingMap = sortedMapField.get(possibleResult);
         } catch (IllegalAccessException var11) {
            throw new ConversionException("Cannot get backing map of TreeSet", var11);
         }

         if (backingMap instanceof TreeMap) {
            treeMap = (TreeMap)backingMap;
            result = possibleResult;
         } else {
            treeMap = null;
         }
      } else {
         treeMap = null;
      }

      if (treeMap == null) {
         PresortedSet set = new PresortedSet(comparator);
         result = comparator == null ? new TreeSet() : new TreeSet(comparator);
         if (inFirstElement) {
            this.addCurrentElementToCollection(reader, context, result, set);
            reader.moveUp();
         }

         this.populateCollection(reader, context, result, set);
         if (set.size() > 0) {
            result.addAll(set);
         }
      } else {
         this.treeMapConverter.populateTreeMap(reader, context, treeMap, unmarshalledComparator);
      }

      return result;
   }

   private Object readResolve() {
      this.treeMapConverter = new TreeMapConverter(this.mapper()) {
         protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, final Map target) {
            TreeSetConverter.this.populateCollection(reader, context, new AbstractList() {
               public boolean add(Object object) {
                  return target.put(object, TreeSetConverter.constantValue != null ? TreeSetConverter.constantValue : object) != null;
               }

               public Object get(int location) {
                  return null;
               }

               public int size() {
                  return target.size();
               }
            });
         }

         protected void putCurrentEntryIntoMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
            Object key = this.readItem(reader, context, map);
            target.put(key, key);
         }
      };
      return this;
   }

   static {
      Object value = null;
      sortedMapField = JVM.hasOptimizedTreeSetAddAll() ? Fields.locate(TreeSet.class, SortedMap.class, false) : null;
      if (sortedMapField != null) {
         TreeSet set = new TreeSet();
         set.add("1");
         set.add("2");
         Map backingMap = null;

         try {
            backingMap = (Map)sortedMapField.get(set);
         } catch (IllegalAccessException var5) {
         }

         if (backingMap != null) {
            Object[] values = backingMap.values().toArray();
            if (values[0] == values[1]) {
               value = values[0];
            }
         }
      } else {
         Field valueField = Fields.locate(TreeSet.class, Object.class, true);
         if (valueField != null) {
            try {
               value = valueField.get((Object)null);
            } catch (IllegalAccessException var4) {
            }
         }
      }

      constantValue = value;
   }
}
