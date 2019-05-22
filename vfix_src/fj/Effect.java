package fj;

import fj.function.Effect0;
import fj.function.Effect1;
import fj.function.Effect2;
import fj.function.Effect3;
import fj.function.Effect4;
import fj.function.Effect5;
import fj.function.Effect6;
import fj.function.Effect7;
import fj.function.Effect8;

public class Effect {
   private Effect() {
   }

   public static P1<Unit> f(Effect0 e) {
      return P.lazy(Effect$$Lambda$1.lambdaFactory$());
   }

   public static final <A> F<A, Unit> f(Effect1<A> e1) {
      return Effect$$Lambda$2.lambdaFactory$(e1);
   }

   public static <A, B> F2<A, B, Unit> f(Effect2<A, B> e) {
      return Effect$$Lambda$3.lambdaFactory$(e);
   }

   public static <A, B, C> F3<A, B, C, Unit> f(Effect3<A, B, C> e) {
      return Effect$$Lambda$4.lambdaFactory$(e);
   }

   public static <A, B, C, D> F4<A, B, C, D, Unit> f(Effect4<A, B, C, D> e) {
      return Effect$$Lambda$5.lambdaFactory$(e);
   }

   public static <A, B, C, D, E> F5<A, B, C, D, E, Unit> f(Effect5<A, B, C, D, E> z) {
      return Effect$$Lambda$6.lambdaFactory$(z);
   }

   public static <A, B, C, D, E, $F> F6<A, B, C, D, E, $F, Unit> f(Effect6<A, B, C, D, E, $F> z) {
      return Effect$$Lambda$7.lambdaFactory$(z);
   }

   public static <A, B, C, D, E, $F, G> F7<A, B, C, D, E, $F, G, Unit> f(Effect7<A, B, C, D, E, $F, G> z) {
      return Effect$$Lambda$8.lambdaFactory$(z);
   }

   public static <A, B, C, D, E, $F, G, H> F8<A, B, C, D, E, $F, G, H, Unit> f(Effect8<A, B, C, D, E, $F, G, H> z) {
      return Effect$$Lambda$9.lambdaFactory$(z);
   }

   public final <A, B> Effect1<B> comap(final Effect1<A> e1, final F<B, A> f) {
      return new Effect1<B>() {
         public void f(B b) {
            e1.f(f.f(b));
         }
      };
   }

   public static <A> Effect1<A> lazy(final F<A, Unit> f) {
      return new Effect1<A>() {
         public void f(A a) {
            f.f(a);
         }
      };
   }

   // $FF: synthetic method
   private static Unit lambda$f$60(Effect8 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
      var0.f(a, b, c, d, e, f, g, h);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$59(Effect7 var0, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
      var0.f(a, b, c, d, e, f, g);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$58(Effect6 var0, Object a, Object b, Object c, Object d, Object e, Object f) {
      var0.f(a, b, c, d, e, f);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$57(Effect5 var0, Object a, Object b, Object c, Object d, Object e) {
      var0.f(a, b, c, d, e);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$56(Effect4 var0, Object a, Object b, Object c, Object d) {
      var0.f(a, b, c, d);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$55(Effect3 var0, Object a, Object b, Object c) {
      var0.f(a, b, c);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$54(Effect2 var0, Object a, Object b) {
      var0.f(a, b);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$53(Effect1 var0, Object a) {
      var0.f(a);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static Unit lambda$f$52(Unit u) {
      return Unit.unit();
   }

   // $FF: synthetic method
   static Unit access$lambda$0(Unit var0) {
      return lambda$f$52(var0);
   }

   // $FF: synthetic method
   static Unit access$lambda$1(Effect1 var0, Object var1) {
      return lambda$f$53(var0, var1);
   }

   // $FF: synthetic method
   static Unit access$lambda$2(Effect2 var0, Object var1, Object var2) {
      return lambda$f$54(var0, var1, var2);
   }

   // $FF: synthetic method
   static Unit access$lambda$3(Effect3 var0, Object var1, Object var2, Object var3) {
      return lambda$f$55(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   static Unit access$lambda$4(Effect4 var0, Object var1, Object var2, Object var3, Object var4) {
      return lambda$f$56(var0, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   static Unit access$lambda$5(Effect5 var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return lambda$f$57(var0, var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   static Unit access$lambda$6(Effect6 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return lambda$f$58(var0, var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   static Unit access$lambda$7(Effect7 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return lambda$f$59(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   // $FF: synthetic method
   static Unit access$lambda$8(Effect8 var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return lambda$f$60(var0, var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
