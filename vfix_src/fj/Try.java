package fj;

import fj.data.IO;
import fj.data.IOFunctions;
import fj.data.Validation;
import fj.function.Try0;
import fj.function.Try1;
import fj.function.Try2;
import fj.function.Try3;
import fj.function.Try4;
import fj.function.Try5;
import fj.function.Try6;
import fj.function.Try7;
import fj.function.Try8;
import java.io.IOException;

public class Try {
   public static <A, E extends Exception> P1<Validation<E, A>> f(Try0<A, E> t) {
      return P.lazy(Try$$Lambda$1.lambdaFactory$(t));
   }

   public static <A, B, E extends Exception> F<A, Validation<E, B>> f(Try1<A, B, E> t) {
      return Try$$Lambda$2.lambdaFactory$(t);
   }

   public static <A, B, C, E extends Exception> F2<A, B, Validation<E, C>> f(Try2<A, B, C, E> t) {
      return Try$$Lambda$3.lambdaFactory$(t);
   }

   public static <A, B, C, D, E extends Exception> F3<A, B, C, Validation<E, D>> f(Try3<A, B, C, D, E> t) {
      return Try$$Lambda$4.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, Z extends Exception> F4<A, B, C, D, Validation<Z, E>> f(Try4<A, B, C, D, E, Z> t) {
      return Try$$Lambda$5.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, F, Z extends Exception> F5<A, B, C, D, E, Validation<Z, F>> f(Try5<A, B, C, D, E, F, Z> t) {
      return Try$$Lambda$6.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, F, G, Z extends Exception> F6<A, B, C, D, E, F, Validation<Z, G>> f(Try6<A, B, C, D, E, F, G, Z> t) {
      return Try$$Lambda$7.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, F, G, H, Z extends Exception> F7<A, B, C, D, E, F, G, Validation<Z, H>> f(Try7<A, B, C, D, E, F, G, H, Z> t) {
      return Try$$Lambda$8.lambdaFactory$(t);
   }

   public static <A, B, C, D, E, F, G, H, I, Z extends Exception> F8<A, B, C, D, E, F, G, H, Validation<Z, I>> f(Try8<A, B, C, D, E, F, G, H, I, Z> t) {
      return Try$$Lambda$9.lambdaFactory$(t);
   }

   public static <A> IO<A> io(Try0<A, ? extends IOException> t) {
      return IOFunctions.io(t);
   }

   // $FF: synthetic method
   private static Validation lambda$f$78(Try8 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
      try {
         return Validation.success(var0.f(a, b, c, d, e, f, g, h));
      } catch (Exception var10) {
         return Validation.fail(var10);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$77(Try7 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
      try {
         return Validation.success(var0.f(a, b, c, d, e, f, g));
      } catch (Exception var9) {
         return Validation.fail(var9);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$76(Try6 var0, Object a, Object b, Object c, Object d, Object e, Object f) {
      try {
         return Validation.success(var0.f(a, b, c, d, e, f));
      } catch (Exception var8) {
         return Validation.fail(var8);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$75(Try5 var0, Object a, Object b, Object c, Object d, Object e) {
      try {
         return Validation.success(var0.f(a, b, c, d, e));
      } catch (Exception var7) {
         return Validation.fail(var7);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$74(Try4 var0, Object a, Object b, Object c, Object d) {
      try {
         return Validation.success(var0.f(a, b, c, d));
      } catch (Exception var6) {
         return Validation.fail(var6);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$73(Try3 var0, Object a, Object b, Object c) {
      try {
         return Validation.success(var0.f(a, b, c));
      } catch (Exception var5) {
         return Validation.fail(var5);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$72(Try2 var0, Object a, Object b) {
      try {
         return Validation.success(var0.f(a, b));
      } catch (Exception var4) {
         return Validation.fail(var4);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$71(Try1 var0, Object a) {
      try {
         return Validation.success(var0.f(a));
      } catch (Exception var3) {
         return Validation.fail(var3);
      }
   }

   // $FF: synthetic method
   private static Validation lambda$f$70(Try0 var0, Unit u) {
      try {
         return Validation.success(var0.f());
      } catch (Exception var3) {
         return Validation.fail(var3);
      }
   }

   // $FF: synthetic method
   static Validation access$lambda$0(Try0 var0, Unit var1) {
      return lambda$f$70(var0, var1);
   }

   // $FF: synthetic method
   static Validation access$lambda$1(Try1 var0, Object var1) {
      return lambda$f$71(var0, var1);
   }

   // $FF: synthetic method
   static Validation access$lambda$2(Try2 var0, Object var1, Object var2) {
      return lambda$f$72(var0, var1, var2);
   }

   // $FF: synthetic method
   static Validation access$lambda$3(Try3 var0, Object var1, Object var2, Object var3) {
      return lambda$f$73(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   static Validation access$lambda$4(Try4 var0, Object var1, Object var2, Object var3, Object var4) {
      return lambda$f$74(var0, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   static Validation access$lambda$5(Try5 var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return lambda$f$75(var0, var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   static Validation access$lambda$6(Try6 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return lambda$f$76(var0, var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   static Validation access$lambda$7(Try7 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return lambda$f$77(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   // $FF: synthetic method
   static Validation access$lambda$8(Try8 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return lambda$f$78(var0, var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
