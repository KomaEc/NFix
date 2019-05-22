package soot.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class LargeNumberedMap<K extends Numberable, V> {
   private Object[] values;
   private ArrayNumberer<K> universe;

   public LargeNumberedMap(ArrayNumberer<K> universe) {
      this.universe = universe;
      int newsize = universe.size();
      if (newsize < 8) {
         newsize = 8;
      }

      this.values = new Object[newsize];
   }

   public boolean put(Numberable key, V value) {
      int number = key.getNumber();
      if (number == 0) {
         throw new RuntimeException("oops, forgot to initialize");
      } else {
         if (number >= this.values.length) {
            Object[] oldValues = this.values;
            this.values = new Object[this.universe.size() * 2 + 5];
            System.arraycopy(oldValues, 0, this.values, 0, oldValues.length);
         }

         boolean ret = this.values[number] != value;
         this.values[number] = value;
         return ret;
      }
   }

   public V get(Numberable key) {
      int i = key.getNumber();
      return i >= this.values.length ? null : this.values[i];
   }

   public Iterator<K> keyIterator() {
      return new Iterator<K>() {
         int cur = 0;

         private void advance() {
            while(this.cur < LargeNumberedMap.this.values.length && LargeNumberedMap.this.values[this.cur] == null) {
               ++this.cur;
            }

         }

         public boolean hasNext() {
            this.advance();
            return this.cur < LargeNumberedMap.this.values.length;
         }

         public K next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return LargeNumberedMap.this.universe.get((long)(this.cur++));
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
