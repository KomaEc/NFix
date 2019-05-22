package org.codehaus.groovy.util;

public class TripleKeyHashMap extends ComplexKeyHashMap {
   public final Object get(Object key1, Object key2, Object key3) {
      int h = hash(31 * (31 * key1.hashCode() + key2.hashCode()) + key3.hashCode());

      for(ComplexKeyHashMap.Entry e = this.table[h & this.table.length - 1]; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals((TripleKeyHashMap.Entry)e, key1, key2, key3)) {
            return e;
         }
      }

      return null;
   }

   public boolean checkEquals(TripleKeyHashMap.Entry e, Object key1, Object key2, Object key3) {
      return e.key1.equals(key1) && e.key2.equals(key2) && e.key3.equals(key3);
   }

   public TripleKeyHashMap.Entry getOrPut(Object key1, Object key2, Object key3) {
      int h = hash(31 * (31 * key1.hashCode() + key2.hashCode()) + key3.hashCode());
      int index = h & this.table.length - 1;

      for(ComplexKeyHashMap.Entry e = this.table[index]; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals((TripleKeyHashMap.Entry)e, key1, key2, key3)) {
            return (TripleKeyHashMap.Entry)e;
         }
      }

      TripleKeyHashMap.Entry entry = this.createEntry();
      entry.next = this.table[index];
      entry.hash = h;
      entry.key1 = key1;
      entry.key2 = key2;
      entry.key3 = key3;
      this.table[index] = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * this.table.length);
      }

      return entry;
   }

   public TripleKeyHashMap.Entry createEntry() {
      return new TripleKeyHashMap.Entry();
   }

   public final ComplexKeyHashMap.Entry remove(Object key1, Object key2, Object key3) {
      int h = hash(31 * (31 * key1.hashCode() + key2.hashCode()) + key3.hashCode());
      int index = h & this.table.length - 1;
      ComplexKeyHashMap.Entry e = this.table[index];

      for(ComplexKeyHashMap.Entry prev = null; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals((TripleKeyHashMap.Entry)e, key1, key2, key3)) {
            if (prev == null) {
               this.table[index] = e.next;
            } else {
               prev.next = e.next;
            }

            --this.size;
            e.next = null;
            return e;
         }

         prev = e;
      }

      return null;
   }

   public static class Entry extends ComplexKeyHashMap.Entry {
      public Object key1;
      public Object key2;
      public Object key3;
   }
}
