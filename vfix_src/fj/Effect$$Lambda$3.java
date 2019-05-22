package fj;

import fj.function.Effect2;

// $FF: synthetic class
final class Effect$$Lambda$3 implements F2 {
   private final Effect2 arg$1;

   private Effect$$Lambda$3(Effect2 var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(Effect2 var0) {
      return new Effect$$Lambda$3(var0);
   }

   public Object f(Object var1, Object var2) {
      return Effect.access$lambda$2(this.arg$1, var1, var2);
   }

   public static F2 lambdaFactory$(Effect2 var0) {
      return new Effect$$Lambda$3(var0);
   }
}
