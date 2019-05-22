package fj;

// $FF: synthetic class
final class F3Functions$$Lambda$1 implements F2 {
   private final F3 arg$1;
   private final Object arg$2;

   private F3Functions$$Lambda$1(F3 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F2 get$Lambda(F3 var0, Object var1) {
      return new F3Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2) {
      return F3Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2);
   }

   public static F2 lambdaFactory$(F3 var0, Object var1) {
      return new F3Functions$$Lambda$1(var0, var1);
   }
}
