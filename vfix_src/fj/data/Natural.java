package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.data.vector.V;
import fj.data.vector.V2;
import java.math.BigInteger;

public final class Natural extends Number {
   private final BigInteger value;
   private static final long serialVersionUID = -588673650944359682L;
   public static final F<BigInteger, Option<Natural>> fromBigInt = new F<BigInteger, Option<Natural>>() {
      public Option<Natural> f(BigInteger i) {
         return Natural.natural(i);
      }
   };
   public static final Natural ZERO = (Natural)natural(0L).some();
   public static final Natural ONE = (Natural)natural(1L).some();
   public static final F<Natural, F<Natural, Natural>> add = Function.curry(new F2<Natural, Natural, Natural>() {
      public Natural f(Natural n1, Natural n2) {
         return n1.add(n2);
      }
   });
   public static final F<Natural, F<Natural, Option<Natural>>> subtract = Function.curry(new F2<Natural, Natural, Option<Natural>>() {
      public Option<Natural> f(Natural o, Natural o1) {
         return o1.subtract(o);
      }
   });
   public static final F<Natural, F<Natural, Natural>> multiply = Function.curry(new F2<Natural, Natural, Natural>() {
      public Natural f(Natural n1, Natural n2) {
         return n1.multiply(n2);
      }
   });
   public static final F<Natural, F<Natural, Natural>> divide = Function.curry(new F2<Natural, Natural, Natural>() {
      public Natural f(Natural n1, Natural n2) {
         return n2.divide(n1);
      }
   });
   public static final F<Natural, F<Natural, Natural>> mod = Function.curry(new F2<Natural, Natural, Natural>() {
      public Natural f(Natural n1, Natural n2) {
         return n2.mod(n1);
      }
   });
   public static final F<Natural, F<Natural, V2<Natural>>> divmod = Function.curry(new F2<Natural, Natural, V2<Natural>>() {
      public V2<Natural> f(Natural n1, Natural n2) {
         return n2.divmod(n1);
      }
   });
   public static final F<Natural, BigInteger> bigIntegerValue = new F<Natural, BigInteger>() {
      public BigInteger f(Natural n) {
         return n.bigIntegerValue();
      }
   };

   private Natural(BigInteger i) {
      if (i.compareTo(BigInteger.ZERO) < 0) {
         throw Bottom.error("Natural less than zero");
      } else {
         this.value = i;
      }
   }

   public static Option<Natural> natural(BigInteger i) {
      return i.compareTo(BigInteger.ZERO) < 0 ? Option.none() : Option.some(new Natural(i));
   }

   public static Option<Natural> natural(long i) {
      return natural(BigInteger.valueOf(i));
   }

   public Natural succ() {
      return this.add(ONE);
   }

   public static F<Natural, Natural> succ_() {
      return new F<Natural, Natural>() {
         public Natural f(Natural natural) {
            return natural.succ();
         }
      };
   }

   public Option<Natural> pred() {
      return this.subtract(ONE);
   }

   public static F<Natural, Option<Natural>> pred_() {
      return new F<Natural, Option<Natural>>() {
         public Option<Natural> f(Natural natural) {
            return natural.pred();
         }
      };
   }

   public Natural add(Natural n) {
      return (Natural)natural(n.value.add(this.value)).some();
   }

   public Option<Natural> subtract(Natural n) {
      return natural(n.value.subtract(this.value));
   }

   public Natural multiply(Natural n) {
      return (Natural)natural(n.value.multiply(this.value)).some();
   }

   public Natural divide(Natural n) {
      return (Natural)natural(this.value.divide(n.value)).some();
   }

   public Natural mod(Natural n) {
      return (Natural)natural(this.value.mod(n.value)).some();
   }

   public V2<Natural> divmod(Natural n) {
      BigInteger[] x = this.value.divideAndRemainder(n.value);
      return V.v(natural(x[0]).some(), natural(x[1]).some());
   }

   public BigInteger bigIntegerValue() {
      return this.value;
   }

   public long longValue() {
      return this.value.longValue();
   }

   public float floatValue() {
      return this.value.floatValue();
   }

   public double doubleValue() {
      return this.value.doubleValue();
   }

   public int intValue() {
      return this.value.intValue();
   }

   public static Natural sum(Stream<Natural> ns) {
      return (Natural)Monoid.naturalAdditionMonoid.sumLeft(ns);
   }

   public static Natural product(Stream<Natural> ns) {
      return (Natural)Monoid.naturalMultiplicationMonoid.sumLeft(ns);
   }

   public static Natural sum(List<Natural> ns) {
      return (Natural)Monoid.naturalAdditionMonoid.sumLeft(ns);
   }

   public static Natural product(List<Natural> ns) {
      return (Natural)Monoid.naturalMultiplicationMonoid.sumLeft(ns);
   }
}
