package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractSet;
import edu.emory.mathcs.backport.java.util.NavigableSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;

public class ConcurrentSkipListSet extends AbstractSet implements NavigableSet, Cloneable, Serializable {
   private static final long serialVersionUID = -2479143111061671589L;
   private final ConcurrentNavigableMap m;

   public ConcurrentSkipListSet() {
      this.m = new ConcurrentSkipListMap();
   }

   public ConcurrentSkipListSet(Comparator comparator) {
      this.m = new ConcurrentSkipListMap(comparator);
   }

   public ConcurrentSkipListSet(Collection c) {
      this.m = new ConcurrentSkipListMap();
      this.addAll(c);
   }

   public ConcurrentSkipListSet(SortedSet s) {
      this.m = new ConcurrentSkipListMap(s.comparator());
      this.addAll(s);
   }

   ConcurrentSkipListSet(ConcurrentNavigableMap m) {
      this.m = m;
   }

   public Object clone() {
      if (this.getClass() != ConcurrentSkipListSet.class) {
         throw new UnsupportedOperationException("Can't clone subclasses");
      } else {
         return new ConcurrentSkipListSet(new ConcurrentSkipListMap(this.m));
      }
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

   public boolean add(Object e) {
      return this.m.putIfAbsent(e, Boolean.TRUE) == null;
   }

   public boolean remove(Object o) {
      return this.m.remove(o, Boolean.TRUE);
   }

   public void clear() {
      this.m.clear();
   }

   public Iterator iterator() {
      return this.m.navigableKeySet().iterator();
   }

   public Iterator descendingIterator() {
      return this.m.descendingKeySet().iterator();
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

   public boolean removeAll(Collection c) {
      boolean modified = false;
      Iterator i = c.iterator();

      while(i.hasNext()) {
         if (this.remove(i.next())) {
            modified = true;
         }
      }

      return modified;
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

   public Object pollFirst() {
      Entry e = this.m.pollFirstEntry();
      return e == null ? null : e.getKey();
   }

   public Object pollLast() {
      Entry e = this.m.pollLastEntry();
      return e == null ? null : e.getKey();
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
