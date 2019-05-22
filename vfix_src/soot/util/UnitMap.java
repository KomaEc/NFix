package soot.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public abstract class UnitMap<T> implements Map<Unit, T> {
   private Map<Unit, T> unitToResult;

   public UnitMap(Body b) {
      this.unitToResult = new HashMap();
      this.map(b);
   }

   public UnitMap(UnitGraph g) {
      this(g.getBody());
   }

   public UnitMap(Body b, int initialCapacity) {
      this.unitToResult = new HashMap(initialCapacity);
      this.map(b);
   }

   public UnitMap(UnitGraph g, int initialCapacity) {
      this(g.getBody(), initialCapacity);
   }

   public UnitMap(Body b, int initialCapacity, float loadFactor) {
      this.unitToResult = new HashMap(initialCapacity);
      this.init();
      this.map(b);
   }

   public UnitMap(UnitGraph g, int initialCapacity, float loadFactor) {
      this(g.getBody(), initialCapacity);
   }

   private void map(Body b) {
      Iterator unitIt = b.getUnits().iterator();

      while(unitIt.hasNext()) {
         Unit currentUnit = (Unit)unitIt.next();
         T o = this.mapTo(currentUnit);
         if (o != null) {
            this.unitToResult.put(currentUnit, o);
         }
      }

   }

   protected void init() {
   }

   protected abstract T mapTo(Unit var1);

   public void clear() {
      this.unitToResult.clear();
   }

   public boolean containsKey(Object key) {
      return this.unitToResult.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.unitToResult.containsValue(value);
   }

   public Set<Entry<Unit, T>> entrySet() {
      return this.unitToResult.entrySet();
   }

   public boolean equals(Object o) {
      return this.unitToResult.equals(o);
   }

   public T get(Object key) {
      return this.unitToResult.get(key);
   }

   public int hashCode() {
      return this.unitToResult.hashCode();
   }

   public boolean isEmpty() {
      return this.unitToResult.isEmpty();
   }

   public Set<Unit> keySet() {
      return this.unitToResult.keySet();
   }

   public T put(Unit key, T value) {
      return this.unitToResult.put(key, value);
   }

   public void putAll(Map<? extends Unit, ? extends T> t) {
      this.unitToResult.putAll(t);
   }

   public T remove(Object key) {
      return this.unitToResult.remove(key);
   }

   public int size() {
      return this.unitToResult.size();
   }

   public Collection<T> values() {
      return this.unitToResult.values();
   }
}
