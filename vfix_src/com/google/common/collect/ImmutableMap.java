package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
   private static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
   private transient ImmutableSet<Entry<K, V>> entrySet;
   private transient ImmutableSet<K> keySet;
   private transient ImmutableCollection<V> values;
   private transient ImmutableSetMultimap<K, V> multimapView;

   public static <K, V> ImmutableMap<K, V> of() {
      return ImmutableBiMap.of();
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
      return ImmutableBiMap.of(k1, v1);
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(k1, v1), entryOf(k2, v2)});
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3)});
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)});
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)});
   }

   static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K key, V value) {
      CollectPreconditions.checkEntryNotNull(key, value);
      return new ImmutableMapEntry.TerminalEntry(key, value);
   }

   public static <K, V> ImmutableMap.Builder<K, V> builder() {
      return new ImmutableMap.Builder();
   }

   static void checkNoConflict(boolean safe, String conflictDescription, Entry<?, ?> entry1, Entry<?, ?> entry2) {
      if (!safe) {
         throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
      }
   }

   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
      if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
         ImmutableMap<K, V> kvMap = (ImmutableMap)map;
         if (!kvMap.isPartialView()) {
            return kvMap;
         }
      } else if (map instanceof EnumMap) {
         return copyOfEnumMapUnsafe(map);
      }

      Entry<?, ?>[] entries = (Entry[])map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
      switch(entries.length) {
      case 0:
         return of();
      case 1:
         Entry<K, V> onlyEntry = entries[0];
         return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
         return new RegularImmutableMap(entries);
      }
   }

   private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) {
      return copyOfEnumMap((EnumMap)map);
   }

   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> original) {
      EnumMap<K, V> copy = new EnumMap(original);
      Iterator i$ = copy.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<?, ?> entry = (Entry)i$.next();
         CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
      }

      return ImmutableEnumMap.asImmutable(copy);
   }

   ImmutableMap() {
   }

   /** @deprecated */
   @Deprecated
   public final V put(K k, V v) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final V remove(Object o) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void putAll(Map<? extends K, ? extends V> map) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(@Nullable Object key) {
      return this.get(key) != null;
   }

   public boolean containsValue(@Nullable Object value) {
      return this.values().contains(value);
   }

   public abstract V get(@Nullable Object var1);

   public ImmutableSet<Entry<K, V>> entrySet() {
      ImmutableSet<Entry<K, V>> result = this.entrySet;
      return result == null ? (this.entrySet = this.createEntrySet()) : result;
   }

   abstract ImmutableSet<Entry<K, V>> createEntrySet();

   public ImmutableSet<K> keySet() {
      ImmutableSet<K> result = this.keySet;
      return result == null ? (this.keySet = this.createKeySet()) : result;
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   public ImmutableCollection<V> values() {
      ImmutableCollection<V> result = this.values;
      return result == null ? (this.values = new ImmutableMapValues(this)) : result;
   }

   @Beta
   public ImmutableSetMultimap<K, V> asMultimap() {
      ImmutableSetMultimap<K, V> result = this.multimapView;
      return result == null ? (this.multimapView = this.createMultimapView()) : result;
   }

   private ImmutableSetMultimap<K, V> createMultimapView() {
      ImmutableMap<K, ImmutableSet<V>> map = this.viewMapValuesAsSingletonSets();
      return new ImmutableSetMultimap(map, map.size(), (Comparator)null);
   }

   private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
      return new ImmutableMap.MapViewOfValuesAsSingletonSets(this);
   }

   public boolean equals(@Nullable Object object) {
      return Maps.equalsImpl(this, object);
   }

   abstract boolean isPartialView();

   public int hashCode() {
      return this.entrySet().hashCode();
   }

   public String toString() {
      return Maps.toStringImpl(this);
   }

   Object writeReplace() {
      return new ImmutableMap.SerializedForm(this);
   }

   static class SerializedForm implements Serializable {
      private final Object[] keys;
      private final Object[] values;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap<?, ?> map) {
         this.keys = new Object[map.size()];
         this.values = new Object[map.size()];
         int i = 0;

         for(Iterator i$ = map.entrySet().iterator(); i$.hasNext(); ++i) {
            Entry<?, ?> entry = (Entry)i$.next();
            this.keys[i] = entry.getKey();
            this.values[i] = entry.getValue();
         }

      }

      Object readResolve() {
         ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder();
         return this.createMap(builder);
      }

      Object createMap(ImmutableMap.Builder<Object, Object> builder) {
         for(int i = 0; i < this.keys.length; ++i) {
            builder.put(this.keys[i], this.values[i]);
         }

         return builder.build();
      }
   }

   private static final class MapViewOfValuesAsSingletonSets<K, V> extends ImmutableMap<K, ImmutableSet<V>> {
      private final ImmutableMap<K, V> delegate;

      MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> delegate) {
         this.delegate = (ImmutableMap)Preconditions.checkNotNull(delegate);
      }

      public int size() {
         return this.delegate.size();
      }

      public boolean containsKey(@Nullable Object key) {
         return this.delegate.containsKey(key);
      }

      public ImmutableSet<V> get(@Nullable Object key) {
         V outerValue = this.delegate.get(key);
         return outerValue == null ? null : ImmutableSet.of(outerValue);
      }

      boolean isPartialView() {
         return false;
      }

      ImmutableSet<Entry<K, ImmutableSet<V>>> createEntrySet() {
         return new ImmutableMapEntrySet<K, ImmutableSet<V>>() {
            ImmutableMap<K, ImmutableSet<V>> map() {
               return MapViewOfValuesAsSingletonSets.this;
            }

            public UnmodifiableIterator<Entry<K, ImmutableSet<V>>> iterator() {
               final Iterator<Entry<K, V>> backingIterator = MapViewOfValuesAsSingletonSets.this.delegate.entrySet().iterator();
               return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                  public boolean hasNext() {
                     return backingIterator.hasNext();
                  }

                  public Entry<K, ImmutableSet<V>> next() {
                     final Entry<K, V> backingEntry = (Entry)backingIterator.next();
                     return new AbstractMapEntry<K, ImmutableSet<V>>() {
                        public K getKey() {
                           return backingEntry.getKey();
                        }

                        public ImmutableSet<V> getValue() {
                           return ImmutableSet.of(backingEntry.getValue());
                        }
                     };
                  }
               };
            }
         };
      }
   }

   public static class Builder<K, V> {
      ImmutableMapEntry.TerminalEntry<K, V>[] entries;
      int size;

      public Builder() {
         this(4);
      }

      Builder(int initialCapacity) {
         this.entries = new ImmutableMapEntry.TerminalEntry[initialCapacity];
         this.size = 0;
      }

      private void ensureCapacity(int minCapacity) {
         if (minCapacity > this.entries.length) {
            this.entries = (ImmutableMapEntry.TerminalEntry[])ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
         }

      }

      public ImmutableMap.Builder<K, V> put(K key, V value) {
         this.ensureCapacity(this.size + 1);
         ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
         this.entries[this.size++] = entry;
         return this;
      }

      public ImmutableMap.Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
         return this.put(entry.getKey(), entry.getValue());
      }

      public ImmutableMap.Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
         this.ensureCapacity(this.size + map.size());
         Iterator i$ = map.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<? extends K, ? extends V> entry = (Entry)i$.next();
            this.put(entry);
         }

         return this;
      }

      public ImmutableMap<K, V> build() {
         switch(this.size) {
         case 0:
            return ImmutableMap.of();
         case 1:
            return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
         default:
            return new RegularImmutableMap(this.size, this.entries);
         }
      }
   }
}
