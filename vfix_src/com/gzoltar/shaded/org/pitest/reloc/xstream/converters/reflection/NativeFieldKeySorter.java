package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class NativeFieldKeySorter implements FieldKeySorter {
   public Map sort(Class type, Map keyedByFieldKey) {
      Map map = new TreeMap(new Comparator() {
         public int compare(Object o1, Object o2) {
            FieldKey fieldKey1 = (FieldKey)o1;
            FieldKey fieldKey2 = (FieldKey)o2;
            int i = fieldKey1.getDepth() - fieldKey2.getDepth();
            if (i == 0) {
               i = fieldKey1.getOrder() - fieldKey2.getOrder();
            }

            return i;
         }
      });
      map.putAll(keyedByFieldKey);
      return map;
   }
}
