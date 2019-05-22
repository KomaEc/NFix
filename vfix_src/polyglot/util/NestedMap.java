package polyglot.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class NestedMap extends AbstractMap implements Map {
   private HashMap myMap;
   private int nShadowed;
   private Set setView;
   private Map superMap;
   private Predicate entryKeyNotInMyMap = new Predicate() {
      public boolean isTrue(Object o) {
         Entry ent = (Entry)o;
         return !NestedMap.this.myMap.containsKey(ent.getKey());
      }
   };
   private Predicate keyNotInMyMap = new Predicate() {
      public boolean isTrue(Object o) {
         return !NestedMap.this.myMap.containsKey(o);
      }
   };

   public NestedMap(Map containing) {
      this.superMap = (Map)(containing == null ? NilMap.EMPTY_MAP : containing);
      this.myMap = new HashMap();
      this.setView = new NestedMap.EntrySet();
      this.nShadowed = 0;
   }

   public Map getContainingMap() {
      return this.superMap instanceof NilMap ? null : this.superMap;
   }

   public void release(Object key) {
      this.myMap.remove(key);
   }

   public Map getInnerMap() {
      return this.myMap;
   }

   public Set entrySet() {
      return this.setView;
   }

   public int size() {
      return this.superMap.size() + this.myMap.size() - this.nShadowed;
   }

   public boolean containsKey(Object key) {
      return this.myMap.containsKey(key) || this.superMap.containsKey(key);
   }

   public Object get(Object key) {
      return this.myMap.containsKey(key) ? this.myMap.get(key) : this.superMap.get(key);
   }

   public Object put(Object key, Object value) {
      if (this.myMap.containsKey(key)) {
         return this.myMap.put(key, value);
      } else {
         Object oldV = this.superMap.get(key);
         this.myMap.put(key, value);
         ++this.nShadowed;
         return oldV;
      }
   }

   public Object remove(Object key) {
      throw new UnsupportedOperationException("Remove from NestedMap");
   }

   public void clear() {
      throw new UnsupportedOperationException("Clear in NestedMap");
   }

   private final class EntrySet extends AbstractSet {
      private EntrySet() {
      }

      public Iterator iterator() {
         return new ConcatenatedIterator(NestedMap.this.myMap.entrySet().iterator(), new FilteringIterator(NestedMap.this.superMap.entrySet(), NestedMap.this.entryKeyNotInMyMap));
      }

      public int size() {
         return NestedMap.this.size();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry ent = (Entry)o;
            Object entKey = ent.getKey();
            Object entVal = ent.getValue();
            if (entVal != null) {
               Object val = NestedMap.this.get(entKey);
               return val != null && val.equals(entVal);
            } else {
               return NestedMap.this.containsKey(entKey) && NestedMap.this.get(entKey) == null;
            }
         }
      }

      public boolean remove(Object o) {
         throw new UnsupportedOperationException("Remove from NestedMap.entrySet");
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   public final class KeySet extends AbstractSet {
      public Iterator iterator() {
         return new ConcatenatedIterator(NestedMap.this.myMap.keySet().iterator(), new FilteringIterator(NestedMap.this.superMap.keySet(), NestedMap.this.keyNotInMyMap));
      }

      public int size() {
         return NestedMap.this.size();
      }

      public boolean contains(Object o) {
         return NestedMap.this.containsKey(o);
      }

      public boolean remove(Object o) {
         throw new UnsupportedOperationException("Remove from NestedMap.keySet");
      }
   }
}
