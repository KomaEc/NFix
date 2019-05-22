package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;

public abstract class Option<A> implements Iterable<A> {
   public static final F<String, Option<Byte>> parseByte = new F<String, Option<Byte>>() {
      public Option<Byte> f(String s) {
         return Validation.parseByte(s).toOption();
      }
   };
   public static final F<String, Option<Double>> parseDouble = new F<String, Option<Double>>() {
      public Option<Double> f(String s) {
         return Validation.parseDouble(s).toOption();
      }
   };
   public static final F<String, Option<Float>> parseFloat = new F<String, Option<Float>>() {
      public Option<Float> f(String s) {
         return Validation.parseFloat(s).toOption();
      }
   };
   public static final F<String, Option<Integer>> parseInt = new F<String, Option<Integer>>() {
      public Option<Integer> f(String s) {
         return Validation.parseInt(s).toOption();
      }
   };
   public static final F<String, Option<Long>> parseLong = new F<String, Option<Long>>() {
      public Option<Long> f(String s) {
         return Validation.parseLong(s).toOption();
      }
   };
   public static final F<String, Option<Short>> parseShort = new F<String, Option<Short>>() {
      public Option<Short> f(String s) {
         return Validation.parseShort(s).toOption();
      }
   };

   private Option() {
   }

   public String toString() {
      Show<A> s = Show.anyShow();
      return Show.optionShow(s).showS((Object)this);
   }

   public final Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   public abstract A some();

   public final boolean isSome() {
      return this instanceof Option.Some;
   }

   public final boolean isNone() {
      return this instanceof Option.None;
   }

   public static <A> F<Option<A>, Boolean> isSome_() {
      return new F<Option<A>, Boolean>() {
         public Boolean f(Option<A> a) {
            return a.isSome();
         }
      };
   }

   public static <A> F<Option<A>, Boolean> isNone_() {
      return new F<Option<A>, Boolean>() {
         public Boolean f(Option<A> a) {
            return a.isNone();
         }
      };
   }

   public final <B> B option(B b, F<A, B> f) {
      return this.isSome() ? f.f(this.some()) : b;
   }

   public final <B> B option(P1<B> b, F<A, B> f) {
      return this.isSome() ? f.f(this.some()) : b._1();
   }

   public final int length() {
      return this.isSome() ? 1 : 0;
   }

   public final A orSome(P1<A> a) {
      return this.isSome() ? this.some() : a._1();
   }

   public final A orSome(A a) {
      return this.isSome() ? this.some() : a;
   }

   public final A valueE(P1<String> message) {
      if (this.isSome()) {
         return this.some();
      } else {
         throw Bottom.error((String)message._1());
      }
   }

   public final A valueE(String message) {
      if (this.isSome()) {
         return this.some();
      } else {
         throw Bottom.error(message);
      }
   }

   public final <B> Option<B> map(F<A, B> f) {
      return this.isSome() ? some(f.f(this.some())) : none();
   }

   public static <A, B> F<F<A, B>, F<Option<A>, Option<B>>> map() {
      return Function.curry(new F2<F<A, B>, Option<A>, Option<B>>() {
         public Option<B> f(F<A, B> abf, Option<A> option) {
            return option.map(abf);
         }
      });
   }

   public final Unit foreach(F<A, Unit> f) {
      return this.isSome() ? (Unit)f.f(this.some()) : Unit.unit();
   }

   public final void foreachDoEffect(Effect1<A> f) {
      if (this.isSome()) {
         f.f(this.some());
      }

   }

   public final Option<A> filter(F<A, Boolean> f) {
      return this.isSome() ? ((Boolean)f.f(this.some()) ? this : none()) : none();
   }

   public final <B> Option<B> bind(F<A, Option<B>> f) {
      return this.isSome() ? (Option)f.f(this.some()) : none();
   }

   public final <B, C> Option<C> bind(Option<B> ob, F<A, F<B, C>> f) {
      return ob.apply(this.map(f));
   }

   public final <B, C, D> Option<D> bind(Option<B> ob, Option<C> oc, F<A, F<B, F<C, D>>> f) {
      return oc.apply(this.bind(ob, f));
   }

   public final <B, C, D, E> Option<E> bind(Option<B> ob, Option<C> oc, Option<D> od, F<A, F<B, F<C, F<D, E>>>> f) {
      return od.apply(this.bind(ob, oc, f));
   }

