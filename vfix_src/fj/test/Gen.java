package fj.test;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Ord;
import fj.P2;
import fj.Unit;
import fj.data.Array;
import fj.data.List;
import fj.function.Effect1;

public final class Gen<A> {
   private final F<Integer, F<Rand, A>> f;

   private Gen(F<Integer, F<Rand, A>> f) {
      this.f = f;
   }

   public A gen(int i, Rand r) {
      return ((F)this.f.f(i)).f(r);
   }

   public <B> Gen<B> map(final F<A, B> f) {
      return new Gen(new F<Integer, F<Rand, B>>() {
         public F<Rand, B> f(final Integer i) {
            return new F<Rand, B>() {
               public B f(Rand r) {
                  return f.f(Gen.this.gen(i, r));
               }
            };
         }
      });
   }

   public Gen<A> filter(final F<A, Boolean> f) {
      return gen(Function.curry(new F2<Integer, Rand, A>() {
         public A f(Integer i, Rand r) {
            Object a;
            do {
               a = Gen.this.gen(i, r);
            } while(!(Boolean)f.f(a));

            return a;
         }
      }));
   }

   public Unit foreach(Integer i, Rand r, F<A, Unit> f) {
      return (Unit)f.f(((F)this.f.f(i)).f(r));
   }

   public void foreachDoEffect(Integer i, Rand r, Effect1<A> f) {
      f.f(((F)this.f.f(i)).f(r));
   }

   public <B> Gen<B> bind(final F<A, Gen<B>> f) {
      return new Gen(new F<Integer, F<Rand, B>>() {
         public F<Rand, B> f(final Integer i) {
            return new F<Rand, B>() {
               public B f(Rand r) {
                  return ((F)((Gen)f.f(Gen.this.gen(i, r))).f.f(i)).f(r);
               }
            };
         }
      });
   }

   public <B, C> Gen<C> bind(Gen<B> gb, F<A, F<B, C>> f) {
      return gb.apply(this.map(f));
   }

   public <B, C, D> Gen<D> bind(Gen<B> gb, Gen<C> gc, F<A, F<B, F<C, D>>> f) {
      return gc.apply(this.bind(gb, f));
   }

