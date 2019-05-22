package fj;

// $FF: synthetic class
final class Rng$$Lambda$1 implements F {
   private final int arg$1;
   private final int arg$2;

   private Rng$$Lambda$1(int var1, int var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(int var0, int var1) {
      return new Rng$$Lambda$1(var0, var1);
   }

   public Object f(Object var1) {
      return Rng.access$lambda$0(this.arg$1, this.arg$2, (Integer)var1);
   }

   public static F lambdaFactory$(int var0, int var1) {
      return new Rng$$Lambda$1(var0, var1);
   }
}
