package fj.test;

import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.Primitive;
import fj.data.Array;
import fj.data.Conversions;
import fj.data.Either;
import fj.data.Java;
import fj.data.List;
import fj.data.Option;
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

public final class Shrink<A> {
   private final F<A, Stream<A>> f;
   public static final Shrink<Long> shrinkLong = new Shrink(new F<Long, Stream<Long>>() {
      public Stream<Long> f(final Long i) {
         if (i == 0L) {
            return Stream.nil();
         } else {
            final Stream<Long> is = Stream.cons(0L, new P1<Stream<Long>>() {
               public Stream<Long> _1() {
                  return Stream.iterate(new F<Long, Long>() {
                     public Long f(Long x) {
                        return x / 2L;
                     }
                  }, i).takeWhile(new F<Long, Boolean>() {
                     public Boolean f(Long x) {
                        return x != 0L;
                     }
                  }).map(new F<Long, Long>() {
                     public Long f(Long x) {
                        return i - x;
                     }
                  });
               }
            });
            return i < 0L ? Stream.cons(-i, new P1<Stream<Long>>() {
               public Stream<Long> _1() {
                  return is;
               }
            }) : is;
         }
      }
   });
   public static final Shrink<Boolean> shrinkBoolean = shrink(Function.constant(Stream.single(false)));
   public static final Shrink<Integer> shrinkInteger;
   public static final Shrink<Byte> shrinkByte;
   public static final Shrink<Character> shrinkCharacter;
   public static final Shrink<Short> shrinkShort;
   public static final Shrink<Float> shrinkFloat;
   public static final Shrink<Double> shrinkDouble;
   public static final Shrink<String> shrinkString;
   public static final Shrink<StringBuffer> shrinkStringBuffer;
   public static final Shrink<StringBuilder> shrinkStringBuilder;
   public static final Shrink<Throwable> shrinkThrowable;
   public static final Shrink<BitSet> shrinkBitSet;
   public static final Shrink<Calendar> shrinkCalendar;
   public static final Shrink<Date> shrinkDate;
   public static final Shrink<GregorianCalendar> shrinkGregorianCalendar;
   public static final Shrink<Properties> shrinkProperties;
   public static final Shrink<java.sql.Date> shrinkSQLDate;
   public static final Shrink<Time> shrinkTime;
   public static final Shrink<Timestamp> shrinkTimestamp;
   public static final Shrink<BigInteger> shrinkBigInteger;
   public static final Shrink<BigDecimal> shrinkBigDecimal;

   private Shrink(F<A, Stream<A>> f) {
      this.f = f;
   }

   public Stream<A> shrink(A a) {
      return (Stream)this.f.f(a);
   }

   public <B> Shrink<B> map(final F<A, B> f, final F<B, A> g) {
      return new Shrink(new F<B, Stream<B>>() {
         public Stream<B> f(B b) {
            return ((Stream)Shrink.this.f.f(g.f(b))).map(f);
         }
      });
   }

   public static <A> Shrink<A> shrink(F<A, Stream<A>> f) {
      return new Shrink(f);
   }

   public static <A> Shrink<A> empty() {
      return new Shrink(new F<A, Stream<A>>() {
         public Stream<A> f(A a) {
            return Stream.nil();
         }
      });
   }

   public static <A> Shrink<Option<A>> shrinkOption(final Shrink<A> sa) {
      return new Shrink(new F<Option<A>, Stream<Option<A>>>() {
         public Stream<Option<A>> f(final Option<A> o) {
            return o.isNone() ? Stream.nil() : Stream.cons(Option.none(), new P1<Stream<Option<A>>>() {
               public Stream<Option<A>> _1() {
                  return sa.shrink(o.some()).map(Option.some_());
               }
            });
         }
      });
   }

   public static <A, B> Shrink<Either<A, B>> shrinkEither(final Shrink<A> sa, final Shrink<B> sb) {
      return new Shrink(new F<Either<A, B>, Stream<Either<A, B>>>() {
         public Stream<Either<A, B>> f(Either<A, B> e) {
            return e.isLeft() ? sa.shrink(e.left().value()).map(Either.left_()) : sb.shrink(e.right().value()).map(Either.right_());
         }
      });
   }

