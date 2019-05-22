package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

class SoftValueRef<K, V> extends SoftReference<V> implements ValueRef<K, V> {
   public K key;

   static <K, V> SoftValueRef<K, V> create(K key, V val, ReferenceQueue<V> q) {
      return val == null ? null : new SoftValueRef(key, val, q);
   }

   private SoftValueRef(K key, V val, ReferenceQueue<V> q) {
      super(val, q);
      this.key = key;
   }

   public K getKey() {
      return this.key;
   }

   public V getValue() {
      return this.get();
   }

   public V setValue(V value) {
      throw new UnsupportedOperationException("setValue");
   }

   public String toString() {
      return String.valueOf(this.get());
   }
}
