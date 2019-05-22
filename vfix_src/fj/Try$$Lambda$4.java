package fj;

import fj.function.Try3;

// $FF: synthetic class
final class Try$$Lambda$4 implements F3 {
   private final Try3 arg$1;

   private Try$$Lambda$4(Try3 var1) {
      this.arg$1 = var1;
   }

   private static F3 get$Lambda(Try3 var0) {
      return new Try$$Lambda$4(var0);
   }

   public Object f(Object var1, Object var2, Object var3) {
      return Try.access$lambda$3(this.arg$1, var1, var2, var3);
   }

   public static F3 lambdaFactory$(Try3 var0) {
      return new Try$$Lambda$4(var0);
   }
}
