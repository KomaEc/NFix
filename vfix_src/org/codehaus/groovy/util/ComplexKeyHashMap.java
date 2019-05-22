package org.codehaus.groovy.util;

import java.util.NoSuchElementException;

public class ComplexKeyHashMap {
   protected ComplexKeyHashMap.Entry[] table;
   protected static final int DEFAULT_CAPACITY = 32;
   protected static final int MINIMUM_CAPACITY = 4;
   protected static final int MAXIMUM_CAPACITY = 268435456;
   protected int size;
   protected transient int threshold;

   public ComplexKeyHashMap() {
      this.init(32);
   }

   public ComplexKeyHashMap(boolean b) {
   }

   public ComplexKeyHashMap(int expectedMaxSize) {
      this.init(this.capacity(expectedMaxSize));
   }

   public static int hash(int h) {
      h += ~(h << 9);
      h ^= h >>> 14;
      h += h << 4;
      h ^= h >>> 10;
      return h;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      Object[] tab = this.table;

      for(int i = 0; i < tab.length; ++i) {
         tab[i] = null;
      }

      this.size = 0;
   }

   public void init(int initCapacity) {
      this.threshold = initCapacity * 6 / 8;
      this.table = new ComplexKeyHashMap.Entry[initCapacity];
   }

   public void resize(int newLength) {
      ComplexKeyHashMap.Entry[] oldTable = this.table;
      int oldLength = this.table.length;
      ComplexKeyHashMap.Entry[] newTable = new ComplexKeyHashMap.Entry[newLength];

      ComplexKeyHashMap.Entry next;
      for(int j = 0; j < oldLength; ++j) {
         for(ComplexKeyHashMap.Entry e = oldTable[j]; e != null; e = next) {
            next = e.next;
            int index = e.hash & newLength - 1;
            e.next = newTable[index];
            newTable[index] = e;
         }
      }

      this.table = newTable;
      this.threshold = 6 * newLength / 8;
   }

   private int capacity(int expectedMaxSize) {
      int minCapacity = 8 * expectedMaxSize / 6;
      int result;
      if (minCapacity <= 268435456 && minCapacity >= 0) {
         for(result = 4; result < minCapacity; result <<= 1) {
         }
      } else {
         result = 268435456;
      }

      return result;
   }

   public ComplexKeyHashMap.Entry[] getTable() {
      return this.table;
   }

   public ComplexKeyHashMap.EntryIterator getEntrySetIterator() {
      return new ComplexKeyHashMap.EntryIterator() {
         ComplexKeyHashMap.Entry next;
         int index;
         ComplexKeyHashMap.Entry current;

         {
            ComplexKeyHashMap.Entry[] t = ComplexKeyHashMap.this.table;
            int i = t.length;
            ComplexKeyHashMap.Entry n = null;
            if (ComplexKeyHashMap.this.size != 0) {
               while(i > 0) {
                  --i;
                  if ((n = t[i]) != null) {
                     break;
                  }
               }
            }

            this.next = n;
            this.index = i;
         }

         public boolean hasNext() {
            return this.next != null;
         }

         public ComplexKeyHashMap.Entry next() {
            return this.nextEntry();
         }

         ComplexKeyHashMap.Entry nextEntry() {
            ComplexKeyHashMap.Entry e = this.next;
            if (e == null) {
               throw new NoSuchElementException();
            } else {
               ComplexKeyHashMap.Entry n = e.next;
               ComplexKeyHashMap.Entry[] t = ComplexKeyHashMap.this.table;

               int i;
               for(i = this.index; n == null && i > 0; n = t[i]) {
                  --i;
               }

               this.index = i;
               this.next = n;
               return this.current = e;
            }
         }
      };
   }

   public interface EntryIterator {
      boolean hasNext();

      ComplexKeyHashMap.Entry next();
   }

   public static class Entry {
      public int hash;
      public ComplexKeyHashMap.Entry next;
      public Object value;

      public Object getValue() {
         return this.value;
      }

      public void setValue(Object value) {
         this.value = value;
      }
   }
}
