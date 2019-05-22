package fj;

import fj.function.TryEffect6;

// $FF: synthetic class
final class TryEffect$$Lambda$7 implements F6 {
   private final TryEffect6 arg$1;

   private TryEffect$$Lambda$7(TryEffect6 var1) {
      this.arg$1 = var1;
   }

   private static F6 get$Lambda(TryEffect6 var0) {
      return new TryEffect$$Lambda$7(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return TryEffect.access$lambda$6(this.arg$1, var1, var2, var3, var4, var5, var6);
   }

   public static F6 lambdaFactory$(TryEffect6 var0) {
      return new TryEffect$$Lambda$7(var0);
   }
}
