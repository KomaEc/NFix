package soot.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class IdentityHashMultiMap<K, V> extends HashMultiMap<K, V> {
   private static final long serialVersionUID = 4960774381646981495L;

   protected Map<K, Set<V>> createMap(int initialSize) {
      return new IdentityHashMap(initialSize);
   }

   protected Set<V> newSet() {
      return new IdentityHashSet();
   }
}
