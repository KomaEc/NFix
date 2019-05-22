package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$28 implements F {
   private final Stream arg$1;

   private IOFunctions$$Lambda$28(Stream var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Stream var0) {
      return new IOFunctions$$Lambda$28(var0);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$27(this.arg$1, var1);
   }

   public static F lambdaFactory$(Stream var0) {
      return new IOFunctions$$Lambda$28(var0);
   }
}
