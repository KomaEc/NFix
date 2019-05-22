package fj;

import fj.function.Effect8;

// $FF: synthetic class
final class Effect$$Lambda$9 implements F8 {
   private final Effect8 arg$1;

   private Effect$$Lambda$9(Effect8 var1) {
      this.arg$1 = var1;
   }

   private static F8 get$Lambda(Effect8 var0) {
      return new Effect$$Lambda$9(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return Effect.access$lambda$8(this.arg$1, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static F8 lambdaFactory$(Effect8 var0) {
      return new Effect$$Lambda$9(var0);
   }
}
