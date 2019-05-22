package fj;

import fj.function.Effect5;

// $FF: synthetic class
final class Effect$$Lambda$6 implements F5 {
   private final Effect5 arg$1;

   private Effect$$Lambda$6(Effect5 var1) {
      this.arg$1 = var1;
   }

   private static F5 get$Lambda(Effect5 var0) {
      return new Effect$$Lambda$6(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return Effect.access$lambda$5(this.arg$1, var1, var2, var3, var4, var5);
   }

   public static F5 lambdaFactory$(Effect5 var0) {
      return new Effect$$Lambda$6(var0);
   }
}
