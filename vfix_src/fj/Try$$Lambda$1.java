package fj;

import fj.function.Try0;

// $FF: synthetic class
final class Try$$Lambda$1 implements F {
   private final Try0 arg$1;

   private Try$$Lambda$1(Try0 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Try0 var0) {
      return new Try$$Lambda$1(var0);
   }

   public Object f(Object var1) {
      return Try.access$lambda$0(this.arg$1, (Unit)var1);
   }

   public static F lambdaFactory$(Try0 var0) {
      return new Try$$Lambda$1(var0);
   }
}
