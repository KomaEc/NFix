package fj;

public abstract class P6<A, B, C, D, E, F> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public abstract D _4();

   public abstract E _5();

   public abstract F _6();

   public final <X> P6<X, B, C, D, E, F> map1(final F<A, X> f) {
      return new P6<X, B, C, D, E, F>() {
         public X _1() {
            return f.f(P6.this._1());
         }

         public B _2() {
            return P6.this._2();
         }

         public C _3() {
            return P6.this._3();
         }

         public D _4() {
            return P6.this._4();
         }

         public E _5() {
            return P6.this._5();
         }

         public F _6() {
            return P6.this._6();
         }
      };
   }

   public final <X> P6<A, X, C, D, E, F> map2(final F<B, X> f) {
      return new P6<A, X, C, D, E, F>() {
         public A _1() {
            return P6.this._1();
         }

         public X _2() {
            return f.f(P6.this._2());
         }

         public C _3() {
            return P6.this._3();
         }

         public D _4() {
            return P6.this._4();
         }

         public E _5() {
            return P6.this._5();
         }

         public F _6() {
            return P6.this._6();
         }
      };
   }

   public final <X> P6<A, B, X, D, E, F> map3(final F<C, X> f) {
      return new P6<A, B, X, D, E, F>() {
         public A _1() {
            return P6.this._1();
         }

         public B _2() {
            return P6.this._2();
         }

         public X _3() {
            return f.f(P6.this._3());
         }

         public D _4() {
            return P6.this._4();
         }

         public E _5() {
            return P6.this._5();
         }

         public F _6() {
            return P6.this._6();
         }
      };
   }

   public final <X> P6<A, B, C, X, E, F> map4(final F<D, X> f) {
      return new P6<A, B, C, X, E, F>() {
         public A _1() {
            return P6.this._1();
         }

         public B _2() {
            return P6.this._2();
         }

         public C _3() {
            return P6.this._3();
         }

         public X _4() {
            return f.f(P6.this._4());
         }

         public E _5() {
            return P6.this._5();
         }

         public F _6() {
            return P6.this._6();
         }
      };
   }

   public final <X> P6<A, B, C, D, X, F> map5(final F<E, X> f) {
      return new P6<A, B, C, D, X, F>() {
         public A _1() {
            return P6.this._1();
         }

         public B _2() {
            return P6.this._2();
         }

         public C _3() {
            return P6.this._3();
         }

         public D _4() {
            return P6.this._4();
         }

         public X _5() {
            return f.f(P6.this._5());
         }

         public F _6() {
            return P6.this._6();
         }
      };
   }

   public final <X> P6<A, B, C, D, E, X> map6(final F<F, X> f) {
      return new P6<A, B, C, D, E, X>() {
         public A _1() {
            return P6.this._1();
         }

         public B _2() {
            return P6.this._2();
         }

         public C _3() {
            return P6.this._3();
         }

         public D _4() {
            return P6.this._4();
         }

         public E _5() {
            return P6.this._5();
         }

         public X _6() {
            return f.f(P6.this._6());
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

   public final P6<A, B, C, D, E, F> memo() {
      return new P6<A, B, C, D, E, F>() {
         private final P1<A> a = P1.memo(P6$7$$Lambda$1.lambdaFactory$(P6.this));
         private final P1<B> b = P1.memo(P6$7$$Lambda$2.lambdaFactory$(P6.this));
         private final P1<C> c = P1.memo(P6$7$$Lambda$3.lambdaFactory$(P6.this));
         private final P1<D> d = P1.memo(P6$7$$Lambda$4.lambdaFactory$(P6.this));
         private final P1<E> e = P1.memo(P6$7$$Lambda$5.lambdaFactory$(P6.this));
         private final P1<F> f = P1.memo(P6$7$$Lambda$6.lambdaFactory$(P6.this));

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

         // $FF: synthetic method
         private static Object lambda$$19(P6 var0, Unit u) {
            return var0._6();
         }

         // $FF: synthetic method
         private static Object lambda$$18(P6 var0, Unit u) {
            return var0._5();
         }

         // $FF: synthetic method
         private static Object lambda$$17(P6 var0, Unit u) {
            return var0._4();
         }

         // $FF: synthetic method
         private static Object lambda$$16(P6 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$15(P6 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$14(P6 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P6 var0, Unit var1) {
            return lambda$$14(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P6 var0, Unit var1) {
            return lambda$$15(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P6 var0, Unit var1) {
            return lambda$$16(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$3(P6 var0, Unit var1) {
            return lambda$$17(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$4(P6 var0, Unit var1) {
            return lambda$$18(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$5(P6 var0, Unit var1) {
            return lambda$$19(var0, var1);
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, A> __1() {
      return new F<P6<A, B, C, D, E, F$>, A>() {
         public A f(P6<A, B, C, D, E, F$> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, B> __2() {
      return new F<P6<A, B, C, D, E, F$>, B>() {
         public B f(P6<A, B, C, D, E, F$> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, C> __3() {
      return new F<P6<A, B, C, D, E, F$>, C>() {
         public C f(P6<A, B, C, D, E, F$> p) {
            return p._3();
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, D> __4() {
      return new F<P6<A, B, C, D, E, F$>, D>() {
         public D f(P6<A, B, C, D, E, F$> p) {
            return p._4();
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, E> __5() {
      return new F<P6<A, B, C, D, E, F$>, E>() {
         public E f(P6<A, B, C, D, E, F$> p) {
            return p._5();
         }
      };
   }

   public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, F$> __6() {
      return new F<P6<A, B, C, D, E, F$>, F$>() {
         public F$ f(P6<A, B, C, D, E, F$> p) {
            return p._6();
         }
      };
   }

   public String toString() {
      return Show.p6Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
