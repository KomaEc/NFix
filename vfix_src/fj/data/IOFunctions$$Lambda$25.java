package fj.data;

import fj.F;

// $FF: synthetic class
final class IOFunctions$$Lambda$25 implements F {
   private final F arg$1;
   private final Object arg$2;

   private IOFunctions$$Lambda$25(F var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(F var0, Object var1) {
      return new IOFunctions$$Lambda$25(var0, var1);
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$24(this.arg$1, this.arg$2, (List)var1);
   }

   public static F lambdaFactory$(F var0, Object var1) {
      return new IOFunctions$$Lambda$25(var0, var1);
   }
}
