package org.testng.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class MultiMap<K, V, C extends Collection<V>> {
   protected final Map<K, C> m_objects = Maps.newHashMap();

   protected abstract C createValue();

   public boolean put(K key, V method) {
      boolean setExists = true;
      C l = (Collection)this.m_objects.get(key);
      if (l == null) {
         setExists = false;
         l = this.createValue();
         this.m_objects.put(key, l);
      }

      return l.add(method) && setExists;
   }

   public C get(K key) {
      return (Collection)this.m_objects.get(key);
   }

   /** @deprecated */
   @Deprecated
   public List<K> getKeys() {
      return new ArrayList(this.keySet());
   }

   public Set<K> keySet() {
      return new HashSet(this.m_objects.keySet());
   }

   public boolean containsKey(K k) {
      return this.m_objects.containsKey(k);
   }

   public String toString() {
      StringBuilder result = new StringBuilder();
      Set<K> indices = this.keySet();
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         K i = i$.next();
         result.append("\n    ").append(i).append(" <-- ");
         Iterator i$ = ((Collection)this.m_objects.get(i)).iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            result.append(o).append(" ");
         }
      }

      return result.toString();
   }

   public boolean isEmpty() {
      return this.m_objects.size() == 0;
   }

   /** @deprecated */
   @Deprecated
   public int getSize() {
      return this.size();
   }

   public int size() {
      return this.m_objects.size();
   }

   /** @deprecated */
   @Deprecated
   public C remove(K key) {
      return this.removeAll(key);
   }

   public boolean remove(K key, V value) {
      C values = this.get(key);
      return values == null ? false : values.remove(value);
   }

   public C removeAll(K key) {
      return (Collection)this.m_objects.remove(key);
   }

   /** @deprecated */
   @Deprecated
   public Set<Entry<K, C>> getEntrySet() {
      return this.entrySet();
   }

   public Set<Entry<K, C>> entrySet() {
      return this.m_objects.entrySet();
   }

   /** @deprecated */
   @Deprecated
   public Collection<C> getValues() {
      return this.values();
   }

   public Collection<C> values() {
      return this.m_objects.values();
   }

   public boolean putAll(K k, Collection<? extends V> values) {
      boolean result = false;

      Object v;
      for(Iterator i$ = values.iterator(); i$.hasNext(); result = this.put(k, v) || result) {
         v = i$.next();
      }

      return result;
   }
}