   public <B, C, D, E> Gen<E> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, F<A, F<B, F<C, F<D, E>>>> f) {
      return gd.apply(this.bind(gb, gc, f));
   }

   public <B, C, D, E, F$> Gen<F$> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
      return ge.apply(this.bind(gb, gc, gd, f));
   }

   public <B, C, D, E, F$, G> Gen<G> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
      return gf.apply(this.bind(gb, gc, gd, ge, f));
   }

   public <B, C, D, E, F$, G, H> Gen<H> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, Gen<G> gg, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
      return gg.apply(this.bind(gb, gc, gd, ge, gf, f));
   }

   public <B, C, D, E, F$, G, H, I> Gen<I> bind(Gen<B> gb, Gen<C> gc, Gen<D> gd, Gen<E> ge, Gen<F$> gf, Gen<G> gg, Gen<H> gh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
      return gh.apply(this.bind(gb, gc, gd, ge, gf, gg, f));
   }

   public <B> Gen<B> apply(Gen<F<A, B>> gf) {
      return gf.bind(new F<F<A, B>, Gen<B>>() {
         public Gen<B> f(final F<A, B> f) {
            return Gen.this.map(new F<A, B>() {
               public B f(A a) {
                  return f.f(a);
               }
            });
         }
      });
   }

   public Gen<A> resize(final int s) {
      return new Gen(new F<Integer, F<Rand, A>>() {
         public F<Rand, A> f(Integer i) {
            return new F<Rand, A>() {
               public A f(Rand r) {
                  return ((F)Gen.this.f.f(s)).f(r);
               }
            };
         }
      });
   }

   public static <A> Gen<A> gen(F<Integer, F<Rand, A>> f) {
      return new Gen(f);
   }

   public static <A> Gen<List<A>> sequence(List<Gen<A>> gs) {
      return (Gen)gs.foldRight((F)(new F<Gen<A>, F<Gen<List<A>>, Gen<List<A>>>>() {
         public F<Gen<List<A>>, Gen<List<A>>> f(final Gen<A> ga) {
            return new F<Gen<List<A>>, Gen<List<A>>>() {
               public Gen<List<A>> f(Gen<List<A>> gas) {
                  return ga.bind(gas, List.cons());
               }
            };
         }
      }), value(List.nil()));
   }

   public static <A> Gen<List<A>> sequenceN(int n, Gen<A> g) {
      return sequence(List.replicate(n, g));
   }

   public static <A> Gen<A> parameterised(final F<Integer, F<Rand, Gen<A>>> f) {
      return new Gen(Function.curry(new F2<Integer, Rand, A>() {
         public A f(Integer i, Rand r) {
            return ((Gen)((F)f.f(i)).f(r)).gen(i, r);
         }
      }));
   }

   public static <A> Gen<A> sized(F<Integer, Gen<A>> f) {
      return parameterised(Function.flip(Function.constant(f)));
   }

   public static <A> Gen<A> value(final A a) {
      return new Gen(new F<Integer, F<Rand, A>>() {
         public F<Rand, A> f(Integer i) {
            return new F<Rand, A>() {
               public A f(Rand r) {
                  return a;
               }
            };
         }
      });
   }

   public static Gen<Integer> choose(int from, int to) {
      final int f = Math.min(from, to);
      final int t = Math.max(from, to);
      return parameterised(Function.curry(new F2<Integer, Rand, Gen<Integer>>() {
         public Gen<Integer> f(Integer i, Rand r) {
            return Gen.value(r.choose(f, t));
         }
      }));
   }

   public static Gen<Double> choose(double from, double to) {
      final double f = Math.min(from, to);
      final double t = Math.max(from, to);
      return parameterised(new F<Integer, F<Rand, Gen<Double>>>() {
         public F<Rand, Gen<Double>> f(Integer i) {
            return new F<Rand, Gen<Double>>() {
               public Gen<Double> f(Rand r) {
                  return Gen.value(r.choose(f, t));
               }
            };
         }
      });
   }

   public static <A> Gen<A> fail() {
      return new Gen(new F<Integer, F<Rand, A>>() {
         public F<Rand, A> f(Integer i) {
            return new F<Rand, A>() {
               public A f(Rand r) {
                  throw Bottom.error("Failing generator");
               }
            };
         }
      });
   }

   public static <A> Gen<A> join(Gen<Gen<A>> g) {
      return g.bind(Function.identity());
   }

   public static <A> Gen<A> frequency(final List<P2<Integer, Gen<A>>> gs) {
      F<P2<Integer, Gen<A>>, Integer> f = P2.__1();
      return choose(1, (Integer)Monoid.intAdditionMonoid.sumLeft(gs.map(f))).bind(new F<Integer, Gen<A>>() {
         public Gen<A> f(Integer i) {
            return (new Pick()).pick(i, gs);
         }
      });

      final class Pick {
         <A> Gen<A> pick(int n, List<P2<Integer, Gen<A>>> gs) {
            if (gs.isEmpty()) {
               return Gen.fail();
            } else {
               int k = (Integer)((P2)gs.head())._1();
               return n <= k ? (Gen)((P2)gs.head())._2() : this.pick(n - k, gs.tail());
            }
         }
      }

   }

   public static <A> Gen<A> elemFrequency(List<P2<Integer, A>> as) {
      return frequency(as.map(new F<P2<Integer, A>, P2<Integer, Gen<A>>>() {
         public P2<Integer, Gen<A>> f(P2<Integer, A> p) {
            return p.map2(new F<A, Gen<A>>() {
               public Gen<A> f(A a) {
                  return Gen.value(a);
               }
            });
         }
      }));
   }

   public static <A> Gen<A> elements(final A... as) {
      return Array.array(as).isEmpty() ? fail() : choose(0, as.length - 1).map(new F<Integer, A>() {
         public A f(Integer i) {
            return as[i];
         }
      });
   }

   public static <A> Gen<A> oneOf(final List<Gen<A>> gs) {
      return gs.isEmpty() ? fail() : choose(0, gs.length() - 1).bind(new F<Integer, Gen<A>>() {
         public Gen<A> f(Integer i) {
            return (Gen)gs.index(i);
         }
      });
   }

   public static <A> Gen<List<A>> listOf(final Gen<A> g, final int x) {
      return sized(new F<Integer, Gen<List<A>>>() {
         public Gen<List<A>> f(Integer size) {
            return Gen.choose(x, size).bind(new F<Integer, Gen<List<A>>>() {
               public Gen<List<A>> f(Integer n) {
                  return Gen.sequenceN(n, g);
               }
            });
         }
      });
   }

   public static <A> Gen<List<A>> listOf(Gen<A> g) {
      return listOf(g, 0);
   }

   public static <A> Gen<List<A>> listOf1(Gen<A> g) {
      return listOf(g, 1);
   }

   public static <A> Gen<List<A>> pick(int n, final List<A> as) {
      return n >= 0 && n <= as.length() ? sequenceN(n, choose(0, as.length() - 1)).map(new F<List<Integer>, List<A>>() {
         public List<A> f(List<Integer> is) {
            List<A> r = List.nil();
            List<Integer> iis = is.sort(Ord.intOrd);

            for(List aas = as.zipIndex(); iis.isNotEmpty() && aas.isNotEmpty(); aas = aas.tail()) {
               if (((Integer)iis.head()).equals(((P2)aas.head())._2())) {
                  iis = iis.tail();
               } else {
                  r = r.snoc(((P2)aas.head())._1());
               }
            }

            return r;
         }
      }) : fail();
   }

   public static <A> Gen<List<A>> someOf(final List<A> as) {
      return choose(0, as.length()).bind(new F<Integer, Gen<List<A>>>() {
         public Gen<List<A>> f(Integer i) {
            return Gen.pick(i, as);
         }
      });
   }

   public static <A, B> Gen<F<A, B>> promote(final F<A, Gen<B>> f) {
      return new Gen(new F<Integer, F<Rand, F<A, B>>>() {
         public F<Rand, F<A, B>> f(final Integer i) {
            return new F<Rand, F<A, B>>() {
               public F<A, B> f(final Rand r) {
                  return new F<A, B>() {
                     public B f(A a) {
                        return ((F)((Gen)f.f(a)).f.f(i)).f(r);
                     }
                  };
               }
            };
         }
      });
   }
}
