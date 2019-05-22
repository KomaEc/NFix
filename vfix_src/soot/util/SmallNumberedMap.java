package soot.util;

import java.util.Iterator;

public final class SmallNumberedMap<T> {
   private Numberable[] array = new Numberable[8];
   private Object[] values = new Object[8];
   private int size = 0;

   public boolean put(Numberable key, T value) {
      int pos = this.findPosition(key);
      if (this.array[pos] == key) {
         if (this.values[pos] == value) {
            return false;
         } else {
            this.values[pos] = value;
            return true;
         }
      } else {
         ++this.size;
         if (this.size * 3 > this.array.length * 2) {
            this.doubleSize();
            pos = this.findPosition(key);
         }

         this.array[pos] = key;
         this.values[pos] = value;
         return true;
      }
   }

   public T get(Numberable key) {
      return this.values[this.findPosition(key)];
   }

   public int nonNullSize() {
      int ret = 0;
      Object[] var2 = this.values;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object element = var2[var4];
         if (element != null) {
            ++ret;
         }
      }

      return ret;
   }

   public Iterator<Numberable> keyIterator() {
      return new SmallNumberedMap.KeyIterator(this);
   }

   public Iterator<T> iterator() {
      return new SmallNumberedMap.ValueIterator(this);
   }

   private final int findPosition(Numberable o) {
      int number = o.getNumber();
      if (number == 0) {
         throw new RuntimeException("unnumbered");
      } else {
         for(number &= this.array.length - 1; this.array[number] != o; number = number + 1 & this.array.length - 1) {
            if (this.array[number] == null) {
               return number;
            }
         }

         return number;
      }
   }

   private final void doubleSize() {
      Numberable[] oldArray = this.array;
      Object[] oldValues = this.values;
      int newLength = this.array.length * 2;
      this.values = new Object[newLength];
      this.array = new Numberable[newLength];

      for(int i = 0; i < oldArray.length; ++i) {
         Numberable element = oldArray[i];
         if (element != null) {
            int pos = this.findPosition(element);
            this.array[pos] = element;
            this.values[pos] = oldValues[i];
         }
      }

   }

   class ValueIterator extends SmallNumberedMap<T>.SmallNumberedMapIterator<T> {
      ValueIterator(SmallNumberedMap<T> map) {
         super(map);
      }

      public final T next() {
         Object ret = SmallNumberedMap.this.values[this.cur];
         ++this.cur;
         this.seekNext();
         return ret;
      }
   }

   class KeyIterator extends SmallNumberedMap<T>.SmallNumberedMapIterator<Numberable> {
      KeyIterator(SmallNumberedMap map) {
         super(map);
      }

      public final Numberable next() {
         Numberable ret = SmallNumberedMap.this.array[this.cur];
         ++this.cur;
         this.seekNext();
         return ret;
      }
   }

   abstract class SmallNumberedMapIterator<C> implements Iterator<C> {
      SmallNumberedMap<C> map;
      int cur = 0;

      SmallNumberedMapIterator(SmallNumberedMap<C> map) {
         this.map = map;
         this.seekNext();
      }

      protected final void seekNext() {
         while(true) {
            try {
               if (this.map.values[this.cur] == null) {
                  ++this.cur;
                  continue;
               }
            } catch (ArrayIndexOutOfBoundsException var2) {
               this.cur = -1;
            }

            return;
         }
      }

      public final boolean hasNext() {
         return this.cur != -1;
      }

      public abstract C next();

      public void remove() {
         throw new RuntimeException("Not implemented.");
      }
   }
}