   public final <B, C, D, E, F$> Option<F$> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
      return oe.apply(this.bind(ob, oc, od, f));
   }

   public final <B, C, D, E, F$, G> Option<G> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
      return of.apply(this.bind(ob, oc, od, oe, f));
   }

   public final <B, C, D, E, F$, G, H> Option<H> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
      return og.apply(this.bind(ob, oc, od, oe, of, f));
   }

   public final <B, C, D, E, F$, G, H, I> Option<I> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, Option<H> oh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
      return oh.apply(this.bind(ob, oc, od, oe, of, og, f));
   }

   public final <B> Option<P2<A, B>> bindProduct(Option<B> ob) {
      return this.bind(ob, P.p2());
   }

   public final <B, C> Option<P3<A, B, C>> bindProduct(Option<B> ob, Option<C> oc) {
      return this.bind(ob, oc, P.p3());
   }

   public final <B, C, D> Option<P4<A, B, C, D>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od) {
      return this.bind(ob, oc, od, P.p4());
   }

   public final <B, C, D, E> Option<P5<A, B, C, D, E>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe) {
      return this.bind(ob, oc, od, oe, P.p5());
   }

   public final <B, C, D, E, F$> Option<P6<A, B, C, D, E, F$>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of) {
      return this.bind(ob, oc, od, oe, of, P.p6());
   }

   public final <B, C, D, E, F$, G> Option<P7<A, B, C, D, E, F$, G>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og) {
      return this.bind(ob, oc, od, oe, of, og, P.p7());
   }

   public final <B, C, D, E, F$, G, H> Option<P8<A, B, C, D, E, F$, G, H>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, Option<H> oh) {
      return this.bind(ob, oc, od, oe, of, og, oh, P.p8());
   }

   public final <B> Option<B> sequence(Option<B> o) {
      F<A, Option<B>> c = Function.constant(o);
      return this.bind(c);
   }

   public final <B> Option<B> apply(Option<F<A, B>> of) {
      return of.bind(new F<F<A, B>, Option<B>>() {
         public Option<B> f(final F<A, B> f) {
            return Option.this.map(new F<A, B>() {
               public B f(A a) {
                  return f.f(a);
               }
            });
         }
      });
   }

   public final Option<A> orElse(P1<Option<A>> o) {
      return this.isSome() ? this : (Option)o._1();
   }

   public final Option<A> orElse(Option<A> o) {
      return this.isSome() ? this : o;
   }

   public final <X> Either<X, A> toEither(P1<X> x) {
      return this.isSome() ? Either.right(this.some()) : Either.left(x._1());
   }

   public final <X> Either<X, A> toEither(X x) {
      return this.isSome() ? Either.right(this.some()) : Either.left(x);
   }

   public final <X> Validation<X, A> toValidation(X x) {
      return Validation.validation(this.toEither(x));
   }

   public static <A, X> F<Option<A>, F<X, Either<X, A>>> toEither() {
      return Function.curry(new F2<Option<A>, X, Either<X, A>>() {
         public Either<X, A> f(Option<A> a, X x) {
            return a.toEither(x);
         }
      });
   }

   public final List<A> toList() {
      return this.isSome() ? List.cons(this.some(), List.nil()) : List.nil();
   }

   public final Stream<A> toStream() {
      return this.isSome() ? Stream.nil().cons(this.some()) : Stream.nil();
   }

   public final Array<A> toArray() {
      return this.isSome() ? Array.array(this.some()) : Array.empty();
   }

   public final Array<A> toArray(Class<A[]> c) {
      if (this.isSome()) {
         A[] a = (Object[])((Object[])java.lang.reflect.Array.newInstance(c.getComponentType(), 1));
         a[0] = this.some();
         return Array.array(a);
      } else {
         return Array.array((Object[])((Object[])java.lang.reflect.Array.newInstance(c.getComponentType(), 0)));
      }
   }

   public final A[] array(Class<A[]> c) {
      return this.toArray(c).array(c);
   }

   public final A toNull() {
      return this.orSome((Object)null);
   }

   public final boolean forall(F<A, Boolean> f) {
      return this.isNone() || (Boolean)f.f(this.some());
   }

   public final boolean exists(F<A, Boolean> f) {
      return this.isSome() && (Boolean)f.f(this.some());
   }

   public final Collection<A> toCollection() {
      return this.toList().toCollection();
   }

   public static <T> F<T, Option<T>> some_() {
      return new F<T, Option<T>>() {
         public Option<T> f(T t) {
            return Option.some(t);
         }
      };
   }

   public static <T> Option<T> some(T t) {
      return new Option.Some(t);
   }

   public static <T> F<T, Option<T>> none_() {
      return new F<T, Option<T>>() {
         public Option<T> f(T t) {
            return Option.none();
         }
      };
   }

   public static <T> Option<T> none() {
      return new Option.None();
   }

   public static <T> Option<T> fromNull(T t) {
      return t == null ? none() : some(t);
   }

   public static <T> F<T, Option<T>> fromNull() {
      return new F<T, Option<T>>() {
         public Option<T> f(T t) {
            return Option.fromNull(t);
         }
      };
   }

   public static <A> Option<A> join(Option<Option<A>> o) {
      F<Option<A>, Option<A>> id = Function.identity();
      return o.bind(id);
   }

   public static <A> Option<List<A>> sequence(final List<Option<A>> a) {
      return a.isEmpty() ? some(List.nil()) : ((Option)a.head()).bind(new F<A, Option<List<A>>>() {
         public Option<List<A>> f(A aa) {
            return Option.sequence(a.tail()).map(List.cons_(aa));
         }
      });
   }

   public static <A> Option<A> iif(F<A, Boolean> f, A a) {
      return (Boolean)f.f(a) ? some(a) : none();
   }

   public static <A> Option<A> iif(boolean p, P1<A> a) {
      return p ? some(a._1()) : none();
   }

   public static <A> Option<A> iif(boolean p, A a) {
      return iif(p, P.p(a));
   }

   public static <A> F2<F<A, Boolean>, A, Option<A>> iif() {
      return new F2<F<A, Boolean>, A, Option<A>>() {
         public Option<A> f(F<A, Boolean> p, A a) {
            return Option.iif(p, a);
         }
      };
   }

   public static <A> List<A> somes(List<Option<A>> as) {
      return as.filter(isSome_()).map(new F<Option<A>, A>() {
         public A f(Option<A> o) {
            return o.some();
         }
      });
   }

   public static <A> Stream<A> somes(Stream<Option<A>> as) {
      return as.filter(isSome_()).map(new F<Option<A>, A>() {
         public A f(Option<A> o) {
            return o.some();
         }
      });
   }

   public static Option<String> fromString(String s) {
      return fromNull(s).bind(new F<String, Option<String>>() {
         public Option<String> f(String s) {
            Option<String> none = Option.none();
            return s.length() == 0 ? none : Option.some(s);
         }
      });
   }

   public static F<String, Option<String>> fromString() {
      return new F<String, Option<String>>() {
         public Option<String> f(String s) {
            return Option.fromString(s);
         }
      };
   }

   public static <A> F<Option<A>, A> fromSome() {
      return new F<Option<A>, A>() {
         public A f(Option<A> option) {
            return option.some();
         }
      };
   }

   public static <A, B, C> F<Option<A>, F<Option<B>, Option<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<Option<A>, Option<B>, Option<C>>() {
         public Option<C> f(Option<A> a, Option<B> b) {
            return a.bind(b, f);
         }
      });
   }

   public static <A, B> F<F<A, Option<B>>, F<Option<A>, Option<B>>> bind() {
      return Function.curry(new F2<F<A, Option<B>>, Option<A>, Option<B>>() {
         public Option<B> f(F<A, Option<B>> f, Option<A> a) {
            return a.bind(f);
         }
      });
   }

   public static <A> F<Option<Option<A>>, Option<A>> join() {
      return new F<Option<Option<A>>, Option<A>>() {
         public Option<A> f(Option<Option<A>> option) {
            return Option.join(option);
         }
      };
   }

   // $FF: synthetic method
   Option(Object x0) {
      this();
   }

   private static final class Some<A> extends Option<A> {
      private final A a;

      Some(A a) {
         super(null);
         this.a = a;
      }

      public A some() {
         return this.a;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.a == null ? 0 : this.a.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            Option.Some<?> other = (Option.Some)obj;
            if (this.a == null) {
               if (other.a != null) {
                  return false;
               }
            } else if (!this.a.equals(other.a)) {
               return false;
            }

            return true;
         }
      }
   }

   private static final class None<A> extends Option<A> {
      private None() {
         super(null);
      }

      public A some() {
         throw Bottom.error("some on None");
      }

      public int hashCode() {
         return 31;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else {
            return this.getClass() == obj.getClass();
         }
      }

      // $FF: synthetic method
      None(Object x0) {
         this();
      }
   }
}
