package fj.test;

import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.F6;
import fj.F7;
import fj.F8;
import fj.Function;
import fj.LcgRng;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import fj.data.State;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public abstract class Coarbitrary<A> {
   public static final Coarbitrary<Boolean> coarbBoolean = new Coarbitrary<Boolean>() {
      public <B> Gen<B> coarbitrary(Boolean b, Gen<B> g) {
         return Variant.variant(b ? 0L : 1L, g);
      }
   };
   public static final Coarbitrary<Integer> coarbInteger = new Coarbitrary<Integer>() {
      public <B> Gen<B> coarbitrary(Integer i, Gen<B> g) {
         return Variant.variant(i >= 0 ? (long)(2 * i) : (long)(-2 * i + 1), g);
      }
   };
   public static final Coarbitrary<Byte> coarbByte = new Coarbitrary<Byte>() {
      public <B> Gen<B> coarbitrary(Byte b, Gen<B> g) {
         return Variant.variant(b >= 0 ? (long)(2 * b) : (long)(-2 * b + 1), g);
      }
   };
   public static final Coarbitrary<Short> coarbShort = new Coarbitrary<Short>() {
      public <B> Gen<B> coarbitrary(Short s, Gen<B> g) {
         return Variant.variant(s >= 0 ? (long)(2 * s) : (long)(-2 * s + 1), g);
      }
   };
   public static final Coarbitrary<Long> coarbLong = new Coarbitrary<Long>() {
      public <B> Gen<B> coarbitrary(Long l, Gen<B> g) {
         return Variant.variant(l >= 0L ? 2L * l : -2L * l + 1L, g);
      }
   };
   public static final Coarbitrary<Character> coarbCharacter = new Coarbitrary<Character>() {
      public <B> Gen<B> coarbitrary(Character c, Gen<B> g) {
         return Variant.variant((long)(c << 1), g);
      }
   };
   public static final Coarbitrary<Float> coarbFloat = new Coarbitrary<Float>() {
      public <B> Gen<B> coarbitrary(Float f, Gen<B> g) {
         return coarbInteger.coarbitrary(Float.floatToRawIntBits(f), g);
      }
   };
   public static final Coarbitrary<Double> coarbDouble = new Coarbitrary<Double>() {
      public <B> Gen<B> coarbitrary(Double d, Gen<B> g) {
         return coarbLong.coarbitrary(Double.doubleToRawLongBits(d), g);
      }
   };
   public static final Coarbitrary<String> coarbString = new Coarbitrary<String>() {
      public <B> Gen<B> coarbitrary(String s, Gen<B> g) {
         return coarbList(coarbCharacter).coarbitrary(List.fromString(s), g);
      }
   };
   public static final Coarbitrary<StringBuffer> coarbStringBuffer = new Coarbitrary<StringBuffer>() {
      public <B> Gen<B> coarbitrary(StringBuffer s, Gen<B> g) {
         return coarbString.coarbitrary(s.toString(), g);
      }
   };
   public static final Coarbitrary<StringBuilder> coarbStringBuilder = new Coarbitrary<StringBuilder>() {
      public <B> Gen<B> coarbitrary(StringBuilder s, Gen<B> g) {
         return coarbString.coarbitrary(s.toString(), g);
      }
   };
   public static final Coarbitrary<Throwable> coarbThrowable;
   public static final Coarbitrary<BitSet> coarbBitSet;
   public static final Coarbitrary<Calendar> coarbCalendar;
   public static final Coarbitrary<Date> coarbDate;
   public static final Coarbitrary<GregorianCalendar> coarbGregorianCalendar;
   public static final Coarbitrary<Properties> coarbProperties;
   public static final Coarbitrary<java.sql.Date> coarbSQLDate;
   public static final Coarbitrary<Timestamp> coarbTimestamp;
   public static final Coarbitrary<Time> coarbTime;
   public static final Coarbitrary<BigInteger> coarbBigInteger;
   public static final Coarbitrary<BigDecimal> coarbBigDecimal;

   public abstract <B> Gen<B> coarbitrary(A var1, Gen<B> var2);

   public final <B> F<Gen<B>, Gen<B>> coarbitrary(final A a) {
      return new F<Gen<B>, Gen<B>>() {
         public Gen<B> f(Gen<B> g) {
            return Coarbitrary.this.coarbitrary(a, g);
         }
      };
   }

   public final <B> Coarbitrary<B> compose(final F<B, A> f) {
      return new Coarbitrary<B>() {
         public <X> Gen<X> coarbitrary(B b, Gen<X> g) {
            return Coarbitrary.this.coarbitrary(f.f(b), g);
         }
      };
   }

   public final <B> Coarbitrary<B> comap(final F<B, A> f) {
      return new Coarbitrary<B>() {
         public <X> Gen<X> coarbitrary(B b, Gen<X> g) {
            return Coarbitrary.this.coarbitrary(f.f(b), g);
         }
      };
   }

   public static <A, B> Coarbitrary<F<A, B>> coarbF(final Arbitrary<A> a, final Coarbitrary<B> c) {
      return new Coarbitrary<F<A, B>>() {
         public <X> Gen<X> coarbitrary(final F<A, B> f, final Gen<X> g) {
            return a.gen.bind(new F<A, Gen<X>>() {
               public Gen<X> f(A ax) {
                  return c.coarbitrary(f.f(ax), g);
               }
            });
         }
      };
   }

   public static <A, B, C> Coarbitrary<F2<A, B, C>> coarbF2(final Arbitrary<A> aa, final Arbitrary<B> ab, final Coarbitrary<C> c) {
      return new Coarbitrary<F2<A, B, C>>() {
         public <X> Gen<X> coarbitrary(F2<A, B, C> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, c)).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D> Coarbitrary<F3<A, B, C, D>> coarbF3(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Coarbitrary<D> c) {
      return new Coarbitrary<F3<A, B, C, D>>() {
         public <X> Gen<X> coarbitrary(F3<A, B, C, D> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, c))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D, E> Coarbitrary<F4<A, B, C, D, E>> coarbF4(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Coarbitrary<E> c) {
      return new Coarbitrary<F4<A, B, C, D, E>>() {
         public <X> Gen<X> coarbitrary(F4<A, B, C, D, E> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, c)))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D, E, F$> Coarbitrary<F5<A, B, C, D, E, F$>> coarbF5(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Coarbitrary<F$> c) {
      return new Coarbitrary<F5<A, B, C, D, E, F$>>() {
         public <X> Gen<X> coarbitrary(F5<A, B, C, D, E, F$> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, c))))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D, E, F$, G> Coarbitrary<F6<A, B, C, D, E, F$, G>> coarbF6(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Coarbitrary<G> c) {
      return new Coarbitrary<F6<A, B, C, D, E, F$, G>>() {
         public <X> Gen<X> coarbitrary(F6<A, B, C, D, E, F$, G> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, c)))))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> Coarbitrary<F7<A, B, C, D, E, F$, G, H>> coarbF7(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Arbitrary<G> ag, final Coarbitrary<H> c) {
      return new Coarbitrary<F7<A, B, C, D, E, F$, G, H>>() {
         public <X> Gen<X> coarbitrary(F7<A, B, C, D, E, F$, G, H> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, coarbF(ag, c))))))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H, I> Coarbitrary<F8<A, B, C, D, E, F$, G, H, I>> coarbF8(final Arbitrary<A> aa, final Arbitrary<B> ab, final Arbitrary<C> ac, final Arbitrary<D> ad, final Arbitrary<E> ae, final Arbitrary<F$> af, final Arbitrary<G> ag, final Arbitrary<H> ah, final Coarbitrary<I> c) {
      return new Coarbitrary<F8<A, B, C, D, E, F$, G, H, I>>() {
         public <X> Gen<X> coarbitrary(F8<A, B, C, D, E, F$, G, H, I> f, Gen<X> g) {
            return coarbF(aa, coarbF(ab, coarbF(ac, coarbF(ad, coarbF(ae, coarbF(af, coarbF(ag, coarbF(ah, c)))))))).coarbitrary(Function.curry(f), g);
         }
      };
   }

   public static <A> Coarbitrary<Option<A>> coarbOption(final Coarbitrary<A> ca) {
      return new Coarbitrary<Option<A>>() {
         public <B> Gen<B> coarbitrary(Option<A> o, Gen<B> g) {
            return o.isNone() ? Variant.variant(0L, g) : Variant.variant(1L, ca.coarbitrary(o.some(), g));
         }
      };
   }

   public static <A, B> Coarbitrary<Either<A, B>> coarbEither(final Coarbitrary<A> ca, final Coarbitrary<B> cb) {
      return new Coarbitrary<Either<A, B>>() {
         public <X> Gen<X> coarbitrary(Either<A, B> e, Gen<X> g) {
            return e.isLeft() ? Variant.variant(0L, ca.coarbitrary(e.left().value(), g)) : Variant.variant(1L, cb.coarbitrary(e.right().value(), g));
         }
      };
   }

   public static <A> Coarbitrary<List<A>> coarbList(final Coarbitrary<A> ca) {
      return new Coarbitrary<List<A>>() {
         public <B> Gen<B> coarbitrary(List<A> as, Gen<B> g) {
            return as.isEmpty() ? Variant.variant(0L, g) : Variant.variant(1L, ca.coarbitrary(as.head(), this.coarbitrary(as.tail(), g)));
         }
      };
   }

   public static <A> Coarbitrary<Stream<A>> coarbStream(final Coarbitrary<A> ca) {
      return new Coarbitrary<Stream<A>>() {
         public <B> Gen<B> coarbitrary(Stream<A> as, Gen<B> g) {
            return as.isEmpty() ? Variant.variant(0L, g) : Variant.variant(1L, ca.coarbitrary(as.head(), this.coarbitrary((Stream)as.tail()._1(), g)));
         }
      };
   }

   public static Coarbitrary<LcgRng> coarbLcgRng() {
      return new Coarbitrary<LcgRng>() {
         public <B> Gen<B> coarbitrary(LcgRng rng, Gen<B> g) {
            long i = rng.getSeed();
            return Variant.variant(i >= 0L ? 2L * i : -2L * i + 1L, g);
         }
      };
   }

   public static <S, A> Coarbitrary<State<S, A>> coarbState(final Arbitrary<S> as, final F2<S, A, Long> f) {
      return new Coarbitrary<State<S, A>>() {
         public <B> Gen<B> coarbitrary(State<S, A> s1, Gen<B> g) {
            return as.gen.bind(Coarbitrary$28$$Lambda$1.lambdaFactory$(s1, f, g));
         }

         // $FF: synthetic method
         private static Gen lambda$coarbitrary$160(State var0, F2 var1, Gen var2, Object r) {
            P2<S, A> p = var0.run(r);
            return Variant.variant((Long)var1.f(p._1(), p._2()), var2);
         }

         // $FF: synthetic method
         static Gen access$lambda$0(State var0, F2 var1, Gen var2, Object var3) {
            return lambda$coarbitrary$160(var0, var1, var2, var3);
         }
      };
   }

   public static <A> Coarbitrary<Array<A>> coarbArray(final Coarbitrary<A> ca) {
      return new Coarbitrary<Array<A>>() {
         public <B> Gen<B> coarbitrary(Array<A> as, Gen<B> g) {
            return coarbList(ca).coarbitrary(as.toList(), g);
         }
      };
   }

   public static Coarbitrary<Throwable> coarbThrowable(Coarbitrary<String> cs) {
      return cs.comap(new F<Throwable, String>() {
         public String f(Throwable t) {
            return t.getMessage();
         }
      });
   }

   public static <A> Coarbitrary<ArrayList<A>> coarbArrayList(final Coarbitrary<A> ca) {
      return new Coarbitrary<ArrayList<A>>() {
         public <B> Gen<B> coarbitrary(ArrayList<A> as, Gen<B> g) {
            return coarbArray(ca).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <K extends Enum<K>, V> Coarbitrary<EnumMap<K, V>> coarbEnumMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<EnumMap<K, V>>() {
         public <B> Gen<B> coarbitrary(EnumMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A extends Enum<A>> Coarbitrary<EnumSet<A>> coarbEnumSet(final Coarbitrary<A> c) {
      return new Coarbitrary<EnumSet<A>>() {
         public <B> Gen<B> coarbitrary(EnumSet<A> as, Gen<B> g) {
            return coarbHashSet(c).coarbitrary(new HashSet(as), g);
         }
      };
   }

   public static <K, V> Coarbitrary<HashMap<K, V>> coarbHashMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<HashMap<K, V>>() {
         public <B> Gen<B> coarbitrary(HashMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A> Coarbitrary<HashSet<A>> coarbHashSet(final Coarbitrary<A> c) {
      return new Coarbitrary<HashSet<A>>() {
         public <B> Gen<B> coarbitrary(HashSet<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <K, V> Coarbitrary<Hashtable<K, V>> coarbHashtable(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<Hashtable<K, V>>() {
         public <B> Gen<B> coarbitrary(Hashtable<K, V> h, Gen<B> g) {
            List<P2<K, V>> x = List.nil();

            Object k;
            for(Iterator var4 = h.keySet().iterator(); var4.hasNext(); x = x.snoc(P.p(k, h.get(k)))) {
               k = var4.next();
            }

            return coarbList(coarbP2(ck, cv)).coarbitrary(x, g);
         }
      };
   }

   public static <K, V> Coarbitrary<IdentityHashMap<K, V>> coarbIdentityHashMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<IdentityHashMap<K, V>>() {
         public <B> Gen<B> coarbitrary(IdentityHashMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <K, V> Coarbitrary<LinkedHashMap<K, V>> coarbLinkedHashMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<LinkedHashMap<K, V>>() {
         public <B> Gen<B> coarbitrary(LinkedHashMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A> Coarbitrary<LinkedHashSet<A>> coarbLinkedHashSet(final Coarbitrary<A> c) {
      return new Coarbitrary<LinkedHashSet<A>>() {
         public <B> Gen<B> coarbitrary(LinkedHashSet<A> as, Gen<B> g) {
            return coarbHashSet(c).coarbitrary(new HashSet(as), g);
         }
      };
   }

   public static <A> Coarbitrary<LinkedList<A>> coarbLinkedList(final Coarbitrary<A> c) {
      return new Coarbitrary<LinkedList<A>>() {
         public <B> Gen<B> coarbitrary(LinkedList<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<PriorityQueue<A>> coarbPriorityQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<PriorityQueue<A>>() {
         public <B> Gen<B> coarbitrary(PriorityQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<Stack<A>> coarbStack(final Coarbitrary<A> c) {
      return new Coarbitrary<Stack<A>>() {
         public <B> Gen<B> coarbitrary(Stack<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <K, V> Coarbitrary<TreeMap<K, V>> coarbTreeMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<TreeMap<K, V>>() {
         public <B> Gen<B> coarbitrary(TreeMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A> Coarbitrary<TreeSet<A>> coarbTreeSet(final Coarbitrary<A> c) {
      return new Coarbitrary<TreeSet<A>>() {
         public <B> Gen<B> coarbitrary(TreeSet<A> as, Gen<B> g) {
            return coarbHashSet(c).coarbitrary(new HashSet(as), g);
         }
      };
   }

   public static <A> Coarbitrary<Vector<A>> coarbVector(final Coarbitrary<A> c) {
      return new Coarbitrary<Vector<A>>() {
         public <B> Gen<B> coarbitrary(Vector<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <K, V> Coarbitrary<WeakHashMap<K, V>> coarbWeakHashMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<WeakHashMap<K, V>>() {
         public <B> Gen<B> coarbitrary(WeakHashMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A> Coarbitrary<ArrayBlockingQueue<A>> coarbArrayBlockingQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<ArrayBlockingQueue<A>>() {
         public <B> Gen<B> coarbitrary(ArrayBlockingQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <K, V> Coarbitrary<ConcurrentHashMap<K, V>> coarbConcurrentHashMap(final Coarbitrary<K> ck, final Coarbitrary<V> cv) {
      return new Coarbitrary<ConcurrentHashMap<K, V>>() {
         public <B> Gen<B> coarbitrary(ConcurrentHashMap<K, V> m, Gen<B> g) {
            return coarbHashtable(ck, cv).coarbitrary(new Hashtable(m), g);
         }
      };
   }

   public static <A> Coarbitrary<ConcurrentLinkedQueue<A>> coarbConcurrentLinkedQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<ConcurrentLinkedQueue<A>>() {
         public <B> Gen<B> coarbitrary(ConcurrentLinkedQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<CopyOnWriteArrayList<A>> coarbCopyOnWriteArrayList(final Coarbitrary<A> c) {
      return new Coarbitrary<CopyOnWriteArrayList<A>>() {
         public <B> Gen<B> coarbitrary(CopyOnWriteArrayList<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<CopyOnWriteArraySet<A>> coarbCopyOnWriteArraySet(final Coarbitrary<A> c) {
      return new Coarbitrary<CopyOnWriteArraySet<A>>() {
         public <B> Gen<B> coarbitrary(CopyOnWriteArraySet<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A extends Delayed> Coarbitrary<DelayQueue<A>> coarbDelayQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<DelayQueue<A>>() {
         public <B> Gen<B> coarbitrary(DelayQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Delayed[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<LinkedBlockingQueue<A>> coarbLinkedBlockingQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<LinkedBlockingQueue<A>>() {
         public <B> Gen<B> coarbitrary(LinkedBlockingQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<PriorityBlockingQueue<A>> coarbPriorityBlockingQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<PriorityBlockingQueue<A>>() {
         public <B> Gen<B> coarbitrary(PriorityBlockingQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<SynchronousQueue<A>> coarbSynchronousQueue(final Coarbitrary<A> c) {
      return new Coarbitrary<SynchronousQueue<A>>() {
         public <B> Gen<B> coarbitrary(SynchronousQueue<A> as, Gen<B> g) {
            return coarbArray(c).coarbitrary(Array.array(as.toArray((Object[])(new Object[as.size()]))), g);
         }
      };
   }

   public static <A> Coarbitrary<P1<A>> coarbP1(final Coarbitrary<A> ca) {
      return new Coarbitrary<P1<A>>() {
         public <B> Gen<B> coarbitrary(P1<A> p, Gen<B> g) {
            return ca.coarbitrary(p._1(), g);
         }
      };
   }

   public static <A, B> Coarbitrary<P2<A, B>> coarbP2(final Coarbitrary<A> ca, final Coarbitrary<B> cb) {
      return new Coarbitrary<P2<A, B>>() {
         public <X> Gen<X> coarbitrary(P2<A, B> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), g));
         }
      };
   }

   public static <A, B, C> Coarbitrary<P3<A, B, C>> coarbP3(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc) {
      return new Coarbitrary<P3<A, B, C>>() {
         public <X> Gen<X> coarbitrary(P3<A, B, C> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), g)));
         }
      };
   }

   public static <A, B, C, D> Coarbitrary<P4<A, B, C, D>> coarbP4(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd) {
      return new Coarbitrary<P4<A, B, C, D>>() {
         public <X> Gen<X> coarbitrary(P4<A, B, C, D> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), g))));
         }
      };
   }

   public static <A, B, C, D, E> Coarbitrary<P5<A, B, C, D, E>> coarbP5(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce) {
      return new Coarbitrary<P5<A, B, C, D, E>>() {
         public <X> Gen<X> coarbitrary(P5<A, B, C, D, E> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), g)))));
         }
      };
   }

   public static <A, B, C, D, E, F$> Coarbitrary<P6<A, B, C, D, E, F$>> coarbP6(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf) {
      return new Coarbitrary<P6<A, B, C, D, E, F$>>() {
         public <X> Gen<X> coarbitrary(P6<A, B, C, D, E, F$> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), g))))));
         }
      };
   }

   public static <A, B, C, D, E, F$, G> Coarbitrary<P7<A, B, C, D, E, F$, G>> coarbP7(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf, final Coarbitrary<G> cg) {
      return new Coarbitrary<P7<A, B, C, D, E, F$, G>>() {
         public <X> Gen<X> coarbitrary(P7<A, B, C, D, E, F$, G> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), cg.coarbitrary(p._7(), g)))))));
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> Coarbitrary<P8<A, B, C, D, E, F$, G, H>> coarbP8(final Coarbitrary<A> ca, final Coarbitrary<B> cb, final Coarbitrary<C> cc, final Coarbitrary<D> cd, final Coarbitrary<E> ce, final Coarbitrary<F$> cf, final Coarbitrary<G> cg, final Coarbitrary<H> ch) {
      return new Coarbitrary<P8<A, B, C, D, E, F$, G, H>>() {
         public <X> Gen<X> coarbitrary(P8<A, B, C, D, E, F$, G, H> p, Gen<X> g) {
            return ca.coarbitrary(p._1(), cb.coarbitrary(p._2(), cc.coarbitrary(p._3(), cd.coarbitrary(p._4(), ce.coarbitrary(p._5(), cf.coarbitrary(p._6(), cg.coarbitrary(p._7(), ch.coarbitrary(p._8(), g))))))));
         }
      };
   }

   static {
      coarbThrowable = coarbThrowable(coarbString);
      coarbBitSet = new Coarbitrary<BitSet>() {
         public <B> Gen<B> coarbitrary(BitSet s, Gen<B> g) {
            List<Boolean> x = List.nil();

            for(int i = 0; i < s.size(); ++i) {
               x = x.snoc(s.get(i));
            }

            return coarbList(coarbBoolean).coarbitrary(x, g);
         }
      };
      coarbCalendar = new Coarbitrary<Calendar>() {
         public <B> Gen<B> coarbitrary(Calendar c, Gen<B> g) {
            return coarbLong.coarbitrary(c.getTime().getTime(), g);
         }
      };
      coarbDate = new Coarbitrary<Date>() {
         public <B> Gen<B> coarbitrary(Date d, Gen<B> g) {
            return coarbLong.coarbitrary(d.getTime(), g);
         }
      };
      coarbGregorianCalendar = new Coarbitrary<GregorianCalendar>() {
         public <B> Gen<B> coarbitrary(GregorianCalendar c, Gen<B> g) {
            return coarbLong.coarbitrary(c.getTime().getTime(), g);
         }
      };
      coarbProperties = new Coarbitrary<Properties>() {
         public <B> Gen<B> coarbitrary(Properties p, Gen<B> g) {
            Hashtable<String, String> t = new Hashtable();
            Iterator var4 = p.keySet().iterator();

            while(var4.hasNext()) {
               Object s = var4.next();
               t.put((String)s, p.getProperty((String)s));
            }

            return coarbHashtable(coarbString, coarbString).coarbitrary(t, g);
         }
      };
      coarbSQLDate = new Coarbitrary<java.sql.Date>() {
         public <B> Gen<B> coarbitrary(java.sql.Date d, Gen<B> g) {
            return coarbLong.coarbitrary(d.getTime(), g);
         }
      };
      coarbTimestamp = new Coarbitrary<Timestamp>() {
         public <B> Gen<B> coarbitrary(Timestamp t, Gen<B> g) {
            return coarbLong.coarbitrary(t.getTime(), g);
         }
      };
      coarbTime = new Coarbitrary<Time>() {
         public <B> Gen<B> coarbitrary(Time t, Gen<B> g) {
            return coarbLong.coarbitrary(t.getTime(), g);
         }
      };
      coarbBigInteger = new Coarbitrary<BigInteger>() {
         public <B> Gen<B> coarbitrary(BigInteger i, Gen<B> g) {
            return Variant.variant((i.compareTo(BigInteger.ZERO) >= 0 ? i.multiply(BigInteger.valueOf(2L)) : i.multiply(BigInteger.valueOf(-2L).add(BigInteger.ONE))).longValue(), g);
         }
      };
      coarbBigDecimal = new Coarbitrary<BigDecimal>() {
         public <B> Gen<B> coarbitrary(BigDecimal d, Gen<B> g) {
            return Variant.variant((d.compareTo(BigDecimal.ZERO) >= 0 ? d.multiply(BigDecimal.valueOf(2L)) : d.multiply(BigDecimal.valueOf(-2L).add(BigDecimal.ONE))).longValue(), g);
         }
      };
   }
}
