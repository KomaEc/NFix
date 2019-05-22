package fj.data;

import fj.P1;

// $FF: synthetic class
final class IOFunctions$$Lambda$7 implements SafeIO {
   private final P1 arg$1;

   private IOFunctions$$Lambda$7(P1 var1) {
      this.arg$1 = var1;
   }

   private static SafeIO get$Lambda(P1 var0) {
      return new IOFunctions$$Lambda$7(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$6(this.arg$1);
   }

   public static SafeIO lambdaFactory$(P1 var0) {
      return new IOFunctions$$Lambda$7(var0);
   }
}