   public static <A> Shrink<List<A>> shrinkList(final Shrink<A> sa) {
      return new Shrink(new F<List<A>, Stream<List<A>>>() {
         public Stream<List<A>> f(List<A> as) {
            Util u = new Util();
            return u.removeChunks(as.length(), as).append(u.shrinkOne(as));
         }
      });

      final class Util {
         Stream<List<A>> removeChunks(int n, final List<A> as) {
            if (as.isEmpty()) {
               return Stream.nil();
            } else if (as.tail().isEmpty()) {
               return Stream.cons(List.nil(), Stream.nil_());
            } else {
               final int n1 = n / 2;
               final int n2 = n - n1;
               final List<A> as1 = as.take(n1);
               final F<List<A>, Boolean> isNotEmpty = List.isNotEmpty_();
               return Stream.cons(as1, new P1<Stream<List<A>>>() {
                  public Stream<List<A>> _1() {
                     final List<A> as2 = as.drop(n1);
                     return Stream.cons(as2, new P1<Stream<List<A>>>() {
                        public Stream<List<A>> _1() {
                           return Util.this.removeChunks(n1, as1).filter(isNotEmpty).map(new F<List<A>, List<A>>() {
                              public List<A> f(List<A> aas) {
                                 return aas.append(as2);
                              }
                           }).interleave(Util.this.removeChunks(n2, as2).filter(isNotEmpty).map(new F<List<A>, List<A>>() {
                              public List<A> f(List<A> aas) {
                                 return as1.append(aas);
                              }
                           }));
                        }
                     });
                  }
               });
            }
         }

         Stream<List<A>> shrinkOne(final List<A> as) {
            return as.isEmpty() ? Stream.nil() : Shrink.this.shrink(as.head()).map(new F<A, List<A>>() {
               public List<A> f(A a) {
                  return as.tail().cons(a);
               }
            }).append(this.shrinkOne(as.tail()).map(new F<List<A>, List<A>>() {
               public List<A> f(List<A> aas) {
                  return aas.cons(as.head());
               }
            }));
         }
      }

   }

   public static <A> Shrink<Array<A>> shrinkArray(Shrink<A> sa) {
      return shrinkList(sa).map(Conversions.List_Array(), Conversions.Array_List());
   }

   public static <A> Shrink<Stream<A>> shrinkStream(Shrink<A> sa) {
      return shrinkList(sa).map(Conversions.List_Stream(), Conversions.Stream_List());
   }

   public static Shrink<Throwable> shrinkThrowable(Shrink<String> ss) {
      return ss.map(new F<String, Throwable>() {
         public Throwable f(String s) {
            return new Throwable(s);
         }
      }, new F<Throwable, String>() {
         public String f(Throwable t) {
            return t.getMessage();
         }
      });
   }

