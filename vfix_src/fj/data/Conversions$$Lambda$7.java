package fj.data;

import fj.F;

// $FF: synthetic class
final class Conversions$$Lambda$7 implements SafeIO {
   private final F arg$1;

   private Conversions$$Lambda$7(F var1) {
      this.arg$1 = var1;
   }

   private static SafeIO get$Lambda(F var0) {
      return new Conversions$$Lambda$7(var0);
   }

   public Object run() {
      return Conversions.access$lambda$6(this.arg$1);
   }

   public static SafeIO lambdaFactory$(F var0) {
      return new Conversions$$Lambda$7(var0);
   }
}
