package fj;

import fj.function.TryEffect7;

// $FF: synthetic class
final class TryEffect$$Lambda$8 implements F7 {
   private final TryEffect7 arg$1;

   private TryEffect$$Lambda$8(TryEffect7 var1) {
      this.arg$1 = var1;
   }

   private static F7 get$Lambda(TryEffect7 var0) {
      return new TryEffect$$Lambda$8(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return TryEffect.access$lambda$7(this.arg$1, var1, var2, var3, var4, var5, var6, var7);
   }

   public static F7 lambdaFactory$(TryEffect7 var0) {
      return new TryEffect$$Lambda$8(var0);
   }
}
