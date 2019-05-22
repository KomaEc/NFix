package org.apache.commons.collections;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

/** @deprecated */
public final class StaticBucketMap implements Map {
   private static final int DEFAULT_BUCKETS = 255;
   private StaticBucketMap.Node[] m_buckets;
   private StaticBucketMap.Lock[] m_locks;

   public StaticBucketMap() {
      this(255);
   }

   public StaticBucketMap(int numBuckets) {
      int size = Math.max(17, numBuckets);
      if (size % 2 == 0) {
         --size;
      }

      this.m_buckets = new StaticBucketMap.Node[size];
      this.m_locks = new StaticBucketMap.Lock[size];

      for(int i = 0; i < size; ++i) {
         this.m_locks[i] = new StaticBucketMap.Lock();
      }

   }

   private final int getHash(Object key) {
      if (key == null) {
         return 0;
      } else {
         int hash = key.hashCode();
         hash += ~(hash << 15);
         hash ^= hash >>> 10;
         hash += hash << 3;
         hash ^= hash >>> 6;
         hash += ~(hash << 11);
         hash ^= hash >>> 16;
         hash %= this.m_buckets.length;
         return hash < 0 ? hash * -1 : hash;
      }
   }

   public Set keySet() {
      return new StaticBucketMap.KeySet();
   }

   public int size() {
      int cnt = 0;

      for(int i = 0; i < this.m_buckets.length; ++i) {
         cnt += this.m_locks[i].size;
      }

      return cnt;
   }

   public Object put(Object key, Object value) {
      int hash = this.getHash(key);
      StaticBucketMap.Lock var4 = this.m_locks[hash];
      synchronized(var4) {
         StaticBucketMap.Node n = this.m_buckets[hash];
         StaticBucketMap.Node next;
         if (n == null) {
            n = new StaticBucketMap.Node();
            n.key = key;
            n.value = value;
            this.m_buckets[hash] = n;
            ++this.m_locks[hash].size;
            next = null;
            return next;
         } else {
            for(next = n; next != null; next = next.next) {
               n = next;
               if (next.key == key || next.key != null && next.key.equals(key)) {
                  Object returnVal = next.value;
                  next.value = value;
                  return returnVal;
               }
            }

            StaticBucketMap.Node newNode = new StaticBucketMap.Node();
            newNode.key = key;
            newNode.value = value;
            n.next = newNode;
            ++this.m_locks[hash].size;
            return null;
         }
      }
   }

   public Object get(Object key) {
      int hash = this.getHash(key);
      StaticBucketMap.Lock var3 = this.m_locks[hash];
      synchronized(var3) {
         for(StaticBucketMap.Node n = this.m_buckets[hash]; n != null; n = n.next) {
            if (n.key == key || n.key != null && n.key.equals(key)) {
               Object var5 = n.value;
               return var5;
            }
         }

         return null;
      }
   }

   public boolean containsKey(Object key) {
      int hash = this.getHash(key);
      StaticBucketMap.Lock var3 = this.m_locks[hash];
      synchronized(var3) {
         for(StaticBucketMap.Node n = this.m_buckets[hash]; n != null; n = n.next) {
            if (n.key == key || n.key != null && n.key.equals(key)) {
               boolean var5 = true;
               return var5;
            }
         }

         return false;
      }
   }

   public boolean containsValue(Object value) {
      for(int i = 0; i < this.m_buckets.length; ++i) {
         StaticBucketMap.Lock var3 = this.m_locks[i];
         synchronized(var3) {
            for(StaticBucketMap.Node n = this.m_buckets[i]; n != null; n = n.next) {
               if (n.value == value || n.value != null && n.value.equals(value)) {
                  boolean var5 = true;
                  return var5;
               }
            }
         }
      }

      return false;
   }

   public Collection values() {
      return new StaticBucketMap.Values();
   }

   public Set entrySet() {
      return new StaticBucketMap.EntrySet();
   }

   public void putAll(Map other) {
      Iterator i = other.keySet().iterator();

      while(i.hasNext()) {
         Object key = i.next();
         this.put(key, other.get(key));
      }

   }

   public Object remove(Object key) {
      int hash = this.getHash(key);
      StaticBucketMap.Lock var3 = this.m_locks[hash];
      synchronized(var3) {
         StaticBucketMap.Node n = this.m_buckets[hash];

         for(StaticBucketMap.Node prev = null; n != null; n = n.next) {
            if (n.key == key || n.key != null && n.key.equals(key)) {
               if (null == prev) {
                  this.m_buckets[hash] = n.next;
               } else {
                  prev.next = n.next;
               }

               --this.m_locks[hash].size;
               Object var6 = n.value;
               return var6;
            }

            prev = n;
         }

         return null;
      }
   }

   public final boolean isEmpty() {
      return this.size() == 0;
   }

   public final void clear() {
      for(int i = 0; i < this.m_buckets.length; ++i) {
         StaticBucketMap.Lock lock = this.m_locks[i];
         synchronized(lock) {
            this.m_buckets[i] = null;
            lock.size = 0;
         }
      }

   }

