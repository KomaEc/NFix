package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.Function;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;

public abstract class Digit<V, A> {
   private final Measured<V, A> m;

   public abstract <B> B foldRight(F<A, F<B, B>> var1, B var2);

   public abstract <B> B foldLeft(F<B, F<A, B>> var1, B var2);

   public final A reduceRight(final F<A, F<A, A>> f) {
      return this.match(new F<One<V, A>, A>() {
         public A f(One<V, A> one) {
            return one.value();
         }
      }, new F<Two<V, A>, A>() {
         public A f(Two<V, A> two) {
            V2<A> v = two.values();
            return ((F)f.f(v._1())).f(v._2());
         }
      }, new F<Three<V, A>, A>() {
         public A f(Three<V, A> three) {
            V3<A> v = three.values();
            return ((F)f.f(v._1())).f(((F)f.f(v._2())).f(v._3()));
         }
      }, new F<Four<V, A>, A>() {
         public A f(Four<V, A> four) {
            V4<A> v = four.values();
            return ((F)f.f(v._1())).f(((F)f.f(v._2())).f(((F)f.f(v._3())).f(v._4())));
         }
      });
   }

   public final A reduceLeft(final F<A, F<A, A>> f) {
      return this.match(new F<One<V, A>, A>() {
         public A f(One<V, A> one) {
            return one.value();
         }
      }, new F<Two<V, A>, A>() {
         public A f(Two<V, A> two) {
            V2<A> v = two.values();
            return ((F)f.f(v._1())).f(v._2());
         }
      }, new F<Three<V, A>, A>() {
         public A f(Three<V, A> three) {
            V3<A> v = three.values();
            return ((F)f.f(((F)f.f(v._1())).f(v._2()))).f(v._3());
         }
      }, new F<Four<V, A>, A>() {
         public A f(Four<V, A> four) {
            V4<A> v = four.values();
            return ((F)f.f(((F)f.f(((F)f.f(v._1())).f(v._2()))).f(v._3()))).f(v._4());
         }
      });
   }

   public final <B> Digit<V, B> map(final F<A, B> f, final Measured<V, B> m) {
      return (Digit)this.match(new F<One<V, A>, Digit<V, B>>() {
         public Digit<V, B> f(One<V, A> one) {
            return new One(m, f.f(one.value()));
         }
      }, new F<Two<V, A>, Digit<V, B>>() {
         public Digit<V, B> f(Two<V, A> two) {
            return new Two(m, two.values().map(f));
         }
      }, new F<Three<V, A>, Digit<V, B>>() {
         public Digit<V, B> f(Three<V, A> three) {
            return new Three(m, three.values().map(f));
         }
      }, new F<Four<V, A>, Digit<V, B>>() {
         public Digit<V, B> f(Four<V, A> four) {
            return new Four(m, four.values().map(f));
         }
      });
   }

   public abstract <B> B match(F<One<V, A>, B> var1, F<Two<V, A>, B> var2, F<Three<V, A>, B> var3, F<Four<V, A>, B> var4);

   Digit(Measured<V, A> m) {
      this.m = m;
   }

   public final V measure() {
      return this.foldLeft(Function.curry(new F2<V, A, V>() {
         public V f(V v, A a) {
            return Digit.this.m.sum(v, Digit.this.m.measure(a));
         }
      }), this.m.zero());
   }

   public final FingerTree<V, A> toTree() {
      final MakeTree<V, A> mk = FingerTree.mkTree(this.m);
      return (FingerTree)this.match(new F<One<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(One<V, A> one) {
            return mk.single(one.value());
         }
      }, new F<Two<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Two<V, A> two) {
            return mk.deep(mk.one(two.values()._1()), new Empty(Digit.this.m.nodeMeasured()), mk.one(two.values()._2()));
         }
      }, new F<Three<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Three<V, A> three) {
            return mk.deep(mk.two(three.values()._1(), three.values()._2()), new Empty(Digit.this.m.nodeMeasured()), mk.one(three.values()._3()));
         }
      }, new F<Four<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Four<V, A> four) {
            return mk.deep(mk.two(four.values()._1(), four.values()._2()), new Empty(Digit.this.m.nodeMeasured()), mk.two(four.values()._3(), four.values()._4()));
         }
      });
   }
}
