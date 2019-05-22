package org.codehaus.groovy.util;

public class ManagedDoubleKeyMap<K1, K2, V> extends AbstractConcurrentDoubleKeyMap<K1, K2, V> {
   public ManagedDoubleKeyMap(ReferenceBundle bundle) {
      super(bundle);
   }

   protected AbstractConcurrentDoubleKeyMap.Segment<K1, K2, V> createSegment(Object segmentInfo, int cap) {
      ReferenceBundle bundle = (ReferenceBundle)segmentInfo;
      return new ManagedDoubleKeyMap.Segment(bundle, cap);
   }

   private static class EntryWithValue<K1, K2, V> extends ManagedDoubleKeyMap.Entry<K1, K2, V> {
      private V value;

      public EntryWithValue(ReferenceBundle bundle, K1 key1, K2 key2, int hash, ManagedDoubleKeyMap.Segment segment) {
         super(bundle, key1, key2, hash, segment);
      }

      public V getValue() {
         return this.value;
      }

      public void setValue(V value) {
         this.value = value;
      }

      public void clean() {
         super.clean();
         this.value = null;
      }
   }

   public static class Entry<K1, K2, V> implements AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> {
      private final int hash;
      final ManagedDoubleKeyMap.Ref<K1> ref1;
      final ManagedDoubleKeyMap.Ref<K2> ref2;
      final ManagedDoubleKeyMap.Segment segment;

      public Entry(ReferenceBundle bundle, K1 key1, K2 key2, int hash, ManagedDoubleKeyMap.Segment segment) {
         this.hash = hash;
         this.segment = segment;
         this.ref1 = new ManagedDoubleKeyMap.Ref(bundle, key1, this);
         this.ref2 = new ManagedDoubleKeyMap.Ref(bundle, key2, this);
      }

      public boolean isValid() {
         return this.ref1.get() != null && this.ref2.get() != null;
      }

      public boolean isEqual(K1 key1, K2 key2, int hash) {
         return this.hash == hash && this.ref1.get() == key1 && this.ref2.get() == key2;
      }

      public V getValue() {
         return this;
      }

      public void setValue(V value) {
      }

      public int getHash() {
         return this.hash;
      }

      public void clean() {
         this.segment.removeEntry(this);
         this.ref1.clear();
         this.ref2.clear();
      }
   }

   static class Ref<K> extends ManagedReference<K> {
      final ManagedDoubleKeyMap.Entry entry;

      public Ref(ReferenceBundle bundle, K referent, ManagedDoubleKeyMap.Entry entry) {
         super(bundle, referent);
         this.entry = entry;
      }

      public void finalizeRef() {
         this.entry.clean();
      }
   }

   static class Segment<K1, K2, V> extends AbstractConcurrentDoubleKeyMap.Segment<K1, K2, V> {
      private ReferenceBundle bundle;

      public Segment(ReferenceBundle bundle, int cap) {
         super(cap);
         this.bundle = bundle;
      }

      protected AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> createEntry(K1 key1, K2 key2, int hash) {
         return new ManagedDoubleKeyMap.EntryWithValue(this.bundle, key1, key2, hash, this);
      }
   }
}
