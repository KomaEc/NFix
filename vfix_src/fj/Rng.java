package fj;

public abstract class Rng {
   public abstract P2<Rng, Integer> nextInt();

   public abstract P2<Rng, Long> nextLong();

   public P2<Rng, Integer> range(int low, int high) {
      return this.nextNatural().map2(Rng$$Lambda$1.lambdaFactory$(high, low));
   }

   public P2<Rng, Integer> nextNatural() {
      return this.nextInt().map2(Rng$$Lambda$2.lambdaFactory$());
   }

   // $FF: synthetic method
   private static Integer lambda$nextNatural$69(Integer x) {
      return x < 0 ? -(x + 1) : x;
   }

   // $FF: synthetic method
   private static Integer lambda$range$68(int var0, int var1, Integer x) {
      return x % (var0 - var1 + 1) + var1;
   }

   // $FF: synthetic method
   static Integer access$lambda$0(int var0, int var1, Integer var2) {
      return lambda$range$68(var0, var1, var2);
   }

   // $FF: synthetic method
   static Integer access$lambda$1(Integer var0) {
      return lambda$nextNatural$69(var0);
   }
}
