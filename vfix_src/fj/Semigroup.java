package fj;

import fj.data.Array;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Semigroup<A> {
   private final F<A, F<A, A>> sum;
   public static final Semigroup<Integer> intAdditionSemigroup = semigroup(new F2<Integer, Integer, Integer>() {
      public Integer f(Integer i1, Integer i2) {
         return i1 + i2;
      }
   });
   public static final Semigroup<Double> doubleAdditionSemigroup = semigroup(new F2<Double, Double, Double>() {
      public Double f(Double d1, Double d2) {
         return d1 + d2;
      }
   });
   public static final Semigroup<Integer> intMultiplicationSemigroup = semigroup(new F2<Integer, Integer, Integer>() {
      public Integer f(Integer i1, Integer i2) {
         return i1 * i2;
      }
   });
   public static final Semigroup<Double> doubleMultiplicationSemigroup = semigroup(new F2<Double, Double, Double>() {
      public Double f(Double d1, Double d2) {
         return d1 * d2;
      }
   });
   public static final Semigroup<Integer> intMaximumSemigroup;
   public static final Semigroup<Integer> intMinimumSemigroup;
   public static final Semigroup<BigInteger> bigintAdditionSemigroup;
   public static final Semigroup<BigInteger> bigintMultiplicationSemigroup;
   public static final Semigroup<BigInteger> bigintMaximumSemigroup;
   public static final Semigroup<BigInteger> bigintMinimumSemigroup;
   public static final Semigroup<BigDecimal> bigdecimalAdditionSemigroup;
   public static final Semigroup<BigDecimal> bigdecimalMultiplicationSemigroup;
   public static final Semigroup<BigDecimal> bigDecimalMaximumSemigroup;
   public static final Semigroup<BigDecimal> bigDecimalMinimumSemigroup;
   public static final Semigroup<Natural> naturalMultiplicationSemigroup;
   public static final Semigroup<Natural> naturalAdditionSemigroup;
   public static final Semigroup<Natural> naturalMaximumSemigroup;
   public static final Semigroup<Natural> naturalMinimumSemigroup;
   public static final Semigroup<Long> longAdditionSemigroup;
   public static final Semigroup<Long> longMultiplicationSemigroup;
   public static final Semigroup<Long> longMaximumSemigroup;
   public static final Semigroup<Long> longMinimumSemigroup;
   public static final Semigroup<Boolean> disjunctionSemigroup;
   public static final Semigroup<Boolean> exclusiveDisjunctionSemiGroup;
   public static final Semigroup<Boolean> conjunctionSemigroup;
   public static final Semigroup<String> stringSemigroup;
   public static final Semigroup<StringBuffer> stringBufferSemigroup;
   public static final Semigroup<StringBuilder> stringBuilderSemigroup;
   public static final Semigroup<Unit> unitSemigroup;

   private Semigroup(F<A, F<A, A>> sum) {
      this.sum = sum;
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

   public static <A> Semigroup<A> semigroup(F<A, F<A, A>> sum) {
      return new Semigroup(sum);
   }

   public static <A> Semigroup<A> semigroup(F2<A, A, A> sum) {
      return new Semigroup(Function.curry(sum));
   }

   public static <A, B> Semigroup<F<A, B>> functionSemigroup(final Semigroup<B> sb) {
      return semigroup(new F2<F<A, B>, F<A, B>, F<A, B>>() {
         public F<A, B> f(final F<A, B> a1, final F<A, B> a2) {
            return new F<A, B>() {
               public B f(A a) {
                  return sb.sum(a1.f(a), a2.f(a));
               }
            };
         }
      });
   }

   public static <A> Semigroup<List<A>> listSemigroup() {
      return semigroup(new F2<List<A>, List<A>, List<A>>() {
         public List<A> f(List<A> a1, List<A> a2) {
            return a1.append(a2);
         }
      });
   }

   public static <A> Semigroup<NonEmptyList<A>> nonEmptyListSemigroup() {
      return semigroup(new F2<NonEmptyList<A>, NonEmptyList<A>, NonEmptyList<A>>() {
         public NonEmptyList<A> f(NonEmptyList<A> a1, NonEmptyList<A> a2) {
            return a1.append(a2);
         }
      });
   }

   public static <A> Semigroup<Option<A>> optionSemigroup() {
      return semigroup(new F2<Option<A>, Option<A>, Option<A>>() {
         public Option<A> f(Option<A> a1, Option<A> a2) {
            return a1.isSome() ? a1 : a2;
         }
      });
   }

   public static <A> Semigroup<Option<A>> firstOptionSemigroup() {
      return semigroup(new F2<Option<A>, Option<A>, Option<A>>() {
         public Option<A> f(Option<A> a1, Option<A> a2) {
            return a1.orElse(a2);
         }
      });
   }

   public static <A> Semigroup<Option<A>> lastOptionSemigroup() {
      return semigroup(new F2<Option<A>, Option<A>, Option<A>>() {
         public Option<A> f(Option<A> a1, Option<A> a2) {
            return a2.orElse(a1);
         }
      });
   }

   public static <A> Semigroup<Stream<A>> streamSemigroup() {
      return semigroup(new F2<Stream<A>, Stream<A>, Stream<A>>() {
         public Stream<A> f(Stream<A> a1, Stream<A> a2) {
            return a1.append(a2);
         }
      });
   }

   public static <A> Semigroup<Array<A>> arraySemigroup() {
      return semigroup(new F2<Array<A>, Array<A>, Array<A>>() {
         public Array<A> f(Array<A> a1, Array<A> a2) {
            return a1.append(a2);
         }
      });
   }

   public static <A> Semigroup<P1<A>> p1Semigroup(final Semigroup<A> sa) {
      return semigroup(new F2<P1<A>, P1<A>, P1<A>>() {
         public P1<A> f(final P1<A> a1, final P1<A> a2) {
            return new P1<A>() {
               public A _1() {
                  return sa.sum(a1._1(), a2._1());
               }
            };
         }
      });
   }

   public static <A, B> Semigroup<P2<A, B>> p2Semigroup(final Semigroup<A> sa, final Semigroup<B> sb) {
      return semigroup(new F2<P2<A, B>, P2<A, B>, P2<A, B>>() {
         public P2<A, B> f(final P2<A, B> a1, final P2<A, B> a2) {
            return new P2<A, B>() {
               public A _1() {
                  return sa.sum(a1._1(), a2._1());
               }

               public B _2() {
                  return sb.sum(a1._2(), a2._2());
               }
            };
         }
      });
   }

   public static <A> Semigroup<Set<A>> setSemigroup() {
      return semigroup(new F2<Set<A>, Set<A>, Set<A>>() {
         public Set<A> f(Set<A> a, Set<A> b) {
            return a.union(b);
         }
      });
   }

   static {
      intMaximumSemigroup = semigroup(Ord.intOrd.max);
      intMinimumSemigroup = semigroup(Ord.intOrd.min);
      bigintAdditionSemigroup = semigroup(new F2<BigInteger, BigInteger, BigInteger>() {
         public BigInteger f(BigInteger i1, BigInteger i2) {
            return i1.add(i2);
         }
      });
      bigintMultiplicationSemigroup = semigroup(new F2<BigInteger, BigInteger, BigInteger>() {
         public BigInteger f(BigInteger i1, BigInteger i2) {
            return i1.multiply(i2);
         }
      });
      bigintMaximumSemigroup = semigroup(Ord.bigintOrd.max);
      bigintMinimumSemigroup = semigroup(Ord.bigintOrd.min);
      bigdecimalAdditionSemigroup = semigroup(new F2<BigDecimal, BigDecimal, BigDecimal>() {
         public BigDecimal f(BigDecimal i1, BigDecimal i2) {
            return i1.add(i2);
         }
      });
      bigdecimalMultiplicationSemigroup = semigroup(new F2<BigDecimal, BigDecimal, BigDecimal>() {
         public BigDecimal f(BigDecimal i1, BigDecimal i2) {
            return i1.multiply(i2);
         }
      });
      bigDecimalMaximumSemigroup = semigroup(Ord.bigdecimalOrd.max);
      bigDecimalMinimumSemigroup = semigroup(Ord.bigdecimalOrd.min);
      naturalMultiplicationSemigroup = semigroup(new F2<Natural, Natural, Natural>() {
         public Natural f(Natural n1, Natural n2) {
            return n1.multiply(n2);
         }
      });
      naturalAdditionSemigroup = semigroup(new F2<Natural, Natural, Natural>() {
         public Natural f(Natural n1, Natural n2) {
            return n1.add(n2);
         }
      });
      naturalMaximumSemigroup = semigroup(Ord.naturalOrd.max);
      naturalMinimumSemigroup = semigroup(Ord.naturalOrd.min);
      longAdditionSemigroup = semigroup(new F2<Long, Long, Long>() {
         public Long f(Long x, Long y) {
            return x + y;
         }
      });
      longMultiplicationSemigroup = semigroup(new F2<Long, Long, Long>() {
         public Long f(Long x, Long y) {
            return x * y;
         }
      });
      longMaximumSemigroup = semigroup(Ord.longOrd.max);
      longMinimumSemigroup = semigroup(Ord.longOrd.min);
      disjunctionSemigroup = semigroup(new F2<Boolean, Boolean, Boolean>() {
         public Boolean f(Boolean b1, Boolean b2) {
            return b1 || b2;
         }
      });
      exclusiveDisjunctionSemiGroup = semigroup(new F2<Boolean, Boolean, Boolean>() {
         public Boolean f(Boolean p, Boolean q) {
            return p && !q || !p && q;
         }
      });
      conjunctionSemigroup = semigroup(new F2<Boolean, Boolean, Boolean>() {
         public Boolean f(Boolean b1, Boolean b2) {
            return b1 && b2;
         }
      });
      stringSemigroup = semigroup(new F2<String, String, String>() {
         public String f(String s1, String s2) {
            return s1 + s2;
         }
      });
      stringBufferSemigroup = semigroup(new F2<StringBuffer, StringBuffer, StringBuffer>() {
         public StringBuffer f(StringBuffer s1, StringBuffer s2) {
            return (new StringBuffer(s1)).append(s2);
         }
      });
      stringBuilderSemigroup = semigroup(new F2<StringBuilder, StringBuilder, StringBuilder>() {
         public StringBuilder f(StringBuilder s1, StringBuilder s2) {
            return (new StringBuilder(s1)).append(s2);
         }
      });
      unitSemigroup = semigroup(new F2<Unit, Unit, Unit>() {
         public Unit f(Unit u1, Unit u2) {
            return Unit.unit();
         }
      });
   }
}
