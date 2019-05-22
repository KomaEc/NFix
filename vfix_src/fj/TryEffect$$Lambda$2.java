package fj;

import fj.function.TryEffect1;

// $FF: synthetic class
final class TryEffect$$Lambda$2 implements F {
   private final TryEffect1 arg$1;

   private TryEffect$$Lambda$2(TryEffect1 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(TryEffect1 var0) {
      return new TryEffect$$Lambda$2(var0);
   }

   public Object f(Object var1) {
      return TryEffect.access$lambda$1(this.arg$1, var1);
   }

   public static F lambdaFactory$(TryEffect1 var0) {
      return new TryEffect$$Lambda$2(var0);
   }
}
