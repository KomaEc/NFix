package fj.data;

import fj.function.Effect0;

// $FF: synthetic class
final class Conversions$$Lambda$3 implements IO {
   private final Effect0 arg$1;

   private Conversions$$Lambda$3(Effect0 var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(Effect0 var0) {
      return new Conversions$$Lambda$3(var0);
   }

   public Object run() {
      return Conversions.access$lambda$2(this.arg$1);
   }

   public static IO lambdaFactory$(Effect0 var0) {
      return new Conversions$$Lambda$3(var0);
   }
}
