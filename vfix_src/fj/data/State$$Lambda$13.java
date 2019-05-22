package fj.data;

import fj.F;

// $FF: synthetic class
final class State$$Lambda$13 implements F {
   private final F arg$1;
   private final Object arg$2;

   private State$$Lambda$13(F var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(F var0, Object var1) {
      return new State$$Lambda$13(var0, var1);
   }

   public Object f(Object var1) {
      return State.access$lambda$12(this.arg$1, this.arg$2, (List)var1);
   }

   public static F lambdaFactory$(F var0, Object var1) {
      return new State$$Lambda$13(var0, var1);
   }
}
