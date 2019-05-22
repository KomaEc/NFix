package fj;

import fj.function.Try7;

// $FF: synthetic class
final class Try$$Lambda$8 implements F7 {
   private final Try7 arg$1;

   private Try$$Lambda$8(Try7 var1) {
      this.arg$1 = var1;
   }

   private static F7 get$Lambda(Try7 var0) {
      return new Try$$Lambda$8(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return Try.access$lambda$7(this.arg$1, var1, var2, var3, var4, var5, var6, var7);
   }

   public static F7 lambdaFactory$(Try7 var0) {
      return new Try$$Lambda$8(var0);
   }
}
