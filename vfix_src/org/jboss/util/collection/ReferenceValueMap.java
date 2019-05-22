package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

public abstract class ReferenceValueMap<K, V> extends AbstractMap<K, V> {
   private Map<K, ValueRef<K, V>> map;
   private ReferenceQueue<V> queue;

   protected ReferenceValueMap() {
      this.queue = new ReferenceQueue();
      this.map = this.createMap();
   }

   protected ReferenceValueMap(int initialCapacity) {
      this.queue = new ReferenceQueue();
      this.map = this.createMap(initialCapacity);
   }

   protected ReferenceValueMap(int initialCapacity, float loadFactor) {
      this.queue = new ReferenceQueue();
      this.map = this.createMap(initialCapacity, loadFactor);
   }

   protected ReferenceValueMap(Map<K, V> t) {
      this(Math.max(2 * t.size(), 11), 0.75F);
      this.putAll(t);
   }

   protected ReferenceValueMap(Comparator<K> comparator) {
      this.queue = new ReferenceQueue();
      this.map = this.createMap(comparator);
   }

   protected ReferenceValueMap(SortedMap<K, ValueRef<K, V>> sorted) {
      this.queue = new ReferenceQueue();
      this.map = this.createMap(sorted);
   }

   protected abstract Map<K, ValueRef<K, V>> createMap();

   protected abstract Map<K, ValueRef<K, V>> createMap(int var1);

   protected abstract Map<K, ValueRef<K, V>> createMap(int var1, float var2);

   protected abstract Map<K, ValueRef<K, V>> createMap(Comparator<K> var1);

   protected abstract Map<K, ValueRef<K, V>> createMap(SortedMap<K, ValueRef<K, V>> var1);

   public int size() {
      this.processQueue();
      return this.map.size();
   }

   public boolean containsKey(Object key) {
      this.processQueue();
      return this.map.containsKey(key);
   }

   public V get(Object key) {
      this.processQueue();
      ValueRef<K, V> ref = (ValueRef)this.map.get(key);
      return ref != null ? ref.get() : null;
   }

   public V put(K key, V value) {
      this.processQueue();
      ValueRef<K, V> ref = this.create(key, value, this.queue);
      ValueRef<K, V> result = (ValueRef)this.map.put(key, ref);
      return result != null ? result.get() : null;
   }

   public V remove(Object key) {
      this.processQueue();
      ValueRef<K, V> result = (ValueRef)this.map.remove(key);
      return result != null ? result.get() : null;
   }

   public Set<Entry<K, V>> entrySet() {
      this.processQueue();
      return new ReferenceValueMap.EntrySet();
   }

   public void clear() {
      this.processQueue();
      this.map.clear();
   }

   private void processQueue() {
      for(ValueRef ref = (ValueRef)this.queue.poll(); ref != null; ref = (ValueRef)this.queue.poll()) {
         if (ref == this.map.get(ref.getKey())) {
            this.map.remove(ref.getKey());
         }
      }

   }

   protected abstract ValueRef<K, V> create(K var1, V var2, ReferenceQueue<V> var3);

   public String toString() {
      return this.map.toString();
   }

   private class EntrySetIterator implements Iterator<Entry<K, V>> {
      private Iterator<Entry<K, ValueRef<K, V>>> delegate;

      public EntrySetIterator(Iterator<Entry<K, ValueRef<K, V>>> delegate) {
         this.delegate = delegate;
      }

      public boolean hasNext() {
         return this.delegate.hasNext();
      }

      public Entry<K, V> next() {
         Entry<K, ValueRef<K, V>> next = (Entry)this.delegate.next();
         return (Entry)next.getValue();
      }

      public void remove() {
         throw new UnsupportedOperationException("remove");
      }
   }

   private class EntrySet extends AbstractSet<Entry<K, V>> {
      private EntrySet() {
      }

      public Iterator<Entry<K, V>> iterator() {
         return ReferenceValueMap.this.new EntrySetIterator(ReferenceValueMap.this.map.entrySet().iterator());
      }

      public int size() {
         return ReferenceValueMap.this.size();
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }
}
