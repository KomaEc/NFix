package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   static final double MAX_LOAD_FACTOR = 1.2D;
   private final transient ImmutableMapEntry<K, V>[] keyTable;
   private final transient ImmutableMapEntry<K, V>[] valueTable;
   private final transient ImmutableMapEntry<K, V>[] entries;
   private final transient int mask;
   private final transient int hashCode;
   private transient ImmutableBiMap<V, K> inverse;

   RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry<?, ?>... entriesToAdd) {
      this(entriesToAdd.length, entriesToAdd);
   }

   RegularImmutableBiMap(int n, ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
      int tableSize = Hashing.closedTableSize(n, 1.2D);
      this.mask = tableSize - 1;
      ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
      ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
      ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
      int hashCode = 0;

      for(int i = 0; i < n; ++i) {
         ImmutableMapEntry.TerminalEntry<K, V> entry = entriesToAdd[i];
         K key = entry.getKey();
         V value = entry.getValue();
         int keyHash = key.hashCode();
         int valueHash = value.hashCode();
         int keyBucket = Hashing.smear(keyHash) & this.mask;
         int valueBucket = Hashing.smear(valueHash) & this.mask;
         ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];

         ImmutableMapEntry nextInValueBucket;
         for(nextInValueBucket = nextInKeyBucket; nextInValueBucket != null; nextInValueBucket = nextInValueBucket.getNextInKeyBucket()) {
            checkNoConflict(!key.equals(nextInValueBucket.getKey()), "key", entry, nextInValueBucket);
         }

         nextInValueBucket = valueTable[valueBucket];

         for(ImmutableMapEntry valueEntry = nextInValueBucket; valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
            checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
         }

         ImmutableMapEntry<K, V> newEntry = nextInKeyBucket == null && nextInValueBucket == null ? entry : new RegularImmutableBiMap.NonTerminalBiMapEntry(entry, nextInKeyBucket, nextInValueBucket);
         keyTable[keyBucket] = (ImmutableMapEntry)newEntry;
         valueTable[valueBucket] = (ImmutableMapEntry)newEntry;
         entries[i] = (ImmutableMapEntry)newEntry;
         hashCode += keyHash ^ valueHash;
      }

      this.keyTable = keyTable;
      this.valueTable = valueTable;
      this.entries = entries;
      this.hashCode = hashCode;
   }

   RegularImmutableBiMap(Entry<?, ?>[] entriesToAdd) {
      int n = entriesToAdd.length;
      int tableSize = Hashing.closedTableSize(n, 1.2D);
      this.mask = tableSize - 1;
      ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
      ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
      ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
      int hashCode = 0;

      for(int i = 0; i < n; ++i) {
         Entry<K, V> entry = entriesToAdd[i];
         K key = entry.getKey();
         V value = entry.getValue();
         CollectPreconditions.checkEntryNotNull(key, value);
         int keyHash = key.hashCode();
         int valueHash = value.hashCode();
         int keyBucket = Hashing.smear(keyHash) & this.mask;
         int valueBucket = Hashing.smear(valueHash) & this.mask;
         ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];

         ImmutableMapEntry nextInValueBucket;
         for(nextInValueBucket = nextInKeyBucket; nextInValueBucket != null; nextInValueBucket = nextInValueBucket.getNextInKeyBucket()) {
            checkNoConflict(!key.equals(nextInValueBucket.getKey()), "key", entry, nextInValueBucket);
         }

         nextInValueBucket = valueTable[valueBucket];

         for(ImmutableMapEntry valueEntry = nextInValueBucket; valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
            checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
         }

         ImmutableMapEntry<K, V> newEntry = nextInKeyBucket == null && nextInValueBucket == null ? new ImmutableMapEntry.TerminalEntry(key, value) : new RegularImmutableBiMap.NonTerminalBiMapEntry(key, value, nextInKeyBucket, nextInValueBucket);
         keyTable[keyBucket] = (ImmutableMapEntry)newEntry;
         valueTable[valueBucket] = (ImmutableMapEntry)newEntry;
         entries[i] = (ImmutableMapEntry)newEntry;
         hashCode += keyHash ^ valueHash;
      }

      this.keyTable = keyTable;
      this.valueTable = valueTable;
      this.entries = entries;
      this.hashCode = hashCode;
   }

   private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) {
      return new ImmutableMapEntry[length];
   }

   @Nullable
   public V get(@Nullable Object key) {
      if (key == null) {
         return null;
      } else {
         int bucket = Hashing.smear(key.hashCode()) & this.mask;

         for(ImmutableMapEntry entry = this.keyTable[bucket]; entry != null; entry = entry.getNextInKeyBucket()) {
            if (key.equals(entry.getKey())) {
               return entry.getValue();
            }
         }

         return null;
      }
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet<K, V>() {
         ImmutableMap<K, V> map() {
            return RegularImmutableBiMap.this;
         }

         public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.asList().iterator();
         }

         ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList(this, RegularImmutableBiMap.this.entries);
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
         }
      };
   }

   boolean isPartialView() {
      return false;
   }

   public int size() {
      return this.entries.length;
   }

   public ImmutableBiMap<V, K> inverse() {
      ImmutableBiMap<V, K> result = this.inverse;
      return result == null ? (this.inverse = new RegularImmutableBiMap.Inverse()) : result;
   }

   private static class InverseSerializedForm<K, V> implements Serializable {
      private final ImmutableBiMap<K, V> forward;
      private static final long serialVersionUID = 1L;

      InverseSerializedForm(ImmutableBiMap<K, V> forward) {
         this.forward = forward;
      }

      Object readResolve() {
         return this.forward.inverse();
      }
   }

   private final class Inverse extends ImmutableBiMap<V, K> {
      private Inverse() {
      }

      public int size() {
         return this.inverse().size();
      }

      public ImmutableBiMap<K, V> inverse() {
         return RegularImmutableBiMap.this;
      }

      public K get(@Nullable Object value) {
         if (value == null) {
            return null;
         } else {
            int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;

            for(ImmutableMapEntry entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; entry = entry.getNextInValueBucket()) {
               if (value.equals(entry.getValue())) {
                  return entry.getKey();
               }
            }

            return null;
         }
      }

      ImmutableSet<Entry<V, K>> createEntrySet() {
         return new RegularImmutableBiMap.Inverse.InverseEntrySet();
      }

      boolean isPartialView() {
         return false;
      }

      Object writeReplace() {
         return new RegularImmutableBiMap.InverseSerializedForm(RegularImmutableBiMap.this);
      }

      // $FF: synthetic method
      Inverse(Object x1) {
         this();
      }

      final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
         ImmutableMap<V, K> map() {
            return Inverse.this;
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
         }

         public UnmodifiableIterator<Entry<V, K>> iterator() {
            return this.asList().iterator();
         }

         ImmutableList<Entry<V, K>> createAsList() {
            return new ImmutableAsList<Entry<V, K>>() {
               public Entry<V, K> get(int index) {
                  Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                  return Maps.immutableEntry(entry.getValue(), entry.getKey());
               }

               ImmutableCollection<Entry<V, K>> delegateCollection() {
                  return InverseEntrySet.this;
               }
            };
         }
      }
   }

   private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V> {
      @Nullable
      private final ImmutableMapEntry<K, V> nextInKeyBucket;
      @Nullable
      private final ImmutableMapEntry<K, V> nextInValueBucket;

      NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
         super(key, value);
         this.nextInKeyBucket = nextInKeyBucket;
         this.nextInValueBucket = nextInValueBucket;
      }

      NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
         super(contents);
         this.nextInKeyBucket = nextInKeyBucket;
         this.nextInValueBucket = nextInValueBucket;
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInKeyBucket() {
         return this.nextInKeyBucket;
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInValueBucket() {
         return this.nextInValueBucket;
      }
   }
}
