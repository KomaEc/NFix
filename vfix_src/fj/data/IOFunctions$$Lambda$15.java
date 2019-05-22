package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$15 implements F {
   private final IO arg$1;

   private IOFunctions$$Lambda$15(IO var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(IO var0) {
      return new IOFunctions$$Lambda$15(var0);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$14(this.arg$1, (F)var1);
   }

   public static F lambdaFactory$(IO var0) {
      return new IOFunctions$$Lambda$15(var0);
   }
}
