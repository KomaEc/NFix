package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class IOFunctions$$Lambda$16 implements F {
   private final IO arg$1;
   private final F2 arg$2;

   private IOFunctions$$Lambda$16(IO var1, F2 var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(IO var0, F2 var1) {
      return new IOFunctions$$Lambda$16(var0, var1);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$15(this.arg$1, this.arg$2, var1);
   }

   public static F lambdaFactory$(IO var0, F2 var1) {
      return new IOFunctions$$Lambda$16(var0, var1);
   }
}
