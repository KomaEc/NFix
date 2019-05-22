package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$8 implements F {
   private final State arg$1;

   private State$$Lambda$8(State var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(State var0) {
      return new State$$Lambda$8(var0);
   }

   public Object f(Object var1) {
      return State.access$lambda$7(this.arg$1, var1);
   }

   public static F lambdaFactory$(State var0) {
      return new State$$Lambda$8(var0);
   }
}
