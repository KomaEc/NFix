package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractCollection;
import edu.emory.mathcs.backport.java.util.AbstractMap;
import edu.emory.mathcs.backport.java.util.AbstractSet;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

public class ConcurrentHashMap extends AbstractMap implements ConcurrentMap, Serializable {
   private static final long serialVersionUID = 7249069246763182397L;
   static final int DEFAULT_INITIAL_CAPACITY = 16;
   static final float DEFAULT_LOAD_FACTOR = 0.75F;
   static final int DEFAULT_CONCURRENCY_LEVEL = 16;
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int RETRIES_BEFORE_LOCK = 2;
   final int segmentMask;
   final int segmentShift;
   final ConcurrentHashMap.Segment[] segments;
   transient Set keySet;
   transient Set entrySet;
   transient Collection values;

   private static int hash(int h) {
      h += h << 15 ^ -12931;
      h ^= h >>> 10;
      h += h << 3;
      h ^= h >>> 6;
      h += (h << 2) + (h << 14);
      return h ^ h >>> 16;
   }

   final ConcurrentHashMap.Segment segmentFor(int hash) {
      return this.segments[hash >>> this.segmentShift & this.segmentMask];
   }

   public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
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
         this.segments = ConcurrentHashMap.Segment.newArray(ssize);
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

         for(int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new ConcurrentHashMap.Segment(cap, loadFactor);
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public ConcurrentHashMap(int initialCapacity, float loadFactor) {
      this(initialCapacity, loadFactor, 16);
   }

   public ConcurrentHashMap(int initialCapacity) {
      this(initialCapacity, 0.75F, 16);
   }

   public ConcurrentHashMap() {
      this(16, 0.75F, 16);
   }

   public ConcurrentHashMap(Map m) {
      this(Math.max((int)((float)m.size() / 0.75F) + 1, 16), 0.75F, 16);
      this.putAll(m);
   }

   public boolean isEmpty() {
      ConcurrentHashMap.Segment[] segments = this.segments;
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
      ConcurrentHashMap.Segment[] segments = this.segments;
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

   public Object get(Object key) {
      int hash = hash(key.hashCode());
      return this.segmentFor(hash).get(key, hash);
   }

   public boolean containsKey(Object key) {
      int hash = hash(key.hashCode());
      return this.segmentFor(hash).containsKey(key, hash);
   }

   public boolean containsValue(Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         ConcurrentHashMap.Segment[] segments = this.segments;
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

         try {
            for(int i = 0; i < segments.length; ++i) {
               if (segments[i].containsValue(value)) {
                  found = true;
                  break;
               }
            }
         } finally {
            for(int i = 0; i < segments.length; ++i) {
               segments[i].unlock();
            }

         }

         return found;
      }
   }

   public boolean contains(Object value) {
      return this.containsValue(value);
   }

   public Object put(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = hash(key.hashCode());
         return this.segmentFor(hash).put(key, hash, value, false);
      }
   }

