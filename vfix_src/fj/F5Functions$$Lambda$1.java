package fj;

// $FF: synthetic class
final class F5Functions$$Lambda$1 implements F4 {
   private final F5 arg$1;
   private final Object arg$2;

   private F5Functions$$Lambda$1(F5 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F4 get$Lambda(F5 var0, Object var1) {
      return new F5Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4) {
      return F5Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2, var3, var4);
   }

   public static F4 lambdaFactory$(F5 var0, Object var1) {
      return new F5Functions$$Lambda$1(var0, var1);
   }
}
