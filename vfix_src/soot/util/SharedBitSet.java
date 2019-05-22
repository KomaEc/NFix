package soot.util;

public final class SharedBitSet {
   BitVector value;
   boolean own;

   public SharedBitSet(int i) {
      this.own = true;
      this.value = new BitVector(i);
   }

   public SharedBitSet() {
      this(32);
   }

   private void acquire() {
      if (!this.own) {
         this.own = true;
         this.value = (BitVector)this.value.clone();
      }
   }

   private void canonicalize() {
      this.value = SharedBitSetCache.v().canonicalize(this.value);
      this.own = false;
   }

   public boolean set(int bit) {
      this.acquire();
      return this.value.set(bit);
   }

   public void clear(int bit) {
      this.acquire();
      this.value.clear(bit);
   }

   public boolean get(int bit) {
      return this.value.get(bit);
   }

   public void and(SharedBitSet other) {
      if (this.own) {
         this.value.and(other.value);
      } else {
         this.value = BitVector.and(this.value, other.value);
         this.own = true;
      }

      this.canonicalize();
   }

   public void or(SharedBitSet other) {
      if (this.own) {
         this.value.or(other.value);
      } else {
         this.value = BitVector.or(this.value, other.value);
         this.own = true;
      }

      this.canonicalize();
   }

   public boolean orAndAndNot(SharedBitSet orset, SharedBitSet andset, SharedBitSet andnotset) {
      this.acquire();
      boolean ret = this.value.orAndAndNot(orset.value, andset.value, andnotset.value);
      this.canonicalize();
      return ret;
   }

   public boolean orAndAndNot(SharedBitSet orset, BitVector andset, SharedBitSet andnotset) {
      this.acquire();
      boolean ret = this.value.orAndAndNot(orset.value, andset, andnotset == null ? null : andnotset.value);
      this.canonicalize();
      return ret;
   }

   public BitSetIterator iterator() {
      return this.value.iterator();
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      BitSetIterator it = this.iterator();

      while(it.hasNext()) {
         b.append(it.next());
         b.append(",");
      }

      return b.toString();
   }
}
