package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Set;

public class ArraySetMultiMap<K, V> extends AbstractMultiMap<K, V> {
   public static final ArraySetMultiMap EMPTY = new ArraySetMultiMap<Object, Object>() {
      public boolean put(Object key, Object val) {
         throw new RuntimeException();
      }

      public boolean putAll(Object key, Collection<? extends Object> vals) {
         throw new RuntimeException();
      }
   };

   public ArraySetMultiMap() {
      super(false);
   }

   public ArraySetMultiMap(boolean create) {
      super(create);
   }

   protected Set<V> createSet() {
      return new ArraySet();
   }

   protected Set<V> emptySet() {
      return ArraySet.empty();
   }

   public ArraySet<V> get(K key) {
      return (ArraySet)super.get(key);
   }
}
