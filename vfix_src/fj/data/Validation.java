package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.F6;
import fj.F7;
import fj.F8;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Semigroup;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Iterator;

public class Validation<E, T> implements Iterable<T> {
   private final Either<E, T> e;
   public static final F<String, Validation<NumberFormatException, Byte>> parseByte = new F<String, Validation<NumberFormatException, Byte>>() {
      public Validation<NumberFormatException, Byte> f(String s) {
         return Validation.parseByte(s);
      }
   };
   public static final F<String, Validation<NumberFormatException, Double>> parseDouble = new F<String, Validation<NumberFormatException, Double>>() {
      public Validation<NumberFormatException, Double> f(String s) {
         return Validation.parseDouble(s);
      }
   };
   public static final F<String, Validation<NumberFormatException, Float>> parseFloat = new F<String, Validation<NumberFormatException, Float>>() {
      public Validation<NumberFormatException, Float> f(String s) {
         return Validation.parseFloat(s);
      }
   };
   public static final F<String, Validation<NumberFormatException, Integer>> parseInt = new F<String, Validation<NumberFormatException, Integer>>() {
      public Validation<NumberFormatException, Integer> f(String s) {
         return Validation.parseInt(s);
      }
   };
   public static final F<String, Validation<NumberFormatException, Long>> parseLong = new F<String, Validation<NumberFormatException, Long>>() {
      public Validation<NumberFormatException, Long> f(String s) {
         return Validation.parseLong(s);
      }
   };
   public static final F<String, Validation<NumberFormatException, Short>> parseShort = new F<String, Validation<NumberFormatException, Short>>() {
      public Validation<NumberFormatException, Short> f(String s) {
         return Validation.parseShort(s);
      }
   };

   protected Validation(Either<E, T> e) {
      this.e = e;
   }

   public boolean isFail() {
      return this.e.isLeft();
   }

   public boolean isSuccess() {
      return this.e.isRight();
   }

   public E fail() {
      if (this.isFail()) {
         return this.e.left().value();
      } else {
         throw Bottom.error("Validation: fail on success value");
      }
   }

   public T success() {
      if (this.isSuccess()) {
         return this.e.right().value();
      } else {
         throw Bottom.error("Validation: success on fail value");
      }
   }

   public <X> X validation(F<E, X> fail, F<T, X> success) {
      return this.e.either(fail, success);
   }

   public Validation<E, T>.FailProjection<E, T> f() {
      return new Validation.FailProjection(this);
   }

   public Either<E, T> toEither() {
      return this.e;
   }

   public T successE(P1<String> err) {
      return this.e.right().valueE(err);
   }

   public T successE(String err) {
      return this.e.right().valueE(P.p(err));
   }

   public T orSuccess(P1<T> t) {
      return this.e.right().orValue(t);
   }

   public T orSuccess(T t) {
      return this.e.right().orValue(P.p(t));
   }

   public T on(F<E, T> f) {
      return this.e.right().on(f);
   }

   public Unit foreach(F<T, Unit> f) {
      return this.e.right().foreach(f);
   }

   public void foreachDoEffect(Effect1<T> f) {
      this.e.right().foreachDoEffect(f);
   }

   public <A> Validation<E, A> map(F<T, A> f) {
      return this.isFail() ? fail(this.fail()) : success(f.f(this.success()));
   }

   public <A> Validation<E, A> bind(F<T, Validation<E, A>> f) {
      return this.isSuccess() ? (Validation)f.f(this.success()) : fail(this.fail());
   }

   public <A> Validation<E, A> sequence(Validation<E, A> v) {
      return this.bind(Function.constant(v));
   }

   public <A> Option<Validation<A, T>> filter(F<T, Boolean> f) {
      return this.e.right().filter(f).map(validation());
   }

   public <A> Validation<E, A> apply(Validation<E, F<T, A>> v) {
      return v.bind(new F<F<T, A>, Validation<E, A>>() {
         public Validation<E, A> f(F<T, A> f) {
            return Validation.this.map(f);
         }
      });
   }

   public boolean forall(F<T, Boolean> f) {
      return this.e.right().forall(f);
   }

   public boolean exists(F<T, Boolean> f) {
      return this.e.right().exists(f);
   }

   public List<T> toList() {
      return this.e.right().toList();
   }

   public Option<T> toOption() {
      return this.e.right().toOption();
   }

   public Array<T> toArray() {
      return this.e.right().toArray();
   }

   public Stream<T> toStream() {
      return this.e.right().toStream();
   }

   public <A> Validation<E, A> accumapply(Semigroup<E> s, Validation<E, F<T, A>> v) {
      return this.isFail() ? fail(v.isFail() ? s.sum(v.fail(), this.fail()) : this.fail()) : (v.isFail() ? fail(v.fail()) : success(((F)v.success()).f(this.success())));
   }