   public static <A> Shrink<ArrayList<A>> shrinkArrayList(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_ArrayList(), Java.ArrayList_List());
   }

   public static <K extends Enum<K>, V> Shrink<EnumMap<K, V>> shrinkEnumMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, EnumMap<K, V>>() {
         public EnumMap<K, V> f(Hashtable<K, V> h) {
            return new EnumMap(h);
         }
      }, new F<EnumMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(EnumMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A extends Enum<A>> Shrink<EnumSet<A>> shrinkEnumSet(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_EnumSet(), Java.EnumSet_List());
   }

   public static <K, V> Shrink<HashMap<K, V>> shrinkHashMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, HashMap<K, V>>() {
         public HashMap<K, V> f(Hashtable<K, V> h) {
            return new HashMap(h);
         }
      }, new F<HashMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(HashMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A> Shrink<HashSet<A>> shrinkHashSet(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_HashSet(), Java.HashSet_List());
   }

   public static <K, V> Shrink<Hashtable<K, V>> shrinkHashtable(Shrink<K> sk, Shrink<V> sv) {
      return shrinkList(shrinkP2(sk, sv)).map(new F<List<P2<K, V>>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(List<P2<K, V>> kvs) {
            Hashtable<K, V> h = new Hashtable();
            kvs.foreachDoEffect(Shrink$19$$Lambda$1.lambdaFactory$(h));
            return h;
         }

         // $FF: synthetic method
         private static void lambda$f$161(Hashtable var0, P2 kv) {
            var0.put(kv._1(), kv._2());
         }

         // $FF: synthetic method
         static void access$lambda$0(Hashtable var0, P2 var1) {
            lambda$f$161(var0, var1);
         }
      }, new F<Hashtable<K, V>, List<P2<K, V>>>() {
         public List<P2<K, V>> f(Hashtable<K, V> h) {
            List<P2<K, V>> x = List.nil();

            Object k;
            for(Iterator var3 = h.keySet().iterator(); var3.hasNext(); x = x.snoc(P.p(k, h.get(k)))) {
               k = var3.next();
            }

            return x;
         }
      });
   }

   public static <K, V> Shrink<IdentityHashMap<K, V>> shrinkIdentityHashMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, IdentityHashMap<K, V>>() {
         public IdentityHashMap<K, V> f(Hashtable<K, V> h) {
            return new IdentityHashMap(h);
         }
      }, new F<IdentityHashMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(IdentityHashMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <K, V> Shrink<LinkedHashMap<K, V>> shrinkLinkedHashMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, LinkedHashMap<K, V>>() {
         public LinkedHashMap<K, V> f(Hashtable<K, V> h) {
            return new LinkedHashMap(h);
         }
      }, new F<LinkedHashMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(LinkedHashMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A> Shrink<LinkedHashSet<A>> shrinkLinkedHashSet(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_LinkedHashSet(), Java.LinkedHashSet_List());
   }

   public static <A> Shrink<LinkedList<A>> shrinkLinkedList(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_LinkedList(), Java.LinkedList_List());
   }

   public static <A> Shrink<PriorityQueue<A>> shrinkPriorityQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_PriorityQueue(), Java.PriorityQueue_List());
   }

   public static <A> Shrink<Stack<A>> shrinkStack(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_Stack(), Java.Stack_List());
   }

   public static <K, V> Shrink<TreeMap<K, V>> shrinkTreeMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, TreeMap<K, V>>() {
         public TreeMap<K, V> f(Hashtable<K, V> h) {
            return new TreeMap(h);
         }
      }, new F<TreeMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(TreeMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A> Shrink<TreeSet<A>> shrinkTreeSet(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_TreeSet(), Java.TreeSet_List());
   }

   public static <A> Shrink<Vector<A>> shrinkVector(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_Vector(), Java.Vector_List());
   }

   public static <K, V> Shrink<WeakHashMap<K, V>> shrinkWeakHashMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, WeakHashMap<K, V>>() {
         public WeakHashMap<K, V> f(Hashtable<K, V> h) {
            return new WeakHashMap(h);
         }
      }, new F<WeakHashMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(WeakHashMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A> Shrink<ArrayBlockingQueue<A>> shrinkArrayBlockingQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_ArrayBlockingQueue(false), Java.ArrayBlockingQueue_List());
   }

   public static <K, V> Shrink<ConcurrentHashMap<K, V>> shrinkConcurrentHashMap(Shrink<K> sk, Shrink<V> sv) {
      return shrinkHashtable(sk, sv).map(new F<Hashtable<K, V>, ConcurrentHashMap<K, V>>() {
         public ConcurrentHashMap<K, V> f(Hashtable<K, V> h) {
            return new ConcurrentHashMap(h);
         }
      }, new F<ConcurrentHashMap<K, V>, Hashtable<K, V>>() {
         public Hashtable<K, V> f(ConcurrentHashMap<K, V> m) {
            return new Hashtable(m);
         }
      });
   }

   public static <A> Shrink<ConcurrentLinkedQueue<A>> shrinkConcurrentLinkedQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_ConcurrentLinkedQueue(), Java.ConcurrentLinkedQueue_List());
   }

   public static <A> Shrink<CopyOnWriteArrayList<A>> shrinkCopyOnWriteArrayList(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_CopyOnWriteArrayList(), Java.CopyOnWriteArrayList_List());
   }

   public static <A> Shrink<CopyOnWriteArraySet<A>> shrinkCopyOnWriteArraySet(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_CopyOnWriteArraySet(), Java.CopyOnWriteArraySet_List());
   }

   public static <A extends Delayed> Shrink<DelayQueue<A>> shrinkDelayQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_DelayQueue(), Java.DelayQueue_List());
   }

   public static <A> Shrink<LinkedBlockingQueue<A>> shrinkLinkedBlockingQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_LinkedBlockingQueue(), Java.LinkedBlockingQueue_List());
   }

   public static <A> Shrink<PriorityBlockingQueue<A>> shrinkPriorityBlockingQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_PriorityBlockingQueue(), Java.PriorityBlockingQueue_List());
   }

   public static <A> Shrink<SynchronousQueue<A>> shrinkSynchronousQueue(Shrink<A> sa) {
      return shrinkList(sa).map(Java.List_SynchronousQueue(false), Java.SynchronousQueue_List());
   }

   public static <A> Shrink<P1<A>> shrinkP1(final Shrink<A> sa) {
      return new Shrink(new F<P1<A>, Stream<P1<A>>>() {
         public Stream<P1<A>> f(P1<A> p) {
            return sa.shrink(p._1()).map(new F<A, P1<A>>() {
               public P1<A> f(A a) {
                  return P.p(a);
               }
            });
         }
      });
   }

   public static <A, B> Shrink<P2<A, B>> shrinkP2(final Shrink<A> sa, final Shrink<B> sb) {
      return new Shrink(new F<P2<A, B>, Stream<P2<A, B>>>() {
         public Stream<P2<A, B>> f(P2<A, B> p) {
            F<A, F<B, P2<A, B>>> p2 = P.p2();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), p2);
         }
      });
   }

   public static <A, B, C> Shrink<P3<A, B, C>> shrinkP3(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc) {
      return new Shrink(new F<P3<A, B, C>, Stream<P3<A, B, C>>>() {
         public Stream<P3<A, B, C>> f(P3<A, B, C> p) {
            F<A, F<B, F<C, P3<A, B, C>>>> p3 = P.p3();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), p3);
         }
      });
   }

   public static <A, B, C, D> Shrink<P4<A, B, C, D>> shrinkP4(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd) {
      return new Shrink(new F<P4<A, B, C, D>, Stream<P4<A, B, C, D>>>() {
         public Stream<P4<A, B, C, D>> f(P4<A, B, C, D> p) {
            F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>> p4 = P.p4();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), sd.shrink(p._4()), p4);
         }
      });
   }

   public static <A, B, C, D, E> Shrink<P5<A, B, C, D, E>> shrinkP5(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se) {
      return new Shrink(new F<P5<A, B, C, D, E>, Stream<P5<A, B, C, D, E>>>() {
         public Stream<P5<A, B, C, D, E>> f(P5<A, B, C, D, E> p) {
            F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>> p5 = P.p5();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), sd.shrink(p._4()), se.shrink(p._5()), p5);
         }
      });
   }

   public static <A, B, C, D, E, F$> Shrink<P6<A, B, C, D, E, F$>> shrinkP6(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf) {
      return new Shrink(new F<P6<A, B, C, D, E, F$>, Stream<P6<A, B, C, D, E, F$>>>() {
         public Stream<P6<A, B, C, D, E, F$>> f(P6<A, B, C, D, E, F$> p) {
            F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>> p6 = P.p6();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), sd.shrink(p._4()), se.shrink(p._5()), sf.shrink(p._6()), p6);
         }
      });
   }

   public static <A, B, C, D, E, F$, G> Shrink<P7<A, B, C, D, E, F$, G>> shrinkP7(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf, final Shrink<G> sg) {
      return new Shrink(new F<P7<A, B, C, D, E, F$, G>, Stream<P7<A, B, C, D, E, F$, G>>>() {
         public Stream<P7<A, B, C, D, E, F$, G>> f(P7<A, B, C, D, E, F$, G> p) {
            F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>> p7 = P.p7();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), sd.shrink(p._4()), se.shrink(p._5()), sf.shrink(p._6()), sg.shrink(p._7()), p7);
         }
      });
   }

   public static <A, B, C, D, E, F$, G, H> Shrink<P8<A, B, C, D, E, F$, G, H>> shrinkP8(final Shrink<A> sa, final Shrink<B> sb, final Shrink<C> sc, final Shrink<D> sd, final Shrink<E> se, final Shrink<F$> sf, final Shrink<G> sg, final Shrink<H> sh) {
      return new Shrink(new F<P8<A, B, C, D, E, F$, G, H>, Stream<P8<A, B, C, D, E, F$, G, H>>>() {
         public Stream<P8<A, B, C, D, E, F$, G, H>> f(P8<A, B, C, D, E, F$, G, H> p) {
            F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>> p8 = P.p8();
            return sa.shrink(p._1()).bind(sb.shrink(p._2()), sc.shrink(p._3()), sd.shrink(p._4()), se.shrink(p._5()), sf.shrink(p._6()), sg.shrink(p._7()), sh.shrink(p._8()), p8);
         }
      });
   }

   static {
      shrinkInteger = shrinkLong.map(Primitive.Long_Integer, Primitive.Integer_Long);
      shrinkByte = shrinkLong.map(Primitive.Long_Byte, Primitive.Byte_Long);
      shrinkCharacter = shrinkLong.map(Primitive.Long_Character, Primitive.Character_Long);
      shrinkShort = shrinkLong.map(Primitive.Long_Short, Primitive.Short_Long);
      shrinkFloat = shrinkLong.map(Primitive.Long_Float, Primitive.Float_Long);
      shrinkDouble = shrinkLong.map(Primitive.Long_Double, Primitive.Double_Long);
      shrinkString = shrinkList(shrinkCharacter).map(Conversions.List_String, Conversions.String_List);
      shrinkStringBuffer = shrinkList(shrinkCharacter).map(Conversions.List_StringBuffer, Conversions.StringBuffer_List);
      shrinkStringBuilder = shrinkList(shrinkCharacter).map(Conversions.List_StringBuilder, Conversions.StringBuilder_List);
      shrinkThrowable = shrinkThrowable(shrinkString);
      shrinkBitSet = shrinkList(shrinkBoolean).map(Java.List_BitSet, Java.BitSet_List);
      shrinkCalendar = shrinkLong.map(new F<Long, Calendar>() {
         public Calendar f(Long i) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(i);
            return c;
         }
      }, new F<Calendar, Long>() {
         public Long f(Calendar c) {
            return c.getTime().getTime();
         }
      });
      shrinkDate = shrinkLong.map(new F<Long, Date>() {
         public Date f(Long i) {
            return new Date(i);
         }
      }, new F<Date, Long>() {
         public Long f(Date d) {
            return d.getTime();
         }
      });
      shrinkGregorianCalendar = shrinkLong.map(new F<Long, GregorianCalendar>() {
         public GregorianCalendar f(Long i) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTimeInMillis(i);
            return c;
         }
      }, new F<GregorianCalendar, Long>() {
         public Long f(GregorianCalendar c) {
            return c.getTime().getTime();
         }
      });
      shrinkProperties = shrinkHashtable(shrinkString, shrinkString).map(new F<Hashtable<String, String>, Properties>() {
         public Properties f(Hashtable<String, String> h) {
            Properties p = new Properties();
            Iterator var3 = h.keySet().iterator();

            while(var3.hasNext()) {
               String k = (String)var3.next();
               p.setProperty(k, (String)h.get(k));
            }

            return p;
         }
      }, new F<Properties, Hashtable<String, String>>() {
         public Hashtable<String, String> f(Properties p) {
            Hashtable<String, String> t = new Hashtable();
            Iterator var3 = p.keySet().iterator();

            while(var3.hasNext()) {
               Object s = var3.next();
               t.put((String)s, p.getProperty((String)s));
            }

            return t;
         }
      });
      shrinkSQLDate = shrinkLong.map(new F<Long, java.sql.Date>() {
         public java.sql.Date f(Long i) {
            return new java.sql.Date(i);
         }
      }, new F<java.sql.Date, Long>() {
         public Long f(java.sql.Date c) {
            return c.getTime();
         }
      });
      shrinkTime = shrinkLong.map(new F<Long, Time>() {
         public Time f(Long i) {
            return new Time(i);
         }
      }, new F<Time, Long>() {
         public Long f(Time c) {
            return c.getTime();
         }
      });
      shrinkTimestamp = shrinkLong.map(new F<Long, Timestamp>() {
         public Timestamp f(Long i) {
            return new Timestamp(i);
         }
      }, new F<Timestamp, Long>() {
         public Long f(Timestamp c) {
            return c.getTime();
         }
      });
      shrinkBigInteger = shrinkP2(shrinkByte, shrinkArray(shrinkByte)).map(new F<P2<Byte, Array<Byte>>, BigInteger>() {
         public BigInteger f(P2<Byte, Array<Byte>> bs) {
            byte[] x = new byte[((Array)bs._2()).length() + 1];

            for(int i = 0; i < ((Array)bs._2()).array().length; ++i) {
               x[i] = (Byte)((Array)bs._2()).get(i);
            }

            x[((Array)bs._2()).length()] = (Byte)bs._1();
            return new BigInteger(x);
         }
      }, new F<BigInteger, P2<Byte, Array<Byte>>>() {
         public P2<Byte, Array<Byte>> f(BigInteger i) {
            byte[] b = i.toByteArray();
            Byte[] x = new Byte[b.length - 1];
            System.arraycopy(b, 0, x, 0, b.length - 1);
            return P.p(b[0], Array.array((Object[])x));
         }
      });
      shrinkBigDecimal = shrinkBigInteger.map(new F<BigInteger, BigDecimal>() {
         public BigDecimal f(BigInteger i) {
            return new BigDecimal(i);
         }
      }, new F<BigDecimal, BigInteger>() {
         public BigInteger f(BigDecimal d) {
            return d.toBigInteger();
         }
      });
   }
}
