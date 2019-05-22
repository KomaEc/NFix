package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Validation;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Ord<A> {
   private final F<A, F<A, Ordering>> f;
   public final F<A, F<A, A>> max = Function.curry(new F2<A, A, A>() {
      public A f(A a, A a1) {
         return Ord.this.max(a, a1);
      }
   });
   public final F<A, F<A, A>> min = Function.curry(new F2<A, A, A>() {
      public A f(A a, A a1) {
         return Ord.this.min(a, a1);
      }
   });
   public static final Ord<Boolean> booleanOrd = new Ord(new F<Boolean, F<Boolean, Ordering>>() {
      public F<Boolean, Ordering> f(final Boolean a1) {
         return new F<Boolean, Ordering>() {
            public Ordering f(Boolean a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Byte> byteOrd = new Ord(new F<Byte, F<Byte, Ordering>>() {
      public F<Byte, Ordering> f(final Byte a1) {
         return new F<Byte, Ordering>() {
            public Ordering f(Byte a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Character> charOrd = new Ord(new F<Character, F<Character, Ordering>>() {
      public F<Character, Ordering> f(final Character a1) {
         return new F<Character, Ordering>() {
            public Ordering f(Character a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Double> doubleOrd = new Ord(new F<Double, F<Double, Ordering>>() {
      public F<Double, Ordering> f(final Double a1) {
         return new F<Double, Ordering>() {
            public Ordering f(Double a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Float> floatOrd = new Ord(new F<Float, F<Float, Ordering>>() {
      public F<Float, Ordering> f(final Float a1) {
         return new F<Float, Ordering>() {
            public Ordering f(Float a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Integer> intOrd = new Ord(new F<Integer, F<Integer, Ordering>>() {
      public F<Integer, Ordering> f(final Integer a1) {
         return new F<Integer, Ordering>() {
            public Ordering f(Integer a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<BigInteger> bigintOrd = new Ord(new F<BigInteger, F<BigInteger, Ordering>>() {
      public F<BigInteger, Ordering> f(final BigInteger a1) {
         return new F<BigInteger, Ordering>() {
            public Ordering f(BigInteger a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<BigDecimal> bigdecimalOrd = new Ord(new F<BigDecimal, F<BigDecimal, Ordering>>() {
      public F<BigDecimal, Ordering> f(final BigDecimal a1) {
         return new F<BigDecimal, Ordering>() {
            public Ordering f(BigDecimal a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Long> longOrd = new Ord(new F<Long, F<Long, Ordering>>() {
      public F<Long, Ordering> f(final Long a1) {
         return new F<Long, Ordering>() {
            public Ordering f(Long a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Short> shortOrd = new Ord(new F<Short, F<Short, Ordering>>() {
      public F<Short, Ordering> f(final Short a1) {
         return new F<Short, Ordering>() {
            public Ordering f(Short a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<Ordering> orderingOrd = new Ord(Function.curry(new F2<Ordering, Ordering, Ordering>() {
      public Ordering f(Ordering o1, Ordering o2) {
         return o1 == o2 ? Ordering.EQ : (o1 == Ordering.LT ? Ordering.LT : (o2 == Ordering.LT ? Ordering.GT : (o1 == Ordering.EQ ? Ordering.LT : Ordering.GT)));
      }
   }));
   public static final Ord<String> stringOrd = new Ord(new F<String, F<String, Ordering>>() {
      public F<String, Ordering> f(final String a1) {
         return new F<String, Ordering>() {
            public Ordering f(String a2) {
               int x = a1.compareTo(a2);
               return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
            }
         };
      }
   });
   public static final Ord<StringBuffer> stringBufferOrd = new Ord(new F<StringBuffer, F<StringBuffer, Ordering>>() {
      public F<StringBuffer, Ordering> f(final StringBuffer a1) {
         return new F<StringBuffer, Ordering>() {
            public Ordering f(StringBuffer a2) {
               return Ord.stringOrd.compare(a1.toString(), a2.toString());
            }
         };
      }
   });
   public static final Ord<StringBuilder> stringBuilderOrd = new Ord(new F<StringBuilder, F<StringBuilder, Ordering>>() {
      public F<StringBuilder, Ordering> f(final StringBuilder a1) {
         return new F<StringBuilder, Ordering>() {
            public Ordering f(StringBuilder a2) {
               return Ord.stringOrd.compare(a1.toString(), a2.toString());
            }
         };
      }
   });
   public static final Ord<Unit> unitOrd = ord(Function.curry(new F2<Unit, Unit, Ordering>() {
      public Ordering f(Unit u1, Unit u2) {
         return Ordering.EQ;
      }
   }));
   public static final Ord<Natural> naturalOrd;

   private Ord(F<A, F<A, Ordering>> f) {
      this.f = f;
   }

   public F<A, F<A, Ordering>> compare() {
      return this.f;
   }

   public Ordering compare(A a1, A a2) {
      return (Ordering)((F)this.f.f(a1)).f(a2);
   }

   public boolean eq(A a1, A a2) {
      return this.compare(a1, a2) == Ordering.EQ;
   }

   public Equal<A> equal() {
      return Equal.equal(Function.curry(new F2<A, A, Boolean>() {
         public Boolean f(A a1, A a2) {
            return Ord.this.eq(a1, a2);
         }
      }));
   }

   public <B> Ord<B> comap(F<B, A> f) {
      return ord(F1Functions.o(F1Functions.o(F1Functions.andThen(f), this.f), f));
   }

   public boolean isLessThan(A a1, A a2) {
      return this.compare(a1, a2) == Ordering.LT;
   }

   public boolean isGreaterThan(A a1, A a2) {
      return this.compare(a1, a2) == Ordering.GT;
   }

   public F<A, Boolean> isLessThan(final A a) {
      return new F<A, Boolean>() {
         public Boolean f(A a2) {
            return Ord.this.compare(a2, a) == Ordering.LT;
         }
      };
   }

   public F<A, Boolean> isGreaterThan(final A a) {
      return new F<A, Boolean>() {
         public Boolean f(A a2) {
            return Ord.this.compare(a2, a) == Ordering.GT;
         }
      };
   }

   public A max(A a1, A a2) {
      return this.isGreaterThan(a1, a2) ? a1 : a2;
   }

   public A min(A a1, A a2) {
      return this.isLessThan(a1, a2) ? a1 : a2;
   }

   public static <A> Ord<A> ord(F<A, F<A, Ordering>> f) {
      return new Ord(f);
   }

   public static <A> Ord<Option<A>> optionOrd(final Ord<A> oa) {
      return new Ord(new F<Option<A>, F<Option<A>, Ordering>>() {
         public F<Option<A>, Ordering> f(final Option<A> o1) {
            return new F<Option<A>, Ordering>() {
               public Ordering f(Option<A> o2) {
                  return o1.isNone() ? (o2.isNone() ? Ordering.EQ : Ordering.LT) : (o2.isNone() ? Ordering.GT : (Ordering)((F)oa.f.f(o1.some())).f(o2.some()));
               }
            };
         }
      });
   }

   public static <A, B> Ord<Either<A, B>> eitherOrd(final Ord<A> oa, final Ord<B> ob) {
      return new Ord(new F<Either<A, B>, F<Either<A, B>, Ordering>>() {
         public F<Either<A, B>, Ordering> f(final Either<A, B> e1) {
            return new F<Either<A, B>, Ordering>() {
               public Ordering f(Either<A, B> e2) {
                  return e1.isLeft() ? (e2.isLeft() ? (Ordering)((F)oa.f.f(e1.left().value())).f(e2.left().value()) : Ordering.LT) : (e2.isLeft() ? Ordering.GT : (Ordering)((F)ob.f.f(e1.right().value())).f(e2.right().value()));
               }
            };
         }
      });
   }

   public static <A, B> Ord<Validation<A, B>> validationOrd(Ord<A> oa, Ord<B> ob) {
      return eitherOrd(oa, ob).comap(Validation.either());
   }

   public static <A> Ord<List<A>> listOrd(final Ord<A> oa) {
      return new Ord(new F<List<A>, F<List<A>, Ordering>>() {
         public F<List<A>, Ordering> f(final List<A> l1) {
            return new F<List<A>, Ordering>() {
               public Ordering f(List<A> l2) {
                  if (l1.isEmpty()) {
                     return l2.isEmpty() ? Ordering.EQ : Ordering.LT;
                  } else if (l2.isEmpty()) {
                     return l1.isEmpty() ? Ordering.EQ : Ordering.GT;
                  } else {
                     Ordering c = oa.compare(l1.head(), l2.head());
                     return c == Ordering.EQ ? (Ordering)((F)Ord.listOrd(oa).f.f(l1.tail())).f(l2.tail()) : c;
                  }
               }
            };
         }
      });
   }

   public static <A> Ord<NonEmptyList<A>> nonEmptyListOrd(Ord<A> oa) {
      return listOrd(oa).comap(NonEmptyList.toList_());
   }

   public static <A> Ord<Stream<A>> streamOrd(final Ord<A> oa) {
      return new Ord(new F<Stream<A>, F<Stream<A>, Ordering>>() {
         public F<Stream<A>, Ordering> f(final Stream<A> s1) {
            return new F<Stream<A>, Ordering>() {
               public Ordering f(Stream<A> s2) {
                  if (s1.isEmpty()) {
                     return s2.isEmpty() ? Ordering.EQ : Ordering.LT;
                  } else if (s2.isEmpty()) {
                     return s1.isEmpty() ? Ordering.EQ : Ordering.GT;
                  } else {
                     Ordering c = oa.compare(s1.head(), s2.head());
                     return c == Ordering.EQ ? (Ordering)((F)Ord.streamOrd(oa).f.f(s1.tail()._1())).f(s2.tail()._1()) : c;
                  }
               }
            };
         }
      });
   }

   public static <A> Ord<Array<A>> arrayOrd(final Ord<A> oa) {
      return new Ord(new F<Array<A>, F<Array<A>, Ordering>>() {
         public F<Array<A>, Ordering> f(final Array<A> a1) {
            return new F<Array<A>, Ordering>() {
               public Ordering f(Array<A> a2) {
                  int i = 0;

                  while(true) {
                     if (i < a1.length() && i < a2.length()) {
                        Ordering c = oa.compare(a1.get(i), a2.get(i));
                        if (c != Ordering.GT && c != Ordering.LT) {
                           ++i;
                           continue;
                        }

                        return c;
                     }

                     return i == a1.length() ? (i == a2.length() ? Ordering.EQ : Ordering.LT) : (i == a1.length() ? Ordering.EQ : Ordering.GT);
                  }
               }
            };
         }
      });
   }

   public static <A> Ord<Set<A>> setOrd(Ord<A> oa) {
      return streamOrd(oa).comap(new F<Set<A>, Stream<A>>() {
         public Stream<A> f(Set<A> as) {
            return as.toStream();
         }
      });
   }

   public static <A> Ord<P1<A>> p1Ord(Ord<A> oa) {
      return oa.comap(P1.__1());
   }

   public static <A, B> Ord<P2<A, B>> p2Ord(final Ord<A> oa, final Ord<B> ob) {
      return ord(Function.curry(new F2<P2<A, B>, P2<A, B>, Ordering>() {
         public Ordering f(P2<A, B> a, P2<A, B> b) {
            return oa.eq(a._1(), b._1()) ? ob.compare(a._2(), b._2()) : oa.compare(a._1(), b._1());
         }
      }));
   }

   public static <A, B, C> Ord<P3<A, B, C>> p3Ord(final Ord<A> oa, final Ord<B> ob, final Ord<C> oc) {
      return ord(Function.curry(new F2<P3<A, B, C>, P3<A, B, C>, Ordering>() {
         public Ordering f(P3<A, B, C> a, P3<A, B, C> b) {
            return oa.eq(a._1(), b._1()) ? Ord.p2Ord(ob, oc).compare(P.p(a._2(), a._3()), P.p(b._2(), b._3())) : oa.compare(a._1(), b._1());
         }
      }));
   }

   public static <A extends Comparable<A>> Ord<A> comparableOrd() {
      return ord(new F<A, F<A, Ordering>>() {
         public F<A, Ordering> f(final A a1) {
            return new F<A, Ordering>() {
               public Ordering f(A a2) {
                  int x = a1.compareTo(a2);
                  return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
               }
            };
         }
      });
   }

   public static <A> Ord<A> hashOrd() {
      return ord(new F<A, F<A, Ordering>>() {
         public F<A, Ordering> f(final A a) {
            return new F<A, Ordering>() {
               public Ordering f(A a2) {
                  int x = a.hashCode() - a2.hashCode();
                  return x < 0 ? Ordering.LT : (x == 0 ? Ordering.EQ : Ordering.GT);
               }
            };
         }
      });
   }

   public static <A> Ord<A> hashEqualsOrd() {
      return ord(new F<A, F<A, Ordering>>() {
         public F<A, Ordering> f(final A a) {
            return new F<A, Ordering>() {
               public Ordering f(A a2) {
                  int x = a.hashCode() - a2.hashCode();
                  return x < 0 ? Ordering.LT : (x == 0 && a.equals(a2) ? Ordering.EQ : Ordering.GT);
               }
            };
         }
      });
   }

   static {
      naturalOrd = bigintOrd.comap(Natural.bigIntegerValue);
   }
}
