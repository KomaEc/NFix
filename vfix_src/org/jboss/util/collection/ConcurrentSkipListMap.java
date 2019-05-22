package org.jboss.util.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ConcurrentSkipListMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable {
   private static final long serialVersionUID = -8627078645895051609L;
   private static final Object BASE_HEADER = new Object();
   transient volatile ConcurrentSkipListMap.HeadIndex<K, V> head;
   private final Comparator<? super K> comparator;
   private transient int randomSeed;
   private transient ConcurrentSkipListMap<K, V>.KeySet keySet;
   private transient ConcurrentSkipListMap<K, V>.EntrySet entrySet;
   private transient ConcurrentSkipListMap<K, V>.Values values;
   private transient ConcurrentSkipListMap<K, V>.DescendingKeySet descendingKeySet;
   private transient ConcurrentSkipListMap<K, V>.DescendingEntrySet descendingEntrySet;
   private static final AtomicReferenceFieldUpdater<ConcurrentSkipListMap, ConcurrentSkipListMap.HeadIndex> headUpdater = (AtomicReferenceFieldUpdater)AccessController.doPrivileged(new PrivilegedAction<AtomicReferenceFieldUpdater<ConcurrentSkipListMap, ConcurrentSkipListMap.HeadIndex>>() {
      public AtomicReferenceFieldUpdater<ConcurrentSkipListMap, ConcurrentSkipListMap.HeadIndex> run() {
         return AtomicReferenceFieldUpdater.newUpdater(ConcurrentSkipListMap.class, ConcurrentSkipListMap.HeadIndex.class, "head");
      }
   });
   private static final int EQ = 1;
   private static final int LT = 2;
   private static final int GT = 0;

   final void initialize() {
      this.keySet = null;
      this.entrySet = null;
      this.values = null;
      this.descendingEntrySet = null;
      this.descendingKeySet = null;
      this.randomSeed = (int)System.nanoTime();
      this.head = new ConcurrentSkipListMap.HeadIndex(new ConcurrentSkipListMap.Node((Object)null, BASE_HEADER, (ConcurrentSkipListMap.Node)null), (ConcurrentSkipListMap.Index)null, (ConcurrentSkipListMap.Index)null, 1);
   }

   private boolean casHead(ConcurrentSkipListMap.HeadIndex<K, V> cmp, ConcurrentSkipListMap.HeadIndex<K, V> val) {
      return headUpdater.compareAndSet(this, cmp, val);
   }

   private Comparable<K> comparable(Object key) throws ClassCastException {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (Comparable)(this.comparator != null ? new ConcurrentSkipListMap.ComparableUsingComparator(key, this.comparator) : (Comparable)key);
      }
   }

   int compare(K k1, K k2) throws ClassCastException {
      Comparator<? super K> cmp = this.comparator;
      return cmp != null ? cmp.compare(k1, k2) : ((Comparable)k1).compareTo(k2);
   }

   boolean inHalfOpenRange(K key, K least, K fence) {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (least == null || this.compare(key, least) >= 0) && (fence == null || this.compare(key, fence) < 0);
      }
   }

   boolean inOpenRange(K key, K least, K fence) {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (least == null || this.compare(key, least) >= 0) && (fence == null || this.compare(key, fence) <= 0);
      }
   }

   private ConcurrentSkipListMap.Node<K, V> findPredecessor(Comparable<K> key) {
      label27:
      while(true) {
         Object q = this.head;

         while(true) {
            while(true) {
               ConcurrentSkipListMap.Index r;
               if ((r = ((ConcurrentSkipListMap.Index)q).right) != null) {
                  if (r.indexesDeletedNode()) {
                     if (!((ConcurrentSkipListMap.Index)q).unlink(r)) {
                        continue label27;
                     }
                     continue;
                  }

                  if (key.compareTo(r.key) > 0) {
                     q = r;
                     continue;
                  }
               }

               ConcurrentSkipListMap.Index d;
               if ((d = ((ConcurrentSkipListMap.Index)q).down) == null) {
                  return ((ConcurrentSkipListMap.Index)q).node;
               }

               q = d;
            }
         }
      }
   }

   private ConcurrentSkipListMap.Node<K, V> findNode(Comparable<K> key) {
      label37:
      while(true) {
         ConcurrentSkipListMap.Node<K, V> b = this.findPredecessor(key);

         ConcurrentSkipListMap.Node f;
         for(ConcurrentSkipListMap.Node n = b.next; n != null; n = f) {
            f = n.next;
            if (n != b.next) {
               continue label37;
            }

            Object v = n.value;
            if (v == null) {
               n.helpDelete(b, f);
               continue label37;
            }

            if (v == n || b.value == null) {
               continue label37;
            }

            int c = key.compareTo(n.key);
            if (c < 0) {
               return null;
            }

            if (c == 0) {
               return n;
            }

            b = n;
         }

         return null;
      }
   }

   private V doGet(Object okey) {
      Comparable<K> key = this.comparable(okey);
      K bound = null;
      Object q = this.head;

      while(true) {
         while(true) {
            Object rk;
            ConcurrentSkipListMap.Index r;
            Object nk;
            if ((r = ((ConcurrentSkipListMap.Index)q).right) != null && (rk = r.key) != null && rk != bound) {
               int c = key.compareTo(rk);
               if (c > 0) {
                  q = r;
                  continue;
               }

               if (c == 0) {
                  nk = r.node.value;
                  return nk != null ? nk : this.getUsingFindNode(key);
               }

               bound = rk;
            }

            ConcurrentSkipListMap.Index d;
            if ((d = ((ConcurrentSkipListMap.Index)q).down) == null) {
               for(ConcurrentSkipListMap.Node n = ((ConcurrentSkipListMap.Index)q).node.next; n != null; n = n.next) {
                  nk = n.key;
                  if (nk != null) {
                     int c = key.compareTo(nk);
                     if (c == 0) {
                        Object v = n.value;
                        return v != null ? v : this.getUsingFindNode(key);
                     }

                     if (c < 0) {
                        return null;
                     }
                  }
               }

               return null;
            }

            q = d;
         }
      }
   }

   private V getUsingFindNode(Comparable<K> key) {
      Object v;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findNode(key);
         if (n == null) {
            return null;
         }

         v = n.value;
      } while(v == null);

      return v;
   }

   private V doPut(K kkey, V value, boolean onlyIfAbsent) {
      Comparable key = this.comparable(kkey);

      ConcurrentSkipListMap.Node n;
      Object v;
      label51:
      do {
         while(true) {
            ConcurrentSkipListMap.Node<K, V> b = this.findPredecessor(key);
            n = b.next;

            while(true) {
               ConcurrentSkipListMap.Node f;
               if (n != null) {
                  f = n.next;
                  if (n != b.next) {
                     break;
                  }

                  v = n.value;
                  if (v == null) {
                     n.helpDelete(b, f);
                     break;
                  }

                  if (v == n || b.value == null) {
                     break;
                  }

                  int c = key.compareTo(n.key);
                  if (c > 0) {
                     b = n;
                     n = f;
                     continue;
                  }

                  if (c == 0) {
                     continue label51;
                  }
               }

               f = new ConcurrentSkipListMap.Node(kkey, value, n);
               if (b.casNext(n, f)) {
                  int level = this.randomLevel();
                  if (level > 0) {
                     this.insertIndex(f, level);
                  }

                  return null;
               }
               break;
            }
         }
      } while(!onlyIfAbsent && !n.casValue(v, value));

      return v;
   }

   private int randomLevel() {
      int level = 0;
      int r = this.randomSeed;
      this.randomSeed = r * 134775813 + 1;
      if (r < 0) {
         while((r <<= 1) > 0) {
            ++level;
         }
      }

      return level;
   }

   private void insertIndex(ConcurrentSkipListMap.Node<K, V> z, int level) {
      ConcurrentSkipListMap.HeadIndex<K, V> h = this.head;
      int max = h.level;
      if (level <= max) {
         ConcurrentSkipListMap.Index<K, V> idx = null;

         for(int i = 1; i <= level; ++i) {
            idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
         }

         this.addIndex(idx, h, level);
      } else {
         level = max + 1;
         ConcurrentSkipListMap.Index<K, V>[] idxs = (ConcurrentSkipListMap.Index[])(new ConcurrentSkipListMap.Index[level + 1]);
         ConcurrentSkipListMap.Index<K, V> idx = null;

         for(int i = 1; i <= level; ++i) {
            idxs[i] = idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
         }

         int k;
         ConcurrentSkipListMap.HeadIndex oldh;
         while(true) {
            oldh = this.head;
            int oldLevel = oldh.level;
            if (level <= oldLevel) {
               k = level;
               break;
            }

            ConcurrentSkipListMap.HeadIndex<K, V> newh = oldh;
            ConcurrentSkipListMap.Node<K, V> oldbase = oldh.node;

            for(int j = oldLevel + 1; j <= level; ++j) {
               newh = new ConcurrentSkipListMap.HeadIndex(oldbase, newh, idxs[j], j);
            }

            if (this.casHead(oldh, newh)) {
               k = oldLevel;
               break;
            }
         }

         this.addIndex(idxs[k], oldh, k);
      }

   }

   private void addIndex(ConcurrentSkipListMap.Index<K, V> idx, ConcurrentSkipListMap.HeadIndex<K, V> h, int indexLevel) {
      int insertionLevel = indexLevel;
      Comparable key = this.comparable(idx.key);

      label49:
      while(true) {
         ConcurrentSkipListMap.Index<K, V> q = h;
         ConcurrentSkipListMap.Index<K, V> t = idx;
         int j = h.level;

         while(true) {
            while(true) {
               ConcurrentSkipListMap.Index<K, V> r = ((ConcurrentSkipListMap.Index)q).right;
               if (r != null) {
                  int c = key.compareTo(r.key);
                  if (r.indexesDeletedNode()) {
                     if (!((ConcurrentSkipListMap.Index)q).unlink(r)) {
                        continue label49;
                     }
                     continue;
                  }

                  if (c > 0) {
                     q = r;
                     continue;
                  }
               }

               if (j == insertionLevel) {
                  if (t.indexesDeletedNode()) {
                     this.findNode(key);
                     return;
                  }

                  if (!((ConcurrentSkipListMap.Index)q).link(r, t)) {
                     continue label49;
                  }

                  --insertionLevel;
                  if (insertionLevel == 0) {
                     if (t.indexesDeletedNode()) {
                        this.findNode(key);
                     }

                     return;
                  }
               }

               if (j > insertionLevel && j <= indexLevel) {
                  t = t.down;
               }

               q = ((ConcurrentSkipListMap.Index)q).down;
               --j;
            }
         }
      }
   }

   private V doRemove(Object okey, Object value) {
      Comparable key = this.comparable(okey);

      while(true) {
         label57:
         while(true) {
            ConcurrentSkipListMap.Node<K, V> b = this.findPredecessor(key);

            ConcurrentSkipListMap.Node f;
            for(ConcurrentSkipListMap.Node n = b.next; n != null; n = f) {
               f = n.next;
               if (n != b.next) {
                  continue label57;
               }

               Object v = n.value;
               if (v == null) {
                  n.helpDelete(b, f);
                  continue label57;
               }

               if (v == n || b.value == null) {
                  continue label57;
               }

               int c = key.compareTo(n.key);
               if (c < 0) {
                  return null;
               }

               if (c <= 0) {
                  if (value != null && !value.equals(v)) {
                     return null;
                  }

                  if (n.casValue(v, (Object)null)) {
                     if (n.appendMarker(f) && b.casNext(n, f)) {
                        this.findPredecessor(key);
                        if (this.head.right == null) {
                           this.tryReduceLevel();
                        }
                     } else {
                        this.findNode(key);
                     }

                     return v;
                  }
                  continue label57;
               }

               b = n;
            }

            return null;
         }
      }
   }

   private void tryReduceLevel() {
      ConcurrentSkipListMap.HeadIndex<K, V> h = this.head;
      ConcurrentSkipListMap.HeadIndex d;
      ConcurrentSkipListMap.HeadIndex e;
      if (h.level > 3 && (d = (ConcurrentSkipListMap.HeadIndex)h.down) != null && (e = (ConcurrentSkipListMap.HeadIndex)d.down) != null && e.right == null && d.right == null && h.right == null && this.casHead(h, d) && h.right != null) {
         this.casHead(d, h);
      }

   }

   boolean removep(Object key) {
      return this.doRemove(key, (Object)null) != null;
   }

   ConcurrentSkipListMap.Node<K, V> findFirst() {
      while(true) {
         ConcurrentSkipListMap.Node<K, V> b = this.head.node;
         ConcurrentSkipListMap.Node<K, V> n = b.next;
         if (n == null) {
            return null;
         }

         if (n.value != null) {
            return n;
         }

         n.helpDelete(b, n.next);
      }
   }

   Object doRemoveFirst(boolean keyOnly) {
      while(true) {
         ConcurrentSkipListMap.Node<K, V> b = this.head.node;
         ConcurrentSkipListMap.Node<K, V> n = b.next;
         if (n == null) {
            return null;
         }

         ConcurrentSkipListMap.Node<K, V> f = n.next;
         if (n == b.next) {
            Object v = n.value;
            if (v == null) {
               n.helpDelete(b, f);
            } else if (n.casValue(v, (Object)null)) {
               if (!n.appendMarker(f) || !b.casNext(n, f)) {
                  this.findFirst();
               }

               this.clearIndexToFirst();
               K key = n.key;
               return keyOnly ? key : new ConcurrentSkipListMap.SnapshotEntry(key, v);
            }
         }
      }
   }

   private void clearIndexToFirst() {
      label24:
      while(true) {
         Object q = this.head;

         do {
            ConcurrentSkipListMap.Index<K, V> r = ((ConcurrentSkipListMap.Index)q).right;
            if (r != null && r.indexesDeletedNode() && !((ConcurrentSkipListMap.Index)q).unlink(r)) {
               continue label24;
            }
         } while((q = ((ConcurrentSkipListMap.Index)q).down) != null);

         if (this.head.right == null) {
            this.tryReduceLevel();
         }

         return;
      }
   }

   K pollFirstKey() {
      return this.doRemoveFirst(true);
   }

   ConcurrentSkipListMap.Node<K, V> findLast() {
      Object q = this.head;

      while(true) {
         ConcurrentSkipListMap.Index r;
         while((r = ((ConcurrentSkipListMap.Index)q).right) == null) {
            ConcurrentSkipListMap.Index d;
            if ((d = ((ConcurrentSkipListMap.Index)q).down) != null) {
               q = d;
            } else {
               ConcurrentSkipListMap.Node<K, V> b = ((ConcurrentSkipListMap.Index)q).node;
               ConcurrentSkipListMap.Node n = b.next;

               while(true) {
                  if (n == null) {
                     return b.isBaseHeader() ? null : b;
                  }

                  ConcurrentSkipListMap.Node<K, V> f = n.next;
                  if (n != b.next) {
                     break;
                  }

                  Object v = n.value;
                  if (v == null) {
                     n.helpDelete(b, f);
                     break;
                  }

                  if (v == n || b.value == null) {
                     break;
                  }

                  b = n;
                  n = f;
               }

               q = this.head;
            }
         }

         if (r.indexesDeletedNode()) {
            ((ConcurrentSkipListMap.Index)q).unlink(r);
            q = this.head;
         } else {
            q = r;
         }
      }
   }

   Object doRemoveLast(boolean keyOnly) {
      while(true) {
         ConcurrentSkipListMap.Node<K, V> b = this.findPredecessorOfLast();
         ConcurrentSkipListMap.Node<K, V> n = b.next;
         if (n == null) {
            if (b.isBaseHeader()) {
               return null;
            }
         } else {
            while(true) {
               ConcurrentSkipListMap.Node<K, V> f = n.next;
               if (n != b.next) {
                  break;
               }

               Object v = n.value;
               if (v == null) {
                  n.helpDelete(b, f);
                  break;
               }

               if (v == n || b.value == null) {
                  break;
               }

               if (f == null) {
                  if (!n.casValue(v, (Object)null)) {
                     break;
                  }

                  K key = n.key;
                  Comparable<K> ck = this.comparable(key);
                  if (n.appendMarker(f) && b.casNext(n, f)) {
                     this.findPredecessor(ck);
                     if (this.head.right == null) {
                        this.tryReduceLevel();
                     }
                  } else {
                     this.findNode(ck);
                  }

                  return keyOnly ? key : new ConcurrentSkipListMap.SnapshotEntry(key, v);
               }

               b = n;
               n = f;
            }
         }
      }
   }

   private ConcurrentSkipListMap.Node<K, V> findPredecessorOfLast() {
      label25:
      while(true) {
         Object q = this.head;

         while(true) {
            while(true) {
               ConcurrentSkipListMap.Index r;
               if ((r = ((ConcurrentSkipListMap.Index)q).right) != null) {
                  if (r.indexesDeletedNode()) {
                     ((ConcurrentSkipListMap.Index)q).unlink(r);
                     continue label25;
                  }

                  if (r.node.next != null) {
                     q = r;
                     continue;
                  }
               }

               ConcurrentSkipListMap.Index d;
               if ((d = ((ConcurrentSkipListMap.Index)q).down) == null) {
                  return ((ConcurrentSkipListMap.Index)q).node;
               }

               q = d;
            }
         }
      }
   }

   K pollLastKey() {
      return this.doRemoveLast(true);
   }

   ConcurrentSkipListMap.Node<K, V> findNear(K kkey, int rel) {
      Comparable key = this.comparable(kkey);

      label60:
      while(true) {
         ConcurrentSkipListMap.Node<K, V> b = this.findPredecessor(key);

         ConcurrentSkipListMap.Node f;
         for(ConcurrentSkipListMap.Node n = b.next; n != null; n = f) {
            f = n.next;
            if (n != b.next) {
               continue label60;
            }

            Object v = n.value;
            if (v == null) {
               n.helpDelete(b, f);
               continue label60;
            }

            if (v == n || b.value == null) {
               continue label60;
            }

            int c = key.compareTo(n.key);
            if (c == 0 && (rel & 1) != 0 || c < 0 && (rel & 2) == 0) {
               return n;
            }

            if (c <= 0 && (rel & 2) != 0) {
               return b.isBaseHeader() ? null : b;
            }

            b = n;
         }

         return (rel & 2) != 0 && !b.isBaseHeader() ? b : null;
      }
   }

   ConcurrentSkipListMap.SnapshotEntry<K, V> getNear(K kkey, int rel) {
      ConcurrentSkipListMap.SnapshotEntry e;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findNear(kkey, rel);
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   ConcurrentSkipListMap.Node<K, V> findCeiling(K key) {
      return key == null ? this.findFirst() : this.findNear(key, 1);
   }

   ConcurrentSkipListMap.Node<K, V> findLower(K key) {
      return key == null ? this.findLast() : this.findNear(key, 2);
   }

   Object getNear(K kkey, int rel, K least, K fence, boolean keyOnly) {
      K key = kkey;
      if ((rel & 2) == 0 && this.compare(kkey, least) < 0) {
         key = least;
         rel |= 1;
      }

      Object k;
      Object v;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findNear(key, rel);
         if (n == null || !this.inHalfOpenRange(n.key, least, fence)) {
            return null;
         }

         k = n.key;
         v = n.getValidValue();
      } while(v == null);

      return keyOnly ? k : new ConcurrentSkipListMap.SnapshotEntry(k, v);
   }

   Object removeFirstEntryOfSubrange(K least, K fence, boolean keyOnly) {
      Object k;
      Object v;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findCeiling(least);
         if (n == null) {
            return null;
         }

         k = n.key;
         if (fence != null && this.compare(k, fence) >= 0) {
            return null;
         }

         v = this.doRemove(k, (Object)null);
      } while(v == null);

      return keyOnly ? k : new ConcurrentSkipListMap.SnapshotEntry(k, v);
   }

   Object removeLastEntryOfSubrange(K least, K fence, boolean keyOnly) {
      Object k;
      Object v;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findLower(fence);
         if (n == null) {
            return null;
         }

         k = n.key;
         if (least != null && this.compare(k, least) < 0) {
            return null;
         }

         v = this.doRemove(k, (Object)null);
      } while(v == null);

      return keyOnly ? k : new ConcurrentSkipListMap.SnapshotEntry(k, v);
   }

   public ConcurrentSkipListMap() {
      this.comparator = null;
      this.initialize();
   }

   public ConcurrentSkipListMap(Comparator<? super K> c) {
      this.comparator = c;
      this.initialize();
   }

   public ConcurrentSkipListMap(Map<? extends K, ? extends V> m) {
      this.comparator = null;
      this.initialize();
      this.putAll(m);
   }

   public ConcurrentSkipListMap(SortedMap<K, ? extends V> m) {
      this.comparator = m.comparator();
      this.initialize();
      this.buildFromSorted(m);
   }

   public Object clone() {
      ConcurrentSkipListMap clone = null;

      try {
         clone = (ConcurrentSkipListMap)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      clone.initialize();
      clone.buildFromSorted(this);
      return clone;
   }

   private void buildFromSorted(SortedMap<K, ? extends V> map) {
      if (map == null) {
         throw new NullPointerException();
      } else {
         ConcurrentSkipListMap.HeadIndex<K, V> h = this.head;
         ConcurrentSkipListMap.Node<K, V> basepred = h.node;
         ArrayList<ConcurrentSkipListMap.Index<K, V>> preds = new ArrayList();

         for(int i = 0; i <= h.level; ++i) {
            preds.add((Object)null);
         }

         ConcurrentSkipListMap.Index<K, V> q = h;

         for(int i = h.level; i > 0; --i) {
            preds.set(i, q);
            q = ((ConcurrentSkipListMap.Index)q).down;
         }

         Iterator it = map.entrySet().iterator();

         while(it.hasNext()) {
            Entry<? extends K, ? extends V> e = (Entry)it.next();
            int j = this.randomLevel();
            if (j > h.level) {
               j = h.level + 1;
            }

            K k = e.getKey();
            V v = e.getValue();
            if (k == null || v == null) {
               throw new NullPointerException();
            }

            ConcurrentSkipListMap.Node<K, V> z = new ConcurrentSkipListMap.Node(k, v, (ConcurrentSkipListMap.Node)null);
            basepred.next = z;
            basepred = z;
            if (j > 0) {
               ConcurrentSkipListMap.Index<K, V> idx = null;

               for(int i = 1; i <= j; ++i) {
                  idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
                  if (i > h.level) {
                     h = new ConcurrentSkipListMap.HeadIndex(h.node, h, idx, i);
                  }

                  if (i < preds.size()) {
                     ((ConcurrentSkipListMap.Index)preds.get(i)).right = idx;
                     preds.set(i, idx);
                  } else {
                     preds.add(idx);
                  }
               }
            }
         }

         this.head = h;
      }
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for(ConcurrentSkipListMap.Node n = this.findFirst(); n != null; n = n.next) {
         V v = n.getValidValue();
         if (v != null) {
            s.writeObject(n.key);
            s.writeObject(v);
         }
      }

      s.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.initialize();
      ConcurrentSkipListMap.HeadIndex<K, V> h = this.head;
      ConcurrentSkipListMap.Node<K, V> basepred = h.node;
      ArrayList<ConcurrentSkipListMap.Index<K, V>> preds = new ArrayList();

      for(int i = 0; i <= h.level; ++i) {
         preds.add((Object)null);
      }

      ConcurrentSkipListMap.Index<K, V> q = h;

      for(int i = h.level; i > 0; --i) {
         preds.set(i, q);
         q = ((ConcurrentSkipListMap.Index)q).down;
      }

      while(true) {
         Object k = s.readObject();
         if (k == null) {
            this.head = h;
            return;
         }

         Object v = s.readObject();
         if (v == null) {
            throw new NullPointerException();
         }

         int j = this.randomLevel();
         if (j > h.level) {
            j = h.level + 1;
         }

         ConcurrentSkipListMap.Node<K, V> z = new ConcurrentSkipListMap.Node(k, v, (ConcurrentSkipListMap.Node)null);
         basepred.next = z;
         basepred = z;
         if (j > 0) {
            ConcurrentSkipListMap.Index<K, V> idx = null;

            for(int i = 1; i <= j; ++i) {
               idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
               if (i > h.level) {
                  h = new ConcurrentSkipListMap.HeadIndex(h.node, h, idx, i);
               }

               if (i < preds.size()) {
                  ((ConcurrentSkipListMap.Index)preds.get(i)).right = idx;
                  preds.set(i, idx);
               } else {
                  preds.add(idx);
               }
            }
         }
      }
   }

   public boolean containsKey(Object key) {
      return this.doGet(key) != null;
   }

   public V get(Object key) {
      return this.doGet(key);
   }

   public V put(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return this.doPut(key, value, false);
      }
   }

   public V remove(Object key) {
      return this.doRemove(key, (Object)null);
   }

   public boolean containsValue(Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         for(ConcurrentSkipListMap.Node n = this.findFirst(); n != null; n = n.next) {
            V v = n.getValidValue();
            if (v != null && value.equals(v)) {
               return true;
            }
         }

         return false;
      }
   }

   public int size() {
      long count = 0L;

      for(ConcurrentSkipListMap.Node n = this.findFirst(); n != null; n = n.next) {
         if (n.getValidValue() != null) {
            ++count;
         }
      }

      return count >= 2147483647L ? Integer.MAX_VALUE : (int)count;
   }

   public boolean isEmpty() {
      return this.findFirst() == null;
   }

   public void clear() {
      this.initialize();
   }

   public Set<K> keySet() {
      ConcurrentSkipListMap<K, V>.KeySet ks = this.keySet;
      return ks != null ? ks : (this.keySet = new ConcurrentSkipListMap.KeySet());
   }

   public Set<K> descendingKeySet() {
      ConcurrentSkipListMap<K, V>.DescendingKeySet ks = this.descendingKeySet;
      return ks != null ? ks : (this.descendingKeySet = new ConcurrentSkipListMap.DescendingKeySet());
   }

   public Collection<V> values() {
      ConcurrentSkipListMap<K, V>.Values vs = this.values;
      return vs != null ? vs : (this.values = new ConcurrentSkipListMap.Values());
   }

   public Set<Entry<K, V>> entrySet() {
      ConcurrentSkipListMap<K, V>.EntrySet es = this.entrySet;
      return es != null ? es : (this.entrySet = new ConcurrentSkipListMap.EntrySet());
   }

   public Set<Entry<K, V>> descendingEntrySet() {
      ConcurrentSkipListMap<K, V>.DescendingEntrySet es = this.descendingEntrySet;
      return es != null ? es : (this.descendingEntrySet = new ConcurrentSkipListMap.DescendingEntrySet());
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map t = (Map)o;

         try {
            return containsAllMappings(this, t) && containsAllMappings(t, this);
         } catch (ClassCastException var4) {
            return false;
         } catch (NullPointerException var5) {
            return false;
         }
      }
   }

   static <K, V> boolean containsAllMappings(Map<K, V> a, Map<K, V> b) {
      Iterator it = b.entrySet().iterator();

      Object k;
      Object v;
      do {
         if (!it.hasNext()) {
            return true;
         }

         Entry<K, V> e = (Entry)it.next();
         k = e.getKey();
         v = e.getValue();
      } while(k != null && v != null && v.equals(a.get(k)));

      return false;
   }

   public V putIfAbsent(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return this.doPut(key, value, true);
      }
   }

   public boolean remove(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return this.doRemove(key, value) != null;
      }
   }

   public boolean replace(K key, V oldValue, V newValue) {
      if (oldValue != null && newValue != null) {
         Comparable k = this.comparable(key);

         while(true) {
            ConcurrentSkipListMap.Node<K, V> n = this.findNode(k);
            if (n == null) {
               return false;
            }

            Object v = n.value;
            if (v != null) {
               if (!oldValue.equals(v)) {
                  return false;
               }

               if (n.casValue(v, newValue)) {
                  return true;
               }
            }
         }
      } else {
         throw new NullPointerException();
      }
   }

   public V replace(K key, V value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         Comparable k = this.comparable(key);

         ConcurrentSkipListMap.Node n;
         Object v;
         do {
            n = this.findNode(k);
            if (n == null) {
               return null;
            }

            v = n.value;
         } while(v == null || !n.casValue(v, value));

         return v;
      }
   }

   public Comparator<? super K> comparator() {
      return this.comparator;
   }

   public K firstKey() {
      ConcurrentSkipListMap.Node<K, V> n = this.findFirst();
      if (n == null) {
         throw new NoSuchElementException();
      } else {
         return n.key;
      }
   }

   public K lastKey() {
      ConcurrentSkipListMap.Node<K, V> n = this.findLast();
      if (n == null) {
         throw new NoSuchElementException();
      } else {
         return n.key;
      }
   }

   public ConcurrentNavigableMap<K, V> subMap(K fromKey, K toKey) {
      if (fromKey != null && toKey != null) {
         return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this, fromKey, toKey);
      } else {
         throw new NullPointerException();
      }
   }

   public ConcurrentNavigableMap<K, V> headMap(K toKey) {
      if (toKey == null) {
         throw new NullPointerException();
      } else {
         return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this, (Object)null, toKey);
      }
   }

   public ConcurrentNavigableMap<K, V> tailMap(K fromKey) {
      if (fromKey == null) {
         throw new NullPointerException();
      } else {
         return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this, fromKey, (Object)null);
      }
   }

   public Entry<K, V> ceilingEntry(K key) {
      return this.getNear(key, 1);
   }

   public K ceilingKey(K key) {
      ConcurrentSkipListMap.Node<K, V> n = this.findNear(key, 1);
      return n == null ? null : n.key;
   }

   public Entry<K, V> lowerEntry(K key) {
      return this.getNear(key, 2);
   }

   public K lowerKey(K key) {
      ConcurrentSkipListMap.Node<K, V> n = this.findNear(key, 2);
      return n == null ? null : n.key;
   }

   public Entry<K, V> floorEntry(K key) {
      return this.getNear(key, 3);
   }

   public K floorKey(K key) {
      ConcurrentSkipListMap.Node<K, V> n = this.findNear(key, 3);
      return n == null ? null : n.key;
   }

   public Entry<K, V> higherEntry(K key) {
      return this.getNear(key, 0);
   }

   public K higherKey(K key) {
      ConcurrentSkipListMap.Node<K, V> n = this.findNear(key, 0);
      return n == null ? null : n.key;
   }

   public Entry<K, V> firstEntry() {
      ConcurrentSkipListMap.SnapshotEntry e;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findFirst();
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   public Entry<K, V> lastEntry() {
      ConcurrentSkipListMap.SnapshotEntry e;
      do {
         ConcurrentSkipListMap.Node<K, V> n = this.findLast();
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   public Entry<K, V> pollFirstEntry() {
      return (ConcurrentSkipListMap.SnapshotEntry)this.doRemoveFirst(false);
   }

   public Entry<K, V> pollLastEntry() {
      return (ConcurrentSkipListMap.SnapshotEntry)this.doRemoveLast(false);
   }

   Iterator<K> keyIterator() {
      return new ConcurrentSkipListMap.KeyIterator();
   }

   Iterator<K> descendingKeyIterator() {
      return new ConcurrentSkipListMap.DescendingKeyIterator();
   }

   ConcurrentSkipListMap<K, V>.SubMapEntryIterator subMapEntryIterator(K least, K fence) {
      return new ConcurrentSkipListMap.SubMapEntryIterator(least, fence);
   }

   ConcurrentSkipListMap<K, V>.DescendingSubMapEntryIterator descendingSubMapEntryIterator(K least, K fence) {
      return new ConcurrentSkipListMap.DescendingSubMapEntryIterator(least, fence);
   }

   ConcurrentSkipListMap<K, V>.SubMapKeyIterator subMapKeyIterator(K least, K fence) {
      return new ConcurrentSkipListMap.SubMapKeyIterator(least, fence);
   }

   ConcurrentSkipListMap<K, V>.DescendingSubMapKeyIterator descendingSubMapKeyIterator(K least, K fence) {
      return new ConcurrentSkipListMap.DescendingSubMapKeyIterator(least, fence);
   }

   ConcurrentSkipListMap<K, V>.SubMapValueIterator subMapValueIterator(K least, K fence) {
      return new ConcurrentSkipListMap.SubMapValueIterator(least, fence);
   }

   static class ConcurrentSkipListSubMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Serializable {
      private static final long serialVersionUID = -7647078645895051609L;
      private final ConcurrentSkipListMap<K, V> m;
      private final K least;
      private final K fence;
      private transient Set<K> keySetView;
      private transient Set<Entry<K, V>> entrySetView;
      private transient Collection<V> valuesView;
      private transient Set<K> descendingKeySetView;
      private transient Set<Entry<K, V>> descendingEntrySetView;

      ConcurrentSkipListSubMap(ConcurrentSkipListMap<K, V> map, K least, K fence) {
         if (least != null && fence != null && map.compare(least, fence) > 0) {
            throw new IllegalArgumentException("inconsistent range");
         } else {
            this.m = map;
            this.least = least;
            this.fence = fence;
         }
      }

      boolean inHalfOpenRange(K key) {
         return this.m.inHalfOpenRange(key, this.least, this.fence);
      }

      boolean inOpenRange(K key) {
         return this.m.inOpenRange(key, this.least, this.fence);
      }

      ConcurrentSkipListMap.Node<K, V> firstNode() {
         return this.m.findCeiling(this.least);
      }

      ConcurrentSkipListMap.Node<K, V> lastNode() {
         return this.m.findLower(this.fence);
      }

      boolean isBeforeEnd(ConcurrentSkipListMap.Node<K, V> n) {
         return n != null && (this.fence == null || n.key == null || this.m.compare(this.fence, n.key) > 0);
      }

      void checkKey(K key) throws IllegalArgumentException {
         if (!this.inHalfOpenRange(key)) {
            throw new IllegalArgumentException("key out of range");
         }
      }

      ConcurrentSkipListMap<K, V> getMap() {
         return this.m;
      }

      K getLeast() {
         return this.least;
      }

      K getFence() {
         return this.fence;
      }

      public boolean containsKey(Object key) {
         return this.inHalfOpenRange(key) && this.m.containsKey(key);
      }

      public V get(Object key) {
         return !this.inHalfOpenRange(key) ? null : this.m.get(key);
      }

      public V put(K key, V value) {
         this.checkKey(key);
         return this.m.put(key, value);
      }

      public V remove(Object key) {
         return !this.inHalfOpenRange(key) ? null : this.m.remove(key);
      }

      public int size() {
         long count = 0L;

         for(ConcurrentSkipListMap.Node n = this.firstNode(); this.isBeforeEnd(n); n = n.next) {
            if (n.getValidValue() != null) {
               ++count;
            }
         }

         return count >= 2147483647L ? Integer.MAX_VALUE : (int)count;
      }

      public boolean isEmpty() {
         return !this.isBeforeEnd(this.firstNode());
      }

      public boolean containsValue(Object value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            for(ConcurrentSkipListMap.Node n = this.firstNode(); this.isBeforeEnd(n); n = n.next) {
               V v = n.getValidValue();
               if (v != null && value.equals(v)) {
                  return true;
               }
            }

            return false;
         }
      }

      public void clear() {
         for(ConcurrentSkipListMap.Node n = this.firstNode(); this.isBeforeEnd(n); n = n.next) {
            if (n.getValidValue() != null) {
               this.m.remove(n.key);
            }
         }

      }

      public V putIfAbsent(K key, V value) {
         this.checkKey(key);
         return this.m.putIfAbsent(key, value);
      }

      public boolean remove(Object key, Object value) {
         return this.inHalfOpenRange(key) && this.m.remove(key, value);
      }

      public boolean replace(K key, V oldValue, V newValue) {
         this.checkKey(key);
         return this.m.replace(key, oldValue, newValue);
      }

      public V replace(K key, V value) {
         this.checkKey(key);
         return this.m.replace(key, value);
      }

      public Comparator<? super K> comparator() {
         return this.m.comparator();
      }

      public K firstKey() {
         ConcurrentSkipListMap.Node<K, V> n = this.firstNode();
         if (this.isBeforeEnd(n)) {
            return n.key;
         } else {
            throw new NoSuchElementException();
         }
      }

      public K lastKey() {
         ConcurrentSkipListMap.Node<K, V> n = this.lastNode();
         if (n != null) {
            K last = n.key;
            if (this.inHalfOpenRange(last)) {
               return last;
            }
         }

         throw new NoSuchElementException();
      }

      public ConcurrentNavigableMap<K, V> subMap(K fromKey, K toKey) {
         if (fromKey != null && toKey != null) {
            if (this.inOpenRange(fromKey) && this.inOpenRange(toKey)) {
               return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this.m, fromKey, toKey);
            } else {
               throw new IllegalArgumentException("key out of range");
            }
         } else {
            throw new NullPointerException();
         }
      }

      public ConcurrentNavigableMap<K, V> headMap(K toKey) {
         if (toKey == null) {
            throw new NullPointerException();
         } else if (!this.inOpenRange(toKey)) {
            throw new IllegalArgumentException("key out of range");
         } else {
            return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this.m, this.least, toKey);
         }
      }

      public ConcurrentNavigableMap<K, V> tailMap(K fromKey) {
         if (fromKey == null) {
            throw new NullPointerException();
         } else if (!this.inOpenRange(fromKey)) {
            throw new IllegalArgumentException("key out of range");
         } else {
            return new ConcurrentSkipListMap.ConcurrentSkipListSubMap(this.m, fromKey, this.fence);
         }
      }

      public Entry<K, V> ceilingEntry(K key) {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.getNear(key, 0 | 1, this.least, this.fence, false);
      }

      public K ceilingKey(K key) {
         return this.m.getNear(key, 0 | 1, this.least, this.fence, true);
      }

      public Entry<K, V> lowerEntry(K key) {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.getNear(key, 2, this.least, this.fence, false);
      }

      public K lowerKey(K key) {
         return this.m.getNear(key, 2, this.least, this.fence, true);
      }

      public Entry<K, V> floorEntry(K key) {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.getNear(key, 2 | 1, this.least, this.fence, false);
      }

      public K floorKey(K key) {
         return this.m.getNear(key, 2 | 1, this.least, this.fence, true);
      }

      public Entry<K, V> higherEntry(K key) {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.getNear(key, 0, this.least, this.fence, false);
      }

      public K higherKey(K key) {
         return this.m.getNear(key, 0, this.least, this.fence, true);
      }

      public Entry<K, V> firstEntry() {
         ConcurrentSkipListMap.SnapshotEntry e;
         do {
            ConcurrentSkipListMap.Node<K, V> n = this.firstNode();
            if (!this.isBeforeEnd(n)) {
               return null;
            }

            e = n.createSnapshot();
         } while(e == null);

         return e;
      }

      public Entry<K, V> lastEntry() {
         while(true) {
            ConcurrentSkipListMap.Node<K, V> n = this.lastNode();
            if (n != null && this.inHalfOpenRange(n.key)) {
               Entry<K, V> e = n.createSnapshot();
               if (e == null) {
                  continue;
               }

               return e;
            }

            return null;
         }
      }

      public Entry<K, V> pollFirstEntry() {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.removeFirstEntryOfSubrange(this.least, this.fence, false);
      }

      public Entry<K, V> pollLastEntry() {
         return (ConcurrentSkipListMap.SnapshotEntry)this.m.removeLastEntryOfSubrange(this.least, this.fence, false);
      }

      public Set<K> keySet() {
         Set<K> ks = this.keySetView;
         return ks != null ? ks : (this.keySetView = new ConcurrentSkipListMap.ConcurrentSkipListSubMap.KeySetView());
      }

      public Set<K> descendingKeySet() {
         Set<K> ks = this.descendingKeySetView;
         return ks != null ? ks : (this.descendingKeySetView = new ConcurrentSkipListMap.ConcurrentSkipListSubMap.DescendingKeySetView());
      }

      public Collection<V> values() {
         Collection<V> vs = this.valuesView;
         return vs != null ? vs : (this.valuesView = new ConcurrentSkipListMap.ConcurrentSkipListSubMap.ValuesView());
      }

      public Set<Entry<K, V>> entrySet() {
         Set<Entry<K, V>> es = this.entrySetView;
         return es != null ? es : (this.entrySetView = new ConcurrentSkipListMap.ConcurrentSkipListSubMap.EntrySetView());
      }

      public Set<Entry<K, V>> descendingEntrySet() {
         Set<Entry<K, V>> es = this.descendingEntrySetView;
         return es != null ? es : (this.descendingEntrySetView = new ConcurrentSkipListMap.ConcurrentSkipListSubMap.DescendingEntrySetView());
      }

      class DescendingEntrySetView extends ConcurrentSkipListMap.ConcurrentSkipListSubMap<K, V>.EntrySetView {
         DescendingEntrySetView() {
            super();
         }

         public Iterator<Entry<K, V>> iterator() {
            return ConcurrentSkipListSubMap.this.m.descendingSubMapEntryIterator(ConcurrentSkipListSubMap.this.least, ConcurrentSkipListSubMap.this.fence);
         }
      }

      class EntrySetView extends AbstractSet<Entry<K, V>> {
         public Iterator<Entry<K, V>> iterator() {
            return ConcurrentSkipListSubMap.this.m.subMapEntryIterator(ConcurrentSkipListSubMap.this.least, ConcurrentSkipListSubMap.this.fence);
         }

         public int size() {
            return ConcurrentSkipListSubMap.this.size();
         }

         public boolean isEmpty() {
            return ConcurrentSkipListSubMap.this.isEmpty();
         }

         public boolean contains(Object o) {
            if (!(o instanceof Entry)) {
               return false;
            } else {
               Entry<K, V> e = (Entry)o;
               K key = e.getKey();
               if (!ConcurrentSkipListSubMap.this.inHalfOpenRange(key)) {
                  return false;
               } else {
                  V v = ConcurrentSkipListSubMap.this.m.get(key);
                  return v != null && v.equals(e.getValue());
               }
            }
         }

         public boolean remove(Object o) {
            if (!(o instanceof Entry)) {
               return false;
            } else {
               Entry<K, V> e = (Entry)o;
               K key = e.getKey();
               return !ConcurrentSkipListSubMap.this.inHalfOpenRange(key) ? false : ConcurrentSkipListSubMap.this.m.remove(key, e.getValue());
            }
         }

         public Object[] toArray() {
            Collection<Entry<K, V>> c = new ArrayList();
            Iterator i$ = this.iterator();

            while(i$.hasNext()) {
               Entry e = (Entry)i$.next();
               c.add(new ConcurrentSkipListMap.SnapshotEntry(e.getKey(), e.getValue()));
            }

            return c.toArray();
         }

         public <T> T[] toArray(T[] a) {
            Collection<Entry<K, V>> c = new ArrayList();
            Iterator i$ = this.iterator();

            while(i$.hasNext()) {
               Entry e = (Entry)i$.next();
               c.add(new ConcurrentSkipListMap.SnapshotEntry(e.getKey(), e.getValue()));
            }

            return c.toArray(a);
         }
      }

      class ValuesView extends AbstractCollection<V> {
         public Iterator<V> iterator() {
            return ConcurrentSkipListSubMap.this.m.subMapValueIterator(ConcurrentSkipListSubMap.this.least, ConcurrentSkipListSubMap.this.fence);
         }

         public int size() {
            return ConcurrentSkipListSubMap.this.size();
         }

         public boolean isEmpty() {
            return ConcurrentSkipListSubMap.this.isEmpty();
         }

         public boolean contains(Object v) {
            return ConcurrentSkipListSubMap.this.containsValue(v);
         }

         public Object[] toArray() {
            Collection<V> c = new ArrayList();
            Iterator i = this.iterator();

            while(i.hasNext()) {
               c.add(i.next());
            }

            return c.toArray();
         }

         public <T> T[] toArray(T[] a) {
            Collection<V> c = new ArrayList();
            Iterator i = this.iterator();

            while(i.hasNext()) {
               c.add(i.next());
            }

            return c.toArray(a);
         }
      }

      class DescendingKeySetView extends ConcurrentSkipListMap.ConcurrentSkipListSubMap<K, V>.KeySetView {
         DescendingKeySetView() {
            super();
         }

         public Iterator<K> iterator() {
            return ConcurrentSkipListSubMap.this.m.descendingSubMapKeyIterator(ConcurrentSkipListSubMap.this.least, ConcurrentSkipListSubMap.this.fence);
         }
      }

      class KeySetView extends AbstractSet<K> {
         public Iterator<K> iterator() {
            return ConcurrentSkipListSubMap.this.m.subMapKeyIterator(ConcurrentSkipListSubMap.this.least, ConcurrentSkipListSubMap.this.fence);
         }

         public int size() {
            return ConcurrentSkipListSubMap.this.size();
         }

         public boolean isEmpty() {
            return ConcurrentSkipListSubMap.this.isEmpty();
         }

         public boolean contains(Object k) {
            return ConcurrentSkipListSubMap.this.containsKey(k);
         }

         public Object[] toArray() {
            Collection<K> c = new ArrayList();
            Iterator i = this.iterator();

            while(i.hasNext()) {
               c.add(i.next());
            }

            return c.toArray();
         }

         public <T> T[] toArray(T[] a) {
            Collection<K> c = new ArrayList();
            Iterator i = this.iterator();

            while(i.hasNext()) {
               c.add(i.next());
            }

            return c.toArray(a);
         }
      }
   }

   class DescendingEntrySet extends ConcurrentSkipListMap<K, V>.EntrySet {
      DescendingEntrySet() {
         super();
      }

      public Iterator<Entry<K, V>> iterator() {
         return ConcurrentSkipListMap.this.new DescendingEntryIterator();
      }
   }

   class EntrySet extends AbstractSet<Entry<K, V>> {
      public Iterator<Entry<K, V>> iterator() {
         return ConcurrentSkipListMap.this.new EntryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry<K, V> e = (Entry)o;
            V v = ConcurrentSkipListMap.this.get(e.getKey());
            return v != null && v.equals(e.getValue());
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry<K, V> e = (Entry)o;
            return ConcurrentSkipListMap.this.remove(e.getKey(), e.getValue());
         }
      }

      public boolean isEmpty() {
         return ConcurrentSkipListMap.this.isEmpty();
      }

      public int size() {
         return ConcurrentSkipListMap.this.size();
      }

      public void clear() {
         ConcurrentSkipListMap.this.clear();
      }

      public Object[] toArray() {
         Collection<Entry<K, V>> c = new ArrayList();
         Iterator i$ = this.iterator();

         while(i$.hasNext()) {
            Entry e = (Entry)i$.next();
            c.add(new ConcurrentSkipListMap.SnapshotEntry(e.getKey(), e.getValue()));
         }

         return c.toArray();
      }

      public <T> T[] toArray(T[] a) {
         Collection<Entry<K, V>> c = new ArrayList();
         Iterator i$ = this.iterator();

         while(i$.hasNext()) {
            Entry e = (Entry)i$.next();
            c.add(new ConcurrentSkipListMap.SnapshotEntry(e.getKey(), e.getValue()));
         }

         return c.toArray(a);
      }
   }

   final class Values extends AbstractCollection<V> {
      public Iterator<V> iterator() {
         return ConcurrentSkipListMap.this.new ValueIterator();
      }

      public boolean isEmpty() {
         return ConcurrentSkipListMap.this.isEmpty();
      }

      public int size() {
         return ConcurrentSkipListMap.this.size();
      }

      public boolean contains(Object o) {
         return ConcurrentSkipListMap.this.containsValue(o);
      }

      public void clear() {
         ConcurrentSkipListMap.this.clear();
      }

      public Object[] toArray() {
         Collection<V> c = new ArrayList();
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray();
      }

      public <T> T[] toArray(T[] a) {
         Collection<V> c = new ArrayList();
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray(a);
      }
   }

   class DescendingKeySet extends ConcurrentSkipListMap<K, V>.KeySet {
      DescendingKeySet() {
         super();
      }

      public Iterator<K> iterator() {
         return ConcurrentSkipListMap.this.new DescendingKeyIterator();
      }
   }

   class KeySet extends AbstractSet<K> {
      public Iterator<K> iterator() {
         return ConcurrentSkipListMap.this.new KeyIterator();
      }

      public boolean isEmpty() {
         return ConcurrentSkipListMap.this.isEmpty();
      }

      public int size() {
         return ConcurrentSkipListMap.this.size();
      }

      public boolean contains(Object o) {
         return ConcurrentSkipListMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         return ConcurrentSkipListMap.this.removep(o);
      }

      public void clear() {
         ConcurrentSkipListMap.this.clear();
      }

      public Object[] toArray() {
         Collection<K> c = new ArrayList();
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray();
      }

      public <T> T[] toArray(T[] a) {
         Collection<K> c = new ArrayList();
         Iterator i = this.iterator();

         while(i.hasNext()) {
            c.add(i.next());
         }

         return c.toArray(a);
      }
   }

   final class DescendingSubMapEntryIterator extends ConcurrentSkipListMap<K, V>.EntryIter implements Iterator<Entry<K, V>> {
      final K least;

      DescendingSubMapEntryIterator(K least, K fence) {
         super();
         this.initDescending(least, fence);
         this.least = least;
      }

      public Entry<K, V> next() {
         this.lastValue = this.nextValue;
         this.descend(this.least);
         return this;
      }
   }

   final class DescendingEntryIterator extends ConcurrentSkipListMap<K, V>.EntryIter implements Iterator<Entry<K, V>> {
      DescendingEntryIterator() {
         super();
         this.initDescending();
      }

      public Entry<K, V> next() {
         this.lastValue = this.nextValue;
         this.descend();
         return this;
      }
   }

   final class SubMapEntryIterator extends ConcurrentSkipListMap<K, V>.EntryIter implements Iterator<Entry<K, V>> {
      final K fence;

      SubMapEntryIterator(K least, K fence) {
         super();
         this.initAscending(least, fence);
         this.fence = fence;
      }

      public Entry<K, V> next() {
         this.lastValue = this.nextValue;
         this.ascend(this.fence);
         return this;
      }
   }

   final class EntryIterator extends ConcurrentSkipListMap<K, V>.EntryIter implements Iterator<Entry<K, V>> {
      EntryIterator() {
         super();
         this.initAscending();
      }

      public Entry<K, V> next() {
         this.lastValue = this.nextValue;
         this.ascend();
         return this;
      }
   }

   abstract class EntryIter extends ConcurrentSkipListMap<K, V>.Iter implements Entry<K, V> {
      Object lastValue;

      EntryIter() {
         super();
      }

      public K getKey() {
         ConcurrentSkipListMap.Node<K, V> l = this.last;
         if (l == null) {
            throw new IllegalStateException();
         } else {
            return l.key;
         }
      }

      public V getValue() {
         Object v = this.lastValue;
         if (this.last != null && v != null) {
            return v;
         } else {
            throw new IllegalStateException();
         }
      }

      public V setValue(V value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (this.last == null) {
            return super.equals(o);
         } else if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return this.getKey().equals(e.getKey()) && this.getValue().equals(e.getValue());
         }
      }

      public int hashCode() {
         return this.last == null ? super.hashCode() : this.getKey().hashCode() ^ this.getValue().hashCode();
      }

      public String toString() {
         return this.last == null ? super.toString() : this.getKey() + "=" + this.getValue();
      }
   }

   final class DescendingSubMapKeyIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<K> {
      final K least;

      DescendingSubMapKeyIterator(K least, K fence) {
         super();
         this.initDescending(least, fence);
         this.least = least;
      }

      public K next() {
         ConcurrentSkipListMap.Node<K, V> n = this.next;
         this.descend(this.least);
         return n.key;
      }
   }

   final class DescendingKeyIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<K> {
      DescendingKeyIterator() {
         super();
         this.initDescending();
      }

      public K next() {
         ConcurrentSkipListMap.Node<K, V> n = this.next;
         this.descend();
         return n.key;
      }
   }

   final class SubMapKeyIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<K> {
      final K fence;

      SubMapKeyIterator(K least, K fence) {
         super();
         this.initAscending(least, fence);
         this.fence = fence;
      }

      public K next() {
         ConcurrentSkipListMap.Node<K, V> n = this.next;
         this.ascend(this.fence);
         return n.key;
      }
   }

   class SubMapValueIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<V> {
      final K fence;

      SubMapValueIterator(K least, K fence) {
         super();
         this.initAscending(least, fence);
         this.fence = fence;
      }

      public V next() {
         Object v = this.nextValue;
         this.ascend(this.fence);
         return v;
      }
   }

   final class KeyIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<K> {
      KeyIterator() {
         super();
         this.initAscending();
      }

      public K next() {
         ConcurrentSkipListMap.Node<K, V> n = this.next;
         this.ascend();
         return n.key;
      }
   }

   final class ValueIterator extends ConcurrentSkipListMap<K, V>.Iter implements Iterator<V> {
      ValueIterator() {
         super();
         this.initAscending();
      }

      public V next() {
         Object v = this.nextValue;
         this.ascend();
         return v;
      }
   }

   abstract class Iter {
      ConcurrentSkipListMap.Node<K, V> last;
      ConcurrentSkipListMap.Node<K, V> next;
      Object nextValue;

      public final boolean hasNext() {
         return this.next != null;
      }

      final void initAscending() {
         while(true) {
            this.next = ConcurrentSkipListMap.this.findFirst();
            if (this.next != null) {
               this.nextValue = this.next.value;
               if (this.nextValue == null || this.nextValue == this.next) {
                  continue;
               }
            }

            return;
         }
      }

      final void initAscending(K least, K fence) {
         while(true) {
            this.next = ConcurrentSkipListMap.this.findCeiling(least);
            if (this.next != null) {
               this.nextValue = this.next.value;
               if (this.nextValue == null || this.nextValue == this.next) {
                  continue;
               }

               if (fence != null && ConcurrentSkipListMap.this.compare(fence, this.next.key) <= 0) {
                  this.next = null;
                  this.nextValue = null;
               }
            }

            return;
         }
      }

      final void ascend() {
         if ((this.last = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            do {
               this.next = this.next.next;
               if (this.next == null) {
                  break;
               }

               this.nextValue = this.next.value;
            } while(this.nextValue == null || this.nextValue == this.next);

         }
      }

      final void ascend(K fence) {
         if ((this.last = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            while(true) {
               this.next = this.next.next;
               if (this.next == null) {
                  break;
               }

               this.nextValue = this.next.value;
               if (this.nextValue != null && this.nextValue != this.next) {
                  if (fence != null && ConcurrentSkipListMap.this.compare(fence, this.next.key) <= 0) {
                     this.next = null;
                     this.nextValue = null;
                  }
                  break;
               }
            }

         }
      }

      final void initDescending() {
         while(true) {
            this.next = ConcurrentSkipListMap.this.findLast();
            if (this.next != null) {
               this.nextValue = this.next.value;
               if (this.nextValue == null || this.nextValue == this.next) {
                  continue;
               }
            }

            return;
         }
      }

      final void initDescending(K least, K fence) {
         while(true) {
            this.next = ConcurrentSkipListMap.this.findLower(fence);
            if (this.next != null) {
               this.nextValue = this.next.value;
               if (this.nextValue == null || this.nextValue == this.next) {
                  continue;
               }

               if (least != null && ConcurrentSkipListMap.this.compare(least, this.next.key) > 0) {
                  this.next = null;
                  this.nextValue = null;
               }
            }

            return;
         }
      }

      final void descend() {
         if ((this.last = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            Object k = this.last.key;

            do {
               this.next = ConcurrentSkipListMap.this.findNear(k, 2);
               if (this.next == null) {
                  break;
               }

               this.nextValue = this.next.value;
            } while(this.nextValue == null || this.nextValue == this.next);

         }
      }

      final void descend(K least) {
         if ((this.last = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            Object k = this.last.key;

            while(true) {
               this.next = ConcurrentSkipListMap.this.findNear(k, 2);
               if (this.next == null) {
                  break;
               }

               this.nextValue = this.next.value;
               if (this.nextValue != null && this.nextValue != this.next) {
                  if (least != null && ConcurrentSkipListMap.this.compare(least, this.next.key) > 0) {
                     this.next = null;
                     this.nextValue = null;
                  }
                  break;
               }
            }

         }
      }

      public void remove() {
         ConcurrentSkipListMap.Node<K, V> l = this.last;
         if (l == null) {
            throw new IllegalStateException();
         } else {
            ConcurrentSkipListMap.this.remove(l.key);
         }
      }
   }

   static final class ComparableUsingComparator<K> implements Comparable<K> {
      final K actualKey;
      final Comparator<? super K> cmp;

      ComparableUsingComparator(K key, Comparator<? super K> cmp) {
         this.actualKey = key;
         this.cmp = cmp;
      }

      public int compareTo(K k2) {
         return this.cmp.compare(this.actualKey, k2);
      }
   }

   static class SnapshotEntry<K, V> implements Entry<K, V> {
      private final K key;
      private final V value;

      SnapshotEntry(K key, V value) {
         this.key = key;
         this.value = value;
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            boolean var10000;
            label38: {
               label27: {
                  Entry e = (Entry)o;
                  if (this.key == null) {
                     if (e.getKey() != null) {
                        break label27;
                     }
                  } else if (!this.key.equals(e.getKey())) {
                     break label27;
                  }

                  if (this.value == null) {
                     if (e.getValue() == null) {
                        break label38;
                     }
                  } else if (this.value.equals(e.getValue())) {
                     break label38;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   static final class HeadIndex<K, V> extends ConcurrentSkipListMap.Index<K, V> {
      final int level;

      HeadIndex(ConcurrentSkipListMap.Node<K, V> node, ConcurrentSkipListMap.Index<K, V> down, ConcurrentSkipListMap.Index<K, V> right, int level) {
         super(node, down, right);
         this.level = level;
      }
   }

   static class Index<K, V> {
      final K key;
      final ConcurrentSkipListMap.Node<K, V> node;
      final ConcurrentSkipListMap.Index<K, V> down;
      volatile ConcurrentSkipListMap.Index<K, V> right;
      static final AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Index, ConcurrentSkipListMap.Index> rightUpdater = (AtomicReferenceFieldUpdater)AccessController.doPrivileged(new PrivilegedAction<AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Index, ConcurrentSkipListMap.Index>>() {
         public AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Index, ConcurrentSkipListMap.Index> run() {
            return AtomicReferenceFieldUpdater.newUpdater(ConcurrentSkipListMap.Index.class, ConcurrentSkipListMap.Index.class, "right");
         }
      });

      Index(ConcurrentSkipListMap.Node<K, V> node, ConcurrentSkipListMap.Index<K, V> down, ConcurrentSkipListMap.Index<K, V> right) {
         this.node = node;
         this.key = node.key;
         this.down = down;
         this.right = right;
      }

      final boolean casRight(ConcurrentSkipListMap.Index<K, V> cmp, ConcurrentSkipListMap.Index<K, V> val) {
         return rightUpdater.compareAndSet(this, cmp, val);
      }

      final boolean indexesDeletedNode() {
         return this.node.value == null;
      }

      final boolean link(ConcurrentSkipListMap.Index<K, V> succ, ConcurrentSkipListMap.Index<K, V> newSucc) {
         ConcurrentSkipListMap.Node<K, V> n = this.node;
         newSucc.right = succ;
         return n.value != null && this.casRight(succ, newSucc);
      }

      final boolean unlink(ConcurrentSkipListMap.Index<K, V> succ) {
         return !this.indexesDeletedNode() && this.casRight(succ, succ.right);
      }
   }

   static final class Node<K, V> {
      final K key;
      volatile Object value;
      volatile ConcurrentSkipListMap.Node<K, V> next;
      static final AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, ConcurrentSkipListMap.Node> nextUpdater = (AtomicReferenceFieldUpdater)AccessController.doPrivileged(new PrivilegedAction<AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, ConcurrentSkipListMap.Node>>() {
         public AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, ConcurrentSkipListMap.Node> run() {
            return AtomicReferenceFieldUpdater.newUpdater(ConcurrentSkipListMap.Node.class, ConcurrentSkipListMap.Node.class, "next");
         }
      });
      static final AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, Object> valueUpdater = (AtomicReferenceFieldUpdater)AccessController.doPrivileged(new PrivilegedAction<AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, Object>>() {
         public AtomicReferenceFieldUpdater<ConcurrentSkipListMap.Node, Object> run() {
            return AtomicReferenceFieldUpdater.newUpdater(ConcurrentSkipListMap.Node.class, Object.class, "value");
         }
      });

      Node(K key, Object value, ConcurrentSkipListMap.Node<K, V> next) {
         this.key = key;
         this.value = value;
         this.next = next;
      }

      Node(ConcurrentSkipListMap.Node<K, V> next) {
         this.key = null;
         this.value = this;
         this.next = next;
      }

      boolean casValue(Object cmp, Object val) {
         return valueUpdater.compareAndSet(this, cmp, val);
      }

      boolean casNext(ConcurrentSkipListMap.Node<K, V> cmp, ConcurrentSkipListMap.Node<K, V> val) {
         return nextUpdater.compareAndSet(this, cmp, val);
      }

      boolean isMarker() {
         return this.value == this;
      }

      boolean isBaseHeader() {
         return this.value == ConcurrentSkipListMap.BASE_HEADER;
      }

      boolean appendMarker(ConcurrentSkipListMap.Node<K, V> f) {
         return this.casNext(f, new ConcurrentSkipListMap.Node(f));
      }

      void helpDelete(ConcurrentSkipListMap.Node<K, V> b, ConcurrentSkipListMap.Node<K, V> f) {
         if (f == this.next && this == b.next) {
            if (f != null && f.value == f) {
               b.casNext(this, f.next);
            } else {
               this.appendMarker(f);
            }
         }

      }

      V getValidValue() {
         Object v = this.value;
         return v != this && v != ConcurrentSkipListMap.BASE_HEADER ? v : null;
      }

      ConcurrentSkipListMap.SnapshotEntry<K, V> createSnapshot() {
         V v = this.getValidValue();
         return v == null ? null : new ConcurrentSkipListMap.SnapshotEntry(this.key, v);
      }
   }
}
