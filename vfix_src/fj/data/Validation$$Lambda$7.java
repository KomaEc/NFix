package fj.data;

import fj.F;

// $FF: synthetic class
final class Validation$$Lambda$7 implements F {
   private final Validation arg$1;

   private Validation$$Lambda$7(Validation var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Validation var0) {
      return new Validation$$Lambda$7(var0);
   }

   public Object f(Object var1) {
      return Validation.access$lambda$6(this.arg$1, (List)var1);
   }

   public static F lambdaFactory$(Validation var0) {
      return new Validation$$Lambda$7(var0);
   }
}
