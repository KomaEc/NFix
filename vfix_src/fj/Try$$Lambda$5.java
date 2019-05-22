package fj;

import fj.function.Try4;

// $FF: synthetic class
final class Try$$Lambda$5 implements F4 {
   private final Try4 arg$1;

   private Try$$Lambda$5(Try4 var1) {
      this.arg$1 = var1;
   }

   private static F4 get$Lambda(Try4 var0) {
      return new Try$$Lambda$5(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4) {
      return Try.access$lambda$4(this.arg$1, var1, var2, var3, var4);
   }

   public static F4 lambdaFactory$(Try4 var0) {
      return new Try$$Lambda$5(var0);
   }
}
