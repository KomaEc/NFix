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
import fj.P;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;

public final class Property {
   private final F<Integer, F<Rand, Result>> f;

   private Property(F<Integer, F<Rand, Result>> f) {
      this.f = f;
   }

   public Result prop(int i, Rand r) {
      return (Result)((F)this.f.f(i)).f(r);
   }

   public Gen<Result> gen() {
      return Gen.gen(new F<Integer, F<Rand, Result>>() {
         public F<Rand, Result> f(final Integer i) {
            return new F<Rand, Result>() {
               public Result f(Rand r) {
                  return (Result)((F)Property.this.f.f(i)).f(r);
               }
            };
         }
      });
   }

   public Property and(Property p) {
      return fromGen(this.gen().bind(p.gen(), new F<Result, F<Result, Result>>() {
         public F<Result, Result> f(final Result res1) {
            return new F<Result, Result>() {
               public Result f(Result res2) {
                  return !res1.isException() && !res1.isFalsified() ? (!res2.isException() && !res2.isFalsified() ? (!res1.isProven() && !res1.isUnfalsified() ? (!res2.isProven() && !res2.isUnfalsified() ? Result.noResult() : res1) : res2) : res2) : res1;
               }
            };
         }
      }));
   }

   public Property or(Property p) {
      return fromGen(this.gen().bind(p.gen(), new F<Result, F<Result, Result>>() {
         public F<Result, Result> f(final Result res1) {
            return new F<Result, Result>() {
               public Result f(Result res2) {
                  return !res1.isException() && !res1.isFalsified() ? (!res2.isException() && !res2.isFalsified() ? (!res1.isProven() && !res1.isUnfalsified() ? (!res2.isProven() && !res2.isUnfalsified() ? Result.noResult() : res2) : res1) : res2) : res1;
               }
            };
         }
      }));
   }

   public Property sequence(Property p) {
      return fromGen(this.gen().bind(p.gen(), new F<Result, F<Result, Result>>() {
         public F<Result, Result> f(final Result res1) {
            return new F<Result, Result>() {
               public Result f(Result res2) {
                  return !res1.isException() && !res1.isProven() && !res1.isUnfalsified() ? (!res2.isException() && !res2.isProven() && !res2.isUnfalsified() ? (res1.isFalsified() ? res2 : (res2.isFalsified() ? res1 : Result.noResult())) : res2) : res1;
               }
            };
         }
      }));
   }

   public CheckResult check(Rand r, int minSuccessful, int maxDiscarded, int minSize, int maxSize) {
      int s = 0;
      int d = 0;
      float sz = (float)minSize;

      CheckResult res;
      while(true) {
         float size = s == 0 && d == 0 ? (float)minSize : sz + ((float)maxSize - sz) / (float)(minSuccessful - s);

         try {
            Result x = (Result)((F)this.f.f(Math.round(size))).f(r);
            if (x.isNoResult()) {
               if (d + 1 >= maxDiscarded) {
                  res = CheckResult.exhausted(s, d + 1);
                  break;
               }

               sz = size;
               ++d;
            } else {
               if (x.isProven()) {
                  res = CheckResult.proven((List)x.args().some(), s + 1, d);
                  break;
               }

               if (x.isUnfalsified()) {
                  if (s + 1 >= minSuccessful) {
                     res = CheckResult.passed(s + 1, d);
                     break;
                  }

                  sz = size;
                  ++s;
               } else {
                  if (x.isFalsified()) {
                     res = CheckResult.falsified((List)x.args().some(), s, d);
                     break;
                  }

                  if (x.isException()) {
                     res = CheckResult.propException((List)x.args().some(), (Throwable)x.exception().some(), s, d);
                     break;
                  }
               }
            }
         } catch (Throwable var12) {
            res = CheckResult.genException(var12, s, d);
            break;
         }
      }

      return res;
   }

   public CheckResult check(int minSuccessful, int maxDiscarded, int minSize, int maxSize) {
      return this.check(Rand.standard, minSuccessful, maxDiscarded, minSize, maxSize);
   }

   public CheckResult check(Rand r) {
      return this.check(r, 100, 500, 0, 100);
   }

   public CheckResult check(Rand r, int minSize, int maxSize) {
      return this.check(r, 100, 500, minSize, maxSize);
   }

   public CheckResult check(int minSize, int maxSize) {
      return this.check(100, 500, minSize, maxSize);
   }

   public CheckResult check() {
      return this.check(0, 100);
   }

   public CheckResult minSuccessful(int minSuccessful) {
      return this.check(minSuccessful, 500, 0, 100);
   }

