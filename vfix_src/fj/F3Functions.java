package fj;

public class F3Functions {
   public static <A, B, C, D> F2<B, C, D> f(F3<A, B, C, D> f, A a) {
      return F3Functions$$Lambda$1.lambdaFactory$(f, a);
   }

   // $FF: synthetic method
   private static Object lambda$f$62(F3 var0, Object var1, Object b, Object c) {
      return var0.f(var1, b, c);
   }

   // $FF: synthetic method
   static Object access$lambda$0(F3 var0, Object var1, Object var2, Object var3) {
      return lambda$f$62(var0, var1, var2, var3);
   }
}
