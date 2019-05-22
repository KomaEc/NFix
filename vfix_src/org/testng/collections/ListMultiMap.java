package org.testng.collections;

import java.util.List;

public class ListMultiMap<K, V> extends MultiMap<K, V, List<V>> {
   protected List<V> createValue() {
      return Lists.newArrayList();
   }

   /** @deprecated */
   @Deprecated
   public static <K, V> ListMultiMap<K, V> create() {
      return Maps.newListMultiMap();
   }
}
