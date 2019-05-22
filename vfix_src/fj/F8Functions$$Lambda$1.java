package fj;

// $FF: synthetic class
final class F8Functions$$Lambda$1 implements F7 {
   private final F8 arg$1;
   private final Object arg$2;

   private F8Functions$$Lambda$1(F8 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F7 get$Lambda(F8 var0, Object var1) {
      return new F8Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return F8Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2, var3, var4, var5, var6, var7);
   }

   public static F7 lambdaFactory$(F8 var0, Object var1) {
      return new F8Functions$$Lambda$1(var0, var1);
   }
}
