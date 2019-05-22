package fj.test;

import fj.F;
import fj.F2;
import fj.data.State;

// $FF: synthetic class
final class Coarbitrary$28$$Lambda$1 implements F {
   private final State arg$1;
   private final F2 arg$2;
   private final Gen arg$3;

   private Coarbitrary$28$$Lambda$1(State var1, F2 var2, Gen var3) {
      this.arg$1 = var1;
      this.arg$2 = var2;
      this.arg$3 = var3;
   }

   private static F get$Lambda(State var0, F2 var1, Gen var2) {
      return new Coarbitrary$28$$Lambda$1(var0, var1, var2);
   }

   public Object f(Object var1) {
      return null.access$lambda$0(this.arg$1, this.arg$2, this.arg$3, var1);
   }

   public static F lambdaFactory$(State var0, F2 var1, Gen var2) {
      return new Coarbitrary$28$$Lambda$1(var0, var1, var2);
   }
}
