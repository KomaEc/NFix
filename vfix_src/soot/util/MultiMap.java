package soot.util;

import heros.solver.Pair;
import java.util.Map;
import java.util.Set;

public interface MultiMap<K, V> extends Iterable<Pair<K, V>> {
   boolean isEmpty();

   int numKeys();

   boolean contains(K var1, V var2);

   boolean containsKey(K var1);

   boolean containsValue(V var1);

   boolean put(K var1, V var2);

   boolean putAll(K var1, Set<V> var2);

   boolean putAll(Map<K, Set<V>> var1);

   boolean putAll(MultiMap<K, V> var1);

   boolean remove(K var1, V var2);

   boolean remove(K var1);

   boolean removeAll(K var1, Set<V> var2);

   Set<V> get(K var1);

   Set<K> keySet();

   Set<V> values();

   boolean equals(Object var1);

   int hashCode();

   int size();

   void clear();
}
