package org.apache.commons.collections.bidimap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.collection.AbstractCollectionDecorator;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections.keyvalue.AbstractMapEntryDecorator;

public abstract class AbstractDualBidiMap implements BidiMap {
   protected final transient Map[] maps = new Map[2];
   protected transient BidiMap inverseBidiMap = null;
   protected transient Set keySet = null;
   protected transient Collection values = null;
   protected transient Set entrySet = null;

   protected AbstractDualBidiMap() {
      this.maps[0] = this.createMap();
      this.maps[1] = this.createMap();
   }

   protected AbstractDualBidiMap(Map normalMap, Map reverseMap) {
      this.maps[0] = normalMap;
      this.maps[1] = reverseMap;
   }

   protected AbstractDualBidiMap(Map normalMap, Map reverseMap, BidiMap inverseBidiMap) {
      this.maps[0] = normalMap;
      this.maps[1] = reverseMap;
      this.inverseBidiMap = inverseBidiMap;
   }

   /** @deprecated */
   protected Map createMap() {
      return null;
   }

   protected abstract BidiMap createBidiMap(Map var1, Map var2, BidiMap var3);

   public Object get(Object key) {
      return this.maps[0].get(key);
   }

   public int size() {
      return this.maps[0].size();
   }

   public boolean isEmpty() {
      return this.maps[0].isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.maps[0].containsKey(key);
   }

   public boolean equals(Object obj) {
      return this.maps[0].equals(obj);
   }

   public int hashCode() {
      return this.maps[0].hashCode();
   }

   public String toString() {
      return this.maps[0].toString();
   }

   public Object put(Object key, Object value) {
      if (this.maps[0].containsKey(key)) {
         this.maps[1].remove(this.maps[0].get(key));
      }

      if (this.maps[1].containsKey(value)) {
         this.maps[0].remove(this.maps[1].get(value));
      }

      Object obj = this.maps[0].put(key, value);
      this.maps[1].put(value, key);
      return obj;
   }

