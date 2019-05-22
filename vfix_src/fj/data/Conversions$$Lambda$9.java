package fj.data;

import fj.F;
import fj.Unit;
import fj.function.Try0;

// $FF: synthetic class
final class Conversions$$Lambda$9 implements F {
   private final Try0 arg$1;

   private Conversions$$Lambda$9(Try0 var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Try0 var0) {
      return new Conversions$$Lambda$9(var0);
   }

   public Object f(Object var1) {
      return Conversions.access$lambda$8(this.arg$1, (Unit)var1);
   }

   public static F lambdaFactory$(Try0 var0) {
      return new Conversions$$Lambda$9(var0);
   }
}
