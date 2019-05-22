package fj;

import fj.function.Try8;

// $FF: synthetic class
final class Try$$Lambda$9 implements F8 {
   private final Try8 arg$1;

   private Try$$Lambda$9(Try8 var1) {
      this.arg$1 = var1;
   }

   private static F8 get$Lambda(Try8 var0) {
      return new Try$$Lambda$9(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return Try.access$lambda$8(this.arg$1, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static F8 lambdaFactory$(Try8 var0) {
      return new Try$$Lambda$9(var0);
   }
}
