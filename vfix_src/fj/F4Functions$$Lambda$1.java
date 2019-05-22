package fj;

// $FF: synthetic class
final class F4Functions$$Lambda$1 implements F3 {
   private final F4 arg$1;
   private final Object arg$2;

   private F4Functions$$Lambda$1(F4 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F3 get$Lambda(F4 var0, Object var1) {
      return new F4Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2, Object var3) {
      return F4Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2, var3);
   }

   public static F3 lambdaFactory$(F4 var0, Object var1) {
      return new F4Functions$$Lambda$1(var0, var1);
   }
}
