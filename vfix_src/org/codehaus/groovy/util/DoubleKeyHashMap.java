package org.codehaus.groovy.util;

public class DoubleKeyHashMap extends ComplexKeyHashMap {
   public final Object get(Object key1, Object key2) {
      int h = hash(31 * key1.hashCode() + key2.hashCode());

      for(ComplexKeyHashMap.Entry e = this.table[h & this.table.length - 1]; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals(e, key1, key2)) {
            return e;
         }
      }

      return null;
   }

   public boolean checkEquals(ComplexKeyHashMap.Entry e, Object key1, Object key2) {
      DoubleKeyHashMap.Entry ee = (DoubleKeyHashMap.Entry)e;
      return ee.key1 == key1 && ee.key2 == key2;
   }

   public DoubleKeyHashMap.Entry getOrPut(Object key1, Object key2) {
      int h = hash(31 * key1.hashCode() + key2.hashCode());
      int index = h & this.table.length - 1;

      for(ComplexKeyHashMap.Entry e = this.table[index]; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals(e, key1, key2)) {
            return (DoubleKeyHashMap.Entry)e;
         }
      }

      ComplexKeyHashMap.Entry entry = this.createEntry(key1, key2, h, index);
      this.table[index] = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * this.table.length);
      }

      return (DoubleKeyHashMap.Entry)entry;
   }

   private ComplexKeyHashMap.Entry createEntry(Object key1, Object key2, int h, int index) {
      DoubleKeyHashMap.Entry entry = this.createEntry();
      entry.next = this.table[index];
      entry.hash = h;
      entry.key1 = key1;
      entry.key2 = key2;
      return entry;
   }

   public DoubleKeyHashMap.Entry createEntry() {
      return new DoubleKeyHashMap.Entry();
   }

   public final ComplexKeyHashMap.Entry remove(Object key1, Object key2) {
      int h = hash(31 * key1.hashCode() + key2.hashCode());
      int index = h & this.table.length - 1;
      ComplexKeyHashMap.Entry e = this.table[index];

      for(ComplexKeyHashMap.Entry prev = null; e != null; e = e.next) {
         if (e.hash == h && this.checkEquals(e, key1, key2)) {
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
   }
}
