package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$31 implements F {
   private final List arg$1;

   private IOFunctions$$Lambda$31(List var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(List var0) {
      return new IOFunctions$$Lambda$31(var0);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$30(this.arg$1, var1);
   }

   public static F lambdaFactory$(List var0) {
      return new IOFunctions$$Lambda$31(var0);
   }
}
