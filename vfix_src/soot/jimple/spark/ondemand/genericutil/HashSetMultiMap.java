package soot.jimple.spark.ondemand.genericutil;

import java.util.HashSet;
import java.util.Set;

public class HashSetMultiMap<K, V> extends AbstractMultiMap<K, V> {
   public HashSetMultiMap() {
      super(false);
   }

   public HashSetMultiMap(boolean create) {
      super(create);
   }

   protected Set<V> createSet() {
      return new HashSet();
   }
}
