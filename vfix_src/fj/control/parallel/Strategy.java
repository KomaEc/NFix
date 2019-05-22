package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.data.Array;
import fj.data.Java;
import fj.data.List;
import fj.function.Effect1;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class Strategy<A> {
   private final F<P1<A>, P1<A>> f;

   private Strategy(F<P1<A>, P1<A>> f) {
      this.f = f;
   }

   public F<P1<A>, P1<A>> f() {
      return this.f;
   }

   public static <A> Strategy<A> strategy(F<P1<A>, P1<A>> f) {
      return new Strategy(f);
   }

   public P1<A> par(P1<A> a) {
      return (P1)this.f().f(a);
   }

   public <B> F<B, P1<A>> concurry(F<B, A> f) {
      return Function.compose(this.f(), P1.curry(f));
   }

   public <B, C> F<B, F<C, P1<A>>> concurry(final F2<B, C, A> f) {
      return new F<B, F<C, P1<A>>>() {
         public F<C, P1<A>> f(B b) {
            return Strategy.this.concurry((F)Function.curry(f).f(b));
         }
      };
   }

   public static <A> List<P1<A>> mergeAll(List<Future<A>> xs) {
      return xs.map(obtain());
   }

   public P1<List<A>> parList(List<P1<A>> ps) {
      return P1.sequence(ps.map(this.f()));
   }

   public <B> P1<List<A>> parMap(F<B, A> f, List<B> bs) {
      return P1.sequence(bs.map(this.concurry(f)));
   }

   public <B> P1<Array<A>> parMap(F<B, A> f, Array<B> bs) {
      return P1.sequence(bs.map(this.concurry(f)));
   }

   public <B> List<A> parMap1(F<B, A> f, List<B> bs) {
      return (List)Function.compose(P1.__1(), this.parMapList(f)).f(bs);
   }

   public <B> Array<A> parMap1(F<B, A> f, Array<B> bs) {
      return (Array)Function.compose(P1.__1(), this.parMapArray(f)).f(bs);
   }

   public <B> F<List<B>, P1<List<A>>> parMapList(final F<B, A> f) {
      return new F<List<B>, P1<List<A>>>() {
         public P1<List<A>> f(List<B> as) {
            return Strategy.this.parMap(f, as);
         }
      };
   }

   public <B> F<F<B, A>, F<List<B>, P1<List<A>>>> parMapList() {
      return new F<F<B, A>, F<List<B>, P1<List<A>>>>() {
         public F<List<B>, P1<List<A>>> f(F<B, A> f) {
            return Strategy.this.parMapList(f);
         }
      };
   }

   public <B> F<F<B, A>, F<List<B>, List<A>>> parMapList1() {
      return new F<F<B, A>, F<List<B>, List<A>>>() {
         public F<List<B>, List<A>> f(final F<B, A> f) {
            return new F<List<B>, List<A>>() {
               public List<A> f(List<B> bs) {
                  return Strategy.this.parMap1(f, bs);
               }
            };
         }
      };
   }

   public <B> F<Array<B>, P1<Array<A>>> parMapArray(final F<B, A> f) {
      return new F<Array<B>, P1<Array<A>>>() {
         public P1<Array<A>> f(Array<B> as) {
            return Strategy.this.parMap(f, as);
         }
      };
   }

   public <B> F<F<B, A>, F<Array<B>, P1<Array<A>>>> parMapArray() {
      return new F<F<B, A>, F<Array<B>, P1<Array<A>>>>() {
         public F<Array<B>, P1<Array<A>>> f(F<B, A> f) {
            return Strategy.this.parMapArray(f);
         }
      };
   }

   public <B> F<F<B, A>, F<Array<B>, Array<A>>> parMapArray1() {
      return new F<F<B, A>, F<Array<B>, Array<A>>>() {
         public F<Array<B>, Array<A>> f(final F<B, A> f) {
            return new F<Array<B>, Array<A>>() {
               public Array<A> f(Array<B> bs) {
                  return Strategy.this.parMap1(f, bs);
               }
            };
         }
      };
   }

   public static <A, B> P1<List<B>> parFlatMap(Strategy<List<B>> s, F<A, List<B>> f, List<A> as) {
      return (P1)P1.fmap(List.join()).f(s.parMap(f, as));
   }

   public static <A, B> P1<Array<B>> parFlatMap(Strategy<Array<B>> s, F<A, Array<B>> f, Array<A> as) {
      return (P1)P1.fmap(Array.join()).f(s.parMap(f, as));
   }

   public static <A> P1<List<A>> parListChunk(Strategy<List<A>> s, int chunkLength, List<P1<A>> as) {
      return (P1)P1.fmap(List.join()).f(s.parList(as.partition(chunkLength).map(P1.sequenceList())));
   }

   public <B, C> P1<List<A>> parZipWith(F2<B, C, A> f, List<B> bs, List<C> cs) {
      return P1.sequence(bs.zipWith(cs, this.concurry(f)));
   }

   public <B, C> P1<Array<A>> parZipWith(F2<B, C, A> f, Array<B> bs, Array<C> cs) {
      return P1.sequence(bs.zipWith(cs, this.concurry(f)));
   }

   public <B, C> F2<List<B>, List<C>, P1<List<A>>> parZipListWith(final F2<B, C, A> f) {
      return new F2<List<B>, List<C>, P1<List<A>>>() {
         public P1<List<A>> f(List<B> bs, List<C> cs) {
            return Strategy.this.parZipWith(f, bs, cs);
         }
      };
   }

   public <B, C> F2<Array<B>, Array<C>, P1<Array<A>>> parZipArrayWith(final F2<B, C, A> f) {
      return new F2<Array<B>, Array<C>, P1<Array<A>>>() {
         public P1<Array<A>> f(Array<B> bs, Array<C> cs) {
            return Strategy.this.parZipWith(f, bs, cs);
         }
      };
   }

   public static <A> F<Future<A>, P1<A>> obtain() {
      return new F<Future<A>, P1<A>>() {
         public P1<A> f(Future<A> t) {
            return Strategy.obtain(t);
         }
      };
   }

   public static <A> P1<A> obtain(final Future<A> t) {
      return new P1<A>() {
         public A _1() {
            try {
               return t.get();
            } catch (InterruptedException var2) {
               Thread.currentThread().interrupt();
               throw new Error(var2);
            } catch (ExecutionException var3) {
               throw new Error(var3);
            }
         }
      };
   }

   public static <A> Effect1<Future<A>> discard() {
      return new Effect1<Future<A>>() {
         public void f(Future<A> a) {
            ((P1)Strategy.obtain().f(a))._1();
         }
      };
   }

   public static <A> Strategy<A> simpleThreadStrategy() {
      return strategy(new F<P1<A>, P1<A>>() {
         public P1<A> f(P1<A> p) {
            FutureTask<A> t = new FutureTask((Callable)Java.P1_Callable().f(p));
            (new Thread(t)).start();
            return Strategy.obtain(t);
         }
      });
   }

   public static <A> Strategy<A> executorStrategy(final ExecutorService s) {
      return strategy(new F<P1<A>, P1<A>>() {
         public P1<A> f(P1<A> p) {
            return Strategy.obtain(s.submit((Callable)Java.P1_Callable().f(p)));
         }
      });
   }

   public static <A> Strategy<A> completionStrategy(final CompletionService<A> s) {
      return strategy(new F<P1<A>, P1<A>>() {
         public P1<A> f(P1<A> p) {
            return Strategy.obtain(s.submit((Callable)Java.P1_Callable().f(p)));
         }
      });
   }

   public static <A> Strategy<A> seqStrategy() {
      return strategy(new F<P1<A>, P1<A>>() {
         public P1<A> f(P1<A> a) {
            return P.p(a._1());
         }
      });
   }

   public static <A> Strategy<A> idStrategy() {
      return strategy(Function.identity());
   }

   public <B> Strategy<B> xmap(F<P1<A>, P1<B>> f, F<P1<B>, P1<A>> g) {
      return strategy(Function.compose(f, Function.compose(this.f(), g)));
   }

   public Strategy<A> map(F<P1<A>, P1<A>> f) {
      return this.xmap(f, Function.identity());
   }

   public Strategy<A> comap(F<P1<A>, P1<A>> f) {
      return this.xmap(Function.identity(), f);
   }

   public Strategy<A> errorStrategy(Effect1<Error> e) {
      return errorStrategy(this, e);
   }

   public static <A> Strategy<A> errorStrategy(Strategy<A> s, final Effect1<Error> e) {
      return s.comap(new F<P1<A>, P1<A>>() {
         public P1<A> f(final P1<A> a) {
            return new P1<A>() {
               public A _1() {
                  try {
                     return a._1();
                  } catch (Throwable var3) {
                     Error error = new Error(var3);
                     e.f(error);
                     throw error;
                  }
               }
            };
         }
      });
   }

   public static <A> Strategy<Callable<A>> callableStrategy(Strategy<Callable<A>> s) {
      return s.comap(new F<P1<Callable<A>>, P1<Callable<A>>>() {
         public P1<Callable<A>> f(P1<Callable<A>> a) {
            return (P1)P1.curry(Callables.normalise()).f(a._1());
         }
      });
   }
}
