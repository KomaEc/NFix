package fj;

import fj.function.Effect3;

// $FF: synthetic class
final class Effect$$Lambda$4 implements F3 {
   private final Effect3 arg$1;

   private Effect$$Lambda$4(Effect3 var1) {
      this.arg$1 = var1;
   }

   private static F3 get$Lambda(Effect3 var0) {
      return new Effect$$Lambda$4(var0);
   }

   public Object f(Object var1, Object var2, Object var3) {
      return Effect.access$lambda$3(this.arg$1, var1, var2, var3);
   }

   public static F3 lambdaFactory$(Effect3 var0) {
      return new Effect$$Lambda$4(var0);
   }
}
