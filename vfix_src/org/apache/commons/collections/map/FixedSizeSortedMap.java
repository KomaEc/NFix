package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections.BoundedMap;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.collections.set.UnmodifiableSet;

public class FixedSizeSortedMap extends AbstractSortedMapDecorator implements SortedMap, BoundedMap, Serializable {
   private static final long serialVersionUID = 3126019624511683653L;

   public static SortedMap decorate(SortedMap map) {
      return new FixedSizeSortedMap(map);
   }

   protected FixedSizeSortedMap(SortedMap map) {
      super(map);
   }

   protected SortedMap getSortedMap() {
      return (SortedMap)super.map;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.map);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.map = (Map)in.readObject();
   }

   public Object put(Object key, Object value) {
      if (!super.map.containsKey(key)) {
         throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
      } else {
         return super.map.put(key, value);
      }
   }

   public void putAll(Map mapToCopy) {
      Iterator it = mapToCopy.keySet().iterator();

      while(it.hasNext()) {
         if (!mapToCopy.containsKey(it.next())) {
            throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
         }
      }

      super.map.putAll(mapToCopy);
   }

   public void clear() {
      throw new UnsupportedOperationException("Map is fixed size");
   }

   public Object remove(Object key) {
      throw new UnsupportedOperationException("Map is fixed size");
   }

   public Set entrySet() {
      Set set = super.map.entrySet();
      return UnmodifiableSet.decorate(set);
   }

   public Set keySet() {
      Set set = super.map.keySet();
      return UnmodifiableSet.decorate(set);
   }

   public Collection values() {
      Collection coll = super.map.values();
      return UnmodifiableCollection.decorate(coll);
   }

   public SortedMap subMap(Object fromKey, Object toKey) {
      SortedMap map = this.getSortedMap().subMap(fromKey, toKey);
      return new FixedSizeSortedMap(map);
   }

   public SortedMap headMap(Object toKey) {
      SortedMap map = this.getSortedMap().headMap(toKey);
      return new FixedSizeSortedMap(map);
   }

   public SortedMap tailMap(Object fromKey) {
      SortedMap map = this.getSortedMap().tailMap(fromKey);
      return new FixedSizeSortedMap(map);
   }

   public boolean isFull() {
      return true;
   }

   public int maxSize() {
      return this.size();
   }
}
