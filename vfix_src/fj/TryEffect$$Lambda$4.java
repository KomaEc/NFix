package fj;

import fj.function.TryEffect3;

// $FF: synthetic class
final class TryEffect$$Lambda$4 implements F3 {
   private final TryEffect3 arg$1;

   private TryEffect$$Lambda$4(TryEffect3 var1) {
      this.arg$1 = var1;
   }

   private static F3 get$Lambda(TryEffect3 var0) {
      return new TryEffect$$Lambda$4(var0);
   }

   public Object f(Object var1, Object var2, Object var3) {
      return TryEffect.access$lambda$3(this.arg$1, var1, var2, var3);
   }

   public static F3 lambdaFactory$(TryEffect3 var0) {
      return new TryEffect$$Lambda$4(var0);
   }
}
