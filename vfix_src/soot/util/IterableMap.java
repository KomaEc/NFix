package soot.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class IterableMap implements Map {
   private HashMap<Object, Object> content_map;
   private HashMap<Object, Object> back_map;
   private HashChain key_chain;
   private HashChain value_chain;
   private transient Set<Object> keySet;
   private transient Set<Object> valueSet;
   private transient Collection<Object> values;

   public IterableMap() {
      this(7, 0.7F);
   }

   public IterableMap(int initialCapacity) {
      this(initialCapacity, 0.7F);
   }

   public IterableMap(int initialCapacity, float loadFactor) {
      this.keySet = null;
      this.valueSet = null;
      this.values = null;
      this.content_map = new HashMap(initialCapacity, loadFactor);
      this.back_map = new HashMap(initialCapacity, loadFactor);
      this.key_chain = new HashChain();
      this.value_chain = new HashChain();
   }

   public void clear() {
      Iterator kcit = this.key_chain.iterator();

      while(kcit.hasNext()) {
         this.content_map.remove(kcit.next());
      }

      Iterator vcit = this.value_chain.iterator();

      while(vcit.hasNext()) {
         this.back_map.remove(vcit.next());
      }

      this.key_chain.clear();
      this.value_chain.clear();
   }

   public Iterator iterator() {
      return this.key_chain.iterator();
   }

   public boolean containsKey(Object key) {
      return this.key_chain.contains(key);
   }

   public boolean containsValue(Object value) {
      return this.value_chain.contains(value);
   }

   public Set entrySet() {
      return this.content_map.entrySet();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof IterableMap)) {
         return false;
      } else {
         IterableMap other = (IterableMap)o;
         if (!this.key_chain.equals(other.key_chain)) {
            return false;
         } else {
            Iterator kcit = this.key_chain.iterator();

            Object ko;
            do {
               if (!kcit.hasNext()) {
                  return true;
               }

               ko = kcit.next();
            } while(other.content_map.get(ko) == this.content_map.get(ko));

            return false;
         }
      }
   }

   public Object get(Object key) {
      return this.content_map.get(key);
   }

   public int hashCode() {
      return this.content_map.hashCode();
   }

   public boolean isEmpty() {
      return this.key_chain.isEmpty();
   }

   public Set<Object> keySet() {
      if (this.keySet == null) {
         this.keySet = new AbstractSet() {
            public Iterator iterator() {
               return IterableMap.this.key_chain.iterator();
            }

            public int size() {
               return IterableMap.this.key_chain.size();
            }

            public boolean contains(Object o) {
               return IterableMap.this.key_chain.contains(o);
            }

            public boolean remove(Object o) {
               if (!IterableMap.this.key_chain.contains(o)) {
                  return false;
               } else if (IterableMap.this.content_map.get(o) == null) {
                  IterableMap.this.remove(o);
                  return true;
               } else {
                  return IterableMap.this.remove(o) != null;
               }
            }

            public void clear() {
               IterableMap.this.clear();
            }
         };
      }

      return this.keySet;
   }

   public Set<Object> valueSet() {
      if (this.valueSet == null) {
         this.valueSet = new AbstractSet() {
            public Iterator iterator() {
               return IterableMap.this.value_chain.iterator();
            }

            public int size() {
               return IterableMap.this.value_chain.size();
            }

            public boolean contains(Object o) {
               return IterableMap.this.value_chain.contains(o);
            }

            public boolean remove(Object o) {
               if (!IterableMap.this.value_chain.contains(o)) {
                  return false;
               } else {
                  HashChain c = (HashChain)IterableMap.this.back_map.get(o);
                  Iterator it = c.snapshotIterator();

                  while(it.hasNext()) {
                     Object ko = it.next();
                     if (IterableMap.this.content_map.get(o) == null) {
                        IterableMap.this.remove(ko);
                     } else if (IterableMap.this.remove(ko) == null) {
                        return false;
                     }
                  }

                  return true;
               }
            }

            public void clear() {
               IterableMap.this.clear();
            }
         };
      }

      return this.valueSet;
   }

   public Object put(Object key, Object value) {
      if (this.key_chain.contains(key)) {
         Object old_value = this.content_map.get(key);
         if (old_value == value) {
            return value;
         } else {
            HashChain kc = (HashChain)this.back_map.get(old_value);
            kc.remove(key);
            if (kc.isEmpty()) {
               this.value_chain.remove(old_value);
               this.back_map.remove(old_value);
            }

            kc = (HashChain)this.back_map.get(value);
            if (kc == null) {
               kc = new HashChain();
               this.back_map.put(value, kc);
               this.value_chain.add(value);
            }

            kc.add(key);
            return old_value;
         }
      } else {
         this.key_chain.add(key);
         this.content_map.put(key, value);
         HashChain kc = (HashChain)this.back_map.get(value);
         if (kc == null) {
            kc = new HashChain();
            this.back_map.put(value, kc);
            this.value_chain.add(value);
         }

         kc.add(key);
         return null;
      }
   }

   public void putAll(Map t) {
      Iterator kit = t instanceof IterableMap ? ((IterableMap)t).key_chain.iterator() : t.keySet().iterator();

      while(kit.hasNext()) {
         Object key = kit.next();
         this.put(key, t.get(key));
      }

   }

   public Object remove(Object key) {
      if (!this.key_chain.contains(key)) {
         return null;
      } else {
         this.key_chain.remove(key);
         Object value = this.content_map.remove(key);
         HashChain c = (HashChain)this.back_map.get(value);
         c.remove(key);
         if (c.size() == 0) {
            this.back_map.remove(value);
         }

         return value;
      }
   }

   public int size() {
      return this.key_chain.size();
   }

   public Collection<Object> values() {
      if (this.values == null) {
         this.values = new AbstractCollection() {
            public Iterator iterator() {
               return IterableMap.this.new Mapping_Iterator(IterableMap.this.key_chain, IterableMap.this.content_map);
            }

            public int size() {
               return IterableMap.this.key_chain.size();
            }

            public boolean contains(Object o) {
               return IterableMap.this.value_chain.contains(o);
            }

            public void clear() {
               IterableMap.this.clear();
            }
         };
      }

      return this.values;
   }

   public class Mapping_Iterator implements Iterator {
      private final Iterator it;
      private HashMap<Object, Object> m;

      public Mapping_Iterator(HashChain c, HashMap<Object, Object> m) {
         this.it = c.iterator();
         this.m = m;
      }

      public boolean hasNext() {
         return this.it.hasNext();
      }

      public Object next() throws NoSuchElementException {
         return this.m.get(this.it.next());
      }

      public void remove() throws UnsupportedOperationException {
         throw new UnsupportedOperationException("You cannot remove from an Iterator on the values() for an IterableMap.");
      }
   }
}
