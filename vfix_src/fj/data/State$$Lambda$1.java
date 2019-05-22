package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$1 implements F {
   private final F arg$1;

   private State$$Lambda$1(F var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(F var0) {
      return new State$$Lambda$1(var0);
   }

   public Object f(Object var1) {
      return State.access$lambda$0(this.arg$1, var1);
   }

   public static F lambdaFactory$(F var0) {
      return new State$$Lambda$1(var0);
   }
}
