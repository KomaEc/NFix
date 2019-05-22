package soot.util;

public class BitVector {
   private long[] bits;

   public BitVector() {
      this(64);
   }

   public BitVector(BitVector other) {
      this.bits = new long[other.bits.length];
      System.arraycopy(other.bits, 0, this.bits, 0, other.bits.length);
   }

   public BitVector(int numBits) {
      int lastIndex = this.indexOf(numBits - 1);
      this.bits = new long[lastIndex + 1];
   }

   private int indexOf(int bit) {
      return bit >> 6;
   }

   private long mask(int bit) {
      return 1L << (bit & 63);
   }

   public void and(BitVector other) {
      if (this != other) {
         long[] otherBits = other.bits;
         int numToAnd = otherBits.length;
         if (this.bits.length < numToAnd) {
            numToAnd = this.bits.length;
         }

         int i;
         for(i = 0; i < numToAnd; ++i) {
            this.bits[i] &= otherBits[i];
         }

         while(i < this.bits.length) {
            this.bits[i] = 0L;
            ++i;
         }

      }
   }

   public void andNot(BitVector other) {
      long[] otherBits = other.bits;
      int numToAnd = otherBits.length;
      if (this.bits.length < numToAnd) {
         numToAnd = this.bits.length;
      }

      for(int i = 0; i < numToAnd; ++i) {
         this.bits[i] &= ~otherBits[i];
      }

   }

   public void clear(int bit) {
      if (this.indexOf(bit) < this.bits.length) {
         long[] var10000 = this.bits;
         int var10001 = this.indexOf(bit);
         var10000[var10001] &= ~this.mask(bit);
      }

   }

