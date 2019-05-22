package fj;

import fj.data.Array;
import fj.data.List;
import fj.data.Stream;
import java.lang.ref.SoftReference;

public abstract class P1<A> {
   public abstract A _1();

   public static <A> F<P1<A>, A> __1() {
      return new F<P1<A>, A>() {
         public A f(P1<A> p) {
            return p._1();
         }
      };
   }

   public static <A, B> F<P1<A>, P1<B>> fmap(final F<A, B> f) {
      return new F<P1<A>, P1<B>>() {
         public P1<B> f(P1<A> a) {
            return a.map(f);
         }
      };
   }

   public <B> P1<B> bind(final F<A, P1<B>> f) {
      return new P1<B>() {
         public B _1() {
            return ((P1)f.f(P1.this._1()))._1();
         }
      };
   }

   public static <A, B> F<A, P1<B>> curry(final F<A, B> f) {
      return new F<A, P1<B>>() {
         public P1<B> f(final A a) {
            return new P1<B>() {
               public B _1() {
                  return f.f(a);
               }
            };
         }
      };
   }

   public <B> P1<B> apply(P1<F<A, B>> cf) {
      return cf.bind(new F<F<A, B>, P1<B>>() {
         public P1<B> f(F<A, B> f) {
            return (P1)P1.fmap(f).f(P1.this);
         }
      });
   }

   public <B, C> P1<C> bind(P1<B> cb, F<A, F<B, C>> f) {
      return cb.apply((P1)fmap(f).f(this));
   }

   public static <A> P1<A> join(P1<P1<A>> a) {
      return a.bind(Function.identity());
   }

   public static <A, B, C> F<P1<A>, F<P1<B>, P1<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<P1<A>, P1<B>, P1<C>>() {
         public P1<C> f(P1<A> pa, P1<B> pb) {
            return pa.bind(pb, f);
         }
      });
   }

   public static <A> P1<List<A>> sequence(List<P1<A>> as) {
      return (P1)as.foldRight((F)liftM2(List.cons()), P.p(List.nil()));
   }

   public static <A> F<List<P1<A>>, P1<List<A>>> sequenceList() {
      return new F<List<P1<A>>, P1<List<A>>>() {
         public P1<List<A>> f(List<P1<A>> as) {
            return P1.sequence(as);
         }
      };
   }

   public static <A> P1<Stream<A>> sequence(Stream<P1<A>> as) {
      return (P1)as.foldRight((F)liftM2(Stream.cons()), P.p(Stream.nil()));
   }

   public static <A> P1<Array<A>> sequence(final Array<P1<A>> as) {
      return new P1<Array<A>>() {
         public Array<A> _1() {
            return as.map(P1.__1());
         }
      };
   }

   public <X> P1<X> map(final F<A, X> f) {
      return new P1<X>() {
         public X _1() {
            return f.f(P1.this._1());
         }
      };
   }

   public P1<A> memo() {
      return new P1<A>() {
         private final Object latch = new Object();
         private volatile SoftReference<A> v;

         public A _1() {
            A a = this.v != null ? this.v.get() : null;
            if (a == null) {
               synchronized(this.latch) {
                  if (this.v == null || this.v.get() == null) {
                     a = P1.this._1();
                  }

                  this.v = new SoftReference(a);
               }
            }

            return a;
         }
      };
   }

   static <A> P1<A> memo(F<Unit, A> f) {
      return P.lazy(f).memo();
   }

   public <B> F<B, A> constant() {
      return new F<B, A>() {
         public A f(B b) {
            return P1.this._1();
         }
      };
   }

   public String toString() {
      return Show.p1Show(Show.anyShow()).showS((Object)this);
   }
}
