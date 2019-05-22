package fj;

import fj.function.Try2;

// $FF: synthetic class
final class Try$$Lambda$3 implements F2 {
   private final Try2 arg$1;

   private Try$$Lambda$3(Try2 var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(Try2 var0) {
      return new Try$$Lambda$3(var0);
   }

   public Object f(Object var1, Object var2) {
      return Try.access$lambda$2(this.arg$1, var1, var2);
   }

   public static F2 lambdaFactory$(Try2 var0) {
      return new Try$$Lambda$3(var0);
   }
}
