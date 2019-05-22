package fj;

// $FF: synthetic class
final class F2Functions$$Lambda$3 implements F2 {
   private final F arg$1;
   private final F2 arg$2;

   private F2Functions$$Lambda$3(F var1, F2 var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F2 get$Lambda(F var0, F2 var1) {
      return new F2Functions$$Lambda$3(var0, var1);
   }

   public Object f(Object var1, Object var2) {
      return F2Functions.access$lambda$2(this.arg$1, this.arg$2, var1, var2);
   }

   public static F2 lambdaFactory$(F var0, F2 var1) {
      return new F2Functions$$Lambda$3(var0, var1);
   }
}
