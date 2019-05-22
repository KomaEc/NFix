package fj;

public abstract class P4<A, B, C, D> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public abstract D _4();

   public final <X> P4<X, B, C, D> map1(final F<A, X> f) {
      return new P4<X, B, C, D>() {
         public X _1() {
            return f.f(P4.this._1());
         }

         public B _2() {
            return P4.this._2();
         }

         public C _3() {
            return P4.this._3();
         }

         public D _4() {
            return P4.this._4();
         }
      };
   }

   public final <X> P4<A, X, C, D> map2(final F<B, X> f) {
      return new P4<A, X, C, D>() {
         public A _1() {
            return P4.this._1();
         }

         public X _2() {
            return f.f(P4.this._2());
         }

         public C _3() {
            return P4.this._3();
         }

         public D _4() {
            return P4.this._4();
         }
      };
   }

   public final <X> P4<A, B, X, D> map3(final F<C, X> f) {
      return new P4<A, B, X, D>() {
         public A _1() {
            return P4.this._1();
         }

         public B _2() {
            return P4.this._2();
         }

         public X _3() {
            return f.f(P4.this._3());
         }

         public D _4() {
            return P4.this._4();
         }
      };
   }

   public final <X> P4<A, B, C, X> map4(final F<D, X> f) {
      return new P4<A, B, C, X>() {
         public A _1() {
            return P4.this._1();
         }

         public B _2() {
            return P4.this._2();
         }

         public C _3() {
            return P4.this._3();
         }

         public X _4() {
            return f.f(P4.this._4());
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

   public final P4<A, B, C, D> memo() {
      return new P4<A, B, C, D>() {
         private final P1<A> a = P1.memo(P4$5$$Lambda$1.lambdaFactory$(P4.this));
         private final P1<B> b = P1.memo(P4$5$$Lambda$2.lambdaFactory$(P4.this));
         private final P1<C> c = P1.memo(P4$5$$Lambda$3.lambdaFactory$(P4.this));
         private final P1<D> d = P1.memo(P4$5$$Lambda$4.lambdaFactory$(P4.this));

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

         // $FF: synthetic method
         private static Object lambda$$8(P4 var0, Unit u) {
            return var0._4();
         }

         // $FF: synthetic method
         private static Object lambda$$7(P4 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$6(P4 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$5(P4 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P4 var0, Unit var1) {
            return lambda$$5(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P4 var0, Unit var1) {
            return lambda$$6(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P4 var0, Unit var1) {
            return lambda$$7(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$3(P4 var0, Unit var1) {
            return lambda$$8(var0, var1);
         }
      };
   }

   public static <A, B, C, D> F<P4<A, B, C, D>, A> __1() {
      return new F<P4<A, B, C, D>, A>() {
         public A f(P4<A, B, C, D> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C, D> F<P4<A, B, C, D>, B> __2() {
      return new F<P4<A, B, C, D>, B>() {
         public B f(P4<A, B, C, D> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C, D> F<P4<A, B, C, D>, C> __3() {
      return new F<P4<A, B, C, D>, C>() {
         public C f(P4<A, B, C, D> p) {
            return p._3();
         }
      };
   }

   public static <A, B, C, D> F<P4<A, B, C, D>, D> __4() {
      return new F<P4<A, B, C, D>, D>() {
         public D f(P4<A, B, C, D> p) {
            return p._4();
         }
      };
   }

   public String toString() {
      return Show.p4Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