   public final boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof Map)) {
         return false;
      } else {
         Map other = (Map)obj;
         return this.entrySet().equals(other.entrySet());
      }
   }

   public final int hashCode() {
      int hashCode = 0;

      for(int i = 0; i < this.m_buckets.length; ++i) {
         StaticBucketMap.Lock var3 = this.m_locks[i];
         synchronized(var3) {
            for(StaticBucketMap.Node n = this.m_buckets[i]; n != null; n = n.next) {
               hashCode += n.hashCode();
            }
         }
      }

      return hashCode;
   }

   public void atomic(Runnable r) {
      if (r == null) {
         throw new NullPointerException();
      } else {
         this.atomic(r, 0);
      }
   }

   private void atomic(Runnable r, int bucket) {
      if (bucket >= this.m_buckets.length) {
         r.run();
      } else {
         StaticBucketMap.Lock var3 = this.m_locks[bucket];
         synchronized(var3) {
            this.atomic(r, bucket + 1);
         }
      }
   }

   private class Values extends AbstractCollection {
      private Values() {
      }

      public int size() {
         return StaticBucketMap.this.size();
      }

      public void clear() {
         StaticBucketMap.this.clear();
      }

      public Iterator iterator() {
         return StaticBucketMap.this.new ValueIterator();
      }

      // $FF: synthetic method
      Values(Object x1) {
         this();
      }
   }

   private class KeySet extends AbstractSet {
      private KeySet() {
      }

      public int size() {
         return StaticBucketMap.this.size();
      }

      public void clear() {
         StaticBucketMap.this.clear();
      }

      public Iterator iterator() {
         return StaticBucketMap.this.new KeyIterator();
      }

      public boolean contains(Object o) {
         return StaticBucketMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         int hash = StaticBucketMap.this.getHash(o);
         StaticBucketMap.Lock var3 = StaticBucketMap.this.m_locks[hash];
         synchronized(var3) {
            for(StaticBucketMap.Node n = StaticBucketMap.this.m_buckets[hash]; n != null; n = n.next) {
               Object k = n.getKey();
               if (k == o || k != null && k.equals(o)) {
                  StaticBucketMap.this.remove(k);
                  boolean var6 = true;
                  return var6;
               }
            }

            return false;
         }
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }

   private class EntrySet extends AbstractSet {
      private EntrySet() {
      }

      public int size() {
         return StaticBucketMap.this.size();
      }

      public void clear() {
         StaticBucketMap.this.clear();
      }

      public Iterator iterator() {
         return StaticBucketMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         Entry entry = (Entry)o;
         int hash = StaticBucketMap.this.getHash(entry.getKey());
         StaticBucketMap.Lock var4 = StaticBucketMap.this.m_locks[hash];
         synchronized(var4) {
            for(StaticBucketMap.Node n = StaticBucketMap.this.m_buckets[hash]; n != null; n = n.next) {
               if (n.equals(entry)) {
                  boolean var6 = true;
                  return var6;
               }
            }

            return false;
         }
      }

      public boolean remove(Object obj) {
         if (!(obj instanceof Entry)) {
            return false;
         } else {
            Entry entry = (Entry)obj;
            int hash = StaticBucketMap.this.getHash(entry.getKey());
            StaticBucketMap.Lock var4 = StaticBucketMap.this.m_locks[hash];
            synchronized(var4) {
               for(StaticBucketMap.Node n = StaticBucketMap.this.m_buckets[hash]; n != null; n = n.next) {
                  if (n.equals(entry)) {
                     StaticBucketMap.this.remove(n.getKey());
                     boolean var6 = true;
                     return var6;
                  }
               }

               return false;
            }
         }
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   private class KeyIterator extends StaticBucketMap.EntryIterator {
      private KeyIterator() {
         super(null);
      }

      public Object next() {
         return this.nextEntry().getKey();
      }

      // $FF: synthetic method
      KeyIterator(Object x1) {
         this();
      }
   }

   private class ValueIterator extends StaticBucketMap.EntryIterator {
      private ValueIterator() {
         super(null);
      }

      public Object next() {
         return this.nextEntry().getValue();
      }

      // $FF: synthetic method
      ValueIterator(Object x1) {
         this();
      }
   }

   private class EntryIterator implements Iterator {
      private ArrayList current;
      private int bucket;
      private Entry last;

      private EntryIterator() {
         this.current = new ArrayList();
      }

      public boolean hasNext() {
         if (this.current.size() > 0) {
            return true;
         } else {
            while(this.bucket < StaticBucketMap.this.m_buckets.length) {
               StaticBucketMap.Lock var1 = StaticBucketMap.this.m_locks[this.bucket];
               synchronized(var1) {
                  for(StaticBucketMap.Node n = StaticBucketMap.this.m_buckets[this.bucket]; n != null; n = n.next) {
                     this.current.add(n);
                  }

                  ++this.bucket;
                  if (this.current.size() > 0) {
                     boolean var3 = true;
                     return var3;
                  }
               }
            }

            return false;
         }
      }

      protected Entry nextEntry() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.last = (Entry)this.current.remove(this.current.size() - 1);
            return this.last;
         }
      }

      public Object next() {
         return this.nextEntry();
      }

      public void remove() {
         if (this.last == null) {
            throw new IllegalStateException();
         } else {
            StaticBucketMap.this.remove(this.last.getKey());
            this.last = null;
         }
      }

      // $FF: synthetic method
      EntryIterator(Object x1) {
         this();
      }
   }

   private static final class Lock {
      public int size;

      private Lock() {
      }

      // $FF: synthetic method
      Lock(Object x0) {
         this();
      }
   }

   private static final class Node implements Entry, KeyValue {
      protected Object key;
      protected Object value;
      protected StaticBucketMap.Node next;

      private Node() {
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public boolean equals(Object o) {
         if (o == null) {
            return false;
         } else if (o == this) {
            return true;
         } else if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e2 = (Entry)o;
            return (this.key == null ? e2.getKey() == null : this.key.equals(e2.getKey())) && (this.value == null ? e2.getValue() == null : this.value.equals(e2.getValue()));
         }
      }

      public Object setValue(Object val) {
         Object retVal = this.value;
         this.value = val;
         return retVal;
      }

      // $FF: synthetic method
      Node(Object x0) {
         this();
      }
   }
}
