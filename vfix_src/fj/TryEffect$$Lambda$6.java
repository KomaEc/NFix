package fj;

import fj.function.TryEffect5;

// $FF: synthetic class
final class TryEffect$$Lambda$6 implements F5 {
   private final TryEffect5 arg$1;

   private TryEffect$$Lambda$6(TryEffect5 var1) {
      this.arg$1 = var1;
   }

   private static F5 get$Lambda(TryEffect5 var0) {
      return new TryEffect$$Lambda$6(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return TryEffect.access$lambda$5(this.arg$1, var1, var2, var3, var4, var5);
   }

   public static F5 lambdaFactory$(TryEffect5 var0) {
      return new TryEffect$$Lambda$6(var0);
   }
}
