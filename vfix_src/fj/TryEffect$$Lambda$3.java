package fj;

import fj.function.TryEffect2;

// $FF: synthetic class
final class TryEffect$$Lambda$3 implements F2 {
   private final TryEffect2 arg$1;

   private TryEffect$$Lambda$3(TryEffect2 var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(TryEffect2 var0) {
      return new TryEffect$$Lambda$3(var0);
   }

   public Object f(Object var1, Object var2) {
      return TryEffect.access$lambda$2(this.arg$1, var1, var2);
   }

   public static F2 lambdaFactory$(TryEffect2 var0) {
      return new TryEffect$$Lambda$3(var0);
   }
}
