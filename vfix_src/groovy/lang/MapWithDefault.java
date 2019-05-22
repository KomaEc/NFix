package groovy.lang;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class MapWithDefault<K, V> implements Map<K, V> {
   private final Map<K, V> delegate;
   private final Closure initClosure;

   private MapWithDefault(Map<K, V> m, Closure initClosure) {
      this.delegate = m;
      this.initClosure = initClosure;
   }

   public static <K, V> Map<K, V> newInstance(Map<K, V> m, Closure initClosure) {
      return new MapWithDefault(m, initClosure);
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.delegate.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.delegate.containsValue(value);
   }

   public V get(Object key) {
      if (!this.delegate.containsKey(key)) {
         this.delegate.put(key, this.initClosure.call(new Object[]{key}));
      }

      return this.delegate.get(key);
   }

   public V put(K key, V value) {
      return this.delegate.put(key, value);
   }

   public V remove(Object key) {
      return this.delegate.remove(key);
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      this.delegate.putAll(m);
   }

   public void clear() {
      this.delegate.clear();
   }

   public Set<K> keySet() {
      return this.delegate.keySet();
   }

   public Collection<V> values() {
      return this.delegate.values();
   }

   public Set<Entry<K, V>> entrySet() {
      return this.delegate.entrySet();
   }

   public boolean equals(Object obj) {
      return this.delegate.equals(obj);
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }
}
