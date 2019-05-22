package polyglot.util;

public class BitVector {
   private int size;
   private boolean[] bits;

   public BitVector() {
      this(32);
   }

   public BitVector(int initialSize) {
      this.size = initialSize;
      this.bits = new boolean[this.size];
   }

   public final void setBit(int which, boolean value) {
      if (which >= this.size) {
         this.size += 32;
         boolean[] newBits = new boolean[this.size];

         for(int i = 0; i < this.bits.length; ++i) {
            newBits[i] = this.bits[i];
         }

         this.bits = newBits;
      }

      this.bits[which] = value;
   }

   public final boolean getBit(int which) {
      return which >= this.size ? false : this.bits[which];
   }
}
