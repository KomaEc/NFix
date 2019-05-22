package fj;

// $FF: synthetic class
final class F7Functions$$Lambda$1 implements F6 {
   private final F7 arg$1;
   private final Object arg$2;

   private F7Functions$$Lambda$1(F7 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F6 get$Lambda(F7 var0, Object var1) {
      return new F7Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return F7Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2, var3, var4, var5, var6);
   }

   public static F6 lambdaFactory$(F7 var0, Object var1) {
      return new F7Functions$$Lambda$1(var0, var1);
   }
}
