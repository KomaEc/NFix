package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;

public class Collections {
   private Collections() {
   }

   public static void sort(List list) {
      java.util.Collections.sort(list);
   }

   public static void sort(List list, Comparator c) {
      java.util.Collections.sort(list, c);
   }

   public static int binarySearch(List list, Object key) {
      return java.util.Collections.binarySearch(list, key);
   }

   public static int binarySearch(List list, Object key, Comparator c) {
      return java.util.Collections.binarySearch(list, key, c);
   }

   public static void reverse(List list) {
      java.util.Collections.reverse(list);
   }

   public static void shuffle(List list) {
      java.util.Collections.shuffle(list);
   }

   public static void shuffle(List list, Random rnd) {
      java.util.Collections.shuffle(list, rnd);
   }

   public static void swap(List list, int i, int j) {
      java.util.Collections.swap(list, i, i);
   }

   public static void fill(List list, Object obj) {
      java.util.Collections.fill(list, obj);
   }

   public static void copy(List dest, List src) {
      java.util.Collections.copy(dest, src);
   }

   public static Object min(Collection coll) {
      return java.util.Collections.min(coll);
   }

   public static Object min(Collection coll, Comparator comp) {
      return java.util.Collections.min(coll, comp);
   }

   public static Object max(Collection coll) {
      return java.util.Collections.max(coll);
   }

   public static Object max(Collection coll, Comparator comp) {
      return java.util.Collections.max(coll, comp);
   }

   public static void rotate(List list, int distance) {
      java.util.Collections.rotate(list, distance);
   }

   public static boolean replaceAll(List list, Object oldVal, Object newVal) {
      return java.util.Collections.replaceAll(list, oldVal, newVal);
   }

   public static int indexOfSubList(List source, List target) {
      return java.util.Collections.indexOfSubList(source, target);
   }

   public static int lastIndexOfSubList(List source, List target) {
      return java.util.Collections.lastIndexOfSubList(source, target);
   }

   public static Collection unmodifiableCollection(Collection c) {
      return java.util.Collections.unmodifiableCollection(c);
   }

   public static Set unmodifiableSet(Set s) {
      return java.util.Collections.unmodifiableSet(s);
   }

   public static SortedSet unmodifiableSortedSet(SortedSet s) {
      return java.util.Collections.unmodifiableSortedSet(s);
   }

   public static List unmodifiableList(List l) {
      return java.util.Collections.unmodifiableList(l);
   }

   public static Map unmodifiableMap(Map m) {
      return java.util.Collections.unmodifiableMap(m);
   }

   public static SortedMap unmodifiableSortedMap(SortedMap m) {
      return java.util.Collections.unmodifiableSortedMap(m);
   }

   public static Collection synchronizedCollection(Collection c) {
      return java.util.Collections.synchronizedCollection(c);
   }

   public static Set synchronizedSet(Set s) {
      return java.util.Collections.synchronizedSet(s);
   }

   public static SortedSet synchronizedSortedSet(SortedSet s) {
      return java.util.Collections.synchronizedSortedSet(s);
   }

   public static List synchronizedList(List l) {
      return java.util.Collections.synchronizedList(l);
   }

   public static Map synchronizedMap(Map m) {
      return java.util.Collections.synchronizedMap(m);
   }

   public static SortedMap synchronizedSortedMap(SortedMap m) {
      return java.util.Collections.synchronizedSortedMap(m);
   }

   public static Collection checkedCollection(Collection c, Class type) {
      return new Collections.CheckedCollection(c, type);
   }

   public static Set checkedSet(Set s, Class type) {
      return new Collections.CheckedSet(s, type);
   }

   public static SortedSet checkedSortedSet(SortedSet s, Class type) {
      return new Collections.CheckedSortedSet(s, type);
   }

   public static List checkedList(List l, Class type) {
      return new Collections.CheckedList(l, type);
   }

   public static Map checkedMap(Map m, Class keyType, Class valueType) {
      return new Collections.CheckedMap(m, keyType, valueType);
   }

   public static SortedMap checkedSortedMap(SortedMap m, Class keyType, Class valueType) {
      return new Collections.CheckedSortedMap(m, keyType, valueType);
   }

   public static Set emptySet() {
      return java.util.Collections.EMPTY_SET;
   }

   public static List emptyList() {
      return java.util.Collections.EMPTY_LIST;
   }

   public static Map emptyMap() {
      return java.util.Collections.EMPTY_MAP;
   }

   public static Set singleton(Object o) {
      return java.util.Collections.singleton(o);
   }

