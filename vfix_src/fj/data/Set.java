package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P2;
import fj.P3;
import fj.function.Booleans;
import java.util.Iterator;

public abstract class Set<A> implements Iterable<A> {
   private final Ord<A> ord;

   private Set(Ord<A> ord) {
      this.ord = ord;
   }

   public final boolean isEmpty() {
      return this instanceof Set.Empty;
   }

   abstract Set.Color color();

   abstract Set<A> l();

   abstract A head();

   abstract Set<A> r();

   public final Ord<A> ord() {
      return this.ord;
   }

   public final P2<Boolean, Set<A>> update(final A a, F<A, A> f) {
      return this.isEmpty() ? P.p(false, this) : (P2)this.tryUpdate(a, f).either(new F<A, P2<Boolean, Set<A>>>() {
         public P2<Boolean, Set<A>> f(A a2) {
            return P.p(true, Set.this.delete(a).insert(a2));
         }
      }, Function.identity());
   }

   private Either<A, P2<Boolean, Set<A>>> tryUpdate(A a, F<A, A> f) {
      if (this.isEmpty()) {
         return Either.right(P.p(false, this));
      } else if (this.ord.isLessThan(a, this.head())) {
         return this.l().tryUpdate(a, f).right().map(new F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>() {
            public P2<Boolean, Set<A>> f(P2<Boolean, Set<A>> set) {
               return (Boolean)set._1() ? P.p(true, new Set.Tree(Set.this.ord, Set.this.color(), (Set)set._2(), Set.this.head(), Set.this.r())) : set;
            }
         });
      } else if (this.ord.eq(a, this.head())) {
         A h = f.f(this.head());
         return this.ord.eq(this.head(), h) ? Either.right(P.p(true, new Set.Tree(this.ord, this.color(), this.l(), h, this.r()))) : Either.left(h);
      } else {
         return this.r().tryUpdate(a, f).right().map(new F<P2<Boolean, Set<A>>, P2<Boolean, Set<A>>>() {
            public P2<Boolean, Set<A>> f(P2<Boolean, Set<A>> set) {
               return (Boolean)set._1() ? P.p(true, new Set.Tree(Set.this.ord, Set.this.color(), Set.this.l(), Set.this.head(), (Set)set._2())) : set;
            }
         });
      }
   }

   public static <A> Set<A> empty(Ord<A> ord) {
      return new Set.Empty(ord);
   }

