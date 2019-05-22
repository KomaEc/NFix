package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class List$$Lambda$2 implements F {
   private final F arg$1;
   private final F arg$2;
   private final F2 arg$3;
   private final Object arg$4;

   private List$$Lambda$2(F var1, F var2, F2 var3, Object var4) {
      this.arg$1 = var1;
      this.arg$2 = var2;
      this.arg$3 = var3;
      this.arg$4 = var4;
   }

   private static F get$Lambda(F var0, F var1, F2 var2, Object var3) {
      return new List$$Lambda$2(var0, var1, var2, var3);
   }

   public Object f(Object var1) {
      return List.access$lambda$1(this.arg$1, this.arg$2, this.arg$3, this.arg$4, (TreeMap)var1);
   }

   public static F lambdaFactory$(F var0, F var1, F2 var2, Object var3) {
      return new List$$Lambda$2(var0, var1, var2, var3);
   }
}
