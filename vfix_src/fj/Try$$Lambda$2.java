package fj;

import fj.function.Try1;

// $FF: synthetic class
final class Try$$Lambda$2 implements F {
   private final Try1 arg$1;

   private Try$$Lambda$2(Try1 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Try1 var0) {
      return new Try$$Lambda$2(var0);
   }

   public Object f(Object var1) {
      return Try.access$lambda$1(this.arg$1, var1);
   }

   public static F lambdaFactory$(Try1 var0) {
      return new Try$$Lambda$2(var0);
   }
}
