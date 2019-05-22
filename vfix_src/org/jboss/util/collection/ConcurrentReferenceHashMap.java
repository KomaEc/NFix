package org.jboss.util.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
   private static final long serialVersionUID = 7249069246763182397L;
   static final ConcurrentReferenceHashMap.ReferenceType DEFAULT_KEY_TYPE;
   static final ConcurrentReferenceHashMap.ReferenceType DEFAULT_VALUE_TYPE;
   static final int DEFAULT_INITIAL_CAPACITY = 16;
   static final float DEFAULT_LOAD_FACTOR = 0.75F;
   static final int DEFAULT_CONCURRENCY_LEVEL = 16;
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int RETRIES_BEFORE_LOCK = 2;
   final int segmentMask;
   final int segmentShift;
   final ConcurrentReferenceHashMap.Segment<K, V>[] segments;
   boolean identityComparisons;
   transient Set<K> keySet;
   transient Set<Entry<K, V>> entrySet;
   transient Collection<V> values;

   private static int hash(int h) {
      h += h << 15 ^ -12931;
      h ^= h >>> 10;
      h += h << 3;
      h ^= h >>> 6;
      h += (h << 2) + (h << 14);
      return h ^ h >>> 16;
   }

   final ConcurrentReferenceHashMap.Segment<K, V> segmentFor(int hash) {
      return this.segments[hash >>> this.segmentShift & this.segmentMask];
   }

   private int hashOf(Object key) {
      return hash(this.identityComparisons ? System.identityHashCode(key) : key.hashCode());
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, EnumSet<ConcurrentReferenceHashMap.Option> options) {
      if (loadFactor > 0.0F && initialCapacity >= 0 && concurrencyLevel > 0) {
         if (concurrencyLevel > 65536) {
            concurrencyLevel = 65536;
         }

         int sshift = 0;

         int ssize;
         for(ssize = 1; ssize < concurrencyLevel; ssize <<= 1) {
            ++sshift;
         }

         this.segmentShift = 32 - sshift;
         this.segmentMask = ssize - 1;
         this.segments = ConcurrentReferenceHashMap.Segment.newArray(ssize);
         if (initialCapacity > 1073741824) {
            initialCapacity = 1073741824;
         }

         int c = initialCapacity / ssize;
         if (c * ssize < initialCapacity) {
            ++c;
         }

         int cap;
         for(cap = 1; cap < c; cap <<= 1) {
         }

         this.identityComparisons = options != null && options.contains(ConcurrentReferenceHashMap.Option.IDENTITY_COMPARISONS);

         for(int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new ConcurrentReferenceHashMap.Segment(cap, loadFactor, keyType, valueType, this.identityComparisons);
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
      this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_KEY_TYPE, DEFAULT_VALUE_TYPE, (EnumSet)null);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
      this(initialCapacity, loadFactor, 16);
   }

   public ConcurrentReferenceHashMap(int initialCapacity, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType) {
      this(initialCapacity, 0.75F, 16, keyType, valueType, (EnumSet)null);
   }

   public ConcurrentReferenceHashMap(ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType) {
      this(16, 0.75F, 16, keyType, valueType, (EnumSet)null);
   }

   public ConcurrentReferenceHashMap(ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, EnumSet<ConcurrentReferenceHashMap.Option> options) {
      this(16, 0.75F, 16, keyType, valueType, options);
   }

   public ConcurrentReferenceHashMap(int initialCapacity) {
      this(initialCapacity, 0.75F, 16);
   }

   public ConcurrentReferenceHashMap() {
      this(16, 0.75F, 16);
   }

   public ConcurrentReferenceHashMap(Map<? extends K, ? extends V> m) {
      this(Math.max((int)((float)m.size() / 0.75F) + 1, 16), 0.75F, 16);
      this.putAll(m);
   }

   public boolean isEmpty() {
      ConcurrentReferenceHashMap.Segment<K, V>[] segments = this.segments;
      int[] mc = new int[segments.length];
      int mcsum = 0;

      int i;
      for(i = 0; i < segments.length; ++i) {
         if (segments[i].count != 0) {
            return false;
         }

         mcsum += mc[i] = segments[i].modCount;
      }

      if (mcsum != 0) {
         for(i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0 || mc[i] != segments[i].modCount) {
               return false;
            }
         }
      }

      return true;
   }

   public int size() {
      ConcurrentReferenceHashMap.Segment<K, V>[] segments = this.segments;
      long sum = 0L;
      long check = 0L;
      int[] mc = new int[segments.length];

      int i;
      for(i = 0; i < 2; ++i) {
         check = 0L;
         sum = 0L;
         int mcsum = 0;

         int i;
         for(i = 0; i < segments.length; ++i) {
            sum += (long)segments[i].count;
            mcsum += mc[i] = segments[i].modCount;
         }

         if (mcsum != 0) {
            for(i = 0; i < segments.length; ++i) {
               check += (long)segments[i].count;
               if (mc[i] != segments[i].modCount) {
                  check = -1L;
                  break;
               }
            }
         }

         if (check == sum) {
            break;
         }
      }

      if (check != sum) {
         sum = 0L;

         for(i = 0; i < segments.length; ++i) {
            segments[i].lock();
         }

         for(i = 0; i < segments.length; ++i) {
            sum += (long)segments[i].count;
         }

         for(i = 0; i < segments.length; ++i) {
            segments[i].unlock();
         }
      }

      return sum > 2147483647L ? Integer.MAX_VALUE : (int)sum;
   }

   public V get(Object key) {
      int hash = this.hashOf(key);
      return this.segmentFor(hash).get(key, hash);
   }

   public boolean containsKey(Object key) {
      int hash = this.hashOf(key);
      return this.segmentFor(hash).containsKey(key, hash);
   }

   public boolean containsValue(Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         ConcurrentReferenceHashMap.Segment<K, V>[] segments = this.segments;
         int[] mc = new int[segments.length];

         int i;
         for(i = 0; i < 2; ++i) {
            int sum = false;
            int mcsum = 0;

            int i;
            for(int i = 0; i < segments.length; ++i) {
               i = segments[i].count;
               mcsum += mc[i] = segments[i].modCount;
               if (segments[i].containsValue(value)) {
                  return true;
               }
            }

            boolean cleanSweep = true;
            if (mcsum != 0) {
               for(i = 0; i < segments.length; ++i) {
                  int c = segments[i].count;
                  if (mc[i] != segments[i].modCount) {
                     cleanSweep = false;
                     break;
                  }
               }
            }

            if (cleanSweep) {
               return false;
            }
         }

         for(i = 0; i < segments.length; ++i) {
            segments[i].lock();
         }

         boolean found = false;
         boolean var13 = false;

         int i;
         try {
            var13 = true;
            i = 0;

            while(true) {
               if (i >= segments.length) {
                  var13 = false;
                  break;
               }

               if (segments[i].containsValue(value)) {
                  found = true;
                  var13 = false;
                  break;
               }

               ++i;
            }
         } finally {
            if (var13) {
               for(int i = 0; i < segments.length; ++i) {
                  segments[i].unlock();
               }

            }
         }

         for(i = 0; i < segments.length; ++i) {
            segments[i].unlock();
         }

         return found;
      }
   }

   public boolean contains(Object value) {
      return this.containsValue(value);
   }

   public V put(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = this.hashOf(key);
         return this.segmentFor(hash).put(key, hash, value, false);
      }
   }

   public V putIfAbsent(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = this.hashOf(key);
         return this.segmentFor(hash).put(key, hash, value, true);
      }
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      Iterator i$ = m.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<? extends K, ? extends V> e = (Entry)i$.next();
         this.put(e.getKey(), e.getValue());
      }

   }

   public V remove(Object key) {
      int hash = this.hashOf(key);
      return this.segmentFor(hash).remove(key, hash, (Object)null, false);
   }

   public boolean remove(Object key, Object value) {
      int hash = this.hashOf(key);
      if (value == null) {
         return false;
      } else {
         return this.segmentFor(hash).remove(key, hash, value, false) != null;
      }
   }

   public boolean replace(K key, V oldValue, V newValue) {
      if (oldValue != null && newValue != null) {
         int hash = this.hashOf(key);
         return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
      } else {
         throw new NullPointerException();
      }
   }

   public V replace(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = this.hashOf(key);
         return this.segmentFor(hash).replace(key, hash, value);
      }
   }

   public void clear() {
      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].clear();
      }

   }

   public void purgeStaleEntries() {
      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].removeStale();
      }

   }

   public Set<K> keySet() {
      Set<K> ks = this.keySet;
      return ks != null ? ks : (this.keySet = new ConcurrentReferenceHashMap.KeySet());
   }

   public Collection<V> values() {
      Collection<V> vs = this.values;
      return vs != null ? vs : (this.values = new ConcurrentReferenceHashMap.Values());
   }

   public Set<Entry<K, V>> entrySet() {
      Set<Entry<K, V>> es = this.entrySet;
      return es != null ? es : (this.entrySet = new ConcurrentReferenceHashMap.EntrySet());
   }

   public Enumeration<K> keys() {
      return new ConcurrentReferenceHashMap.KeyIterator();
   }

   public Enumeration<V> elements() {
      return new ConcurrentReferenceHashMap.ValueIterator();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for(int k = 0; k < this.segments.length; ++k) {
         ConcurrentReferenceHashMap.Segment<K, V> seg = this.segments[k];
         seg.lock();

         try {
            ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = seg.table;

            for(int i = 0; i < tab.length; ++i) {
               for(ConcurrentReferenceHashMap.HashEntry e = tab[i]; e != null; e = e.next) {
                  K key = e.key();
                  if (key != null) {
                     s.writeObject(key);
                     s.writeObject(e.value());
                  }
               }
            }
         } finally {
            seg.unlock();
         }
      }

      s.writeObject((Object)null);
      s.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();

      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].setTable(new ConcurrentReferenceHashMap.HashEntry[1]);
      }

      while(true) {
         K key = s.readObject();
         V value = s.readObject();
         if (key == null) {
            return;
         }

         this.put(key, value);
      }
   }

   static {
      DEFAULT_KEY_TYPE = ConcurrentReferenceHashMap.ReferenceType.WEAK;
      DEFAULT_VALUE_TYPE = ConcurrentReferenceHashMap.ReferenceType.STRONG;
   }

   final class EntrySet extends AbstractSet<Entry<K, V>> {
      public Iterator<Entry<K, V>> iterator() {
         return ConcurrentReferenceHashMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry<?, ?> e = (Entry)o;
            V v = ConcurrentReferenceHashMap.this.get(e.getKey());
            return v != null && v.equals(e.getValue());
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry<?, ?> e = (Entry)o;
            return ConcurrentReferenceHashMap.this.remove(e.getKey(), e.getValue());
         }
      }

      public int size() {
         return ConcurrentReferenceHashMap.this.size();
      }

      public boolean isEmpty() {
         return ConcurrentReferenceHashMap.this.isEmpty();
      }

      public void clear() {
         ConcurrentReferenceHashMap.this.clear();
      }
   }

   final class Values extends AbstractCollection<V> {
      public Iterator<V> iterator() {
         return ConcurrentReferenceHashMap.this.new ValueIterator();
      }

      public int size() {
         return ConcurrentReferenceHashMap.this.size();
      }

      public boolean isEmpty() {
         return ConcurrentReferenceHashMap.this.isEmpty();
      }

      public boolean contains(Object o) {
         return ConcurrentReferenceHashMap.this.containsValue(o);
      }

      public void clear() {
         ConcurrentReferenceHashMap.this.clear();
      }
   }

   final class KeySet extends AbstractSet<K> {
      public Iterator<K> iterator() {
         return ConcurrentReferenceHashMap.this.new KeyIterator();
      }

      public int size() {
         return ConcurrentReferenceHashMap.this.size();
      }

      public boolean isEmpty() {
         return ConcurrentReferenceHashMap.this.isEmpty();
      }

      public boolean contains(Object o) {
         return ConcurrentReferenceHashMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         return ConcurrentReferenceHashMap.this.remove(o) != null;
      }

      public void clear() {
         ConcurrentReferenceHashMap.this.clear();
      }
   }

   final class EntryIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<Entry<K, V>> {
      EntryIterator() {
         super();
      }

      public Entry<K, V> next() {
         ConcurrentReferenceHashMap.HashEntry<K, V> e = super.nextEntry();
         return ConcurrentReferenceHashMap.this.new WriteThroughEntry(e.key(), e.value());
      }
   }

   final class WriteThroughEntry extends ConcurrentReferenceHashMap.SimpleEntry<K, V> {
      private static final long serialVersionUID = -7900634345345313646L;

      WriteThroughEntry(K k, V v) {
         super(k, v);
      }

      public V setValue(V value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            V v = super.setValue(value);
            ConcurrentReferenceHashMap.this.put(this.getKey(), value);
            return v;
         }
      }
   }

   static class SimpleEntry<K, V> implements Entry<K, V>, Serializable {
      private static final long serialVersionUID = -8499721149061103585L;
      private final K key;
      private V value;

      public SimpleEntry(K key, V value) {
         this.key = key;
         this.value = value;
      }

      public SimpleEntry(Entry<? extends K, ? extends V> entry) {
         this.key = entry.getKey();
         this.value = entry.getValue();
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         V oldValue = this.value;
         this.value = value;
         return oldValue;
      }

      public boolean equals(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return eq(this.key, e.getKey()) && eq(this.value, e.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.value;
      }

      private static boolean eq(Object o1, Object o2) {
         return o1 == null ? o2 == null : o1.equals(o2);
      }
   }

   final class ValueIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<V>, Enumeration<V> {
      ValueIterator() {
         super();
      }

      public V next() {
         return super.nextEntry().value();
      }

      public V nextElement() {
         return super.nextEntry().value();
      }
   }

   final class KeyIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<K>, Enumeration<K> {
      KeyIterator() {
         super();
      }

      public K next() {
         return super.nextEntry().key();
      }

      public K nextElement() {
         return super.nextEntry().key();
      }
   }

   abstract class HashIterator {
      int nextSegmentIndex;
      int nextTableIndex;
      ConcurrentReferenceHashMap.HashEntry<K, V>[] currentTable;
      ConcurrentReferenceHashMap.HashEntry<K, V> nextEntry;
      ConcurrentReferenceHashMap.HashEntry<K, V> lastReturned;
      K currentKey;

      HashIterator() {
         this.nextSegmentIndex = ConcurrentReferenceHashMap.this.segments.length - 1;
         this.nextTableIndex = -1;
         this.advance();
      }

      public boolean hasMoreElements() {
         return this.hasNext();
      }

      final void advance() {
         if (this.nextEntry == null || (this.nextEntry = this.nextEntry.next) == null) {
            while(this.nextTableIndex >= 0) {
               if ((this.nextEntry = this.currentTable[this.nextTableIndex--]) != null) {
                  return;
               }
            }

            while(true) {
               ConcurrentReferenceHashMap.Segment seg;
               do {
                  if (this.nextSegmentIndex < 0) {
                     return;
                  }

                  seg = ConcurrentReferenceHashMap.this.segments[this.nextSegmentIndex--];
               } while(seg.count == 0);

               this.currentTable = seg.table;

               for(int j = this.currentTable.length - 1; j >= 0; --j) {
                  if ((this.nextEntry = this.currentTable[j]) != null) {
                     this.nextTableIndex = j - 1;
                     return;
                  }
               }
            }
         }
      }

      public boolean hasNext() {
         while(this.nextEntry != null) {
            if (this.nextEntry.key() != null) {
               return true;
            }

            this.advance();
         }

         return false;
      }

      ConcurrentReferenceHashMap.HashEntry<K, V> nextEntry() {
         do {
            if (this.nextEntry == null) {
               throw new NoSuchElementException();
            }

            this.lastReturned = this.nextEntry;
            this.currentKey = this.lastReturned.key();
            this.advance();
         } while(this.currentKey == null);

         return this.lastReturned;
      }

      public void remove() {
         if (this.lastReturned == null) {
            throw new IllegalStateException();
         } else {
            ConcurrentReferenceHashMap.this.remove(this.currentKey);
            this.lastReturned = null;
         }
      }
   }

   static final class Segment<K, V> extends ReentrantLock implements Serializable {
      private static final long serialVersionUID = 2249069246763182397L;
      transient volatile int count;
      transient int modCount;
      transient int threshold;
      transient volatile ConcurrentReferenceHashMap.HashEntry<K, V>[] table;
      final float loadFactor;
      transient volatile ReferenceQueue<Object> refQueue;
      final ConcurrentReferenceHashMap.ReferenceType keyType;
      final ConcurrentReferenceHashMap.ReferenceType valueType;
      final boolean identityComparisons;

      Segment(int initialCapacity, float lf, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, boolean identityComparisons) {
         this.loadFactor = lf;
         this.keyType = keyType;
         this.valueType = valueType;
         this.identityComparisons = identityComparisons;
         this.setTable(ConcurrentReferenceHashMap.HashEntry.newArray(initialCapacity));
      }

      static final <K, V> ConcurrentReferenceHashMap.Segment<K, V>[] newArray(int i) {
         return new ConcurrentReferenceHashMap.Segment[i];
      }

      private boolean keyEq(Object src, Object dest) {
         return this.identityComparisons ? src == dest : src.equals(dest);
      }

      void setTable(ConcurrentReferenceHashMap.HashEntry<K, V>[] newTable) {
         this.threshold = (int)((float)newTable.length * this.loadFactor);
         this.table = newTable;
         this.refQueue = new ReferenceQueue();
      }

      ConcurrentReferenceHashMap.HashEntry<K, V> getFirst(int hash) {
         ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
         return tab[hash & tab.length - 1];
      }

      ConcurrentReferenceHashMap.HashEntry<K, V> newHashEntry(K key, int hash, ConcurrentReferenceHashMap.HashEntry<K, V> next, V value) {
         return new ConcurrentReferenceHashMap.HashEntry(key, hash, next, value, this.keyType, this.valueType, this.refQueue);
      }

      V readValueUnderLock(ConcurrentReferenceHashMap.HashEntry<K, V> e) {
         this.lock();

         Object var2;
         try {
            this.removeStale();
            var2 = e.value();
         } finally {
            this.unlock();
         }

         return var2;
      }

      V get(Object key, int hash) {
         if (this.count != 0) {
            for(ConcurrentReferenceHashMap.HashEntry e = this.getFirst(hash); e != null; e = e.next) {
               if (e.hash == hash && this.keyEq(key, e.key())) {
                  Object opaque = e.valueRef;
                  if (opaque != null) {
                     return e.dereferenceValue(opaque);
                  }

                  return this.readValueUnderLock(e);
               }
            }
         }

         return null;
      }

      boolean containsKey(Object key, int hash) {
         if (this.count != 0) {
            for(ConcurrentReferenceHashMap.HashEntry e = this.getFirst(hash); e != null; e = e.next) {
               if (e.hash == hash && this.keyEq(key, e.key())) {
                  return true;
               }
            }
         }

         return false;
      }

      boolean containsValue(Object value) {
         if (this.count != 0) {
            ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
            int len = tab.length;

            for(int i = 0; i < len; ++i) {
               for(ConcurrentReferenceHashMap.HashEntry e = tab[i]; e != null; e = e.next) {
                  Object opaque = e.valueRef;
                  Object v;
                  if (opaque == null) {
                     v = this.readValueUnderLock(e);
                  } else {
                     v = e.dereferenceValue(opaque);
                  }

                  if (value.equals(v)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }

      boolean replace(K key, int hash, V oldValue, V newValue) {
         this.lock();

         try {
            this.removeStale();

            ConcurrentReferenceHashMap.HashEntry e;
            for(e = this.getFirst(hash); e != null && (e.hash != hash || !this.keyEq(key, e.key())); e = e.next) {
            }

            boolean replaced = false;
            if (e != null && oldValue.equals(e.value())) {
               replaced = true;
               e.setValue(newValue, this.valueType, this.refQueue);
            }

            boolean var7 = replaced;
            return var7;
         } finally {
            this.unlock();
         }
      }

      V replace(K key, int hash, V newValue) {
         this.lock();

         Object var6;
         try {
            this.removeStale();

            ConcurrentReferenceHashMap.HashEntry e;
            for(e = this.getFirst(hash); e != null && (e.hash != hash || !this.keyEq(key, e.key())); e = e.next) {
            }

            V oldValue = null;
            if (e != null) {
               oldValue = e.value();
               e.setValue(newValue, this.valueType, this.refQueue);
            }

            var6 = oldValue;
         } finally {
            this.unlock();
         }

         return var6;
      }

      V put(K key, int hash, V value, boolean onlyIfAbsent) {
         this.lock();

         try {
            this.removeStale();
            int c = this.count;
            if (c++ > this.threshold) {
               int reduced = this.rehash();
               if (reduced > 0) {
                  this.count = (c -= reduced) - 1;
               }
            }

            ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
            int index = hash & tab.length - 1;
            ConcurrentReferenceHashMap.HashEntry<K, V> first = tab[index];

            ConcurrentReferenceHashMap.HashEntry e;
            for(e = first; e != null && (e.hash != hash || !this.keyEq(key, e.key())); e = e.next) {
            }

            Object oldValue;
            if (e != null) {
               oldValue = e.value();
               if (!onlyIfAbsent || oldValue == null) {
                  e.setValue(value, this.valueType, this.refQueue);
               }
            } else {
               oldValue = null;
               ++this.modCount;
               tab[index] = this.newHashEntry(key, hash, first, value);
               this.count = c;
            }

            Object var11 = oldValue;
            return var11;
         } finally {
            this.unlock();
         }
      }

      int rehash() {
         ConcurrentReferenceHashMap.HashEntry<K, V>[] oldTable = this.table;
         int oldCapacity = oldTable.length;
         if (oldCapacity >= 1073741824) {
            return 0;
         } else {
            ConcurrentReferenceHashMap.HashEntry<K, V>[] newTable = ConcurrentReferenceHashMap.HashEntry.newArray(oldCapacity << 1);
            this.threshold = (int)((float)newTable.length * this.loadFactor);
            int sizeMask = newTable.length - 1;
            int reduce = 0;

            for(int i = 0; i < oldCapacity; ++i) {
               ConcurrentReferenceHashMap.HashEntry<K, V> e = oldTable[i];
               if (e != null) {
                  ConcurrentReferenceHashMap.HashEntry<K, V> next = e.next;
                  int idx = e.hash & sizeMask;
                  if (next == null) {
                     newTable[idx] = e;
                  } else {
                     ConcurrentReferenceHashMap.HashEntry<K, V> lastRun = e;
                     int lastIdx = idx;

                     ConcurrentReferenceHashMap.HashEntry p;
                     for(p = next; p != null; p = p.next) {
                        int k = p.hash & sizeMask;
                        if (k != lastIdx) {
                           lastIdx = k;
                           lastRun = p;
                        }
                     }

                     newTable[lastIdx] = lastRun;

                     for(p = e; p != lastRun; p = p.next) {
                        K key = p.key();
                        if (key == null) {
                           ++reduce;
                        } else {
                           int k = p.hash & sizeMask;
                           ConcurrentReferenceHashMap.HashEntry<K, V> n = newTable[k];
                           newTable[k] = this.newHashEntry(key, p.hash, n, p.value());
                        }
                     }
                  }
               }
            }

            this.table = newTable;
            return reduce;
         }
      }

      V remove(Object key, int hash, Object value, boolean refRemove) {
         this.lock();

         Object v;
         try {
            if (!refRemove) {
               this.removeStale();
            }

            int c = this.count - 1;
            ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
            int index = hash & tab.length - 1;
            ConcurrentReferenceHashMap.HashEntry<K, V> first = tab[index];

            ConcurrentReferenceHashMap.HashEntry e;
            for(e = first; e != null && key != e.keyRef && (refRemove || hash != e.hash || !this.keyEq(key, e.key())); e = e.next) {
            }

            V oldValue = null;
            if (e != null) {
               v = e.value();
               if (value == null || value.equals(v)) {
                  oldValue = v;
                  ++this.modCount;
                  ConcurrentReferenceHashMap.HashEntry<K, V> newFirst = e.next;

                  for(ConcurrentReferenceHashMap.HashEntry p = first; p != e; p = p.next) {
                     K pKey = p.key();
                     if (pKey == null) {
                        --c;
                     } else {
                        newFirst = this.newHashEntry(pKey, p.hash, newFirst, p.value());
                     }
                  }

                  tab[index] = newFirst;
                  this.count = c;
               }
            }

            v = oldValue;
         } finally {
            this.unlock();
         }

         return v;
      }

      final void removeStale() {
         ConcurrentReferenceHashMap.KeyReference ref;
         while((ref = (ConcurrentReferenceHashMap.KeyReference)this.refQueue.poll()) != null) {
            this.remove(ref.keyRef(), ref.keyHash(), (Object)null, true);
         }

      }

      void clear() {
         if (this.count != 0) {
            this.lock();

            try {
               ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;

               for(int i = 0; i < tab.length; ++i) {
                  tab[i] = null;
               }

               ++this.modCount;
               this.refQueue = new ReferenceQueue();
               this.count = 0;
            } finally {
               this.unlock();
            }
         }

      }
   }

   static final class HashEntry<K, V> {
      final Object keyRef;
      final int hash;
      volatile Object valueRef;
      final ConcurrentReferenceHashMap.HashEntry<K, V> next;

      HashEntry(K key, int hash, ConcurrentReferenceHashMap.HashEntry<K, V> next, V value, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue) {
         this.hash = hash;
         this.next = next;
         this.keyRef = this.newKeyReference(key, keyType, refQueue);
         this.valueRef = this.newValueReference(value, valueType, refQueue);
      }

      final Object newKeyReference(K key, ConcurrentReferenceHashMap.ReferenceType keyType, ReferenceQueue<Object> refQueue) {
         if (keyType == ConcurrentReferenceHashMap.ReferenceType.WEAK) {
            return new ConcurrentReferenceHashMap.WeakKeyReference(key, this.hash, refQueue);
         } else {
            return keyType == ConcurrentReferenceHashMap.ReferenceType.SOFT ? new ConcurrentReferenceHashMap.SoftKeyReference(key, this.hash, refQueue) : key;
         }
      }

      final Object newValueReference(V value, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue) {
         if (valueType == ConcurrentReferenceHashMap.ReferenceType.WEAK) {
            return new ConcurrentReferenceHashMap.WeakValueReference(value, this.keyRef, this.hash, refQueue);
         } else {
            return valueType == ConcurrentReferenceHashMap.ReferenceType.SOFT ? new ConcurrentReferenceHashMap.SoftValueReference(value, this.keyRef, this.hash, refQueue) : value;
         }
      }

      final K key() {
         return this.keyRef instanceof ConcurrentReferenceHashMap.KeyReference ? ((Reference)this.keyRef).get() : this.keyRef;
      }

      final V value() {
         return this.dereferenceValue(this.valueRef);
      }

      final V dereferenceValue(Object value) {
         return value instanceof ConcurrentReferenceHashMap.KeyReference ? ((Reference)value).get() : value;
      }

      final void setValue(V value, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue) {
         this.valueRef = this.newValueReference(value, valueType, refQueue);
      }

      static final <K, V> ConcurrentReferenceHashMap.HashEntry<K, V>[] newArray(int i) {
         return new ConcurrentReferenceHashMap.HashEntry[i];
      }
   }

   static final class SoftValueReference<V> extends SoftReference<V> implements ConcurrentReferenceHashMap.KeyReference {
      final Object keyRef;
      final int hash;

      SoftValueReference(V value, Object keyRef, int hash, ReferenceQueue<Object> refQueue) {
         super(value, refQueue);
         this.keyRef = keyRef;
         this.hash = hash;
      }

      public final int keyHash() {
         return this.hash;
      }

      public final Object keyRef() {
         return this.keyRef;
      }
   }

   static final class WeakValueReference<V> extends WeakReference<V> implements ConcurrentReferenceHashMap.KeyReference {
      final Object keyRef;
      final int hash;

      WeakValueReference(V value, Object keyRef, int hash, ReferenceQueue<Object> refQueue) {
         super(value, refQueue);
         this.keyRef = keyRef;
         this.hash = hash;
      }

      public final int keyHash() {
         return this.hash;
      }

      public final Object keyRef() {
         return this.keyRef;
      }
   }

   static final class SoftKeyReference<K> extends SoftReference<K> implements ConcurrentReferenceHashMap.KeyReference {
      final int hash;

      SoftKeyReference(K key, int hash, ReferenceQueue<Object> refQueue) {
         super(key, refQueue);
         this.hash = hash;
      }

      public final int keyHash() {
         return this.hash;
      }

      public final Object keyRef() {
         return this;
      }
   }

   static final class WeakKeyReference<K> extends WeakReference<K> implements ConcurrentReferenceHashMap.KeyReference {
      final int hash;

      WeakKeyReference(K key, int hash, ReferenceQueue<Object> refQueue) {
         super(key, refQueue);
         this.hash = hash;
      }

      public final int keyHash() {
         return this.hash;
      }

      public final Object keyRef() {
         return this;
      }
   }

   interface KeyReference {
      int keyHash();

      Object keyRef();
   }

   public static enum Option {
      IDENTITY_COMPARISONS;
   }

   public static enum ReferenceType {
      STRONG,
      WEAK,
      SOFT;
   }
}