   public Object clone() {
      try {
         BitVector ret = (BitVector)super.clone();
         System.arraycopy(this.bits, 0, ret.bits, 0, ret.bits.length);
         return ret;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public boolean equals(Object o) {
      if (!(o instanceof BitVector)) {
         return false;
      } else {
         BitVector other = (BitVector)o;
         int min = this.bits.length;
         long[] longer = other.bits;
         if (other.bits.length < min) {
            min = other.bits.length;
            longer = this.bits;
         }

         int i;
         for(i = 0; i < min; ++i) {
            if (this.bits[i] != other.bits[i]) {
               return false;
            }
         }

         while(i < longer.length) {
            if (longer[i] != 0L) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }

   public boolean get(int bit) {
      if (this.indexOf(bit) >= this.bits.length) {
         return false;
      } else {
         return (this.bits[this.indexOf(bit)] & this.mask(bit)) != 0L;
      }
   }

   public int hashCode() {
      long ret = 0L;
      long[] var3 = this.bits;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long element = var3[var5];
         ret ^= element;
      }

      return (int)(ret >> 32 ^ ret);
   }

   public int length() {
      int i;
      for(i = this.bits.length - 1; i >= 0 && this.bits[i] == 0L; --i) {
      }

      if (i < 0) {
         return 0;
      } else {
         long j = this.bits[i];
         ++i;
         i <<= 6;

         for(long k = Long.MIN_VALUE; (k & j) == 0L; --i) {
            k >>= 1;
         }

         return i;
      }
   }

   public void copyFrom(BitVector other) {
      if (this != other) {
         long[] otherBits = other.bits;

         int j;
         for(j = otherBits.length - 1; j >= 0 && otherBits[j] == 0L; --j) {
         }

         this.expand(j << 6);

         int i;
         for(i = j + 1; j >= 0; --j) {
            this.bits[j] = otherBits[j];
         }

         while(i < this.bits.length) {
            this.bits[i] = 0L;
            ++i;
         }

      }
   }

   public void or(BitVector other) {
      if (this != other) {
         long[] otherBits = other.bits;

         int j;
         for(j = otherBits.length - 1; j >= 0 && otherBits[j] == 0L; --j) {
         }

         this.expand(j << 6);

         while(j >= 0) {
            long[] var10000 = this.bits;
            var10000[j] |= otherBits[j];
            --j;
         }

      }
   }

   public int cardinality() {
      int c = 0;
      long[] var2 = this.bits;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         for(long v = var2[var4]; v != 0L; ++c) {
            v &= v - 1L;
         }
      }

      return c;
   }

   public boolean intersects(BitVector other) {
      long[] otherBits = other.bits;
      int numToCheck = otherBits.length;
      if (this.bits.length < numToCheck) {
         numToCheck = this.bits.length;
      }

      for(int i = 0; i < numToCheck; ++i) {
         if ((this.bits[i] & otherBits[i]) != 0L) {
            return true;
         }
      }

      return false;
   }

   private void expand(int bit) {
      int n = this.indexOf(bit) + 1;
      if (n > this.bits.length) {
         if (this.bits.length * 2 > n) {
            n = this.bits.length * 2;
         }

         long[] newBits = new long[n];
         System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
         this.bits = newBits;
      }
   }

   public void xor(BitVector other) {
      if (this != other) {
         long[] otherBits = other.bits;

         int j;
         for(j = otherBits.length - 1; j >= 0 && otherBits[j] == 0L; --j) {
         }

         this.expand(j << 6);

         while(j >= 0) {
            long[] var10000 = this.bits;
            var10000[j] ^= otherBits[j];
            --j;
         }

      }
   }

   public boolean set(int bit) {
      this.expand(bit);
      boolean ret = !this.get(bit);
      long[] var10000 = this.bits;
      int var10001 = this.indexOf(bit);
      var10000[var10001] |= this.mask(bit);
      return ret;
   }

   public int size() {
      return this.bits.length << 6;
   }

   public String toString() {
      StringBuffer ret = new StringBuffer();
      ret.append('{');
      boolean start = true;
      BitSetIterator it = new BitSetIterator(this.bits);

      while(it.hasNext()) {
         int bit = it.next();
         if (!start) {
            ret.append(", ");
         }

         start = false;
         ret.append(bit);
      }

      ret.append('}');
      return ret.toString();
   }

   public boolean orAndAndNot(BitVector orset, BitVector andset, BitVector andnotset) {
      boolean ret = false;
      long[] a = null;
      long[] b = null;
      long[] c = null;
      long[] d = null;
      long[] e = null;
      long[] a = this.bits;
      int al = a.length;
      int bl;
      if (orset == null) {
         bl = 0;
      } else {
         b = orset.bits;
         bl = b.length;
      }

      int cl;
      if (andset == null) {
         cl = 0;
      } else {
         c = andset.bits;
         cl = c.length;
      }

      int dl;
      if (andnotset == null) {
         dl = 0;
      } else {
         d = andnotset.bits;
         dl = d.length;
      }

      long[] e;
      if (al < bl) {
         e = new long[bl];
         System.arraycopy(a, 0, e, 0, al);
         this.bits = e;
      } else {
         e = a;
      }

      int i = 0;
      long l;
      if (c == null) {
         if (dl > bl) {
            while(i < bl) {
               l = b[i] & ~d[i];
               if ((l & ~e[i]) != 0L) {
                  ret = true;
               }

               e[i] |= l;
               ++i;
            }
         } else {
            while(i < dl) {
               l = b[i] & ~d[i];
               if ((l & ~e[i]) != 0L) {
                  ret = true;
               }

               e[i] |= l;
               ++i;
            }

            while(i < bl) {
               l = b[i];
               if ((l & ~e[i]) != 0L) {
                  ret = true;
               }

               e[i] |= l;
               ++i;
            }
         }
      } else if (bl <= cl && bl <= dl) {
         while(i < bl) {
            l = b[i] & c[i] & ~d[i];
            if ((l & ~e[i]) != 0L) {
               ret = true;
            }

            e[i] |= l;
            ++i;
         }
      } else if (cl <= bl && cl <= dl) {
         while(i < cl) {
            l = b[i] & c[i] & ~d[i];
            if ((l & ~e[i]) != 0L) {
               ret = true;
            }

            e[i] |= l;
            ++i;
         }
      } else {
         while(i < dl) {
            l = b[i] & c[i] & ~d[i];
            if ((l & ~e[i]) != 0L) {
               ret = true;
            }

            e[i] |= l;
            ++i;
         }

         int shorter = cl;
         if (bl < cl) {
            shorter = bl;
         }

         while(i < shorter) {
            l = b[i] & c[i];
            if ((l & ~e[i]) != 0L) {
               ret = true;
            }

            e[i] |= l;
            ++i;
         }
      }

      return ret;
   }

   public static BitVector and(BitVector set1, BitVector set2) {
      int min = set1.size();
      int max = set2.size();
      if (min > max) {
         min = max;
      }

      BitVector ret = new BitVector(min);
      long[] retbits = ret.bits;
      long[] bits1 = set1.bits;
      long[] bits2 = set2.bits;
      min >>= 6;

      for(int i = 0; i < min; ++i) {
         retbits[i] = bits1[i] & bits2[i];
      }

      return ret;
   }

   public static BitVector or(BitVector set1, BitVector set2) {
      int min = set1.size();
      int max = set2.size();
      if (min > max) {
         min = max;
         max = set1.size();
      }

      BitVector ret = new BitVector(max);
      long[] retbits = ret.bits;
      long[] bits1 = set1.bits;
      long[] bits2 = set2.bits;
      min >>= 6;
      max >>= 6;

      for(int i = 0; i < min; ++i) {
         retbits[i] = bits1[i] | bits2[i];
      }

      if (bits1.length == min) {
         System.arraycopy(bits2, min, retbits, min, max - min);
      } else {
         System.arraycopy(bits1, min, retbits, min, max - min);
      }

      return ret;
   }

   public BitSetIterator iterator() {
      return new BitSetIterator(this.bits);
   }
}
