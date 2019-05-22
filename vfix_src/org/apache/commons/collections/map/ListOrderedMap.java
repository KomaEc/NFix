package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections.keyvalue.AbstractMapEntry;
import org.apache.commons.collections.list.UnmodifiableList;

public class ListOrderedMap extends AbstractMapDecorator implements OrderedMap, Serializable {
   private static final long serialVersionUID = 2728177751851003750L;
   protected final List insertOrder;

   public static OrderedMap decorate(Map map) {
      return new ListOrderedMap(map);
   }

   public ListOrderedMap() {
      this(new HashMap());
   }

   protected ListOrderedMap(Map map) {
      super(map);
      this.insertOrder = new ArrayList();
      this.insertOrder.addAll(this.getMap().keySet());
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.map);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.map = (Map)in.readObject();
   }

   public MapIterator mapIterator() {
      return this.orderedMapIterator();
   }

   public OrderedMapIterator orderedMapIterator() {
      return new ListOrderedMap.ListOrderedMapIterator(this);
   }

   public Object firstKey() {
      if (this.size() == 0) {
         throw new NoSuchElementException("Map is empty");
      } else {
         return this.insertOrder.get(0);
      }
   }

   public Object lastKey() {
      if (this.size() == 0) {
         throw new NoSuchElementException("Map is empty");
      } else {
         return this.insertOrder.get(this.size() - 1);
      }
   }

   public Object nextKey(Object key) {
      int index = this.insertOrder.indexOf(key);
      return index >= 0 && index < this.size() - 1 ? this.insertOrder.get(index + 1) : null;
   }

   public Object previousKey(Object key) {
      int index = this.insertOrder.indexOf(key);
      return index > 0 ? this.insertOrder.get(index - 1) : null;
   }

   public Object put(Object key, Object value) {
      if (this.getMap().containsKey(key)) {
         return this.getMap().put(key, value);
      } else {
         Object result = this.getMap().put(key, value);
         this.insertOrder.add(key);
         return result;
      }
   }

   public void putAll(Map map) {
      Iterator it = map.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         this.put(entry.getKey(), entry.getValue());
      }

   }

   public Object remove(Object key) {
      Object result = this.getMap().remove(key);
      this.insertOrder.remove(key);
      return result;
   }

   public void clear() {
      this.getMap().clear();
      this.insertOrder.clear();
   }

   public Set keySet() {
      return new ListOrderedMap.KeySetView(this);
   }

   public List keyList() {
      return UnmodifiableList.decorate(this.insertOrder);
   }

   public Collection values() {
      return new ListOrderedMap.ValuesView(this);
   }

   public List valueList() {
      return new ListOrderedMap.ValuesView(this);
   }

   public Set entrySet() {
      return new ListOrderedMap.EntrySetView(this, this.insertOrder);
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuffer buf = new StringBuffer();
         buf.append('{');
         boolean first = true;
         Iterator it = this.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (first) {
               first = false;
            } else {
               buf.append(", ");
            }

            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
         }

         buf.append('}');
         return buf.toString();
      }
   }

   public Object get(int index) {
      return this.insertOrder.get(index);
   }

   public Object getValue(int index) {
      return this.get(this.insertOrder.get(index));
   }

   public int indexOf(Object key) {
      return this.insertOrder.indexOf(key);
   }

   public Object setValue(int index, Object value) {
      Object key = this.insertOrder.get(index);
      return this.put(key, value);
   }

   public Object put(int index, Object key, Object value) {
      Map m = this.getMap();
      if (m.containsKey(key)) {
         Object result = m.remove(key);
         int pos = this.insertOrder.indexOf(key);
         this.insertOrder.remove(pos);
         if (pos < index) {
            --index;
         }

         this.insertOrder.add(index, key);
         m.put(key, value);
         return result;
      } else {
         this.insertOrder.add(index, key);
         m.put(key, value);
         return null;
      }
   }

   public Object remove(int index) {
      return this.remove(this.get(index));
   }

   public List asList() {
      return this.keyList();
   }

   static class ListOrderedMapIterator implements OrderedMapIterator, ResettableIterator {
      private final ListOrderedMap parent;
      private ListIterator iterator;
      private Object last = null;
      private boolean readable = false;

      ListOrderedMapIterator(ListOrderedMap parent) {
         this.parent = parent;
         this.iterator = parent.insertOrder.listIterator();
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      public Object next() {
         this.last = this.iterator.next();
         this.readable = true;
         return this.last;
      }

      public boolean hasPrevious() {
         return this.iterator.hasPrevious();
      }

      public Object previous() {
         this.last = this.iterator.previous();
         this.readable = true;
         return this.last;
      }

      public void remove() {
         if (!this.readable) {
            throw new IllegalStateException("remove() can only be called once after next()");
         } else {
            this.iterator.remove();
            this.parent.map.remove(this.last);
            this.readable = false;
         }
      }

      public Object getKey() {
         if (!this.readable) {
            throw new IllegalStateException("getKey() can only be called after next() and before remove()");
         } else {
            return this.last;
         }
      }

      public Object getValue() {
         if (!this.readable) {
            throw new IllegalStateException("getValue() can only be called after next() and before remove()");
         } else {
            return this.parent.get(this.last);
         }
      }

      public Object setValue(Object value) {
         if (!this.readable) {
            throw new IllegalStateException("setValue() can only be called after next() and before remove()");
         } else {
            return this.parent.map.put(this.last, value);
         }
      }

      public void reset() {
         this.iterator = this.parent.insertOrder.listIterator();
         this.last = null;
         this.readable = false;
      }

      public String toString() {
         return this.readable ? "Iterator[" + this.getKey() + "=" + this.getValue() + "]" : "Iterator[]";
      }
   }

   static class ListOrderedMapEntry extends AbstractMapEntry {
      private final ListOrderedMap parent;

      ListOrderedMapEntry(ListOrderedMap parent, Object key) {
         super(key, (Object)null);
         this.parent = parent;
      }

      public Object getValue() {
         return this.parent.get(super.key);
      }

      public Object setValue(Object value) {
         return this.parent.getMap().put(super.key, value);
      }
   }

   static class ListOrderedIterator extends AbstractIteratorDecorator {
      private final ListOrderedMap parent;
      private Object last = null;

      ListOrderedIterator(ListOrderedMap parent, List insertOrder) {
         super(insertOrder.iterator());
         this.parent = parent;
      }

      public Object next() {
         this.last = super.next();
         return new ListOrderedMap.ListOrderedMapEntry(this.parent, this.last);
      }

      public void remove() {
         super.remove();
         this.parent.getMap().remove(this.last);
      }
   }

   static class EntrySetView extends AbstractSet {
      private final ListOrderedMap parent;
      private final List insertOrder;
      private Set entrySet;

      public EntrySetView(ListOrderedMap parent, List insertOrder) {
         this.parent = parent;
         this.insertOrder = insertOrder;
      }

      private Set getEntrySet() {
         if (this.entrySet == null) {
            this.entrySet = this.parent.getMap().entrySet();
         }

         return this.entrySet;
      }

      public int size() {
         return this.parent.size();
      }

      public boolean isEmpty() {
         return this.parent.isEmpty();
      }

      public boolean contains(Object obj) {
         return this.getEntrySet().contains(obj);
      }

      public boolean containsAll(Collection coll) {
         return this.getEntrySet().containsAll(coll);
      }

      public boolean remove(Object obj) {
         if (!(obj instanceof Entry)) {
            return false;
         } else if (this.getEntrySet().contains(obj)) {
            Object key = ((Entry)obj).getKey();
            this.parent.remove(key);
            return true;
         } else {
            return false;
         }
      }

      public void clear() {
         this.parent.clear();
      }

      public boolean equals(Object obj) {
         return obj == this ? true : this.getEntrySet().equals(obj);
      }

      public int hashCode() {
         return this.getEntrySet().hashCode();
      }

      public String toString() {
         return this.getEntrySet().toString();
      }

      public Iterator iterator() {
         return new ListOrderedMap.ListOrderedIterator(this.parent, this.insertOrder);
      }
   }

   static class KeySetView extends AbstractSet {
      private final ListOrderedMap parent;

      KeySetView(ListOrderedMap parent) {
         this.parent = parent;
      }

      public int size() {
         return this.parent.size();
      }

      public boolean contains(Object value) {
         return this.parent.containsKey(value);
      }

      public void clear() {
         this.parent.clear();
      }

      public Iterator iterator() {
         return new AbstractIteratorDecorator(this.parent.entrySet().iterator()) {
            public Object next() {
               return ((Entry)super.next()).getKey();
            }
         };
      }
   }

   static class ValuesView extends AbstractList {
      private final ListOrderedMap parent;

      ValuesView(ListOrderedMap parent) {
         this.parent = parent;
      }

      public int size() {
         return this.parent.size();
      }

      public boolean contains(Object value) {
         return this.parent.containsValue(value);
      }

      public void clear() {
         this.parent.clear();
      }

      public Iterator iterator() {
         return new AbstractIteratorDecorator(this.parent.entrySet().iterator()) {
            public Object next() {
               return ((Entry)super.iterator.next()).getValue();
            }
         };
      }

      public Object get(int index) {
         return this.parent.getValue(index);
      }

      public Object set(int index, Object value) {
         return this.parent.setValue(index, value);
      }

      public Object remove(int index) {
         return this.parent.remove(index);
      }
   }
}
