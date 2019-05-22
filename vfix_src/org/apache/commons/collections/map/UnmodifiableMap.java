package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.collections.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections.set.UnmodifiableSet;

public final class UnmodifiableMap extends AbstractMapDecorator implements IterableMap, Unmodifiable, Serializable {
   private static final long serialVersionUID = 2737023427269031941L;

   public static Map decorate(Map map) {
      return (Map)(map instanceof Unmodifiable ? map : new UnmodifiableMap(map));
   }

   private UnmodifiableMap(Map map) {
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

   public MapIterator mapIterator() {
      if (super.map instanceof IterableMap) {
         MapIterator it = ((IterableMap)super.map).mapIterator();
         return UnmodifiableMapIterator.decorate(it);
      } else {
         MapIterator it = new EntrySetMapIterator(super.map);
         return UnmodifiableMapIterator.decorate(it);
      }
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
