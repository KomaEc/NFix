package fj.data;

import fj.Bottom;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.function.Booleans;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Stream<A> implements Iterable<A> {
   private Stream() {
   }

   public final Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   public abstract A head();

   public abstract P1<Stream<A>> tail();

   public final boolean isEmpty() {
      return this instanceof Stream.Nil;
   }

   public final boolean isNotEmpty() {
      return this instanceof Stream.Cons;
   }

   public final <B> B stream(B nil, F<A, F<P1<Stream<A>>, B>> cons) {
      return this.isEmpty() ? nil : ((F)cons.f(this.head())).f(this.tail());
   }

   public final <B> B foldRight(final F<A, F<P1<B>, B>> f, final B b) {
      return this.isEmpty() ? b : ((F)f.f(this.head())).f(new P1<B>() {
         public B _1() {
            return ((Stream)Stream.this.tail()._1()).foldRight(f, b);
         }
      });
   }

   public final <B> B foldRight(F2<A, P1<B>, B> f, B b) {
      return this.foldRight(Function.curry(f), b);
   }

   public final <B> B foldRight1(F<A, F<B, B>> f, B b) {
      return this.foldRight(Function.compose((F)Function.andThen().f(P1.__1()), f), b);
   }

   public final <B> B foldRight1(F2<A, B, B> f, B b) {
      return this.foldRight1(Function.curry(f), b);
   }

   public final <B> B foldLeft(F<B, F<A, B>> f, B b) {
      B x = b;

      for(Stream xs = this; !xs.isEmpty(); xs = (Stream)xs.tail()._1()) {
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
         return ((Stream)this.tail()._1()).foldLeft(f, this.head());
      }
   }

   public final A orHead(P1<A> a) {
      return this.isEmpty() ? a._1() : this.head();
   }

   public final P1<Stream<A>> orTail(P1<Stream<A>> as) {
      return this.isEmpty() ? as : this.tail();
   }

   public final Stream<A> intersperse(final A a) {
      return this.isEmpty() ? this : cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return this.prefix(a, (Stream)Stream.this.tail()._1());
         }

         public Stream<A> prefix(A x, final Stream<A> xs) {
            return xs.isEmpty() ? xs : Stream.cons(x, P.p(Stream.cons(xs.head(), new P1<Stream<A>>() {
               public Stream<A> _1() {
                  return prefix(a, (Stream)xs.tail()._1());
               }
            })));
         }
      });
   }

   public final <B> Stream<B> map(final F<A, B> f) {
      return this.isEmpty() ? nil() : cons(f.f(this.head()), new P1<Stream<B>>() {
         public Stream<B> _1() {
            return ((Stream)Stream.this.tail()._1()).map(f);
         }
      });
   }

   public static <A, B> F<F<A, B>, F<Stream<A>, Stream<B>>> map_() {
      return new F<F<A, B>, F<Stream<A>, Stream<B>>>() {
         public F<Stream<A>, Stream<B>> f(final F<A, B> f) {
            return new F<Stream<A>, Stream<B>>() {
               public Stream<B> f(Stream<A> as) {
                  return as.map(f);
               }
            };
         }
      };
   }

   public final Unit foreach(F<A, Unit> f) {
      for(Stream xs = this; xs.isNotEmpty(); xs = (Stream)xs.tail()._1()) {
         f.f(xs.head());
      }

      return Unit.unit();
   }

   public final void foreachDoEffect(Effect1<A> f) {
      for(Stream xs = this; xs.isNotEmpty(); xs = (Stream)xs.tail()._1()) {
         f.f(xs.head());
      }

   }

   public final Stream<A> filter(final F<A, Boolean> f) {
      final Stream<A> as = this.dropWhile(Booleans.not(f));
      return as.isNotEmpty() ? cons(as.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return ((Stream)as.tail()._1()).filter(f);
         }
      }) : as;
   }

   public final Stream<A> append(final Stream<A> as) {
      return this.isEmpty() ? as : cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return ((Stream)Stream.this.tail()._1()).append(as);
         }
      });
   }

   public final Stream<A> append(final P1<Stream<A>> as) {
      return this.isEmpty() ? (Stream)as._1() : cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return ((Stream)Stream.this.tail()._1()).append(as);
         }
      });
   }

   public final Stream<A> minus(Equal<A> eq, Stream<A> xs) {
      return this.removeAll(Function.compose(Monoid.disjunctionMonoid.sumLeftS(), xs.mapM(Function.curry(eq.eq()))));
   }

   public final Stream<A> removeAll(F<A, Boolean> f) {
      return this.filter(Function.compose(Booleans.not, f));
   }

   public static <A, B> F<B, Stream<A>> sequence_(Stream<F<B, A>> fs) {
      return (F)fs.foldRight((F2)(new F2<F<B, A>, P1<F<B, Stream<A>>>, F<B, Stream<A>>>() {
         public F<B, Stream<A>> f(F<B, A> baf, P1<F<B, Stream<A>>> p1) {
            return Function.bind(baf, (F)p1._1(), Function.curry(new F2<A, Stream<A>, Stream<A>>() {
               public Stream<A> f(A a, Stream<A> stream) {
                  return Stream.cons(a, P.p(stream));
               }
            }));
         }
      }), Function.constant(nil()));
   }

   public final <B, C> F<B, Stream<C>> mapM(F<A, F<B, C>> f) {
      return sequence_(this.map(f));
   }

   public final <B> Stream<B> bind(F<A, Stream<B>> f) {
      return ((Stream)this.map(f).foldLeft((F2)(new F2<Stream<B>, Stream<B>, Stream<B>>() {
         public Stream<B> f(Stream<B> accumulator, Stream<B> element) {
            Stream<B> result = accumulator;

            Object single;
            for(Iterator var4 = element.iterator(); var4.hasNext(); result = result.cons(single)) {
               single = var4.next();
            }

            return result;
         }
      }), nil())).reverse();
   }

   public final <B, C> Stream<C> bind(Stream<B> sb, F<A, F<B, C>> f) {
      return sb.apply(this.map(f));
   }

   public final <B, C> Stream<C> bind(Stream<B> sb, F2<A, B, C> f) {
      return this.bind(sb, Function.curry(f));
   }

   public final <B, C, D> Stream<D> bind(Stream<B> sb, Stream<C> sc, F<A, F<B, F<C, D>>> f) {
      return sc.apply(this.bind(sb, f));
   }

   public final <B, C, D, E> Stream<E> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, F<A, F<B, F<C, F<D, E>>>> f) {
      return sd.apply(this.bind(sb, sc, f));
   }

   public final <B, C, D, E, F$> Stream<F$> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
      return se.apply(this.bind(sb, sc, sd, f));
   }

   public final <B, C, D, E, F$, G> Stream<G> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
      return sf.apply(this.bind(sb, sc, sd, se, f));
   }

   public final <B, C, D, E, F$, G, H> Stream<H> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, Stream<G> sg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
      return sg.apply(this.bind(sb, sc, sd, se, sf, f));
   }

   public final <B, C, D, E, F$, G, H, I> Stream<I> bind(Stream<B> sb, Stream<C> sc, Stream<D> sd, Stream<E> se, Stream<F$> sf, Stream<G> sg, Stream<H> sh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
      return sh.apply(this.bind(sb, sc, sd, se, sf, sg, f));
   }

   public final <B> Stream<B> sequence(Stream<B> bs) {
      F<A, Stream<B>> c = Function.constant(bs);
      return this.bind(c);
   }

   public final <B> Stream<B> apply(Stream<F<A, B>> sf) {
      return sf.bind(new F<F<A, B>, Stream<B>>() {
         public Stream<B> f(final F<A, B> f) {
            return Stream.this.map(new F<A, B>() {
               public B f(A a) {
                  return f.f(a);
               }
            });
         }
      });
   }

   public final Stream<A> interleave(final Stream<A> as) {
      return this.isEmpty() ? as : (as.isEmpty() ? this : cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return as.interleave((Stream)Stream.this.tail()._1());
         }
      }));
   }

   public final Stream<A> sort(Ord<A> o) {
      return mergesort(o, this.map((F)Function.flip(cons()).f(P.p(nil()))));
   }

   private static <A> Stream<A> mergesort(Ord<A> o, Stream<Stream<A>> s) {
      if (s.isEmpty()) {
         return nil();
      } else {
         Stream xss;
         for(xss = s; ((Stream)xss.tail()._1()).isNotEmpty(); xss = mergePairs(o, xss)) {
         }

         return (Stream)xss.head();
      }
   }

   private static <A> Stream<Stream<A>> mergePairs(final Ord<A> o, Stream<Stream<A>> s) {
      if (!s.isEmpty() && !((Stream)s.tail()._1()).isEmpty()) {
         final Stream<Stream<A>> t = (Stream)s.tail()._1();
         return cons(merge(o, (Stream)s.head(), (Stream)t.head()), new P1<Stream<Stream<A>>>() {
            public Stream<Stream<A>> _1() {
               return Stream.mergePairs(o, (Stream)t.tail()._1());
            }
         });
      } else {
         return s;
      }
   }

   private static <A> Stream<A> merge(final Ord<A> o, final Stream<A> xs, final Stream<A> ys) {
      if (xs.isEmpty()) {
         return ys;
      } else if (ys.isEmpty()) {
         return xs;
      } else {
         A x = xs.head();
         A y = ys.head();
         return o.isGreaterThan(x, y) ? cons(y, new P1<Stream<A>>() {
            public Stream<A> _1() {
               return Stream.merge(o, xs, (Stream)ys.tail()._1());
            }
         }) : cons(x, new P1<Stream<A>>() {
            public Stream<A> _1() {
               return Stream.merge(o, (Stream)xs.tail()._1(), ys);
            }
         });
      }
   }

   public final Stream<A> sort(Ord<A> o, Strategy<Unit> s) {
      return (Stream)this.qs(o, s).claim();
   }

   private Promise<Stream<A>> qs(Ord<A> o, Strategy<Unit> s) {
      if (this.isEmpty()) {
         return Promise.promise(s, P.p(this));
      } else {
         F<Boolean, Boolean> id = Function.identity();
         A x = this.head();
         P1<Stream<A>> xs = this.tail();
         Promise<Stream<A>> left = Promise.join(s, xs.map(flt(o, s, x, id)));
         Promise<Stream<A>> right = (Promise)xs.map(flt(o, s, x, Booleans.not))._1();
         Monoid<Stream<A>> m = Monoid.streamMonoid();
         return right.fmap(m.sum(single(x))).apply(left.fmap(m.sum()));
      }
   }

   private static <A> F<Stream<A>, Promise<Stream<A>>> qs_(final Ord<A> o, final Strategy<Unit> s) {
      return new F<Stream<A>, Promise<Stream<A>>>() {
         public Promise<Stream<A>> f(Stream<A> xs) {
            return xs.qs(o, s);
         }
      };
   }

   private static <A> F<Stream<A>, Promise<Stream<A>>> flt(Ord<A> o, Strategy<Unit> s, A x, F<Boolean, Boolean> f) {
      F<F<A, Boolean>, F<Stream<A>, Stream<A>>> filter = filter();
      F<A, Boolean> lt = o.isLessThan(x);
      return Function.compose(qs_(o, s), (F)filter.f(Function.compose(f, lt)));
   }

   public final Collection<A> toCollection() {
      return new AbstractCollection<A>() {
         public Iterator<A> iterator() {
            return new Iterator<A>() {
               private Stream<A> xs = Stream.this;

               public boolean hasNext() {
                  return this.xs.isNotEmpty();
               }

               public A next() {
                  if (this.xs.isEmpty()) {
                     throw new NoSuchElementException();
                  } else {
                     A a = this.xs.head();
                     this.xs = (Stream)this.xs.tail()._1();
                     return a;
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }

         public int size() {
            return Stream.this.length();
         }
      };
   }

   public static Stream<Integer> range(final int from, final long to) {
      return (long)from >= to ? nil() : cons(from, new P1<Stream<Integer>>() {
         public Stream<Integer> _1() {
            return Stream.range(from + 1, to);
         }
      });
   }

   public static <A> Stream<A> stream(A... as) {
      return as.length == 0 ? nil() : unfold(P2.tuple(new F2<A[], Integer, Option<P2<A, P2<A[], Integer>>>>() {
         public Option<P2<A, P2<A[], Integer>>> f(A[] as, Integer i) {
            return i >= as.length ? Option.none() : Option.some(P.p(as[i], P.p(as, i + 1)));
         }
      }), P.p(as, 0));
   }

   public static <A> Stream<A> forever(Enumerator<A> e, A from) {
      return forever(e, from, 1L);
   }

   public static <A> Stream<A> forever(final Enumerator<A> e, final A from, final long step) {
      return cons(from, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return (Stream)e.plus(from, step).map(new F<A, Stream<A>>() {
               public Stream<A> f(A a) {
                  return Stream.forever(e, a, step);
               }
            }).orSome((Object)Stream.nil());
         }
      });
   }

   public static <A> Stream<A> range(Enumerator<A> e, A from, A to) {
      return range(e, from, to, 1L);
   }

   public static <A> Stream<A> range(final Enumerator<A> e, final A from, final A to, final long step) {
      final Ordering o = e.order().compare(from, to);
      return o != Ordering.EQ && (step <= 0L || o != Ordering.GT) && (step >= 0L || o != Ordering.LT) ? cons(from, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.join(e.plus(from, step).filter(new F<A, Boolean>() {
               public Boolean f(A a) {
                  boolean var10000;
                  label23: {
                     if (o == Ordering.LT) {
                        if (!e.order().isLessThan(to, a)) {
                           break label23;
                        }
                     } else if (!e.order().isGreaterThan(to, a)) {
                        break label23;
                     }

                     var10000 = false;
                     return var10000;
                  }

                  var10000 = true;
                  return var10000;
               }
            }).map(new F<A, Stream<A>>() {
               public Stream<A> f(A a) {
                  return Stream.range(e, a, to, step);
               }
            }).toStream());
         }
      }) : single(from);
   }

   public static Stream<Integer> range(final int from) {
      return cons(from, new P1<Stream<Integer>>() {
         public Stream<Integer> _1() {
            return Stream.range(from + 1);
         }
      });
   }

   public static <A> F<F<A, Boolean>, F<Stream<A>, Stream<A>>> filter() {
      return Function.curry(new F2<F<A, Boolean>, Stream<A>, Stream<A>>() {
         public Stream<A> f(F<A, Boolean> f, Stream<A> as) {
            return as.filter(f);
         }
      });
   }

   public final <B> Stream<B> zapp(final Stream<F<A, B>> fs) {
      return !fs.isEmpty() && !this.isEmpty() ? cons(((F)fs.head()).f(this.head()), new P1<Stream<B>>() {
         public Stream<B> _1() {
            return ((Stream)Stream.this.tail()._1()).zapp((Stream)fs.tail()._1());
         }
      }) : nil();
   }

   public final <B, C> Stream<C> zipWith(Stream<B> bs, F<A, F<B, C>> f) {
      return bs.zapp(this.zapp(repeat(f)));
   }

   public final <B, C> Stream<C> zipWith(Stream<B> bs, F2<A, B, C> f) {
      return this.zipWith(bs, Function.curry(f));
   }

   public final <B, C> F<Stream<B>, Stream<C>> zipWith(final F<A, F<B, C>> f) {
      return new F<Stream<B>, Stream<C>>() {
         public Stream<C> f(Stream<B> stream) {
            return Stream.this.zipWith(stream, f);
         }
      };
   }

   public final <B> Stream<P2<A, B>> zip(Stream<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(bs, __2);
   }

   public final Stream<P2<A, Integer>> zipIndex() {
      return this.zipWith(range(0), new F2<A, Integer, P2<A, Integer>>() {
         public P2<A, Integer> f(A a, Integer i) {
            return P.p(a, i);
         }
      });
   }

   public final <X> Either<X, A> toEither(P1<X> x) {
      return this.isEmpty() ? Either.left(x._1()) : Either.right(this.head());
   }

   public final Option<A> toOption() {
      return this.isEmpty() ? Option.none() : Option.some(this.head());
   }

   public final List<A> toList() {
      List<A> as = List.nil();

      for(Stream x = this; !x.isEmpty(); x = (Stream)x.tail()._1()) {
         as = as.snoc(x.head());
      }

      return as;
   }

   public final Array<A> toArray() {
      int l = this.length();
      Object[] a = new Object[l];
      Stream<A> x = this;

      for(int i = 0; i < l; ++i) {
         a[i] = x.head();
         x = (Stream)x.tail()._1();
      }

      return Array.mkArray(a);
   }

   public final Array<A> toArray(Class<A[]> c) {
      A[] a = (Object[])((Object[])java.lang.reflect.Array.newInstance(c.getComponentType(), this.length()));
      int i = 0;

      for(Iterator var4 = this.iterator(); var4.hasNext(); ++i) {
         A x = var4.next();
         a[i] = x;
      }

      return Array.array(a);
   }

   public final A[] array(Class<A[]> c) {
      return this.toArray(c).array(c);
   }

   public final Stream<A> cons(A a) {
      return new Stream.Cons(a, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.this;
         }
      });
   }

   public static String asString(Stream<Character> cs) {
      return LazyString.fromStream(cs).toString();
   }

   public static Stream<Character> fromString(String s) {
      return LazyString.str(s).toStream();
   }

   public final Stream<A> snoc(A a) {
      return this.snoc(P.p(a));
   }

   public final Stream<A> snoc(final P1<A> a) {
      return this.append(new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.single(a._1());
         }
      });
   }

   public final Stream<A> take(final int n) {
      return n > 0 && !this.isEmpty() ? cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return ((Stream)Stream.this.tail()._1()).take(n - 1);
         }
      }) : nil();
   }

   public final Stream<A> drop(int i) {
      int c = 0;

      Stream xs;
      for(xs = this; xs.isNotEmpty() && c < i; xs = (Stream)xs.tail()._1()) {
         ++c;
      }

      return xs;
   }

   public final Stream<A> takeWhile(final F<A, Boolean> f) {
      return this.isEmpty() ? this : ((Boolean)f.f(this.head()) ? cons(this.head(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return ((Stream)Stream.this.tail()._1()).takeWhile(f);
         }
      }) : nil());
   }

   public final Stream<A> dropWhile(F<A, Boolean> f) {
      Stream as;
      for(as = this; !as.isEmpty() && (Boolean)f.f(as.head()); as = (Stream)as.tail()._1()) {
      }

      return as;
   }

   public final P2<Stream<A>, Stream<A>> span(final F<A, Boolean> p) {
      if (this.isEmpty()) {
         return P.p(this, this);
      } else if ((Boolean)p.f(this.head())) {
         final P1<P2<Stream<A>, Stream<A>>> yszs = new P1<P2<Stream<A>, Stream<A>>>() {
            public P2<Stream<A>, Stream<A>> _1() {
               return ((Stream)Stream.this.tail()._1()).span(p);
            }
         };
         return new P2<Stream<A>, Stream<A>>() {
            public Stream<A> _1() {
               return Stream.cons(Stream.this.head(), yszs.map(P2.__1()));
            }

            public Stream<A> _2() {
               return (Stream)((P2)yszs._1())._2();
            }
         };
      } else {
         return P.p(nil(), this);
      }
   }

   public final Stream<A> replace(final F<A, Boolean> p, final A a) {
      if (this.isEmpty()) {
         return nil();
      } else {
         final P2<Stream<A>, Stream<A>> s = this.span(p);
         return ((Stream)s._1()).append(cons(a, new P1<Stream<A>>() {
            public Stream<A> _1() {
               return ((Stream)((Stream)s._2()).tail()._1()).replace(p, a);
            }
         }));
      }
   }

   public final P2<Stream<A>, Stream<A>> split(F<A, Boolean> p) {
      return this.span(Function.compose(Booleans.not, p));
   }

   public final Stream<A> reverse() {
      return (Stream)this.foldLeft((F)(new F<Stream<A>, F<A, Stream<A>>>() {
         public F<A, Stream<A>> f(final Stream<A> as) {
            return new F<A, Stream<A>>() {
               public Stream<A> f(A a) {
                  return Stream.cons(a, new P1<Stream<A>>() {
                     public Stream<A> _1() {
                        return as;
                     }
                  });
               }
            };
         }
      }), nil());
   }

   public final A last() {
      return this.reverse().head();
   }

   public final int length() {
      Stream<A> xs = this;

      int i;
      for(i = 0; !xs.isEmpty(); ++i) {
         xs = (Stream)xs.tail()._1();
      }

      return i;
   }

   public final A index(int i) {
      if (i < 0) {
         throw Bottom.error("index " + i + " out of range on stream");
      } else {
         Stream<A> xs = this;

         for(int c = 0; c < i; ++c) {
            if (xs.isEmpty()) {
               throw Bottom.error("index " + i + " out of range on stream");
            }

            xs = (Stream)xs.tail()._1();
         }

         if (xs.isEmpty()) {
            throw Bottom.error("index " + i + " out of range on stream");
         } else {
            return xs.head();
         }
      }
   }

   public final boolean forall(F<A, Boolean> f) {
      return this.isEmpty() || (Boolean)f.f(this.head()) && ((Stream)this.tail()._1()).forall(f);
   }

   public final boolean exists(F<A, Boolean> f) {
      return this.dropWhile(Booleans.not(f)).isNotEmpty();
   }

   public final Option<A> find(F<A, Boolean> f) {
      for(Stream as = this; as.isNotEmpty(); as = (Stream)as.tail()._1()) {
         if ((Boolean)f.f(as.head())) {
            return Option.some(as.head());
         }
      }

      return Option.none();
   }

   public final <B> Stream<B> cobind(F<Stream<A>, B> k) {
      return this.substreams().map(k);
   }

   public final Stream<Stream<A>> tails() {
      return this.isEmpty() ? nil() : cons(this, new P1<Stream<Stream<A>>>() {
         public Stream<Stream<A>> _1() {
            return ((Stream)Stream.this.tail()._1()).tails();
         }
      });
   }

   public final Stream<Stream<A>> inits() {
      Stream<Stream<A>> nil = cons(nil(), new P1<Stream<Stream<A>>>() {
         public Stream<Stream<A>> _1() {
            return Stream.nil();
         }
      });
      return this.isEmpty() ? nil : nil.append(new P1<Stream<Stream<A>>>() {
         public Stream<Stream<A>> _1() {
            return ((Stream)Stream.this.tail()._1()).inits().map((F)Stream.cons_().f(Stream.this.head()));
         }
      });
   }

   public final Stream<Stream<A>> substreams() {
      return this.tails().bind(new F<Stream<A>, Stream<Stream<A>>>() {
         public Stream<Stream<A>> f(Stream<A> stream) {
            return stream.inits();
         }
      });
   }

   public final Option<Integer> indexOf(final F<A, Boolean> p) {
      return this.zipIndex().find(new F<P2<A, Integer>, Boolean>() {
         public Boolean f(P2<A, Integer> p2) {
            return (Boolean)p.f(p2._1());
         }
      }).map(P2.__2());
   }

   public final <B> Stream<B> sequenceW(final Stream<F<Stream<A>, B>> fs) {
      return fs.isEmpty() ? nil() : cons(((F)fs.head()).f(this), new P1<Stream<B>>() {
         public Stream<B> _1() {
            return Stream.this.sequenceW((Stream)fs.tail()._1());
         }
      });
   }

   public final F<Integer, A> toFunction() {
      return new F<Integer, A>() {
         public A f(Integer i) {
            return Stream.this.index(i);
         }
      };
   }

   public static <A> Stream<A> fromFunction(F<Natural, A> f) {
      return fromFunction(Enumerator.naturalEnumerator, f, Natural.ZERO);
   }

   public static <A, B> Stream<A> fromFunction(final Enumerator<B> e, final F<B, A> f, final B i) {
      return cons(f.f(i), new P1<Stream<A>>() {
         public Stream<A> _1() {
            Option<B> s = e.successor(i);
            return s.isSome() ? Stream.fromFunction(e, f, s.some()) : Stream.nil();
         }
      });
   }

   public static <A, B> P2<Stream<A>, Stream<B>> unzip(Stream<P2<A, B>> xs) {
      return (P2)xs.foldRight((F2)(new F2<P2<A, B>, P1<P2<Stream<A>, Stream<B>>>, P2<Stream<A>, Stream<B>>>() {
         public P2<Stream<A>, Stream<B>> f(P2<A, B> p, P1<P2<Stream<A>, Stream<B>>> ps) {
            P2<Stream<A>, Stream<B>> pp = (P2)ps._1();
            return P.p(Stream.cons(p._1(), P.p(pp._1())), Stream.cons(p._2(), P.p(pp._2())));
         }
      }), P.p(nil(), nil()));
   }

   public static <A, B, C> F<Stream<A>, F<Stream<B>, F<F<A, F<B, C>>, Stream<C>>>> zipWith() {
      return Function.curry(new F3<Stream<A>, Stream<B>, F<A, F<B, C>>, Stream<C>>() {
         public Stream<C> f(Stream<A> as, Stream<B> bs, F<A, F<B, C>> f) {
            return as.zipWith(bs, f);
         }
      });
   }

   public static <A> F<A, F<P1<Stream<A>>, Stream<A>>> cons() {
      return new F<A, F<P1<Stream<A>>, Stream<A>>>() {
         public F<P1<Stream<A>>, Stream<A>> f(final A a) {
            return new F<P1<Stream<A>>, Stream<A>>() {
               public Stream<A> f(P1<Stream<A>> list) {
                  return Stream.cons(a, list);
               }
            };
         }
      };
   }

   public static <A> F<A, F<Stream<A>, Stream<A>>> cons_() {
      return Function.curry(new F2<A, Stream<A>, Stream<A>>() {
         public Stream<A> f(A a, Stream<A> as) {
            return as.cons(a);
         }
      });
   }

   public static <A> Stream<A> nil() {
      return new Stream.Nil();
   }

   public static <A> P1<Stream<A>> nil_() {
      return new P1<Stream<A>>() {
         public Stream<A> _1() {
            return new Stream.Nil();
         }
      };
   }

   public static <A> F<Stream<A>, Boolean> isEmpty_() {
      return new F<Stream<A>, Boolean>() {
         public Boolean f(Stream<A> as) {
            return as.isEmpty();
         }
      };
   }

   public static <A> F<Stream<A>, Boolean> isNotEmpty_() {
      return new F<Stream<A>, Boolean>() {
         public Boolean f(Stream<A> as) {
            return as.isNotEmpty();
         }
      };
   }

   public static <A> Stream<A> single(A a) {
      return cons(a, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.nil();
         }
      });
   }

   public static <A> F<A, Stream<A>> single() {
      return new F<A, Stream<A>>() {
         public Stream<A> f(A a) {
            return Stream.single(a);
         }
      };
   }

   public static <A> Stream<A> cons(A head, P1<Stream<A>> tail) {
      return new Stream.Cons(head, tail);
   }

   public static <A> Stream<A> join(Stream<Stream<A>> o) {
      return (Stream)Monoid.streamMonoid().sumRight(o);
   }

   public static <A> F<Stream<Stream<A>>, Stream<A>> join() {
      return new F<Stream<Stream<A>>, Stream<A>>() {
         public Stream<A> f(Stream<Stream<A>> as) {
            return Stream.join(as);
         }
      };
   }

   public static <A, B> Stream<A> unfold(final F<B, Option<P2<A, B>>> f, B b) {
      Option<P2<A, B>> o = (Option)f.f(b);
      if (o.isNone()) {
         return nil();
      } else {
         final P2<A, B> p = (P2)o.some();
         return cons(p._1(), new P1<Stream<A>>() {
            public Stream<A> _1() {
               return Stream.unfold(f, p._2());
            }
         });
      }
   }

   public static <A> Stream<A> iterateWhile(final F<A, A> f, final F<A, Boolean> p, A a) {
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

   public static <A> Stream<A> iterableStream(Iterable<A> i) {
      final class Util {
         public <A> Stream<A> iteratorStream(final Iterator<A> i) {
            if (i.hasNext()) {
               A a = i.next();
               return Stream.cons(a, new P1<Stream<A>>() {
                  public Stream<A> _1() {
                     return Util.this.iteratorStream(i);
                  }
               });
            } else {
               return Stream.nil();
            }
         }
      }

      return (new Util()).iteratorStream(i.iterator());
   }

   public static <A> Stream<A> repeat(final A a) {
      return cons(a, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.repeat(a);
         }
      });
   }

   public static <A> Stream<A> cycle(final Stream<A> as) {
      if (as.isEmpty()) {
         throw Bottom.error("cycle on empty list");
      } else {
         return as.append(new P1<Stream<A>>() {
            public Stream<A> _1() {
               return Stream.cycle(as);
            }
         });
      }
   }

   public static <A> Stream<A> iterate(final F<A, A> f, final A a) {
      return cons(a, new P1<Stream<A>>() {
         public Stream<A> _1() {
            return Stream.iterate(f, f.f(a));
         }
      });
   }

   public static <A> F<F<A, A>, F<A, Stream<A>>> iterate() {
      return Function.curry(new F2<F<A, A>, A, Stream<A>>() {
         public Stream<A> f(F<A, A> f, A a) {
            return Stream.iterate(f, a);
         }
      });
   }

   public static <A, B> F<F<A, Stream<B>>, F<Stream<A>, Stream<B>>> bind_() {
      return Function.curry(new F2<F<A, Stream<B>>, Stream<A>, Stream<B>>() {
         public Stream<B> f(F<A, Stream<B>> f, Stream<A> as) {
            return as.bind(f);
         }
      });
   }

   public static <A, B> F<F<A, F<P1<B>, B>>, F<B, F<Stream<A>, B>>> foldRight() {
      return Function.curry(new F3<F<A, F<P1<B>, B>>, B, Stream<A>, B>() {
         public B f(F<A, F<P1<B>, B>> f, B b, Stream<A> as) {
            return as.foldRight(f, b);
         }
      });
   }

   // $FF: synthetic method
   Stream(Object x0) {
      this();
   }

   private static final class Cons<A> extends Stream<A> {
      private final A head;
      private final P1<Stream<A>> tail;

      Cons(A head, P1<Stream<A>> tail) {
         super(null);
         this.head = head;
         this.tail = tail.memo();
      }

      public A head() {
         return this.head;
      }

      public P1<Stream<A>> tail() {
         return this.tail;
      }
   }

   private static final class Nil<A> extends Stream<A> {
      private Nil() {
         super(null);
      }

      public A head() {
         throw Bottom.error("head on empty stream");
      }

      public P1<Stream<A>> tail() {
         throw Bottom.error("tail on empty stream");
      }

      // $FF: synthetic method
      Nil(Object x0) {
         this();
      }
   }
}
