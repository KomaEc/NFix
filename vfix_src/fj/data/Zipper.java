package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F3;
import fj.Function;
import fj.Ord;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.Show;
import fj.function.Integers;
import java.util.Iterator;

public final class Zipper<A> implements Iterable<Zipper<A>> {
   private final Stream<A> left;
   private final A focus;
   private final Stream<A> right;

   private Zipper(Stream<A> left, A focus, Stream<A> right) {
      this.left = left;
      this.focus = focus;
      this.right = right;
   }

   public static <A> Zipper<A> zipper(Stream<A> left, A focus, Stream<A> right) {
      return new Zipper(left, focus, right);
   }

   public static <A> Zipper<A> zipper(P3<Stream<A>, A, Stream<A>> p) {
      return new Zipper((Stream)p._1(), p._2(), (Stream)p._3());
   }

   public static <A> F3<Stream<A>, A, Stream<A>, Zipper<A>> zipper() {
      return new F3<Stream<A>, A, Stream<A>, Zipper<A>>() {
         public Zipper<A> f(Stream<A> l, A a, Stream<A> r) {
            return Zipper.zipper(l, a, r);
         }
      };
   }

   public P3<Stream<A>, A, Stream<A>> p() {
      return P.p(this.left, this.focus, this.right);
   }

   public static <A> F<Zipper<A>, P3<Stream<A>, A, Stream<A>>> p_() {
      return new F<Zipper<A>, P3<Stream<A>, A, Stream<A>>>() {
         public P3<Stream<A>, A, Stream<A>> f(Zipper<A> a) {
            return a.p();
         }
      };
   }

   public static <A> Ord<Zipper<A>> ord(Ord<A> o) {
      Ord<Stream<A>> so = Ord.streamOrd(o);
      return Ord.p3Ord(so, o, so).comap(p_());
   }

   public static <A> Equal<Zipper<A>> eq(Equal<A> e) {
      Equal<Stream<A>> se = Equal.streamEqual(e);
      return Equal.p3Equal(se, e, se).comap(p_());
   }

   public static <A> Show<Zipper<A>> show(Show<A> s) {
      Show<Stream<A>> ss = Show.streamShow(s);
      return Show.p3Show(ss, s, ss).comap(p_());
   }

   public <B> Zipper<B> map(F<A, B> f) {
      return zipper(this.left.map(f), f.f(this.focus), this.right.map(f));
   }

   public <B> B foldRight(F<A, F<B, B>> f, B z) {
      return this.left.foldLeft(Function.flip(f), this.right.cons(this.focus).foldRight(Function.compose((F)Function.andThen().f(P1.__1()), f), z));
   }

   public static <A> Zipper<A> single(A a) {
      return zipper(Stream.nil(), a, Stream.nil());
   }

   public static <A> Option<Zipper<A>> fromStream(Stream<A> a) {
      return a.isEmpty() ? Option.none() : Option.some(zipper(Stream.nil(), a.head(), (Stream)a.tail()._1()));
   }

   public static <A> Option<Zipper<A>> fromStreamEnd(Stream<A> a) {
      if (a.isEmpty()) {
         return Option.none();
      } else {
         Stream<A> xs = a.reverse();
         return Option.some(zipper((Stream)xs.tail()._1(), xs.head(), Stream.nil()));
      }
   }

   public A focus() {
      return this.focus;
   }

   public Option<Zipper<A>> next() {
      return this.right.isEmpty() ? Option.none() : Option.some(this.tryNext());
   }

   public Zipper<A> tryNext() {
      if (this.right.isEmpty()) {
         throw new Error("Tried next at the end of a zipper.");
      } else {
         return zipper(this.left.cons(this.focus), this.right.head(), (Stream)this.right.tail()._1());
      }
   }

   public Option<Zipper<A>> previous() {
      return this.left.isEmpty() ? Option.none() : Option.some(this.tryPrevious());
   }

   public Zipper<A> tryPrevious() {
      if (this.left.isEmpty()) {
         throw new Error("Tried previous at the beginning of a zipper.");
      } else {
         return zipper((Stream)this.left.tail()._1(), this.left.head(), this.right.cons(this.focus));
      }
   }

   public static <A> F<Zipper<A>, Option<Zipper<A>>> next_() {
      return new F<Zipper<A>, Option<Zipper<A>>>() {
         public Option<Zipper<A>> f(Zipper<A> as) {
            return as.next();
         }
      };
   }

   public static <A> F<Zipper<A>, Option<Zipper<A>>> previous_() {
      return new F<Zipper<A>, Option<Zipper<A>>>() {
         public Option<Zipper<A>> f(Zipper<A> as) {
            return as.previous();
         }
      };
   }

   public Zipper<A> insertLeft(A a) {
      return zipper(this.left, a, this.right.cons(this.focus));
   }

   public Zipper<A> insertRight(A a) {
      return zipper(this.left.cons(this.focus), a, this.right);
   }

   public Option<Zipper<A>> deleteLeft() {
      return this.left.isEmpty() && this.right.isEmpty() ? Option.none() : Option.some(zipper(this.left.isEmpty() ? this.left : (Stream)this.left.tail()._1(), this.left.isEmpty() ? this.right.head() : this.left.head(), this.left.isEmpty() ? (Stream)this.right.tail()._1() : this.right));
   }

   public Option<Zipper<A>> deleteRight() {
      return this.left.isEmpty() && this.right.isEmpty() ? Option.none() : Option.some(zipper(this.right.isEmpty() ? (Stream)this.left.tail()._1() : this.left, this.right.isEmpty() ? this.left.head() : this.right.head(), this.right.isEmpty() ? this.right : (Stream)this.right.tail()._1()));
   }