   public final boolean member(A x) {
      boolean var10000;
      label28: {
         if (!this.isEmpty()) {
            if (this.ord.isLessThan(x, this.head())) {
               if (this.l().member(x)) {
                  break label28;
               }
            } else if (this.ord.eq(this.head(), x) || this.r().member(x)) {
               break label28;
            }
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   public static <A> F<Set<A>, F<A, Boolean>> member() {
      return Function.curry(new F2<Set<A>, A, Boolean>() {
         public Boolean f(Set<A> s, A a) {
            return s.member(a);
         }
      });
   }

   public final Set<A> insert(A x) {
      return this.ins(x).makeBlack();
   }

   public static <A> F<A, F<Set<A>, Set<A>>> insert() {
      return Function.curry(new F2<A, Set<A>, Set<A>>() {
         public Set<A> f(A a, Set<A> set) {
            return set.insert(a);
         }
      });
   }

   private Set<A> ins(A x) {
      return (Set)(this.isEmpty() ? new Set.Tree(this.ord, Set.Color.R, empty(this.ord), x, empty(this.ord)) : (this.ord.isLessThan(x, this.head()) ? balance(this.ord, this.color(), this.l().ins(x), this.head(), this.r()) : (this.ord.eq(x, this.head()) ? new Set.Tree(this.ord, this.color(), this.l(), x, this.r()) : balance(this.ord, this.color(), this.l(), this.head(), this.r().ins(x)))));
   }

   private Set<A> makeBlack() {
      return new Set.Tree(this.ord, Set.Color.B, this.l(), this.head(), this.r());
   }

   private static <A> Set.Tree<A> tr(Ord<A> o, Set<A> a, A x, Set<A> b, A y, Set<A> c, A z, Set<A> d) {
      return new Set.Tree(o, Set.Color.R, new Set.Tree(o, Set.Color.B, a, x, b), y, new Set.Tree(o, Set.Color.B, c, z, d));
   }

   private static <A> Set<A> balance(Ord<A> ord, Set.Color c, Set<A> l, A h, Set<A> r) {
      return c == Set.Color.B && l.isTR() && l.l().isTR() ? tr(ord, l.l().l(), l.l().head(), l.l().r(), l.head(), l.r(), h, r) : (c == Set.Color.B && l.isTR() && l.r().isTR() ? tr(ord, l.l(), l.head(), l.r().l(), l.r().head(), l.r().r(), h, r) : (c == Set.Color.B && r.isTR() && r.l().isTR() ? tr(ord, l, h, r.l().l(), r.l().head(), r.l().r(), r.head(), r.r()) : (c == Set.Color.B && r.isTR() && r.r().isTR() ? tr(ord, l, h, r.l(), r.head(), r.r().l(), r.r().head(), r.r().r()) : new Set.Tree(ord, c, l, h, r))));
   }

   private boolean isTR() {
      return !this.isEmpty() && this.color() == Set.Color.R;
   }

   public final Iterator<A> iterator() {
      return this.toStream().iterator();
   }

   public static <A> Set<A> single(Ord<A> o, A a) {
      return empty(o).insert(a);
   }

   public final <B> Set<B> map(Ord<B> o, F<A, B> f) {
      return iterableSet(o, this.toStream().map(f));
   }

   public final <B> B foldMap(F<A, B> f, Monoid<B> m) {
      return this.isEmpty() ? m.zero() : m.sum(m.sum(this.r().foldMap(f, m), f.f(this.head())), this.l().foldMap(f, m));
   }

   public final List<A> toList() {
      return (List)this.foldMap(List.cons(List.nil()), Monoid.listMonoid());
   }

   public final Stream<A> toStream() {
      return (Stream)this.foldMap(Stream.single(), Monoid.streamMonoid());
   }

   public final <B> Set<B> bind(Ord<B> o, F<A, Set<B>> f) {
      return join(o, this.map(Ord.setOrd(o), f));
   }

   public final Set<A> union(Set<A> s) {
      return iterableSet(this.ord, s.toStream().append(this.toStream()));
   }

   public static <A> F<Set<A>, F<Set<A>, Set<A>>> union() {
      return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() {
         public Set<A> f(Set<A> s1, Set<A> s2) {
            return s1.union(s2);
         }
      });
   }

   public final Set<A> filter(F<A, Boolean> f) {
      return iterableSet(this.ord, this.toStream().filter(f));
   }

   public final Set<A> delete(A a) {
      return this.minus(single(this.ord, a));
   }

   public final F<A, F<Set<A>, Set<A>>> delete() {
      return Function.curry(new F2<A, Set<A>, Set<A>>() {
         public Set<A> f(A a, Set<A> set) {
            return set.delete(a);
         }
      });
   }

   public final Set<A> intersect(Set<A> s) {
      return this.filter((F)member().f(s));
   }

   public static <A> F<Set<A>, F<Set<A>, Set<A>>> intersect() {
      return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() {
         public Set<A> f(Set<A> s1, Set<A> s2) {
            return s1.intersect(s2);
         }
      });
   }

   public final Set<A> minus(Set<A> s) {
      return this.filter(Function.compose(Booleans.not, (F)member().f(s)));
   }

   public static <A> F<Set<A>, F<Set<A>, Set<A>>> minus() {
      return Function.curry(new F2<Set<A>, Set<A>, Set<A>>() {
         public Set<A> f(Set<A> s1, Set<A> s2) {
            return s1.minus(s2);
         }
      });
   }

   public final int size() {
      F<A, Integer> one = Function.constant(1);
      return (Integer)this.foldMap(one, Monoid.intAdditionMonoid);
   }

   public final P3<Set<A>, Option<A>, Set<A>> split(A a) {
      if (this.isEmpty()) {
         return P.p(empty(this.ord), Option.none(), empty(this.ord));
      } else {
         A h = this.head();
         Ordering i = this.ord.compare(a, h);
         P3 lg;
         if (i == Ordering.LT) {
            lg = this.l().split(a);
            return P.p(lg._1(), lg._2(), ((Set)lg._3()).insert(h).union(this.r()));
         } else if (i == Ordering.GT) {
            lg = this.r().split(a);
            return P.p(((Set)lg._1()).insert(h).union(this.l()), lg._2(), lg._3());
         } else {
            return P.p(this.l(), Option.some(h), this.r());
         }
      }
   }

   public final boolean subsetOf(Set<A> s) {
      if (!this.isEmpty() && !s.isEmpty()) {
         P3<Set<A>, Option<A>, Set<A>> find = s.split(this.head());
         return ((Option)find._2()).isSome() && this.l().subsetOf((Set)find._1()) && this.r().subsetOf((Set)find._3());
      } else {
         return this.isEmpty();
      }
   }

   public static <A> Set<A> join(Ord<A> o, Set<Set<A>> s) {
      F<Set<A>, Set<A>> id = Function.identity();
      return (Set)s.foldMap(id, Monoid.setMonoid(o));
   }

   public static <A> Set<A> iterableSet(Ord<A> o, Iterable<A> as) {
      Set<A> s = empty(o);

      Object a;
      for(Iterator var3 = as.iterator(); var3.hasNext(); s = s.insert(a)) {
         a = var3.next();
      }

      return s;
   }

   public static <A> Set<A> set(Ord<A> o, A... as) {
      Set<A> s = empty(o);
      Object[] var3 = as;
      int var4 = as.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         A a = var3[var5];
         s = s.insert(a);
      }

      return s;
   }

   // $FF: synthetic method
   Set(Ord x0, Object x1) {
      this(x0);
   }

   private static final class Tree<A> extends Set<A> {
      private final Set.Color c;
      private final Set<A> a;
      private final A x;
      private final Set<A> b;

      private Tree(Ord<A> ord, Set.Color c, Set<A> a, A x, Set<A> b) {
         super(ord, null);
         this.c = c;
         this.a = a;
         this.x = x;
         this.b = b;
      }

      public Set.Color color() {
         return this.c;
      }

      public Set<A> l() {
         return this.a;
      }

      public A head() {
         return this.x;
      }

      public Set<A> r() {
         return this.b;
      }

      // $FF: synthetic method
      Tree(Ord x0, Set.Color x1, Set x2, Object x3, Set x4, Object x5) {
         this(x0, x1, x2, x3, x4);
      }
   }

   private static final class Empty<A> extends Set<A> {
      private Empty(Ord<A> ord) {
         super(ord, null);
      }

      public Set.Color color() {
         return Set.Color.B;
      }

      public Set<A> l() {
         throw new Error("Left on empty set.");
      }

      public Set<A> r() {
         throw new Error("Right on empty set.");
      }

      public A head() {
         throw new Error("Head on empty set.");
      }

      // $FF: synthetic method
      Empty(Ord x0, Object x1) {
         this(x0);
      }
   }

   private static enum Color {
      R,
      B;
   }
}
