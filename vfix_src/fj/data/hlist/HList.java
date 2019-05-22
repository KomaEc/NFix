package fj.data.hlist;

import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.P;
import fj.P2;
import fj.Unit;

public abstract class HList<A extends HList<A>> {
   private static final HList.HNil nil = new HList.HNil();

   HList() {
   }

   public abstract <E> HList.HCons<E, A> extend(E var1);

   public abstract <E> HList.Apply<Unit, P2<E, A>, HList.HCons<E, A>> extender();

   public static HList.HNil nil() {
      return nil;
   }

   public static <E, L extends HList<L>> HList.HCons<E, L> cons(E e, L l) {
      return new HList.HCons(e, l);
   }

   public static <E> HList.HCons<E, HList.HNil> single(E e) {
      return cons(e, nil());
   }

   public static final class HNil extends HList<HList.HNil> {
      HNil() {
      }

      public <E> HList.HCons<E, HList.HNil> extend(E e) {
         return cons(e, this);
      }

      public <E> HList.Apply<Unit, P2<E, HList.HNil>, HList.HCons<E, HList.HNil>> extender() {
         return HList.Apply.cons();
      }
   }

   public static final class HCons<E, L extends HList<L>> extends HList<HList.HCons<E, L>> {
      private final E e;
      private final L l;

      HCons(E e, L l) {
         this.e = e;
         this.l = l;
      }

      public E head() {
         return this.e;
      }

      public L tail() {
         return this.l;
      }

      public <X> HList.Apply<Unit, P2<X, HList.HCons<E, L>>, HList.HCons<X, HList.HCons<E, L>>> extender() {
         return HList.Apply.cons();
      }

      public <X> HList.HCons<X, HList.HCons<E, L>> extend(X e) {
         return cons(e, this);
      }
   }

   public static final class HFoldr<G, V, L, R> {
      private final F3<G, V, L, R> foldRight;

      private HFoldr(F3<G, V, L, R> foldRight) {
         this.foldRight = foldRight;
      }

      public static <G, V> HList.HFoldr<G, V, HList.HNil, V> hFoldr() {
         return new HList.HFoldr(new F3<G, V, HList.HNil, V>() {
            public V f(G f, V v, HList.HNil hNil) {
               return v;
            }
         });
      }

      public static <E, G, V, L extends HList<L>, R, RR, H extends HList.HFoldr<G, V, L, R>, PP extends HList.Apply<G, P2<E, R>, RR>> HList.HFoldr<G, V, HList.HCons<E, L>, RR> hFoldr(final PP p, final H h) {
         return new HList.HFoldr(new F3<G, V, HList.HCons<E, L>, RR>() {
            public RR f(G f, V v, HList.HCons<E, L> c) {
               return p.apply(f, P.p(c.head(), h.foldRight(f, v, c.tail())));
            }
         });
      }

      public R foldRight(G f, V v, L l) {
         return this.foldRight.f(f, v, l);
      }
   }

   public abstract static class Apply<F$, A, R> {
      public abstract R apply(F$ var1, A var2);

      public static <X, Y> HList.Apply<F<X, Y>, X, Y> f() {
         return new HList.Apply<F<X, Y>, X, Y>() {
            public Y apply(F<X, Y> f, X x) {
               return f.f(x);
            }
         };
      }

      public static <X> HList.Apply<Unit, X, X> id() {
         return new HList.Apply<Unit, X, X>() {
            public X apply(Unit f, X x) {
               return x;
            }
         };
      }

      public static <X, Y, Z> HList.Apply<Unit, P2<F<X, Y>, F<Y, Z>>, F<X, Z>> comp() {
         return new HList.Apply<Unit, P2<F<X, Y>, F<Y, Z>>, F<X, Z>>() {
            public F<X, Z> apply(Unit f, P2<F<X, Y>, F<Y, Z>> fs) {
               return Function.compose((F)fs._2(), (F)fs._1());
            }
         };
      }

      public static <E, L extends HList<L>> HList.Apply<Unit, P2<E, L>, HList.HCons<E, L>> cons() {
         return new HList.Apply<Unit, P2<E, L>, HList.HCons<E, L>>() {
            public HList.HCons<E, L> apply(Unit f, P2<E, L> p) {
               return HList.cons(p._1(), (HList)p._2());
            }
         };
      }

      public static <A, B, C> HList.Apply<HList.HAppend<A, B, C>, P2<A, B>, C> append() {
         return new HList.Apply<HList.HAppend<A, B, C>, P2<A, B>, C>() {
            public C apply(HList.HAppend<A, B, C> f, P2<A, B> p) {
               return f.append(p._1(), p._2());
            }
         };
      }
   }

   public static final class HAppend<A, B, C> {
      private final F2<A, B, C> append;

      private HAppend(F2<A, B, C> f) {
         this.append = f;
      }

      public C append(A a, B b) {
         return this.append.f(a, b);
      }

      public static <L extends HList<L>> HList.HAppend<HList.HNil, L, L> append() {
         return new HList.HAppend(new F2<HList.HNil, L, L>() {
            public L f(HList.HNil hNil, L l) {
               return l;
            }
         });
      }

      public static <X, A extends HList<A>, B, C extends HList<C>, H extends HList.HAppend<A, B, C>> HList.HAppend<HList.HCons<X, A>, B, HList.HCons<X, C>> append(final H h) {
         return new HList.HAppend(new F2<HList.HCons<X, A>, B, HList.HCons<X, C>>() {
            public HList.HCons<X, C> f(HList.HCons<X, A> c, B l) {
               return HList.cons(c.head(), (HList)h.append(c.tail(), l));
            }
         });
      }
   }
}
