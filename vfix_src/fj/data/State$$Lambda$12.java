package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class State$$Lambda$12 implements F2 {
   private final F arg$1;

   private State$$Lambda$12(F var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(F var0) {
      return new State$$Lambda$12(var0);
   }

   public Object f(Object var1, Object var2) {
      return State.access$lambda$11(this.arg$1, (State)var1, var2);
   }

   public static F2 lambdaFactory$(F var0) {
      return new State$$Lambda$12(var0);
   }
}
