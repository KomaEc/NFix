package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractCollection;
import edu.emory.mathcs.backport.java.util.AbstractMap;
import edu.emory.mathcs.backport.java.util.AbstractSet;
import edu.emory.mathcs.backport.java.util.Collections;
import edu.emory.mathcs.backport.java.util.NavigableMap;
import edu.emory.mathcs.backport.java.util.NavigableSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;

public class ConcurrentSkipListMap extends AbstractMap implements ConcurrentNavigableMap, Cloneable, Serializable {
   private static final long serialVersionUID = -8627078645895051609L;
   private static final Random seedGenerator = new Random();
   private static final Object BASE_HEADER = new Object();
   private transient volatile ConcurrentSkipListMap.HeadIndex head;
   private final Comparator comparator;
   private transient int randomSeed;
   private transient ConcurrentSkipListMap.KeySet keySet;
   private transient ConcurrentSkipListMap.EntrySet entrySet;
   private transient ConcurrentSkipListMap.Values values;
   private transient ConcurrentNavigableMap descendingMap;
   private static final int EQ = 1;
   private static final int LT = 2;
   private static final int GT = 0;

   final void initialize() {
      this.keySet = null;
      this.entrySet = null;
      this.values = null;
      this.descendingMap = null;
      this.randomSeed = seedGenerator.nextInt() | 256;
      this.head = new ConcurrentSkipListMap.HeadIndex(new ConcurrentSkipListMap.Node((Object)null, BASE_HEADER, (ConcurrentSkipListMap.Node)null), (ConcurrentSkipListMap.Index)null, (ConcurrentSkipListMap.Index)null, 1);
   }

   private synchronized boolean casHead(ConcurrentSkipListMap.HeadIndex cmp, ConcurrentSkipListMap.HeadIndex val) {
      if (this.head == cmp) {
         this.head = val;
         return true;
      } else {
         return false;
      }
   }