   public static List singletonList(Object o) {
      return java.util.Collections.singletonList(o);
   }

   public static Map singletonMap(Object key, Object value) {
      return java.util.Collections.singletonMap(key, value);
   }

   public static List nCopies(int n, Object o) {
      return java.util.Collections.nCopies(n, o);
   }

   public static Comparator reverseOrder() {
      return java.util.Collections.reverseOrder();
   }

   public static Comparator reverseOrder(Comparator cmp) {
      return (Comparator)(cmp instanceof Collections.ReverseComparator ? ((Collections.ReverseComparator)cmp).cmp : (cmp == null ? reverseOrder() : new Collections.ReverseComparator(cmp)));
   }

   public static Enumeration enumeration(Collection c) {
      return java.util.Collections.enumeration(c);
   }

   public static ArrayList list(Enumeration e) {
      return java.util.Collections.list(e);
   }

   public static int frequency(Collection c, Object o) {
      int freq = 0;
      Iterator itr;
      if (o == null) {
         itr = c.iterator();

         while(itr.hasNext()) {
            if (itr.next() == null) {
               ++freq;
            }
         }
      } else {
         itr = c.iterator();

         while(itr.hasNext()) {
            if (o.equals(itr.next())) {
               ++freq;
            }
         }
      }

      return freq;
   }

   public static boolean disjoint(Collection a, Collection b) {
      if (a instanceof Set && (!(b instanceof Set) || a.size() < b.size())) {
         Collection tmp = a;
         a = b;
         b = tmp;
      }

      Iterator itr = a.iterator();

      do {
         if (!itr.hasNext()) {
            return true;
         }
      } while(!b.contains(itr.next()));

      return false;
   }

   public static boolean addAll(Collection c, Object[] a) {
      boolean modified = false;

      for(int i = 0; i < a.length; ++i) {
         modified |= c.add(a[i]);
      }

      return modified;
   }

   public static Set newSetFromMap(Map map) {
      return new Collections.SetFromMap(map);
   }

