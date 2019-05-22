package fj.data;

import fj.P1;

// $FF: synthetic class
final class IOFunctions$$Lambda$2 implements IO {
   private final P1 arg$1;

   private IOFunctions$$Lambda$2(P1 var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(P1 var0) {
      return new IOFunctions$$Lambda$2(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$1(this.arg$1);
   }

   public static IO lambdaFactory$(P1 var0) {
      return new IOFunctions$$Lambda$2(var0);
   }
}
