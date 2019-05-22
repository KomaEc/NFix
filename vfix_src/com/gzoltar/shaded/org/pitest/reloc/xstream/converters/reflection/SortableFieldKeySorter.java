package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.OrderRetainingMap;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortableFieldKeySorter implements FieldKeySorter, Caching {
   private final Map map = new HashMap();

   public Map sort(Class type, Map keyedByFieldKey) {
      if (!this.map.containsKey(type)) {
         return keyedByFieldKey;
      } else {
         Map result = new OrderRetainingMap();
         FieldKey[] fieldKeys = (FieldKey[])((FieldKey[])keyedByFieldKey.keySet().toArray(new FieldKey[keyedByFieldKey.size()]));
         Arrays.sort(fieldKeys, (Comparator)this.map.get(type));

         for(int i = 0; i < fieldKeys.length; ++i) {
            result.put(fieldKeys[i], keyedByFieldKey.get(fieldKeys[i]));
         }

         return result;
      }
   }

   public void registerFieldOrder(Class type, String[] fields) {
      this.map.put(type, new SortableFieldKeySorter.FieldComparator(fields));
   }

   public void flushCache() {
      this.map.clear();
   }

   private class FieldComparator implements Comparator {
      private final String[] fieldOrder;

      public FieldComparator(String[] fields) {
         this.fieldOrder = fields;
      }

      public int compare(String first, String second) {
         int firstPosition = -1;
         int secondPosition = -1;

         for(int i = 0; i < this.fieldOrder.length; ++i) {
            if (this.fieldOrder[i].equals(first)) {
               firstPosition = i;
            }

            if (this.fieldOrder[i].equals(second)) {
               secondPosition = i;
            }
         }

         if (firstPosition != -1 && secondPosition != -1) {
            return firstPosition - secondPosition;
         } else {
            throw new StreamException("You have not given XStream a list of all fields to be serialized.");
         }
      }

      public int compare(Object firstObject, Object secondObject) {
         FieldKey first = (FieldKey)firstObject;
         FieldKey second = (FieldKey)secondObject;
         return this.compare(first.getFieldName(), second.getFieldName());
      }
   }
}
