package fj.data;

import fj.F;
import fj.Unit;

// $FF: synthetic class
final class IOFunctions$$Lambda$29 implements F {
   private final Stream arg$1;

   private IOFunctions$$Lambda$29(Stream var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Stream var0) {
      return new IOFunctions$$Lambda$29(var0);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$28(this.arg$1, (Unit)var1);
   }

   public static F lambdaFactory$(Stream var0) {
      return new IOFunctions$$Lambda$29(var0);
   }
}
