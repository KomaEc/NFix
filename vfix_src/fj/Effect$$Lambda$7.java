package fj;

import fj.function.Effect6;

// $FF: synthetic class
final class Effect$$Lambda$7 implements F6 {
   private final Effect6 arg$1;

   private Effect$$Lambda$7(Effect6 var1) {
      this.arg$1 = var1;
   }

   private static F6 get$Lambda(Effect6 var0) {
      return new Effect$$Lambda$7(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return Effect.access$lambda$6(this.arg$1, var1, var2, var3, var4, var5, var6);
   }

   public static F6 lambdaFactory$(Effect6 var0) {
      return new Effect$$Lambda$7(var0);
   }
}
