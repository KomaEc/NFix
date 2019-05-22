package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class IOFunctions$$Lambda$22 implements F {
   private final F2 arg$1;
   private final Object arg$2;

   private IOFunctions$$Lambda$22(F2 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(F2 var0, Object var1) {
      return new IOFunctions$$Lambda$22(var0, var1);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$21(this.arg$1, this.arg$2, var1);
   }

   public static F lambdaFactory$(F2 var0, Object var1) {
      return new IOFunctions$$Lambda$22(var0, var1);
   }
}