   public void putAll(Map map) {
      Iterator it = map.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         this.put(entry.getKey(), entry.getValue());
      }

   }

   public Object remove(Object key) {
      Object value = null;
      if (this.maps[0].containsKey(key)) {
         value = this.maps[0].remove(key);
         this.maps[1].remove(value);
      }

      return value;
   }

   public void clear() {
      this.maps[0].clear();
      this.maps[1].clear();
   }

   public boolean containsValue(Object value) {
      return this.maps[1].containsKey(value);
   }

   public MapIterator mapIterator() {
      return new AbstractDualBidiMap.BidiMapIterator(this);
   }

   public Object getKey(Object value) {
      return this.maps[1].get(value);
   }

   public Object removeValue(Object value) {
      Object key = null;
      if (this.maps[1].containsKey(value)) {
         key = this.maps[1].remove(value);
         this.maps[0].remove(key);
      }

      return key;
   }

   public BidiMap inverseBidiMap() {
      if (this.inverseBidiMap == null) {
         this.inverseBidiMap = this.createBidiMap(this.maps[1], this.maps[0], this);
      }

      return this.inverseBidiMap;
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new AbstractDualBidiMap.KeySet(this);
      }

      return this.keySet;
   }

   protected Iterator createKeySetIterator(Iterator iterator) {
      return new AbstractDualBidiMap.KeySetIterator(iterator, this);
   }

   public Collection values() {
      if (this.values == null) {
         this.values = new AbstractDualBidiMap.Values(this);
      }

      return this.values;
   }

   protected Iterator createValuesIterator(Iterator iterator) {
      return new AbstractDualBidiMap.ValuesIterator(iterator, this);
   }

   public Set entrySet() {
      if (this.entrySet == null) {
         this.entrySet = new AbstractDualBidiMap.EntrySet(this);
      }

      return this.entrySet;
   }

   protected Iterator createEntrySetIterator(Iterator iterator) {
      return new AbstractDualBidiMap.EntrySetIterator(iterator, this);
   }

   protected static class BidiMapIterator implements MapIterator, ResettableIterator {
      protected final AbstractDualBidiMap parent;
      protected Iterator iterator;
      protected Entry last = null;
      protected boolean canRemove = false;

      protected BidiMapIterator(AbstractDualBidiMap parent) {
         this.parent = parent;
         this.iterator = parent.maps[0].entrySet().iterator();
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      public Object next() {
         this.last = (Entry)this.iterator.next();
         this.canRemove = true;
         return this.last.getKey();
      }

      public void remove() {
         if (!this.canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
         } else {
            Object value = this.last.getValue();
            this.iterator.remove();
            this.parent.maps[1].remove(value);
            this.last = null;
            this.canRemove = false;
         }
      }

      public Object getKey() {
         if (this.last == null) {
            throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
         } else {
            return this.last.getKey();
         }
      }

      public Object getValue() {
         if (this.last == null) {
            throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
         } else {
            return this.last.getValue();
         }
      }

      public Object setValue(Object value) {
         if (this.last == null) {
            throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
         } else if (this.parent.maps[1].containsKey(value) && this.parent.maps[1].get(value) != this.last.getKey()) {
            throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
         } else {
            return this.parent.put(this.last.getKey(), value);
         }
      }

      public void reset() {
         this.iterator = this.parent.maps[0].entrySet().iterator();
         this.last = null;
         this.canRemove = false;
      }

      public String toString() {
         return this.last != null ? "MapIterator[" + this.getKey() + "=" + this.getValue() + "]" : "MapIterator[]";
      }
   }

   protected static class MapEntry extends AbstractMapEntryDecorator {
      protected final AbstractDualBidiMap parent;

      protected MapEntry(Entry entry, AbstractDualBidiMap parent) {
         super(entry);
         this.parent = parent;
      }

      public Object setValue(Object value) {
         Object key = this.getKey();
         if (this.parent.maps[1].containsKey(value) && this.parent.maps[1].get(value) != key) {
            throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
         } else {
            this.parent.put(key, value);
            Object oldValue = super.setValue(value);
            return oldValue;
         }
      }
   }

   protected static class EntrySetIterator extends AbstractIteratorDecorator {
      protected final AbstractDualBidiMap parent;
      protected Entry last = null;
      protected boolean canRemove = false;

      protected EntrySetIterator(Iterator iterator, AbstractDualBidiMap parent) {
         super(iterator);
         this.parent = parent;
      }

      public Object next() {
         this.last = new AbstractDualBidiMap.MapEntry((Entry)super.next(), this.parent);
         this.canRemove = true;
         return this.last;
      }

      public void remove() {
         if (!this.canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
         } else {
            Object value = this.last.getValue();
            super.remove();
            this.parent.maps[1].remove(value);
            this.last = null;
            this.canRemove = false;
         }
      }
   }

   protected static class EntrySet extends AbstractDualBidiMap.View implements Set {
      protected EntrySet(AbstractDualBidiMap parent) {
         super(parent.maps[0].entrySet(), parent);
      }

      public Iterator iterator() {
         return super.parent.createEntrySetIterator(super.iterator());
      }

      public boolean remove(Object obj) {
         if (!(obj instanceof Entry)) {
            return false;
         } else {
            Entry entry = (Entry)obj;
            Object key = entry.getKey();
            if (super.parent.containsKey(key)) {
               Object value = super.parent.maps[0].get(key);
               if (value == null ? entry.getValue() == null : value.equals(entry.getValue())) {
                  super.parent.maps[0].remove(key);
                  super.parent.maps[1].remove(value);
                  return true;
               }
            }

            return false;
         }
      }
   }

   protected static class ValuesIterator extends AbstractIteratorDecorator {
      protected final AbstractDualBidiMap parent;
      protected Object lastValue = null;
      protected boolean canRemove = false;

      protected ValuesIterator(Iterator iterator, AbstractDualBidiMap parent) {
         super(iterator);
         this.parent = parent;
      }

      public Object next() {
         this.lastValue = super.next();
         this.canRemove = true;
         return this.lastValue;
      }

      public void remove() {
         if (!this.canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
         } else {
            super.remove();
            this.parent.maps[1].remove(this.lastValue);
            this.lastValue = null;
            this.canRemove = false;
         }
      }
   }

   protected static class Values extends AbstractDualBidiMap.View implements Set {
      protected Values(AbstractDualBidiMap parent) {
         super(parent.maps[0].values(), parent);
      }

      public Iterator iterator() {
         return super.parent.createValuesIterator(super.iterator());
      }

      public boolean contains(Object value) {
         return super.parent.maps[1].containsKey(value);
      }

      public boolean remove(Object value) {
         if (super.parent.maps[1].containsKey(value)) {
            Object key = super.parent.maps[1].remove(value);
            super.parent.maps[0].remove(key);
            return true;
         } else {
            return false;
         }
      }
   }

   protected static class KeySetIterator extends AbstractIteratorDecorator {
      protected final AbstractDualBidiMap parent;
      protected Object lastKey = null;
      protected boolean canRemove = false;

      protected KeySetIterator(Iterator iterator, AbstractDualBidiMap parent) {
         super(iterator);
         this.parent = parent;
      }

      public Object next() {
         this.lastKey = super.next();
         this.canRemove = true;
         return this.lastKey;
      }

      public void remove() {
         if (!this.canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
         } else {
            Object value = this.parent.maps[0].get(this.lastKey);
            super.remove();
            this.parent.maps[1].remove(value);
            this.lastKey = null;
            this.canRemove = false;
         }
      }
   }

   protected static class KeySet extends AbstractDualBidiMap.View implements Set {
      protected KeySet(AbstractDualBidiMap parent) {
         super(parent.maps[0].keySet(), parent);
      }

      public Iterator iterator() {
         return super.parent.createKeySetIterator(super.iterator());
      }

      public boolean contains(Object key) {
         return super.parent.maps[0].containsKey(key);
      }

      public boolean remove(Object key) {
         if (super.parent.maps[0].containsKey(key)) {
            Object value = super.parent.maps[0].remove(key);
            super.parent.maps[1].remove(value);
            return true;
         } else {
            return false;
         }
      }
   }

   protected abstract static class View extends AbstractCollectionDecorator {
      protected final AbstractDualBidiMap parent;

      protected View(Collection coll, AbstractDualBidiMap parent) {
         super(coll);
         this.parent = parent;
      }

      public boolean removeAll(Collection coll) {
         if (!this.parent.isEmpty() && !coll.isEmpty()) {
            boolean modified = false;
            Iterator it = this.iterator();

            while(it.hasNext()) {
               if (coll.contains(it.next())) {
                  it.remove();
                  modified = true;
               }
            }

            return modified;
         } else {
            return false;
         }
      }

      public boolean retainAll(Collection coll) {
         if (this.parent.isEmpty()) {
            return false;
         } else if (coll.isEmpty()) {
            this.parent.clear();
            return true;
         } else {
            boolean modified = false;
            Iterator it = this.iterator();

            while(it.hasNext()) {
               if (!coll.contains(it.next())) {
                  it.remove();
                  modified = true;
               }
            }

            return modified;
         }
      }

      public void clear() {
         this.parent.clear();
      }
   }
}
