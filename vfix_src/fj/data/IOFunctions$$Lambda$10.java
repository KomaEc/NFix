package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class IOFunctions$$Lambda$10 implements F2 {
   private final F arg$1;

   private IOFunctions$$Lambda$10(F var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(F var0) {
      return new IOFunctions$$Lambda$10(var0);
   }

   public Object f(Object var1, Object var2) {
      return IOFunctions.access$lambda$9(this.arg$1, var1, (IO)var2);
   }

   public static F2 lambdaFactory$(F var0) {
      return new IOFunctions$$Lambda$10(var0);
   }
}
