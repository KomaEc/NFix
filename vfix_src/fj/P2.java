package fj;

import fj.data.List;
import fj.data.Stream;
import java.util.Iterator;

public abstract class P2<A, B> {
   public abstract A _1();

   public abstract B _2();

   public final P2<B, A> swap() {
      return new P2<B, A>() {
         public B _1() {
            return P2.this._2();
         }

         public A _2() {
            return P2.this._1();
         }
      };
   }

   public final <X> P2<X, B> map1(final F<A, X> f) {
      return new P2<X, B>() {
         public X _1() {
            return f.f(P2.this._1());
         }

         public B _2() {
            return P2.this._2();
         }
      };
   }

   public final <X> P2<A, X> map2(final F<B, X> f) {
      return new P2<A, X>() {
         public A _1() {
            return P2.this._1();
         }

         public X _2() {
            return f.f(P2.this._2());
         }
      };
   }

   public final <C, D> P2<C, D> split(F<A, C> f, F<B, D> g) {
      F<P2<A, D>, P2<C, D>> ff = map1_(f);
      F<P2<A, B>, P2<A, D>> gg = map2_(g);
      return (P2)Function.compose(ff, gg).f(this);
   }

   public final <C> P2<C, B> cobind(final F<P2<A, B>, C> k) {
      return new P2<C, B>() {
         public C _1() {
            return k.f(P2.this);
         }

         public B _2() {
            return P2.this._2();
         }
      };
   }

   public final P2<P2<A, B>, B> duplicate() {
      F<P2<A, B>, P2<A, B>> id = Function.identity();
      return this.cobind(id);
   }

   public final <C> P2<C, B> inject(C c) {
      F<P2<A, B>, C> co = Function.constant(c);
      return this.cobind(co);
   }

   public final <C> List<C> sequenceW(List<F<P2<A, B>, C>> fs) {
      List.Buffer<C> cs = List.Buffer.empty();

      F f;
      for(Iterator var3 = fs.iterator(); var3.hasNext(); cs = cs.snoc(f.f(this))) {
         f = (F)var3.next();
      }

      return cs.toList();
   }

   public final <C> Stream<C> sequenceW(final Stream<F<P2<A, B>, C>> fs) {
      return fs.isEmpty() ? Stream.nil() : Stream.cons(((F)fs.head()).f(this), new P1<Stream<C>>() {
         public Stream<C> _1() {
            return P2.this.sequenceW((Stream)fs.tail()._1());
         }
      });
   }

   public final P1<A> _1_() {
      return (P1)F1Functions.lazy(__1()).f(this);
   }

   public final P1<B> _2_() {
      return (P1)F1Functions.lazy(__2()).f(this);
   }

   public final P2<A, B> memo() {
      return new P2<A, B>() {
         private final P1<A> a = P1.memo(P2$6$$Lambda$1.lambdaFactory$(P2.this));
         private final P1<B> b = P1.memo(P2$6$$Lambda$2.lambdaFactory$(P2.this));

         public A _1() {
            return this.a._1();
         }

         public B _2() {
            return this.b._1();
         }

         // $FF: synthetic method
         private static Object lambda$$1(P2 var0, Unit u) {
            return var0._2();
         }

         // $FF: synthetic method
         private static Object lambda$$0(P2 var0, Unit u) {
            return var0._1();
         }

         // $FF: synthetic method
         static Object access$lambda$0(P2 var0, Unit var1) {
            return lambda$$0(var0, var1);
         }

         // $FF: synthetic method
         static Object access$lambda$1(P2 var0, Unit var1) {
            return lambda$$1(var0, var1);
         }
      };
   }

   public static <A, B, C, D> F<P2<A, B>, P2<C, D>> split_(final F<A, C> f, final F<B, D> g) {
      return new F<P2<A, B>, P2<C, D>>() {
         public P2<C, D> f(P2<A, B> p) {
            return p.split(f, g);
         }
      };
   }

   public static <A, B, X> F<P2<A, B>, P2<X, B>> map1_(final F<A, X> f) {
      return new F<P2<A, B>, P2<X, B>>() {
         public P2<X, B> f(P2<A, B> p) {
            return p.map1(f);
         }
      };
   }

   public static <A, B, X> F<P2<A, B>, P2<A, X>> map2_(final F<B, X> f) {
      return new F<P2<A, B>, P2<A, X>>() {
         public P2<A, X> f(P2<A, B> p) {
            return p.map2(f);
         }
      };
   }

   public static <B, C, D> P2<C, D> fanout(F<B, C> f, F<B, D> g, B b) {
      return ((P2)Function.join(P.p2()).f(b)).split(f, g);
   }

   public static <A, B> P2<B, B> map(F<A, B> f, P2<A, A> p) {
      return p.split(f, f);
   }

   public static <A, B> F<P2<A, B>, P2<B, A>> swap_() {
      return new F<P2<A, B>, P2<B, A>>() {
         public P2<B, A> f(P2<A, B> p) {
            return p.swap();
         }
      };
   }

   public static <A, B> F<P2<A, B>, A> __1() {
      return new F<P2<A, B>, A>() {
         public A f(P2<A, B> p) {
            return p._1();
         }
      };
   }

   public static <A, B> F<P2<A, B>, B> __2() {
      return new F<P2<A, B>, B>() {
         public B f(P2<A, B> p) {
            return p._2();
         }
      };
   }

   public static <A, B, C> F<P2<A, B>, C> tuple(final F<A, F<B, C>> f) {
      return new F<P2<A, B>, C>() {
         public C f(P2<A, B> p) {
            return ((F)f.f(p._1())).f(p._2());
         }
      };
   }

   public static <A, B, C> F<P2<A, B>, C> tuple(F2<A, B, C> f) {
      return tuple(Function.curry(f));
   }

   public static <A, B, C> F2<A, B, C> untuple(final F<P2<A, B>, C> f) {
      return new F2<A, B, C>() {
         public C f(A a, B b) {
            return f.f(P.p(a, b));
         }
      };
   }

   public String toString() {
      return Show.p2Show(Show.anyShow(), Show.anyShow()).showS((Object)this);
   }
}
