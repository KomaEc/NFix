package fj;

public class F6Functions {
   public static <A, B, C, D, E, F$, G> F5<B, C, D, E, F$, G> f(F6<A, B, C, D, E, F$, G> func, A a) {
      return F6Functions$$Lambda$1.lambdaFactory$(func, a);
   }

   // $FF: synthetic method
   private static Object lambda$f$65(F6 var0, Object var1, Object b, Object c, Object d, Object e, Object f) {
      return var0.f(var1, b, c, d, e, f);
   }

   // $FF: synthetic method
   static Object access$lambda$0(F6 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return lambda$f$65(var0, var1, var2, var3, var4, var5, var6);
   }
}
