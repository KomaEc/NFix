package edu.emory.mathcs.backport.java.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public abstract class AbstractMap extends java.util.AbstractMap {
   transient Set keySet;

   protected AbstractMap() {
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new AbstractSet() {
            public int size() {
               return AbstractMap.this.size();
            }

            public boolean contains(Object e) {
               return AbstractMap.this.containsKey(e);
            }

            public Iterator iterator() {
               return new Iterator() {
                  final Iterator itr = AbstractMap.this.entrySet().iterator();

                  public boolean hasNext() {
                     return this.itr.hasNext();
                  }

                  public Object next() {
                     return ((Entry)this.itr.next()).getKey();
                  }

                  public void remove() {
                     this.itr.remove();
                  }
               };
            }
         };
      }

      return this.keySet;
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   public static class SimpleImmutableEntry implements Entry {
      private final Object key;
      private final Object value;

      public SimpleImmutableEntry(Object key, Object value) {
         this.key = key;
         this.value = value;
      }

      public SimpleImmutableEntry(Entry entry) {
         this.key = entry.getKey();
         this.value = entry.getValue();
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return AbstractMap.eq(this.key, e.getKey()) && AbstractMap.eq(this.value, e.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }

   public static class SimpleEntry implements Entry {
      private final Object key;
      private Object value;

      public SimpleEntry(Object key, Object value) {
         this.key = key;
         this.value = value;
      }

      public SimpleEntry(Entry entry) {
         this.key = entry.getKey();
         this.value = entry.getValue();
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object value) {
         Object oldValue = this.value;
         this.value = value;
         return oldValue;
      }

      public boolean equals(Object o) {
         if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return AbstractMap.eq(this.key, e.getKey()) && AbstractMap.eq(this.value, e.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }
}
