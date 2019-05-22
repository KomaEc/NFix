package fj.data;

import fj.Bottom;
import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;

public abstract class Either<A, B> {
   private Either() {
   }

   public final Either<A, B>.LeftProjection<A, B> left() {
      return new Either.LeftProjection(this);
   }

   public final Either<A, B>.RightProjection<A, B> right() {
      return new Either.RightProjection(this);
   }

   public abstract boolean isLeft();

   public abstract boolean isRight();

   public final <X> X either(F<A, X> left, F<B, X> right) {
      return this.isLeft() ? left.f(this.left().value()) : right.f(this.right().value());
   }

   public final Either<B, A> swap() {
      return (Either)(this.isLeft() ? new Either.Right(((Either.Left)this).a) : new Either.Left(((Either.Right)this).b));
   }

   public static <A, B> Either<A, B> left(A a) {
      return new Either.Left(a);
   }

   public static <A, B> F<A, Either<A, B>> left_() {
      return new F<A, Either<A, B>>() {
         public Either<A, B> f(A a) {
            return Either.left(a);
         }
      };
   }

   public static <A, B> F<B, Either<A, B>> right_() {
      return new F<B, Either<A, B>>() {
         public Either<A, B> f(B b) {
            return Either.right(b);
         }
      };
   }

   public static <A, B> Either<A, B> right(B b) {
      return new Either.Right(b);
   }

   public static <A, B, X> F<F<A, X>, F<Either<A, B>, Either<X, B>>> leftMap_() {
      return new F<F<A, X>, F<Either<A, B>, Either<X, B>>>() {
         public F<Either<A, B>, Either<X, B>> f(final F<A, X> axf) {
            return new F<Either<A, B>, Either<X, B>>() {
               public Either<X, B> f(Either<A, B> e) {
                  return e.left().map(axf);
               }
            };
         }
      };
   }

   public static <A, B, X> F<F<B, X>, F<Either<A, B>, Either<A, X>>> rightMap_() {
      return new F<F<B, X>, F<Either<A, B>, Either<A, X>>>() {
         public F<Either<A, B>, Either<A, X>> f(final F<B, X> axf) {
            return new F<Either<A, B>, Either<A, X>>() {
               public Either<A, X> f(Either<A, B> e) {
                  return e.right().map(axf);
               }
            };
         }
      };
   }

   public static <A, B> Either<A, B> joinLeft(Either<Either<A, B>, B> e) {
      F<Either<A, B>, Either<A, B>> id = Function.identity();
      return e.left().bind(id);
   }

   public static <A, B> Either<A, B> joinRight(Either<A, Either<A, B>> e) {
      F<Either<A, B>, Either<A, B>> id = Function.identity();
      return e.right().bind(id);
   }

   public static <A, X> Either<List<A>, X> sequenceLeft(final List<Either<A, X>> a) {
      return a.isEmpty() ? left(List.nil()) : ((Either)a.head()).left().bind(new F<A, Either<List<A>, X>>() {
         public Either<List<A>, X> f(A aa) {
            return Either.sequenceLeft(a.tail()).left().map(List.cons_(aa));
         }
      });
   }

   public static <B, X> Either<X, List<B>> sequenceRight(final List<Either<X, B>> a) {
      return a.isEmpty() ? right(List.nil()) : ((Either)a.head()).right().bind(new F<B, Either<X, List<B>>>() {
         public Either<X, List<B>> f(B bb) {
            return Either.sequenceRight(a.tail()).right().map(List.cons_(bb));
         }
      });
   }

   public static <A> A reduce(Either<A, A> e) {
      return e.isLeft() ? e.left().value() : e.right().value();
   }

   public static <A, B> Either<A, B> iif(boolean c, P1<B> right, P1<A> left) {
      return (Either)(c ? new Either.Right(right._1()) : new Either.Left(left._1()));
   }

