package org.codehaus.groovy.util;

public class ManagedConcurrentMap<K, V> extends AbstractConcurrentMap<K, V> {
   protected ReferenceBundle bundle;

   public ManagedConcurrentMap(ReferenceBundle bundle) {
      super(bundle);
      this.bundle = bundle;
      if (bundle == null) {
         throw new IllegalArgumentException("bundle must not be null");
      }
   }

   protected ManagedConcurrentMap.Segment<K, V> createSegment(Object segmentInfo, int cap) {
      ReferenceBundle bundle = (ReferenceBundle)segmentInfo;
      if (bundle == null) {
         throw new IllegalArgumentException("bundle must not be null");
      } else {
         return new ManagedConcurrentMap.Segment(bundle, cap);
      }
   }

   public static class EntryWithValue<K, V> extends ManagedConcurrentMap.Entry<K, V> {
      private V value;

      public EntryWithValue(ReferenceBundle bundle, ManagedConcurrentMap.Segment segment, K key, int hash, V value) {
         super(bundle, segment, key, hash);
         this.setValue(value);
      }

      public V getValue() {
         return this.value;
      }

      public void setValue(V value) {
         this.value = value;
      }

      public void finalizeRef() {
         this.value = null;
         super.finalizeRef();
      }
   }

   public static class Entry<K, V> extends ManagedReference<K> implements AbstractConcurrentMap.Entry<K, V> {
      private final ManagedConcurrentMap.Segment segment;
      private int hash;

      public Entry(ReferenceBundle bundle, ManagedConcurrentMap.Segment segment, K key, int hash) {
         super(bundle, key);
         this.segment = segment;
         this.hash = hash;
      }

      public boolean isValid() {
         return this.get() != null;
      }

      public boolean isEqual(K key, int hash) {
         return this.hash == hash && this.get() == key;
      }

      public V getValue() {
         return this;
      }

      public void setValue(V value) {
      }

      public int getHash() {
         return this.hash;
      }

      public void finalizeRef() {
         super.finalizeReference();
         this.segment.removeEntry(this);
      }
   }

   public static class Segment<K, V> extends AbstractConcurrentMap.Segment<K, V> {
      protected final ReferenceBundle bundle;

      public Segment(ReferenceBundle bundle, int cap) {
         super(cap);
         this.bundle = bundle;
         if (bundle == null) {
            throw new IllegalArgumentException("bundle must not be null");
         }
      }

      protected AbstractConcurrentMap.Entry<K, V> createEntry(K key, int hash, V value) {
         if (this.bundle == null) {
            throw new IllegalArgumentException("bundle must not be null");
         } else {
            return new ManagedConcurrentMap.EntryWithValue(this.bundle, this, key, hash, value);
         }
      }
   }
}
