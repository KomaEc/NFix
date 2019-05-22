package soot;

import java.util.AbstractMap;
import java.util.Set;
import java.util.Map.Entry;

public class AbstractUnitAllMapTo<K, V> extends AbstractMap<K, V> {
   V dest;

   public AbstractUnitAllMapTo(V dest) {
      this.dest = dest;
   }

   public V get(Object key) {
      return this.dest;
   }

   public Set<Entry<K, V>> entrySet() {
      throw new UnsupportedOperationException();
   }
}
