package fj.data.fingertrees;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class FingerTree$$Lambda$1 implements F2 {
   private final F arg$1;

   private FingerTree$$Lambda$1(F var1) {
      this.arg$1 = var1;
   }

   private static F2 get$Lambda(F var0) {
      return new FingerTree$$Lambda$1(var0);
   }

   public Object f(Object var1, Object var2) {
      return FingerTree.access$lambda$0(this.arg$1, (FingerTree)var1, var2);
   }

   public static F2 lambdaFactory$(F var0) {
      return new FingerTree$$Lambda$1(var0);
   }
}
