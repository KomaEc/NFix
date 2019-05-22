package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$5 implements IO {
   private final F arg$1;

   private IOFunctions$$Lambda$5(F var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(F var0) {
      return new IOFunctions$$Lambda$5(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$4(this.arg$1);
   }

   public static IO lambdaFactory$(F var0) {
      return new IOFunctions$$Lambda$5(var0);
   }
}
