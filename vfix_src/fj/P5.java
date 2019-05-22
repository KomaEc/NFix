package fj;

public abstract class P5<A, B, C, D, E> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public abstract D _4();

   public abstract E _5();

   public final <X> P5<X, B, C, D, E> map1(final F<A, X> f) {
      return new P5<X, B, C, D, E>() {
         public X _1() {
            return f.f(P5.this._1());
         }

         public B _2() {
            return P5.this._2();
         }

         public C _3() {
            return P5.this._3();
         }

         public D _4() {
            return P5.this._4();
         }

         public E _5() {
            return P5.this._5();
         }
      };
   }

   public final <X> P5<A, X, C, D, E> map2(final F<B, X> f) {
      return new P5<A, X, C, D, E>() {
         public A _1() {
            return P5.this._1();
         }

         public X _2() {
            return f.f(P5.this._2());
         }

         public C _3() {
            return P5.this._3();
         }

         public D _4() {
            return P5.this._4();
         }

         public E _5() {
            return P5.this._5();
         }
      };
   }

   public final <X> P5<A, B, X, D, E> map3(final F<C, X> f) {
      return new P5<A, B, X, D, E>() {
         public A _1() {
            return P5.this._1();
         }

         public B _2() {
            return P5.this._2();
         }

         public X _3() {
            return f.f(P5.this._3());
         }

         public D _4() {
            return P5.this._4();
         }

         public E _5() {
            return P5.this._5();
         }
      };
   }

   public final <X> P5<A, B, C, X, E> map4(final F<D, X> f) {
      return new P5<A, B, C, X, E>() {
         public A _1() {
            return P5.this._1();
         }

         public B _2() {
            return P5.this._2();
         }

         public C _3() {
            return P5.this._3();
         }

         public X _4() {
            return f.f(P5.this._4());
         }

         public E _5() {
            return P5.this._5();
         }
      };
   }

   public final <X> P5<A, B, C, D, X> map5(final F<E, X> f) {
      return new P5<A, B, C, D, X>() {
         public A _1() {
            return P5.this._1();
         }

         public B _2() {
            return P5.this._2();
         }

         public C _3() {
            return P5.this._3();
         }

         public D _4() {
            return P5.this._4();
         }

         public X _5() {
            return f.f(P5.this._5());
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

   public final P5<A, B, C, D, E> memo() {
      return new P5<A, B, C, D, E>() {
         private final P1<A> a = P1.memo(P5$6$$Lambda$1.lambdaFactory$(P5.this));
         private final P1<B> b = P1.memo(P5$6$$Lambda$2.lambdaFactory$(P5.this));
         private final P1<C> c = P1.memo(P5$6$$Lambda$3.lambdaFactory$(P5.this));
         private final P1<D> d = P1.memo(P5$6$$Lambda$4.lambdaFactory$(P5.this));
         private final P1<E> e = P1.memo(P5$6$$Lambda$5.lambdaFactory$(P5.this));

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

         // $FF: synthetic method
         private static Object lambda$$13(P5 var0, Unit u) {
            return var0._5();
         }

         // $FF: synthetic method
         private static Object lambda$$12(P5 var0, Unit u) {
            return var0._4();
         }

         // $FF: synthetic method
         private static Object lambda$$11(P5 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$10(P5 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$9(P5 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P5 var0, Unit var1) {
            return lambda$$9(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P5 var0, Unit var1) {
            return lambda$$10(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P5 var0, Unit var1) {
            return lambda$$11(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$3(P5 var0, Unit var1) {
            return lambda$$12(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$4(P5 var0, Unit var1) {
            return lambda$$13(var0, var1);
         }
      };
   }

   public static <A, B, C, D, E> F<P5<A, B, C, D, E>, A> __1() {
      return new F<P5<A, B, C, D, E>, A>() {
         public A f(P5<A, B, C, D, E> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C, D, E> F<P5<A, B, C, D, E>, B> __2() {
      return new F<P5<A, B, C, D, E>, B>() {
         public B f(P5<A, B, C, D, E> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C, D, E> F<P5<A, B, C, D, E>, C> __3() {
      return new F<P5<A, B, C, D, E>, C>() {
         public C f(P5<A, B, C, D, E> p) {
            return p._3();
         }
      };
   }

   public static <A, B, C, D, E> F<P5<A, B, C, D, E>, D> __4() {
      return new F<P5<A, B, C, D, E>, D>() {
         public D f(P5<A, B, C, D, E> p) {
            return p._4();
         }
      };
   }

   public static <A, B, C, D, E> F<P5<A, B, C, D, E>, E> __5() {
      return new F<P5<A, B, C, D, E>, E>() {
         public E f(P5<A, B, C, D, E> p) {
            return p._5();
         }
      };
   }

   public String toString() {
      return Show.p5Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
