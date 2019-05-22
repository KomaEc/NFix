package org.jboss.util.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

public class MapDelegateSet<E> extends AbstractSet<E> implements Serializable {
   private static final long serialVersionUID = 1L;
   private final Map<E, Object> map;
   private static final Object PRESENT = new Object();

   public MapDelegateSet(Map<E, Object> map) {
      if (map == null) {
         throw new IllegalArgumentException("Null map");
      } else {
         this.map = map;
      }
   }

   public Iterator<E> iterator() {
      return this.map.keySet().iterator();
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

   public boolean add(E o) {
      return this.map.put(o, PRESENT) == null;
   }

   public boolean remove(Object o) {
      return this.map.remove(o) == PRESENT;
   }

   public void clear() {
      this.map.clear();
   }

   public String toString() {
      return this.map.keySet().toString();
   }
}
