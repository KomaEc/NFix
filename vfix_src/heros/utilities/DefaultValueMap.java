package heros.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class DefaultValueMap<K, V> implements Map<K, V> {
   private HashMap<K, V> map = new HashMap();

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.map.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.map.containsValue(value);
   }

   protected abstract V createItem(K var1);

   public V getOrCreate(K key) {
      if (!this.map.containsKey(key)) {
         V value = this.createItem(key);
         this.map.put(key, value);
         return value;
      } else {
         return this.map.get(key);
      }
   }

   public V get(Object key) {
      return this.map.get(key);
   }

   public V put(K key, V value) {
      return this.map.put(key, value);
   }

   public V remove(Object key) {
      return this.map.remove(key);
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      this.map.putAll(m);
   }

   public void clear() {
      this.map.clear();
   }

   public Set<K> keySet() {
      return this.map.keySet();
   }

   public Collection<V> values() {
      return this.map.values();
   }

   public Set<Entry<K, V>> entrySet() {
      return this.map.entrySet();
   }
}