   public static <A, B> List<A> lefts(List<Either<A, B>> es) {
      return (List)es.foldRight((F)(new F<Either<A, B>, F<List<A>, List<A>>>() {
         public F<List<A>, List<A>> f(final Either<A, B> e) {
            return new F<List<A>, List<A>>() {
               public List<A> f(List<A> as) {
                  return e.isLeft() ? as.cons(e.left().value()) : as;
               }
            };
         }
      }), List.nil());
   }

   public static <A, B> List<B> rights(List<Either<A, B>> es) {
      return (List)es.foldRight((F)(new F<Either<A, B>, F<List<B>, List<B>>>() {
         public F<List<B>, List<B>> f(final Either<A, B> e) {
            return new F<List<B>, List<B>>() {
               public List<B> f(List<B> bs) {
                  return e.isRight() ? bs.cons(e.right().value()) : bs;
               }
            };
         }
      }), List.nil());
   }

   public String toString() {
      return Show.eitherShow(Show.anyShow(), Show.anyShow()).showS((Object)this);
   }

   // $FF: synthetic method
   Either(Object x0) {
      this();
   }

   public final class RightProjection<A, B> implements Iterable<B> {
      private final Either<A, B> e;

      private RightProjection(Either<A, B> e) {
         this.e = e;
      }

      public Iterator<B> iterator() {
         return this.toCollection().iterator();
      }

      public Either<A, B> either() {
         return this.e;
      }

      public B valueE(P1<String> err) {
         if (this.e.isRight()) {
            return ((Either.Right)this.e).b;
         } else {
            throw Bottom.error((String)err._1());
         }
      }

      public B value() {
         return this.valueE(P.p("right.value on Left"));
      }

      public B orValue(P1<B> b) {
         return Either.this.isRight() ? this.value() : b._1();
      }

      public B on(F<A, B> f) {
         return Either.this.isRight() ? this.value() : f.f(this.e.left().value());
      }

      public Unit foreach(F<B, Unit> f) {
         if (Either.this.isRight()) {
            f.f(this.value());
         }

         return Unit.unit();
      }

      public void foreachDoEffect(Effect1<B> f) {
         if (Either.this.isRight()) {
            f.f(this.value());
         }

      }

      public <X> Either<A, X> map(F<B, X> f) {
         return (Either)(Either.this.isRight() ? new Either.Right(f.f(this.value())) : new Either.Left(this.e.left().value()));
      }

      public <X> Either<A, X> bind(F<B, Either<A, X>> f) {
         return (Either)(Either.this.isRight() ? (Either)f.f(this.value()) : new Either.Left(this.e.left().value()));
      }

      public <X> Either<A, X> sequence(Either<A, X> e) {
         return this.bind(Function.constant(e));
      }

      public <X> Option<Either<X, B>> filter(F<B, Boolean> f) {
         return Either.this.isRight() ? ((Boolean)f.f(this.value()) ? Option.some(new Either.Right(this.value())) : Option.none()) : Option.none();
      }

      public <X> Either<A, X> apply(Either<A, F<B, X>> e) {
         return e.right().bind(new F<F<B, X>, Either<A, X>>() {
            public Either<A, X> f(F<B, X> f) {
               return RightProjection.this.map(f);
            }
         });
      }

      public boolean forall(F<B, Boolean> f) {
         return Either.this.isLeft() || (Boolean)f.f(this.value());
      }

      public boolean exists(F<B, Boolean> f) {
         return Either.this.isRight() && (Boolean)f.f(this.value());
      }

      public List<B> toList() {
         return Either.this.isRight() ? List.single(this.value()) : List.nil();
      }

      public Option<B> toOption() {
         return Either.this.isRight() ? Option.some(this.value()) : Option.none();
      }

      public Array<B> toArray() {
         if (Either.this.isRight()) {
            Object[] a = new Object[]{this.value()};
            return Array.mkArray(a);
         } else {
            return Array.empty();
         }
      }

      public Stream<B> toStream() {
         return Either.this.isRight() ? Stream.single(this.value()) : Stream.nil();
      }

      public Collection<B> toCollection() {
         return this.toList().toCollection();
      }