   public Zipper<A> deleteOthers() {
      Stream<A> nil = Stream.nil();
      return zipper(nil, this.focus, nil);
   }

   public int length() {
      return (Integer)this.foldRight(Function.constant(Integers.add.f(1)), 0);
   }

   public boolean atStart() {
      return this.left.isEmpty();
   }

   public boolean atEnd() {
      return this.right.isEmpty();
   }

   public Zipper<Zipper<A>> positions() {
      Stream<Zipper<A>> left = Stream.unfold(new F<Zipper<A>, Option<P2<Zipper<A>, Zipper<A>>>>() {
         public Option<P2<Zipper<A>, Zipper<A>>> f(Zipper<A> p) {
            return p.previous().map(Function.join(P.p2()));
         }
      }, this);
      Stream<Zipper<A>> right = Stream.unfold(new F<Zipper<A>, Option<P2<Zipper<A>, Zipper<A>>>>() {
         public Option<P2<Zipper<A>, Zipper<A>>> f(Zipper<A> p) {
            return p.next().map(Function.join(P.p2()));
         }
      }, this);
      return zipper(left, this, right);
   }

   public <B> Zipper<B> cobind(F<Zipper<A>, B> f) {
      return this.positions().map(f);
   }

   public Zipper<P2<A, Boolean>> zipWithFocus() {
      return zipper(this.left.zip(Stream.repeat(false)), P.p(this.focus, true), this.right.zip(Stream.repeat(false)));
   }

   public Option<Zipper<A>> move(int n) {
      int ll = this.left.length();
      int rl = this.right.length();
      Option<Zipper<A>> p = Option.some(this);
      if (n >= 0 && n < this.length()) {
         int i;
         if (ll >= n) {
            for(i = ll - n; i > 0; --i) {
               p = p.bind(previous_());
            }
         } else if (rl >= n) {
            for(i = rl - n; i > 0; --i) {
               p = p.bind(next_());
            }
         }

         return p;
      } else {
         return Option.none();
      }
   }

   public static <A> F<Integer, F<Zipper<A>, Option<Zipper<A>>>> move() {
      return Function.curry(new F2<Integer, Zipper<A>, Option<Zipper<A>>>() {
         public Option<Zipper<A>> f(Integer i, Zipper<A> a) {
            return a.move(i);
         }
      });
   }

   public Option<Zipper<A>> find(final F<A, Boolean> p) {
      if ((Boolean)p.f(this.focus())) {
         return Option.some(this);
      } else {
         Zipper<Zipper<A>> ps = this.positions();
         return ps.lefts().interleave(ps.rights()).find(new F<Zipper<A>, Boolean>() {
            public Boolean f(Zipper<A> zipper) {
               return (Boolean)p.f(zipper.focus());
            }
         });
      }
   }

   public int index() {
      return this.left.length();
   }

   public Zipper<A> cycleNext() {
      if (this.left.isEmpty() && this.right.isEmpty()) {
         return this;
      } else if (this.right.isEmpty()) {
         Stream<A> xs = this.left.reverse();
         return zipper(Stream.nil(), xs.head(), ((Stream)xs.tail()._1()).snoc(P.p(this.focus)));
      } else {
         return this.tryNext();
      }
   }

   public Zipper<A> cyclePrevious() {
      if (this.left.isEmpty() && this.right.isEmpty()) {
         return this;
      } else if (this.left.isEmpty()) {
         Stream<A> xs = this.right.reverse();
         return zipper(((Stream)xs.tail()._1()).snoc(P.p(this.focus)), xs.head(), Stream.nil());
      } else {
         return this.tryPrevious();
      }
   }

   public Option<Zipper<A>> deleteLeftCycle() {
      if (this.left.isEmpty() && this.right.isEmpty()) {
         return Option.none();
      } else if (this.left.isNotEmpty()) {
         return Option.some(zipper((Stream)this.left.tail()._1(), this.left.head(), this.right));
      } else {
         Stream<A> xs = this.right.reverse();
         return Option.some(zipper((Stream)xs.tail()._1(), xs.head(), Stream.nil()));
      }
   }

   public Option<Zipper<A>> deleteRightCycle() {
      if (this.left.isEmpty() && this.right.isEmpty()) {
         return Option.none();
      } else if (this.right.isNotEmpty()) {
         return Option.some(zipper(this.left, this.right.head(), (Stream)this.right.tail()._1()));
      } else {
         Stream<A> xs = this.left.reverse();
         return Option.some(zipper(Stream.nil(), xs.head(), (Stream)xs.tail()._1()));
      }
   }

   public Zipper<A> replace(A a) {
      return zipper(this.left, a, this.right);
   }

   public Stream<A> toStream() {
      return this.left.reverse().snoc(P.p(this.focus)).append(this.right);
   }

   public Stream<A> lefts() {
      return this.left;
   }

   public Stream<A> rights() {
      return this.right;
   }

   public <B, C> Zipper<C> zipWith(Zipper<B> bs, F2<A, B, C> f) {
      return (Zipper)F2Functions.zipZipperM(f).f(this, bs);
   }

   public <B, C> Zipper<C> zipWith(Zipper<B> bs, F<A, F<B, C>> f) {
      return this.zipWith(bs, Function.uncurryF2(f));
   }

   public Iterator<Zipper<A>> iterator() {
      return this.positions().toStream().iterator();
   }
}
