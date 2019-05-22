package fj;

import fj.function.Effect4;

// $FF: synthetic class
final class Effect$$Lambda$5 implements F4 {
   private final Effect4 arg$1;

   private Effect$$Lambda$5(Effect4 var1) {
      this.arg$1 = var1;
   }

   private static F4 get$Lambda(Effect4 var0) {
      return new Effect$$Lambda$5(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4) {
      return Effect.access$lambda$4(this.arg$1, var1, var2, var3, var4);
   }

   public static F4 lambdaFactory$(Effect4 var0) {
      return new Effect$$Lambda$5(var0);
   }
}
