package fj;

public class LcgRng extends Rng {
   private final Long seed;

   public LcgRng() {
      this(System.currentTimeMillis());
   }

   public LcgRng(long s) {
      this.seed = s;
   }

   public long getSeed() {
      return this.seed;
   }

   public P2<Rng, Integer> nextInt() {
      P2<Rng, Long> p = this.nextLong();
      int i = (int)(Long)p._2();
      return P.p(p._1(), i);
   }

   public P2<Rng, Long> nextLong() {
      P2<Long, Long> p = nextLong(this.seed);
      return P.p(new LcgRng((Long)p._1()), p._2());
   }

   static P2<Long, Long> nextLong(long seed) {
      long newSeed = seed * 25214903917L + 11L & 281474976710655L;
      long n = Long.valueOf(newSeed >>> 16);
      return P.p(newSeed, n);
   }
}
