package soot.jimple.spark.ondemand.genericutil;

import java.util.Set;

public class ArraySetDupesMultiMap<K, V> extends AbstractMultiMap<K, V> {
   public ArraySetDupesMultiMap(boolean create) {
      super(create);
   }

   public ArraySetDupesMultiMap() {
      this(false);
   }

   protected Set<V> createSet() {
      return new ArraySet(1, false);
   }
}
