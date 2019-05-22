package fj;

import fj.function.Effect1;

// $FF: synthetic class
final class Effect$$Lambda$2 implements F {
   private final Effect1 arg$1;

   private Effect$$Lambda$2(Effect1 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Effect1 var0) {
      return new Effect$$Lambda$2(var0);
   }

   public Object f(Object var1) {
      return Effect.access$lambda$1(this.arg$1, var1);
   }

   public static F lambdaFactory$(Effect1 var0) {
      return new Effect$$Lambda$2(var0);
   }
}
