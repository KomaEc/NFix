package org.jboss.util.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class FastCopyHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
   private static final Object NULL = new Object();
   private static final long serialVersionUID = 10929568968762L;
   private static final int DEFAULT_CAPACITY = 8;
   private static final int MAXIMUM_CAPACITY = 1073741824;
   private static final float DEFAULT_LOAD_FACTOR = 0.67F;
   private transient FastCopyHashMap.Entry<K, V>[] table;
   private transient int size;
   private transient int threshold;
   private final float loadFactor;
   private transient int modCount;
   private transient FastCopyHashMap<K, V>.KeySet keySet;
   private transient FastCopyHashMap<K, V>.Values values;
   private transient FastCopyHashMap<K, V>.EntrySet entrySet;

   public FastCopyHashMap(int initialCapacity, float loadFactor) {
      if (initialCapacity < 0) {
         throw new IllegalArgumentException("Can not have a negative size table!");
      } else {
         if (initialCapacity > 1073741824) {
            initialCapacity = 1073741824;
         }

         if (loadFactor > 0.0F && loadFactor <= 1.0F) {
            this.loadFactor = loadFactor;
            this.init(initialCapacity, loadFactor);
         } else {
            throw new IllegalArgumentException("Load factor must be greater than 0 and less than or equal to 1");
         }
      }
   }

   public FastCopyHashMap(Map<? extends K, ? extends V> map) {
      if (map instanceof FastCopyHashMap) {
         FastCopyHashMap<? extends K, ? extends V> fast = (FastCopyHashMap)map;
         this.table = (FastCopyHashMap.Entry[])((FastCopyHashMap.Entry[])fast.table.clone());
         this.loadFactor = fast.loadFactor;
         this.size = fast.size;
         this.threshold = fast.threshold;
      } else {
         this.loadFactor = 0.67F;
         this.init(map.size(), this.loadFactor);
         this.putAll(map);
      }

   }

   private void init(int initialCapacity, float loadFactor) {
      int c;
      for(c = 1; c < initialCapacity; c <<= 1) {
      }

      this.threshold = (int)((float)c * loadFactor);
      if (initialCapacity > this.threshold && c < 1073741824) {
         c <<= 1;
         this.threshold = (int)((float)c * loadFactor);
      }

      this.table = (FastCopyHashMap.Entry[])(new FastCopyHashMap.Entry[c]);
   }

   public FastCopyHashMap(int initialCapacity) {
      this(initialCapacity, 0.67F);
   }

   public FastCopyHashMap() {
      this(8);
   }

   private static final int hash(Object key) {
      int h = key.hashCode();
      h ^= h >>> 20 ^ h >>> 12;
      return h ^ h >>> 7 ^ h >>> 4;
   }

   private static final <K> K maskNull(K key) {
      return key == null ? NULL : key;
   }

   private static final <K> K unmaskNull(K key) {
      return key == NULL ? null : key;
   }

   private int nextIndex(int index, int length) {
      index = index >= length - 1 ? 0 : index + 1;
      return index;
   }

   private static final boolean eq(Object o1, Object o2) {
      return o1 == o2 || o1 != null && o1.equals(o2);
   }

   private static final int index(int hashCode, int length) {
      return hashCode & length - 1;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public V get(Object key) {
      key = maskNull(key);
      int hash = hash(key);
      int length = this.table.length;
      int index = index(hash, length);
      int start = index;

      do {
         FastCopyHashMap.Entry<K, V> e = this.table[index];
         if (e == null) {
            return null;
         }

         if (e.hash == hash && eq(key, e.key)) {
            return e.value;
         }

         index = this.nextIndex(index, length);
      } while(index != start);

      return null;
   }

   public boolean containsKey(Object key) {
      key = maskNull(key);
      int hash = hash(key);
      int length = this.table.length;
      int index = index(hash, length);
      int start = index;

      do {
         FastCopyHashMap.Entry<K, V> e = this.table[index];
         if (e == null) {
            return false;
         }

         if (e.hash == hash && eq(key, e.key)) {
            return true;
         }

         index = this.nextIndex(index, length);
      } while(index != start);

      return false;
   }

   public boolean containsValue(Object value) {
      FastCopyHashMap.Entry[] arr$ = this.table;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         FastCopyHashMap.Entry<K, V> e = arr$[i$];
         if (e != null && eq(value, e.value)) {
            return true;
         }
      }

      return false;
   }

   public V put(K key, V value) {
      key = maskNull(key);
      FastCopyHashMap.Entry<K, V>[] table = this.table;
      int hash = hash(key);
      int length = table.length;
      int index = index(hash, length);
      int start = index;

      do {
         FastCopyHashMap.Entry<K, V> e = table[index];
         if (e == null) {
            ++this.modCount;
            table[index] = new FastCopyHashMap.Entry(key, hash, value);
            if (++this.size >= this.threshold) {
               this.resize(length);
            }

            return null;
         }

         if (e.hash == hash && eq(key, e.key)) {
            table[index] = new FastCopyHashMap.Entry(e.key, e.hash, value);
            return e.value;
         }

         index = this.nextIndex(index, length);
      } while(index != start);

      throw new IllegalStateException("Table is full!");
   }

   private void resize(int from) {
      int newLength = from << 1;
      if (newLength <= 1073741824 && newLength > from) {
         FastCopyHashMap.Entry<K, V>[] newTable = new FastCopyHashMap.Entry[newLength];
         FastCopyHashMap.Entry<K, V>[] old = this.table;
         FastCopyHashMap.Entry[] arr$ = old;
         int len$ = old.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            FastCopyHashMap.Entry<K, V> e = arr$[i$];
            if (e != null) {
               int index;
               for(index = index(e.hash, newLength); newTable[index] != null; index = this.nextIndex(index, newLength)) {
               }

               newTable[index] = e;
            }
         }

         this.threshold = (int)(this.loadFactor * (float)newLength);
         this.table = newTable;
      }
   }

   public void putAll(Map<? extends K, ? extends V> map) {
      int size = map.size();
      if (size != 0) {
         if (size > this.threshold) {
            if (size > 1073741824) {
               size = 1073741824;
            }

            int length;
            for(length = this.table.length; length < size; length <<= 1) {
            }

            this.resize(length);
         }

         Iterator i$ = map.entrySet().iterator();

         while(i$.hasNext()) {
            java.util.Map.Entry<? extends K, ? extends V> e = (java.util.Map.Entry)i$.next();
            this.put(e.getKey(), e.getValue());
         }

      }
   }

   public V remove(Object key) {
      key = maskNull(key);
      FastCopyHashMap.Entry<K, V>[] table = this.table;
      int length = table.length;
      int hash = hash(key);
      int start = index(hash, length);
      int index = start;

      do {
         FastCopyHashMap.Entry<K, V> e = table[index];
         if (e == null) {
            return null;
         }

         if (e.hash == hash && eq(key, e.key)) {
            table[index] = null;
            this.relocate(index);
            ++this.modCount;
            --this.size;
            return e.value;
         }

         index = this.nextIndex(index, length);
      } while(index != start);

      return null;
   }

   private void relocate(int start) {
      FastCopyHashMap.Entry<K, V>[] table = this.table;
      int length = table.length;
      int current = this.nextIndex(start, length);

      while(true) {
         FastCopyHashMap.Entry<K, V> e = table[current];
         if (e == null) {
            return;
         }

         int prefer = index(e.hash, length);
         if (current < prefer && (prefer <= start || start <= current) || prefer <= start && start <= current) {
            table[start] = e;
            table[current] = null;
            start = current;
         }

         current = this.nextIndex(current, length);
      }
   }

   public void clear() {
      ++this.modCount;
      FastCopyHashMap.Entry<K, V>[] table = this.table;

      for(int i = 0; i < table.length; ++i) {
         table[i] = null;
      }

      this.size = 0;
   }

   public Object clone() {
      try {
         FastCopyHashMap<K, V> clone = (FastCopyHashMap)super.clone();
         clone.table = (FastCopyHashMap.Entry[])this.table.clone();
         clone.entrySet = null;
         clone.values = null;
         clone.keySet = null;
         return clone;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public void printDebugStats() {
      int optimal = 0;
      int total = 0;
      int totalSkew = 0;
      int maxSkew = 0;

      for(int i = 0; i < this.table.length; ++i) {
         FastCopyHashMap.Entry<K, V> e = this.table[i];
         if (e != null) {
            ++total;
            int target = index(e.hash, this.table.length);
            if (i == target) {
               ++optimal;
            } else {
               int skew = Math.abs(i - target);
               if (skew > maxSkew) {
                  maxSkew = skew;
               }

               totalSkew += skew;
            }
         }
      }

      System.out.println(" Size:             " + this.size);
      System.out.println(" Real Size:        " + total);
      System.out.println(" Optimal:          " + optimal + " (" + (float)optimal * 100.0F / (float)total + "%)");
      System.out.println(" Average Distance: " + (float)totalSkew / (float)(total - optimal));
      System.out.println(" Max Distance:     " + maxSkew);
   }

   public Set<java.util.Map.Entry<K, V>> entrySet() {
      if (this.entrySet == null) {
         this.entrySet = new FastCopyHashMap.EntrySet();
      }

      return this.entrySet;
   }

   public Set<K> keySet() {
      if (this.keySet == null) {
         this.keySet = new FastCopyHashMap.KeySet();
      }

      return this.keySet;
   }

   public Collection<V> values() {
      if (this.values == null) {
         this.values = new FastCopyHashMap.Values();
      }

      return this.values;
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      int size = s.readInt();
      this.init(size, this.loadFactor);

      for(int i = 0; i < size; ++i) {
         K key = s.readObject();
         V value = s.readObject();
         this.putForCreate(key, value);
      }

      this.size = size;
   }

   private void putForCreate(K key, V value) {
      key = maskNull(key);
      FastCopyHashMap.Entry<K, V>[] table = this.table;
      int hash = hash(key);
      int length = table.length;
      int index = index(hash, length);

      for(FastCopyHashMap.Entry e = table[index]; e != null; e = table[index]) {
         index = this.nextIndex(index, length);
      }

      table[index] = new FastCopyHashMap.Entry(key, hash, value);
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeInt(this.size);
      FastCopyHashMap.Entry[] arr$ = this.table;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         FastCopyHashMap.Entry<K, V> e = arr$[i$];
         if (e != null) {
            s.writeObject(unmaskNull(e.key));
            s.writeObject(e.value);
         }
      }

   }

   protected static class SimpleEntry<K, V> implements java.util.Map.Entry<K, V> {
      private K key;
      private V value;

      SimpleEntry(K key, V value) {
         this.key = key;
         this.value = value;
      }

      SimpleEntry(java.util.Map.Entry<K, V> entry) {
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
         V old = this.value;
         this.value = value;
         return old;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            return FastCopyHashMap.eq(this.key, e.getKey()) && FastCopyHashMap.eq(this.value, e.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : FastCopyHashMap.hash(this.key)) ^ (this.value == null ? 0 : FastCopyHashMap.hash(this.value));
      }

      public String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   private class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
      private EntrySet() {
      }

      public Iterator<java.util.Map.Entry<K, V>> iterator() {
         return FastCopyHashMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
            Object value = FastCopyHashMap.this.get(entry.getKey());
            return FastCopyHashMap.eq(entry.getValue(), value);
         }
      }

      public void clear() {
         FastCopyHashMap.this.clear();
      }

      public boolean isEmpty() {
         return FastCopyHashMap.this.isEmpty();
      }

      public int size() {
         return FastCopyHashMap.this.size();
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   private class Values extends AbstractCollection<V> {
      private Values() {
      }

      public Iterator<V> iterator() {
         return FastCopyHashMap.this.new ValueIterator();
      }

      public void clear() {
         FastCopyHashMap.this.clear();
      }

      public int size() {
         return FastCopyHashMap.this.size();
      }

      // $FF: synthetic method
      Values(Object x1) {
         this();
      }
   }

   private class KeySet extends AbstractSet<K> {
      private KeySet() {
      }

      public Iterator<K> iterator() {
         return FastCopyHashMap.this.new KeyIterator();
      }

      public void clear() {
         FastCopyHashMap.this.clear();
      }

      public boolean contains(Object o) {
         return FastCopyHashMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         int size = this.size();
         FastCopyHashMap.this.remove(o);
         return this.size() < size;
      }

      public int size() {
         return FastCopyHashMap.this.size();
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }

   private class EntryIterator extends FastCopyHashMap<K, V>.FastCopyHashMapIterator<java.util.Map.Entry<K, V>> {
      private EntryIterator() {
         super(null);
      }

      public java.util.Map.Entry<K, V> next() {
         FastCopyHashMap.Entry<K, V> e = this.nextEntry();
         return new FastCopyHashMap.EntryIterator.WriteThroughEntry(FastCopyHashMap.unmaskNull(e.key), e.value);
      }

      // $FF: synthetic method
      EntryIterator(Object x1) {
         this();
      }

      private class WriteThroughEntry extends FastCopyHashMap.SimpleEntry<K, V> {
         WriteThroughEntry(K key, V value) {
            super(key, value);
         }

         public V setValue(V value) {
            if (EntryIterator.this.table != FastCopyHashMap.this.table) {
               FastCopyHashMap.this.put(this.getKey(), value);
            }

            return super.setValue(value);
         }
      }
   }

   private class ValueIterator extends FastCopyHashMap<K, V>.FastCopyHashMapIterator<V> {
      private ValueIterator() {
         super(null);
      }

      public V next() {
         return this.nextEntry().value;
      }

      // $FF: synthetic method
      ValueIterator(Object x1) {
         this();
      }
   }

   private class KeyIterator extends FastCopyHashMap<K, V>.FastCopyHashMapIterator<K> {
      private KeyIterator() {
         super(null);
      }

      public K next() {
         return FastCopyHashMap.unmaskNull(this.nextEntry().key);
      }

      // $FF: synthetic method
      KeyIterator(Object x1) {
         this();
      }
   }

   private abstract class FastCopyHashMapIterator<E> implements Iterator<E> {
      private int next;
      private int expectedCount;
      private int current;
      private boolean hasNext;
      FastCopyHashMap.Entry<K, V>[] table;

      private FastCopyHashMapIterator() {
         this.next = 0;
         this.expectedCount = FastCopyHashMap.this.modCount;
         this.current = -1;
         this.table = FastCopyHashMap.this.table;
      }

      public boolean hasNext() {
         if (this.hasNext) {
            return true;
         } else {
            FastCopyHashMap.Entry<K, V>[] table = this.table;

            for(int i = this.next; i < table.length; ++i) {
               if (table[i] != null) {
                  this.next = i;
                  return this.hasNext = true;
               }
            }

            this.next = table.length;
            return false;
         }
      }

      protected FastCopyHashMap.Entry<K, V> nextEntry() {
         if (FastCopyHashMap.this.modCount != this.expectedCount) {
            throw new ConcurrentModificationException();
         } else if (!this.hasNext && !this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.current = this.next++;
            this.hasNext = false;
            return this.table[this.current];
         }
      }

      public void remove() {
         if (FastCopyHashMap.this.modCount != this.expectedCount) {
            throw new ConcurrentModificationException();
         } else {
            int current = this.current;
            int delete = current;
            if (current == -1) {
               throw new IllegalStateException();
            } else {
               this.current = -1;
               this.next = current;
               FastCopyHashMap.Entry<K, V>[] table = this.table;
               if (table != FastCopyHashMap.this.table) {
                  FastCopyHashMap.this.remove(table[current].key);
                  table[current] = null;
                  this.expectedCount = FastCopyHashMap.this.modCount;
               } else {
                  int length = table.length;
                  int i = current;
                  table[current] = null;
                  FastCopyHashMap.this.size--;

                  while(true) {
                     FastCopyHashMap.Entry e;
                     int prefer;
                     do {
                        i = FastCopyHashMap.this.nextIndex(i, length);
                        e = table[i];
                        if (e == null) {
                           return;
                        }

                        prefer = FastCopyHashMap.index(e.hash, length);
                     } while((i >= prefer || prefer > delete && delete > i) && (prefer > delete || delete > i));

                     if (i < current && current <= delete && table == FastCopyHashMap.this.table) {
                        int remaining = length - current;
                        FastCopyHashMap.Entry<K, V>[] newTable = (FastCopyHashMap.Entry[])(new FastCopyHashMap.Entry[remaining]);
                        System.arraycopy(table, current, newTable, 0, remaining);
                        this.table = newTable;
                        this.next = 0;
                     }

                     table[delete] = e;
                     table[i] = null;
                     delete = i;
                  }
               }
            }
         }
      }

      // $FF: synthetic method
      FastCopyHashMapIterator(Object x1) {
         this();
      }
   }

   private static final class Entry<K, V> {
      final K key;
      final int hash;
      final V value;

      Entry(K key, int hash, V value) {
         this.key = key;
         this.hash = hash;
         this.value = value;
      }
   }
}
