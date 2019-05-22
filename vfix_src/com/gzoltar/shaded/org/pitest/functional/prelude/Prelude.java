package com.gzoltar.shaded.org.pitest.functional.prelude;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.functional.predicate.And;
import com.gzoltar.shaded.org.pitest.functional.predicate.Not;
import com.gzoltar.shaded.org.pitest.functional.predicate.Or;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class Prelude {
   public static final <A> And<A> and(F<A, Boolean>... ps) {
      return new And(Arrays.asList(ps));
   }

   public static final <A> And<A> and(Iterable<? extends F<A, Boolean>> ps) {
      return new And(ps);
   }

   public static final <A> Not<A> not(F<A, Boolean> p) {
      return new Not(p);
   }

   public static final <A> Or<A> or(Predicate<A>... ps) {
      return new Or(Arrays.asList(ps));
   }

   public static final <A> Or<A> or(Iterable<Predicate<A>> ps) {
      return new Or(ps);
   }

   public static final <A> SideEffect1<A> accumulateTo(final Collection<A> collection) {
      return new SideEffect1<A>() {
         public void apply(A a) {
            collection.add(a);
         }
      };
   }

   public static <A, B> SideEffect1<A> putToMap(final Map<A, B> map, final B value) {
      return new SideEffect1<A>() {
         public void apply(A key) {
            map.put(key, value);
         }
      };
   }

   public static final <A> F<A, A> id() {
      return new F<A, A>() {
         public A apply(A a) {
            return a;
         }
      };
   }

   public static final <A> F<A, A> id(Class<A> type) {
      return id();
   }

   public static final <T> SideEffect1<T> print() {
      return printTo(System.out);
   }

   public static final <T> SideEffect1<T> print(Class<T> type) {
      return print();
   }

   public static final <T> SideEffect1<T> printTo(Class<T> type, PrintStream stream) {
      return printTo(stream);
   }

   public static final <T> SideEffect1<T> printTo(final PrintStream stream) {
      return new SideEffect1<T>() {
         public void apply(T a) {
            stream.print(a);
         }
      };
   }

   public static <T> SideEffect1<T> printWith(final T t) {
      return new SideEffect1<T>() {
         public void apply(T a) {
            System.out.print(t + " : " + a);
         }
      };
   }

   public static <T extends Number> Predicate<T> isGreaterThan(final T value) {
      return new Predicate<T>() {
         public Boolean apply(T o) {
            return o.longValue() > value.longValue();
         }
      };
   }

   public static <T> Predicate<T> isEqualTo(final T value) {
      return new Predicate<T>() {
         public Boolean apply(T o) {
            return o.equals(value);
         }
      };
   }

   public static <T> Predicate<T> isNotNull() {
      return new Predicate<T>() {
         public Boolean apply(T o) {
            return o != null;
         }
      };
   }

   public static <T> Predicate<T> isNull() {
      return new Predicate<T>() {
         public Boolean apply(T o) {
            return o == null;
         }
      };
   }

   public static <T> F<T, Iterable<T>> asList(Class<T> type) {
      return new F<T, Iterable<T>>() {
         public Iterable<T> apply(T a) {
            return Collections.singletonList(a);
         }
      };
   }

   public static <T> SideEffect1<T> noSideEffect(Class<T> clazz) {
      return new SideEffect1<T>() {
         public void apply(T a) {
         }
      };
   }
}