   public CheckResult minSuccessful(Rand r, int minSuccessful) {
      return this.check(r, minSuccessful, 500, 0, 100);
   }

   public CheckResult maxDiscarded(int maxDiscarded) {
      return this.check(100, maxDiscarded, 0, 100);
   }

   public CheckResult maxDiscarded(Rand r, int maxDiscarded) {
      return this.check(r, 100, maxDiscarded, 0, 100);
   }

   public CheckResult minSize(int minSize) {
      return this.check(100, 500, minSize, 100);
   }

   public CheckResult minSize(Rand r, int minSize) {
      return this.check(r, 100, 500, minSize, 100);
   }

   public CheckResult maxSize(int maxSize) {
      return this.check(100, 500, 0, maxSize);
   }

   public CheckResult maxSize(Rand r, int maxSize) {
      return this.check(r, 100, 500, 0, maxSize);
   }

   public static Property implies(boolean b, P1<Property> p) {
      return b ? (Property)p._1() : new Property(new F<Integer, F<Rand, Result>>() {
         public F<Rand, Result> f(Integer i) {
            return new F<Rand, Result>() {
               public Result f(Rand r) {
                  return Result.noResult();
               }
            };
         }
      });
   }

   public static Property prop(F<Integer, F<Rand, Result>> f) {
      return new Property(f);
   }

   public static Property prop(final Result r) {
      return new Property(new F<Integer, F<Rand, Result>>() {
         public F<Rand, Result> f(Integer integer) {
            return new F<Rand, Result>() {
               public Result f(Rand x) {
                  return r;
               }
            };
         }
      });
   }

   public static Property prop(boolean b) {
      return b ? prop(Result.proven(List.nil())) : prop(Result.falsified(List.nil()));
   }

   public static Property fromGen(final Gen<Result> g) {
      return prop(new F<Integer, F<Rand, Result>>() {
         public F<Rand, Result> f(final Integer i) {
            return new F<Rand, Result>() {
               public Result f(Rand r) {
                  return (Result)g.gen(i, r);
               }
            };
         }
      });
   }

   public static <A> Property forall(final Gen<A> g, final Shrink<A> shrink, final F<A, P1<Property>> f) {
      return prop(new F<Integer, F<Rand, Result>>() {
         public F<Rand, Result> f(final Integer i) {
            return new F<Rand, Result>() {
               public Result f(final Rand r) {
                  final class Util {
                     Option<P2<A, Result>> first(Stream<A> as, final int shrinks) {
                        final Stream<Option<P2<A, Result>>> results = as.map(new F<A, Option<P2<A, Result>>>() {
                           public Option<P2<A, Result>> f(final A a) {
                              Result result = Property.exception((P1)f.f(a)).prop(i, r);
                              return result.toOption().map(new F<Result, P2<A, Result>>() {
                                 public P2<A, Result> f(Result result) {
                                    return P.p(a, result.provenAsUnfalsified().addArg(Arg.arg(a, shrinks)));
                                 }
                              });
                           }
                        });
                        return results.isEmpty() ? Option.none() : (Option)results.find(new F<Option<P2<A, Result>>, Boolean>() {
                           public Boolean f(Option<P2<A, Result>> o) {
                              return Util.this.failed(o);
                           }
                        }).orSome(new P1<Option<P2<A, Result>>>() {
                           public Option<P2<A, Result>> _1() {
                              return (Option)results.head();
                           }
                        });
                     }

                     public boolean failed(Option<P2<A, Result>> o) {
                        return o.isSome() && ((Result)((P2)o.some())._2()).failed();
                     }
                  }

                  Util u = new Util();
                  Option<P2<A, Result>> x = u.first(Stream.single(g.gen(i, r)), 0);
                  F<P2<A, Result>, Result> __2 = P2.__2();
                  if (!u.failed(x)) {
                     return Result.noResult(x.map(__2));
                  } else {
                     int shrinks = 0;

                     Option or;
                     do {
                        ++shrinks;
                        or = x.map(__2);
                        x = u.first(shrink.shrink(((P2)x.some())._1()), shrinks);
                     } while(u.failed(x));

                     return Result.noResult(or);
                  }
               }
            };
         }
      });
   }

   public static <A> Property propertyP(Arbitrary<A> aa, Shrink<A> sa, F<A, P1<Property>> f) {
      return forall(aa.gen, sa, f);
   }

   public static <A> Property property(Arbitrary<A> aa, Shrink<A> sa, F<A, Property> f) {
      return propertyP(aa, sa, P1.curry(f));
   }

