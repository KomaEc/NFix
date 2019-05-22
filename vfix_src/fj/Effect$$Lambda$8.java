package fj;

import fj.function.Effect7;

// $FF: synthetic class
final class Effect$$Lambda$8 implements F7 {
   private final Effect7 arg$1;

   private Effect$$Lambda$8(Effect7 var1) {
      this.arg$1 = var1;
   }

   private static F7 get$Lambda(Effect7 var0) {
      return new Effect$$Lambda$8(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return Effect.access$lambda$7(this.arg$1, var1, var2, var3, var4, var5, var6, var7);
   }

   public static F7 lambdaFactory$(Effect7 var0) {
      return new Effect$$Lambda$8(var0);
   }
}
