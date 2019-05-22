package fj.data;

import fj.function.Try0;

// $FF: synthetic class
final class IOFunctions$$Lambda$1 implements Try0 {
   private final IO arg$1;

   private IOFunctions$$Lambda$1(IO var1) {
      this.arg$1 = var1;
   }

   private static Try0 get$Lambda(IO var0) {
      return new IOFunctions$$Lambda$1(var0);
   }

   public Object f() {
      return IOFunctions.access$lambda$0(this.arg$1);
   }

   public static Try0 lambdaFactory$(IO var0) {
      return new IOFunctions$$Lambda$1(var0);
   }
}