      // $FF: synthetic method
      RightProjection(Either x1, Object x2) {
         this(x1);
      }
   }

   public final class LeftProjection<A, B> implements Iterable<A> {
      private final Either<A, B> e;

      private LeftProjection(Either<A, B> e) {
         this.e = e;
      }

      public Iterator<A> iterator() {
         return this.toCollection().iterator();
      }

      public Either<A, B> either() {
         return this.e;
      }

      public A valueE(P1<String> err) {
         if (this.e.isLeft()) {
            return ((Either.Left)this.e).a;
         } else {
            throw Bottom.error((String)err._1());
         }
      }

      public A valueE(String err) {
         return this.valueE(P.p(err));
      }

      public A value() {
         return this.valueE(P.p("left.value on Right"));
      }

      public A orValue(P1<A> a) {
         return Either.this.isLeft() ? this.value() : a._1();
      }

      public A orValue(A a) {
         return Either.this.isLeft() ? this.value() : a;
      }

      public A on(F<B, A> f) {
         return Either.this.isLeft() ? this.value() : f.f(this.e.right().value());
      }

      public Unit foreach(F<A, Unit> f) {
         if (Either.this.isLeft()) {
            f.f(this.value());
         }

         return Unit.unit();
      }

      public void foreachDoEffect(Effect1<A> f) {
         if (Either.this.isLeft()) {
            f.f(this.value());
         }

      }

      public <X> Either<X, B> map(F<A, X> f) {
         return (Either)(Either.this.isLeft() ? new Either.Left(f.f(this.value())) : new Either.Right(this.e.right().value()));
      }

      public <X> Either<X, B> bind(F<A, Either<X, B>> f) {
         return (Either)(Either.this.isLeft() ? (Either)f.f(this.value()) : new Either.Right(this.e.right().value()));
      }

      public <X> Either<X, B> sequence(Either<X, B> e) {
         return this.bind(Function.constant(e));
      }

      public <X> Option<Either<A, X>> filter(F<A, Boolean> f) {
         return Either.this.isLeft() ? ((Boolean)f.f(this.value()) ? Option.some(new Either.Left(this.value())) : Option.none()) : Option.none();
      }

      public <X> Either<X, B> apply(Either<F<A, X>, B> e) {
         return e.left().bind(new F<F<A, X>, Either<X, B>>() {
            public Either<X, B> f(F<A, X> f) {
               return LeftProjection.this.map(f);
            }
         });
      }

      public boolean forall(F<A, Boolean> f) {
         return Either.this.isRight() || (Boolean)f.f(this.value());
      }

      public boolean exists(F<A, Boolean> f) {
         return Either.this.isLeft() && (Boolean)f.f(this.value());
      }

      public List<A> toList() {
         return Either.this.isLeft() ? List.single(this.value()) : List.nil();
      }

      public Option<A> toOption() {
         return Either.this.isLeft() ? Option.some(this.value()) : Option.none();
      }

      public Array<A> toArray() {
         if (Either.this.isLeft()) {
            Object[] a = new Object[]{this.value()};
            return Array.mkArray(a);
         } else {
            return Array.mkArray(new Object[0]);
         }
      }

      public Stream<A> toStream() {
         return Either.this.isLeft() ? Stream.single(this.value()) : Stream.nil();
      }

      public Collection<A> toCollection() {
         return this.toList().toCollection();
      }

      // $FF: synthetic method
      LeftProjection(Either x1, Object x2) {
         this(x1);
      }
   }

   private static final class Right<A, B> extends Either<A, B> {
      private final B b;

      Right(B b) {
         super(null);
         this.b = b;
      }

      public boolean isLeft() {
         return false;
      }

      public boolean isRight() {
         return true;
      }
   }

   private static final class Left<A, B> extends Either<A, B> {
      private final A a;

      Left(A a) {
         super(null);
         this.a = a;
      }

      public boolean isLeft() {
         return true;
      }

      public boolean isRight() {
         return false;
      }
   }
}
