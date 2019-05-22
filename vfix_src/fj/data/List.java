package fj.data;

import fj.Bottom;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F3;
import fj.Function;
import fj.Hash;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Show;
import fj.Unit;
import fj.control.Trampoline;
import fj.function.Booleans;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class List<A> implements Iterable<A> {
   private List() {
   }

   public final Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   public abstract A head();

   public abstract List<A> tail();

   public final int length() {
      return (Integer)this.foldLeft((F)(new F<Integer, F<A, Integer>>() {
         public F<A, Integer> f(final Integer i) {
            return new F<A, Integer>() {
               public Integer f(A a) {
                  return i + 1;
               }
            };
         }
      }), 0);
   }

   public final boolean isEmpty() {
      return this instanceof List.Nil;
   }

   public final boolean isNotEmpty() {
      return this instanceof List.Cons;
   }

   public final <B> B list(B nil, F<A, F<List<A>, B>> cons) {
      return this.isEmpty() ? nil : ((F)cons.f(this.head())).f(this.tail());
   }

   public final A orHead(P1<A> a) {
      return this.isEmpty() ? a._1() : this.head();
   }

   public final List<A> orTail(P1<List<A>> as) {
      return this.isEmpty() ? (List)as._1() : this.tail();
   }

   public final Option<A> toOption() {
      return this.isEmpty() ? Option.none() : Option.some(this.head());
   }

   public final <X> Either<X, A> toEither(P1<X> x) {
      return this.isEmpty() ? Either.left(x._1()) : Either.right(this.head());
   }

   public final Stream<A> toStream() {
      Stream<A> nil = Stream.nil();
      return (Stream)this.foldRight((F)(new F<A, F<Stream<A>, Stream<A>>>() {
         public F<Stream<A>, Stream<A>> f(final A a) {
            return new F<Stream<A>, Stream<A>>() {
               public Stream<A> f(Stream<A> as) {
                  return as.cons(a);
               }
            };
         }
      }), nil);
   }

   public final Array<A> toArray() {
      Object[] a = new Object[this.length()];
      List<A> x = this;

      for(int i = 0; i < this.length(); ++i) {
         a[i] = x.head();
         x = x.tail();
      }

      return Array.mkArray(a);
   }

   public final Array<A> toArray(Class<A[]> c) {
      A[] a = (Object[])((Object[])java.lang.reflect.Array.newInstance(c.getComponentType(), this.length()));
      List<A> x = this;

      for(int i = 0; i < this.length(); ++i) {
         a[i] = x.head();
         x = x.tail();
      }

      return Array.array(a);
   }

   public final A[] array(Class<A[]> c) {
      return this.toArray(c).array(c);
   }

   public final List<A> cons(A a) {
      return new List.Cons(a, this);
   }

   public final List<A> conss(A a) {
      return new List.Cons(a, this);
   }

   public final <B> List<B> map(F<A, B> f) {
      List.Buffer<B> bs = List.Buffer.empty();

      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         bs.snoc(f.f(xs.head()));
      }

      return bs.toList();
   }

   public final Unit foreach(F<A, Unit> f) {
      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         f.f(xs.head());
      }

      return Unit.unit();
   }

   public final void foreachDoEffect(Effect1<A> f) {
      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         f.f(xs.head());
      }

   }

   public final List<A> filter(F<A, Boolean> f) {
      List.Buffer<A> b = List.Buffer.empty();

      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         A h = xs.head();
         if ((Boolean)f.f(h)) {
            b.snoc(h);
         }
      }

      return b.toList();
   }

   public final List<A> removeAll(F<A, Boolean> f) {
      return this.filter(Function.compose(Booleans.not, f));
   }

   public final List<A> delete(A a, Equal<A> e) {
      P2<List<A>, List<A>> p = this.span(Function.compose(Booleans.not, e.eq(a)));
      return ((List)p._2()).isEmpty() ? (List)p._1() : ((List)p._1()).append(((List)p._2()).tail());
   }

   public final List<A> takeWhile(F<A, Boolean> f) {
      List.Buffer<A> b = List.Buffer.empty();
      boolean taking = true;

      for(List xs = this; xs.isNotEmpty() && taking; xs = xs.tail()) {
         A h = xs.head();
         if ((Boolean)f.f(h)) {
            b.snoc(h);
         } else {
            taking = false;
         }
      }

      return b.toList();
   }

   public final List<A> dropWhile(F<A, Boolean> f) {
      List xs;
      for(xs = this; xs.isNotEmpty() && (Boolean)f.f(xs.head()); xs = xs.tail()) {
      }

      return xs;
   }

   public final P2<List<A>, List<A>> span(F<A, Boolean> p) {
      List.Buffer<A> b = List.Buffer.empty();

      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         if (!(Boolean)p.f(xs.head())) {
            return P.p(b.toList(), xs);
         }

         b.snoc(xs.head());
      }

      return P.p(b.toList(), nil());
   }

   public final P2<List<A>, List<A>> breakk(final F<A, Boolean> p) {
      return this.span(new F<A, Boolean>() {
         public Boolean f(A a) {
            return !(Boolean)p.f(a);
         }
      });
   }

   public final List<List<A>> group(Equal<A> e) {
      if (this.isEmpty()) {
         return nil();
      } else {
         P2<List<A>, List<A>> z = this.tail().span(e.eq(this.head()));
         return cons(((List)z._1()).cons(this.head()), ((List)z._2()).group(e));
      }
   }

   public final <B> List<B> bind(F<A, List<B>> f) {
      List.Buffer<B> b = List.Buffer.empty();

      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         b.append((List)f.f(xs.head()));
      }

      return b.toList();
   }

   public final <B, C> List<C> bind(List<B> lb, F<A, F<B, C>> f) {
      return lb.apply(this.map(f));
   }

   public final <B, C> List<C> bind(List<B> lb, F2<A, B, C> f) {
      return this.bind(lb, Function.curry(f));
   }

   public static <A, B, C> F<List<A>, F<List<B>, List<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<List<A>, List<B>, List<C>>() {
         public List<C> f(List<A> as, List<B> bs) {
            return as.bind(bs, f);
         }
      });
   }

   public final <B, C, D> List<D> bind(List<B> lb, List<C> lc, F<A, F<B, F<C, D>>> f) {
      return lc.apply(this.bind(lb, f));
   }

   public final <B, C, D, E> List<E> bind(List<B> lb, List<C> lc, List<D> ld, F<A, F<B, F<C, F<D, E>>>> f) {
      return ld.apply(this.bind(lb, lc, f));
   }

   public final <B, C, D, E, F$> List<F$> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
      return le.apply(this.bind(lb, lc, ld, f));
   }

   public final <B, C, D, E, F$, G> List<G> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
      return lf.apply(this.bind(lb, lc, ld, le, f));
   }

   public final <B, C, D, E, F$, G, H> List<H> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, List<G> lg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
      return lg.apply(this.bind(lb, lc, ld, le, lf, f));
   }

   public final <B, C, D, E, F$, G, H, I> List<I> bind(List<B> lb, List<C> lc, List<D> ld, List<E> le, List<F$> lf, List<G> lg, List<H> lh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
      return lh.apply(this.bind(lb, lc, ld, le, lf, lg, f));
   }

   public final <B> List<B> sequence(List<B> bs) {
      F<A, List<B>> c = Function.constant(bs);
      return this.bind(c);
   }

   public final <B> List<B> apply(List<F<A, B>> lf) {
      return lf.bind(new F<F<A, B>, List<B>>() {
         public List<B> f(F<A, B> f) {
            return List.this.map(f);
         }
      });
   }

   public final List<A> append(List<A> as) {
      return List.Buffer.fromList(this).append(as).toList();
   }

   public final <B> B foldRight(F<A, F<B, B>> f, B b) {
      return this.isEmpty() ? b : ((F)f.f(this.head())).f(this.tail().foldRight(f, b));
   }

   public final <B> B foldRight(F2<A, B, B> f, B b) {
      return this.foldRight(Function.curry(f), b);
   }

   public final <B> Trampoline<B> foldRightC(final F2<A, B, B> f, final B b) {
      return Trampoline.suspend(new P1<Trampoline<B>>() {
         public Trampoline<B> _1() {
            return List.this.isEmpty() ? Trampoline.pure(b) : List.this.tail().foldRightC(f, b).map(F2Functions.f(f, List.this.head()));
         }
      });
   }

   public final <B> B foldLeft(F<B, F<A, B>> f, B b) {
      B x = b;

      for(List xs = this; !xs.isEmpty(); xs = xs.tail()) {
         x = ((F)f.f(x)).f(xs.head());
      }

      return x;
   }

   public final <B> B foldLeft(F2<B, A, B> f, B b) {
      return this.foldLeft(Function.curry(f), b);
   }

   public final A foldLeft1(F2<A, A, A> f) {
      return this.foldLeft1(Function.curry(f));
   }

   public final A foldLeft1(F<A, F<A, A>> f) {
      if (this.isEmpty()) {
         throw Bottom.error("Undefined: foldLeft1 on empty list");
      } else {
         return this.tail().foldLeft(f, this.head());
      }
   }

   public final List<A> reverse() {
      return (List)this.foldLeft((F)(new F<List<A>, F<A, List<A>>>() {
         public F<A, List<A>> f(final List<A> as) {
            return new F<A, List<A>>() {
               public List<A> f(A a) {
                  return List.cons(a, as);
               }
            };
         }
      }), nil());
   }

   public final A index(int i) {
      if (i >= 0 && i <= this.length() - 1) {
         List<A> xs = this;

         for(int c = 0; c < i; ++c) {
            xs = xs.tail();
         }

         return xs.head();
      } else {
         throw Bottom.error("index " + i + " out of range on list with length " + this.length());
      }
   }

   public final List<A> take(int i) {
      return i > 0 && !this.isEmpty() ? cons(this.head(), this.tail().take(i - 1)) : nil();
   }

   public final List<A> drop(int i) {
      int c = 0;

      List xs;
      for(xs = this; xs.isNotEmpty() && c < i; xs = xs.tail()) {
         ++c;
      }

      return xs;
   }

   public final P2<List<A>, List<A>> splitAt(int i) {
      P2<List<A>, List<A>> s = P.p(nil(), nil());
      int c = 0;

      for(List xs = this; xs.isNotEmpty(); xs = xs.tail()) {
         final A h = xs.head();
         s = c < i ? s.map1(new F<List<A>, List<A>>() {
            public List<A> f(List<A> as) {
               return as.snoc(h);
            }
         }) : s.map2(new F<List<A>, List<A>>() {
            public List<A> f(List<A> as) {
               return as.snoc(h);
            }
         });
         ++c;
      }

      return s;
   }

   public final List<List<A>> partition(final int n) {
      if (n < 1) {
         throw Bottom.error("Can't create list partitions shorter than 1 element long.");
      } else if (this.isEmpty()) {
         throw Bottom.error("Partition on empty list.");
      } else {
         return unfold(new F<List<A>, Option<P2<List<A>, List<A>>>>() {
            public Option<P2<List<A>, List<A>>> f(List<A> as) {
               return as.isEmpty() ? Option.none() : Option.some(as.splitAt(n));
            }
         }, this);
      }
   }

   public final List<List<A>> inits() {
      List<List<A>> s = single(nil());
      if (this.isNotEmpty()) {
         s = s.append(this.tail().inits().map((F)cons().f(this.head())));
      }

      return s;
   }

   public final List<List<A>> tails() {
      return this.isEmpty() ? single(nil()) : cons(this, this.tail().tails());
   }

   public final List<A> sort(Ord<A> o) {
      if (this.isEmpty()) {
         return nil();
      } else if (this.tail().isEmpty()) {
         return this;
      } else {
         P2<List<A>, List<A>> s = this.splitAt(this.length() / 2);

         final class Merge<A> {
            List<A> merge(List<A> xs, List<A> ys, Ord<A> o) {
               List.Buffer buf = List.Buffer.empty();

               while(true) {
                  if (xs.isEmpty()) {
                     buf.append(ys);
                     break;
                  }

                  if (ys.isEmpty()) {
                     buf.append(xs);
                     break;
                  }

                  A x = xs.head();
                  A y = ys.head();
                  if (o.isLessThan(x, y)) {
                     buf.snoc(x);
                     xs = xs.tail();
                  } else {
                     buf.snoc(y);
                     ys = ys.tail();
                  }
               }

               return buf.toList();
            }
         }

         return (new Merge()).merge(((List)s._1()).sort(o), ((List)s._2()).sort(o), o);
      }
   }

   public final <B, C> List<C> zipWith(List<B> bs, F<A, F<B, C>> f) {
      List.Buffer<C> buf = List.Buffer.empty();

      for(List as = this; as.isNotEmpty() && bs.isNotEmpty(); bs = bs.tail()) {
         buf.snoc(((F)f.f(as.head())).f(bs.head()));
         as = as.tail();
      }

      return buf.toList();
   }

   public final <B, C> List<C> zipWith(List<B> bs, F2<A, B, C> f) {
      return this.zipWith(bs, Function.curry(f));
   }

   public static <A, B, C> F<List<A>, F<List<B>, F<F<A, F<B, C>>, List<C>>>> zipWith() {
      return Function.curry(new F3<List<A>, List<B>, F<A, F<B, C>>, List<C>>() {
         public List<C> f(List<A> as, List<B> bs, F<A, F<B, C>> f) {
            return as.zipWith(bs, f);
         }
      });
   }

   public final <B> List<P2<A, B>> zip(List<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(bs, __2);
   }

   public static <A, B> F<List<A>, F<List<B>, List<P2<A, B>>>> zip() {
      return Function.curry(new F2<List<A>, List<B>, List<P2<A, B>>>() {
         public List<P2<A, B>> f(List<A> as, List<B> bs) {
            return as.zip(bs);
         }
      });
   }

   public final List<P2<A, Integer>> zipIndex() {
      return this.zipWith(range(0, this.length()), new F<A, F<Integer, P2<A, Integer>>>() {
         public F<Integer, P2<A, Integer>> f(final A a) {
            return new F<Integer, P2<A, Integer>>() {
               public P2<A, Integer> f(Integer i) {
                  return P.p(a, i);
               }
            };
         }
      });
   }

   public final List<A> snoc(A a) {
      return List.Buffer.fromList(this).snoc(a).toList();
   }

   public final boolean forall(F<A, Boolean> f) {
      return this.isEmpty() || (Boolean)f.f(this.head()) && this.tail().forall(f);
   }

   public final boolean exists(F<A, Boolean> f) {
      return this.find(f).isSome();
   }

   public final Option<A> find(F<A, Boolean> f) {
      for(List as = this; as.isNotEmpty(); as = as.tail()) {
         if ((Boolean)f.f(as.head())) {
            return Option.some(as.head());
         }
      }

      return Option.none();
   }

   public final List<A> intersperse(A a) {
      return !this.isEmpty() && !this.tail().isEmpty() ? cons(this.head(), cons(a, this.tail().intersperse(a))) : this;
   }

   public final List<A> intercalate(List<List<A>> as) {
      return join(as.intersperse(this));
   }

   public final List<A> nub() {
      return this.nub(Equal.anyEqual());
   }

   public final List<A> nub(final Equal<A> eq) {
      return this.isEmpty() ? this : cons(this.head(), this.tail().filter(new F<A, Boolean>() {
         public Boolean f(A a) {
            return !eq.eq(a, List.this.head());
         }
      }).nub(eq));
   }

   public final List<A> nub(Ord<A> o) {
      return this.sort(o).group(o.equal()).map(head_());
   }

   public static <A> F<List<A>, A> head_() {
      return new F<List<A>, A>() {
         public A f(List<A> list) {
            return list.head();
         }
      };
   }

   public static <A> F<List<A>, List<A>> tail_() {
      return new F<List<A>, List<A>>() {
         public List<A> f(List<A> list) {
            return list.tail();
         }
      };
   }

   public final List<A> minus(Equal<A> eq, List<A> xs) {
      return this.removeAll(Function.compose(Monoid.disjunctionMonoid.sumLeft(), xs.mapM(Function.curry(eq.eq()))));
   }

   public final <B, C> F<B, List<C>> mapM(F<A, F<B, C>> f) {
      return sequence_(this.map(f));
   }

   public final <B> Option<List<B>> mapMOption(final F<A, Option<B>> f) {
      return (Option)this.foldRight((F2)(new F2<A, Option<List<B>>, Option<List<B>>>() {
         public Option<List<B>> f(A a, final Option<List<B>> bs) {
            return ((Option)f.f(a)).bind(new F<B, Option<List<B>>>() {
               public Option<List<B>> f(final B b) {
                  return bs.map(new F<List<B>, List<B>>() {
                     public List<B> f(List<B> bbs) {
                        return bbs.cons(b);
                     }
                  });
               }
            });
         }
      }), Option.some(nil()));
   }

   public final <B> Trampoline<List<B>> mapMTrampoline(final F<A, Trampoline<B>> f) {
      return (Trampoline)this.foldRight((F2)(new F2<A, Trampoline<List<B>>, Trampoline<List<B>>>() {
         public Trampoline<List<B>> f(A a, final Trampoline<List<B>> bs) {
            return ((Trampoline)f.f(a)).bind(new F<B, Trampoline<List<B>>>() {
               public Trampoline<List<B>> f(final B b) {
                  return bs.map(new F<List<B>, List<B>>() {
                     public List<B> f(List<B> bbs) {
                        return bbs.cons(b);
                     }
                  });
               }
            });
         }
      }), Trampoline.pure(nil()));
   }

   public final Option<Integer> elementIndex(Equal<A> e, A a) {
      return lookup(e, this.zipIndex(), a);
   }

   public final A last() {
      A a = this.head();

      for(List xs = this.tail(); xs.isNotEmpty(); xs = xs.tail()) {
         a = xs.head();
      }

      return a;
   }

   public final List<A> init() {
      List<A> ys = this;

      List.Buffer a;
      for(a = List.Buffer.empty(); ys.isNotEmpty() && ys.tail().isNotEmpty(); ys = ys.tail()) {
         a.snoc(this.head());
      }

      return a.toList();
   }

   public final List<A> insertBy(F<A, F<A, Ordering>> f, A x) {
      List<A> ys = this;

      List.Buffer xs;
      for(xs = List.Buffer.empty(); ys.isNotEmpty() && ((F)f.f(x)).f(ys.head()) == Ordering.GT; ys = ys.tail()) {
         xs = xs.snoc(ys.head());
      }

      return xs.append(ys.cons(x)).toList();
   }

   public final A mode(Ord<A> o) {
      return ((List)this.sort(o).group(o.equal()).maximum(Ord.intOrd.comap(length_()))).head();
   }

   public final <B> TreeMap<B, List<A>> groupBy(F<A, B> keyFunction) {
      return this.groupBy(keyFunction, Ord.hashOrd());
   }

   public final <B> TreeMap<B, List<A>> groupBy(F<A, B> keyFunction, Ord<B> keyOrd) {
      return this.groupBy(keyFunction, Function.identity(), keyOrd);
   }

   public final <B, C> TreeMap<B, List<C>> groupBy(F<A, B> keyFunction, F<A, C> valueFunction) {
      return this.groupBy(keyFunction, valueFunction, Ord.hashOrd());
   }

   public final <B, C> TreeMap<B, List<C>> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, Ord<B> keyOrd) {
      return this.groupBy(keyFunction, valueFunction, nil(), List$$Lambda$1.lambdaFactory$(), keyOrd);
   }

   public final <B, C> TreeMap<B, C> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, Monoid<C> monoid, Ord<B> keyOrd) {
      return this.groupBy(keyFunction, valueFunction, monoid.zero(), Function.uncurryF2(monoid.sum()), keyOrd);
   }

   public final <B, C, D> TreeMap<B, D> groupBy(F<A, B> keyFunction, F<A, C> valueFunction, D groupingIdentity, F2<C, D, D> groupingAcc, Ord<B> keyOrd) {
      return (TreeMap)this.foldLeft((F)List$$Lambda$2.lambdaFactory$(keyFunction, valueFunction, groupingAcc, groupingIdentity), TreeMap.empty(keyOrd));
   }

   public boolean allEqual(Equal<A> eq) {
      return this.isEmpty() || this.tail().isEmpty() || eq.eq(this.head(), this.tail().head()) && this.tail().allEqual(eq);
   }

   public static <A> F<List<A>, Integer> length_() {
      return new F<List<A>, Integer>() {
         public Integer f(List<A> a) {
            return a.length();
         }
      };
   }

   public final A maximum(Ord<A> o) {
      return this.foldLeft1(o.max);
   }

   public final A minimum(Ord<A> o) {
      return this.foldLeft1(o.min);
   }

   public final Collection<A> toCollection() {
      return new AbstractCollection<A>() {
         public Iterator<A> iterator() {
            return new Iterator<A>() {
               private List<A> xs = List.this;

               public boolean hasNext() {
                  return this.xs.isNotEmpty();
               }

               public A next() {
                  if (this.xs.isEmpty()) {
                     throw new NoSuchElementException();
                  } else {
                     A a = this.xs.head();
                     this.xs = this.xs.tail();
                     return a;
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }

         public int size() {
            return List.this.length();
         }
      };
   }

   public static <A> List<A> list(A... as) {
      return Array.array(as).toList();
   }

   public static <A> List<A> nil() {
      return new List.Nil();
   }

   public static <A> F<A, F<List<A>, List<A>>> cons() {
      return new F<A, F<List<A>, List<A>>>() {
         public F<List<A>, List<A>> f(final A a) {
            return new F<List<A>, List<A>>() {
               public List<A> f(List<A> tail) {
                  return List.cons(a, tail);
               }
            };
         }
      };
   }

   public static <A> F2<A, List<A>, List<A>> cons_() {
      return List$$Lambda$3.lambdaFactory$();
   }

   public static <A> F<A, List<A>> cons(final List<A> tail) {
      return new F<A, List<A>>() {
         public List<A> f(A a) {
            return tail.cons(a);
         }
      };
   }

   public static <A> F<List<A>, List<A>> cons_(final A a) {
      return new F<List<A>, List<A>>() {
         public List<A> f(List<A> as) {
            return as.cons(a);
         }
      };
   }

   public static <A> List<A> cons(A head, List<A> tail) {
      return new List.Cons(head, tail);
   }

   public static <A> F<List<A>, Boolean> isEmpty_() {
      return new F<List<A>, Boolean>() {
         public Boolean f(List<A> as) {
            return as.isEmpty();
         }
      };
   }

   public static <A> F<List<A>, Boolean> isNotEmpty_() {
      return new F<List<A>, Boolean>() {
         public Boolean f(List<A> as) {
            return as.isNotEmpty();
         }
      };
   }

   public static <A> List<A> join(List<List<A>> o) {
      F<List<A>, List<A>> id = Function.identity();
      return o.bind(id);
   }

   public static <A> F<List<List<A>>, List<A>> join() {
      return new F<List<List<A>>, List<A>>() {
         public List<A> f(List<List<A>> as) {
            return List.join(as);
         }
      };
   }

   public static <A, B> List<A> unfold(F<B, Option<P2<A, B>>> f, B b) {
      List.Buffer<A> buf = List.Buffer.empty();

      for(Option o = (Option)f.f(b); o.isSome(); o = (Option)f.f(((P2)o.some())._2())) {
         buf = buf.snoc(((P2)o.some())._1());
      }

      return buf.toList();
   }

   public static <A, B> P2<List<A>, List<B>> unzip(List<P2<A, B>> xs) {
      List.Buffer<A> ba = List.Buffer.empty();
      List.Buffer<B> bb = List.Buffer.empty();

      P2 p;
      for(Iterator var3 = xs.iterator(); var3.hasNext(); bb = bb.snoc(p._2())) {
         p = (P2)var3.next();
         ba = ba.snoc(p._1());
      }

      return P.p(ba.toList(), bb.toList());
   }

   public static <A> List<A> replicate(int n, A a) {
      return n <= 0 ? nil() : replicate(n - 1, a).cons(a);
   }

   public static List<Integer> range(int from, int to) {
      return from >= to ? nil() : cons(from, range(from + 1, to));
   }

   public static List<Character> fromString(String s) {
      List<Character> cs = nil();

      for(int i = s.length() - 1; i >= 0; --i) {
         cs = cons(s.charAt(i), cs);
      }

      return cs;
   }

   public static F<String, List<Character>> fromString() {
      return new F<String, List<Character>>() {
         public List<Character> f(String s) {
            return List.fromString(s);
         }
      };
   }

   public static String asString(List<Character> cs) {
      final StringBuilder sb = new StringBuilder();
      cs.foreach(new F<Character, Unit>() {
         public Unit f(Character c) {
            sb.append(c);
            return Unit.unit();
         }
      });
      return sb.toString();
   }

   public static F<List<Character>, String> asString() {
      return new F<List<Character>, String>() {
         public String f(List<Character> cs) {
            return List.asString(cs);
         }
      };
   }

   public static <A> List<A> single(A a) {
      return cons(a, nil());
   }

   public static <A> List<A> iterateWhile(final F<A, A> f, final F<A, Boolean> p, A a) {
      return unfold(new F<A, Option<P2<A, A>>>() {
         public Option<P2<A, A>> f(final A o) {
            return Option.iif(new F<P2<A, A>, Boolean>() {
               public Boolean f(P2<A, A> p2) {
                  return (Boolean)p.f(o);
               }
            }, P.p(o, f.f(o)));
         }
      }, a);
   }

   public static <A, B> Option<B> lookup(final Equal<A> e, List<P2<A, B>> x, final A a) {
      return x.find(new F<P2<A, B>, Boolean>() {
         public Boolean f(P2<A, B> p) {
            return e.eq(p._1(), a);
         }
      }).map(P2.__2());
   }

   public static <A, B> F2<List<P2<A, B>>, A, Option<B>> lookup(final Equal<A> e) {
      return new F2<List<P2<A, B>>, A, Option<B>>() {
         public Option<B> f(List<P2<A, B>> x, A a) {
            return List.lookup(e, x, a);
         }
      };
   }

   public static <A, B> F<F<A, List<B>>, F<List<A>, List<B>>> bind_() {
      return Function.curry(new F2<F<A, List<B>>, List<A>, List<B>>() {
         public List<B> f(F<A, List<B>> f, List<A> as) {
            return as.bind(f);
         }
      });
   }

   public static <A, B> F<F<A, B>, F<List<A>, List<B>>> map_() {
      return Function.curry(new F2<F<A, B>, List<A>, List<B>>() {
         public List<B> f(F<A, B> f, List<A> as) {
            return as.map(f);
         }
      });
   }

   public static <A, B> F<B, List<A>> sequence_(List<F<B, A>> fs) {
      return (F)fs.foldRight((F)Function.lift(cons()), Function.constant(nil()));
   }

   public static <A, B> F<F<B, F<A, B>>, F<B, F<List<A>, B>>> foldLeft() {
      return Function.curry(new F3<F<B, F<A, B>>, B, List<A>, B>() {
         public B f(F<B, F<A, B>> f, B b, List<A> as) {
            return as.foldLeft(f, b);
         }
      });
   }

   public static <A> F<Integer, F<List<A>, List<A>>> take() {
      return Function.curry(new F2<Integer, List<A>, List<A>>() {
         public List<A> f(Integer n, List<A> as) {
            return as.take(n);
         }
      });
   }

   public static <A> List<A> iterableList(Iterable<A> i) {
      List.Buffer<A> bs = List.Buffer.empty();
      Iterator var2 = i.iterator();

      while(var2.hasNext()) {
         A a = var2.next();
         bs.snoc(a);
      }

      return bs.toList();
   }

   public boolean equals(Object obj) {
      return obj != null && obj instanceof List ? Equal.listEqual(Equal.anyEqual()).eq(this, (List)obj) : false;
   }

   public int hashCode() {
      return Hash.listHash(Hash.anyHash()).hash((Object)this);
   }

   public String toString() {
      return (String)Show.listShow(Show.anyShow()).show((Object)this).foldLeft((F2)(new F2<String, Character, String>() {
         public String f(String s, Character c) {
            return s + c;
         }
      }), "");
   }

   // $FF: synthetic method
   private static List lambda$cons_$38(Object a, List listA) {
      return cons(a, listA);
   }

   // $FF: synthetic method
   private static F lambda$groupBy$37(F var0, F var1, F2 var2, Object var3, TreeMap map) {
      return List$$Lambda$4.lambdaFactory$(var0, var1, map, var2, var3);
   }

   // $FF: synthetic method
   private static TreeMap lambda$null$36(F var0, F var1, TreeMap var2, F2 var3, Object var4, Object element) {
      B key = var0.f(element);
      C value = var1.f(element);
      return var2.set(key, var2.get(key).map(List$$Lambda$5.lambdaFactory$(var3, value)).orSome(var3.f(value, var4)));
   }

   // $FF: synthetic method
   private static Object lambda$null$35(F2 var0, Object var1, Object existing) {
      return var0.f(var1, existing);
   }

   // $FF: synthetic method
   List(Object x0) {
      this();
   }

   // $FF: synthetic method
   static List access$lambda$0(Object var0, List var1) {
      return cons(var0, var1);
   }

   // $FF: synthetic method
   static F access$lambda$1(F var0, F var1, F2 var2, Object var3, TreeMap var4) {
      return lambda$groupBy$37(var0, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   static List access$lambda$2(Object var0, List var1) {
      return lambda$cons_$38(var0, var1);
   }

   // $FF: synthetic method
   static TreeMap access$lambda$3(F var0, F var1, TreeMap var2, F2 var3, Object var4, Object var5) {
      return lambda$null$36(var0, var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   static Object access$lambda$4(F2 var0, Object var1, Object var2) {
      return lambda$null$35(var0, var1, var2);
   }

   public static final class Buffer<A> implements Iterable<A> {
      private List<A> start = List.nil();
      private List.Cons<A> tail;
      private boolean exported;

      public Iterator<A> iterator() {
         return this.start.iterator();
      }

      public List.Buffer<A> snoc(A a) {
         if (this.exported) {
            this.copy();
         }

         List.Cons<A> t = new List.Cons(a, List.nil());
         if (this.tail == null) {
            this.start = t;
         } else {
            this.tail.tail(t);
         }

         this.tail = t;
         return this;
      }

      public List.Buffer<A> append(List<A> as) {
         for(List xs = as; xs.isNotEmpty(); xs = xs.tail()) {
            this.snoc(xs.head());
         }

         return this;
      }

      public List<A> toList() {
         this.exported = !this.start.isEmpty();
         return this.start;
      }

      public Collection<A> toCollection() {
         return this.start.toCollection();
      }

      public static <A> List.Buffer<A> empty() {
         return new List.Buffer();
      }

      public static <A> List.Buffer<A> fromList(List<A> as) {
         List.Buffer<A> b = new List.Buffer();

         for(List xs = as; xs.isNotEmpty(); xs = xs.tail()) {
            b.snoc(xs.head());
         }

         return b;
      }

      public static <A> List.Buffer<A> iterableBuffer(Iterable<A> i) {
         List.Buffer<A> b = empty();
         Iterator var2 = i.iterator();

         while(var2.hasNext()) {
            A a = var2.next();
            b.snoc(a);
         }

         return b;
      }

      private void copy() {
         List<A> s = this.start;
         List.Cons<A> t = this.tail;
         this.start = List.nil();

         for(this.exported = false; s != t; s = s.tail()) {
            this.snoc(s.head());
         }

         if (t != null) {
            this.snoc(t.head());
         }

      }
   }

   private static final class Cons<A> extends List<A> {
      private final A head;
      private List<A> tail;

      Cons(A head, List<A> tail) {
         super(null);
         this.head = head;
         this.tail = tail;
      }

      public A head() {
         return this.head;
      }

      public List<A> tail() {
         return this.tail;
      }

      private void tail(List<A> tail) {
         this.tail = tail;
      }
   }

   private static final class Nil<A> extends List<A> {
      private Nil() {
         super(null);
      }

      public A head() {
         throw Bottom.error("head on empty list");
      }

      public List<A> tail() {
         throw Bottom.error("tail on empty list");
      }

      // $FF: synthetic method
      Nil(Object x0) {
         this();
      }
   }
}
