package fj;

import fj.data.Array;
import fj.data.List;
import fj.data.Natural;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Monoid<A> {
   private final F<A, F<A, A>> sum;
   private final A zero;
   public static final Monoid<Integer> intAdditionMonoid;
   public static final Monoid<Integer> intMultiplicationMonoid;
   public static final Monoid<Double> doubleAdditionMonoid;
   public static final Monoid<Double> doubleMultiplicationMonoid;
   public static final Monoid<BigInteger> bigintAdditionMonoid;
   public static final Monoid<BigInteger> bigintMultiplicationMonoid;
   public static final Monoid<BigDecimal> bigdecimalAdditionMonoid;
   public static final Monoid<BigDecimal> bigdecimalMultiplicationMonoid;
   public static final Monoid<Natural> naturalAdditionMonoid;
   public static final Monoid<Natural> naturalMultiplicationMonoid;
   public static final Monoid<Long> longAdditionMonoid;
   public static final Monoid<Long> longMultiplicationMonoid;
   public static final Monoid<Boolean> disjunctionMonoid;
   public static final Monoid<Boolean> exclusiveDisjunctionMonoid;
   public static final Monoid<Boolean> conjunctionMonoid;
   public static final Monoid<String> stringMonoid;
   public static final Monoid<StringBuffer> stringBufferMonoid;
   public static final Monoid<StringBuilder> stringBuilderMonoid;

   private Monoid(F<A, F<A, A>> sum, A zero) {
      this.sum = sum;
      this.zero = zero;
   }

   public Semigroup<A> semigroup() {
      return Semigroup.semigroup(this.sum);
   }

   public A sum(A a1, A a2) {
      return ((F)this.sum.f(a1)).f(a2);
   }

   public F<A, A> sum(A a1) {
      return (F)this.sum.f(a1);
   }

   public F<A, F<A, A>> sum() {
      return this.sum;
   }

   public A zero() {
      return this.zero;
   }

   public A sumRight(List<A> as) {
      return as.foldRight(this.sum, this.zero);
   }

   public A sumRight(Stream<A> as) {
      return as.foldRight(new F2<A, P1<A>, A>() {
         public A f(A a, P1<A> ap1) {
            return Monoid.this.sum(a, ap1._1());
         }
      }, this.zero);
   }

   public A sumLeft(List<A> as) {
      return as.foldLeft(this.sum, this.zero);
   }

   public A sumLeft(Stream<A> as) {
      return as.foldLeft(this.sum, this.zero);
   }

   public F<List<A>, A> sumLeft() {
      return new F<List<A>, A>() {
         public A f(List<A> as) {
            return Monoid.this.sumLeft(as);
         }
      };
   }

   public F<List<A>, A> sumRight() {
      return new F<List<A>, A>() {
         public A f(List<A> as) {
            return Monoid.this.sumRight(as);
         }
      };
   }

   public F<Stream<A>, A> sumLeftS() {
      return new F<Stream<A>, A>() {
         public A f(Stream<A> as) {
            return Monoid.this.sumLeft(as);
         }
      };
   }

   public A join(Iterable<A> as, A a) {
      Stream<A> s = Stream.iterableStream(as);
      return s.isEmpty() ? this.zero : s.foldLeft1(Function.compose(this.sum, (F)Function.flip(this.sum).f(a)));
   }

   public static <A> Monoid<A> monoid(F<A, F<A, A>> sum, A zero) {
      return new Monoid(sum, zero);
   }

   public static <A> Monoid<A> monoid(F2<A, A, A> sum, A zero) {
      return new Monoid(Function.curry(sum), zero);
   }

   public static <A> Monoid<A> monoid(Semigroup<A> s, A zero) {
      return new Monoid(s.sum(), zero);
   }

   public static <A, B> Monoid<F<A, B>> functionMonoid(Monoid<B> mb) {
      return monoid((Semigroup)Semigroup.functionSemigroup(mb.semigroup()), Function.constant(mb.zero));
   }

   public static <A> Monoid<List<A>> listMonoid() {
      return monoid((Semigroup)Semigroup.listSemigroup(), List.nil());
   }

   public static <A> Monoid<Option<A>> optionMonoid() {
      return monoid((Semigroup)Semigroup.optionSemigroup(), Option.none());
   }

   public static <A> Monoid<Option<A>> firstOptionMonoid() {
      return monoid((Semigroup)Semigroup.firstOptionSemigroup(), Option.none());
   }

   public static <A> Monoid<Option<A>> lastOptionMonoid() {
      return monoid((Semigroup)Semigroup.lastOptionSemigroup(), Option.none());
   }

   public static <A> Monoid<Stream<A>> streamMonoid() {
      return monoid((Semigroup)Semigroup.streamSemigroup(), Stream.nil());
   }

   public static <A> Monoid<Array<A>> arrayMonoid() {
      return monoid((Semigroup)Semigroup.arraySemigroup(), Array.empty());
   }

   public static <A> Monoid<Set<A>> setMonoid(Ord<A> o) {
      return monoid((Semigroup)Semigroup.setSemigroup(), Set.empty(o));
   }

   static {
      intAdditionMonoid = monoid((Semigroup)Semigroup.intAdditionSemigroup, 0);
      intMultiplicationMonoid = monoid((Semigroup)Semigroup.intMultiplicationSemigroup, 1);
      doubleAdditionMonoid = monoid((Semigroup)Semigroup.doubleAdditionSemigroup, 0.0D);
      doubleMultiplicationMonoid = monoid((Semigroup)Semigroup.doubleMultiplicationSemigroup, 1.0D);
      bigintAdditionMonoid = monoid((Semigroup)Semigroup.bigintAdditionSemigroup, BigInteger.ZERO);
      bigintMultiplicationMonoid = monoid((Semigroup)Semigroup.bigintMultiplicationSemigroup, BigInteger.ONE);
      bigdecimalAdditionMonoid = monoid((Semigroup)Semigroup.bigdecimalAdditionSemigroup, BigDecimal.ZERO);
      bigdecimalMultiplicationMonoid = monoid((Semigroup)Semigroup.bigdecimalMultiplicationSemigroup, BigDecimal.ONE);
      naturalAdditionMonoid = monoid((Semigroup)Semigroup.naturalAdditionSemigroup, Natural.ZERO);
      naturalMultiplicationMonoid = monoid((Semigroup)Semigroup.naturalMultiplicationSemigroup, Natural.ONE);
      longAdditionMonoid = monoid((Semigroup)Semigroup.longAdditionSemigroup, 0L);
      longMultiplicationMonoid = monoid((Semigroup)Semigroup.longMultiplicationSemigroup, 1L);
      disjunctionMonoid = monoid((Semigroup)Semigroup.disjunctionSemigroup, false);
      exclusiveDisjunctionMonoid = monoid((Semigroup)Semigroup.exclusiveDisjunctionSemiGroup, false);
      conjunctionMonoid = monoid((Semigroup)Semigroup.conjunctionSemigroup, true);
      stringMonoid = monoid((Semigroup)Semigroup.stringSemigroup, "");
      stringBufferMonoid = monoid((Semigroup)Semigroup.stringBufferSemigroup, new StringBuffer());
      stringBuilderMonoid = monoid((Semigroup)Semigroup.stringBuilderSemigroup, new StringBuilder());
   }
}
