package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Map.Entry;

public class TreeSet extends java.util.AbstractSet implements NavigableSet, Cloneable, Serializable {
   private static final long serialVersionUID = -2479143000061671589L;
   private static final Object PRESENT = new Object();
   private transient NavigableMap map;

   public TreeSet() {
      this.map = new TreeMap();
   }

   public TreeSet(Comparator comparator) {
      this.map = new TreeMap(comparator);
   }

   public TreeSet(Collection c) {
      this.map = new TreeMap();
      this.addAll(c);
   }

   public TreeSet(SortedSet s) {
      this.map = new TreeMap(s.comparator());
      this.addAll(s);
   }

   private TreeSet(NavigableMap map) {
      this.map = map;
   }

   public Object lower(Object e) {
      return this.map.lowerKey(e);
   }

   public Object floor(Object e) {
      return this.map.floorKey(e);
   }

   public Object ceiling(Object e) {
      return this.map.ceilingKey(e);
   }

   public Object higher(Object e) {
      return this.map.higherKey(e);
   }

   public Object pollFirst() {
      Entry e = this.map.pollFirstEntry();
      return e != null ? e.getKey() : null;
   }

   public Object pollLast() {
      Entry e = this.map.pollLastEntry();
      return e != null ? e.getKey() : null;
   }

   public Iterator iterator() {
      return this.map.keySet().iterator();
   }

   public Iterator descendingIterator() {
      return this.map.descendingKeySet().iterator();
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

   public NavigableSet subSet(Object fromElement, boolean fromInclusive, Object toElement, boolean toInclusive) {
      return new TreeSet(this.map.subMap(fromElement, fromInclusive, toElement, toInclusive));
   }

   public NavigableSet headSet(Object toElement, boolean toInclusive) {
      return new TreeSet(this.map.headMap(toElement, toInclusive));
   }

   public NavigableSet tailSet(Object fromElement, boolean fromInclusive) {
      return new TreeSet(this.map.tailMap(fromElement, fromInclusive));
   }

   public NavigableSet descendingSet() {
      return new TreeSet(this.map.descendingMap());
   }

   public Comparator comparator() {
      return this.map.comparator();
   }

   public Object first() {
      return this.map.firstKey();
   }

   public Object last() {
      return this.map.lastKey();
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public boolean contains(Object o) {
      return this.map.containsKey(o);
   }

   public Object[] toArray() {
      return this.map.keySet().toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.map.keySet().toArray(a);
   }

   public boolean add(Object o) {
      return this.map.put(o, PRESENT) == null;
   }

   public boolean remove(Object o) {
      return this.map.remove(o) != null;
   }

   public boolean addAll(Collection c) {
      if (this.map.size() == 0 && c.size() > 0 && c instanceof SortedSet && this.map instanceof TreeMap && eq(((SortedSet)c).comparator(), this.comparator())) {
         ((TreeMap)this.map).buildFromSorted(new TreeSet.MapIterator(c.iterator()), c.size());
         return true;
      } else {
         return super.addAll(c);
      }
   }

   public void clear() {
      this.map.clear();
   }

   public Object clone() {
      TreeSet clone;
      try {
         clone = (TreeSet)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      clone.map = new TreeMap(this.map);
      return clone;
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(this.map.comparator());
      out.writeInt(this.map.size());
      Iterator itr = this.map.keySet().iterator();

      while(itr.hasNext()) {
         out.writeObject(itr.next());
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      Comparator comparator = (Comparator)in.readObject();
      TreeMap map = new TreeMap(comparator);
      int size = in.readInt();

      try {
         map.buildFromSorted(new TreeSet.IOIterator(in, size), size);
         this.map = map;
      } catch (TreeMap.IteratorIOException var6) {
         throw var6.getException();
      } catch (TreeMap.IteratorNoClassException var7) {
         throw var7.getException();
      }
   }

   static class IOIterator extends TreeMap.IOIterator {
      IOIterator(ObjectInputStream in, int remaining) {
         super(in, remaining);
      }

      public Object next() {
         if (this.remaining <= 0) {
            throw new NoSuchElementException();
         } else {
            --this.remaining;

            try {
               return new AbstractMap.SimpleImmutableEntry(this.ois.readObject(), TreeSet.PRESENT);
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

   private static class MapIterator implements Iterator {
      final Iterator itr;

      MapIterator(Iterator itr) {
         this.itr = itr;
      }

      public boolean hasNext() {
         return this.itr.hasNext();
      }

      public Object next() {
         return new AbstractMap.SimpleImmutableEntry(this.itr.next(), TreeSet.PRESENT);
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
