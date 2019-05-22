package fj.data;

import fj.function.Try0;

// $FF: synthetic class
final class Conversions$$Lambda$11 implements IO {
   private final Try0 arg$1;

   private Conversions$$Lambda$11(Try0 var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(Try0 var0) {
      return new Conversions$$Lambda$11(var0);
   }

   public Object run() {
      return Conversions.access$lambda$10(this.arg$1);
   }

   public static IO lambdaFactory$(Try0 var0) {
      return new Conversions$$Lambda$11(var0);
   }
}
