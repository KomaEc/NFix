package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$16 implements F {
   private final List arg$1;

   private State$$Lambda$16(List var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(List var0) {
      return new State$$Lambda$16(var0);
   }

   public Object f(Object var1) {
      return State.access$lambda$15(this.arg$1, var1);
   }

   public static F lambdaFactory$(List var0) {
      return new State$$Lambda$16(var0);
   }
}
