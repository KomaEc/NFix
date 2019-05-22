package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$2 implements F {
   private final Object arg$1;

   private State$$Lambda$2(Object var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(Object var0) {
      return new State$$Lambda$2(var0);
   }

   public Object f(Object var1) {
      return State.access$lambda$1(this.arg$1, var1);
   }

   public static F lambdaFactory$(Object var0) {
      return new State$$Lambda$2(var0);
   }
}
