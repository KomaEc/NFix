package fj.function;

import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Stream;

public final class Booleans {
   public static final F<Boolean, F<Boolean, Boolean>> or;
   public static final F<Boolean, F<Boolean, Boolean>> and;
   public static final F<Boolean, F<Boolean, Boolean>> xor;
   public static final F<Boolean, Boolean> not;
   public static final F<Boolean, F<Boolean, Boolean>> implies;
   public static final F<Boolean, F<Boolean, Boolean>> if_;
   public static final F<Boolean, F<Boolean, Boolean>> iff;
   public static final F<Boolean, F<Boolean, Boolean>> nimp;
   public static final F<Boolean, F<Boolean, Boolean>> nif;
   public static final F<Boolean, F<Boolean, Boolean>> nor;

   private Booleans() {
      throw new UnsupportedOperationException();
   }

   public static boolean and(List<Boolean> l) {
      return (Boolean)Monoid.conjunctionMonoid.sumLeft(l);
   }

   public static boolean and(Stream<Boolean> l) {
      return (Boolean)Monoid.conjunctionMonoid.sumLeft(l);
   }

   public static boolean or(List<Boolean> l) {
      return (Boolean)Monoid.disjunctionMonoid.sumLeft(l);
   }

   public static boolean or(Stream<Boolean> l) {
      return (Boolean)Monoid.disjunctionMonoid.sumLeft(l);
   }

   public static <A> F<A, Boolean> not(F<A, Boolean> p) {
      return Function.compose(not, p);
   }

   public static <A> F<Boolean, F<A, F<A, A>>> cond() {
      return Function.curry(new F3<Boolean, A, A, A>() {
         public A f(Boolean p, A a1, A a2) {
            return p ? a1 : a2;
         }
      });
   }

   static {
      or = Semigroup.disjunctionSemigroup.sum();
      and = Semigroup.conjunctionSemigroup.sum();
      xor = Semigroup.exclusiveDisjunctionSemiGroup.sum();
      not = new F<Boolean, Boolean>() {
         public Boolean f(Boolean p) {
            return !p;
         }
      };
      implies = Function.curry(new F2<Boolean, Boolean, Boolean>() {
         public Boolean f(Boolean p, Boolean q) {
            return !p || q;
         }
      });
      if_ = Function.flip(implies);
      iff = Function.compose2(not, xor);
      nimp = Function.compose2(not, implies);
      nif = Function.compose2(not, if_);
      nor = Function.compose2(not, or);
   }
}
