package soot.util;

import java.util.NoSuchElementException;

public class BitSetIterator {
   long[] bits;
   int index;
   long save = 0L;
   static final int[] lookup = new int[]{-1, 0, 1, 39, 2, 15, 40, 23, 3, 12, 16, 59, 41, 19, 24, 54, 4, -1, 13, 10, 17, 62, 60, 28, 42, 30, 20, 51, 25, 44, 55, 47, 5, 32, -1, 38, 14, 22, 11, 58, 18, 53, -1, 9, 61, 27, 29, 50, 43, 46, 31, 37, 21, 57, 52, 8, 26, 49, 45, 36, 56, 7, 48, 35, 6, 34, 33};

   BitSetIterator(long[] bits) {
      this.bits = bits;

      for(this.index = 0; this.index < bits.length && bits[this.index] == 0L; ++this.index) {
      }

      if (this.index < bits.length) {
         this.save = bits[this.index];
      }

   }

   public boolean hasNext() {
      return this.index < this.bits.length;
   }

   public int next() {
      if (this.index >= this.bits.length) {
         throw new NoSuchElementException();
      } else {
         long k = this.save & this.save - 1L;
         long diff = this.save ^ k;
         this.save = k;
         int result = diff < 0L ? 64 * this.index + 63 : 64 * this.index + lookup[(int)(diff % 67L)];
         if (this.save == 0L) {
            ++this.index;

            while(this.index < this.bits.length && this.bits[this.index] == 0L) {
               ++this.index;
            }

            if (this.index < this.bits.length) {
               this.save = this.bits[this.index];
            }
         }

         return result;
      }
   }
}