   public static <A> Property propertyP(Arbitrary<A> aa, F<A, P1<Property>> f) {
      return propertyP(aa, Shrink.empty(), f);
   }

   public static <A> Property property(Arbitrary<A> aa, F<A, Property> f) {
      return propertyP(aa, P1.curry(f));
   }

   public static <A, B> Property propertyP(Arbitrary<A> aa, final Arbitrary<B> ab, Shrink<A> sa, final Shrink<B> sb, final F<A, F<B, P1<Property>>> f) {
      return property(aa, sa, new F<A, Property>() {
         public Property f(final A a) {
            return Property.propertyP(ab, sb, new F<B, P1<Property>>() {
               public P1<Property> f(B b) {
                  return (P1)((F)f.f(a)).f(b);
               }
            });
         }
      });
   }

   public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F<A, F<B, Property>> f) {
      return propertyP(aa, ab, sa, sb, Function.compose2(P.p1(), f));
   }

   public static <A, B> Property propertyP(Arbitrary<A> aa, final Arbitrary<B> ab, final F<A, F<B, P1<Property>>> f) {
      return property(aa, new F<A, Property>() {
         public Property f(final A a) {
            return Property.propertyP(ab, new F<B, P1<Property>>() {
               public P1<Property> f(B b) {
                  return (P1)((F)f.f(a)).f(b);
               }
            });
         }
      });
   }

   public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, F<A, F<B, Property>> f) {
      return propertyP(aa, ab, Function.compose2(P.p1(), f));
   }

   public static <A, B> Property propertyP(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F2<A, B, P1<Property>> f) {
      return propertyP(aa, ab, sa, sb, Function.curry(f));
   }

   public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Shrink<A> sa, Shrink<B> sb, F2<A, B, Property> f) {
      return propertyP(aa, ab, sa, sb, Function.compose2(P.p1(), Function.curry(f)));
   }

   public static <A, B> Property propertyP(Arbitrary<A> aa, Arbitrary<B> ab, F2<A, B, P1<Property>> f) {
      return propertyP(aa, ab, Function.curry(f));
   }

   public static <A, B> Property property(Arbitrary<A> aa, Arbitrary<B> ab, F2<A, B, Property> f) {
      return propertyP(aa, ab, Function.compose2(P.p1(), Function.curry(f)));
   }

   public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, final Arbitrary<C> ac, Shrink<A> sa, Shrink<B> sb, final Shrink<C> sc, final F<A, F<B, F<C, Property>>> f) {
      return property(aa, ab, sa, sb, new F<A, F<B, Property>>() {
         public F<B, Property> f(final A a) {
            return new F<B, Property>() {
               public Property f(final B b) {
                  return Property.property(ac, sc, new F<C, Property>() {
                     public Property f(C c) {
                        return (Property)((F)((F)f.f(a)).f(b)).f(c);
                     }
                  });
               }
            };
         }
      });
   }

   public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, final Arbitrary<C> ac, final F<A, F<B, F<C, Property>>> f) {
      return property(aa, ab, new F<A, F<B, Property>>() {
         public F<B, Property> f(final A a) {
            return new F<B, Property>() {
               public Property f(final B b) {
                  return Property.property(ac, new F<C, Property>() {
                     public Property f(C c) {
                        return (Property)((F)((F)f.f(a)).f(b)).f(c);
                     }
                  });
               }
            };
         }
      });
   }

   public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, F3<A, B, C, Property> f) {
      return property(aa, ab, ac, sa, sb, sc, Function.curry(f));
   }

   public static <A, B, C> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, F3<A, B, C, Property> f) {
      return property(aa, ab, ac, Function.curry(f));
   }

   public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, final Arbitrary<D> ad, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, final Shrink<D> sd, final F<A, F<B, F<C, F<D, Property>>>> f) {
      return property(aa, ab, ac, sa, sb, sc, new F<A, F<B, F<C, Property>>>() {
         public F<B, F<C, Property>> f(final A a) {
            return new F<B, F<C, Property>>() {
               public F<C, Property> f(final B b) {
                  return new F<C, Property>() {
                     public Property f(final C c) {
                        return Property.property(ad, sd, new F<D, Property>() {
                           public Property f(D d) {
                              return (Property)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d);
                           }
                        });
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, final Arbitrary<D> ad, final F<A, F<B, F<C, F<D, Property>>>> f) {
      return property(aa, ab, ac, new F<A, F<B, F<C, Property>>>() {
         public F<B, F<C, Property>> f(final A a) {
            return new F<B, F<C, Property>>() {
               public F<C, Property> f(final B b) {
                  return new F<C, Property>() {
                     public Property f(final C c) {
                        return Property.property(ad, new F<D, Property>() {
                           public Property f(D d) {
                              return (Property)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d);
                           }
                        });
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, F4<A, B, C, D, Property> f) {
      return property(aa, ab, ac, ad, sa, sb, sc, sd, Function.curry(f));
   }

   public static <A, B, C, D> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, F4<A, B, C, D, Property> f) {
      return property(aa, ab, ac, ad, Function.curry(f));
   }

   public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, final Arbitrary<E> ae, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, final Shrink<E> se, final F<A, F<B, F<C, F<D, F<E, Property>>>>> f) {
      return property(aa, ab, ac, ad, sa, sb, sc, sd, new F<A, F<B, F<C, F<D, Property>>>>() {
         public F<B, F<C, F<D, Property>>> f(final A a) {
            return new F<B, F<C, F<D, Property>>>() {
               public F<C, F<D, Property>> f(final B b) {
                  return new F<C, F<D, Property>>() {
                     public F<D, Property> f(final C c) {
                        return new F<D, Property>() {
                           public Property f(final D d) {
                              return Property.property(ae, se, new F<E, Property>() {
                                 public Property f(E e) {
                                    return (Property)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e);
                                 }
                              });
                           }
                        };
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, final Arbitrary<E> ae, final F<A, F<B, F<C, F<D, F<E, Property>>>>> f) {
      return property(aa, ab, ac, ad, new F<A, F<B, F<C, F<D, Property>>>>() {
         public F<B, F<C, F<D, Property>>> f(final A a) {
            return new F<B, F<C, F<D, Property>>>() {
               public F<C, F<D, Property>> f(final B b) {
                  return new F<C, F<D, Property>>() {
                     public F<D, Property> f(final C c) {
                        return new F<D, Property>() {
                           public Property f(final D d) {
                              return Property.property(ae, new F<E, Property>() {
                                 public Property f(E e) {
                                    return (Property)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e);
                                 }
                              });
                           }
                        };
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, F5<A, B, C, D, E, Property> f) {
      return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, Function.curry(f));
   }

   public static <A, B, C, D, E> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, F5<A, B, C, D, E, Property> f) {
      return property(aa, ab, ac, ad, ae, Function.curry(f));
   }

   public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, final Arbitrary<F$> af, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, final Shrink<F$> sf, final F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> f) {
      return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, new F<A, F<B, F<C, F<D, F<E, Property>>>>>() {
         public F<B, F<C, F<D, F<E, Property>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, Property>>>>() {
               public F<C, F<D, F<E, Property>>> f(final B b) {
                  return new F<C, F<D, F<E, Property>>>() {
                     public F<D, F<E, Property>> f(final C c) {
                        return new F<D, F<E, Property>>() {
                           public F<E, Property> f(final D d) {
                              return new F<E, Property>() {
                                 public Property f(final E e) {
                                    return Property.property(af, sf, new F<F$, Property>() {
                                       public Property f(F$ f$) {
                                          return (Property)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
                                       }
                                    });
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, final Arbitrary<F$> af, final F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>> f) {
      return property(aa, ab, ac, ad, ae, new F<A, F<B, F<C, F<D, F<E, Property>>>>>() {
         public F<B, F<C, F<D, F<E, Property>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, Property>>>>() {
               public F<C, F<D, F<E, Property>>> f(final B b) {
                  return new F<C, F<D, F<E, Property>>>() {
                     public F<D, F<E, Property>> f(final C c) {
                        return new F<D, F<E, Property>>() {
                           public F<E, Property> f(final D d) {
                              return new F<E, Property>() {
                                 public Property f(final E e) {
                                    return Property.property(af, new F<F$, Property>() {
                                       public Property f(F$ f$) {
                                          return (Property)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
                                       }
                                    });
                                 }
                              };
                           }
                        };
                     }
                  };
               }
            };
         }
      });
   }

   public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, F6<A, B, C, D, E, F$, Property> f) {
      return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, Function.curry(f));
   }

   public static <A, B, C, D, E, F$> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, F6<A, B, C, D, E, F$, Property> f) {
      return property(aa, ab, ac, ad, ae, af, Function.curry(f));
   }

   public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, final Arbitrary<G> ag, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, final Shrink<G> sg, final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> f) {
      return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, new F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, Property>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, Property>>>>>() {
               public F<C, F<D, F<E, F<F$, Property>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, Property>>>>() {
                     public F<D, F<E, F<F$, Property>>> f(final C c) {
                        return new F<D, F<E, F<F$, Property>>>() {
                           public F<E, F<F$, Property>> f(final D d) {
                              return new F<E, F<F$, Property>>() {
                                 public F<F$, Property> f(final E e) {
                                    return new F<F$, Property>() {
                                       public Property f(final F$ f$) {
                                          return Property.property(ag, sg, new F<G, Property>() {
                                             public Property f(G g) {
                                                return (Property)((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
                                             }
                                          });
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
      });
   }

   public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, final Arbitrary<G> ag, final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>> f) {
      return property(aa, ab, ac, ad, ae, af, new F<A, F<B, F<C, F<D, F<E, F<F$, Property>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, Property>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, Property>>>>>() {
               public F<C, F<D, F<E, F<F$, Property>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, Property>>>>() {
                     public F<D, F<E, F<F$, Property>>> f(final C c) {
                        return new F<D, F<E, F<F$, Property>>>() {
                           public F<E, F<F$, Property>> f(final D d) {
                              return new F<E, F<F$, Property>>() {
                                 public F<F$, Property> f(final E e) {
                                    return new F<F$, Property>() {
                                       public Property f(final F$ f$) {
                                          return Property.property(ag, new F<G, Property>() {
                                             public Property f(G g) {
                                                return (Property)((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
                                             }
                                          });
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
      });
   }

   public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, F7<A, B, C, D, E, F$, G, Property> f) {
      return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, Function.curry(f));
   }

   public static <A, B, C, D, E, F$, G> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, F7<A, B, C, D, E, F$, G, Property> f) {
      return property(aa, ab, ac, ad, ae, af, ag, Function.curry(f));
   }

   public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, final Arbitrary<H> ah, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, final Shrink<H> sh, final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, Property>>>>>>>> f) {
      return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, Property>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, Property>>>>>() {
                     public F<D, F<E, F<F$, F<G, Property>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, Property>>>>() {
                           public F<E, F<F$, F<G, Property>>> f(final D d) {
                              return new F<E, F<F$, F<G, Property>>>() {
                                 public F<F$, F<G, Property>> f(final E e) {
                                    return new F<F$, F<G, Property>>() {
                                       public F<G, Property> f(final F$ f$) {
                                          return new F<G, Property>() {
                                             public Property f(final G g) {
                                                return Property.property(ah, sh, new F<H, Property>() {
                                                   public Property f(H h) {
                                                      return (Property)((F)((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g)).f(h);
                                                   }
                                                });
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
      });
   }

   public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, final Arbitrary<H> ah, final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, Property>>>>>>>> f) {
      return property(aa, ab, ac, ad, ae, af, ag, new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, Property>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, Property>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, Property>>>>>() {
                     public F<D, F<E, F<F$, F<G, Property>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, Property>>>>() {
                           public F<E, F<F$, F<G, Property>>> f(final D d) {
                              return new F<E, F<F$, F<G, Property>>>() {
                                 public F<F$, F<G, Property>> f(final E e) {
                                    return new F<F$, F<G, Property>>() {
                                       public F<G, Property> f(final F$ f$) {
                                          return new F<G, Property>() {
                                             public Property f(final G g) {
                                                return Property.property(ah, new F<H, Property>() {
                                                   public Property f(H h) {
                                                      return (Property)((F)((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g)).f(h);
                                                   }
                                                });
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
      });
   }

   public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, Shrink<A> sa, Shrink<B> sb, Shrink<C> sc, Shrink<D> sd, Shrink<E> se, Shrink<F$> sf, Shrink<G> sg, Shrink<H> sh, F8<A, B, C, D, E, F$, G, H, Property> f) {
      return property(aa, ab, ac, ad, ae, af, ag, ah, sa, sb, sc, sd, se, sf, sg, sh, Function.curry(f));
   }

   public static <A, B, C, D, E, F$, G, H> Property property(Arbitrary<A> aa, Arbitrary<B> ab, Arbitrary<C> ac, Arbitrary<D> ad, Arbitrary<E> ae, Arbitrary<F$> af, Arbitrary<G> ag, Arbitrary<H> ah, F8<A, B, C, D, E, F$, G, H, Property> f) {
      return property(aa, ab, ac, ad, ae, af, ag, ah, Function.curry(f));
   }

   public static Property exception(P1<Property> p) {
      try {
         return (Property)p._1();
      } catch (final Throwable var2) {
         return new Property(new F<Integer, F<Rand, Result>>() {
            public F<Rand, Result> f(Integer i) {
               return new F<Rand, Result>() {
                  public Result f(Rand r) {
                     return Result.exception(List.nil(), var2);
                  }
               };
            }
         });
      }
   }
}
