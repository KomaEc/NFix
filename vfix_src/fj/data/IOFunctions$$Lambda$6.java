package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$6 implements SafeIO {
   private final F arg$1;

   private IOFunctions$$Lambda$6(F var1) {
      this.arg$1 = var1;
   }

   private static SafeIO get$Lambda(F var0) {
      return new IOFunctions$$Lambda$6(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$5(this.arg$1);
   }

   public static SafeIO lambdaFactory$(F var0) {
      return new IOFunctions$$Lambda$6(var0);
   }
}
