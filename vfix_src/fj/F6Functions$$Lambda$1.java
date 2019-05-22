package fj;

// $FF: synthetic class
final class F6Functions$$Lambda$1 implements F5 {
   private final F6 arg$1;
   private final Object arg$2;

   private F6Functions$$Lambda$1(F6 var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F5 get$Lambda(F6 var0, Object var1) {
      return new F6Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return F6Functions.access$lambda$0(this.arg$1, this.arg$2, var1, var2, var3, var4, var5);
   }

   public static F5 lambdaFactory$(F6 var0, Object var1) {
      return new F6Functions$$Lambda$1(var0, var1);
   }
}
