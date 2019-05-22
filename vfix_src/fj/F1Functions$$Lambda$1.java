package fj;

// $FF: synthetic class
final class F1Functions$$Lambda$1 implements F {
   private final F arg$1;
   private final Object arg$2;

   private F1Functions$$Lambda$1(F var1, Object var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(F var0, Object var1) {
      return new F1Functions$$Lambda$1(var0, var1);
   }

   public Object f(Object var1) {
      return F1Functions.access$lambda$0(this.arg$1, this.arg$2, (Unit)var1);
   }

   public static F lambdaFactory$(F var0, Object var1) {
      return new F1Functions$$Lambda$1(var0, var1);
   }
}