   public static Queue asLifoQueue(Deque deque) {
      return new Collections.AsLifoQueue(deque);
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   private static class ReverseComparator implements Comparator, Serializable {
      final Comparator cmp;

      ReverseComparator(Comparator cmp) {
         this.cmp = cmp;
      }

      public int compare(Object o1, Object o2) {
         return this.cmp.compare(o2, o1);
      }

      public boolean equals(Object o) {
         return o == this || o instanceof Collections.ReverseComparator && this.cmp.equals(((Collections.ReverseComparator)o).cmp);
      }

      public int hashCode() {
         return this.cmp.hashCode() ^ 268435456;
      }
   }

   private static class AsLifoQueue extends AbstractQueue implements Queue, Serializable {
      final Deque deque;

      AsLifoQueue(Deque deque) {
         this.deque = deque;
      }

      public boolean add(Object e) {
         return this.deque.offerFirst(e);
      }

      public boolean offer(Object e) {
         return this.deque.offerFirst(e);
      }

      public Object remove() {
         return this.deque.removeFirst();
      }

      public Object poll() {
         return this.deque.pollFirst();
      }

      public Object element() {
         return this.deque.getFirst();
      }

      public Object peek() {
         return this.deque.peekFirst();
      }

      public int size() {
         return this.deque.size();
      }

      public void clear() {
         this.deque.clear();
      }

      public boolean isEmpty() {
         return this.deque.isEmpty();
      }

      public Object[] toArray() {
         return this.deque.toArray();
      }

      public Object[] toArray(Object[] a) {
         return this.deque.toArray(a);
      }

      public boolean contains(Object o) {
         return this.deque.contains(o);
      }

      public boolean remove(Object o) {
         return this.deque.remove(o);
      }

      public Iterator iterator() {
         return this.deque.iterator();
      }

      public String toString() {
         return this.deque.toString();
      }

      public boolean containsAll(Collection c) {
         return this.deque.containsAll(c);
      }

      public boolean removeAll(Collection c) {
         return this.deque.removeAll(c);
      }

      public boolean retainAll(Collection c) {
         return this.deque.retainAll(c);
      }
   }

   private static class SetFromMap extends AbstractSet implements Serializable {
      private static final Object PRESENT;
      final Map map;
      transient Set keySet;

      SetFromMap(Map map) {
         this.map = map;
         this.keySet = map.keySet();
      }

      public int hashCode() {
         return this.keySet.hashCode();
      }

      public int size() {
         return this.map.size();
      }

      public void clear() {
         this.map.clear();
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public boolean add(Object o) {
         return this.map.put(o, PRESENT) == null;
      }

      public boolean contains(Object o) {
         return this.map.containsKey(o);
      }

      public boolean equals(Object o) {
         return o == this || this.keySet.equals(o);
      }

      public boolean remove(Object o) {
         return this.map.remove(o) == PRESENT;
      }

      public boolean removeAll(Collection c) {
         return this.keySet.removeAll(c);
      }

      public boolean retainAll(Collection c) {
         return this.keySet.retainAll(c);
      }

      public Iterator iterator() {
         return this.keySet.iterator();
      }

      public Object[] toArray() {
         return this.keySet.toArray();
      }

      public Object[] toArray(Object[] a) {
         return this.keySet.toArray(a);
      }

      public boolean addAll(Collection c) {
         boolean modified = false;

         for(Iterator it = c.iterator(); it.hasNext(); modified |= this.map.put(it.next(), PRESENT) == null) {
         }

         return modified;
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         this.keySet = this.map.keySet();
      }

      static {
         PRESENT = Boolean.TRUE;
      }
   }

   private static class CheckedSortedMap extends Collections.CheckedMap implements SortedMap, Serializable {
      final SortedMap map;

      CheckedSortedMap(SortedMap map, Class keyType, Class valueType) {
         super(map, keyType, valueType);
         this.map = map;
      }

      public Comparator comparator() {
         return this.map.comparator();
      }

      public Object firstKey() {
         return this.map.firstKey();
      }

      public Object lastKey() {
         return this.map.lastKey();
      }

      public SortedMap subMap(Object fromKey, Object toKey) {
         return new Collections.CheckedSortedMap(this.map.subMap(fromKey, toKey), this.keyType, this.valueType);
      }

      public SortedMap headMap(Object toKey) {
         return new Collections.CheckedSortedMap(this.map.headMap(toKey), this.keyType, this.valueType);
      }

      public SortedMap tailMap(Object fromKey) {
         return new Collections.CheckedSortedMap(this.map.tailMap(fromKey), this.keyType, this.valueType);
      }
   }

   private static class CheckedMap implements Map, Serializable {
      final Map map;
      final Class keyType;
      final Class valueType;
      transient Set entrySet;
      private transient Object[] emptyKeyArray;
      private transient Object[] emptyValueArray;

      CheckedMap(Map map, Class keyType, Class valueType) {
         if (map != null && keyType != null && valueType != null) {
            this.map = map;
            this.keyType = keyType;
            this.valueType = valueType;
         } else {
            throw new NullPointerException();
         }
      }

      private void typeCheckKey(Object key) {
         if (!this.keyType.isInstance(key)) {
            throw new ClassCastException("Attempted to use a key of type " + key.getClass().getName() + " with a map with keys of type " + this.keyType.getName());
         }
      }

      private void typeCheckValue(Object value) {
         if (!this.valueType.isInstance(value)) {
            throw new ClassCastException("Attempted to use a value of type " + value.getClass().getName() + " with a map with values of type " + this.valueType.getName());
         }
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public boolean equals(Object o) {
         return o == this || this.map.equals(o);
      }

      public int size() {
         return this.map.size();
      }

      public void clear() {
         this.map.clear();
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public boolean containsKey(Object key) {
         return this.map.containsKey(key);
      }

      public boolean containsValue(Object value) {
         return this.map.containsValue(value);
      }

      public Collection values() {
         return this.map.values();
      }

      public Set keySet() {
         return this.map.keySet();
      }

      public void putAll(Map m) {
         if (this.emptyKeyArray == null) {
            this.emptyKeyArray = (Object[])Array.newInstance(this.keyType, 0);
         }

         if (this.emptyValueArray == null) {
            this.emptyValueArray = (Object[])Array.newInstance(this.valueType, 0);
         }

         Object[] keys;
         try {
            keys = m.keySet().toArray(this.emptyKeyArray);
         } catch (ArrayStoreException var6) {
            throw new ClassCastException("Attempted to use an invalid key type  with a map with keys of type " + this.keyType.getName());
         }

         Object[] values;
         try {
            values = m.keySet().toArray(this.emptyKeyArray);
         } catch (ArrayStoreException var5) {
            throw new ClassCastException("Attempted to use an invalid value type  with a map with values of type " + this.valueType.getName());
         }

         if (keys.length != values.length) {
            throw new ConcurrentModificationException();
         } else {
            for(int i = 0; i < keys.length; ++i) {
               this.map.put(keys[i], values[i]);
            }

         }
      }

      public Set entrySet() {
         if (this.entrySet == null) {
            this.entrySet = new Collections.CheckedMap.EntrySetView(this.map.entrySet());
         }

         return this.entrySet;
      }

      public Object get(Object key) {
         return this.map.get(key);
      }

      public Object remove(Object key) {
         return this.map.remove(key);
      }

      public Object put(Object key, Object value) {
         this.typeCheckKey(key);
         this.typeCheckValue(value);
         return this.map.put(key, value);
      }

      private class EntryView implements Entry, Serializable {
         final Entry entry;

         EntryView(Entry entry) {
            this.entry = entry;
         }

         public Object getKey() {
            return this.entry.getKey();
         }

         public Object getValue() {
            return this.entry.getValue();
         }

         public int hashCode() {
            return this.entry.hashCode();
         }

         public boolean equals(Object o) {
            if (o == this) {
               return true;
            } else if (!(o instanceof Entry)) {
               return false;
            } else {
               Entry e = (Entry)o;
               return Collections.eq(this.getKey(), e.getKey()) && Collections.eq(this.getValue(), e.getValue());
            }
         }

         public Object setValue(Object val) {
            CheckedMap.this.typeCheckValue(val);
            return this.entry.setValue(val);
         }
      }

      private class EntrySetView extends AbstractSet implements Set {
         final Set entrySet;

         EntrySetView(Set entrySet) {
            this.entrySet = entrySet;
         }

         public int size() {
            return this.entrySet.size();
         }

         public boolean isEmpty() {
            return this.entrySet.isEmpty();
         }

         public boolean remove(Object o) {
            return this.entrySet.remove(o);
         }

         public void clear() {
            this.entrySet.clear();
         }

         public boolean contains(Object o) {
            return !(o instanceof Entry) ? false : this.entrySet.contains(CheckedMap.this.new EntryView((Entry)o));
         }

         public Iterator iterator() {
            final Iterator itr = this.entrySet.iterator();
            return new Iterator() {
               public boolean hasNext() {
                  return itr.hasNext();
               }

               public Object next() {
                  return CheckedMap.this.new EntryView((Entry)itr.next());
               }

               public void remove() {
                  itr.remove();
               }
            };
         }

         public Object[] toArray() {
            Object[] a = this.entrySet.toArray();
            if (a.getClass().getComponentType().isAssignableFrom(Collections.CheckedMap.EntryView.class)) {
               for(int ix = 0; ix < a.length; ++ix) {
                  a[ix] = CheckedMap.this.new EntryView((Entry)a[ix]);
               }

               return a;
            } else {
               Object[] newa = new Object[a.length];

               for(int i = 0; i < a.length; ++i) {
                  newa[i] = CheckedMap.this.new EntryView((Entry)a[i]);
               }

               return newa;
            }
         }

         public Object[] toArray(Object[] a) {
            Object[] base;
            if (a.length == 0) {
               base = a;
            } else {
               base = (Object[])Array.newInstance(a.getClass().getComponentType(), a.length);
            }

            base = this.entrySet.toArray(base);

            for(int i = 0; i < base.length; ++i) {
               base[i] = CheckedMap.this.new EntryView((Entry)base[i]);
            }

            if (base.length > a.length) {
               a = base;
            } else {
               System.arraycopy(base, 0, a, 0, base.length);
               if (base.length < a.length) {
                  a[base.length] = null;
               }
            }

            return a;
         }
      }
   }

   private static class CheckedSortedSet extends Collections.CheckedSet implements SortedSet, Serializable {
      final SortedSet set;

      CheckedSortedSet(SortedSet set, Class type) {
         super(set, type);
         this.set = set;
      }

      public Object first() {
         return this.set.first();
      }

      public Object last() {
         return this.set.last();
      }

      public Comparator comparator() {
         return this.set.comparator();
      }

      public SortedSet headSet(Object toElement) {
         return new Collections.CheckedSortedSet(this.set.headSet(toElement), this.type);
      }

      public SortedSet tailSet(Object fromElement) {
         return new Collections.CheckedSortedSet(this.set.tailSet(fromElement), this.type);
      }

      public SortedSet subSet(Object fromElement, Object toElement) {
         return new Collections.CheckedSortedSet(this.set.subSet(fromElement, toElement), this.type);
      }
   }

   private static class CheckedSet extends Collections.CheckedCollection implements Set, Serializable {
      CheckedSet(Set set, Class type) {
         super(set, type);
      }

      public int hashCode() {
         return this.coll.hashCode();
      }

      public boolean equals(Object o) {
         return o == this || this.coll.equals(o);
      }
   }

   private static class CheckedList extends Collections.CheckedCollection implements List, Serializable {
      final List list;

      CheckedList(List list, Class type) {
         super(list, type);
         this.list = list;
      }

      public Object get(int index) {
         return this.list.get(index);
      }

      public Object remove(int index) {
         return this.list.remove(index);
      }

      public int indexOf(Object o) {
         return this.list.indexOf(o);
      }

      public int lastIndexOf(Object o) {
         return this.list.lastIndexOf(o);
      }

      public int hashCode() {
         return this.list.hashCode();
      }

      public boolean equals(Object o) {
         return o == this || this.list.equals(o);
      }

      public Object set(int index, Object element) {
         this.typeCheck(element);
         return this.list.set(index, element);
      }

      public void add(int index, Object element) {
         this.typeCheck(element);
         this.list.add(index, element);
      }

      public boolean addAll(int index, Collection c) {
         Object[] checked;
         try {
            checked = c.toArray(this.getEmptyArr());
         } catch (ArrayStoreException var5) {
            throw new ClassCastException("Attempted to insert an element of invalid type  to a list of type " + this.type.getName());
         }

         return this.list.addAll(index, Arrays.asList(checked));
      }

      public List subList(int fromIndex, int toIndex) {
         return new Collections.CheckedList(this.list.subList(fromIndex, toIndex), this.type);
      }

      public ListIterator listIterator() {
         return new Collections.CheckedList.ListItr(this.list.listIterator());
      }

      public ListIterator listIterator(int index) {
         return new Collections.CheckedList.ListItr(this.list.listIterator(index));
      }

      private class ListItr implements ListIterator {
         final ListIterator itr;

         ListItr(ListIterator itr) {
            this.itr = itr;
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public boolean hasPrevious() {
            return this.itr.hasPrevious();
         }

         public int nextIndex() {
            return this.itr.nextIndex();
         }

         public int previousIndex() {
            return this.itr.previousIndex();
         }

         public Object next() {
            return this.itr.next();
         }

         public Object previous() {
            return this.itr.previous();
         }

         public void remove() {
            this.itr.remove();
         }

         public void set(Object element) {
            CheckedList.this.typeCheck(element);
            this.itr.set(element);
         }

         public void add(Object element) {
            CheckedList.this.typeCheck(element);
            this.itr.add(element);
         }
      }
   }

   private static class CheckedCollection implements Collection, Serializable {
      final Collection coll;
      final Class type;
      transient Object[] emptyArr;

      CheckedCollection(Collection coll, Class type) {
         if (coll != null && type != null) {
            this.coll = coll;
            this.type = type;
         } else {
            throw new NullPointerException();
         }
      }

      void typeCheck(Object obj) {
         if (!this.type.isInstance(obj)) {
            throw new ClassCastException("Attempted to insert an element of type " + obj.getClass().getName() + " to a collection of type " + this.type.getName());
         }
      }

      public int size() {
         return this.coll.size();
      }

      public void clear() {
         this.coll.clear();
      }

      public boolean isEmpty() {
         return this.coll.isEmpty();
      }

      public Object[] toArray() {
         return this.coll.toArray();
      }

      public Object[] toArray(Object[] a) {
         return this.coll.toArray(a);
      }

      public boolean contains(Object o) {
         return this.coll.contains(o);
      }

      public boolean remove(Object o) {
         return this.coll.remove(o);
      }

      public boolean containsAll(Collection c) {
         return this.coll.containsAll(c);
      }

      public boolean removeAll(Collection c) {
         return this.coll.removeAll(c);
      }

      public boolean retainAll(Collection c) {
         return this.coll.retainAll(c);
      }

      public String toString() {
         return this.coll.toString();
      }

      public boolean add(Object o) {
         this.typeCheck(o);
         return this.coll.add(o);
      }

      public boolean addAll(Collection c) {
         Object[] checked;
         try {
            checked = c.toArray(this.getEmptyArr());
         } catch (ArrayStoreException var4) {
            throw new ClassCastException("Attempted to insert an element of invalid type  to a collection of type " + this.type.getName());
         }

         return this.coll.addAll(Arrays.asList(checked));
      }

      public Iterator iterator() {
         return new Collections.CheckedCollection.Itr(this.coll.iterator());
      }

      protected Object[] getEmptyArr() {
         if (this.emptyArr == null) {
            this.emptyArr = (Object[])Array.newInstance(this.type, 0);
         }

         return this.emptyArr;
      }

      class Itr implements Iterator {
         final Iterator itr;

         Itr(Iterator itr) {
            this.itr = itr;
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public Object next() {
            return this.itr.next();
         }

         public void remove() {
            this.itr.remove();
         }
      }
   }
}
