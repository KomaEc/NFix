package fj;

public abstract class P8<A, B, C, D, E, F, G, H> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public abstract D _4();

   public abstract E _5();

   public abstract F _6();

   public abstract G _7();

   public abstract H _8();

   public final <X> P8<X, B, C, D, E, F, G, H> map1(final F<A, X> f) {
      return new P8<X, B, C, D, E, F, G, H>() {
         public X _1() {
            return f.f(P8.this._1());
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, X, C, D, E, F, G, H> map2(final F<B, X> f) {
      return new P8<A, X, C, D, E, F, G, H>() {
         public A _1() {
            return P8.this._1();
         }

         public X _2() {
            return f.f(P8.this._2());
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, X, D, E, F, G, H> map3(final F<C, X> f) {
      return new P8<A, B, X, D, E, F, G, H>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public X _3() {
            return f.f(P8.this._3());
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, C, X, E, F, G, H> map4(final F<D, X> f) {
      return new P8<A, B, C, X, E, F, G, H>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public X _4() {
            return f.f(P8.this._4());
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, C, D, X, F, G, H> map5(final F<E, X> f) {
      return new P8<A, B, C, D, X, F, G, H>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public X _5() {
            return f.f(P8.this._5());
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, C, D, E, X, G, H> map6(final F<F, X> f) {
      return new P8<A, B, C, D, E, X, G, H>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public X _6() {
            return f.f(P8.this._6());
         }

         public G _7() {
            return P8.this._7();
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, C, D, E, F, X, H> map7(final F<G, X> f) {
      return new P8<A, B, C, D, E, F, X, H>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public X _7() {
            return f.f(P8.this._7());
         }

         public H _8() {
            return P8.this._8();
         }
      };
   }

   public final <X> P8<A, B, C, D, E, F, G, X> map8(final F<H, X> f) {
      return new P8<A, B, C, D, E, F, G, X>() {
         public A _1() {
            return P8.this._1();
         }

         public B _2() {
            return P8.this._2();
         }

         public C _3() {
            return P8.this._3();
         }

         public D _4() {
            return P8.this._4();
         }

         public E _5() {
            return P8.this._5();
         }

         public F _6() {
            return P8.this._6();
         }

         public G _7() {
            return P8.this._7();
         }

         public X _8() {
            return f.f(P8.this._8());
         }
      };
   }

   public final P1<A> _1_() {
      return (P1)F1Functions.lazy(__1()).f(this);
   }

   public final P1<B> _2_() {
      return (P1)F1Functions.lazy(__2()).f(this);
   }

   public final P1<C> _3_() {
      return (P1)F1Functions.lazy(__3()).f(this);
   }

   public final P1<D> _4_() {
      return (P1)F1Functions.lazy(__4()).f(this);
   }

   public final P1<E> _5_() {
      return (P1)F1Functions.lazy(__5()).f(this);
   }

   public final P1<F> _6_() {
      return (P1)F1Functions.lazy(__6()).f(this);
   }

   public final P1<G> _7_() {
      return (P1)F1Functions.lazy(__7()).f(this);
   }

   public final P1<H> _8_() {
      return (P1)F1Functions.lazy(__8()).f(this);
   }

   public final P8<A, B, C, D, E, F, G, H> memo() {
      return new P8<A, B, C, D, E, F, G, H>() {
         private final P1<A> a = P1.memo(P8$9$$Lambda$1.lambdaFactory$(P8.this));
         private final P1<B> b = P1.memo(P8$9$$Lambda$2.lambdaFactory$(P8.this));
         private final P1<C> c = P1.memo(P8$9$$Lambda$3.lambdaFactory$(P8.this));
         private final P1<D> d = P1.memo(P8$9$$Lambda$4.lambdaFactory$(P8.this));
         private final P1<E> e = P1.memo(P8$9$$Lambda$5.lambdaFactory$(P8.this));
         private final P1<F> f = P1.memo(P8$9$$Lambda$6.lambdaFactory$(P8.this));
         private final P1<G> g = P1.memo(P8$9$$Lambda$7.lambdaFactory$(P8.this));
         private final P1<H> h = P1.memo(P8$9$$Lambda$8.lambdaFactory$(P8.this));

         public A _1() {
            return this.a._1();
         }

         public B _2() {
            return this.b._1();
         }

         public C _3() {
            return this.c._1();
         }

         public D _4() {
            return this.d._1();
         }

         public E _5() {
            return this.e._1();
         }

         public F _6() {
            return this.f._1();
         }

         public G _7() {
            return this.g._1();
         }

         public H _8() {
            return this.h._1();
         }

         // $FF: synthetic method
         private static Object lambda$$34(P8 var0, Unit u) {
            return var0._8();
         }

         // $FF: synthetic method
         private static Object lambda$$33(P8 var0, Unit u) {
            return var0._7();
         }

         // $FF: synthetic method
         private static Object lambda$$32(P8 var0, Unit u) {
            return var0._6();
         }

         // $FF: synthetic method
         private static Object lambda$$31(P8 var0, Unit u) {
            return var0._5();
         }

         // $FF: synthetic method
         private static Object lambda$$30(P8 var0, Unit u) {
            return var0._4();
         }

         // $FF: synthetic method
         private static Object lambda$$29(P8 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$28(P8 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$27(P8 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P8 var0, Unit var1) {
            return lambda$$27(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P8 var0, Unit var1) {
            return lambda$$28(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P8 var0, Unit var1) {
            return lambda$$29(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$3(P8 var0, Unit var1) {
            return lambda$$30(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$4(P8 var0, Unit var1) {
            return lambda$$31(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$5(P8 var0, Unit var1) {
            return lambda$$32(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$6(P8 var0, Unit var1) {
            return lambda$$33(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$7(P8 var0, Unit var1) {
            return lambda$$34(var0, var1);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, A> __1() {
      return new F<P8<A, B, C, D, E, F$, G, H>, A>() {
         public A f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, B> __2() {
      return new F<P8<A, B, C, D, E, F$, G, H>, B>() {
         public B f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, C> __3() {
      return new F<P8<A, B, C, D, E, F$, G, H>, C>() {
         public C f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._3();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, D> __4() {
      return new F<P8<A, B, C, D, E, F$, G, H>, D>() {
         public D f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._4();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, E> __5() {
      return new F<P8<A, B, C, D, E, F$, G, H>, E>() {
         public E f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._5();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, F$> __6() {
      return new F<P8<A, B, C, D, E, F$, G, H>, F$>() {
         public F$ f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._6();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, G> __7() {
      return new F<P8<A, B, C, D, E, F$, G, H>, G>() {
         public G f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._7();
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, H> __8() {
      return new F<P8<A, B, C, D, E, F$, G, H>, H>() {
         public H f(P8<A, B, C, D, E, F$, G, H> p) {
            return p._8();
         }
      };
   }

   public String toString() {
      return Show.p8Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
