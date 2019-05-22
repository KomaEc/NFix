package fj.data;

import fj.function.Try0;

// $FF: synthetic class
final class IOFunctions$$Lambda$3 implements IO {
   private final Try0 arg$1;

   private IOFunctions$$Lambda$3(Try0 var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(Try0 var0) {
      return new IOFunctions$$Lambda$3(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$2(this.arg$1);
   }

   public static IO lambdaFactory$(Try0 var0) {
      return new IOFunctions$$Lambda$3(var0);
   }
}
