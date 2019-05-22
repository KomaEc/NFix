package org.codehaus.groovy.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ListHashMap<K, V> implements Map<K, V> {
   private int listFill;
   private Object[] listKeys;
   private Object[] listValues;
   private int size;
   private Map<K, V> innerMap;
   private final int maxListFill;

   public ListHashMap() {
      this(3);
   }

   public ListHashMap(int listSize) {
      this.listFill = 0;
      this.size = 0;
      this.listKeys = new Object[listSize];
      this.listValues = new Object[listSize];
      this.maxListFill = listSize;
   }

   public void clear() {
      this.listFill = 0;
      this.innerMap = null;

      for(int i = 0; i < this.maxListFill; ++i) {
         this.listValues[i] = null;
         this.listKeys[i] = null;
      }

   }

   public boolean containsKey(Object key) {
      if (this.size < this.maxListFill) {
         for(int i = 0; i < this.listFill; ++i) {
            if (this.listKeys[i].equals(key)) {
               return true;
            }
         }

         return false;
      } else {
         return this.innerMap.containsKey(key);
      }
   }

   public boolean containsValue(Object value) {
      if (this.size < this.maxListFill) {
         for(int i = 0; i < this.listFill; ++i) {
            if (this.listValues[i].equals(value)) {
               return true;
            }
         }

         return false;
      } else {
         return this.innerMap.containsValue(value);
      }
   }

   private Map<K, V> makeMap() {
      Map<K, V> m = new HashMap();

      for(int i = 0; i < this.size; ++i) {
         m.put(this.listKeys[i], this.listValues[i]);
      }

      return m;
   }

   public Set<Entry<K, V>> entrySet() {
      Map m;
      if (this.size > this.maxListFill) {
         m = this.innerMap;
      } else {
         m = this.makeMap();
      }

      return m.entrySet();
   }

   public V get(Object key) {
      if (this.size == 0) {
         return null;
      } else if (this.size < this.maxListFill) {
         for(int i = 0; i < this.maxListFill; ++i) {
            if (this.listKeys[i].equals(key)) {
               return this.listValues[i];
            }
         }

         return null;
      } else {
         return this.innerMap.get(key);
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public Set<K> keySet() {
      Map m;
      if (this.size >= this.maxListFill) {
         m = this.innerMap;
      } else {
         m = this.makeMap();
      }

      return m.keySet();
   }

   public V put(K key, V value) {
      if (this.size < this.maxListFill) {
         for(int i = 0; i < this.listFill; ++i) {
            if (this.listKeys[i].equals(key)) {
               V old = this.listValues[i];
               this.listValues[i] = value;
               return old;
            }
         }

         if (this.size < this.maxListFill - 1) {
            ++this.size;
            this.listKeys[this.listFill] = key;
            this.listValues[this.listFill] = value;
            ++this.listFill;
            return null;
         }

         this.innerMap = this.makeMap();
      }

      return this.innerMap.put(key, value);
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      if (this.size + m.size() >= this.maxListFill) {
         if (this.size < this.maxListFill) {
            this.innerMap = this.makeMap();
         }

         this.innerMap.putAll(m);
         this.size += m.size();
      } else {
         for(Iterator i$ = m.entrySet().iterator(); i$.hasNext(); ++this.listFill) {
            Entry<? extends K, ? extends V> entry = (Entry)i$.next();
            this.listKeys[this.listFill] = entry.getKey();
            this.listValues[this.listFill] = entry.getValue();
         }

         this.size += m.size();
      }
   }

   public V remove(Object key) {
      if (this.size < this.maxListFill) {
         for(int i = 0; i < this.listFill; ++i) {
            if (this.listKeys[i].equals(key)) {
               V old = this.listValues[i];
               --this.listFill;
               --this.size;
               this.listValues[i] = this.listValues[this.listFill];
               this.listKeys[i] = this.listValues[this.listFill];
               return old;
            }
         }

         return null;
      } else {
         V old = this.innerMap.remove(key);
         if (old != null) {
            --this.size;
         }

         if (this.size < this.maxListFill) {
            this.mapToList();
         }

         return old;
      }
   }

   private void mapToList() {
      int i = 0;

      for(Iterator i$ = this.innerMap.entrySet().iterator(); i$.hasNext(); ++i) {
         Entry<? extends K, ? extends V> entry = (Entry)i$.next();
         this.listKeys[i] = entry.getKey();
         this.listValues[i] = entry.getValue();
      }

      this.listFill = this.innerMap.size();
      this.innerMap = null;
   }

   public int size() {
      return this.size;
   }

   public Collection<V> values() {
      if (this.size >= this.maxListFill) {
         return this.innerMap.values();
      } else {
         ArrayList<V> list = new ArrayList(this.size);

         for(int i = 0; i < this.listFill; ++i) {
            list.add(this.listValues[i]);
         }

         return list;
      }
   }
}
