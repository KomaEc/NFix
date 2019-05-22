package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Ord;
import fj.Ordering;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Enumerator<A> {
   private final F<A, Option<A>> successor;
   private final F<A, Option<A>> predecessor;
   private final Option<A> max;
   private final Option<A> min;
   private final Ord<A> order;
   private final F<A, F<Long, Option<A>>> plus;
   public static final Enumerator<Boolean> booleanEnumerator;
   public static final Enumerator<Byte> byteEnumerator;
   public static final Enumerator<Character> charEnumerator;
   public static final Enumerator<Double> doubleEnumerator;
   public static final Enumerator<Float> floatEnumerator;
   public static final Enumerator<Integer> intEnumerator;
   public static final Enumerator<BigInteger> bigintEnumerator;
   public static final Enumerator<BigDecimal> bigdecimalEnumerator;
   public static final Enumerator<Long> longEnumerator;
   public static final Enumerator<Short> shortEnumerator;
   public static final Enumerator<Ordering> orderingEnumerator;
   public static final Enumerator<Natural> naturalEnumerator;

   private Enumerator(F<A, Option<A>> successor, F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order, F<A, F<Long, Option<A>>> plus) {
      this.successor = successor;
      this.predecessor = predecessor;
      this.max = max;
      this.min = min;
      this.order = order;
      this.plus = plus;
   }

   public F<A, Option<A>> successor() {
      return this.successor;
   }

   public Option<A> successor(A a) {
      return (Option)this.successor.f(a);
   }

   public F<A, Option<A>> predecessor() {
      return this.predecessor;
   }

   public Option<A> predecessor(A a) {
      return (Option)this.predecessor.f(a);
   }

   public Option<A> max() {
      return this.max;
   }

   public Option<A> min() {
      return this.min;
   }

   public F<A, F<Long, Option<A>>> plus() {
      return this.plus;
   }

   public F<Long, Option<A>> plus(A a) {
      return (F)this.plus.f(a);
   }

   public F<A, Option<A>> plus(long l) {
      return (F)Function.flip(this.plus).f(l);
   }

   public Option<A> plus(A a, long l) {
      return (Option)((F)this.plus.f(a)).f(l);
   }

   public Ord<A> order() {
      return this.order;
   }

   public <B> Enumerator<B> xmap(final F<A, B> f, F<B, A> g) {
      F<Option<A>, Option<B>> of = new F<Option<A>, Option<B>>() {
         public Option<B> f(Option<A> o) {
            return o.map(f);
         }
      };
      return enumerator(Function.compose(Function.compose(of, this.successor), g), Function.compose(Function.compose(of, this.predecessor), g), this.max.map(f), this.min.map(f), this.order.comap(g), Function.compose(Function.compose((F)Function.compose().f(of), this.plus), g));
   }

   public Stream<A> toStream(A a) {
      F<A, A> id = Function.identity();
      return Stream.fromFunction(this, id, a);
   }

   public Enumerator<A> setMin(Option<A> min) {
      return enumerator(this.successor, this.predecessor, this.max, min, this.order, this.plus);
   }

   public Enumerator<A> setMax(Option<A> max) {
      return enumerator(this.successor, this.predecessor, max, this.min, this.order, this.plus);
   }

   public static <A> Enumerator<A> enumerator(F<A, Option<A>> successor, F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order, F<A, F<Long, Option<A>>> plus) {
      return new Enumerator(successor, predecessor, max, min, order, plus);
   }

   public static <A> Enumerator<A> enumerator(final F<A, Option<A>> successor, final F<A, Option<A>> predecessor, Option<A> max, Option<A> min, Ord<A> order) {
      return new Enumerator(successor, predecessor, max, min, order, Function.curry(new F2<A, Long, Option<A>>() {
         public Option<A> f(A a, Long l) {
            if (l == 0L) {
               return Option.some(a);
            } else {
               Object aa;
               long x;
               Option s;
               if (l < 0L) {
                  aa = a;

                  for(x = l; x < 0L; ++x) {
                     s = (Option)predecessor.f(aa);
                     if (s.isNone()) {
                        return Option.none();
                     }

                     aa = s.some();
                  }

                  return Option.some(aa);
               } else {
                  aa = a;

                  for(x = l; x > 0L; --x) {
                     s = (Option)successor.f(aa);
                     if (s.isNone()) {
                        return Option.none();
                     }

                     aa = s.some();
                  }

                  return Option.some(aa);
               }
            }
         }
      }));
   }

   static {
      booleanEnumerator = enumerator(new F<Boolean, Option<Boolean>>() {
         public Option<Boolean> f(Boolean b) {
            return b ? Option.none() : Option.some(true);
         }
      }, new F<Boolean, Option<Boolean>>() {
         public Option<Boolean> f(Boolean b) {
            return b ? Option.some(false) : Option.none();
         }
      }, Option.some(true), Option.some(false), Ord.booleanOrd);
      byteEnumerator = enumerator(new F<Byte, Option<Byte>>() {
         public Option<Byte> f(Byte b) {
            return b == 127 ? Option.none() : Option.some((byte)(b + 1));
         }
      }, new F<Byte, Option<Byte>>() {
         public Option<Byte> f(Byte b) {
            return b == -128 ? Option.none() : Option.some((byte)(b - 1));
         }
      }, Option.some((byte)127), Option.some(-128), Ord.byteOrd);
      charEnumerator = enumerator(new F<Character, Option<Character>>() {
         public Option<Character> f(Character c) {
            return c == '\uffff' ? Option.none() : Option.some((char)(c + 1));
         }
      }, new F<Character, Option<Character>>() {
         public Option<Character> f(Character c) {
            return c == 0 ? Option.none() : Option.some((char)(c - 1));
         }
      }, Option.some('\uffff'), Option.some('\u0000'), Ord.charOrd);
      doubleEnumerator = enumerator(new F<Double, Option<Double>>() {
         public Option<Double> f(Double d) {
            return d == Double.MAX_VALUE ? Option.none() : Option.some(d + 1.0D);
         }
      }, new F<Double, Option<Double>>() {
         public Option<Double> f(Double d) {
            return d == Double.MIN_VALUE ? Option.none() : Option.some(d - 1.0D);
         }
      }, Option.some(Double.MAX_VALUE), Option.some(Double.MIN_VALUE), Ord.doubleOrd);
      floatEnumerator = enumerator(new F<Float, Option<Float>>() {
         public Option<Float> f(Float f) {
            return f == Float.MAX_VALUE ? Option.none() : Option.some(f + 1.0F);
         }
      }, new F<Float, Option<Float>>() {
         public Option<Float> f(Float f) {
            return f == Float.MIN_VALUE ? Option.none() : Option.some(f - 1.0F);
         }
      }, Option.some(Float.MAX_VALUE), Option.some(Float.MIN_VALUE), Ord.floatOrd);
      intEnumerator = enumerator(new F<Integer, Option<Integer>>() {
         public Option<Integer> f(Integer i) {
            return i == Integer.MAX_VALUE ? Option.none() : Option.some(i + 1);
         }
      }, new F<Integer, Option<Integer>>() {
         public Option<Integer> f(Integer i) {
            return i == Integer.MIN_VALUE ? Option.none() : Option.some(i - 1);
         }
      }, Option.some(Integer.MAX_VALUE), Option.some(Integer.MIN_VALUE), Ord.intOrd);
      bigintEnumerator = enumerator(new F<BigInteger, Option<BigInteger>>() {
         public Option<BigInteger> f(BigInteger i) {
            return Option.some(i.add(BigInteger.ONE));
         }
      }, new F<BigInteger, Option<BigInteger>>() {
         public Option<BigInteger> f(BigInteger i) {
            return Option.some(i.subtract(BigInteger.ONE));
         }
      }, Option.none(), Option.none(), Ord.bigintOrd, Function.curry(new F2<BigInteger, Long, Option<BigInteger>>() {
         public Option<BigInteger> f(BigInteger i, Long l) {
            return Option.some(i.add(BigInteger.valueOf(l)));
         }
      }));
      bigdecimalEnumerator = enumerator(new F<BigDecimal, Option<BigDecimal>>() {
         public Option<BigDecimal> f(BigDecimal i) {
            return Option.some(i.add(BigDecimal.ONE));
         }
      }, new F<BigDecimal, Option<BigDecimal>>() {
         public Option<BigDecimal> f(BigDecimal i) {
            return Option.some(i.subtract(BigDecimal.ONE));
         }
      }, Option.none(), Option.none(), Ord.bigdecimalOrd, Function.curry(new F2<BigDecimal, Long, Option<BigDecimal>>() {
         public Option<BigDecimal> f(BigDecimal d, Long l) {
            return Option.some(d.add(BigDecimal.valueOf(l)));
         }
      }));
      longEnumerator = enumerator(new F<Long, Option<Long>>() {
         public Option<Long> f(Long i) {
            return i == Long.MAX_VALUE ? Option.none() : Option.some(i + 1L);
         }
      }, new F<Long, Option<Long>>() {
         public Option<Long> f(Long i) {
            return i == Long.MIN_VALUE ? Option.none() : Option.some(i - 1L);
         }
      }, Option.some(Long.MAX_VALUE), Option.some(Long.MIN_VALUE), Ord.longOrd);
      shortEnumerator = enumerator(new F<Short, Option<Short>>() {
         public Option<Short> f(Short i) {
            return i == 32767 ? Option.none() : Option.some((short)(i + 1));
         }
      }, new F<Short, Option<Short>>() {
         public Option<Short> f(Short i) {
            return i == -32768 ? Option.none() : Option.some((short)(i - 1));
         }
      }, Option.some((short)32767), Option.some(-32768), Ord.shortOrd);
      orderingEnumerator = enumerator(new F<Ordering, Option<Ordering>>() {
         public Option<Ordering> f(Ordering o) {
            return o == Ordering.LT ? Option.some(Ordering.EQ) : (o == Ordering.EQ ? Option.some(Ordering.GT) : Option.none());
         }
      }, new F<Ordering, Option<Ordering>>() {
         public Option<Ordering> f(Ordering o) {
            return o == Ordering.GT ? Option.some(Ordering.EQ) : (o == Ordering.EQ ? Option.some(Ordering.LT) : Option.none());
         }
      }, Option.some(Ordering.GT), Option.some(Ordering.LT), Ord.orderingOrd);
      naturalEnumerator = enumerator(new F<Natural, Option<Natural>>() {
         public Option<Natural> f(Natural n) {
            return Option.some(n.succ());
         }
      }, new F<Natural, Option<Natural>>() {
         public Option<Natural> f(Natural n) {
            return n.pred();
         }
      }, Option.none(), Option.some(Natural.ZERO), Ord.naturalOrd, Function.curry(new F2<Natural, Long, Option<Natural>>() {
         public Option<Natural> f(Natural n, Long l) {
            return Option.some(n).apply(Natural.natural(l).map(Function.curry(new F2<Natural, Natural, Natural>() {
               public Natural f(Natural n1, Natural n2) {
                  return n1.add(n2);
               }
            })));
         }
      }));
   }
}
