package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.function.Effect1;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class Promise<A> {
   private final Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> actor;
   private final Strategy<Unit> s;
   private final CountDownLatch l = new CountDownLatch(1);
   private volatile Option<A> v = Option.none();
   private final Queue<Actor<A>> waiting = new LinkedList();

   private Promise(Strategy<Unit> s, Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> qa) {
      this.s = s;
      this.actor = qa;
   }

   private static <A> Promise<A> mkPromise(Strategy<Unit> s) {
      Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> q = Actor.queueActor(s, new Effect1<P2<Either<P1<A>, Actor<A>>, Promise<A>>>() {
         public void f(P2<Either<P1<A>, Actor<A>>, Promise<A>> p) {
            Promise<A> snd = (Promise)p._2();
            Queue<Actor<A>> as = snd.waiting;
            if (((Either)p._1()).isLeft()) {
               A a = ((P1)((Either)p._1()).left().value())._1();
               snd.v = Option.some(a);
               snd.l.countDown();

               while(!as.isEmpty()) {
                  ((Actor)as.remove()).act(a);
               }
            } else if (snd.v.isNone()) {
               as.add(((Either)p._1()).right().value());
            } else {
               ((Actor)((Either)p._1()).right().value()).act(snd.v.some());
            }

         }
      });
      return new Promise(s, q);
   }

   public static <A> Promise<A> promise(Strategy<Unit> s, P1<A> a) {
      Promise<A> p = mkPromise(s);
      p.actor.act(P.p(Either.left(a), p));
      return p;
   }

   public static <A> F<P1<A>, Promise<A>> promise(final Strategy<Unit> s) {
      return new F<P1<A>, Promise<A>>() {
         public Promise<A> f(P1<A> a) {
            return Promise.promise(s, a);
         }
      };
   }

   public static <A> Promise<Callable<A>> promise(Strategy<Unit> s, final Callable<A> a) {
      return promise(s, new P1<Callable<A>>() {
         public Callable<A> _1() {
            return Callables.normalise(a);
         }
      });
   }

   public static <A, B> F<A, Promise<B>> promise(final Strategy<Unit> s, final F<A, B> f) {
      return new F<A, Promise<B>>() {
         public Promise<B> f(A a) {
            return Promise.promise(s, (P1)P1.curry(f).f(a));
         }
      };
   }

   public void to(Actor<A> a) {
      this.actor.act(P.p(Either.right(a), this));
   }

   public <B> Promise<B> fmap(F<A, B> f) {
      return this.bind(promise(this.s, f));
   }

   public static <A, B> F<Promise<A>, Promise<B>> fmap_(final F<A, B> f) {
      return new F<Promise<A>, Promise<B>>() {
         public Promise<B> f(Promise<A> a) {
            return a.fmap(f);
         }
      };
   }

   public static <A> Promise<A> join(Promise<Promise<A>> p) {
      F<Promise<A>, Promise<A>> id = Function.identity();
      return p.bind(id);
   }

   public static <A> Promise<A> join(Strategy<Unit> s, P1<Promise<A>> p) {
      return join(promise(s, p));
   }

   public <B> Promise<B> bind(F<A, Promise<B>> f) {
      final Promise<B> r = mkPromise(this.s);
      Actor<B> ab = Actor.actor(this.s, new Effect1<B>() {
         public void f(B b) {
            r.actor.act(P.p(Either.left(P.p(b)), r));
         }
      });
      this.to(ab.promise().comap(f));
      return r;
   }

   public <B> Promise<B> apply(Promise<F<A, B>> pf) {
      return pf.bind(new F<F<A, B>, Promise<B>>() {
         public Promise<B> f(F<A, B> f) {
            return Promise.this.fmap(f);
         }
      });
   }

   public <B, C> Promise<C> bind(Promise<B> pb, F<A, F<B, C>> f) {
      return pb.apply(this.fmap(f));
   }

   public <B, C> Promise<C> bind(P1<Promise<B>> p, F<A, F<B, C>> f) {
      return join(this.s, p).apply(this.fmap(f));
   }

   public static <A, B, C> F<Promise<A>, F<Promise<B>, Promise<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<Promise<A>, Promise<B>, Promise<C>>() {
         public Promise<C> f(Promise<A> ca, Promise<B> cb) {
            return ca.bind(cb, f);
         }
      });
   }

   public static <A> Promise<List<A>> sequence(Strategy<Unit> s, List<Promise<A>> as) {
      return join((Promise)foldRight(s, liftM2(List.cons()), promise(s, P.p(List.nil()))).f(as));
   }

   public static <A> F<List<Promise<A>>, Promise<List<A>>> sequence(final Strategy<Unit> s) {
      return new F<List<Promise<A>>, Promise<List<A>>>() {
         public Promise<List<A>> f(List<Promise<A>> as) {
            return Promise.sequence(s, as);
         }
      };
   }

   public static <A> Promise<Stream<A>> sequence(Strategy<Unit> s, Stream<Promise<A>> as) {
      return join((Promise)foldRightS(s, Function.curry(new F2<Promise<A>, P1<Promise<Stream<A>>>, Promise<Stream<A>>>() {
         public Promise<Stream<A>> f(Promise<A> o, final P1<Promise<Stream<A>>> p) {
            return o.bind(new F<A, Promise<Stream<A>>>() {
               public Promise<Stream<A>> f(A a) {
                  return ((Promise)p._1()).fmap((F)Stream.cons_().f(a));
               }
            });
         }
      }), promise(s, P.p(Stream.nil()))).f(as));
   }

   public static <A> F<List<Promise<A>>, Promise<List<A>>> sequenceS(final Strategy<Unit> s) {
      return new F<List<Promise<A>>, Promise<List<A>>>() {
         public Promise<List<A>> f(List<Promise<A>> as) {
            return Promise.sequence(s, as);
         }
      };
   }

   public static <A> Promise<P1<A>> sequence(Strategy<Unit> s, P1<Promise<A>> p) {
      return join(promise(s, p)).fmap(P.p1());
   }

   public static <A, B> F<List<A>, Promise<B>> foldRight(final Strategy<Unit> s, final F<A, F<B, B>> f, final B b) {
      return new F<List<A>, Promise<B>>() {
         public Promise<B> f(List<A> as) {
            return as.isEmpty() ? Promise.promise(s, P.p(b)) : (Promise)((F)Promise.liftM2(f).f(Promise.promise(s, P.p(as.head())))).f(Promise.join(s, (P1)P1.curry(this).f(as.tail())));
         }
      };
   }

   public static <A, B> F<Stream<A>, Promise<B>> foldRightS(final Strategy<Unit> s, final F<A, F<P1<B>, B>> f, final B b) {
      return new F<Stream<A>, Promise<B>>() {
         public Promise<B> f(final Stream<A> as) {
            return as.isEmpty() ? Promise.promise(s, P.p(b)) : (Promise)((F)Promise.liftM2(f).f(Promise.promise(s, P.p(as.head())))).f(Promise.join(s, new P1<Promise<P1<B>>>() {
               public Promise<P1<B>> _1() {
                  return f((Stream)as.tail()._1()).fmap(P.p1());
               }
            }));
         }
      };
   }

   public A claim() {
      try {
         this.l.await();
      } catch (InterruptedException var2) {
         throw new Error(var2);
      }

      return this.v.some();
   }

   public Option<A> claim(long timeout, TimeUnit unit) {
      try {
         if (this.l.await(timeout, unit)) {
            return this.v;
         }
      } catch (InterruptedException var5) {
         throw new Error(var5);
      }

      return Option.none();
   }

   public boolean isFulfilled() {
      return this.v.isSome();
   }

   public <B> Promise<B> cobind(final F<Promise<A>, B> f) {
      return promise(this.s, new P1<B>() {
         public B _1() {
            return f.f(Promise.this);
         }
      });
   }

   public Promise<Promise<A>> cojoin() {
      F<Promise<A>, Promise<A>> id = Function.identity();
      return this.cobind(id);
   }

   public <B> Stream<B> sequenceW(final Stream<F<Promise<A>, B>> fs) {
      return fs.isEmpty() ? Stream.nil() : Stream.cons(((F)fs.head()).f(this), new P1<Stream<B>>() {
         public Stream<B> _1() {
            return Promise.this.sequenceW((Stream)fs.tail()._1());
         }
      });
   }
}
