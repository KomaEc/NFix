package org.codehaus.groovy.runtime.metaclass;

import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class MemoryAwareConcurrentReadMap {
   protected final MemoryAwareConcurrentReadMap.BarrierLock barrierLock;
   protected transient Object lastWrite;
   public static final int DEFAULT_INITIAL_CAPACITY = 32;
   private static final int MINIMUM_CAPACITY = 4;
   private static final int MAXIMUM_CAPACITY = 1073741824;
   public static final float DEFAULT_LOAD_FACTOR = 0.75F;
   protected transient MemoryAwareConcurrentReadMap.Entry[] table;
   protected transient int count;
   protected int threshold;
   protected float loadFactor;
   private ReferenceQueue queue;
   private static final MemoryAwareConcurrentReadMap.Reference DUMMY_REF = new MemoryAwareConcurrentReadMap.DummyRef();

   protected final void recordModification(Object x) {
      synchronized(this.barrierLock) {
         this.lastWrite = x;
      }
   }

   protected final MemoryAwareConcurrentReadMap.Entry[] getTableForReading() {
      synchronized(this.barrierLock) {
         return this.table;
      }
   }

   private int p2capacity(int initialCapacity) {
      int cap = initialCapacity;
      int result;
      if (initialCapacity <= 1073741824 && initialCapacity >= 0) {
         for(result = 4; result < cap; result <<= 1) {
         }
      } else {
         result = 1073741824;
      }

      return result;
   }

   private static int hash(Object x) {
      int h = x.hashCode();
      return (h << 7) - h + (h >>> 9) + (h >>> 17);
   }

   protected boolean eq(Object x, Object y) {
      return x == y;
   }

   public MemoryAwareConcurrentReadMap(int initialCapacity, float loadFactor) {
      this.barrierLock = new MemoryAwareConcurrentReadMap.BarrierLock();
      if (loadFactor <= 0.0F) {
         throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
      } else {
         this.loadFactor = loadFactor;
         int cap = this.p2capacity(initialCapacity);
         this.table = new MemoryAwareConcurrentReadMap.Entry[cap];
         this.threshold = (int)((float)cap * loadFactor);
         this.queue = new ReferenceQueue();
      }
   }

   public MemoryAwareConcurrentReadMap(int initialCapacity) {
      this(initialCapacity, 0.75F);
   }

   public MemoryAwareConcurrentReadMap() {
      this(32, 0.75F);
   }

   public synchronized int size() {
      return this.count;
   }

   public synchronized boolean isEmpty() {
      return this.count == 0;
   }

   public Object get(Object key) {
      int hash = hash(key);
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;
      int index = hash & tab.length - 1;
      MemoryAwareConcurrentReadMap.Entry first = tab[index];
      MemoryAwareConcurrentReadMap.Entry e = first;

      while(true) {
         while(true) {
            while(e == null) {
               MemoryAwareConcurrentReadMap.Entry[] reread = this.getTableForReading();
               if (tab == reread && first == tab[index]) {
                  return null;
               }

               tab = reread;
               e = first = reread[index = hash & reread.length - 1];
            }

            Object eKey = e.getKey();
            Object eValue = e.getValue();
            if (e.hash == hash && this.eq(key, eKey)) {
               if (e.value != DUMMY_REF) {
                  return eValue;
               }

               synchronized(this) {
                  if (eKey == null && eValue == null) {
                     this.expungeStaleEntries();
                  }

                  tab = this.table;
               }

               e = first = tab[index = hash & tab.length - 1];
            } else {
               e = e.next;
            }
         }
      }
   }

   public Object put(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         int hash = hash(key);
         MemoryAwareConcurrentReadMap.Entry[] tab = this.table;
         int index = hash & tab.length - 1;
         MemoryAwareConcurrentReadMap.Entry first = tab[index];

         MemoryAwareConcurrentReadMap.Entry e;
         for(e = first; e != null && (e.hash != hash || !this.eq(key, e.getKey())); e = e.next) {
         }

         synchronized(this) {
            if (tab == this.table) {
               if (e == null) {
                  if (first == tab[index]) {
                     MemoryAwareConcurrentReadMap.Entry newEntry = new MemoryAwareConcurrentReadMap.Entry(hash, key, value, first, this.queue);
                     tab[index] = newEntry;
                     if (++this.count >= this.threshold) {
                        this.rehash();
                     } else {
                        this.recordModification(newEntry);
                     }

                     return null;
                  }
               } else {
                  Object oldValue = e.getValue();
                  if (first == tab[index] && oldValue != null) {
                     e.setValue(e.value);
                     return oldValue;
                  }
               }
            }

            return this.sput(key, value, hash);
         }
      }
   }

   protected Object sput(Object key, Object value, int hash) {
      this.expungeStaleEntries();
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;
      int index = hash & tab.length - 1;
      MemoryAwareConcurrentReadMap.Entry first = tab[index];

      for(MemoryAwareConcurrentReadMap.Entry e = first; e != null; e = e.next) {
         if (e.hash == hash && this.eq(key, e.getKey())) {
            Object oldValue = e.getValue();
            e.setValue(e.value);
            return oldValue;
         }
      }

      MemoryAwareConcurrentReadMap.Entry newEntry = new MemoryAwareConcurrentReadMap.Entry(hash, key, value, first, this.queue);
      tab[index] = newEntry;
      if (++this.count >= this.threshold) {
         this.rehash();
      } else {
         this.recordModification(newEntry);
      }

      return null;
   }

   protected void rehash() {
      MemoryAwareConcurrentReadMap.Entry[] oldTable = this.table;
      int oldCapacity = oldTable.length;
      if (oldCapacity >= 1073741824) {
         this.threshold = Integer.MAX_VALUE;
      } else {
         int newCapacity = oldCapacity << 1;
         int mask = newCapacity - 1;
         this.threshold = (int)((float)newCapacity * this.loadFactor);
         MemoryAwareConcurrentReadMap.Entry[] newTable = new MemoryAwareConcurrentReadMap.Entry[newCapacity];

         for(int i = 0; i < oldCapacity; ++i) {
            MemoryAwareConcurrentReadMap.Entry e = oldTable[i];
            if (e != null) {
               int idx = e.hash & mask;
               MemoryAwareConcurrentReadMap.Entry next = e.next;
               if (next == null) {
                  newTable[idx] = e;
               } else {
                  MemoryAwareConcurrentReadMap.Entry lastRun = e;
                  int lastIdx = idx;

                  MemoryAwareConcurrentReadMap.Entry p;
                  int k;
                  for(p = next; p != null; p = p.next) {
                     k = p.hash & mask;
                     if (k != lastIdx) {
                        lastIdx = k;
                        lastRun = p;
                     }
                  }

                  newTable[lastIdx] = lastRun;

                  for(p = e; p != lastRun; p = p.next) {
                     k = p.hash & mask;
                     newTable[k] = new MemoryAwareConcurrentReadMap.Entry(p.hash, p.getKey(), p.getValue(), newTable[k], this.queue);
                  }
               }
            }
         }

         this.table = newTable;
         this.recordModification(newTable);
      }
   }

   public Object remove(Object key) {
      int hash = hash(key);
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;
      int index = hash & tab.length - 1;
      MemoryAwareConcurrentReadMap.Entry first = tab[index];

      MemoryAwareConcurrentReadMap.Entry e;
      for(e = first; e != null && (e.hash != hash || !this.eq(key, e.getKey())); e = e.next) {
      }

      synchronized(this) {
         if (tab == this.table) {
            if (e == null) {
               if (first == tab[index]) {
                  return null;
               }
            } else {
               Object oldValue = e.getValue();
               if (first == tab[index] && oldValue != null) {
                  e.setValue((MemoryAwareConcurrentReadMap.Reference)null);
                  --this.count;
                  MemoryAwareConcurrentReadMap.Entry head = e.next;

                  for(MemoryAwareConcurrentReadMap.Entry p = first; p != e; p = p.next) {
                     head = new MemoryAwareConcurrentReadMap.Entry(p.hash, p.key, p.value, head);
                  }

                  tab[index] = head;
                  this.recordModification(head);
                  return oldValue;
               }
            }
         }

         return this.sremove(key, hash);
      }
   }

   protected Object sremove(Object key, int hash) {
      this.expungeStaleEntries();
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;
      int index = hash & tab.length - 1;
      MemoryAwareConcurrentReadMap.Entry first = tab[index];

      for(MemoryAwareConcurrentReadMap.Entry e = first; e != null; e = e.next) {
         if (e.hash == hash && this.eq(key, e.getKey())) {
            Object oldValue = e.getValue();
            e.setValue((MemoryAwareConcurrentReadMap.Reference)null);
            --this.count;
            MemoryAwareConcurrentReadMap.Entry head = e.next;

            for(MemoryAwareConcurrentReadMap.Entry p = first; p != e; p = p.next) {
               head = new MemoryAwareConcurrentReadMap.Entry(p.hash, p.getKey(), p.getValue(), head, this.queue);
            }

            tab[index] = head;
            this.recordModification(head);
            return oldValue;
         }
      }

      return null;
   }

   public synchronized void clear() {
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;

      for(int i = 0; i < tab.length; ++i) {
         for(MemoryAwareConcurrentReadMap.Entry e = tab[i]; e != null; e = e.next) {
            e.setValue((MemoryAwareConcurrentReadMap.Reference)null);
         }

         tab[i] = null;
      }

      this.count = 0;
      this.recordModification(tab);
   }

   private void expungeStaleEntries() {
      MemoryAwareConcurrentReadMap.Entry[] tab = this.table;

      while(true) {
         while(true) {
            MemoryAwareConcurrentReadMap.SoftRef ref;
            MemoryAwareConcurrentReadMap.Entry entry;
            do {
               do {
                  if ((ref = (MemoryAwareConcurrentReadMap.SoftRef)this.queue.poll()) == null) {
                     return;
                  }

                  entry = ref.entry;
               } while(entry == null);

               ref.entry = null;
            } while(entry.key != ref && entry.value != ref);

            int hash = entry.hash;
            int index = hash & tab.length - 1;
            MemoryAwareConcurrentReadMap.Entry first = tab[index];

            for(MemoryAwareConcurrentReadMap.Entry e = first; e != null; e = e.next) {
               if (e == entry) {
                  entry.key.clear();
                  entry.setValue((MemoryAwareConcurrentReadMap.Reference)null);
                  --this.count;
                  MemoryAwareConcurrentReadMap.Entry head = e.next;

                  for(MemoryAwareConcurrentReadMap.Entry p = first; p != e; p = p.next) {
                     head = new MemoryAwareConcurrentReadMap.Entry(p.hash, p.key, p.value, head);
                  }

                  tab[index] = head;
                  this.recordModification(head);
                  break;
               }
            }
         }
      }
   }

   private static class Entry {
      private final int hash;
      private final MemoryAwareConcurrentReadMap.SoftRef key;
      private final MemoryAwareConcurrentReadMap.Entry next;
      private volatile MemoryAwareConcurrentReadMap.Reference value;

      Entry(int hash, Object key, Object value, MemoryAwareConcurrentReadMap.Entry next, ReferenceQueue queue) {
         this.hash = hash;
         this.key = new MemoryAwareConcurrentReadMap.SoftRef(this, key, queue);
         this.next = next;
         this.value = new MemoryAwareConcurrentReadMap.SoftRef(this, value, queue);
      }

      Entry(int hash, MemoryAwareConcurrentReadMap.SoftRef key, MemoryAwareConcurrentReadMap.Reference value, MemoryAwareConcurrentReadMap.Entry next) {
         this.hash = hash;
         this.key = key;
         key.entry = this;
         this.next = next;
         this.value = MemoryAwareConcurrentReadMap.DUMMY_REF;
         this.setValue(value);
      }

      public Object getKey() {
         return this.key.get();
      }

      public Object getValue() {
         return this.value.get();
      }

      public Object setValue(MemoryAwareConcurrentReadMap.Reference value) {
         Object oldValue = this.value.get();
         if (value != null && value != MemoryAwareConcurrentReadMap.DUMMY_REF) {
            MemoryAwareConcurrentReadMap.SoftRef ref = (MemoryAwareConcurrentReadMap.SoftRef)value;
            ref.entry = this;
            this.value = value;
         } else {
            this.value = MemoryAwareConcurrentReadMap.DUMMY_REF;
         }

         return oldValue;
      }
   }

   private static class SoftRef extends SoftReference implements MemoryAwareConcurrentReadMap.Reference {
      private volatile MemoryAwareConcurrentReadMap.Entry entry;

      public SoftRef(MemoryAwareConcurrentReadMap.Entry e, Object v, ReferenceQueue q) {
         super(v, q);
         this.entry = e;
      }

      public void clear() {
         super.clear();
         this.entry = null;
      }
   }

   private static class DummyRef implements MemoryAwareConcurrentReadMap.Reference {
      private DummyRef() {
      }

      public Object get() {
         return null;
      }

      // $FF: synthetic method
      DummyRef(Object x0) {
         this();
      }
   }

   private interface Reference {
      Object get();
   }

   protected static class BarrierLock implements Serializable {
   }
}
