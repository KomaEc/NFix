package fj;

import fj.function.Try6;

// $FF: synthetic class
final class Try$$Lambda$7 implements F6 {
   private final Try6 arg$1;

   private Try$$Lambda$7(Try6 var1) {
      this.arg$1 = var1;
   }

   private static F6 get$Lambda(Try6 var0) {
      return new Try$$Lambda$7(var0);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return Try.access$lambda$6(this.arg$1, var1, var2, var3, var4, var5, var6);
   }

   public static F6 lambdaFactory$(Try6 var0) {
      return new Try$$Lambda$7(var0);
   }
}
