package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.LazyString;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.Writer;
import fj.data.hlist.HList;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Equal<A> {
   private final F<A, F<A, Boolean>> f;
   public static final Equal<Boolean> booleanEqual = anyEqual();
   public static final Equal<Byte> byteEqual = anyEqual();
   public static final Equal<Character> charEqual = anyEqual();
   public static final Equal<Double> doubleEqual = anyEqual();
   public static final Equal<Float> floatEqual = anyEqual();
   public static final Equal<Integer> intEqual = anyEqual();
   public static final Equal<BigInteger> bigintEqual = anyEqual();
   public static final Equal<BigDecimal> bigdecimalEqual = anyEqual();
   public static final Equal<Long> longEqual = anyEqual();
   public static final Equal<Short> shortEqual = anyEqual();
   public static final Equal<String> stringEqual = anyEqual();
   public static final Equal<StringBuffer> stringBufferEqual = new Equal(new F<StringBuffer, F<StringBuffer, Boolean>>() {
      public F<StringBuffer, Boolean> f(final StringBuffer sb1) {
         return new F<StringBuffer, Boolean>() {
            public Boolean f(StringBuffer sb2) {
               if (sb1.length() == sb2.length()) {
                  for(int i = 0; i < sb1.length(); ++i) {
                     if (sb1.charAt(i) != sb2.charAt(i)) {
                        return false;
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            }
         };
      }
   });
   public static final Equal<StringBuilder> stringBuilderEqual = new Equal(new F<StringBuilder, F<StringBuilder, Boolean>>() {
      public F<StringBuilder, Boolean> f(final StringBuilder sb1) {
         return new F<StringBuilder, Boolean>() {
            public Boolean f(StringBuilder sb2) {
               if (sb1.length() == sb2.length()) {
                  for(int i = 0; i < sb1.length(); ++i) {
                     if (sb1.charAt(i) != sb2.charAt(i)) {
                        return false;
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            }
         };
      }
   });
   public static final Equal<LazyString> eq;
   public static final Equal<HList.HNil> hListEqual;

   private Equal(F<A, F<A, Boolean>> f) {
      this.f = f;
   }

   public boolean eq(A a1, A a2) {
      return (Boolean)((F)this.f.f(a1)).f(a2);
   }

   public F2<A, A, Boolean> eq() {
      return new F2<A, A, Boolean>() {
         public Boolean f(A a, A a1) {
            return Equal.this.eq(a, a1);
         }
      };
   }

   public F<A, Boolean> eq(final A a) {
      return new F<A, Boolean>() {
         public Boolean f(A a1) {
            return Equal.this.eq(a, a1);
         }
      };
   }

   public <B> Equal<B> comap(F<B, A> f) {
      return equal(F1Functions.o(F1Functions.o(F1Functions.andThen(f), this.f), f));
   }

   public static <A> Equal<A> equal(F<A, F<A, Boolean>> f) {
      return new Equal(f);
   }

   public static <A> Equal<A> anyEqual() {
      return new Equal(new F<A, F<A, Boolean>>() {
         public F<A, Boolean> f(final A a1) {
            return new F<A, Boolean>() {
               public Boolean f(A a2) {
                  return a1.equals(a2);
               }
            };
         }
      });
   }

   public static <A, B> Equal<Either<A, B>> eitherEqual(final Equal<A> ea, final Equal<B> eb) {
      return new Equal(new F<Either<A, B>, F<Either<A, B>, Boolean>>() {
         public F<Either<A, B>, Boolean> f(final Either<A, B> e1) {
            return new F<Either<A, B>, Boolean>() {
               public Boolean f(Either<A, B> e2) {
                  return e1.isLeft() && e2.isLeft() && (Boolean)((F)ea.f.f(e1.left().value())).f(e2.left().value()) || e1.isRight() && e2.isRight() && (Boolean)((F)eb.f.f(e1.right().value())).f(e2.right().value());
               }
            };
         }
      });
   }

   public static <A, B> Equal<Validation<A, B>> validationEqual(Equal<A> ea, Equal<B> eb) {
      return eitherEqual(ea, eb).comap(Validation.either());
   }

   public static <A> Equal<List<A>> listEqual(final Equal<A> ea) {
      return new Equal(new F<List<A>, F<List<A>, Boolean>>() {
         public F<List<A>, Boolean> f(final List<A> a1) {
            return new F<List<A>, Boolean>() {
               public Boolean f(List<A> a2) {
                  List<A> x1 = a1;

                  List x2;
                  for(x2 = a2; x1.isNotEmpty() && x2.isNotEmpty(); x2 = x2.tail()) {
                     if (!ea.eq(x1.head(), x2.head())) {
                        return false;
                     }

                     x1 = x1.tail();
                  }

                  return x1.isEmpty() && x2.isEmpty();
               }
            };
         }
      });
   }

   public static <A> Equal<NonEmptyList<A>> nonEmptyListEqual(Equal<A> ea) {
      return listEqual(ea).comap(NonEmptyList.toList_());
   }

   public static <A> Equal<Option<A>> optionEqual(final Equal<A> ea) {
      return new Equal(new F<Option<A>, F<Option<A>, Boolean>>() {
         public F<Option<A>, Boolean> f(final Option<A> o1) {
            return new F<Option<A>, Boolean>() {
               public Boolean f(Option<A> o2) {
                  return o1.isNone() && o2.isNone() || o1.isSome() && o2.isSome() && (Boolean)((F)ea.f.f(o1.some())).f(o2.some());
               }
            };
         }
      });
   }

   public static <A> Equal<Stream<A>> streamEqual(final Equal<A> ea) {
      return new Equal(new F<Stream<A>, F<Stream<A>, Boolean>>() {
         public F<Stream<A>, Boolean> f(final Stream<A> a1) {
            return new F<Stream<A>, Boolean>() {
               public Boolean f(Stream<A> a2) {
                  Stream<A> x1 = a1;

                  Stream x2;
                  for(x2 = a2; x1.isNotEmpty() && x2.isNotEmpty(); x2 = (Stream)x2.tail()._1()) {
                     if (!ea.eq(x1.head(), x2.head())) {
                        return false;
                     }

                     x1 = (Stream)x1.tail()._1();
                  }

                  return x1.isEmpty() && x2.isEmpty();
               }
            };
         }
      });
   }

   public static <A> Equal<Array<A>> arrayEqual(final Equal<A> ea) {
      return new Equal(new F<Array<A>, F<Array<A>, Boolean>>() {
         public F<Array<A>, Boolean> f(final Array<A> a1) {
            return new F<Array<A>, Boolean>() {
               public Boolean f(Array<A> a2) {
                  if (a1.length() == a2.length()) {
                     for(int i = 0; i < a1.length(); ++i) {
                        if (!ea.eq(a1.get(i), a2.get(i))) {
                           return false;
                        }
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            };
         }
      });
   }

   public static <A> Equal<Tree<A>> treeEqual(final Equal<A> ea) {
      return new Equal(Function.curry(new F2<Tree<A>, Tree<A>, Boolean>() {
         public Boolean f(Tree<A> t1, Tree<A> t2) {
            return ea.eq(t1.root(), t2.root()) && Equal.p1Equal(Equal.streamEqual(Equal.treeEqual(ea))).eq(t2.subForest(), t1.subForest());
         }
      }));
   }

   public static <A> Equal<P1<A>> p1Equal(final Equal<A> ea) {
      return new Equal(new F<P1<A>, F<P1<A>, Boolean>>() {
         public F<P1<A>, Boolean> f(final P1<A> p1) {
            return new F<P1<A>, Boolean>() {
               public Boolean f(P1<A> p2) {
                  return ea.eq(p1._1(), p2._1());
               }
            };
         }
      });
   }

   public static <A, B> Equal<P2<A, B>> p2Equal(final Equal<A> ea, final Equal<B> eb) {
      return new Equal(new F<P2<A, B>, F<P2<A, B>, Boolean>>() {
         public F<P2<A, B>, Boolean> f(final P2<A, B> p1) {
            return new F<P2<A, B>, Boolean>() {
               public Boolean f(P2<A, B> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2());
               }
            };
         }
      });
   }

   public static <A, B, C> Equal<P3<A, B, C>> p3Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec) {
      return new Equal(new F<P3<A, B, C>, F<P3<A, B, C>, Boolean>>() {
         public F<P3<A, B, C>, Boolean> f(final P3<A, B, C> p1) {
            return new F<P3<A, B, C>, Boolean>() {
               public Boolean f(P3<A, B, C> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3());
               }
            };
         }
      });
   }

   public static <A, B, C, D> Equal<P4<A, B, C, D>> p4Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed) {
      return new Equal(new F<P4<A, B, C, D>, F<P4<A, B, C, D>, Boolean>>() {
         public F<P4<A, B, C, D>, Boolean> f(final P4<A, B, C, D> p1) {
            return new F<P4<A, B, C, D>, Boolean>() {
               public Boolean f(P4<A, B, C, D> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4());
               }
            };
         }
      });
   }

   public static <A, B, C, D, E> Equal<P5<A, B, C, D, E>> p5Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee) {
      return new Equal(new F<P5<A, B, C, D, E>, F<P5<A, B, C, D, E>, Boolean>>() {
         public F<P5<A, B, C, D, E>, Boolean> f(final P5<A, B, C, D, E> p1) {
            return new F<P5<A, B, C, D, E>, Boolean>() {
               public Boolean f(P5<A, B, C, D, E> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5());
               }
            };
         }
      });
   }

   public static <A, B, C, D, E, F$> Equal<P6<A, B, C, D, E, F$>> p6Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef) {
      return new Equal(new F<P6<A, B, C, D, E, F$>, F<P6<A, B, C, D, E, F$>, Boolean>>() {
         public F<P6<A, B, C, D, E, F$>, Boolean> f(final P6<A, B, C, D, E, F$> p1) {
            return new F<P6<A, B, C, D, E, F$>, Boolean>() {
               public Boolean f(P6<A, B, C, D, E, F$> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6());
               }
            };
         }
      });
   }

   public static <A, B, C, D, E, F$, G> Equal<P7<A, B, C, D, E, F$, G>> p7Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef, final Equal<G> eg) {
      return new Equal(new F<P7<A, B, C, D, E, F$, G>, F<P7<A, B, C, D, E, F$, G>, Boolean>>() {
         public F<P7<A, B, C, D, E, F$, G>, Boolean> f(final P7<A, B, C, D, E, F$, G> p1) {
            return new F<P7<A, B, C, D, E, F$, G>, Boolean>() {
               public Boolean f(P7<A, B, C, D, E, F$, G> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6()) && eg.eq(p1._7(), p2._7());
               }
            };
         }
      });
   }

   public static <A, B, C, D, E, F$, G, H> Equal<P8<A, B, C, D, E, F$, G, H>> p8Equal(final Equal<A> ea, final Equal<B> eb, final Equal<C> ec, final Equal<D> ed, final Equal<E> ee, final Equal<F$> ef, final Equal<G> eg, final Equal<H> eh) {
      return new Equal(new F<P8<A, B, C, D, E, F$, G, H>, F<P8<A, B, C, D, E, F$, G, H>, Boolean>>() {
         public F<P8<A, B, C, D, E, F$, G, H>, Boolean> f(final P8<A, B, C, D, E, F$, G, H> p1) {
            return new F<P8<A, B, C, D, E, F$, G, H>, Boolean>() {
               public Boolean f(P8<A, B, C, D, E, F$, G, H> p2) {
                  return ea.eq(p1._1(), p2._1()) && eb.eq(p1._2(), p2._2()) && ec.eq(p1._3(), p2._3()) && ed.eq(p1._4(), p2._4()) && ee.eq(p1._5(), p2._5()) && ef.eq(p1._6(), p2._6()) && eg.eq(p1._7(), p2._7()) && eh.eq(p1._8(), p2._8());
               }
            };
         }
      });
   }

   public static <A> Equal<V2<A>> v2Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V2.toStream_());
   }

   public static <A> Equal<V3<A>> v3Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V3.toStream_());
   }

   public static <A> Equal<V4<A>> v4Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V4.toStream_());
   }

   public static <A> Equal<V5<A>> v5Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V5.toStream_());
   }

   public static <A> Equal<V6<A>> v6Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V6.toStream_());
   }

   public static <A> Equal<V7<A>> v7Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V7.toStream_());
   }

   public static <A> Equal<V8<A>> v8Equal(Equal<A> ea) {
      return streamEqual(ea).comap(V8.toStream_());
   }

   public static <E, L extends HList<L>> Equal<HList.HCons<E, L>> hListEqual(final Equal<E> e, final Equal<L> l) {
      return equal(Function.curry(new F2<HList.HCons<E, L>, HList.HCons<E, L>, Boolean>() {
         public Boolean f(HList.HCons<E, L> c1, HList.HCons<E, L> c2) {
            return e.eq(c1.head(), c2.head()) && l.eq(c1.tail(), c2.tail());
         }
      }));
   }

   public static <A> Equal<Set<A>> setEqual(final Equal<A> e) {
      return equal(Function.curry(new F2<Set<A>, Set<A>, Boolean>() {
         public Boolean f(Set<A> a, Set<A> b) {
            return Equal.streamEqual(e).eq(a.toStream(), b.toStream());
         }
      }));
   }

   public static <A, B> Equal<Writer<A, B>> writerEqual(Equal<A> eq1, Equal<B> eq2) {
      return new Equal(Equal$$Lambda$1.lambdaFactory$(eq1, eq2));
   }

   // $FF: synthetic method
   private static F lambda$writerEqual$47(Equal var0, Equal var1, Writer w1) {
      return Equal$$Lambda$2.lambdaFactory$(var0, var1, w1);
   }

   // $FF: synthetic method
   private static Boolean lambda$null$46(Equal var0, Equal var1, Writer var2, Writer w2) {
      return p2Equal(var0, var1).eq(var2.run(), w2.run());
   }

   static {
      eq = streamEqual(charEqual).comap(LazyString.toStream);
      hListEqual = anyEqual();
   }

   // $FF: synthetic method
   static F access$lambda$0(Equal var0, Equal var1, Writer var2) {
      return lambda$writerEqual$47(var0, var1, var2);
   }

   // $FF: synthetic method
   static Boolean access$lambda$1(Equal var0, Equal var1, Writer var2, Writer var3) {
      return lambda$null$46(var0, var1, var2, var3);
   }
}
