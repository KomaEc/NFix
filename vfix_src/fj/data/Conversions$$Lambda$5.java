package fj.data;

import fj.function.Effect0;

// $FF: synthetic class
final class Conversions$$Lambda$5 implements SafeIO {
   private final Effect0 arg$1;

   private Conversions$$Lambda$5(Effect0 var1) {
      this.arg$1 = var1;
   }

   private static SafeIO get$Lambda(Effect0 var0) {
      return new Conversions$$Lambda$5(var0);
   }

   public Object run() {
      return Conversions.access$lambda$4(this.arg$1);
   }

   public static SafeIO lambdaFactory$(Effect0 var0) {
      return new Conversions$$Lambda$5(var0);
   }
}
