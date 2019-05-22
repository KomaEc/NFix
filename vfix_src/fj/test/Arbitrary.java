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
import fj.data.Enumerator;
import fj.data.List;
import fj.data.Option;
import fj.data.Reader;
import fj.data.State;
import fj.data.Stream;
import fj.function.Effect1;
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
import java.util.Locale;
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

public final class Arbitrary<A> {
   public final Gen<A> gen;
   public static final Arbitrary<Boolean> arbBoolean = arbitrary(Gen.elements(true, false));
   public static final Arbitrary<Integer> arbInteger = arbitrary(Gen.sized(new F<Integer, Gen<Integer>>() {
      public Gen<Integer> f(Integer i) {
         return Gen.choose(-i, i);
      }
   }));
   public static final Arbitrary<Integer> arbIntegerBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Integer>>() {
      public Gen<Integer> f(Integer i) {
         return Gen.frequency(List.list(P.p(1, Gen.value(0)), P.p(1, Gen.value(1)), P.p(1, Gen.value(-1)), P.p(1, Gen.value(Integer.MAX_VALUE)), P.p(1, Gen.value(Integer.MIN_VALUE)), P.p(1, Gen.value(2147483646)), P.p(1, Gen.value(-2147483647)), P.p(93, Arbitrary.arbInteger.gen)));
      }
   }));
   public static final Arbitrary<Long> arbLong;
   public static final Arbitrary<Long> arbLongBoundaries;
   public static final Arbitrary<Byte> arbByte;
   public static final Arbitrary<Byte> arbByteBoundaries;
   public static final Arbitrary<Short> arbShort;
   public static final Arbitrary<Short> arbShortBoundaries;
   public static final Arbitrary<Character> arbCharacter;
   public static final Arbitrary<Character> arbCharacterBoundaries;
   public static final Arbitrary<Double> arbDouble;
   public static final Arbitrary<Double> arbDoubleBoundaries;
   public static final Arbitrary<Float> arbFloat;
   public static final Arbitrary<Float> arbFloatBoundaries;
   public static final Arbitrary<String> arbString;
   public static final Arbitrary<String> arbUSASCIIString;
   public static final Arbitrary<String> arbAlphaNumString;
   public static final Arbitrary<StringBuffer> arbStringBuffer;
   public static final Arbitrary<StringBuilder> arbStringBuilder;
   public static final Arbitrary<Throwable> arbThrowable;
   public static final Arbitrary<BitSet> arbBitSet;
   public static final Arbitrary<Calendar> arbCalendar;
   public static final Arbitrary<Date> arbDate;
   public static final Arbitrary<GregorianCalendar> arbGregorianCalendar;
   public static final Arbitrary<Properties> arbProperties;
   public static final Arbitrary<java.sql.Date> arbSQLDate;
   public static final Arbitrary<Time> arbTime;
   public static final Arbitrary<Timestamp> arbTimestamp;
   public static final Arbitrary<BigInteger> arbBigInteger;
   public static final Arbitrary<BigDecimal> arbBigDecimal;
   public static final Arbitrary<Locale> arbLocale;

   private Arbitrary(Gen<A> gen) {
      this.gen = gen;
   }

   public static <A> Arbitrary<A> arbitrary(Gen<A> g) {
      return new Arbitrary(g);
   }

   public static <A, B> Arbitrary<F<A, B>> arbF(final Coarbitrary<A> c, final Arbitrary<B> a) {
      return arbitrary(Gen.promote(new F<A, Gen<B>>() {
         public Gen<B> f(A x) {
            return c.coarbitrary(x, a.gen);
         }
      }));
   }

   public static <A, B> Arbitrary<Reader<A, B>> arbReader(Coarbitrary<A> aa, Arbitrary<B> ab) {
      return arbitrary(arbF(aa, ab).gen.map(Arbitrary$$Lambda$1.lambdaFactory$()));
   }

   public static <S, A> Arbitrary<State<S, A>> arbState(Arbitrary<S> as, Coarbitrary<S> cs, Arbitrary<A> aa) {
      return arbitrary(arbF(cs, arbP2(as, aa)).gen.map(Arbitrary$$Lambda$2.lambdaFactory$()));
   }

   public static <A> Arbitrary<LcgRng> arbLcgRng() {
      return arbitrary(arbLong.gen.map(Arbitrary$$Lambda$3.lambdaFactory$()));
   }

   public static <A, B> Arbitrary<F<A, B>> arbFInvariant(Arbitrary<B> a) {
      return arbitrary(a.gen.map(Function.constant()));
   }

   public static <A, B, C> Arbitrary<F2<A, B, C>> arbF2(Coarbitrary<A> ca, Coarbitrary<B> cb, Arbitrary<C> a) {
      return arbitrary(arbF(ca, arbF(cb, a)).gen.map(Function.uncurryF2()));
   }

   public static <A, B, C> Arbitrary<F2<A, B, C>> arbF2Invariant(Arbitrary<C> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF2(), Function.compose(Function.constant(), Function.constant()))));
   }

   public static <A, B, C, D> Arbitrary<F3<A, B, C, D>> arbF3(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Arbitrary<D> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, a))).gen.map(Function.uncurryF3()));
   }

   public static <A, B, C, D> Arbitrary<F3<A, B, C, D>> arbF3Invariant(Arbitrary<D> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF3(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))));
   }

   public static <A, B, C, D, E> Arbitrary<F4<A, B, C, D, E>> arbF4(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Arbitrary<E> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, a)))).gen.map(Function.uncurryF4()));
   }

   public static <A, B, C, D, E> Arbitrary<F4<A, B, C, D, E>> arbF4Invariant(Arbitrary<E> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF4(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))));
   }

   public static <A, B, C, D, E, F$> Arbitrary<F5<A, B, C, D, E, F$>> arbF5(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Arbitrary<F$> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, a))))).gen.map(Function.uncurryF5()));
   }

   public static <A, B, C, D, E, F$> Arbitrary<F5<A, B, C, D, E, F$>> arbF5Invariant(Arbitrary<F$> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF5(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))))));
   }

   public static <A, B, C, D, E, F$, G> Arbitrary<F6<A, B, C, D, E, F$, G>> arbF6(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Arbitrary<G> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, a)))))).gen.map(Function.uncurryF6()));
   }

   public static <A, B, C, D, E, F$, G> Arbitrary<F6<A, B, C, D, E, F$, G>> arbF6Invariant(Arbitrary<G> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF6(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))))));
   }

   public static <A, B, C, D, E, F$, G, H> Arbitrary<F7<A, B, C, D, E, F$, G, H>> arbF7(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Coarbitrary<G> cg, Arbitrary<H> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, arbF(cg, a))))))).gen.map(Function.uncurryF7()));
   }

   public static <A, B, C, D, E, F$, G, H> Arbitrary<F7<A, B, C, D, E, F$, G, H>> arbF7Invariant(Arbitrary<H> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF7(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant())))))))));
   }

   public static <A, B, C, D, E, F$, G, H, I> Arbitrary<F8<A, B, C, D, E, F$, G, H, I>> arbF8(Coarbitrary<A> ca, Coarbitrary<B> cb, Coarbitrary<C> cc, Coarbitrary<D> cd, Coarbitrary<E> ce, Coarbitrary<F$> cf, Coarbitrary<G> cg, Coarbitrary<H> ch, Arbitrary<I> a) {
      return arbitrary(arbF(ca, arbF(cb, arbF(cc, arbF(cd, arbF(ce, arbF(cf, arbF(cg, arbF(ch, a)))))))).gen.map(Function.uncurryF8()));
   }

   public static <A, B, C, D, E, F$, G, H, I> Arbitrary<F8<A, B, C, D, E, F$, G, H, I>> arbF8Invariant(Arbitrary<I> a) {
      return arbitrary(a.gen.map(Function.compose(Function.uncurryF8(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.compose(Function.constant(), Function.constant()))))))))));
   }

   public static <A> Arbitrary<Gen<A>> arbGen(final Arbitrary<A> aa) {
      return arbitrary(Gen.sized(new F<Integer, Gen<Gen<A>>>() {
         public Gen<Gen<A>> f(Integer i) {
            return i == 0 ? Gen.fail() : aa.gen.map(new F<A, Gen<A>>() {
               public Gen<A> f(A a) {
                  return Gen.value(a);
               }
            }).resize(i - 1);
         }
      }));
   }

   public static <A> Arbitrary<Option<A>> arbOption(final Arbitrary<A> aa) {
      return arbitrary(Gen.sized(new F<Integer, Gen<Option<A>>>() {
         public Gen<Option<A>> f(Integer i) {
            return i == 0 ? Gen.value(Option.none()) : aa.gen.map(new F<A, Option<A>>() {
               public Option<A> f(A a) {
                  return Option.some(a);
               }
            }).resize(i - 1);
         }
      }));
   }

   public static <A, B> Arbitrary<Either<A, B>> arbEither(Arbitrary<A> aa, Arbitrary<B> ab) {
      Gen<Either<A, B>> left = aa.gen.map(new F<A, Either<A, B>>() {
         public Either<A, B> f(A a) {
            return Either.left(a);
         }
      });
      Gen<Either<A, B>> right = ab.gen.map(new F<B, Either<A, B>>() {
         public Either<A, B> f(B b) {
            return Either.right(b);
         }
      });
      return arbitrary(Gen.oneOf(List.list(left, right)));
   }

   public static <A> Arbitrary<List<A>> arbList(Arbitrary<A> aa) {
      return arbitrary(Gen.listOf(aa.gen));
   }

   public static <A> Arbitrary<Stream<A>> arbStream(Arbitrary<A> aa) {
      return arbitrary(arbList(aa).gen.map(new F<List<A>, Stream<A>>() {
         public Stream<A> f(List<A> as) {
            return as.toStream();
         }
      }));
   }

   public static <A> Arbitrary<Array<A>> arbArray(Arbitrary<A> aa) {
      return arbitrary(arbList(aa).gen.map(new F<List<A>, Array<A>>() {
         public Array<A> f(List<A> as) {
            return as.toArray();
         }
      }));
   }

   public static Arbitrary<Throwable> arbThrowable(Arbitrary<String> as) {
      return arbitrary(as.gen.map(new F<String, Throwable>() {
         public Throwable f(String msg) {
            return new Throwable(msg);
         }
      }));
   }

   public static <A> Arbitrary<ArrayList<A>> arbArrayList(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, ArrayList<A>>() {
         public ArrayList<A> f(Array<A> a) {
            return new ArrayList(a.toCollection());
         }
      }));
   }

   public static <A extends Enum<A>> Arbitrary<A> arbEnumValue(Class<A> clazz) {
      return arbitrary(Gen.elements(clazz.getEnumConstants()));
   }

   public static <K extends Enum<K>, V> Arbitrary<EnumMap<K, V>> arbEnumMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, EnumMap<K, V>>() {
         public EnumMap<K, V> f(Hashtable<K, V> ht) {
            return new EnumMap(ht);
         }
      }));
   }

   public static <A extends Enum<A>> Arbitrary<EnumSet<A>> arbEnumSet(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, EnumSet<A>>() {
         public EnumSet<A> f(Array<A> a) {
            return EnumSet.copyOf(a.toCollection());
         }
      }));
   }

   public static <K, V> Arbitrary<HashMap<K, V>> arbHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, HashMap<K, V>>() {
         public HashMap<K, V> f(Hashtable<K, V> ht) {
            return new HashMap(ht);
         }
      }));
   }

   public static <A> Arbitrary<HashSet<A>> arbHashSet(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, HashSet<A>>() {
         public HashSet<A> f(Array<A> a) {
            return new HashSet(a.toCollection());
         }
      }));
   }

   public static <K, V> Arbitrary<Hashtable<K, V>> arbHashtable(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbList(ak).gen.bind(arbList(av).gen, new F<List<K>, F<List<V>, Hashtable<K, V>>>() {
         public F<List<V>, Hashtable<K, V>> f(final List<K> ks) {
            return new F<List<V>, Hashtable<K, V>>() {
               public Hashtable<K, V> f(List<V> vs) {
                  final Hashtable<K, V> t = new Hashtable();
                  ks.zip(vs).foreachDoEffect(new Effect1<P2<K, V>>() {
                     public void f(P2<K, V> kv) {
                        t.put(kv._1(), kv._2());
                     }
                  });
                  return t;
               }
            };
         }
      }));
   }

   public static <K, V> Arbitrary<IdentityHashMap<K, V>> arbIdentityHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, IdentityHashMap<K, V>>() {
         public IdentityHashMap<K, V> f(Hashtable<K, V> ht) {
            return new IdentityHashMap(ht);
         }
      }));
   }

   public static <K, V> Arbitrary<LinkedHashMap<K, V>> arbLinkedHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, LinkedHashMap<K, V>>() {
         public LinkedHashMap<K, V> f(Hashtable<K, V> ht) {
            return new LinkedHashMap(ht);
         }
      }));
   }

   public static <A> Arbitrary<LinkedHashSet<A>> arbLinkedHashSet(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, LinkedHashSet<A>>() {
         public LinkedHashSet<A> f(Array<A> a) {
            return new LinkedHashSet(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<LinkedList<A>> arbLinkedList(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, LinkedList<A>>() {
         public LinkedList<A> f(Array<A> a) {
            return new LinkedList(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<PriorityQueue<A>> arbPriorityQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, PriorityQueue<A>>() {
         public PriorityQueue<A> f(Array<A> a) {
            return new PriorityQueue(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<Stack<A>> arbStack(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, Stack<A>>() {
         public Stack<A> f(Array<A> a) {
            Stack<A> s = new Stack();
            s.addAll(a.toCollection());
            return s;
         }
      }));
   }

   public static <K, V> Arbitrary<TreeMap<K, V>> arbTreeMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, TreeMap<K, V>>() {
         public TreeMap<K, V> f(Hashtable<K, V> ht) {
            return new TreeMap(ht);
         }
      }));
   }

   public static <A> Arbitrary<TreeSet<A>> arbTreeSet(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, TreeSet<A>>() {
         public TreeSet<A> f(Array<A> a) {
            return new TreeSet(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<Vector<A>> arbVector(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, Vector<A>>() {
         public Vector<A> f(Array<A> a) {
            return new Vector(a.toCollection());
         }
      }));
   }

   public static <K, V> Arbitrary<WeakHashMap<K, V>> arbWeakHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, WeakHashMap<K, V>>() {
         public WeakHashMap<K, V> f(Hashtable<K, V> ht) {
            return new WeakHashMap(ht);
         }
      }));
   }

   public static <A> Arbitrary<ArrayBlockingQueue<A>> arbArrayBlockingQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.bind(arbInteger.gen, arbBoolean.gen, new F<Array<A>, F<Integer, F<Boolean, ArrayBlockingQueue<A>>>>() {
         public F<Integer, F<Boolean, ArrayBlockingQueue<A>>> f(final Array<A> a) {
            return new F<Integer, F<Boolean, ArrayBlockingQueue<A>>>() {
               public F<Boolean, ArrayBlockingQueue<A>> f(final Integer capacity) {
                  return new F<Boolean, ArrayBlockingQueue<A>>() {
                     public ArrayBlockingQueue<A> f(Boolean fair) {
                        return new ArrayBlockingQueue(a.length() + Math.abs(capacity), fair, a.toCollection());
                     }
                  };
               }
            };
         }
      }));
   }

   public static <K, V> Arbitrary<ConcurrentHashMap<K, V>> arbConcurrentHashMap(Arbitrary<K> ak, Arbitrary<V> av) {
      return arbitrary(arbHashtable(ak, av).gen.map(new F<Hashtable<K, V>, ConcurrentHashMap<K, V>>() {
         public ConcurrentHashMap<K, V> f(Hashtable<K, V> ht) {
            return new ConcurrentHashMap(ht);
         }
      }));
   }

   public static <A> Arbitrary<ConcurrentLinkedQueue<A>> arbConcurrentLinkedQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, ConcurrentLinkedQueue<A>>() {
         public ConcurrentLinkedQueue<A> f(Array<A> a) {
            return new ConcurrentLinkedQueue(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<CopyOnWriteArrayList<A>> arbCopyOnWriteArrayList(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, CopyOnWriteArrayList<A>>() {
         public CopyOnWriteArrayList<A> f(Array<A> a) {
            return new CopyOnWriteArrayList(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<CopyOnWriteArraySet<A>> arbCopyOnWriteArraySet(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, CopyOnWriteArraySet<A>>() {
         public CopyOnWriteArraySet<A> f(Array<A> a) {
            return new CopyOnWriteArraySet(a.toCollection());
         }
      }));
   }

   public static <A extends Delayed> Arbitrary<DelayQueue<A>> arbDelayQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, DelayQueue<A>>() {
         public DelayQueue<A> f(Array<A> a) {
            return new DelayQueue(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<LinkedBlockingQueue<A>> arbLinkedBlockingQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, LinkedBlockingQueue<A>>() {
         public LinkedBlockingQueue<A> f(Array<A> a) {
            return new LinkedBlockingQueue(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<PriorityBlockingQueue<A>> arbPriorityBlockingQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.map(new F<Array<A>, PriorityBlockingQueue<A>>() {
         public PriorityBlockingQueue<A> f(Array<A> a) {
            return new PriorityBlockingQueue(a.toCollection());
         }
      }));
   }

   public static <A> Arbitrary<SynchronousQueue<A>> arbSynchronousQueue(Arbitrary<A> aa) {
      return arbitrary(arbArray(aa).gen.bind(arbBoolean.gen, new F<Array<A>, F<Boolean, SynchronousQueue<A>>>() {
         public F<Boolean, SynchronousQueue<A>> f(final Array<A> a) {
            return new F<Boolean, SynchronousQueue<A>>() {
               public SynchronousQueue<A> f(Boolean fair) {
                  SynchronousQueue<A> q = new SynchronousQueue(fair);
                  q.addAll(a.toCollection());
                  return q;
               }
            };
         }
      }));
   }

   public static <A> Arbitrary<P1<A>> arbP1(Arbitrary<A> aa) {
      return arbitrary(aa.gen.map(new F<A, P1<A>>() {
         public P1<A> f(A a) {
            return P.p(a);
         }
      }));
   }

   public static <A, B> Arbitrary<P2<A, B>> arbP2(Arbitrary<A> aa, Arbitrary<B> ab) {
      return arbitrary(aa.gen.bind(ab.gen, new F<A, F<B, P2<A, B>>>() {
         public F<B, P2<A, B>> f(final A a) {
            return new F<B, P2<A, B>>() {
               public P2<A, B> f(B b) {
                  return P.p(a, b);
               }
            };
         }
      }));
   }

   public static <A, B, C> Arbitrary<P3<A, B, C>> arbP3(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, new F<A, F<B, F<C, P3<A, B, C>>>>() {
         public F<B, F<C, P3<A, B, C>>> f(final A a) {
            return new F<B, F<C, P3<A, B, C>>>() {
               public F<C, P3<A, B, C>> f(final B b) {
                  return new F<C, P3<A, B, C>>() {
                     public P3<A, B, C> f(C c) {
                        return P.p(a, b, c);
                     }
                  };
               }
            };
         }
      }));
   }

   public static <A, B, C, D> Arbitrary<P4<A, B, C, D>> arbP4(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, new F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>>() {
         public F<B, F<C, F<D, P4<A, B, C, D>>>> f(final A a) {
            return new F<B, F<C, F<D, P4<A, B, C, D>>>>() {
               public F<C, F<D, P4<A, B, C, D>>> f(final B b) {
                  return new F<C, F<D, P4<A, B, C, D>>>() {
                     public F<D, P4<A, B, C, D>> f(final C c) {
                        return new F<D, P4<A, B, C, D>>() {
                           public P4<A, B, C, D> f(D d) {
                              return P.p(a, b, c, d);
                           }
                        };
                     }
                  };
               }
            };
         }
      }));
   }

   public static <A, B, C, D, E> Arbitrary<P5<A, B, C, D, E>> arbP5(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, new F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>>() {
         public F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>() {
               public F<C, F<D, F<E, P5<A, B, C, D, E>>>> f(final B b) {
                  return new F<C, F<D, F<E, P5<A, B, C, D, E>>>>() {
                     public F<D, F<E, P5<A, B, C, D, E>>> f(final C c) {
                        return new F<D, F<E, P5<A, B, C, D, E>>>() {
                           public F<E, P5<A, B, C, D, E>> f(final D d) {
                              return new F<E, P5<A, B, C, D, E>>() {
                                 public P5<A, B, C, D, E> f(E e) {
                                    return P.p(a, b, c, d, e);
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      }));
   }

   public static <A, B, C, D, E, F$> Arbitrary<P6<A, B, C, D, E, F$>> arbP6(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, new F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>() {
               public F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>() {
                     public F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>> f(final C c) {
                        return new F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>() {
                           public F<E, F<F$, P6<A, B, C, D, E, F$>>> f(final D d) {
                              return new F<E, F<F$, P6<A, B, C, D, E, F$>>>() {
                                 public F<F$, P6<A, B, C, D, E, F$>> f(final E e) {
                                    return new F<F$, P6<A, B, C, D, E, F$>>() {
                                       public P6<A, B, C, D, E, F$> f(F$ f) {
                                          return P.p(a, b, c, d, e, f);
                                       }
                                    };
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      }));
   }

   public static <A, B, C, D, E, F$, G> Arbitrary<P7<A, B, C, D, E, F$, G>> arbP7(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, ag.gen, new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>() {
                     public F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>() {
                           public F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>> f(final D d) {
                              return new F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>() {
                                 public F<F$, F<G, P7<A, B, C, D, E, F$, G>>> f(final E e) {
                                    return new F<F$, F<G, P7<A, B, C, D, E, F$, G>>>() {
                                       public F<G, P7<A, B, C, D, E, F$, G>> f(final F$ f) {
                                          return new F<G, P7<A, B, C, D, E, F$, G>>() {
                                             public P7<A, B, C, D, E, F$, G> f(G g) {
                                                return P.p(a, b, c, d, e, f, g);
                                             }
                                          };
                                       }
                                    };
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      }));
   }

   public static <A, B, C, D, E, F$, G, H> Arbitrary<P8<A, B, C, D, E, F$, G, H>> arbP8(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah) {
      return arbitrary(aa.gen.bind(ab.gen, ac.gen, ad.gen, ae.gen, af.gen, ag.gen, ah.gen, new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>() {
                     public F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>() {
                           public F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>> f(final D d) {
                              return new F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>() {
                                 public F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>> f(final E e) {
                                    return new F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>() {
                                       public F<G, F<H, P8<A, B, C, D, E, F$, G, H>>> f(final F$ f) {
                                          return new F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>() {
                                             public F<H, P8<A, B, C, D, E, F$, G, H>> f(final G g) {
                                                return new F<H, P8<A, B, C, D, E, F$, G, H>>() {
                                                   public P8<A, B, C, D, E, F$, G, H> f(H h) {
                                                      return P.p(a, b, c, d, e, f, g, h);
                                                   }
                                                };
                                             }
                                          };
                                       }
                                    };
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      }));
   }

   // $FF: synthetic method
   private static LcgRng lambda$arbLcgRng$159(Long l) {
      return new LcgRng(l);
   }

   // $FF: synthetic method
   private static State lambda$arbState$158(F f) {
      return State.unit(f);
   }

   // $FF: synthetic method
   private static Reader lambda$arbReader$157(F f) {
      return Reader.unit(f);
   }

   static {
      arbLong = arbitrary(arbInteger.gen.bind(arbInteger.gen, new F<Integer, F<Integer, Long>>() {
         public F<Integer, Long> f(final Integer i1) {
            return new F<Integer, Long>() {
               public Long f(Integer i2) {
                  return (long)i1 << 32 & (long)i2;
               }
            };
         }
      }));
      arbLongBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Long>>() {
         public Gen<Long> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(0L)), P.p(1, Gen.value(1L)), P.p(1, Gen.value(-1L)), P.p(1, Gen.value(Long.MAX_VALUE)), P.p(1, Gen.value(Long.MIN_VALUE)), P.p(1, Gen.value(9223372036854775806L)), P.p(1, Gen.value(-9223372036854775807L)), P.p(93, Arbitrary.arbLong.gen)));
         }
      }));
      arbByte = arbitrary(arbInteger.gen.map(new F<Integer, Byte>() {
         public Byte f(Integer i) {
            return (byte)i;
         }
      }));
      arbByteBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Byte>>() {
         public Gen<Byte> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value((byte)0)), P.p(1, Gen.value((byte)1)), P.p(1, Gen.value(-1)), P.p(1, Gen.value((byte)127)), P.p(1, Gen.value(-128)), P.p(1, Gen.value((byte)126)), P.p(1, Gen.value(-127)), P.p(93, Arbitrary.arbByte.gen)));
         }
      }));
      arbShort = arbitrary(arbInteger.gen.map(new F<Integer, Short>() {
         public Short f(Integer i) {
            return (short)i;
         }
      }));
      arbShortBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Short>>() {
         public Gen<Short> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(Short.valueOf((short)0))), P.p(1, Gen.value(Short.valueOf((short)1))), P.p(1, Gen.value(Short.valueOf((short)-1))), P.p(1, Gen.value((short)32767)), P.p(1, Gen.value(-32768)), P.p(1, Gen.value((short)32766)), P.p(1, Gen.value(-32767)), P.p(93, Arbitrary.arbShort.gen)));
         }
      }));
      arbCharacter = arbitrary(Gen.choose(0, 65536).map(new F<Integer, Character>() {
         public Character f(Integer i) {
            return (char)i;
         }
      }));
      arbCharacterBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Character>>() {
         public Gen<Character> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value('\u0000')), P.p(1, Gen.value('\u0001')), P.p(1, Gen.value('\uffff')), P.p(1, Gen.value('\ufffe')), P.p(95, Arbitrary.arbCharacter.gen)));
         }
      }));
      arbDouble = arbitrary(Gen.sized(new F<Integer, Gen<Double>>() {
         public Gen<Double> f(Integer i) {
            return Gen.choose((double)(-i), (double)i);
         }
      }));
      arbDoubleBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Double>>() {
         public Gen<Double> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(0.0D)), P.p(1, Gen.value(1.0D)), P.p(1, Gen.value(-1.0D)), P.p(1, Gen.value(Double.MAX_VALUE)), P.p(1, Gen.value(Double.MIN_VALUE)), P.p(1, Gen.value(Double.NaN)), P.p(1, Gen.value(Double.NEGATIVE_INFINITY)), P.p(1, Gen.value(Double.POSITIVE_INFINITY)), P.p(1, Gen.value(Double.MAX_VALUE)), P.p(91, Arbitrary.arbDouble.gen)));
         }
      }));
      arbFloat = arbitrary(arbDouble.gen.map(new F<Double, Float>() {
         public Float f(Double d) {
            return (float)d;
         }
      }));
      arbFloatBoundaries = arbitrary(Gen.sized(new F<Integer, Gen<Float>>() {
         public Gen<Float> f(Integer i) {
            return Gen.frequency(List.list(P.p(1, Gen.value(0.0F)), P.p(1, Gen.value(1.0F)), P.p(1, Gen.value(-1.0F)), P.p(1, Gen.value(Float.MAX_VALUE)), P.p(1, Gen.value(Float.MIN_VALUE)), P.p(1, Gen.value(Float.NaN)), P.p(1, Gen.value(Float.NEGATIVE_INFINITY)), P.p(1, Gen.value(Float.POSITIVE_INFINITY)), P.p(1, Gen.value(Float.MAX_VALUE)), P.p(91, Arbitrary.arbFloat.gen)));
         }
      }));
      arbString = arbitrary(arbList(arbCharacter).gen.map(new F<List<Character>, String>() {
         public String f(List<Character> cs) {
            return List.asString(cs);
         }
      }));
      arbUSASCIIString = arbitrary(arbList(arbCharacter).gen.map(new F<List<Character>, String>() {
         public String f(List<Character> cs) {
            return List.asString(cs.map(new F<Character, Character>() {
               public Character f(Character c) {
                  return (char)(c % 128);
               }
            }));
         }
      }));
      arbAlphaNumString = arbitrary(arbList(arbitrary(Gen.elements(Stream.range(Enumerator.charEnumerator, 'a', 'z').append(Stream.range(Enumerator.charEnumerator, 'A', 'Z')).append(Stream.range(Enumerator.charEnumerator, '0', '9')).toArray().array(Character[].class)))).gen.map(List.asString()));
      arbStringBuffer = arbitrary(arbString.gen.map(new F<String, StringBuffer>() {
         public StringBuffer f(String s) {
            return new StringBuffer(s);
         }
      }));
      arbStringBuilder = arbitrary(arbString.gen.map(new F<String, StringBuilder>() {
         public StringBuilder f(String s) {
            return new StringBuilder(s);
         }
      }));
      arbThrowable = arbThrowable(arbString);
      arbBitSet = arbitrary(arbList(arbBoolean).gen.map(new F<List<Boolean>, BitSet>() {
         public BitSet f(List<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.zipIndex().foreachDoEffect(new Effect1<P2<Boolean, Integer>>() {
               public void f(P2<Boolean, Integer> bi) {
                  s.set((Integer)bi._2(), (Boolean)bi._1());
               }
            });
            return s;
         }
      }));
      arbCalendar = arbitrary(arbLong.gen.map(new F<Long, Calendar>() {
         public Calendar f(Long i) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(i);
            return c;
         }
      }));
      arbDate = arbitrary(arbLong.gen.map(new F<Long, Date>() {
         public Date f(Long i) {
            return new Date(i);
         }
      }));
      arbGregorianCalendar = arbitrary(arbLong.gen.map(new F<Long, GregorianCalendar>() {
         public GregorianCalendar f(Long i) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTimeInMillis(i);
            return c;
         }
      }));
      arbProperties = arbitrary(arbHashtable(arbString, arbString).gen.map(new F<Hashtable<String, String>, Properties>() {
         public Properties f(Hashtable<String, String> ht) {
            Properties p = new Properties();
            Iterator var3 = ht.keySet().iterator();

            while(var3.hasNext()) {
               String k = (String)var3.next();
               p.setProperty(k, (String)ht.get(k));
            }

            return p;
         }
      }));
      arbSQLDate = arbitrary(arbLong.gen.map(new F<Long, java.sql.Date>() {
         public java.sql.Date f(Long i) {
            return new java.sql.Date(i);
         }
      }));
      arbTime = arbitrary(arbLong.gen.map(new F<Long, Time>() {
         public Time f(Long i) {
            return new Time(i);
         }
      }));
      arbTimestamp = arbitrary(arbLong.gen.map(new F<Long, Timestamp>() {
         public Timestamp f(Long i) {
            return new Timestamp(i);
         }
      }));
      arbBigInteger = arbitrary(arbArray(arbByte).gen.bind(arbByte.gen, new F<Array<Byte>, F<Byte, BigInteger>>() {
         public F<Byte, BigInteger> f(final Array<Byte> a) {
            return new F<Byte, BigInteger>() {
               public BigInteger f(Byte b) {
                  byte[] x = new byte[a.length() + 1];

                  for(int i = 0; i < a.array().length; ++i) {
                     x[i] = (Byte)a.get(i);
                  }

                  x[a.length()] = b;
                  return new BigInteger(x);
               }
            };
         }
      }));
      arbBigDecimal = arbitrary(arbBigInteger.gen.map(new F<BigInteger, BigDecimal>() {
         public BigDecimal f(BigInteger i) {
            return new BigDecimal(i);
         }
      }));
      arbLocale = arbitrary(Gen.elements(Locale.getAvailableLocales()));
   }

   // $FF: synthetic method
   static Reader access$lambda$0(F var0) {
      return lambda$arbReader$157(var0);
   }

   // $FF: synthetic method
   static State access$lambda$1(F var0) {
      return lambda$arbState$158(var0);
   }

   // $FF: synthetic method
   static LcgRng access$lambda$2(Long var0) {
      return lambda$arbLcgRng$159(var0);
   }
}
