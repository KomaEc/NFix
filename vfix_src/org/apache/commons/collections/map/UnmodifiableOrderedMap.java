package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.collections.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections.iterators.UnmodifiableOrderedMapIterator;
import org.apache.commons.collections.set.UnmodifiableSet;

public final class UnmodifiableOrderedMap extends AbstractOrderedMapDecorator implements Unmodifiable, Serializable {
   private static final long serialVersionUID = 8136428161720526266L;

   public static OrderedMap decorate(OrderedMap map) {
      return (OrderedMap)(map instanceof Unmodifiable ? map : new UnmodifiableOrderedMap(map));
   }

   private UnmodifiableOrderedMap(OrderedMap map) {
      super(map);
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
      MapIterator it = this.getOrderedMap().mapIterator();
      return UnmodifiableMapIterator.decorate(it);
   }

   public OrderedMapIterator orderedMapIterator() {
      OrderedMapIterator it = this.getOrderedMap().orderedMapIterator();
      return UnmodifiableOrderedMapIterator.decorate(it);
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public Object put(Object key, Object value) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map mapToCopy) {
      throw new UnsupportedOperationException();
   }

   public Object remove(Object key) {
      throw new UnsupportedOperationException();
   }

   public Set entrySet() {
      Set set = super.entrySet();
      return UnmodifiableEntrySet.decorate(set);
   }

   public Set keySet() {
      Set set = super.keySet();
      return UnmodifiableSet.decorate(set);
   }

   public Collection values() {
      Collection coll = super.values();
      return UnmodifiableCollection.decorate(coll);
   }
}
