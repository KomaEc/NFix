package fj.data;

import fj.F;
import fj.F2;

// $FF: synthetic class
final class List$$Lambda$4 implements F {
   private final F arg$1;
   private final F arg$2;
   private final TreeMap arg$3;
   private final F2 arg$4;
   private final Object arg$5;

   private List$$Lambda$4(F var1, F var2, TreeMap var3, F2 var4, Object var5) {
      this.arg$1 = var1;
      this.arg$2 = var2;
      this.arg$3 = var3;
      this.arg$4 = var4;
      this.arg$5 = var5;
   }

   private static F get$Lambda(F var0, F var1, TreeMap var2, F2 var3, Object var4) {
      return new List$$Lambda$4(var0, var1, var2, var3, var4);
   }

   public Object f(Object var1) {
      return List.access$lambda$3(this.arg$1, this.arg$2, this.arg$3, this.arg$4, this.arg$5, var1);
   }

   public static F lambdaFactory$(F var0, F var1, TreeMap var2, F2 var3, Object var4) {
      return new List$$Lambda$4(var0, var1, var2, var3, var4);
   }
}
