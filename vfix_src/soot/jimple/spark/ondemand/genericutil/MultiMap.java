package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Set;

public interface MultiMap<K, V> {
   Set<V> get(K var1);

   boolean put(K var1, V var2);

   boolean remove(K var1, V var2);

   Set<K> keySet();

   boolean containsKey(K var1);

   int size();

   String toString();

   boolean putAll(K var1, Collection<? extends V> var2);

   Set<V> removeAll(K var1);

   void clear();

   boolean isEmpty();
}
