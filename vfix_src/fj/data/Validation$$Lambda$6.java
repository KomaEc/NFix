package fj.data;

import fj.F;

// $FF: synthetic class
final class Validation$$Lambda$6 implements F {
   private final Validation arg$1;

   private Validation$$Lambda$6(Validation var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Validation var0) {
      return new Validation$$Lambda$6(var0);
   }

   public Object f(Object var1) {
      return Validation.access$lambda$5(this.arg$1, (List)var1);
   }

   public static F lambdaFactory$(Validation var0) {
      return new Validation$$Lambda$6(var0);
   }
}
