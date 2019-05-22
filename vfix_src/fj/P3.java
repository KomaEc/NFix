package fj;

public abstract class P3<A, B, C> {
   public abstract A _1();

   public abstract B _2();

   public abstract C _3();

   public final <X> P3<X, B, C> map1(final F<A, X> f) {
      return new P3<X, B, C>() {
         public X _1() {
            return f.f(P3.this._1());
         }

         public B _2() {
            return P3.this._2();
         }

         public C _3() {
            return P3.this._3();
         }
      };
   }

   public final <X> P3<A, X, C> map2(final F<B, X> f) {
      return new P3<A, X, C>() {
         public A _1() {
            return P3.this._1();
         }

         public X _2() {
            return f.f(P3.this._2());
         }

         public C _3() {
            return P3.this._3();
         }
      };
   }

   public final <X> P3<A, B, X> map3(final F<C, X> f) {
      return new P3<A, B, X>() {
         public A _1() {
            return P3.this._1();
         }

         public B _2() {
            return P3.this._2();
         }

         public X _3() {
            return f.f(P3.this._3());
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

   public final P3<A, B, C> memo() {
      return new P3<A, B, C>() {
         private final P1<A> a = P1.memo(P3$4$$Lambda$1.lambdaFactory$(P3.this));
         private final P1<B> b = P1.memo(P3$4$$Lambda$2.lambdaFactory$(P3.this));
         private final P1<C> c = P1.memo(P3$4$$Lambda$3.lambdaFactory$(P3.this));

         public A _1() {
            return this.a._1();
         }

         public B _2() {
            return this.b._1();
         }

         public C _3() {
            return this.c._1();
         }

         // $FF: synthetic method
         private static Object lambda$$4(P3 var0, Unit u) {
            return var0._3();
         }

         // $FF: synthetic method
         private static Object lambda$$3(P3 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$2(P3 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P3 var0, Unit var1) {
            return lambda$$2(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P3 var0, Unit var1) {
            return lambda$$3(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$2(P3 var0, Unit var1) {
            return lambda$$4(var0, var1);
         }
      };
   }

   public static <A, B, C> F<P3<A, B, C>, A> __1() {
      return new F<P3<A, B, C>, A>() {
         public A f(P3<A, B, C> p) {
            return p._1();
         }
      };
   }

   public static <A, B, C> F<P3<A, B, C>, B> __2() {
      return new F<P3<A, B, C>, B>() {
         public B f(P3<A, B, C> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C> F<P3<A, B, C>, C> __3() {
      return new F<P3<A, B, C>, C>() {
         public C f(P3<A, B, C> p) {
            return p._3();
         }
      };
   }

   public String toString() {
      return Show.p3Show(Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