   private Comparable comparable(Object key) throws ClassCastException {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (Comparable)(this.comparator != null ? new ConcurrentSkipListMap.ComparableUsingComparator(key, this.comparator) : (Comparable)key);
      }
   }

   int compare(Object k1, Object k2) throws ClassCastException {
      Comparator cmp = this.comparator;
      return cmp != null ? cmp.compare(k1, k2) : ((Comparable)k1).compareTo(k2);
   }

   boolean inHalfOpenRange(Object key, Object least, Object fence) {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (least == null || this.compare(key, least) >= 0) && (fence == null || this.compare(key, fence) < 0);
      }
   }

   boolean inOpenRange(Object key, Object least, Object fence) {
      if (key == null) {
         throw new NullPointerException();
      } else {
         return (least == null || this.compare(key, least) >= 0) && (fence == null || this.compare(key, fence) <= 0);
      }
   }

   private ConcurrentSkipListMap.Node findPredecessor(Comparable key) {
      if (key == null) {
         throw new NullPointerException();
      } else {
         label33:
         while(true) {
            ConcurrentSkipListMap.Index q = this.head;
            ConcurrentSkipListMap.Index r = ((ConcurrentSkipListMap.Index)q).right;

            while(true) {
               while(true) {
                  if (r != null) {
                     ConcurrentSkipListMap.Node n = r.node;
                     Object k = n.key;
                     if (n.value == null) {
                        if (!((ConcurrentSkipListMap.Index)q).unlink(r)) {
                           continue label33;
                        }

                        r = ((ConcurrentSkipListMap.Index)q).right;
                        continue;
                     }

                     if (key.compareTo(k) > 0) {
                        q = r;
                        r = r.right;
                        continue;
                     }
                  }

                  ConcurrentSkipListMap.Index d = ((ConcurrentSkipListMap.Index)q).down;
                  if (d == null) {
                     return ((ConcurrentSkipListMap.Index)q).node;
                  }

                  q = d;
                  r = d.right;
               }
            }
         }
      }
   }

   private ConcurrentSkipListMap.Node findNode(Comparable key) {
      label37:
      while(true) {
         ConcurrentSkipListMap.Node b = this.findPredecessor(key);

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
            if (c == 0) {
               return n;
            }

            if (c < 0) {
               return null;
            }

            b = n;
         }

         return null;
      }
   }

   private Object doGet(Object okey) {
      Comparable key = this.comparable(okey);
      ConcurrentSkipListMap.Node bound = null;
      ConcurrentSkipListMap.Index q = this.head;
      ConcurrentSkipListMap.Index r = ((ConcurrentSkipListMap.Index)q).right;

      while(true) {
         while(true) {
            ConcurrentSkipListMap.Node n;
            Object k;
            int c;
            if (r != null && (n = r.node) != bound && (k = n.key) != null) {
               if ((c = key.compareTo(k)) > 0) {
                  q = r;
                  r = r.right;
                  continue;
               }

               if (c == 0) {
                  Object v = n.value;
                  return v != null ? v : this.getUsingFindNode(key);
               }

               bound = n;
            }

            ConcurrentSkipListMap.Index d;
            if ((d = ((ConcurrentSkipListMap.Index)q).down) == null) {
               for(n = ((ConcurrentSkipListMap.Index)q).node.next; n != null; n = n.next) {
                  if ((k = n.key) != null) {
                     if ((c = key.compareTo(k)) == 0) {
                        Object v = n.value;
                        return v != null ? v : this.getUsingFindNode(key);
                     }

                     if (c < 0) {
                        break;
                     }
                  }
               }

               return null;
            }

            q = d;
            r = d.right;
         }
      }
   }

   private Object getUsingFindNode(Comparable key) {
      Object v;
      do {
         ConcurrentSkipListMap.Node n = this.findNode(key);
         if (n == null) {
            return null;
         }

         v = n.value;
      } while(v == null);

      return v;
   }

   private Object doPut(Object kkey, Object value, boolean onlyIfAbsent) {
      Comparable key = this.comparable(kkey);

      ConcurrentSkipListMap.Node n;
      Object v;
      label51:
      do {
         while(true) {
            ConcurrentSkipListMap.Node b = this.findPredecessor(key);
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
      int x = this.randomSeed;
      x ^= x << 13;
      x ^= x >>> 17;
      this.randomSeed = x ^= x << 5;
      if ((x & '老') != 0) {
         return 0;
      } else {
         int level;
         for(level = 1; ((x >>>= 1) & 1) != 0; ++level) {
         }

         return level;
      }
   }

   private void insertIndex(ConcurrentSkipListMap.Node z, int level) {
      ConcurrentSkipListMap.HeadIndex h = this.head;
      int max = h.level;
      if (level <= max) {
         ConcurrentSkipListMap.Index idx = null;

         for(int i = 1; i <= level; ++i) {
            idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
         }

         this.addIndex(idx, h, level);
      } else {
         level = max + 1;
         ConcurrentSkipListMap.Index[] idxs = (ConcurrentSkipListMap.Index[])(new ConcurrentSkipListMap.Index[level + 1]);
         ConcurrentSkipListMap.Index idx = null;

         for(int i = 1; i <= level; ++i) {
            idxs[i] = idx = new ConcurrentSkipListMap.Index(z, idx, (ConcurrentSkipListMap.Index)null);
         }

         int var9;
         ConcurrentSkipListMap.HeadIndex oldh;
         while(true) {
            oldh = this.head;
            int oldLevel = oldh.level;
            if (level <= oldLevel) {
               var9 = level;
               break;
            }

            ConcurrentSkipListMap.HeadIndex newh = oldh;
            ConcurrentSkipListMap.Node oldbase = oldh.node;

            for(int j = oldLevel + 1; j <= level; ++j) {
               newh = new ConcurrentSkipListMap.HeadIndex(oldbase, newh, idxs[j], j);
            }

            if (this.casHead(oldh, newh)) {
               var9 = oldLevel;
               break;
            }
         }

         this.addIndex(idxs[var9], oldh, var9);
      }

   }

   private void addIndex(ConcurrentSkipListMap.Index idx, ConcurrentSkipListMap.HeadIndex h, int indexLevel) {
      int insertionLevel = indexLevel;
      Comparable key = this.comparable(idx.node.key);
      if (key == null) {
         throw new NullPointerException();
      } else {
         label53:
         while(true) {
            int j = h.level;
            ConcurrentSkipListMap.Index q = h;
            ConcurrentSkipListMap.Index r = h.right;
            ConcurrentSkipListMap.Index t = idx;

            while(true) {
               while(true) {
                  if (r != null) {
                     ConcurrentSkipListMap.Node n = r.node;
                     int c = key.compareTo(n.key);
                     if (n.value == null) {
                        if (!((ConcurrentSkipListMap.Index)q).unlink(r)) {
                           continue label53;
                        }

                        r = ((ConcurrentSkipListMap.Index)q).right;
                        continue;
                     }

                     if (c > 0) {
                        q = r;
                        r = r.right;
                        continue;
                     }
                  }

                  if (j == insertionLevel) {
                     if (t.indexesDeletedNode()) {
                        this.findNode(key);
                        return;
                     }

                     if (!((ConcurrentSkipListMap.Index)q).link(r, t)) {
                        continue label53;
                     }

                     --insertionLevel;
                     if (insertionLevel == 0) {
                        if (t.indexesDeletedNode()) {
                           this.findNode(key);
                        }

                        return;
                     }
                  }

                  --j;
                  if (j >= insertionLevel && j < indexLevel) {
                     t = t.down;
                  }

                  q = ((ConcurrentSkipListMap.Index)q).down;
                  r = ((ConcurrentSkipListMap.Index)q).right;
               }
            }
         }
      }
   }

   final Object doRemove(Object okey, Object value) {
      Comparable key = this.comparable(okey);

      while(true) {
         label57:
         while(true) {
            ConcurrentSkipListMap.Node b = this.findPredecessor(key);

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
      ConcurrentSkipListMap.HeadIndex h = this.head;
      ConcurrentSkipListMap.HeadIndex d;
      ConcurrentSkipListMap.HeadIndex e;
      if (h.level > 3 && (d = (ConcurrentSkipListMap.HeadIndex)h.down) != null && (e = (ConcurrentSkipListMap.HeadIndex)d.down) != null && e.right == null && d.right == null && h.right == null && this.casHead(h, d) && h.right != null) {
         this.casHead(d, h);
      }

   }

   ConcurrentSkipListMap.Node findFirst() {
      while(true) {
         ConcurrentSkipListMap.Node b = this.head.node;
         ConcurrentSkipListMap.Node n = b.next;
         if (n == null) {
            return null;
         }

         if (n.value != null) {
            return n;
         }

         n.helpDelete(b, n.next);
      }
   }

   Entry doRemoveFirstEntry() {
      while(true) {
         ConcurrentSkipListMap.Node b = this.head.node;
         ConcurrentSkipListMap.Node n = b.next;
         if (n == null) {
            return null;
         }

         ConcurrentSkipListMap.Node f = n.next;
         if (n == b.next) {
            Object v = n.value;
            if (v == null) {
               n.helpDelete(b, f);
            } else if (n.casValue(v, (Object)null)) {
               if (!n.appendMarker(f) || !b.casNext(n, f)) {
                  this.findFirst();
               }

               this.clearIndexToFirst();
               return new AbstractMap.SimpleImmutableEntry(n.key, v);
            }
         }
      }
   }

   private void clearIndexToFirst() {
      label24:
      while(true) {
         Object q = this.head;

         do {
            ConcurrentSkipListMap.Index r = ((ConcurrentSkipListMap.Index)q).right;
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

   ConcurrentSkipListMap.Node findLast() {
      Object q = this.head;

      while(true) {
         ConcurrentSkipListMap.Index r;
         while((r = ((ConcurrentSkipListMap.Index)q).right) == null) {
            ConcurrentSkipListMap.Index d;
            if ((d = ((ConcurrentSkipListMap.Index)q).down) != null) {
               q = d;
            } else {
               ConcurrentSkipListMap.Node b = ((ConcurrentSkipListMap.Index)q).node;
               ConcurrentSkipListMap.Node n = b.next;

               while(true) {
                  if (n == null) {
                     return b.isBaseHeader() ? null : b;
                  }

                  ConcurrentSkipListMap.Node f = n.next;
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

   private ConcurrentSkipListMap.Node findPredecessorOfLast() {
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

   Entry doRemoveLastEntry() {
      while(true) {
         ConcurrentSkipListMap.Node b = this.findPredecessorOfLast();
         ConcurrentSkipListMap.Node n = b.next;
         if (n == null) {
            if (b.isBaseHeader()) {
               return null;
            }
         } else {
            while(true) {
               ConcurrentSkipListMap.Node f = n.next;
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

                  Object key = n.key;
                  Comparable ck = this.comparable(key);
                  if (n.appendMarker(f) && b.casNext(n, f)) {
                     this.findPredecessor(ck);
                     if (this.head.right == null) {
                        this.tryReduceLevel();
                     }
                  } else {
                     this.findNode(ck);
                  }

                  return new AbstractMap.SimpleImmutableEntry(key, v);
               }

               b = n;
               n = f;
            }
         }
      }
   }

   ConcurrentSkipListMap.Node findNear(Object kkey, int rel) {
      Comparable key = this.comparable(kkey);

      label60:
      while(true) {
         ConcurrentSkipListMap.Node b = this.findPredecessor(key);

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

   AbstractMap.SimpleImmutableEntry getNear(Object key, int rel) {
      AbstractMap.SimpleImmutableEntry e;
      do {
         ConcurrentSkipListMap.Node n = this.findNear(key, rel);
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   public ConcurrentSkipListMap() {
      this.comparator = null;
      this.initialize();
   }

   public ConcurrentSkipListMap(Comparator comparator) {
      this.comparator = comparator;
      this.initialize();
   }

   public ConcurrentSkipListMap(Map m) {
      this.comparator = null;
      this.initialize();
      this.putAll(m);
   }

   public ConcurrentSkipListMap(SortedMap m) {
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

   private void buildFromSorted(SortedMap map) {
      if (map == null) {
         throw new NullPointerException();
      } else {
         ConcurrentSkipListMap.HeadIndex h = this.head;
         ConcurrentSkipListMap.Node basepred = h.node;
         ArrayList preds = new ArrayList();

         for(int i = 0; i <= h.level; ++i) {
            preds.add((Object)null);
         }

         ConcurrentSkipListMap.Index q = h;

         for(int i = h.level; i > 0; --i) {
            preds.set(i, q);
            q = ((ConcurrentSkipListMap.Index)q).down;
         }

         Iterator it = map.entrySet().iterator();

         while(it.hasNext()) {
            Entry e = (Entry)it.next();
            int j = this.randomLevel();
            if (j > h.level) {
               j = h.level + 1;
            }

            Object k = e.getKey();
            Object v = e.getValue();
            if (k == null || v == null) {
               throw new NullPointerException();
            }

            ConcurrentSkipListMap.Node z = new ConcurrentSkipListMap.Node(k, v, (ConcurrentSkipListMap.Node)null);
            basepred.next = z;
            basepred = z;
            if (j > 0) {
               ConcurrentSkipListMap.Index idx = null;

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
         Object v = n.getValidValue();
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
      ConcurrentSkipListMap.HeadIndex h = this.head;
      ConcurrentSkipListMap.Node basepred = h.node;
      ArrayList preds = new ArrayList();

      for(int i = 0; i <= h.level; ++i) {
         preds.add((Object)null);
      }

      ConcurrentSkipListMap.Index q = h;

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

         ConcurrentSkipListMap.Node z = new ConcurrentSkipListMap.Node(k, v, (ConcurrentSkipListMap.Node)null);
         basepred.next = z;
         basepred = z;
         if (j > 0) {
            ConcurrentSkipListMap.Index idx = null;

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

   public Object get(Object key) {
      return this.doGet(key);
   }

   public Object put(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return this.doPut(key, value, false);
      }
   }

   public Object remove(Object key) {
      return this.doRemove(key, (Object)null);
   }

   public boolean containsValue(Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         for(ConcurrentSkipListMap.Node n = this.findFirst(); n != null; n = n.next) {
            Object v = n.getValidValue();
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

   public Set keySet() {
      ConcurrentSkipListMap.KeySet ks = this.keySet;
      return ks != null ? ks : (this.keySet = new ConcurrentSkipListMap.KeySet(this));
   }

   public NavigableSet navigableKeySet() {
      ConcurrentSkipListMap.KeySet ks = this.keySet;
      return ks != null ? ks : (this.keySet = new ConcurrentSkipListMap.KeySet(this));
   }

   public Collection values() {
      ConcurrentSkipListMap.Values vs = this.values;
      return vs != null ? vs : (this.values = new ConcurrentSkipListMap.Values(this));
   }

   public Set entrySet() {
      ConcurrentSkipListMap.EntrySet es = this.entrySet;
      return es != null ? es : (this.entrySet = new ConcurrentSkipListMap.EntrySet(this));
   }

   public NavigableMap descendingMap() {
      ConcurrentNavigableMap dm = this.descendingMap;
      return dm != null ? dm : (this.descendingMap = new ConcurrentSkipListMap.SubMap(this, (Object)null, false, (Object)null, false, true));
   }

   public NavigableSet descendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map m = (Map)o;

         try {
            Iterator itr = this.entrySet().iterator();

            Entry e;
            while(itr.hasNext()) {
               e = (Entry)itr.next();
               if (!e.getValue().equals(m.get(e.getKey()))) {
                  return false;
               }
            }

            itr = m.entrySet().iterator();

            Object k;
            Object v;
            do {
               if (!itr.hasNext()) {
                  return true;
               }

               e = (Entry)itr.next();
               k = e.getKey();
               v = e.getValue();
            } while(k != null && v != null && v.equals(this.get(k)));

            return false;
         } catch (ClassCastException var7) {
            return false;
         } catch (NullPointerException var8) {
            return false;
         }
      }
   }

   public Object putIfAbsent(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return this.doPut(key, value, true);
      }
   }

   public boolean remove(Object key, Object value) {
      if (key == null) {
         throw new NullPointerException();
      } else if (value == null) {
         return false;
      } else {
         return this.doRemove(key, value) != null;
      }
   }

   public boolean replace(Object key, Object oldValue, Object newValue) {
      if (oldValue != null && newValue != null) {
         Comparable k = this.comparable(key);

         while(true) {
            ConcurrentSkipListMap.Node n = this.findNode(k);
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

   public Object replace(Object key, Object value) {
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

   public Comparator comparator() {
      return this.comparator;
   }

   public Object firstKey() {
      ConcurrentSkipListMap.Node n = this.findFirst();
      if (n == null) {
         throw new NoSuchElementException();
      } else {
         return n.key;
      }
   }

   public Object lastKey() {
      ConcurrentSkipListMap.Node n = this.findLast();
      if (n == null) {
         throw new NoSuchElementException();
      } else {
         return n.key;
      }
   }

   public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
      if (fromKey != null && toKey != null) {
         return new ConcurrentSkipListMap.SubMap(this, fromKey, fromInclusive, toKey, toInclusive, false);
      } else {
         throw new NullPointerException();
      }
   }

   public NavigableMap headMap(Object toKey, boolean inclusive) {
      if (toKey == null) {
         throw new NullPointerException();
      } else {
         return new ConcurrentSkipListMap.SubMap(this, (Object)null, false, toKey, inclusive, false);
      }
   }

   public NavigableMap tailMap(Object fromKey, boolean inclusive) {
      if (fromKey == null) {
         throw new NullPointerException();
      } else {
         return new ConcurrentSkipListMap.SubMap(this, fromKey, inclusive, (Object)null, false, false);
      }
   }

   public SortedMap subMap(Object fromKey, Object toKey) {
      return this.subMap(fromKey, true, toKey, false);
   }

   public SortedMap headMap(Object toKey) {
      return this.headMap(toKey, false);
   }

   public SortedMap tailMap(Object fromKey) {
      return this.tailMap(fromKey, true);
   }

   public Entry lowerEntry(Object key) {
      return this.getNear(key, 2);
   }

   public Object lowerKey(Object key) {
      ConcurrentSkipListMap.Node n = this.findNear(key, 2);
      return n == null ? null : n.key;
   }

   public Entry floorEntry(Object key) {
      return this.getNear(key, 3);
   }

   public Object floorKey(Object key) {
      ConcurrentSkipListMap.Node n = this.findNear(key, 3);
      return n == null ? null : n.key;
   }

   public Entry ceilingEntry(Object key) {
      return this.getNear(key, 1);
   }

   public Object ceilingKey(Object key) {
      ConcurrentSkipListMap.Node n = this.findNear(key, 1);
      return n == null ? null : n.key;
   }

   public Entry higherEntry(Object key) {
      return this.getNear(key, 0);
   }

   public Object higherKey(Object key) {
      ConcurrentSkipListMap.Node n = this.findNear(key, 0);
      return n == null ? null : n.key;
   }

   public Entry firstEntry() {
      AbstractMap.SimpleImmutableEntry e;
      do {
         ConcurrentSkipListMap.Node n = this.findFirst();
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   public Entry lastEntry() {
      AbstractMap.SimpleImmutableEntry e;
      do {
         ConcurrentSkipListMap.Node n = this.findLast();
         if (n == null) {
            return null;
         }

         e = n.createSnapshot();
      } while(e == null);

      return e;
   }

   public Entry pollFirstEntry() {
      return this.doRemoveFirstEntry();
   }

   public Entry pollLastEntry() {
      return this.doRemoveLastEntry();
   }

   Iterator keyIterator() {
      return new ConcurrentSkipListMap.KeyIterator();
   }

   Iterator valueIterator() {
      return new ConcurrentSkipListMap.ValueIterator();
   }

   Iterator entryIterator() {
      return new ConcurrentSkipListMap.EntryIterator();
   }

   static final List toList(Collection c) {
      List list = new ArrayList();
      Iterator itr = c.iterator();

      while(itr.hasNext()) {
         list.add(itr.next());
      }

      return list;
   }

   static final class SubMap extends AbstractMap implements ConcurrentNavigableMap, Cloneable, Serializable {
      private static final long serialVersionUID = -7647078645895051609L;
      private final ConcurrentSkipListMap m;
      private final Object lo;
      private final Object hi;
      private final boolean loInclusive;
      private final boolean hiInclusive;
      private final boolean isDescending;
      private transient ConcurrentSkipListMap.KeySet keySetView;
      private transient Set entrySetView;
      private transient Collection valuesView;

      SubMap(ConcurrentSkipListMap map, Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive, boolean isDescending) {
         if (fromKey != null && toKey != null && map.compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException("inconsistent range");
         } else {
            this.m = map;
            this.lo = fromKey;
            this.hi = toKey;
            this.loInclusive = fromInclusive;
            this.hiInclusive = toInclusive;
            this.isDescending = isDescending;
         }
      }

      private boolean tooLow(Object key) {
         if (this.lo != null) {
            int c = this.m.compare(key, this.lo);
            if (c < 0 || c == 0 && !this.loInclusive) {
               return true;
            }
         }

         return false;
      }

      private boolean tooHigh(Object key) {
         if (this.hi != null) {
            int c = this.m.compare(key, this.hi);
            if (c > 0 || c == 0 && !this.hiInclusive) {
               return true;
            }
         }

         return false;
      }

      private boolean inBounds(Object key) {
         return !this.tooLow(key) && !this.tooHigh(key);
      }

      private void checkKeyBounds(Object key) throws IllegalArgumentException {
         if (key == null) {
            throw new NullPointerException();
         } else if (!this.inBounds(key)) {
            throw new IllegalArgumentException("key out of range");
         }
      }

      private boolean isBeforeEnd(ConcurrentSkipListMap.Node n) {
         if (n == null) {
            return false;
         } else if (this.hi == null) {
            return true;
         } else {
            Object k = n.key;
            if (k == null) {
               return true;
            } else {
               int c = this.m.compare(k, this.hi);
               return c <= 0 && (c != 0 || this.hiInclusive);
            }
         }
      }

      private ConcurrentSkipListMap.Node loNode() {
         if (this.lo == null) {
            return this.m.findFirst();
         } else {
            return this.loInclusive ? this.m.findNear(this.lo, 0 | 1) : this.m.findNear(this.lo, 0);
         }
      }

      private ConcurrentSkipListMap.Node hiNode() {
         if (this.hi == null) {
            return this.m.findLast();
         } else {
            return this.hiInclusive ? this.m.findNear(this.hi, 2 | 1) : this.m.findNear(this.hi, 2);
         }
      }

      private Object lowestKey() {
         ConcurrentSkipListMap.Node n = this.loNode();
         if (this.isBeforeEnd(n)) {
            return n.key;
         } else {
            throw new NoSuchElementException();
         }
      }

      private Object highestKey() {
         ConcurrentSkipListMap.Node n = this.hiNode();
         if (n != null) {
            Object last = n.key;
            if (this.inBounds(last)) {
               return last;
            }
         }

         throw new NoSuchElementException();
      }

      private Entry lowestEntry() {
         AbstractMap.SimpleImmutableEntry e;
         do {
            ConcurrentSkipListMap.Node n = this.loNode();
            if (!this.isBeforeEnd(n)) {
               return null;
            }

            e = n.createSnapshot();
         } while(e == null);

         return e;
      }

      private Entry highestEntry() {
         while(true) {
            ConcurrentSkipListMap.Node n = this.hiNode();
            if (n != null && this.inBounds(n.key)) {
               Entry e = n.createSnapshot();
               if (e == null) {
                  continue;
               }

               return e;
            }

            return null;
         }
      }

      private Entry removeLowest() {
         Object k;
         Object v;
         do {
            ConcurrentSkipListMap.Node n = this.loNode();
            if (n == null) {
               return null;
            }

            k = n.key;
            if (!this.inBounds(k)) {
               return null;
            }

            v = this.m.doRemove(k, (Object)null);
         } while(v == null);

         return new AbstractMap.SimpleImmutableEntry(k, v);
      }

      private Entry removeHighest() {
         Object k;
         Object v;
         do {
            ConcurrentSkipListMap.Node n = this.hiNode();
            if (n == null) {
               return null;
            }

            k = n.key;
            if (!this.inBounds(k)) {
               return null;
            }

            v = this.m.doRemove(k, (Object)null);
         } while(v == null);

         return new AbstractMap.SimpleImmutableEntry(k, v);
      }

      private Entry getNearEntry(Object key, int rel) {
         if (this.isDescending) {
            if ((rel & 2) == 0) {
               rel |= 2;
            } else {
               rel &= ~2;
            }
         }

         if (this.tooLow(key)) {
            return (rel & 2) != 0 ? null : this.lowestEntry();
         } else if (this.tooHigh(key)) {
            return (rel & 2) != 0 ? this.highestEntry() : null;
         } else {
            Object k;
            Object v;
            do {
               ConcurrentSkipListMap.Node n = this.m.findNear(key, rel);
               if (n == null || !this.inBounds(n.key)) {
                  return null;
               }

               k = n.key;
               v = n.getValidValue();
            } while(v == null);

            return new AbstractMap.SimpleImmutableEntry(k, v);
         }
      }

      private Object getNearKey(Object key, int rel) {
         if (this.isDescending) {
            if ((rel & 2) == 0) {
               rel |= 2;
            } else {
               rel &= ~2;
            }
         }

         ConcurrentSkipListMap.Node n;
         if (this.tooLow(key)) {
            if ((rel & 2) == 0) {
               n = this.loNode();
               if (this.isBeforeEnd(n)) {
                  return n.key;
               }
            }

            return null;
         } else {
            Object last;
            if (this.tooHigh(key)) {
               if ((rel & 2) != 0) {
                  n = this.hiNode();
                  if (n != null) {
                     last = n.key;
                     if (this.inBounds(last)) {
                        return last;
                     }
                  }
               }

               return null;
            } else {
               Object v;
               do {
                  n = this.m.findNear(key, rel);
                  if (n == null || !this.inBounds(n.key)) {
                     return null;
                  }

                  last = n.key;
                  v = n.getValidValue();
               } while(v == null);

               return last;
            }
         }
      }

      public boolean containsKey(Object key) {
         if (key == null) {
            throw new NullPointerException();
         } else {
            return this.inBounds(key) && this.m.containsKey(key);
         }
      }

      public Object get(Object key) {
         if (key == null) {
            throw new NullPointerException();
         } else {
            return !this.inBounds(key) ? null : this.m.get(key);
         }
      }

      public Object put(Object key, Object value) {
         this.checkKeyBounds(key);
         return this.m.put(key, value);
      }

      public Object remove(Object key) {
         return !this.inBounds(key) ? null : this.m.remove(key);
      }

      public int size() {
         long count = 0L;

         for(ConcurrentSkipListMap.Node n = this.loNode(); this.isBeforeEnd(n); n = n.next) {
            if (n.getValidValue() != null) {
               ++count;
            }
         }

         return count >= 2147483647L ? Integer.MAX_VALUE : (int)count;
      }

      public boolean isEmpty() {
         return !this.isBeforeEnd(this.loNode());
      }

      public boolean containsValue(Object value) {
         if (value == null) {
            throw new NullPointerException();
         } else {
            for(ConcurrentSkipListMap.Node n = this.loNode(); this.isBeforeEnd(n); n = n.next) {
               Object v = n.getValidValue();
               if (v != null && value.equals(v)) {
                  return true;
               }
            }

            return false;
         }
      }

      public void clear() {
         for(ConcurrentSkipListMap.Node n = this.loNode(); this.isBeforeEnd(n); n = n.next) {
            if (n.getValidValue() != null) {
               this.m.remove(n.key);
            }
         }

      }

      public Object putIfAbsent(Object key, Object value) {
         this.checkKeyBounds(key);
         return this.m.putIfAbsent(key, value);
      }

      public boolean remove(Object key, Object value) {
         return this.inBounds(key) && this.m.remove(key, value);
      }

      public boolean replace(Object key, Object oldValue, Object newValue) {
         this.checkKeyBounds(key);
         return this.m.replace(key, oldValue, newValue);
      }

      public Object replace(Object key, Object value) {
         this.checkKeyBounds(key);
         return this.m.replace(key, value);
      }

      public Comparator comparator() {
         Comparator cmp = this.m.comparator();
         return this.isDescending ? Collections.reverseOrder(cmp) : cmp;
      }

      private ConcurrentSkipListMap.SubMap newSubMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
         if (this.isDescending) {
            Object tk = fromKey;
            fromKey = toKey;
            toKey = tk;
            boolean ti = fromInclusive;
            fromInclusive = toInclusive;
            toInclusive = ti;
         }

         int c;
         if (this.lo != null) {
            if (fromKey == null) {
               fromKey = this.lo;
               fromInclusive = this.loInclusive;
            } else {
               c = this.m.compare(fromKey, this.lo);
               if (c < 0 || c == 0 && !this.loInclusive && fromInclusive) {
                  throw new IllegalArgumentException("key out of range");
               }
            }
         }

         if (this.hi != null) {
            if (toKey == null) {
               toKey = this.hi;
               toInclusive = this.hiInclusive;
            } else {
               c = this.m.compare(toKey, this.hi);
               if (c > 0 || c == 0 && !this.hiInclusive && toInclusive) {
                  throw new IllegalArgumentException("key out of range");
               }
            }
         }

         return new ConcurrentSkipListMap.SubMap(this.m, fromKey, fromInclusive, toKey, toInclusive, this.isDescending);
      }

      public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
         if (fromKey != null && toKey != null) {
            return this.newSubMap(fromKey, fromInclusive, toKey, toInclusive);
         } else {
            throw new NullPointerException();
         }
      }

      public NavigableMap headMap(Object toKey, boolean inclusive) {
         if (toKey == null) {
            throw new NullPointerException();
         } else {
            return this.newSubMap((Object)null, false, toKey, inclusive);
         }
      }

      public NavigableMap tailMap(Object fromKey, boolean inclusive) {
         if (fromKey == null) {
            throw new NullPointerException();
         } else {
            return this.newSubMap(fromKey, inclusive, (Object)null, false);
         }
      }

      public SortedMap subMap(Object fromKey, Object toKey) {
         return this.subMap(fromKey, true, toKey, false);
      }

      public SortedMap headMap(Object toKey) {
         return this.headMap(toKey, false);
      }

      public SortedMap tailMap(Object fromKey) {
         return this.tailMap(fromKey, true);
      }

      public NavigableMap descendingMap() {
         return new ConcurrentSkipListMap.SubMap(this.m, this.lo, this.loInclusive, this.hi, this.hiInclusive, !this.isDescending);
      }

      public Entry ceilingEntry(Object key) {
         return this.getNearEntry(key, 0 | 1);
      }

      public Object ceilingKey(Object key) {
         return this.getNearKey(key, 0 | 1);
      }

      public Entry lowerEntry(Object key) {
         return this.getNearEntry(key, 2);
      }

      public Object lowerKey(Object key) {
         return this.getNearKey(key, 2);
      }

      public Entry floorEntry(Object key) {
         return this.getNearEntry(key, 2 | 1);
      }

      public Object floorKey(Object key) {
         return this.getNearKey(key, 2 | 1);
      }

      public Entry higherEntry(Object key) {
         return this.getNearEntry(key, 0);
      }

      public Object higherKey(Object key) {
         return this.getNearKey(key, 0);
      }

      public Object firstKey() {
         return this.isDescending ? this.highestKey() : this.lowestKey();
      }

      public Object lastKey() {
         return this.isDescending ? this.lowestKey() : this.highestKey();
      }

      public Entry firstEntry() {
         return this.isDescending ? this.highestEntry() : this.lowestEntry();
      }

      public Entry lastEntry() {
         return this.isDescending ? this.lowestEntry() : this.highestEntry();
      }

      public Entry pollFirstEntry() {
         return this.isDescending ? this.removeHighest() : this.removeLowest();
      }

      public Entry pollLastEntry() {
         return this.isDescending ? this.removeLowest() : this.removeHighest();
      }

      public Set keySet() {
         ConcurrentSkipListMap.KeySet ks = this.keySetView;
         return ks != null ? ks : (this.keySetView = new ConcurrentSkipListMap.KeySet(this));
      }

      public NavigableSet navigableKeySet() {
         ConcurrentSkipListMap.KeySet ks = this.keySetView;
         return ks != null ? ks : (this.keySetView = new ConcurrentSkipListMap.KeySet(this));
      }

      public Collection values() {
         Collection vs = this.valuesView;
         return vs != null ? vs : (this.valuesView = new ConcurrentSkipListMap.Values(this));
      }

      public Set entrySet() {
         Set es = this.entrySetView;
         return es != null ? es : (this.entrySetView = new ConcurrentSkipListMap.EntrySet(this));
      }

      public NavigableSet descendingKeySet() {
         return this.descendingMap().navigableKeySet();
      }

      Iterator keyIterator() {
         return new ConcurrentSkipListMap.SubMap.SubMapKeyIterator();
      }

      Iterator valueIterator() {
         return new ConcurrentSkipListMap.SubMap.SubMapValueIterator();
      }

      Iterator entryIterator() {
         return new ConcurrentSkipListMap.SubMap.SubMapEntryIterator();
      }

      final class SubMapEntryIterator extends ConcurrentSkipListMap.SubMap.SubMapIter {
         SubMapEntryIterator() {
            super();
         }

         public Object next() {
            ConcurrentSkipListMap.Node n = this.next;
            Object v = this.nextValue;
            this.advance();
            return new AbstractMap.SimpleImmutableEntry(n.key, v);
         }
      }

      final class SubMapKeyIterator extends ConcurrentSkipListMap.SubMap.SubMapIter {
         SubMapKeyIterator() {
            super();
         }

         public Object next() {
            ConcurrentSkipListMap.Node n = this.next;
            this.advance();
            return n.key;
         }
      }

      final class SubMapValueIterator extends ConcurrentSkipListMap.SubMap.SubMapIter {
         SubMapValueIterator() {
            super();
         }

         public Object next() {
            Object v = this.nextValue;
            this.advance();
            return v;
         }
      }

      abstract class SubMapIter implements Iterator {
         ConcurrentSkipListMap.Node lastReturned;
         ConcurrentSkipListMap.Node next;
         Object nextValue;

         SubMapIter() {
            while(true) {
               this.next = SubMap.this.isDescending ? SubMap.this.hiNode() : SubMap.this.loNode();
               if (this.next == null) {
                  break;
               }

               Object x = this.next.value;
               if (x != null && x != this.next) {
                  if (!SubMap.this.inBounds(this.next.key)) {
                     this.next = null;
                  } else {
                     this.nextValue = x;
                  }
                  break;
               }
            }

         }

         public final boolean hasNext() {
            return this.next != null;
         }

         final void advance() {
            if (this.next == null) {
               throw new NoSuchElementException();
            } else {
               this.lastReturned = this.next;
               if (SubMap.this.isDescending) {
                  this.descend();
               } else {
                  this.ascend();
               }

            }
         }

         private void ascend() {
            while(true) {
               this.next = this.next.next;
               if (this.next != null) {
                  Object x = this.next.value;
                  if (x == null || x == this.next) {
                     continue;
                  }

                  if (SubMap.this.tooHigh(this.next.key)) {
                     this.next = null;
                  } else {
                     this.nextValue = x;
                  }
               }

               return;
            }
         }

         private void descend() {
            while(true) {
               this.next = SubMap.this.m.findNear(this.lastReturned.key, 2);
               if (this.next != null) {
                  Object x = this.next.value;
                  if (x == null || x == this.next) {
                     continue;
                  }

                  if (SubMap.this.tooLow(this.next.key)) {
                     this.next = null;
                  } else {
                     this.nextValue = x;
                  }
               }

               return;
            }
         }

         public void remove() {
            ConcurrentSkipListMap.Node l = this.lastReturned;
            if (l == null) {
               throw new IllegalStateException();
            } else {
               SubMap.this.m.remove(l.key);
               this.lastReturned = null;
            }
         }
      }
   }

   static final class EntrySet extends AbstractSet {
      private final ConcurrentNavigableMap m;

      EntrySet(ConcurrentNavigableMap map) {
         this.m = map;
      }

      public Iterator iterator() {
         return this.m instanceof ConcurrentSkipListMap ? ((ConcurrentSkipListMap)this.m).entryIterator() : ((ConcurrentSkipListMap.SubMap)this.m).entryIterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            Object v = this.m.get(e.getKey());
            return v != null && v.equals(e.getValue());
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return this.m.remove(e.getKey(), e.getValue());
         }
      }

      public boolean isEmpty() {
         return this.m.isEmpty();
      }

      public int size() {
         return this.m.size();
      }

      public void clear() {
         this.m.clear();
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Set)) {
            return false;
         } else {
            Collection c = (Collection)o;

            try {
               return this.containsAll(c) && c.containsAll(this);
            } catch (ClassCastException var4) {
               return false;
            } catch (NullPointerException var5) {
               return false;
            }
         }
      }

      public Object[] toArray() {
         return ConcurrentSkipListMap.toList(this).toArray();
      }

      public Object[] toArray(Object[] a) {
         return ConcurrentSkipListMap.toList(this).toArray(a);
      }
   }

   static final class Values extends AbstractCollection {
      private final ConcurrentNavigableMap m;

      Values(ConcurrentNavigableMap map) {
         this.m = map;
      }

      public Iterator iterator() {
         return this.m instanceof ConcurrentSkipListMap ? ((ConcurrentSkipListMap)this.m).valueIterator() : ((ConcurrentSkipListMap.SubMap)this.m).valueIterator();
      }

      public boolean isEmpty() {
         return this.m.isEmpty();
      }

      public int size() {
         return this.m.size();
      }

      public boolean contains(Object o) {
         return this.m.containsValue(o);
      }

      public void clear() {
         this.m.clear();
      }

      public Object[] toArray() {
         return ConcurrentSkipListMap.toList(this).toArray();
      }

      public Object[] toArray(Object[] a) {
         return ConcurrentSkipListMap.toList(this).toArray(a);
      }
   }

   static final class KeySet extends AbstractSet implements NavigableSet {
      private final ConcurrentNavigableMap m;

      KeySet(ConcurrentNavigableMap map) {
         this.m = map;
      }

      public int size() {
         return this.m.size();
      }

      public boolean isEmpty() {
         return this.m.isEmpty();
      }

      public boolean contains(Object o) {
         return this.m.containsKey(o);
      }

      public boolean remove(Object o) {
         return this.m.remove(o) != null;
      }

      public void clear() {
         this.m.clear();
      }

      public Object lower(Object e) {
         return this.m.lowerKey(e);
      }

      public Object floor(Object e) {
         return this.m.floorKey(e);
      }

      public Object ceiling(Object e) {
         return this.m.ceilingKey(e);
      }

      public Object higher(Object e) {
         return this.m.higherKey(e);
      }

      public Comparator comparator() {
         return this.m.comparator();
      }

      public Object first() {
         return this.m.firstKey();
      }

      public Object last() {
         return this.m.lastKey();
      }

      public Object pollFirst() {
         Entry e = this.m.pollFirstEntry();
         return e == null ? null : e.getKey();
      }

      public Object pollLast() {
         Entry e = this.m.pollLastEntry();
         return e == null ? null : e.getKey();
      }

      public Object[] toArray() {
         return ConcurrentSkipListMap.toList(this).toArray();
      }

      public Object[] toArray(Object[] a) {
         return ConcurrentSkipListMap.toList(this).toArray(a);
      }

      public Iterator iterator() {
         return this.m instanceof ConcurrentSkipListMap ? ((ConcurrentSkipListMap)this.m).keyIterator() : ((ConcurrentSkipListMap.SubMap)this.m).keyIterator();
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Set)) {
            return false;
         } else {
            Collection c = (Collection)o;

            try {
               return this.containsAll(c) && c.containsAll(this);
            } catch (ClassCastException var4) {
               return false;
            } catch (NullPointerException var5) {
               return false;
            }
         }
      }

      public Iterator descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet subSet(Object fromElement, boolean fromInclusive, Object toElement, boolean toInclusive) {
         return new ConcurrentSkipListSet((ConcurrentNavigableMap)this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
      }

      public NavigableSet headSet(Object toElement, boolean inclusive) {
         return new ConcurrentSkipListSet((ConcurrentNavigableMap)this.m.headMap(toElement, inclusive));
      }

      public NavigableSet tailSet(Object fromElement, boolean inclusive) {
         return new ConcurrentSkipListSet((ConcurrentNavigableMap)this.m.tailMap(fromElement, inclusive));
      }

      public SortedSet subSet(Object fromElement, Object toElement) {
         return this.subSet(fromElement, true, toElement, false);
      }

      public SortedSet headSet(Object toElement) {
         return this.headSet(toElement, false);
      }

      public SortedSet tailSet(Object fromElement) {
         return this.tailSet(fromElement, true);
      }

      public NavigableSet descendingSet() {
         return new ConcurrentSkipListSet((ConcurrentNavigableMap)this.m.descendingMap());
      }
   }

   final class EntryIterator extends ConcurrentSkipListMap.Iter {
      EntryIterator() {
         super();
      }

      public Object next() {
         ConcurrentSkipListMap.Node n = this.next;
         Object v = this.nextValue;
         this.advance();
         return new AbstractMap.SimpleImmutableEntry(n.key, v);
      }
   }

   final class KeyIterator extends ConcurrentSkipListMap.Iter {
      KeyIterator() {
         super();
      }

      public Object next() {
         ConcurrentSkipListMap.Node n = this.next;
         this.advance();
         return n.key;
      }
   }

   final class ValueIterator extends ConcurrentSkipListMap.Iter {
      ValueIterator() {
         super();
      }

      public Object next() {
         Object v = this.nextValue;
         this.advance();
         return v;
      }
   }

   abstract class Iter implements Iterator {
      ConcurrentSkipListMap.Node lastReturned;
      ConcurrentSkipListMap.Node next;
      Object nextValue;

      Iter() {
         while(true) {
            this.next = ConcurrentSkipListMap.this.findFirst();
            if (this.next == null) {
               break;
            }

            Object x = this.next.value;
            if (x != null && x != this.next) {
               this.nextValue = x;
               break;
            }
         }

      }

      public final boolean hasNext() {
         return this.next != null;
      }

      final void advance() {
         if (this.next == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.next;

            while(true) {
               this.next = this.next.next;
               if (this.next == null) {
                  break;
               }

               Object x = this.next.value;
               if (x != null && x != this.next) {
                  this.nextValue = x;
                  break;
               }
            }

         }
      }

      public void remove() {
         ConcurrentSkipListMap.Node l = this.lastReturned;
         if (l == null) {
            throw new IllegalStateException();
         } else {
            ConcurrentSkipListMap.this.remove(l.key);
            this.lastReturned = null;
         }
      }
   }

   static final class ComparableUsingComparator implements Comparable {
      final Object actualKey;
      final Comparator cmp;

      ComparableUsingComparator(Object key, Comparator cmp) {
         this.actualKey = key;
         this.cmp = cmp;
      }

      public int compareTo(Object k2) {
         return this.cmp.compare(this.actualKey, k2);
      }
   }

   static final class HeadIndex extends ConcurrentSkipListMap.Index {
      final int level;

      HeadIndex(ConcurrentSkipListMap.Node node, ConcurrentSkipListMap.Index down, ConcurrentSkipListMap.Index right, int level) {
         super(node, down, right);
         this.level = level;
      }
   }

   static class Index {
      final ConcurrentSkipListMap.Node node;
      final ConcurrentSkipListMap.Index down;
      volatile ConcurrentSkipListMap.Index right;

      Index(ConcurrentSkipListMap.Node node, ConcurrentSkipListMap.Index down, ConcurrentSkipListMap.Index right) {
         this.node = node;
         this.down = down;
         this.right = right;
      }

      final synchronized boolean casRight(ConcurrentSkipListMap.Index cmp, ConcurrentSkipListMap.Index val) {
         if (this.right == cmp) {
            this.right = val;
            return true;
         } else {
            return false;
         }
      }

      final boolean indexesDeletedNode() {
         return this.node.value == null;
      }

      final boolean link(ConcurrentSkipListMap.Index succ, ConcurrentSkipListMap.Index newSucc) {
         ConcurrentSkipListMap.Node n = this.node;
         newSucc.right = succ;
         return n.value != null && this.casRight(succ, newSucc);
      }

      final boolean unlink(ConcurrentSkipListMap.Index succ) {
         return !this.indexesDeletedNode() && this.casRight(succ, succ.right);
      }
   }

   static final class Node {
      final Object key;
      volatile Object value;
      volatile ConcurrentSkipListMap.Node next;

      Node(Object key, Object value, ConcurrentSkipListMap.Node next) {
         this.key = key;
         this.value = value;
         this.next = next;
      }

      Node(ConcurrentSkipListMap.Node next) {
         this.key = null;
         this.value = this;
         this.next = next;
      }

      synchronized boolean casValue(Object cmp, Object val) {
         if (this.value == cmp) {
            this.value = val;
            return true;
         } else {
            return false;
         }
      }

      synchronized boolean casNext(ConcurrentSkipListMap.Node cmp, ConcurrentSkipListMap.Node val) {
         if (this.next == cmp) {
            this.next = val;
            return true;
         } else {
            return false;
         }
      }

      boolean isMarker() {
         return this.value == this;
      }

      boolean isBaseHeader() {
         return this.value == ConcurrentSkipListMap.BASE_HEADER;
      }

      boolean appendMarker(ConcurrentSkipListMap.Node f) {
         return this.casNext(f, new ConcurrentSkipListMap.Node(f));
      }

      void helpDelete(ConcurrentSkipListMap.Node b, ConcurrentSkipListMap.Node f) {
         if (f == this.next && this == b.next) {
            if (f != null && f.value == f) {
               b.casNext(this, f.next);
            } else {
               this.appendMarker(f);
            }
         }

      }

      Object getValidValue() {
         Object v = this.value;
         return v != this && v != ConcurrentSkipListMap.BASE_HEADER ? v : null;
      }

      AbstractMap.SimpleImmutableEntry createSnapshot() {
         Object v = this.getValidValue();
         return v == null ? null : new AbstractMap.SimpleImmutableEntry(this.key, v);
      }
   }
}
