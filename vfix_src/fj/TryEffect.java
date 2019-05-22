package fj;

import fj.data.Validation;
import fj.function.TryEffect0;
import fj.function.TryEffect1;
import fj.function.TryEffect2;
import fj.function.TryEffect3;
import fj.function.TryEffect4;
import fj.function.TryEffect5;
import fj.function.TryEffect6;
import fj.function.TryEffect7;
import fj.function.TryEffect8;

public class TryEffect {
   private TryEffect() {
   }

   public static <A, Z extends Exception> P1<Validation<Z, Unit>> f(TryEffect0<Z> t) {
      return P.lazy(TryEffect$$Lambda$1.lambdaFactory$(t));
   }

   public static <A, Z extends Exception> F<A, Validation<Z, Unit>> f(TryEffect1<A, Z> t) {
      return TryEffect$$Lambda$2.lambdaFactory$(t);
   }

   public static <A, B, Z extends Exception> F2<A, B, Validation<Z, Unit>> f(TryEffect2<A, B, Z> t) {
      return TryEffect$$Lambda$3.lambdaFactory$(t);
   }

   public static <A, B, C, Z extends Exception> F3<A, B, C, Validation<Z, Unit>> f(TryEffect3<A, B, C, Z> t) {
      return TryEffect$$Lambda$4.lambdaFactory$(t);
   }

   public static <A, B, C, D, Z extends Exception> F4<A, B, C, D, Validation<Z, Unit>> f(TryEffect4<A, B, C, D, Z> t) {
      return TryEffect$$Lambda$5.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, Z extends Exception> F5<A, B, C, D, E, Validation<Z, Unit>> f(TryEffect5<A, B, C, D, E, Z> t) {
      return TryEffect$$Lambda$6.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, $F, Z extends Exception> F6<A, B, C, D, E, $F, Validation<Z, Unit>> f(TryEffect6<A, B, C, D, E, $F, Z> t) {
      return TryEffect$$Lambda$7.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, $F, G, Z extends Exception> F7<A, B, C, D, E, $F, G, Validation<Z, Unit>> f(TryEffect7<A, B, C, D, E, $F, G, Z> t) {
      return TryEffect$$Lambda$8.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, $F, G, H, Z extends Exception> F8<A, B, C, D, E, $F, G, H, Validation<Z, Unit>> f(TryEffect8<A, B, C, D, E, $F, G, H, Z> t) {
      return TryEffect$$Lambda$9.lambdaFactory$(t);
   }

   // $FF: synthetic method
   private static Validation lambda$f$87(TryEffect8 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
      try {
         var0.f(a, b, c, d, e, f, g, h);
         return Validation.success(Unit.unit());
      } catch (Exception var10) {
         return Validation.fail(var10);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$86(TryEffect7 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
      try {
         var0.f(a, b, c, d, e, f, g);
         return Validation.success(Unit.unit());
      } catch (Exception var9) {
         return Validation.fail(var9);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$85(TryEffect6 var0, Object a, Object b, Object c, Object d, Object e, Object f) {
      try {
         var0.f(a, b, c, d, e, f);
         return Validation.success(Unit.unit());
      } catch (Exception var8) {
         return Validation.fail(var8);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$84(TryEffect5 var0, Object a, Object b, Object c, Object d, Object e) {
      try {
         var0.f(a, b, c, d, e);
         return Validation.success(Unit.unit());
      } catch (Exception var7) {
         return Validation.fail(var7);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$83(TryEffect4 var0, Object a, Object b, Object c, Object d) {
      try {
         var0.f(a, b, c, d);
         return Validation.success(Unit.unit());
      } catch (Exception var6) {
         return Validation.fail(var6);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$82(TryEffect3 var0, Object a, Object b, Object c) {
      try {
         var0.f(a, b, c);
         return Validation.success(Unit.unit());
      } catch (Exception var5) {
         return Validation.fail(var5);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$81(TryEffect2 var0, Object a, Object b) {
      try {
         var0.f(a, b);
         return Validation.success(Unit.unit());
      } catch (Exception var4) {
         return Validation.fail(var4);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$80(TryEffect1 var0, Object a) {
      try {
         var0.f(a);
         return Validation.success(Unit.unit());
      } catch (Exception var3) {
         return Validation.fail(var3);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$79(TryEffect0 var0, Unit u) {
      try {
         var0.f();
         return Validation.success(Unit.unit());
      } catch (Exception var3) {
         return Validation.fail(var3);
      }
   }

   // $FF: synthetic method
   static Validation access$lambda$0(TryEffect0 var0, Unit var1) {
      return lambda$f$79(var0, var1);
   }

   // $FF: synthetic method
   static Validation access$lambda$1(TryEffect1 var0, Object var1) {
      return lambda$f$80(var0, var1);
   }

   // $FF: synthetic method
   static Validation access$lambda$2(TryEffect2 var0, Object var1, Object var2) {
      return lambda$f$81(var0, var1, var2);
   }

   // $FF: synthetic method
   static Validation access$lambda$3(TryEffect3 var0, Object var1, Object var2, Object var3) {
      return lambda$f$82(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   static Validation access$lambda$4(TryEffect4 var0, Object var1, Object var2, Object var3, Object var4) {
      return lambda$f$83(var0, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   static Validation access$lambda$5(TryEffect5 var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return lambda$f$84(var0, var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   static Validation access$lambda$6(TryEffect6 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return lambda$f$85(var0, var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   static Validation access$lambda$7(TryEffect7 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return lambda$f$86(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   // $FF: synthetic method
   static Validation access$lambda$8(TryEffect8 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return lambda$f$87(var0, var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
