package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public class TreeMap extends AbstractMap implements NavigableMap, Serializable {
   private static final long serialVersionUID = 919286545866124006L;
   private final Comparator comparator;
   private transient TreeMap.Entry root;
   private transient int size = 0;
   private transient int modCount = 0;
   private transient TreeMap.EntrySet entrySet;
   private transient TreeMap.KeySet navigableKeySet;
   private transient NavigableMap descendingMap;
   private transient Comparator reverseComparator;

   public TreeMap() {
      this.comparator = null;
   }

   public TreeMap(Comparator comparator) {
      this.comparator = comparator;
   }

   public TreeMap(SortedMap map) {
      this.comparator = map.comparator();
      this.buildFromSorted(map.entrySet().iterator(), map.size());
   }

   public TreeMap(Map map) {
      this.comparator = null;
      this.putAll(map);
   }

   public int size() {
      return this.size;
   }

   public void clear() {
      this.root = null;
      this.size = 0;
      ++this.modCount;
   }

   public Object clone() {
      TreeMap clone;
      try {
         clone = (TreeMap)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      clone.root = null;
      clone.size = 0;
      clone.modCount = 0;
      if (!this.isEmpty()) {
         clone.buildFromSorted(this.entrySet().iterator(), this.size);
      }

      return clone;
   }

   public Object put(Object key, Object value) {
      if (this.root == null) {
         this.root = new TreeMap.Entry(key, value);
         ++this.size;
         ++this.modCount;
         return null;
      } else {
         TreeMap.Entry t = this.root;

         while(true) {
            int diff = compare(key, t.getKey(), this.comparator);
            if (diff == 0) {
               return t.setValue(value);
            }

            TreeMap.Entry e;
            if (diff <= 0) {
               if (t.left == null) {
                  ++this.size;
                  ++this.modCount;
                  e = new TreeMap.Entry(key, value);
                  e.parent = t;
                  t.left = e;
                  this.fixAfterInsertion(e);
                  return null;
               }

               t = t.left;
            } else {
               if (t.right == null) {
                  ++this.size;
                  ++this.modCount;
                  e = new TreeMap.Entry(key, value);
                  e.parent = t;
                  t.right = e;
                  this.fixAfterInsertion(e);
                  return null;
               }

               t = t.right;
            }
         }
      }
   }

   public Object get(Object key) {
      TreeMap.Entry entry = this.getEntry(key);
      return entry == null ? null : entry.getValue();
   }

   public boolean containsKey(Object key) {
      return this.getEntry(key) != null;
   }

   public Set entrySet() {
      if (this.entrySet == null) {
         this.entrySet = new TreeMap.EntrySet();
      }

      return this.entrySet;
   }

   private static TreeMap.Entry successor(TreeMap.Entry e) {
      if (e.right != null) {
         for(e = e.right; e.left != null; e = e.left) {
         }

         return e;
      } else {
         TreeMap.Entry p;
         for(p = e.parent; p != null && e == p.right; p = p.parent) {
            e = p;
         }

         return p;
      }
   }

   private static TreeMap.Entry predecessor(TreeMap.Entry e) {
      if (e.left != null) {
         for(e = e.left; e.right != null; e = e.right) {
         }

         return e;
      } else {
         TreeMap.Entry p;
         for(p = e.parent; p != null && e == p.left; p = p.parent) {
            e = p;
         }

         return p;
      }
   }

   private TreeMap.Entry getEntry(Object key) {
      TreeMap.Entry t = this.root;
      if (this.comparator != null) {
         while(t != null) {
            int diff = this.comparator.compare(key, t.key);
            if (diff == 0) {
               return t;
            }

            t = diff < 0 ? t.left : t.right;
         }

         return null;
      } else {
         int diff;
         for(Comparable c = (Comparable)key; t != null; t = diff < 0 ? t.left : t.right) {
            diff = c.compareTo(t.key);
            if (diff == 0) {
               return t;
            }
         }

         return null;
      }
   }

   private TreeMap.Entry getHigherEntry(Object key) {
      TreeMap.Entry t = this.root;
      if (t == null) {
         return null;
      } else {
         while(true) {
            while(true) {
               int diff = compare(key, t.key, this.comparator);
               if (diff < 0) {
                  if (t.left == null) {
                     return t;
                  }

                  t = t.left;
               } else {
                  if (t.right == null) {
                     TreeMap.Entry parent;
                     for(parent = t.parent; parent != null && t == parent.right; parent = parent.parent) {
                        t = parent;
                     }

                     return parent;
                  }

                  t = t.right;
               }
            }
         }
      }
   }

   private TreeMap.Entry getFirstEntry() {
      TreeMap.Entry e = this.root;
      if (e == null) {
         return null;
      } else {
         while(e.left != null) {
            e = e.left;
         }

         return e;
      }
   }

   private TreeMap.Entry getLastEntry() {
      TreeMap.Entry e = this.root;
      if (e == null) {
         return null;
      } else {
         while(e.right != null) {
            e = e.right;
         }

         return e;
      }
   }

   private TreeMap.Entry getCeilingEntry(Object key) {
      TreeMap.Entry e = this.root;
      if (e == null) {
         return null;
      } else {
         while(true) {
            while(true) {
               int diff = compare(key, e.key, this.comparator);
               if (diff < 0) {
                  if (e.left == null) {
                     return e;
                  }

                  e = e.left;
               } else {
                  if (diff <= 0) {
                     return e;
                  }

                  if (e.right == null) {
                     TreeMap.Entry p;
                     for(p = e.parent; p != null && e == p.right; p = p.parent) {
                        e = p;
                     }

                     return p;
                  }

                  e = e.right;
               }
            }
         }
      }
   }

   private TreeMap.Entry getLowerEntry(Object key) {
      TreeMap.Entry e = this.root;
      if (e == null) {
         return null;
      } else {
         while(true) {
            while(true) {
               int diff = compare(key, e.key, this.comparator);
               if (diff > 0) {
                  if (e.right == null) {
                     return e;
                  }

                  e = e.right;
               } else {
                  if (e.left == null) {
                     TreeMap.Entry p;
                     for(p = e.parent; p != null && e == p.left; p = p.parent) {
                        e = p;
                     }

                     return p;
                  }

                  e = e.left;
               }
            }
         }
      }
   }

   private TreeMap.Entry getFloorEntry(Object key) {
      TreeMap.Entry e = this.root;
      if (e == null) {
         return null;
      } else {
         while(true) {
            while(true) {
               int diff = compare(key, e.key, this.comparator);
               if (diff > 0) {
                  if (e.right == null) {
                     return e;
                  }

                  e = e.right;
               } else {
                  if (diff >= 0) {
                     return e;
                  }

                  if (e.left == null) {
                     TreeMap.Entry p;
                     for(p = e.parent; p != null && e == p.left; p = p.parent) {
                        e = p;
                     }

                     return p;
                  }

                  e = e.left;
               }
            }
         }
      }
   }

   void buildFromSorted(Iterator itr, int size) {
      ++this.modCount;
      this.size = size;
      int bottom = 0;

      for(int ssize = 1; ssize - 1 < size; ssize <<= 1) {
         ++bottom;
      }

      this.root = createFromSorted(itr, size, 0, bottom);
   }

   private static TreeMap.Entry createFromSorted(Iterator itr, int size, int level, int bottom) {
      ++level;
      if (size == 0) {
         return null;
      } else {
         int leftSize = size - 1 >> 1;
         int rightSize = size - 1 - leftSize;
         TreeMap.Entry left = createFromSorted(itr, leftSize, level, bottom);
         java.util.Map.Entry orig = (java.util.Map.Entry)itr.next();
         TreeMap.Entry right = createFromSorted(itr, rightSize, level, bottom);
         TreeMap.Entry e = new TreeMap.Entry(orig.getKey(), orig.getValue());
         if (left != null) {
            e.left = left;
            left.parent = e;
         }

         if (right != null) {
            e.right = right;
            right.parent = e;
         }

         if (level == bottom) {
            e.color = false;
         }

         return e;
      }
   }

   private void delete(TreeMap.Entry e) {
      if (e.left == null && e.right == null && e.parent == null) {
         this.root = null;
         this.size = 0;
         ++this.modCount;
      } else {
         TreeMap.Entry replacement;
         if (e.left != null && e.right != null) {
            replacement = successor(e);
            e.key = replacement.key;
            e.element = replacement.element;
            e = replacement;
         }

         if (e.left == null && e.right == null) {
            if (e.color) {
               this.fixAfterDeletion(e);
            }

            if (e.parent != null) {
               if (e == e.parent.left) {
                  e.parent.left = null;
               } else if (e == e.parent.right) {
                  e.parent.right = null;
               }

               e.parent = null;
            }
         } else {
            replacement = e.left;
            if (replacement == null) {
               replacement = e.right;
            }

            replacement.parent = e.parent;
            if (e.parent == null) {
               this.root = replacement;
            } else if (e == e.parent.left) {
               e.parent.left = replacement;
            } else {
               e.parent.right = replacement;
            }

            e.left = null;
            e.right = null;
            e.parent = null;
            if (e.color) {
               this.fixAfterDeletion(replacement);
            }
         }

         --this.size;
         ++this.modCount;
      }
   }

   static boolean colorOf(TreeMap.Entry p) {
      return p == null ? true : p.color;
   }

   static TreeMap.Entry parentOf(TreeMap.Entry p) {
      return p == null ? null : p.parent;
   }

   private static void setColor(TreeMap.Entry p, boolean c) {
      if (p != null) {
         p.color = c;
      }

   }

   private static TreeMap.Entry leftOf(TreeMap.Entry p) {
      return p == null ? null : p.left;
   }

   private static TreeMap.Entry rightOf(TreeMap.Entry p) {
      return p == null ? null : p.right;
   }

   private final void rotateLeft(TreeMap.Entry e) {
      TreeMap.Entry r = e.right;
      e.right = r.left;
      if (r.left != null) {
         r.left.parent = e;
      }

      r.parent = e.parent;
      if (e.parent == null) {
         this.root = r;
      } else if (e.parent.left == e) {
         e.parent.left = r;
      } else {
         e.parent.right = r;
      }

      r.left = e;
      e.parent = r;
   }

   private final void rotateRight(TreeMap.Entry e) {
      TreeMap.Entry l = e.left;
      e.left = l.right;
      if (l.right != null) {
         l.right.parent = e;
      }

      l.parent = e.parent;
      if (e.parent == null) {
         this.root = l;
      } else if (e.parent.right == e) {
         e.parent.right = l;
      } else {
         e.parent.left = l;
      }

      l.right = e;
      e.parent = l;
   }

   private final void fixAfterInsertion(TreeMap.Entry e) {
      e.color = false;
      TreeMap.Entry x = e;

      while(x != null && x != this.root && !x.parent.color) {
         TreeMap.Entry y;
         if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
            y = rightOf(parentOf(parentOf(x)));
            if (!colorOf(y)) {
               setColor(parentOf(x), true);
               setColor(y, true);
               setColor(parentOf(parentOf(x)), false);
               x = parentOf(parentOf(x));
            } else {
               if (x == rightOf(parentOf(x))) {
                  x = parentOf(x);
                  this.rotateLeft(x);
               }

               setColor(parentOf(x), true);
               setColor(parentOf(parentOf(x)), false);
               if (parentOf(parentOf(x)) != null) {
                  this.rotateRight(parentOf(parentOf(x)));
               }
            }
         } else {
            y = leftOf(parentOf(parentOf(x)));
            if (!colorOf(y)) {
               setColor(parentOf(x), true);
               setColor(y, true);
               setColor(parentOf(parentOf(x)), false);
               x = parentOf(parentOf(x));
            } else {
               if (x == leftOf(parentOf(x))) {
                  x = parentOf(x);
                  this.rotateRight(x);
               }

               setColor(parentOf(x), true);
               setColor(parentOf(parentOf(x)), false);
               if (parentOf(parentOf(x)) != null) {
                  this.rotateLeft(parentOf(parentOf(x)));
               }
            }
         }
      }

      this.root.color = true;
   }

   private final TreeMap.Entry fixAfterDeletion(TreeMap.Entry e) {
      TreeMap.Entry x = e;

      while(x != this.root && colorOf(x)) {
         TreeMap.Entry sib;
         if (x == leftOf(parentOf(x))) {
            sib = rightOf(parentOf(x));
            if (!colorOf(sib)) {
               setColor(sib, true);
               setColor(parentOf(x), false);
               this.rotateLeft(parentOf(x));
               sib = rightOf(parentOf(x));
            }

            if (colorOf(leftOf(sib)) && colorOf(rightOf(sib))) {
               setColor(sib, false);
               x = parentOf(x);
            } else {
               if (colorOf(rightOf(sib))) {
                  setColor(leftOf(sib), true);
                  setColor(sib, false);
                  this.rotateRight(sib);
                  sib = rightOf(parentOf(x));
               }

               setColor(sib, colorOf(parentOf(x)));
               setColor(parentOf(x), true);
               setColor(rightOf(sib), true);
               this.rotateLeft(parentOf(x));
               x = this.root;
            }
         } else {
            sib = leftOf(parentOf(x));
            if (!colorOf(sib)) {
               setColor(sib, true);
               setColor(parentOf(x), false);
               this.rotateRight(parentOf(x));
               sib = leftOf(parentOf(x));
            }

            if (colorOf(rightOf(sib)) && colorOf(leftOf(sib))) {
               setColor(sib, false);
               x = parentOf(x);
            } else {
               if (colorOf(leftOf(sib))) {
                  setColor(rightOf(sib), true);
                  setColor(sib, false);
                  this.rotateLeft(sib);
                  sib = leftOf(parentOf(x));
               }

               setColor(sib, colorOf(parentOf(x)));
               setColor(parentOf(x), true);
               setColor(leftOf(sib), true);
               this.rotateRight(parentOf(x));
               x = this.root;
            }
         }
      }

      setColor(x, true);
      return this.root;
   }

   private TreeMap.Entry getMatchingEntry(Object o) {
      if (!(o instanceof java.util.Map.Entry)) {
         return null;
      } else {
         java.util.Map.Entry e = (java.util.Map.Entry)o;
         TreeMap.Entry found = this.getEntry(e.getKey());
         return found != null && eq(found.getValue(), e.getValue()) ? found : null;
      }
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   private static int compare(Object o1, Object o2, Comparator cmp) {
      return cmp == null ? ((Comparable)o1).compareTo(o2) : cmp.compare(o1, o2);
   }

   public java.util.Map.Entry lowerEntry(Object key) {
      java.util.Map.Entry e = this.getLowerEntry(key);
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public Object lowerKey(Object key) {
      java.util.Map.Entry e = this.getLowerEntry(key);
      return e == null ? null : e.getKey();
   }

   public java.util.Map.Entry floorEntry(Object key) {
      TreeMap.Entry e = this.getFloorEntry(key);
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public Object floorKey(Object key) {
      TreeMap.Entry e = this.getFloorEntry(key);
      return e == null ? null : e.key;
   }

   public java.util.Map.Entry ceilingEntry(Object key) {
      TreeMap.Entry e = this.getCeilingEntry(key);
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public Object ceilingKey(Object key) {
      TreeMap.Entry e = this.getCeilingEntry(key);
      return e == null ? null : e.key;
   }

   public java.util.Map.Entry higherEntry(Object key) {
      TreeMap.Entry e = this.getHigherEntry(key);
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public Object higherKey(Object key) {
      TreeMap.Entry e = this.getHigherEntry(key);
      return e == null ? null : e.key;
   }

   public java.util.Map.Entry firstEntry() {
      TreeMap.Entry e = this.getFirstEntry();
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public java.util.Map.Entry lastEntry() {
      TreeMap.Entry e = this.getLastEntry();
      return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
   }

   public java.util.Map.Entry pollFirstEntry() {
      TreeMap.Entry e = this.getFirstEntry();
      if (e == null) {
         return null;
      } else {
         java.util.Map.Entry res = new AbstractMap.SimpleImmutableEntry(e);
         this.delete(e);
         return res;
      }
   }

   public java.util.Map.Entry pollLastEntry() {
      TreeMap.Entry e = this.getLastEntry();
      if (e == null) {
         return null;
      } else {
         java.util.Map.Entry res = new AbstractMap.SimpleImmutableEntry(e);
         this.delete(e);
         return res;
      }
   }

   public NavigableMap descendingMap() {
      NavigableMap map = this.descendingMap;
      if (map == null) {
         this.descendingMap = (NavigableMap)(map = new TreeMap.DescendingSubMap(true, (Object)null, true, true, (Object)null, true));
      }

      return (NavigableMap)map;
   }

   public NavigableSet descendingKeySet() {
      return this.descendingMap().navigableKeySet();
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

   public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
      return new TreeMap.AscendingSubMap(false, fromKey, fromInclusive, false, toKey, toInclusive);
   }

   public NavigableMap headMap(Object toKey, boolean toInclusive) {
      return new TreeMap.AscendingSubMap(true, (Object)null, true, false, toKey, toInclusive);
   }

   public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
      return new TreeMap.AscendingSubMap(false, fromKey, fromInclusive, true, (Object)null, true);
   }

   public Comparator comparator() {
      return this.comparator;
   }

   final Comparator reverseComparator() {
      if (this.reverseComparator == null) {
         this.reverseComparator = Collections.reverseOrder(this.comparator);
      }

      return this.reverseComparator;
   }

   public Object firstKey() {
      TreeMap.Entry e = this.getFirstEntry();
      if (e == null) {
         throw new NoSuchElementException();
      } else {
         return e.key;
      }
   }

   public Object lastKey() {
      TreeMap.Entry e = this.getLastEntry();
      if (e == null) {
         throw new NoSuchElementException();
      } else {
         return e.key;
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean containsValue(Object value) {
      if (this.root == null) {
         return false;
      } else {
         return value == null ? containsNull(this.root) : containsValue(this.root, value);
      }
   }

   private static boolean containsNull(TreeMap.Entry e) {
      if (e.element == null) {
         return true;
      } else if (e.left != null && containsNull(e.left)) {
         return true;
      } else {
         return e.right != null && containsNull(e.right);
      }
   }

   private static boolean containsValue(TreeMap.Entry e, Object val) {
      if (val.equals(e.element)) {
         return true;
      } else if (e.left != null && containsValue(e.left, val)) {
         return true;
      } else {
         return e.right != null && containsValue(e.right, val);
      }
   }

   public Object remove(Object key) {
      TreeMap.Entry e = this.getEntry(key);
      if (e == null) {
         return null;
      } else {
         Object old = e.getValue();
         this.delete(e);
         return old;
      }
   }

   public void putAll(Map map) {
      if (map instanceof SortedMap) {
         SortedMap smap = (SortedMap)map;
         if (eq(this.comparator, smap.comparator())) {
            this.buildFromSorted(smap.entrySet().iterator(), map.size());
            return;
         }
      }

      super.putAll(map);
   }

   public Set keySet() {
      return this.navigableKeySet();
   }

   public NavigableSet navigableKeySet() {
      if (this.navigableKeySet == null) {
         this.navigableKeySet = new TreeMap.AscendingKeySet();
      }

      return this.navigableKeySet;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeInt(this.size);

      for(TreeMap.Entry e = this.getFirstEntry(); e != null; e = successor(e)) {
         out.writeObject(e.key);
         out.writeObject(e.element);
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      int size = in.readInt();

      try {
         this.buildFromSorted(new TreeMap.IOIterator(in, size), size);
      } catch (TreeMap.IteratorIOException var4) {
         throw var4.getException();
      } catch (TreeMap.IteratorNoClassException var5) {
         throw var5.getException();
      }
   }

   private class SubMap extends AbstractMap implements Serializable, NavigableMap {
      private static final long serialVersionUID = -6520786458950516097L;
      final Object fromKey;
      final Object toKey;

      SubMap() {
         this.fromKey = this.toKey = null;
      }

      private Object readResolve() {
         return TreeMap.this.new AscendingSubMap(this.fromKey == null, this.fromKey, true, this.toKey == null, this.toKey, false);
      }

      public java.util.Map.Entry lowerEntry(Object key) {
         throw new Error();
      }

      public Object lowerKey(Object key) {
         throw new Error();
      }

      public java.util.Map.Entry floorEntry(Object key) {
         throw new Error();
      }

      public Object floorKey(Object key) {
         throw new Error();
      }

      public java.util.Map.Entry ceilingEntry(Object key) {
         throw new Error();
      }

      public Object ceilingKey(Object key) {
         throw new Error();
      }

      public java.util.Map.Entry higherEntry(Object key) {
         throw new Error();
      }

      public Object higherKey(Object key) {
         throw new Error();
      }

      public java.util.Map.Entry firstEntry() {
         throw new Error();
      }

      public java.util.Map.Entry lastEntry() {
         throw new Error();
      }

      public java.util.Map.Entry pollFirstEntry() {
         throw new Error();
      }

      public java.util.Map.Entry pollLastEntry() {
         throw new Error();
      }

      public NavigableMap descendingMap() {
         throw new Error();
      }

      public NavigableSet navigableKeySet() {
         throw new Error();
      }

      public NavigableSet descendingKeySet() {
         throw new Error();
      }

      public Set entrySet() {
         throw new Error();
      }

      public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
         throw new Error();
      }

      public NavigableMap headMap(Object toKey, boolean inclusive) {
         throw new Error();
      }

      public NavigableMap tailMap(Object fromKey, boolean inclusive) {
         throw new Error();
      }

      public SortedMap subMap(Object fromKey, Object toKey) {
         throw new Error();
      }

      public SortedMap headMap(Object toKey) {
         throw new Error();
      }

      public SortedMap tailMap(Object fromKey) {
         throw new Error();
      }

      public Comparator comparator() {
         throw new Error();
      }

      public Object firstKey() {
         throw new Error();
      }

      public Object lastKey() {
         throw new Error();
      }
   }

   static class IOIterator implements Iterator {
      final ObjectInputStream ois;
      int remaining;

      IOIterator(ObjectInputStream ois, int remaining) {
         this.ois = ois;
         this.remaining = remaining;
      }

      public boolean hasNext() {
         return this.remaining > 0;
      }

      public Object next() {
         if (this.remaining <= 0) {
            throw new NoSuchElementException();
         } else {
            --this.remaining;

            try {
               return new AbstractMap.SimpleImmutableEntry(this.ois.readObject(), this.ois.readObject());
            } catch (IOException var2) {
               throw new TreeMap.IteratorIOException(var2);
            } catch (ClassNotFoundException var3) {
               throw new TreeMap.IteratorNoClassException(var3);
            }
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   static class IteratorNoClassException extends RuntimeException {
      IteratorNoClassException(ClassNotFoundException e) {
         super(e);
      }

      ClassNotFoundException getException() {
         return (ClassNotFoundException)this.getCause();
      }
   }

   static class IteratorIOException extends RuntimeException {
      IteratorIOException(IOException e) {
         super(e);
      }

      IOException getException() {
         return (IOException)this.getCause();
      }
   }

   class DescendingSubMap extends TreeMap.NavigableSubMap {
      DescendingSubMap(boolean fromStart, Object fromKey, boolean fromInclusive, boolean toEnd, Object toKey, boolean toInclusive) {
         super(fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
      }

      public Comparator comparator() {
         return TreeMap.this.reverseComparator();
      }

      protected TreeMap.Entry first() {
         return this.absHighest();
      }

      protected TreeMap.Entry last() {
         return this.absLowest();
      }

      protected TreeMap.Entry lower(Object key) {
         return this.absHigher(key);
      }

      protected TreeMap.Entry floor(Object key) {
         return this.absCeiling(key);
      }

      protected TreeMap.Entry ceiling(Object key) {
         return this.absFloor(key);
      }

      protected TreeMap.Entry higher(Object key) {
         return this.absLower(key);
      }

      protected TreeMap.Entry uncheckedHigher(TreeMap.Entry e) {
         return TreeMap.predecessor(e);
      }

      public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
         if (!this.inRange(fromKey, fromInclusive)) {
            throw new IllegalArgumentException("fromKey out of range");
         } else if (!this.inRange(toKey, toInclusive)) {
            throw new IllegalArgumentException("toKey out of range");
         } else {
            return TreeMap.this.new DescendingSubMap(false, toKey, toInclusive, false, fromKey, fromInclusive);
         }
      }

      public NavigableMap headMap(Object toKey, boolean toInclusive) {
         if (!this.inRange(toKey, toInclusive)) {
            throw new IllegalArgumentException("toKey out of range");
         } else {
            return TreeMap.this.new DescendingSubMap(false, toKey, toInclusive, this.toEnd, this.toKey, this.toInclusive);
         }
      }

      public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
         if (!this.inRange(fromKey, fromInclusive)) {
            throw new IllegalArgumentException("fromKey out of range");
         } else {
            return TreeMap.this.new DescendingSubMap(this.fromStart, this.fromKey, this.fromInclusive, false, fromKey, fromInclusive);
         }
      }

      public NavigableMap descendingMap() {
         if (this.descendingMap == null) {
            this.descendingMap = TreeMap.this.new AscendingSubMap(this.fromStart, this.fromKey, this.fromInclusive, this.toEnd, this.toKey, this.toInclusive);
         }

         return this.descendingMap;
      }
   }

   class AscendingSubMap extends TreeMap.NavigableSubMap {
      AscendingSubMap(boolean fromStart, Object fromKey, boolean fromInclusive, boolean toEnd, Object toKey, boolean toInclusive) {
         super(fromStart, fromKey, fromInclusive, toEnd, toKey, toInclusive);
      }

      public Comparator comparator() {
         return TreeMap.this.comparator;
      }

      protected TreeMap.Entry first() {
         return this.absLowest();
      }

      protected TreeMap.Entry last() {
         return this.absHighest();
      }

      protected TreeMap.Entry lower(Object key) {
         return this.absLower(key);
      }

      protected TreeMap.Entry floor(Object key) {
         return this.absFloor(key);
      }

      protected TreeMap.Entry ceiling(Object key) {
         return this.absCeiling(key);
      }

      protected TreeMap.Entry higher(Object key) {
         return this.absHigher(key);
      }

      protected TreeMap.Entry uncheckedHigher(TreeMap.Entry e) {
         return TreeMap.successor(e);
      }

      public NavigableMap subMap(Object fromKey, boolean fromInclusive, Object toKey, boolean toInclusive) {
         if (!this.inRange(fromKey, fromInclusive)) {
            throw new IllegalArgumentException("fromKey out of range");
         } else if (!this.inRange(toKey, toInclusive)) {
            throw new IllegalArgumentException("toKey out of range");
         } else {
            return TreeMap.this.new AscendingSubMap(false, fromKey, fromInclusive, false, toKey, toInclusive);
         }
      }

      public NavigableMap headMap(Object toKey, boolean toInclusive) {
         if (!this.inRange(toKey, toInclusive)) {
            throw new IllegalArgumentException("toKey out of range");
         } else {
            return TreeMap.this.new AscendingSubMap(this.fromStart, this.fromKey, this.fromInclusive, false, toKey, toInclusive);
         }
      }

      public NavigableMap tailMap(Object fromKey, boolean fromInclusive) {
         if (!this.inRange(fromKey, fromInclusive)) {
            throw new IllegalArgumentException("fromKey out of range");
         } else {
            return TreeMap.this.new AscendingSubMap(false, fromKey, fromInclusive, this.toEnd, this.toKey, this.toInclusive);
         }
      }

      public NavigableMap descendingMap() {
         if (this.descendingMap == null) {
            this.descendingMap = TreeMap.this.new DescendingSubMap(this.fromStart, this.fromKey, this.fromInclusive, this.toEnd, this.toKey, this.toInclusive);
         }

         return this.descendingMap;
      }
   }

   private abstract class NavigableSubMap extends AbstractMap implements NavigableMap, Serializable {
      private static final long serialVersionUID = -6520786458950516097L;
      final Object fromKey;
      final Object toKey;
      final boolean fromStart;
      final boolean toEnd;
      final boolean fromInclusive;
      final boolean toInclusive;
      transient int cachedSize = -1;
      transient int cacheVersion;
      transient TreeMap.NavigableSubMap.SubEntrySet entrySet;
      transient NavigableMap descendingMap;
      transient NavigableSet navigableKeySet;

      NavigableSubMap(boolean fromStart, Object fromKey, boolean fromInclusive, boolean toEnd, Object toKey, boolean toInclusive) {
         if (!fromStart && !toEnd) {
            if (TreeMap.compare(fromKey, toKey, TreeMap.this.comparator) > 0) {
               throw new IllegalArgumentException("fromKey > toKey");
            }
         } else {
            if (!fromStart) {
               TreeMap.compare(fromKey, fromKey, TreeMap.this.comparator);
            }

            if (!toEnd) {
               TreeMap.compare(toKey, toKey, TreeMap.this.comparator);
            }
         }

         this.fromStart = fromStart;
         this.toEnd = toEnd;
         this.fromKey = fromKey;
         this.toKey = toKey;
         this.fromInclusive = fromInclusive;
         this.toInclusive = toInclusive;
      }

      final TreeMap.Entry checkLoRange(TreeMap.Entry e) {
         return e != null && !this.absTooLow(e.key) ? e : null;
      }

      final TreeMap.Entry checkHiRange(TreeMap.Entry e) {
         return e != null && !this.absTooHigh(e.key) ? e : null;
      }

      final boolean inRange(Object key) {
         return !this.absTooLow(key) && !this.absTooHigh(key);
      }

      final boolean inRangeExclusive(Object key) {
         return (this.fromStart || TreeMap.compare(key, this.fromKey, TreeMap.this.comparator) >= 0) && (this.toEnd || TreeMap.compare(this.toKey, key, TreeMap.this.comparator) >= 0);
      }

      final boolean inRange(Object key, boolean inclusive) {
         return inclusive ? this.inRange(key) : this.inRangeExclusive(key);
      }

      private boolean absTooHigh(Object key) {
         if (this.toEnd) {
            return false;
         } else {
            int c = TreeMap.compare(key, this.toKey, TreeMap.this.comparator);
            return c > 0 || c == 0 && !this.toInclusive;
         }
      }

      private boolean absTooLow(Object key) {
         if (this.fromStart) {
            return false;
         } else {
            int c = TreeMap.compare(key, this.fromKey, TreeMap.this.comparator);
            return c < 0 || c == 0 && !this.fromInclusive;
         }
      }

      protected abstract TreeMap.Entry first();

      protected abstract TreeMap.Entry last();

      protected abstract TreeMap.Entry lower(Object var1);

      protected abstract TreeMap.Entry floor(Object var1);

      protected abstract TreeMap.Entry ceiling(Object var1);

      protected abstract TreeMap.Entry higher(Object var1);

      protected abstract TreeMap.Entry uncheckedHigher(TreeMap.Entry var1);

      final TreeMap.Entry absLowest() {
         return this.checkHiRange(this.fromStart ? TreeMap.this.getFirstEntry() : (this.fromInclusive ? TreeMap.this.getCeilingEntry(this.fromKey) : TreeMap.this.getHigherEntry(this.fromKey)));
      }

      final TreeMap.Entry absHighest() {
         return this.checkLoRange(this.toEnd ? TreeMap.this.getLastEntry() : (this.toInclusive ? TreeMap.this.getFloorEntry(this.toKey) : TreeMap.this.getLowerEntry(this.toKey)));
      }

      final TreeMap.Entry absLower(Object key) {
         return this.absTooHigh(key) ? this.absHighest() : this.checkLoRange(TreeMap.this.getLowerEntry(key));
      }

      final TreeMap.Entry absFloor(Object key) {
         return this.absTooHigh(key) ? this.absHighest() : this.checkLoRange(TreeMap.this.getFloorEntry(key));
      }

      final TreeMap.Entry absCeiling(Object key) {
         return this.absTooLow(key) ? this.absLowest() : this.checkHiRange(TreeMap.this.getCeilingEntry(key));
      }

      final TreeMap.Entry absHigher(Object key) {
         return this.absTooLow(key) ? this.absLowest() : this.checkHiRange(TreeMap.this.getHigherEntry(key));
      }

      public java.util.Map.Entry firstEntry() {
         TreeMap.Entry e = this.first();
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object firstKey() {
         TreeMap.Entry e = this.first();
         if (e == null) {
            throw new NoSuchElementException();
         } else {
            return e.key;
         }
      }

      public java.util.Map.Entry lastEntry() {
         TreeMap.Entry e = this.last();
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object lastKey() {
         TreeMap.Entry e = this.last();
         if (e == null) {
            throw new NoSuchElementException();
         } else {
            return e.key;
         }
      }

      public java.util.Map.Entry pollFirstEntry() {
         TreeMap.Entry e = this.first();
         if (e == null) {
            return null;
         } else {
            java.util.Map.Entry result = new AbstractMap.SimpleImmutableEntry(e);
            TreeMap.this.delete(e);
            return result;
         }
      }

      public java.util.Map.Entry pollLastEntry() {
         TreeMap.Entry e = this.last();
         if (e == null) {
            return null;
         } else {
            java.util.Map.Entry result = new AbstractMap.SimpleImmutableEntry(e);
            TreeMap.this.delete(e);
            return result;
         }
      }

      public java.util.Map.Entry lowerEntry(Object key) {
         TreeMap.Entry e = this.lower(key);
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object lowerKey(Object key) {
         TreeMap.Entry e = this.lower(key);
         return e == null ? null : e.key;
      }

      public java.util.Map.Entry floorEntry(Object key) {
         TreeMap.Entry e = this.floor(key);
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object floorKey(Object key) {
         TreeMap.Entry e = this.floor(key);
         return e == null ? null : e.key;
      }

      public java.util.Map.Entry ceilingEntry(Object key) {
         TreeMap.Entry e = this.ceiling(key);
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object ceilingKey(Object key) {
         TreeMap.Entry e = this.ceiling(key);
         return e == null ? null : e.key;
      }

      public java.util.Map.Entry higherEntry(Object key) {
         TreeMap.Entry e = this.higher(key);
         return e == null ? null : new AbstractMap.SimpleImmutableEntry(e);
      }

      public Object higherKey(Object key) {
         TreeMap.Entry e = this.higher(key);
         return e == null ? null : e.key;
      }

      public NavigableSet descendingKeySet() {
         return this.descendingMap().navigableKeySet();
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

      public int size() {
         if (this.cachedSize < 0 || this.cacheVersion != TreeMap.this.modCount) {
            this.cachedSize = this.recalculateSize();
            this.cacheVersion = TreeMap.this.modCount;
         }

         return this.cachedSize;
      }

      private int recalculateSize() {
         TreeMap.Entry terminator = this.absHighest();
         Object terminalKey = terminator != null ? terminator.key : null;
         int size = 0;

         for(TreeMap.Entry e = this.absLowest(); e != null; e = e.key == terminalKey ? null : TreeMap.successor(e)) {
            ++size;
         }

         return size;
      }

      public boolean isEmpty() {
         return this.absLowest() == null;
      }

      public boolean containsKey(Object key) {
         return this.inRange(key) && TreeMap.this.containsKey(key);
      }

      public Object get(Object key) {
         return !this.inRange(key) ? null : TreeMap.this.get(key);
      }

      public Object put(Object key, Object value) {
         if (!this.inRange(key)) {
            throw new IllegalArgumentException("Key out of range");
         } else {
            return TreeMap.this.put(key, value);
         }
      }

      public Object remove(Object key) {
         return !this.inRange(key) ? null : TreeMap.this.remove(key);
      }

      public Set entrySet() {
         if (this.entrySet == null) {
            this.entrySet = new TreeMap.NavigableSubMap.SubEntrySet();
         }

         return this.entrySet;
      }

      public Set keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet navigableKeySet() {
         if (this.navigableKeySet == null) {
            this.navigableKeySet = new TreeMap.NavigableSubMap.SubKeySet();
         }

         return this.navigableKeySet;
      }

      private TreeMap.Entry getMatchingSubEntry(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return null;
         } else {
            java.util.Map.Entry e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            if (!this.inRange(key)) {
               return null;
            } else {
               TreeMap.Entry found = TreeMap.this.getEntry(key);
               return found != null && TreeMap.eq(found.getValue(), e.getValue()) ? found : null;
            }
         }
      }

      class SubKeyIterator implements Iterator {
         final Iterator itr;

         SubKeyIterator(Iterator itr) {
            this.itr = itr;
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public Object next() {
            return ((java.util.Map.Entry)this.itr.next()).getKey();
         }

         public void remove() {
            this.itr.remove();
         }
      }

      class SubEntryIterator extends TreeMap.BaseEntryIterator implements Iterator {
         final Object terminalKey;

         SubEntryIterator() {
            super(NavigableSubMap.this.first());
            TreeMap.Entry terminator = NavigableSubMap.this.last();
            this.terminalKey = terminator == null ? null : terminator.key;
         }

         public boolean hasNext() {
            return this.cursor != null;
         }

         public Object next() {
            TreeMap.Entry curr = this.cursor;
            if (curr == null) {
               throw new NoSuchElementException();
            } else if (this.expectedModCount != TreeMap.this.modCount) {
               throw new ConcurrentModificationException();
            } else {
               this.cursor = curr.key == this.terminalKey ? null : NavigableSubMap.this.uncheckedHigher(curr);
               this.lastRet = curr;
               return curr;
            }
         }
      }

      class SubKeySet extends java.util.AbstractSet implements NavigableSet {
         public int size() {
            return NavigableSubMap.this.size();
         }

         public boolean isEmpty() {
            return NavigableSubMap.this.isEmpty();
         }

         public void clear() {
            NavigableSubMap.this.clear();
         }

         public boolean contains(Object o) {
            return TreeMap.this.getEntry(o) != null;
         }

         public boolean remove(Object o) {
            if (!NavigableSubMap.this.inRange(o)) {
               return false;
            } else {
               TreeMap.Entry found = TreeMap.this.getEntry(o);
               if (found == null) {
                  return false;
               } else {
                  TreeMap.this.delete(found);
                  return true;
               }
            }
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

         public Iterator iterator() {
            return NavigableSubMap.this.new SubKeyIterator(NavigableSubMap.this.entrySet().iterator());
         }

         public Iterator descendingIterator() {
            return NavigableSubMap.this.new SubKeyIterator(NavigableSubMap.this.descendingMap().entrySet().iterator());
         }

         public Object lower(Object e) {
            return NavigableSubMap.this.lowerKey(e);
         }

         public Object floor(Object e) {
            return NavigableSubMap.this.floorKey(e);
         }

         public Object ceiling(Object e) {
            return NavigableSubMap.this.ceilingKey(e);
         }

         public Object higher(Object e) {
            return NavigableSubMap.this.higherKey(e);
         }

         public Object first() {
            return NavigableSubMap.this.firstKey();
         }

         public Object last() {
            return NavigableSubMap.this.lastKey();
         }

         public Comparator comparator() {
            return NavigableSubMap.this.comparator();
         }

         public Object pollFirst() {
            java.util.Map.Entry e = NavigableSubMap.this.pollFirstEntry();
            return e == null ? null : e.getKey();
         }

         public Object pollLast() {
            java.util.Map.Entry e = NavigableSubMap.this.pollLastEntry();
            return e == null ? null : e.getKey();
         }

         public NavigableSet subSet(Object fromElement, boolean fromInclusive, Object toElement, boolean toInclusive) {
            return (NavigableSet)NavigableSubMap.this.subMap(fromElement, fromInclusive, toElement, toInclusive).keySet();
         }

         public NavigableSet headSet(Object toElement, boolean inclusive) {
            return (NavigableSet)NavigableSubMap.this.headMap(toElement, inclusive).keySet();
         }

         public NavigableSet tailSet(Object fromElement, boolean inclusive) {
            return (NavigableSet)NavigableSubMap.this.tailMap(fromElement, inclusive).keySet();
         }

         public NavigableSet descendingSet() {
            return (NavigableSet)NavigableSubMap.this.descendingMap().keySet();
         }
      }

      class SubEntrySet extends java.util.AbstractSet {
         public int size() {
            return NavigableSubMap.this.size();
         }

         public boolean isEmpty() {
            return NavigableSubMap.this.isEmpty();
         }

         public boolean contains(Object o) {
            return NavigableSubMap.this.getMatchingSubEntry(o) != null;
         }

         public boolean remove(Object o) {
            TreeMap.Entry e = NavigableSubMap.this.getMatchingSubEntry(o);
            if (e == null) {
               return false;
            } else {
               TreeMap.this.delete(e);
               return true;
            }
         }

         public Iterator iterator() {
            return NavigableSubMap.this.new SubEntryIterator();
         }
      }
   }

   class DescendingKeySet extends TreeMap.KeySet {
      DescendingKeySet() {
         super();
      }

      public Iterator iterator() {
         return TreeMap.this.new DescendingKeyIterator(TreeMap.this.getLastEntry());
      }

      public Iterator descendingIterator() {
         return TreeMap.this.new KeyIterator(TreeMap.this.getFirstEntry());
      }

      public Object lower(Object e) {
         return TreeMap.this.higherKey(e);
      }

      public Object floor(Object e) {
         return TreeMap.this.ceilingKey(e);
      }

      public Object ceiling(Object e) {
         return TreeMap.this.floorKey(e);
      }

      public Object higher(Object e) {
         return TreeMap.this.lowerKey(e);
      }

      public Object first() {
         return TreeMap.this.lastKey();
      }

      public Object last() {
         return TreeMap.this.firstKey();
      }

      public Comparator comparator() {
         return TreeMap.this.descendingMap().comparator();
      }

      public Object pollFirst() {
         java.util.Map.Entry e = TreeMap.this.pollLastEntry();
         return e == null ? null : e.getKey();
      }

      public Object pollLast() {
         java.util.Map.Entry e = TreeMap.this.pollFirstEntry();
         return e == null ? null : e.getKey();
      }

      public NavigableSet subSet(Object fromElement, boolean fromInclusive, Object toElement, boolean toInclusive) {
         return (NavigableSet)TreeMap.this.descendingMap().subMap(fromElement, fromInclusive, toElement, toInclusive).keySet();
      }

      public NavigableSet headSet(Object toElement, boolean inclusive) {
         return (NavigableSet)TreeMap.this.descendingMap().headMap(toElement, inclusive).keySet();
      }

      public NavigableSet tailSet(Object fromElement, boolean inclusive) {
         return (NavigableSet)TreeMap.this.descendingMap().tailMap(fromElement, inclusive).keySet();
      }

      public NavigableSet descendingSet() {
         return (NavigableSet)TreeMap.this.keySet();
      }
   }

   class AscendingKeySet extends TreeMap.KeySet {
      AscendingKeySet() {
         super();
      }

      public Iterator iterator() {
         return TreeMap.this.new KeyIterator(TreeMap.this.getFirstEntry());
      }

      public Iterator descendingIterator() {
         return TreeMap.this.new DescendingKeyIterator(TreeMap.this.getFirstEntry());
      }

      public Object lower(Object e) {
         return TreeMap.this.lowerKey(e);
      }

      public Object floor(Object e) {
         return TreeMap.this.floorKey(e);
      }

      public Object ceiling(Object e) {
         return TreeMap.this.ceilingKey(e);
      }

      public Object higher(Object e) {
         return TreeMap.this.higherKey(e);
      }

      public Object first() {
         return TreeMap.this.firstKey();
      }

      public Object last() {
         return TreeMap.this.lastKey();
      }

      public Comparator comparator() {
         return TreeMap.this.comparator();
      }

      public Object pollFirst() {
         java.util.Map.Entry e = TreeMap.this.pollFirstEntry();
         return e == null ? null : e.getKey();
      }

      public Object pollLast() {
         java.util.Map.Entry e = TreeMap.this.pollLastEntry();
         return e == null ? null : e.getKey();
      }

      public NavigableSet subSet(Object fromElement, boolean fromInclusive, Object toElement, boolean toInclusive) {
         return (NavigableSet)TreeMap.this.subMap(fromElement, fromInclusive, toElement, toInclusive).keySet();
      }

      public NavigableSet headSet(Object toElement, boolean inclusive) {
         return (NavigableSet)TreeMap.this.headMap(toElement, inclusive).keySet();
      }

      public NavigableSet tailSet(Object fromElement, boolean inclusive) {
         return (NavigableSet)TreeMap.this.tailMap(fromElement, inclusive).keySet();
      }

      public NavigableSet descendingSet() {
         return (NavigableSet)TreeMap.this.descendingMap().keySet();
      }
   }

   abstract class KeySet extends java.util.AbstractSet implements NavigableSet {
      public int size() {
         return TreeMap.this.size();
      }

      public boolean isEmpty() {
         return TreeMap.this.isEmpty();
      }

      public void clear() {
         TreeMap.this.clear();
      }

      public boolean contains(Object o) {
         return TreeMap.this.getEntry(o) != null;
      }

      public boolean remove(Object o) {
         TreeMap.Entry found = TreeMap.this.getEntry(o);
         if (found == null) {
            return false;
         } else {
            TreeMap.this.delete(found);
            return true;
         }
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
   }

   class ValueSet extends java.util.AbstractSet {
      public int size() {
         return TreeMap.this.size();
      }

      public boolean isEmpty() {
         return TreeMap.this.isEmpty();
      }

      public void clear() {
         TreeMap.this.clear();
      }

      public boolean contains(Object o) {
         for(TreeMap.Entry e = TreeMap.this.getFirstEntry(); e != null; e = TreeMap.successor(e)) {
            if (TreeMap.eq(o, e.element)) {
               return true;
            }
         }

         return false;
      }

      public Iterator iterator() {
         return TreeMap.this.new ValueIterator(TreeMap.this.getFirstEntry());
      }

      public boolean remove(Object o) {
         for(TreeMap.Entry e = TreeMap.this.getFirstEntry(); e != null; e = TreeMap.successor(e)) {
            if (TreeMap.eq(o, e.element)) {
               TreeMap.this.delete(e);
               return true;
            }
         }

         return false;
      }
   }

   class DescendingEntrySet extends TreeMap.EntrySet {
      DescendingEntrySet() {
         super();
      }

      public Iterator iterator() {
         return TreeMap.this.new DescendingEntryIterator(TreeMap.this.getLastEntry());
      }
   }

   class EntrySet extends java.util.AbstractSet {
      public int size() {
         return TreeMap.this.size();
      }

      public boolean isEmpty() {
         return TreeMap.this.isEmpty();
      }

      public void clear() {
         TreeMap.this.clear();
      }

      public Iterator iterator() {
         return TreeMap.this.new EntryIterator(TreeMap.this.getFirstEntry());
      }

      public boolean contains(Object o) {
         return TreeMap.this.getMatchingEntry(o) != null;
      }

      public boolean remove(Object o) {
         TreeMap.Entry e = TreeMap.this.getMatchingEntry(o);
         if (e == null) {
            return false;
         } else {
            TreeMap.this.delete(e);
            return true;
         }
      }
   }

   class DescendingValueIterator extends TreeMap.BaseEntryIterator implements Iterator {
      DescendingValueIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.prevEntry().element;
      }
   }

   class DescendingKeyIterator extends TreeMap.BaseEntryIterator implements Iterator {
      DescendingKeyIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.prevEntry().key;
      }
   }

   class DescendingEntryIterator extends TreeMap.BaseEntryIterator implements Iterator {
      DescendingEntryIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.prevEntry();
      }
   }

   class ValueIterator extends TreeMap.BaseEntryIterator implements Iterator {
      ValueIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.nextEntry().element;
      }
   }

   class KeyIterator extends TreeMap.BaseEntryIterator implements Iterator {
      KeyIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.nextEntry().key;
      }
   }

   class EntryIterator extends TreeMap.BaseEntryIterator implements Iterator {
      EntryIterator(TreeMap.Entry cursor) {
         super(cursor);
      }

      public Object next() {
         return this.nextEntry();
      }
   }

   private class BaseEntryIterator {
      TreeMap.Entry cursor;
      TreeMap.Entry lastRet;
      int expectedModCount;

      BaseEntryIterator(TreeMap.Entry cursor) {
         this.cursor = cursor;
         this.expectedModCount = TreeMap.this.modCount;
      }

      public boolean hasNext() {
         return this.cursor != null;
      }

      TreeMap.Entry nextEntry() {
         TreeMap.Entry curr = this.cursor;
         if (curr == null) {
            throw new NoSuchElementException();
         } else if (this.expectedModCount != TreeMap.this.modCount) {
            throw new ConcurrentModificationException();
         } else {
            this.cursor = TreeMap.successor(curr);
            this.lastRet = curr;
            return curr;
         }
      }

      TreeMap.Entry prevEntry() {
         TreeMap.Entry curr = this.cursor;
         if (curr == null) {
            throw new NoSuchElementException();
         } else if (this.expectedModCount != TreeMap.this.modCount) {
            throw new ConcurrentModificationException();
         } else {
            this.cursor = TreeMap.predecessor(curr);
            this.lastRet = curr;
            return curr;
         }
      }

      public void remove() {
         if (this.lastRet == null) {
            throw new IllegalStateException();
         } else if (this.expectedModCount != TreeMap.this.modCount) {
            throw new ConcurrentModificationException();
         } else {
            if (this.lastRet.left != null && this.lastRet.right != null && this.cursor != null) {
               this.cursor = this.lastRet;
            }

            TreeMap.this.delete(this.lastRet);
            this.lastRet = null;
            ++this.expectedModCount;
         }
      }
   }

   public static class Entry implements java.util.Map.Entry, Cloneable, Serializable {
      private static final boolean RED = false;
      private static final boolean BLACK = true;
      private Object key;
      private Object element;
      private boolean color;
      private TreeMap.Entry left;
      private TreeMap.Entry right;
      private TreeMap.Entry parent;

      public Entry(Object key, Object element) {
         this.key = key;
         this.element = element;
         this.color = true;
      }

      protected Object clone() throws CloneNotSupportedException {
         TreeMap.Entry t = new TreeMap.Entry(this.key, this.element);
         t.color = this.color;
         return t;
      }

      public final Object getKey() {
         return this.key;
      }

      public final Object getValue() {
         return this.element;
      }

      public final Object setValue(Object v) {
         Object old = this.element;
         this.element = v;
         return old;
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry e = (java.util.Map.Entry)o;
            return TreeMap.eq(this.key, e.getKey()) && TreeMap.eq(this.element, e.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.element == null ? 0 : this.element.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.element;
      }
   }
}
