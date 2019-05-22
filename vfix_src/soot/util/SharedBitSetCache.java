package soot.util;

import soot.G;
import soot.Singletons;

public final class SharedBitSetCache {
   public static final int size = 32749;
   public BitVector[] cache = new BitVector[32749];
   public BitVector[] orAndAndNotCache = new BitVector[32749];

   public SharedBitSetCache(Singletons.Global g) {
   }

   public static SharedBitSetCache v() {
      return G.v().soot_util_SharedBitSetCache();
   }

   public BitVector canonicalize(BitVector set) {
      int hash = set.hashCode();
      if (hash < 0) {
         hash = -hash;
      }

      hash %= 32749;
      return this.cache[hash] != null && this.cache[hash].equals(set) ? this.cache[hash] : (this.cache[hash] = set);
   }
}
