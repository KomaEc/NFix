package fj;

import fj.function.TryEffect4;

// $FF: synthetic class
final class TryEffect$$Lambda$5 implements F4 {
   private final TryEffect4 arg$1;

   private TryEffect$$Lambda$5(TryEffect4 var1) {
      this.arg$1 = var1;
   }

   private static F4 get$Lambda(TryEffect4 var0) {
      return new TryEffect$$Lambda$5(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4) {
      return TryEffect.access$lambda$4(this.arg$1, var1, var2, var3, var4);
   }

   public static F4 lambdaFactory$(TryEffect4 var0) {
      return new TryEffect$$Lambda$5(var0);
   }
}
