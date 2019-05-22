package soot.util;

import java.util.Iterator;

public final class NumberedSet<N extends Numberable> {
   private Numberable[] array = new Numberable[8];
   private BitVector bits;
   private int size = 0;
   private ArrayNumberer<N> universe;

   public NumberedSet(ArrayNumberer<N> universe) {
      this.universe = universe;
   }

   public boolean add(Numberable o) {
      int pos;
      if (this.array != null) {
         pos = this.findPosition(o);
         if (this.array[pos] == o) {
            return false;
         } else {
            ++this.size;
            if (this.size * 3 > this.array.length * 2) {
               this.doubleSize();
               if (this.array == null) {
                  int number = o.getNumber();
                  if (number == 0) {
                     throw new RuntimeException("unnumbered");
                  }

                  return this.bits.set(number);
               }

               pos = this.findPosition(o);
            }

            this.array[pos] = o;
            return true;
         }
      } else {
         pos = o.getNumber();
         if (pos == 0) {
            throw new RuntimeException("unnumbered");
         } else if (this.bits.set(pos)) {
            ++this.size;
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean contains(Numberable o) {
      if (this.array != null) {
         return this.array[this.findPosition(o)] != null;
      } else {
         int number = o.getNumber();
         if (number == 0) {
            throw new RuntimeException("unnumbered");
         } else {
            return this.bits.get(number);
         }
      }
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
      int uniSize = this.universe.size();
      Numberable[] oldArray;
      Numberable[] var3;
      int var4;
      int var5;
      Numberable element;
      if (this.array.length * 128 > uniSize) {
         this.bits = new BitVector(uniSize);
         oldArray = this.array;
         this.array = null;
         var3 = oldArray;
         var4 = oldArray.length;

         for(var5 = 0; var5 < var4; ++var5) {
            element = var3[var5];
            if (element != null) {
               this.bits.set(element.getNumber());
            }
         }
      } else {
         oldArray = this.array;
         this.array = new Numberable[this.array.length * 2];
         var3 = oldArray;
         var4 = oldArray.length;

         for(var5 = 0; var5 < var4; ++var5) {
            element = var3[var5];
            if (element != null) {
               this.array[this.findPosition(element)] = element;
            }
         }
      }

   }

   public Iterator<N> iterator() {
      return (Iterator)(this.array == null ? new NumberedSet.BitSetIterator(this) : new NumberedSet.NumberedSetIterator(this));
   }

   public final int size() {
      return this.size;
   }

   class NumberedSetIterator implements Iterator<N> {
      NumberedSet<N> set;
      int cur = 0;

      NumberedSetIterator(NumberedSet<N> set) {
         this.set = set;
         this.seekNext();
      }

      protected final void seekNext() {
         while(true) {
            try {
               if (this.set.array[this.cur] == null) {
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

      public void remove() {
         throw new RuntimeException("Not implemented.");
      }

      public final N next() {
         N ret = this.set.array[this.cur];
         ++this.cur;
         this.seekNext();
         return ret;
      }
   }

   class BitSetIterator implements Iterator<N> {
      soot.util.BitSetIterator iter;

      BitSetIterator(NumberedSet<N> set) {
         this.iter = set.bits.iterator();
      }

      public final boolean hasNext() {
         return this.iter.hasNext();
      }

      public void remove() {
         throw new RuntimeException("Not implemented.");
      }

      public final N next() {
         return NumberedSet.this.universe.get((long)this.iter.next());
      }
   }
}
