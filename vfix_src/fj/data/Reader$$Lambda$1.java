package fj.data;

import fj.F;

// $FF: synthetic class
final class Reader$$Lambda$1 implements F {
   private final Object arg$1;

   private Reader$$Lambda$1(Object var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Object var0) {
      return new Reader$$Lambda$1(var0);
   }

   public Object f(Object var1) {
      return Reader.access$lambda$0(this.arg$1, var1);
   }

   public static F lambdaFactory$(Object var0) {
      return new Reader$$Lambda$1(var0);
   }
}
