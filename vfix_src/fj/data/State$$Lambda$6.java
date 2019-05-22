package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$6 implements F {
   private final State arg$1;
   private final F arg$2;

   private State$$Lambda$6(State var1, F var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(State var0, F var1) {
      return new State$$Lambda$6(var0, var1);
   }

   public Object f(Object var1) {
      return State.access$lambda$5(this.arg$1, this.arg$2, var1);
   }

   public static F lambdaFactory$(State var0, F var1) {
      return new State$$Lambda$6(var0, var1);
   }
}