   public Object putIfAbsent(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = hash(key.hashCode());
         return this.segmentFor(hash).put(key, hash, value, true);
      }
   }

   public void putAll(Map m) {
      Iterator it = m.entrySet().iterator();

      while(it.hasNext()) {
         Entry e = (Entry)it.next();
         this.put(e.getKey(), e.getValue());
      }

   }

   public Object remove(Object key) {
      int hash = hash(key.hashCode());
      return this.segmentFor(hash).remove(key, hash, (Object)null);
   }

   public boolean remove(Object key, Object value) {
      if (value == null) {
         return false;
      } else {
         int hash = hash(key.hashCode());
         return this.segmentFor(hash).remove(key, hash, value) != null;
      }
   }

   public boolean replace(Object key, Object oldValue, Object newValue) {
      if (oldValue != null && newValue != null) {
         int hash = hash(key.hashCode());
         return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
      } else {
         throw new NullPointerException();
      }
   }

   public Object replace(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = hash(key.hashCode());
         return this.segmentFor(hash).replace(key, hash, value);
      }
   }

   public void clear() {
      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].clear();
      }

   }

   public Set keySet() {
      Set ks = this.keySet;
      return ks != null ? ks : (this.keySet = new ConcurrentHashMap.KeySet());
   }

   public Collection values() {
      Collection vs = this.values;
      return vs != null ? vs : (this.values = new ConcurrentHashMap.Values());
   }

   public Set entrySet() {
      Set es = this.entrySet;
      return es != null ? es : (this.entrySet = new ConcurrentHashMap.EntrySet());
   }

   public Enumeration keys() {
      return new ConcurrentHashMap.KeyIterator();
   }

   public Enumeration elements() {
      return new ConcurrentHashMap.ValueIterator();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for(int k = 0; k < this.segments.length; ++k) {
         ConcurrentHashMap.Segment seg = this.segments[k];
         seg.lock();

         try {
            ConcurrentHashMap.HashEntry[] tab = seg.table;

            for(int i = 0; i < tab.length; ++i) {
               for(ConcurrentHashMap.HashEntry e = tab[i]; e != null; e = e.next) {
                  s.writeObject(e.key);
                  s.writeObject(e.value);
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
         this.segments[i].setTable(new ConcurrentHashMap.HashEntry[1]);
      }

      while(true) {
         Object key = s.readObject();
         Object value = s.readObject();
         if (key == null) {
            return;
         }

         this.put(key, value);
      }
   }

   final class EntrySet extends AbstractSet {
      public Iterator iterator() {
         return ConcurrentHashMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            Object v = ConcurrentHashMap.this.get(e.getKey());
            return v != null && v.equals(e.getValue());
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return ConcurrentHashMap.this.remove(e.getKey(), e.getValue());
         }
      }

      public int size() {
         return ConcurrentHashMap.this.size();
      }

      public void clear() {
         ConcurrentHashMap.this.clear();
      }
   }

   final class Values extends AbstractCollection {
      public Iterator iterator() {
         return ConcurrentHashMap.this.new ValueIterator();
      }

      public int size() {
         return ConcurrentHashMap.this.size();
      }

      public boolean contains(Object o) {
         return ConcurrentHashMap.this.containsValue(o);
      }

      public void clear() {
         ConcurrentHashMap.this.clear();
      }
   }

   final class KeySet extends AbstractSet {
      public Iterator iterator() {
         return ConcurrentHashMap.this.new KeyIterator();
      }

      public int size() {
         return ConcurrentHashMap.this.size();
      }

      public boolean contains(Object o) {
         return ConcurrentHashMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         return ConcurrentHashMap.this.remove(o) != null;
      }

      public void clear() {
         ConcurrentHashMap.this.clear();
      }
   }

   final class EntryIterator extends ConcurrentHashMap.HashIterator implements Iterator {
      EntryIterator() {
         super();
      }

      public Object next() {
         ConcurrentHashMap.HashEntry e = super.nextEntry();
         return ConcurrentHashMap.this.new WriteThroughEntry(e.key, e.value);
      }
   }

   final class WriteThroughEntry extends AbstractMap.SimpleEntry {
      WriteThroughEntry(Object k, Object v) {
         super(k, v);
      }

      public Object setValue(Object value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            Object v = super.setValue(value);
            ConcurrentHashMap.this.put(this.getKey(), value);
            return v;
         }
      }
   }

   final class ValueIterator extends ConcurrentHashMap.HashIterator implements Iterator, Enumeration {
      ValueIterator() {
         super();
      }

      public Object next() {
         return super.nextEntry().value;
      }

      public Object nextElement() {
         return super.nextEntry().value;
      }
   }

   final class KeyIterator extends ConcurrentHashMap.HashIterator implements Iterator, Enumeration {
      KeyIterator() {
         super();
      }

      public Object next() {
         return super.nextEntry().key;
      }

      public Object nextElement() {
         return super.nextEntry().key;
      }
   }

   abstract class HashIterator {
      int nextSegmentIndex;
      int nextTableIndex;
      ConcurrentHashMap.HashEntry[] currentTable;
      ConcurrentHashMap.HashEntry nextEntry;
      ConcurrentHashMap.HashEntry lastReturned;

      HashIterator() {
         this.nextSegmentIndex = ConcurrentHashMap.this.segments.length - 1;
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
               ConcurrentHashMap.Segment seg;
               do {
                  if (this.nextSegmentIndex < 0) {
                     return;
                  }

                  seg = ConcurrentHashMap.this.segments[this.nextSegmentIndex--];
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
         return this.nextEntry != null;
      }

      ConcurrentHashMap.HashEntry nextEntry() {
         if (this.nextEntry == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.nextEntry;
            this.advance();
            return this.lastReturned;
         }
      }

      public void remove() {
         if (this.lastReturned == null) {
            throw new IllegalStateException();
         } else {
            ConcurrentHashMap.this.remove(this.lastReturned.key);
            this.lastReturned = null;
         }
      }
   }

   static final class Segment extends ReentrantLock implements Serializable {
      private static final long serialVersionUID = 2249069246763182397L;
      transient volatile int count;
      transient int modCount;
      transient int threshold;
      transient volatile ConcurrentHashMap.HashEntry[] table;
      final float loadFactor;

      Segment(int initialCapacity, float lf) {
         this.loadFactor = lf;
         this.setTable(ConcurrentHashMap.HashEntry.newArray(initialCapacity));
      }

      static final ConcurrentHashMap.Segment[] newArray(int i) {
         return new ConcurrentHashMap.Segment[i];
      }

      void setTable(ConcurrentHashMap.HashEntry[] newTable) {
         this.threshold = (int)((float)newTable.length * this.loadFactor);
         this.table = newTable;
      }

      ConcurrentHashMap.HashEntry getFirst(int hash) {
         ConcurrentHashMap.HashEntry[] tab = this.table;
         return tab[hash & tab.length - 1];
      }

      Object readValueUnderLock(ConcurrentHashMap.HashEntry e) {
         this.lock();

         Object var2;
         try {
            var2 = e.value;
         } finally {
            this.unlock();
         }

         return var2;
      }

      Object get(Object key, int hash) {
         if (this.count != 0) {
            for(ConcurrentHashMap.HashEntry e = this.getFirst(hash); e != null; e = e.next) {
               if (e.hash == hash && key.equals(e.key)) {
                  Object v = e.value;
                  if (v != null) {
                     return v;
                  }

                  return this.readValueUnderLock(e);
               }
            }
         }

         return null;
      }

      boolean containsKey(Object key, int hash) {
         if (this.count != 0) {
            for(ConcurrentHashMap.HashEntry e = this.getFirst(hash); e != null; e = e.next) {
               if (e.hash == hash && key.equals(e.key)) {
                  return true;
               }
            }
         }

         return false;
      }

      boolean containsValue(Object value) {
         if (this.count != 0) {
            ConcurrentHashMap.HashEntry[] tab = this.table;
            int len = tab.length;

            for(int i = 0; i < len; ++i) {
               for(ConcurrentHashMap.HashEntry e = tab[i]; e != null; e = e.next) {
                  Object v = e.value;
                  if (v == null) {
                     v = this.readValueUnderLock(e);
                  }

                  if (value.equals(v)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }

      boolean replace(Object key, int hash, Object oldValue, Object newValue) {
         this.lock();

         try {
            ConcurrentHashMap.HashEntry e;
            for(e = this.getFirst(hash); e != null && (e.hash != hash || !key.equals(e.key)); e = e.next) {
            }

            boolean replaced = false;
            if (e != null && oldValue.equals(e.value)) {
               replaced = true;
               e.value = newValue;
            }

            boolean var7 = replaced;
            return var7;
         } finally {
            this.unlock();
         }
      }

      Object replace(Object key, int hash, Object newValue) {
         this.lock();

         Object var6;
         try {
            ConcurrentHashMap.HashEntry e;
            for(e = this.getFirst(hash); e != null && (e.hash != hash || !key.equals(e.key)); e = e.next) {
            }

            Object oldValue = null;
            if (e != null) {
               oldValue = e.value;
               e.value = newValue;
            }

            var6 = oldValue;
         } finally {
            this.unlock();
         }

         return var6;
      }

      Object put(Object key, int hash, Object value, boolean onlyIfAbsent) {
         this.lock();

         try {
            int c = this.count;
            if (c++ > this.threshold) {
               this.rehash();
            }

            ConcurrentHashMap.HashEntry[] tab = this.table;
            int index = hash & tab.length - 1;
            ConcurrentHashMap.HashEntry first = tab[index];

            ConcurrentHashMap.HashEntry e;
            for(e = first; e != null && (e.hash != hash || !key.equals(e.key)); e = e.next) {
            }

            Object oldValue;
            if (e != null) {
               oldValue = e.value;
               if (!onlyIfAbsent) {
                  e.value = value;
               }
            } else {
               oldValue = null;
               ++this.modCount;
               tab[index] = new ConcurrentHashMap.HashEntry(key, hash, first, value);
               this.count = c;
            }

            Object var11 = oldValue;
            return var11;
         } finally {
            this.unlock();
         }
      }

      void rehash() {
         ConcurrentHashMap.HashEntry[] oldTable = this.table;
         int oldCapacity = oldTable.length;
         if (oldCapacity < 1073741824) {
            ConcurrentHashMap.HashEntry[] newTable = ConcurrentHashMap.HashEntry.newArray(oldCapacity << 1);
            this.threshold = (int)((float)newTable.length * this.loadFactor);
            int sizeMask = newTable.length - 1;

            for(int i = 0; i < oldCapacity; ++i) {
               ConcurrentHashMap.HashEntry e = oldTable[i];
               if (e != null) {
                  ConcurrentHashMap.HashEntry next = e.next;
                  int idx = e.hash & sizeMask;
                  if (next == null) {
                     newTable[idx] = e;
                  } else {
                     ConcurrentHashMap.HashEntry lastRun = e;
                     int lastIdx = idx;

                     ConcurrentHashMap.HashEntry p;
                     int k;
                     for(p = next; p != null; p = p.next) {
                        k = p.hash & sizeMask;
                        if (k != lastIdx) {
                           lastIdx = k;
                           lastRun = p;
                        }
                     }

                     newTable[lastIdx] = lastRun;

                     for(p = e; p != lastRun; p = p.next) {
                        k = p.hash & sizeMask;
                        ConcurrentHashMap.HashEntry n = newTable[k];
                        newTable[k] = new ConcurrentHashMap.HashEntry(p.key, p.hash, n, p.value);
                     }
                  }
               }
            }

            this.table = newTable;
         }
      }

      Object remove(Object key, int hash, Object value) {
         this.lock();

         Object v;
         try {
            int c = this.count - 1;
            ConcurrentHashMap.HashEntry[] tab = this.table;
            int index = hash & tab.length - 1;
            ConcurrentHashMap.HashEntry first = tab[index];

            ConcurrentHashMap.HashEntry e;
            for(e = first; e != null && (e.hash != hash || !key.equals(e.key)); e = e.next) {
            }

            Object oldValue = null;
            if (e != null) {
               v = e.value;
               if (value == null || value.equals(v)) {
                  oldValue = v;
                  ++this.modCount;
                  ConcurrentHashMap.HashEntry newFirst = e.next;

                  for(ConcurrentHashMap.HashEntry p = first; p != e; p = p.next) {
                     newFirst = new ConcurrentHashMap.HashEntry(p.key, p.hash, newFirst, p.value);
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

      void clear() {
         if (this.count != 0) {
            this.lock();

            try {
               ConcurrentHashMap.HashEntry[] tab = this.table;

               for(int i = 0; i < tab.length; ++i) {
                  tab[i] = null;
               }

               ++this.modCount;
               this.count = 0;
            } finally {
               this.unlock();
            }
         }

      }
   }

   static final class HashEntry {
      final Object key;
      final int hash;
      volatile Object value;
      final ConcurrentHashMap.HashEntry next;

      HashEntry(Object key, int hash, ConcurrentHashMap.HashEntry next, Object value) {
         this.key = key;
         this.hash = hash;
         this.next = next;
         this.value = value;
      }

      static final ConcurrentHashMap.HashEntry[] newArray(int i) {
         return new ConcurrentHashMap.HashEntry[i];
      }
   }
}
