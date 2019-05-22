package org.codehaus.groovy.util;

public class SingleKeyHashMap extends ComplexKeyHashMap {
   public SingleKeyHashMap() {
   }

   public SingleKeyHashMap(boolean b) {
      super(false);
   }

   public boolean containsKey(String name) {
      return this.get(name) != null;
   }

   public void put(Object key, Object value) {
      this.getOrPut(key).value = value;
   }

   public final Object get(Object key) {
      int h = hash(key.hashCode());

      for(ComplexKeyHashMap.Entry e = this.table[h & this.table.length - 1]; e != null; e = e.next) {
         if (e.hash == h && ((SingleKeyHashMap.Entry)e).key.equals(key)) {
            return ((SingleKeyHashMap.Entry)e).value;
         }
      }

      return null;
   }

   public SingleKeyHashMap.Entry getOrPut(Object key) {
      int h = hash(key.hashCode());
      ComplexKeyHashMap.Entry[] t = this.table;
      int index = h & t.length - 1;

      for(ComplexKeyHashMap.Entry e = t[index]; e != null; e = e.next) {
         if (e.hash == h && ((SingleKeyHashMap.Entry)e).key.equals(key)) {
            return (SingleKeyHashMap.Entry)e;
         }
      }

      SingleKeyHashMap.Entry entry = new SingleKeyHashMap.Entry();
      entry.next = t[index];
      entry.hash = h;
      entry.key = key;
      t[index] = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * t.length);
      }

      return entry;
   }

   public SingleKeyHashMap.Entry getOrPutEntry(SingleKeyHashMap.Entry element) {
      Object key = element.key;
      int h = element.hash;
      ComplexKeyHashMap.Entry[] t = this.table;
      int index = h & t.length - 1;

      for(ComplexKeyHashMap.Entry e = t[index]; e != null; e = e.next) {
         if (e.hash == h && ((SingleKeyHashMap.Entry)e).key.equals(key)) {
            return (SingleKeyHashMap.Entry)e;
         }
      }

      SingleKeyHashMap.Entry entry = new SingleKeyHashMap.Entry();
      entry.next = t[index];
      entry.hash = h;
      entry.key = key;
      t[index] = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * t.length);
      }

      return entry;
   }

   public SingleKeyHashMap.Entry putCopyOfUnexisting(SingleKeyHashMap.Entry ee) {
      int h = ee.hash;
      ComplexKeyHashMap.Entry[] t = this.table;
      int index = h & t.length - 1;
      SingleKeyHashMap.Entry entry = new SingleKeyHashMap.Entry();
      entry.next = t[index];
      entry.hash = h;
      entry.key = ee.key;
      entry.value = ee.value;
      t[index] = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * t.length);
      }

      return entry;
   }

   public final ComplexKeyHashMap.Entry remove(Object key) {
      int h = hash(key.hashCode());
      int index = h & this.table.length - 1;
      ComplexKeyHashMap.Entry e = this.table[index];

      for(ComplexKeyHashMap.Entry prev = null; e != null; e = e.next) {
         if (e.hash == h && ((SingleKeyHashMap.Entry)e).key.equals(key)) {
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

   public static SingleKeyHashMap copy(SingleKeyHashMap dst, SingleKeyHashMap src, SingleKeyHashMap.Copier copier) {
      dst.threshold = src.threshold;
      dst.size = src.size;
      int len = src.table.length;
      ComplexKeyHashMap.Entry[] t = new ComplexKeyHashMap.Entry[len];
      ComplexKeyHashMap.Entry[] tt = src.table;

      for(int i = 0; i != len; ++i) {
         for(SingleKeyHashMap.Entry e = (SingleKeyHashMap.Entry)tt[i]; e != null; e = (SingleKeyHashMap.Entry)e.next) {
            SingleKeyHashMap.Entry ee = new SingleKeyHashMap.Entry();
            ee.hash = e.hash;
            ee.key = e.key;
            ee.value = copier.copy(e.value);
            ee.next = t[i];
            t[i] = ee;
         }
      }

      dst.table = t;
      return dst;
   }

   public interface Copier {
      Object copy(Object var1);
   }

   public static class Entry extends ComplexKeyHashMap.Entry {
      public Object key;

      public Object getKey() {
         return this.key;
      }
   }
}
