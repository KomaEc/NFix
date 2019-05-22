package fj;

import fj.function.TryEffect8;

// $FF: synthetic class
final class TryEffect$$Lambda$9 implements F8 {
   private final TryEffect8 arg$1;

   private TryEffect$$Lambda$9(TryEffect8 var1) {
      this.arg$1 = var1;
   }

   private static F8 get$Lambda(TryEffect8 var0) {
      return new TryEffect$$Lambda$9(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return TryEffect.access$lambda$8(this.arg$1, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static F8 lambdaFactory$(TryEffect8 var0) {
      return new TryEffect$$Lambda$9(var0);
   }
}
