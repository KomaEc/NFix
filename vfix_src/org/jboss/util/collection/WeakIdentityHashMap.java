package org.jboss.util.collection;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class WeakIdentityHashMap implements Map {
   private static final int DEFAULT_INITIAL_CAPACITY = 16;
   private static final int MAXIMUM_CAPACITY = 1073741824;
   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
   private WeakIdentityHashMap.Entry[] table;
   private int size;
   private int threshold;
   private final float loadFactor;
   private final ReferenceQueue queue;
   private volatile int modCount;
   transient volatile Set keySet;
   transient volatile Collection values;
   private static final Object NULL_KEY = new Object();
   private transient Set entrySet;

   public WeakIdentityHashMap(int initialCapacity, float loadFactor) {
      this.queue = new ReferenceQueue();
      this.keySet = null;
      this.values = null;
      this.entrySet = null;
      if (initialCapacity < 0) {
         throw new IllegalArgumentException("Illegal Initial Capacity: " + initialCapacity);
      } else {
         if (initialCapacity > 1073741824) {
            initialCapacity = 1073741824;
         }

         if (loadFactor > 0.0F && !Float.isNaN(loadFactor)) {
            int capacity;
            for(capacity = 1; capacity < initialCapacity; capacity <<= 1) {
            }

            this.table = new WeakIdentityHashMap.Entry[capacity];
            this.loadFactor = loadFactor;
            this.threshold = (int)((float)capacity * loadFactor);
         } else {
            throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
         }
      }
   }

   public WeakIdentityHashMap(int initialCapacity) {
      this(initialCapacity, 0.75F);
   }

   public WeakIdentityHashMap() {
      this.queue = new ReferenceQueue();
      this.keySet = null;
      this.values = null;
      this.entrySet = null;
      this.loadFactor = 0.75F;
      this.threshold = 16;
      this.table = new WeakIdentityHashMap.Entry[16];
   }

   public WeakIdentityHashMap(Map t) {
      this(Math.max((int)((float)t.size() / 0.75F) + 1, 16), 0.75F);
      this.putAll(t);
   }

   private static Object maskNull(Object key) {
      return key == null ? NULL_KEY : key;
   }

   private static Object unmaskNull(Object key) {
      return key == NULL_KEY ? null : key;
   }

   int hash(Object x) {
      int h = System.identityHashCode(x);
      return h - (h << 7);
   }

   static int indexFor(int h, int length) {
      return h & length - 1;
   }

   private void expungeStaleEntries() {
      label25:
      while(true) {
         Reference r;
         if ((r = this.queue.poll()) != null) {
            WeakIdentityHashMap.Entry e = (WeakIdentityHashMap.Entry)r;
            int h = e.hash;
            int i = indexFor(h, this.table.length);
            WeakIdentityHashMap.Entry prev = this.table[i];
            WeakIdentityHashMap.Entry p = prev;

            while(true) {
               if (p == null) {
                  continue label25;
               }

               WeakIdentityHashMap.Entry next = p.next;
               if (p == e) {
                  if (prev == e) {
                     this.table[i] = next;
                  } else {
                     prev.next = next;
                  }

                  e.next = null;
                  e.value = null;
                  --this.size;
                  continue label25;
               }

               prev = p;
               p = next;
            }
         }

         return;
      }
   }

   private WeakIdentityHashMap.Entry[] getTable() {
      this.expungeStaleEntries();
      return this.table;
   }

   public int size() {
      if (this.size == 0) {
         return 0;
      } else {
         this.expungeStaleEntries();
         return this.size;
      }
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public Object get(Object key) {
      Object k = maskNull(key);
      int h = this.hash(k);
      WeakIdentityHashMap.Entry[] tab = this.getTable();
      int index = indexFor(h, tab.length);

      for(WeakIdentityHashMap.Entry e = tab[index]; e != null; e = e.next) {
         if (e.hash == h && k == e.get()) {
            return e.value;
         }
      }

      return null;
   }

   public boolean containsKey(Object key) {
      return this.getEntry(key) != null;
   }

   WeakIdentityHashMap.Entry getEntry(Object key) {
      Object k = maskNull(key);
      int h = this.hash(k);
      WeakIdentityHashMap.Entry[] tab = this.getTable();
      int index = indexFor(h, tab.length);

      WeakIdentityHashMap.Entry e;
      for(e = tab[index]; e != null && (e.hash != h || k != e.get()); e = e.next) {
      }

      return e;
   }

   public Object put(Object key, Object value) {
      Object k = maskNull(key);
      int h = this.hash(k);
      WeakIdentityHashMap.Entry[] tab = this.getTable();
      int i = indexFor(h, tab.length);

      for(WeakIdentityHashMap.Entry e = tab[i]; e != null; e = e.next) {
         if (h == e.hash && k == e.get()) {
            Object oldValue = e.value;
            if (value != oldValue) {
               e.value = value;
            }

            return oldValue;
         }
      }

      ++this.modCount;
      tab[i] = new WeakIdentityHashMap.Entry(k, value, this.queue, h, tab[i]);
      if (++this.size >= this.threshold) {
         this.resize(tab.length * 2);
      }

      return null;
   }

   void resize(int newCapacity) {
      WeakIdentityHashMap.Entry[] oldTable = this.getTable();
      int oldCapacity = oldTable.length;
      if (this.size >= this.threshold && oldCapacity <= newCapacity) {
         WeakIdentityHashMap.Entry[] newTable = new WeakIdentityHashMap.Entry[newCapacity];
         this.transfer(oldTable, newTable);
         this.table = newTable;
         if (this.size >= this.threshold / 2) {
            this.threshold = (int)((float)newCapacity * this.loadFactor);
         } else {
            this.expungeStaleEntries();
            this.transfer(newTable, oldTable);
            this.table = oldTable;
         }

      }
   }

   private void transfer(WeakIdentityHashMap.Entry[] src, WeakIdentityHashMap.Entry[] dest) {
      for(int j = 0; j < src.length; ++j) {
         WeakIdentityHashMap.Entry e = src[j];

         WeakIdentityHashMap.Entry next;
         for(src[j] = null; e != null; e = next) {
            next = e.next;
            Object key = e.get();
            if (key == null) {
               e.next = null;
               e.value = null;
               --this.size;
            } else {
               int i = indexFor(e.hash, dest.length);
               e.next = dest[i];
               dest[i] = e;
            }
         }
      }

   }

   public void putAll(Map t) {
      int n = t.size();
      if (n != 0) {
         if (n >= this.threshold) {
            n = (int)((float)n / this.loadFactor + 1.0F);
            if (n > 1073741824) {
               n = 1073741824;
            }

            int capacity;
            for(capacity = this.table.length; capacity < n; capacity <<= 1) {
            }

            this.resize(capacity);
         }

         Iterator i = t.entrySet().iterator();

         while(i.hasNext()) {
            java.util.Map.Entry e = (java.util.Map.Entry)i.next();
            this.put(e.getKey(), e.getValue());
         }

      }
   }

   public Object remove(Object key) {
      Object k = maskNull(key);
      int h = this.hash(k);
      WeakIdentityHashMap.Entry[] tab = this.getTable();
      int i = indexFor(h, tab.length);
      WeakIdentityHashMap.Entry prev = tab[i];

      WeakIdentityHashMap.Entry next;
      for(WeakIdentityHashMap.Entry e = prev; e != null; e = next) {
         next = e.next;
         if (h == e.hash && k == e.get()) {
            ++this.modCount;
            --this.size;
            if (prev == e) {
               tab[i] = next;
            } else {
               prev.next = next;
            }

            return e.value;
         }

         prev = e;
      }

      return null;
   }

   WeakIdentityHashMap.Entry removeMapping(Object o) {
      if (!(o instanceof java.util.Map.Entry)) {
         return null;
      } else {
         WeakIdentityHashMap.Entry[] tab = this.getTable();
         java.util.Map.Entry entry = (java.util.Map.Entry)o;
         Object k = maskNull(entry.getKey());
         int h = this.hash(k);
         int i = indexFor(h, tab.length);
         WeakIdentityHashMap.Entry prev = tab[i];

         WeakIdentityHashMap.Entry next;
         for(WeakIdentityHashMap.Entry e = prev; e != null; e = next) {
            next = e.next;
            if (h == e.hash && e.equals(entry)) {
               ++this.modCount;
               --this.size;
               if (prev == e) {
                  tab[i] = next;
               } else {
                  prev.next = next;
               }

               return e;
            }

            prev = e;
         }

         return null;
      }
   }

   public void clear() {
      while(this.queue.poll() != null) {
      }

      ++this.modCount;
      WeakIdentityHashMap.Entry[] tab = this.table;

      for(int i = 0; i < tab.length; ++i) {
         tab[i] = null;
      }

      this.size = 0;

      while(this.queue.poll() != null) {
      }

   }

   public boolean containsValue(Object value) {
      if (value == null) {
         return this.containsNullValue();
      } else {
         WeakIdentityHashMap.Entry[] tab = this.getTable();
         int i = tab.length;

         while(i-- > 0) {
            for(WeakIdentityHashMap.Entry e = tab[i]; e != null; e = e.next) {
               if (value.equals(e.value)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean containsNullValue() {
      WeakIdentityHashMap.Entry[] tab = this.getTable();
      int i = tab.length;

      while(i-- > 0) {
         for(WeakIdentityHashMap.Entry e = tab[i]; e != null; e = e.next) {
            if (e.value == null) {
               return true;
            }
         }
      }

      return false;
   }

   public Set keySet() {
      Set ks = this.keySet;
      return ks != null ? ks : (this.keySet = new WeakIdentityHashMap.KeySet());
   }

   public Collection values() {
      Collection vs = this.values;
      return vs != null ? vs : (this.values = new WeakIdentityHashMap.Values());
   }

   public Set entrySet() {
      Set es = this.entrySet;
      return es != null ? es : (this.entrySet = new WeakIdentityHashMap.EntrySet());
   }

   static class SimpleEntry implements java.util.Map.Entry {
      Object key;
      Object value;

      public SimpleEntry(Object key, Object value) {
         this.key = key;
         this.value = value;
      }

      public SimpleEntry(java.util.Map.Entry e) {
         this.key = e.getKey();
         this.value = e.getValue();
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object value) {
         Object oldValue = this.value;
         this.value = value;
         return oldValue;
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry e = (java.util.Map.Entry)o;
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

   private class EntrySet extends AbstractSet {
      private EntrySet() {
      }

      public Iterator iterator() {
         return WeakIdentityHashMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry e = (java.util.Map.Entry)o;
            WeakIdentityHashMap.Entry candidate = WeakIdentityHashMap.this.getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
         }
      }

      public boolean remove(Object o) {
         return WeakIdentityHashMap.this.removeMapping(o) != null;
      }

      public int size() {
         return WeakIdentityHashMap.this.size();
      }

      public void clear() {
         WeakIdentityHashMap.this.clear();
      }

      public Object[] toArray() {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(new WeakIdentityHashMap.SimpleEntry((java.util.Map.Entry)i.next()));
         }

         return c.toArray();
      }

      public Object[] toArray(Object[] a) {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(new WeakIdentityHashMap.SimpleEntry((java.util.Map.Entry)i.next()));
         }

         return c.toArray(a);
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   private class Values extends AbstractCollection {
      private Values() {
      }

      public Iterator iterator() {
         return WeakIdentityHashMap.this.new ValueIterator();
      }

      public int size() {
         return WeakIdentityHashMap.this.size();
      }

      public boolean contains(Object o) {
         return WeakIdentityHashMap.this.containsValue(o);
      }

      public void clear() {
         WeakIdentityHashMap.this.clear();
      }

      public Object[] toArray() {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray();
      }

      public Object[] toArray(Object[] a) {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray(a);
      }

      // $FF: synthetic method
      Values(Object x1) {
         this();
      }
   }

   private class KeySet extends AbstractSet {
      private KeySet() {
      }

      public Iterator iterator() {
         return WeakIdentityHashMap.this.new KeyIterator();
      }

      public int size() {
         return WeakIdentityHashMap.this.size();
      }

      public boolean contains(Object o) {
         return WeakIdentityHashMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         if (WeakIdentityHashMap.this.containsKey(o)) {
            WeakIdentityHashMap.this.remove(o);
            return true;
         } else {
            return false;
         }
      }

      public void clear() {
         WeakIdentityHashMap.this.clear();
      }

      public Object[] toArray() {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray();
      }

      public Object[] toArray(Object[] a) {
         Collection c = new ArrayList(this.size());
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray(a);
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }

   private class EntryIterator extends WeakIdentityHashMap.HashIterator {
      private EntryIterator() {
         super();
      }

      public Object next() {
         return this.nextEntry();
      }

      // $FF: synthetic method
      EntryIterator(Object x1) {
         this();
      }
   }

   private class KeyIterator extends WeakIdentityHashMap.HashIterator {
      private KeyIterator() {
         super();
      }

      public Object next() {
         return this.nextEntry().getKey();
      }

      // $FF: synthetic method
      KeyIterator(Object x1) {
         this();
      }
   }

   private class ValueIterator extends WeakIdentityHashMap.HashIterator {
      private ValueIterator() {
         super();
      }

      public Object next() {
         return this.nextEntry().value;
      }

      // $FF: synthetic method
      ValueIterator(Object x1) {
         this();
      }
   }

   private abstract class HashIterator implements Iterator {
      int index;
      WeakIdentityHashMap.Entry entry = null;
      WeakIdentityHashMap.Entry lastReturned = null;
      int expectedModCount;
      Object nextKey;
      Object currentKey;

      HashIterator() {
         this.expectedModCount = WeakIdentityHashMap.this.modCount;
         this.nextKey = null;
         this.currentKey = null;
         this.index = WeakIdentityHashMap.this.size() != 0 ? WeakIdentityHashMap.this.table.length : 0;
      }

      public boolean hasNext() {
         WeakIdentityHashMap.Entry[] t = WeakIdentityHashMap.this.table;

         while(this.nextKey == null) {
            WeakIdentityHashMap.Entry e = this.entry;

            int i;
            for(i = this.index; e == null && i > 0; e = t[i]) {
               --i;
            }

            this.entry = e;
            this.index = i;
            if (e == null) {
               this.currentKey = null;
               return false;
            }

            this.nextKey = e.get();
            if (this.nextKey == null) {
               this.entry = this.entry.next;
            }
         }

         return true;
      }

      protected WeakIdentityHashMap.Entry nextEntry() {
         if (WeakIdentityHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else if (this.nextKey == null && !this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.entry;
            this.entry = this.entry.next;
            this.currentKey = this.nextKey;
            this.nextKey = null;
            return this.lastReturned;
         }
      }

      public void remove() {
         if (this.lastReturned == null) {
            throw new IllegalStateException();
         } else if (WeakIdentityHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else {
            WeakIdentityHashMap.this.remove(this.currentKey);
            this.expectedModCount = WeakIdentityHashMap.this.modCount;
            this.lastReturned = null;
            this.currentKey = null;
         }
      }
   }

   private static class Entry extends WeakReference implements java.util.Map.Entry {
      private Object value;
      private final int hash;
      private WeakIdentityHashMap.Entry next;

      Entry(Object key, Object value, ReferenceQueue queue, int hash, WeakIdentityHashMap.Entry next) {
         super(key, queue);
         this.value = value;
         this.hash = hash;
         this.next = next;
      }

      public Object getKey() {
         return WeakIdentityHashMap.unmaskNull(this.get());
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object newValue) {
         Object oldValue = this.value;
         this.value = newValue;
         return oldValue;
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry e = (java.util.Map.Entry)o;
            Object k1 = this.getKey();
            Object k2 = e.getKey();
            if (k1 == k2) {
               Object v1 = this.getValue();
               Object v2 = e.getValue();
               if (v1 == v2 || v1 != null && v1.equals(v2)) {
                  return true;
               }
            }

            return false;
         }
      }

      public int hashCode() {
         Object k = this.getKey();
         Object v = this.getValue();
         return (k == null ? 0 : System.identityHashCode(k)) ^ (v == null ? 0 : v.hashCode());
      }

      public String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }
}
