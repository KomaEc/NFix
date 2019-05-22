package fj;

public class F4Functions {
   public static <A, B, C, D, E> F3<B, C, D, E> f(F4<A, B, C, D, E> f, A a) {
      return F4Functions$$Lambda$1.lambdaFactory$(f, a);
   }

   // $FF: synthetic method
   private static Object lambda$f$63(F4 var0, Object var1, Object b, Object c, Object d) {
      return var0.f(var1, b, c, d);
   }

   // $FF: synthetic method
   static Object access$lambda$0(F4 var0, Object var1, Object var2, Object var3, Object var4) {
      return lambda$f$63(var0, var1, var2, var3, var4);
   }
}
