package org.jboss.util.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LazyMap<K, V> implements Map<K, V>, Serializable {
   private static final long serialVersionUID = 1L;
   private Map<K, V> delegate = Collections.emptyMap();

   private Map<K, V> createImplementation() {
      return (Map)(!(this.delegate instanceof HashMap) ? new HashMap(this.delegate) : this.delegate);
   }

   public void clear() {
      this.delegate = Collections.emptyMap();
   }

   public boolean containsKey(Object key) {
      return this.delegate.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.delegate.containsValue(value);
   }

   public Set<Entry<K, V>> entrySet() {
      return this.delegate.entrySet();
   }

   public V get(Object key) {
      return this.delegate.get(key);
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Set<K> keySet() {
      return this.delegate.keySet();
   }

   public V put(K key, V value) {
      if (this.delegate.isEmpty()) {
         this.delegate = Collections.singletonMap(key, value);
         return null;
      } else {
         this.delegate = this.createImplementation();
         return this.delegate.put(key, value);
      }
   }

   public void putAll(Map<? extends K, ? extends V> t) {
      this.delegate = this.createImplementation();
      this.delegate.putAll(t);
   }

   public V remove(Object key) {
      this.delegate = this.createImplementation();
      return this.delegate.remove(key);
   }

   public int size() {
      return this.delegate.size();
   }

   public Collection<V> values() {
      return this.delegate.values();
   }

   public String toString() {
      return this.delegate.toString();
   }
}
