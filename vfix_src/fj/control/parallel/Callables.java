package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P1;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import java.util.concurrent.Callable;

public final class Callables {
   private Callables() {
   }

   public static <A> Callable<A> callable(final A a) {
      return new Callable<A>() {
         public A call() throws Exception {
            return a;
         }
      };
   }

   public static <A> Callable<A> callable(final Exception e) {
      return new Callable<A>() {
         public A call() throws Exception {
            throw e;
         }
      };
   }

   public static <A> F<A, Callable<A>> callable() {
      return new F<A, Callable<A>>() {
         public Callable<A> f(A a) {
            return Callables.callable(a);
         }
      };
   }

   public static <A, B> F<A, Callable<B>> callable(final F<A, B> f) {
      return new F<A, Callable<B>>() {
         public Callable<B> f(final A a) {
            return new Callable<B>() {
               public B call() {
                  return f.f(a);
               }
            };
         }
      };
   }

   public static <A, B> F<F<A, B>, F<A, Callable<B>>> arrow() {
      return new F<F<A, B>, F<A, Callable<B>>>() {
         public F<A, Callable<B>> f(F<A, B> f) {
            return Callables.callable(f);
         }
      };
   }

   public static <A, B> Callable<B> bind(final Callable<A> a, final F<A, Callable<B>> f) {
      return new Callable<B>() {
         public B call() throws Exception {
            return ((Callable)f.f(a.call())).call();
         }
      };
   }

   public static <A, B> F<Callable<A>, Callable<B>> fmap(final F<A, B> f) {
      return new F<Callable<A>, Callable<B>>() {
         public Callable<B> f(Callable<A> a) {
            return Callables.bind(a, Callables.callable(f));
         }
      };
   }

   public static <A, B> Callable<B> apply(final Callable<A> ca, Callable<F<A, B>> cf) {
      return bind(cf, new F<F<A, B>, Callable<B>>() {
         public Callable<B> f(F<A, B> f) {
            return (Callable)Callables.fmap(f).f(ca);
         }
      });
   }

   public static <A, B, C> Callable<C> bind(Callable<A> ca, Callable<B> cb, F<A, F<B, C>> f) {
      return apply(cb, (Callable)fmap(f).f(ca));
   }

   public static <A> Callable<A> join(Callable<Callable<A>> a) {
      return bind(a, Function.identity());
   }

   public static <A, B, C> F<Callable<A>, F<Callable<B>, Callable<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<Callable<A>, Callable<B>, Callable<C>>() {
         public Callable<C> f(Callable<A> ca, Callable<B> cb) {
            return Callables.bind(ca, cb, f);
         }
      });
   }

   public static <A> Callable<List<A>> sequence(List<Callable<A>> as) {
      return (Callable)as.foldRight((F)liftM2(List.cons()), callable((Object)List.nil()));
   }

   public static <A> F<List<Callable<A>>, Callable<List<A>>> sequence_() {
      return new F<List<Callable<A>>, Callable<List<A>>>() {
         public Callable<List<A>> f(List<Callable<A>> as) {
            return Callables.sequence(as);
         }
      };
   }

   public static <A> P1<Option<A>> option(final Callable<A> a) {
      return new P1<Option<A>>() {
         public Option<A> _1() {
            try {
               return Option.some(a.call());
            } catch (Exception var2) {
               return Option.none();
            }
         }
      };
   }

   public static <A> F<Callable<A>, P1<Option<A>>> option() {
      return new F<Callable<A>, P1<Option<A>>>() {
         public P1<Option<A>> f(Callable<A> a) {
            return Callables.option(a);
         }
      };
   }

   public static <A> P1<Either<Exception, A>> either(final Callable<A> a) {
      return new P1<Either<Exception, A>>() {
         public Either<Exception, A> _1() {
            try {
               return Either.right(a.call());
            } catch (Exception var2) {
               return Either.left(var2);
            }
         }
      };
   }

   public static <A> F<Callable<A>, P1<Either<Exception, A>>> either() {
      return new F<Callable<A>, P1<Either<Exception, A>>>() {
         public P1<Either<Exception, A>> f(Callable<A> a) {
            return Callables.either(a);
         }
      };
   }

   public static <A> Callable<A> fromEither(final P1<Either<Exception, A>> e) {
      return new Callable<A>() {
         public A call() throws Exception {
            Either<Exception, A> e1 = (Either)e._1();
            if (e1.isLeft()) {
               throw (Exception)e1.left().value();
            } else {
               return e1.right().value();
            }
         }
      };
   }

   public static <A> F<P1<Either<Exception, A>>, Callable<A>> fromEither() {
      return new F<P1<Either<Exception, A>>, Callable<A>>() {
         public Callable<A> f(P1<Either<Exception, A>> e) {
            return Callables.fromEither(e);
         }
      };
   }

   public static <A> Callable<A> fromOption(final P1<Option<A>> o) {
      return new Callable<A>() {
         public A call() throws Exception {
            Option<A> o1 = (Option)o._1();
            if (o1.isSome()) {
               return o1.some();
            } else {
               throw new Exception("No value.");
            }
         }
      };
   }

   public static <A> F<P1<Option<A>>, Callable<A>> fromOption() {
      return new F<P1<Option<A>>, Callable<A>>() {
         public Callable<A> f(P1<Option<A>> o) {
            return Callables.fromOption(o);
         }
      };
   }

   public static <A> Callable<A> normalise(Callable<A> a) {
      try {
         return callable(a.call());
      } catch (Exception var2) {
         return callable(var2);
      }
   }

   public static <A> F<Callable<A>, Callable<A>> normalise() {
      return new F<Callable<A>, Callable<A>>() {
         public Callable<A> f(Callable<A> a) {
            return Callables.normalise(a);
         }
      };
   }
}