   public <A, B> Validation<E, B> accumulate(Semigroup<E> s, Validation<E, A> va, F<T, F<A, B>> f) {
      return va.accumapply(s, this.map(f));
   }

   public <A, B> Validation<E, B> accumulate(Semigroup<E> s, Validation<E, A> va, F2<T, A, B> f) {
      return va.accumapply(s, this.map(Function.curry(f)));
   }

   public <A> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va) {
      return this.accumulate(s, va, new F2<T, A, Unit>() {
         public Unit f(T t, A a) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C> Validation<E, C> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, F<T, F<A, F<B, C>>> f) {
      return vb.accumapply(s, this.accumulate(s, va, f));
   }

   public <A, B, C> Validation<E, C> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, F3<T, A, B, C> f) {
      return vb.accumapply(s, this.accumulate(s, va, Function.curry(f)));
   }

   public <A, B> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb) {
      return this.accumulate(s, va, vb, new F3<T, A, B, Unit>() {
         public Unit f(T t, A a, B b) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C, D> Validation<E, D> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, F<T, F<A, F<B, F<C, D>>>> f) {
      return vc.accumapply(s, this.accumulate(s, va, vb, f));
   }

   public <A, B, C, D> Validation<E, D> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, F4<T, A, B, C, D> f) {
      return vc.accumapply(s, this.accumulate(s, va, vb, Function.curry(f)));
   }

   public <A, B, C> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc) {
      return this.accumulate(s, va, vb, vc, new F4<T, A, B, C, Unit>() {
         public Unit f(T t, A a, B b, C c) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C, D, E$> Validation<E, E$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, F<T, F<A, F<B, F<C, F<D, E$>>>>> f) {
      return vd.accumapply(s, this.accumulate(s, va, vb, vc, f));
   }

   public <A, B, C, D, E$> Validation<E, E$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, F5<T, A, B, C, D, E$> f) {
      return vd.accumapply(s, this.accumulate(s, va, vb, vc, Function.curry(f)));
   }

   public <A, B, C, D> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd) {
      return this.accumulate(s, va, vb, vc, vd, new F5<T, A, B, C, D, Unit>() {
         public Unit f(T t, A a, B b, C c, D d) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C, D, E$, F$> Validation<E, F$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, F<T, F<A, F<B, F<C, F<D, F<E$, F$>>>>>> f) {
      return ve.accumapply(s, this.accumulate(s, va, vb, vc, vd, f));
   }

   public <A, B, C, D, E$, F$> Validation<E, F$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, F6<T, A, B, C, D, E$, F$> f) {
      return ve.accumapply(s, this.accumulate(s, va, vb, vc, vd, Function.curry(f)));
   }

   public <A, B, C, D, E$> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve) {
      return this.accumulate(s, va, vb, vc, vd, ve, new F6<T, A, B, C, D, E$, Unit>() {
         public Unit f(T t, A a, B b, C c, D d, E$ e) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C, D, E$, F$, G> Validation<E, G> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, F<T, F<A, F<B, F<C, F<D, F<E$, F<F$, G>>>>>>> f) {
      return vf.accumapply(s, this.accumulate(s, va, vb, vc, vd, ve, f));
   }

   public <A, B, C, D, E$, F$, G> Validation<E, G> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, F7<T, A, B, C, D, E$, F$, G> f) {
      return vf.accumapply(s, this.accumulate(s, va, vb, vc, vd, ve, Function.curry(f)));
   }

   public <A, B, C, D, E$, F$> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf) {
      return this.accumulate(s, va, vb, vc, vd, ve, vf, new F7<T, A, B, C, D, E$, F$, Unit>() {
         public Unit f(T t, A a, B b, C c, D d, E$ e, F$ f) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public <A, B, C, D, E$, F$, G, H> Validation<E, H> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg, F<T, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, H>>>>>>>> f) {
      return vg.accumapply(s, this.accumulate(s, va, vb, vc, vd, ve, vf, f));
   }

   public <A, B, C, D, E$, F$, G, H> Validation<E, H> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg, F8<T, A, B, C, D, E$, F$, G, H> f) {
      return vg.accumapply(s, this.accumulate(s, va, vb, vc, vd, ve, vf, Function.curry(f)));
   }

   public <A, B, C, D, E$, F$, G> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg) {
      return this.accumulate(s, va, vb, vc, vd, ve, vf, vg, new F8<T, A, B, C, D, E$, F$, G, Unit>() {
         public Unit f(T t, A a, B b, C c, D d, E$ e, F$ f, G g) {
            return Unit.unit();
         }
      }).f().toOption();
   }

   public Iterator<T> iterator() {
      return this.toEither().right().iterator();
   }

   public Validation<List<E>, T> accumulate() {
      return this.isFail() ? fail(List.single(this.fail())) : success(this.success());
   }

   public <B> Validation<List<E>, B> accumulate(F<T, B> f) {
      return this.isFail() ? fail(List.single(this.fail())) : success(f.f(this.success()));
   }

   public <B, C> Validation<List<E>, C> accumulate(Validation<E, B> v2, F2<T, B, C> f) {
      List<E> list = List.nil();
      if (this.isFail()) {
         list = list.cons(this.fail());
      }

      if (v2.isFail()) {
         list = list.cons(v2.fail());
      }

      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success()));
   }

   public <B, C, D> Validation<List<E>, D> accumulate(Validation<E, B> v2, Validation<E, C> v3, F3<T, B, C, D> f) {
      List<E> list = fails(List.list(this, v2, v3));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success()));
   }

   public <B, C, D, $E> Validation<List<E>, E> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, F4<T, B, C, D, E> f) {
      List<E> list = fails(List.list(this, v2, v3, v4));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success(), v4.success()));
   }

   public <B, C, D, $E, $F> Validation<List<E>, $F> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, F5<T, B, C, D, $E, $F> f) {
      List<E> list = fails(List.list(this, v2, v3, v4, v5));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success(), v4.success(), v5.success()));
   }

   public <B, C, D, $E, $F, G> Validation<List<E>, G> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, F6<T, B, C, D, $E, $F, G> f) {
      List<E> list = fails(List.list(this, v2, v3, v4, v5));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success()));
   }

   public <B, C, D, $E, $F, G, H> Validation<List<E>, H> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, Validation<E, G> v7, F7<T, B, C, D, $E, $F, G, H> f) {
      List<E> list = fails(List.list(this, v2, v3, v4, v5));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success(), v7.success()));
   }

   public <B, C, D, $E, $F, G, H, I> Validation<List<E>, I> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, Validation<E, G> v7, Validation<E, H> v8, F8<T, B, C, D, $E, $F, G, H, I> f) {
      List<E> list = fails(List.list(this, v2, v3, v4, v5));
      return !list.isEmpty() ? fail(list) : success(f.f(this.success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success(), v7.success(), v8.success()));
   }

   public static <A, E> Validation<List<E>, List<A>> sequence(List<Validation<E, A>> list) {
      F2<Validation<E, A>, Validation<List<E>, List<A>>, Validation<List<E>, List<A>>> f2 = Validation$$Lambda$1.lambdaFactory$();
      return (Validation)list.foldRight((F2)f2, success(List.nil()));
   }

   public static <A, E> List<E> fails(List<Validation<E, ?>> list) {
      return list.filter(Validation$$Lambda$2.lambdaFactory$()).map(Validation$$Lambda$3.lambdaFactory$());
   }

   public static <A, E> List<A> successes(List<Validation<?, A>> list) {
      return list.filter(Validation$$Lambda$4.lambdaFactory$()).map(Validation$$Lambda$5.lambdaFactory$());
   }

   public Validation<NonEmptyList<E>, T> nel() {
      return this.isSuccess() ? success(this.success()) : fail(NonEmptyList.nel(this.fail()));
   }

   public static <E, T> Validation<E, T> validation(Either<E, T> e) {
      return new Validation(e);
   }

   public static <E, T> F<Either<E, T>, Validation<E, T>> validation() {
      return new F<Either<E, T>, Validation<E, T>>() {
         public Validation<E, T> f(Either<E, T> e) {
            return Validation.validation(e);
         }
      };
   }

   public static <E, T> F<Validation<E, T>, Either<E, T>> either() {
      return new F<Validation<E, T>, Either<E, T>>() {
         public Either<E, T> f(Validation<E, T> v) {
            return v.toEither();
         }
      };
   }

   public static <E, T> Validation<E, T> success(T t) {
      return validation(Either.right(t));
   }

   public static <E, T> Validation<E, T> fail(E e) {
      return validation(Either.left(e));
   }

   public static <E, T> Validation<NonEmptyList<E>, T> failNEL(E e) {
      return fail(NonEmptyList.nel(e));
   }

   public static <E, T> Validation<E, T> condition(boolean c, E e, T t) {
      return c ? success(t) : fail(e);
   }

   public static Validation<NumberFormatException, Byte> parseByte(String s) {
      try {
         return success(Byte.parseByte(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public static Validation<NumberFormatException, Double> parseDouble(String s) {
      try {
         return success(Double.parseDouble(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public static Validation<NumberFormatException, Float> parseFloat(String s) {
      try {
         return success(Float.parseFloat(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public static Validation<NumberFormatException, Integer> parseInt(String s) {
      try {
         return success(Integer.parseInt(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public static Validation<NumberFormatException, Long> parseLong(String s) {
      try {
         return success(Long.parseLong(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public static Validation<NumberFormatException, Short> parseShort(String s) {
      try {
         return success(Short.parseShort(s));
      } catch (NumberFormatException var2) {
         return fail(var2);
      }
   }

   public String toString() {
      return Show.validationShow(Show.anyShow(), Show.anyShow()).showS((Object)this);
   }

   // $FF: synthetic method
   private static Object lambda$successes$45(Validation v) {
      return v.success();
   }

   // $FF: synthetic method
   private static Boolean lambda$successes$44(Validation v) {
      return v.isSuccess();
   }

   // $FF: synthetic method
   private static Object lambda$fails$43(Validation v) {
      return v.fail();
   }

   // $FF: synthetic method
   private static Boolean lambda$fails$42(Validation v) {
      return v.isFail();
   }

   // $FF: synthetic method
   private static Validation lambda$sequence$41(Validation v, Validation acc) {
      if (acc.isFail() && v.isFail()) {
         return validation(acc.toEither().left().map(Validation$$Lambda$6.lambdaFactory$(v)));
      } else {
         return acc.isSuccess() && v.isSuccess() ? acc.map(Validation$$Lambda$7.lambdaFactory$(v)) : acc;
      }
   }

   // $FF: synthetic method
   private static List lambda$null$40(Validation var0, List l) {
      return l.cons(var0.success());
   }

   // $FF: synthetic method
   private static List lambda$null$39(Validation var0, List l) {
      return l.cons(var0.fail());
   }

   // $FF: synthetic method
   static Validation access$lambda$0(Validation var0, Validation var1) {
      return lambda$sequence$41(var0, var1);
   }

   // $FF: synthetic method
   static Boolean access$lambda$1(Validation var0) {
      return lambda$fails$42(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$2(Validation var0) {
      return lambda$fails$43(var0);
   }

   // $FF: synthetic method
   static Boolean access$lambda$3(Validation var0) {
      return lambda$successes$44(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$4(Validation var0) {
      return lambda$successes$45(var0);
   }

   // $FF: synthetic method
   static List access$lambda$5(Validation var0, List var1) {
      return lambda$null$39(var0, var1);
   }

   // $FF: synthetic method
   static List access$lambda$6(Validation var0, List var1) {
      return lambda$null$40(var0, var1);
   }

   public final class FailProjection<E, T> implements Iterable<E> {
      private final Validation<E, T> v;

      private FailProjection(Validation<E, T> v) {
         this.v = v;
      }

      public Validation<E, T> validation() {
         return this.v;
      }

      public E failE(P1<String> err) {
         return this.v.toEither().left().valueE(err);
      }

      public E failE(String err) {
         return this.failE(P.p(err));
      }

      public E orFail(P1<E> e) {
         return this.v.toEither().left().orValue(e);
      }

      public E orFail(E e) {
         return this.orFail(P.p(e));
      }

      public E on(F<T, E> f) {
         return this.v.toEither().left().on(f);
      }

      public Unit foreach(F<E, Unit> f) {
         return this.v.toEither().left().foreach(f);
      }

      public void foreachDoEffect(Effect1<E> f) {
         this.v.toEither().left().foreachDoEffect(f);
      }

      public <A> Validation<A, T> map(F<E, A> f) {
         return Validation.validation(this.v.toEither().left().map(f));
      }

      public <A> Validation<A, T> bind(F<E, Validation<A, T>> f) {
         return this.v.isFail() ? (Validation)f.f(this.v.fail()) : Validation.success(this.v.success());
      }

      public <A> Validation<A, T> sequence(final Validation<A, T> v) {
         return this.bind(new F<E, Validation<A, T>>() {
            public Validation<A, T> f(E e) {
               return v;
            }
         });
      }

      public <A> Option<Validation<E, A>> filter(F<E, Boolean> f) {
         return this.v.toEither().left().filter(f).map(Validation.validation());
      }

      public <A> Validation<A, T> apply(Validation<F<E, A>, T> v) {
         return v.f().bind(new F<F<E, A>, Validation<A, T>>() {
            public Validation<A, T> f(F<E, A> f) {
               return FailProjection.this.map(f);
            }
         });
      }

      public boolean forall(F<E, Boolean> f) {
         return this.v.toEither().left().forall(f);
      }

      public boolean exists(F<E, Boolean> f) {
         return this.v.toEither().left().exists(f);
      }

      public List<E> toList() {
         return this.v.toEither().left().toList();
      }

      public Option<E> toOption() {
         return this.v.toEither().left().toOption();
      }

      public Array<E> toArray() {
         return this.v.toEither().left().toArray();
      }

      public Stream<E> toStream() {
         return this.v.toEither().left().toStream();
      }

      public Iterator<E> iterator() {
         return this.v.toEither().left().iterator();
      }

      // $FF: synthetic method
      FailProjection(Validation x1, Object x2) {
         this(x1);
      }
   }
}
