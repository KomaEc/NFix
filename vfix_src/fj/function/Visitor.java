package fj.function;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Option;

public final class Visitor {
   private Visitor() {
      throw new UnsupportedOperationException();
   }

   public static <X> X findFirst(List<Option<X>> values, P1<X> def) {
      return ((Option)Monoid.firstOptionMonoid().sumLeft(values)).orSome(def);
   }

   public static <X> X nullablefindFirst(List<X> values, P1<X> def) {
      return findFirst(values.map(Option.fromNull()), def);
   }

   public static <A, B> B visitor(List<F<A, Option<B>>> visitors, P1<B> def, A value) {
      return findFirst(visitors.map(Function.apply(value)), def);
   }

   public static <A, B> B nullableVisitor(List<F<A, B>> visitors, P1<B> def, A value) {
      return visitor(visitors.map(new F<F<A, B>, F<A, Option<B>>>() {
         public F<A, Option<B>> f(F<A, B> k) {
            return Function.compose(Option.fromNull(), k);
         }
      }), def, value);
   }

   public static <A, B> F<B, F<A, B>> association(final List<P2<A, B>> x, final Equal<A> eq) {
      return Function.curry(new F2<B, A, B>() {
         public B f(B def, A a) {
            return List.lookup(eq, x, a).orSome(def);
         }
      });
   }

   public static <A, B> F<P1<B>, F<A, B>> associationLazy(final List<P2<A, B>> x, final Equal<A> eq) {
      return Function.curry(new F2<P1<B>, A, B>() {
         public B f(P1<B> def, A a) {
            return List.lookup(eq, x, a).orSome(def);
         }
      });
   }
}
