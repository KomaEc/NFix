package fj;

import fj.function.Try5;

// $FF: synthetic class
final class Try$$Lambda$6 implements F5 {
   private final Try5 arg$1;

   private Try$$Lambda$6(Try5 var1) {
      this.arg$1 = var1;
   }

   private static F5 get$Lambda(Try5 var0) {
      return new Try$$Lambda$6(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return Try.access$lambda$5(this.arg$1, var1, var2, var3, var4, var5);
   }

   public static F5 lambdaFactory$(Try5 var0) {
      return new Try$$Lambda$6(var0);
   }
}
