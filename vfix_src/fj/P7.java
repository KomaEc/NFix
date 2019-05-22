package fj;

public abstract class P7<A, B, C, D, E, F, G> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public abstract D _4();

   public abstract E _5();

   public abstract F _6();

   public abstract G _7();

   public final <X> P7<X, B, C, D, E, F, G> map1(final F<A, X> f) {
      return new P7<X, B, C, D, E, F, G>() {
         public X _1() {
            return f.f(P7.this._1());
         }

         public B _2() {
            return P7.this._2();
         }

         public C _3() {
            return P7.this._3();
         }

         public D _4() {
            return P7.this._4();
         }

         public E _5() {
            return P7.this._5();
         }

         public F _6() {
            return P7.this._6();
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, X, C, D, E, F, G> map2(final F<B, X> f) {
      return new P7<A, X, C, D, E, F, G>() {
         public A _1() {
            return P7.this._1();
         }

         public X _2() {
            return f.f(P7.this._2());
         }

         public C _3() {
            return P7.this._3();
         }

         public D _4() {
            return P7.this._4();
         }

         public E _5() {
            return P7.this._5();
         }

         public F _6() {
            return P7.this._6();
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, B, X, D, E, F, G> map3(final F<C, X> f) {
      return new P7<A, B, X, D, E, F, G>() {
         public A _1() {
            return P7.this._1();
         }

         public B _2() {
            return P7.this._2();
         }

         public X _3() {
            return f.f(P7.this._3());
         }

         public D _4() {
            return P7.this._4();
         }

         public E _5() {
            return P7.this._5();
         }

         public F _6() {
            return P7.this._6();
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, B, C, X, E, F, G> map4(final F<D, X> f) {
      return new P7<A, B, C, X, E, F, G>() {
         public A _1() {
            return P7.this._1();
         }

         public B _2() {
            return P7.this._2();
         }

         public C _3() {
            return P7.this._3();
         }

         public X _4() {
            return f.f(P7.this._4());
         }

         public E _5() {
            return P7.this._5();
         }

         public F _6() {
            return P7.this._6();
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, B, C, D, X, F, G> map5(final F<E, X> f) {
      return new P7<A, B, C, D, X, F, G>() {
         public A _1() {
            return P7.this._1();
         }

         public B _2() {
            return P7.this._2();
         }

         public C _3() {
            return P7.this._3();
         }

         public D _4() {
            return P7.this._4();
         }

         public X _5() {
            return f.f(P7.this._5());
         }

         public F _6() {
            return P7.this._6();
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, B, C, D, E, X, G> map6(final F<F, X> f) {
      return new P7<A, B, C, D, E, X, G>() {
         public A _1() {
            return P7.this._1();
         }

         public B _2() {
            return P7.this._2();
         }

         public C _3() {
            return P7.this._3();
         }

         public D _4() {
            return P7.this._4();
         }

         public E _5() {
            return P7.this._5();
         }

         public X _6() {
            return f.f(P7.this._6());
         }

         public G _7() {
            return P7.this._7();
         }
      };
   }

   public final <X> P7<A, B, C, D, E, F, X> map7(final F<G, X> f) {
      return new P7<A, B, C, D, E, F, X>() {
         public A _1() {
            return P7.this._1();
         }

         public B _2() {
            return P7.this._2();
         }

         public C _3() {
            return P7.this._3();
         }

         public D _4() {
            return P7.this._4();
         }

         public E _5() {
            return P7.this._5();
         }

         public F _6() {
            return P7.this._6();
         }

         public X _7() {
            return f.f(P7.this._7());
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

   public final P7<A, B, C, D, E, F, G> memo() {
      return new P7<A, B, C, D, E, F, G>() {
         private final P1<A> a = P1.memo(P7$8$$Lambda$1.lambdaFactory$(P7.this));
         private final P1<B> b = P1.memo(P7$8$$Lambda$2.lambdaFactory$(P7.this));
         private final P1<C> c = P1.memo(P7$8$$Lambda$3.lambdaFactory$(P7.this));
         private final P1<D> d = P1.memo(P7$8$$Lambda$4.lambdaFactory$(P7.this));
         private final P1<E> e = P1.memo(P7$8$$Lambda$5.lambdaFactory$(P7.this));
         private final P1<F> f = P1.memo(P7$8$$Lambda$6.lambdaFactory$(P7.this));
         private final P1<G> g = P1.memo(P7$8$$Lambda$7.lambdaFactory$(P7.this));

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

         // $FF: synthetic method
         private static Object lambda$$26(P7 var0, Unit u) {
            return var0._7();
         }

         // $FF: synthetic method
         private static Object lambda$$25(P7 var0, Unit u) {
            return var0._6();
         }

         // $FF: synthetic method
         private static Object lambda$$24(P7 var0, Unit u) {
            return var0._5();
         }

         // $FF: synthetic method
         private static Object lambda$$23(P7 var0, Unit u) {
            return var0._4();
         }

         // $FF: synthetic method
         private static Object lambda$$22(P7 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$21(P7 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$20(P7 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P7 var0, Unit var1) {
            return lambda$$20(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P7 var0, Unit var1) {
            return lambda$$21(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P7 var0, Unit var1) {
            return lambda$$22(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$3(P7 var0, Unit var1) {
            return lambda$$23(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$4(P7 var0, Unit var1) {
            return lambda$$24(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$5(P7 var0, Unit var1) {
            return lambda$$25(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$6(P7 var0, Unit var1) {
            return lambda$$26(var0, var1);
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, A> __1() {
      return new F<P7<A, B, C, D, E, F$, G>, A>() {
         public A f(P7<A, B, C, D, E, F$, G> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, B> __2() {
      return new F<P7<A, B, C, D, E, F$, G>, B>() {
         public B f(P7<A, B, C, D, E, F$, G> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, C> __3() {
      return new F<P7<A, B, C, D, E, F$, G>, C>() {
         public C f(P7<A, B, C, D, E, F$, G> p) {
            return p._3();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, D> __4() {
      return new F<P7<A, B, C, D, E, F$, G>, D>() {
         public D f(P7<A, B, C, D, E, F$, G> p) {
            return p._4();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, E> __5() {
      return new F<P7<A, B, C, D, E, F$, G>, E>() {
         public E f(P7<A, B, C, D, E, F$, G> p) {
            return p._5();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, F$> __6() {
      return new F<P7<A, B, C, D, E, F$, G>, F$>() {
         public F$ f(P7<A, B, C, D, E, F$, G> p) {
            return p._6();
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, G> __7() {
      return new F<P7<A, B, C, D, E, F$, G>, G>() {
         public G f(P7<A, B, C, D, E, F$, G> p) {
            return p._7();
         }
      };
   }

   public String toString() {
      return Show.p7Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
