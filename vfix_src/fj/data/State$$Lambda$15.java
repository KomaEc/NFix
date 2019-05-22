package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$15 implements F {
   private final State arg$1;

   private State$$Lambda$15(State var1) {
      this.arg$1 = var1;
   }

   private static F get$Lambda(State var0) {
      return new State$$Lambda$15(var0);
   }

   public Object f(Object var1) {
      return State.access$lambda$14(this.arg$1, (List)var1);
   }

   public static F lambdaFactory$(State var0) {
      return new State$$Lambda$15(var0);
   }
}
