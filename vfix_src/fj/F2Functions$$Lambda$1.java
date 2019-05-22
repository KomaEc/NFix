package fj;

// $FF: synthetic class
final class F2Functions$$Lambda$1 implements F2 {
   private final F2 arg$1;
   private final F arg$2;

   private F2Functions$$Lambda$1(F2 var1, F var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F2 get$Lambda(F2 var0, F var1) {
      return new F2Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2) {
      return F2Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2);
   }

   public static F2 lambdaFactory$(F2 var0, F var1) {
      return new F2Functions$$Lambda$1(var0, var1);
   }
}
