package fj;

import fj.function.TryEffect0;

// $FF: synthetic class
final class TryEffect$$Lambda$1 implements F {
   private final TryEffect0 arg$1;

   private TryEffect$$Lambda$1(TryEffect0 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(TryEffect0 var0) {
      return new TryEffect$$Lambda$1(var0);
   }

   public Object f(Object var1) {
      return TryEffect.access$lambda$0(this.arg$1, (Unit)var1);
   }

   public static F lambdaFactory$(TryEffect0 var0) {
      return new TryEffect$$Lambda$1(var0);
   }
}
