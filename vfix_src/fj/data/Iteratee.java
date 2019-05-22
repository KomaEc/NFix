package fj.data;

import fj.F;
import fj.F1Functions;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;

public final class Iteratee {
   private Iteratee() {
      throw new UnsupportedOperationException();
   }

   public abstract static class IterV<E, A> {
      IterV() {
      }

      public static <E, A> Iteratee.IterV<E, A> cont(final F<Iteratee.Input<E>, Iteratee.IterV<E, A>> f) {
         return new Iteratee.IterV<E, A>() {
            public <Z> Z fold(F<P2<A, Iteratee.Input<E>>, Z> done, F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Z> cont) {
               return cont.f(f);
            }
         };
      }

      public abstract <Z> Z fold(F<P2<A, Iteratee.Input<E>>, Z> var1, F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Z> var2);

      public static <E, A> Iteratee.IterV<E, A> done(A a, Iteratee.Input<E> i) {
         final P2<A, Iteratee.Input<E>> p = P.p(a, i);
         return new Iteratee.IterV<E, A>() {
            public <Z> Z fold(F<P2<A, Iteratee.Input<E>>, Z> done, F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Z> cont) {
               return done.f(p);
            }
         };
      }

      public final A run() {
         final F<Iteratee.IterV<E, A>, Option<A>> runCont = new F<Iteratee.IterV<E, A>, Option<A>>() {
            final F<P2<A, Iteratee.Input<E>>, Option<A>> done = F1Functions.andThen(P2.__1(), Option.some_());
            final F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Option<A>> cont = Function.constant(Option.none());

            public Option<A> f(Iteratee.IterV<E, A> i) {
               return (Option)i.fold(this.done, this.cont);
            }
         };
         F<P2<A, Iteratee.Input<E>>, A> done = P2.__1();
         F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, A> cont = new F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, A>() {
            public A f(F<Iteratee.Input<E>, Iteratee.IterV<E, A>> k) {
               return ((Option)runCont.f(k.f(Iteratee.Input.eof()))).valueE("diverging iteratee");
            }
         };
         return this.fold(done, cont);
      }

      public final <B> Iteratee.IterV<E, B> bind(final F<A, Iteratee.IterV<E, B>> f) {
         F<P2<A, Iteratee.Input<E>>, Iteratee.IterV<E, B>> done = new F<P2<A, Iteratee.Input<E>>, Iteratee.IterV<E, B>>() {
            public Iteratee.IterV<E, B> f(P2<A, Iteratee.Input<E>> xe) {
               final Iteratee.Input<E> e = (Iteratee.Input)xe._2();
               F<P2<B, Iteratee.Input<E>>, Iteratee.IterV<E, B>> done = new F<P2<B, Iteratee.Input<E>>, Iteratee.IterV<E, B>>() {
                  public Iteratee.IterV<E, B> f(P2<B, Iteratee.Input<E>> y_) {
                     B y = y_._1();
                     return Iteratee.IterV.done(y, e);
                  }
               };
               F<F<Iteratee.Input<E>, Iteratee.IterV<E, B>>, Iteratee.IterV<E, B>> cont = new F<F<Iteratee.Input<E>, Iteratee.IterV<E, B>>, Iteratee.IterV<E, B>>() {
                  public Iteratee.IterV<E, B> f(F<Iteratee.Input<E>, Iteratee.IterV<E, B>> k) {
                     return (Iteratee.IterV)k.f(e);
                  }
               };
               A x = xe._1();
               return (Iteratee.IterV)((Iteratee.IterV)f.f(x)).fold(done, cont);
            }
         };
         F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Iteratee.IterV<E, B>> cont = new F<F<Iteratee.Input<E>, Iteratee.IterV<E, A>>, Iteratee.IterV<E, B>>() {
            public Iteratee.IterV<E, B> f(final F<Iteratee.Input<E>, Iteratee.IterV<E, A>> k) {
               return Iteratee.IterV.cont(new F<Iteratee.Input<E>, Iteratee.IterV<E, B>>() {
                  public Iteratee.IterV<E, B> f(Iteratee.Input<E> e) {
                     return ((Iteratee.IterV)k.f(e)).bind(f);
                  }
               });
            }
         };
         return (Iteratee.IterV)this.fold(done, cont);
      }

      public static final <E> Iteratee.IterV<E, Integer> length() {
         F<Integer, F<Iteratee.Input<E>, Iteratee.IterV<E, Integer>>> step = new F<Integer, F<Iteratee.Input<E>, Iteratee.IterV<E, Integer>>>() {
            final F<Integer, F<Iteratee.Input<E>, Iteratee.IterV<E, Integer>>> step = this;

            public F<Iteratee.Input<E>, Iteratee.IterV<E, Integer>> f(final Integer acc) {
               final P1<Iteratee.IterV<E, Integer>> empty = new P1<Iteratee.IterV<E, Integer>>() {
                  public Iteratee.IterV<E, Integer> _1() {
                     return Iteratee.IterV.cont((F)step.f(acc));
                  }
               };
               final P1<F<E, Iteratee.IterV<E, Integer>>> el = new P1<F<E, Iteratee.IterV<E, Integer>>>() {
                  public F<E, Iteratee.IterV<E, Integer>> _1() {
                     return P.p(Iteratee.IterV.cont((F)step.f(acc + 1))).constant();
                  }
               };
               final P1<Iteratee.IterV<E, Integer>> eof = new P1<Iteratee.IterV<E, Integer>>() {
                  public Iteratee.IterV<E, Integer> _1() {
                     return Iteratee.IterV.done(acc, Iteratee.Input.eof());
                  }
               };
               return new F<Iteratee.Input<E>, Iteratee.IterV<E, Integer>>() {
                  public Iteratee.IterV<E, Integer> f(Iteratee.Input<E> s) {
                     return (Iteratee.IterV)s.apply(empty, el, eof);
                  }
               };
            }
         };
         return cont((F)step.f(0));
      }

      public static final <E> Iteratee.IterV<E, Unit> drop(final int n) {
         F<Iteratee.Input<E>, Iteratee.IterV<E, Unit>> step = new F<Iteratee.Input<E>, Iteratee.IterV<E, Unit>>() {
            final F<Iteratee.Input<E>, Iteratee.IterV<E, Unit>> step = this;
            final P1<Iteratee.IterV<E, Unit>> empty = new P1<Iteratee.IterV<E, Unit>>() {
               public Iteratee.IterV<E, Unit> _1() {
                  return Iteratee.IterV.cont(step);
               }
            };
            final P1<F<E, Iteratee.IterV<E, Unit>>> el = new P1<F<E, Iteratee.IterV<E, Unit>>>() {
               public F<E, Iteratee.IterV<E, Unit>> _1() {
                  return P.p(Iteratee.IterV.drop(n - 1)).constant();
               }
            };
            final P1<Iteratee.IterV<E, Unit>> eof = new P1<Iteratee.IterV<E, Unit>>() {
               public Iteratee.IterV<E, Unit> _1() {
                  return Iteratee.IterV.done(Unit.unit(), Iteratee.Input.eof());
               }
            };

            public Iteratee.IterV<E, Unit> f(Iteratee.Input<E> s) {
               return (Iteratee.IterV)s.apply(this.empty, this.el, this.eof);
            }
         };
         return n == 0 ? done(Unit.unit(), Iteratee.Input.empty()) : cont(step);
      }

      public static final <E> Iteratee.IterV<E, Option<E>> head() {
         F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>> step = new F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>>() {
            final F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>> step = this;
            final P1<Iteratee.IterV<E, Option<E>>> empty = new P1<Iteratee.IterV<E, Option<E>>>() {
               public Iteratee.IterV<E, Option<E>> _1() {
                  return Iteratee.IterV.cont(step);
               }
            };
            final P1<F<E, Iteratee.IterV<E, Option<E>>>> el = new P1<F<E, Iteratee.IterV<E, Option<E>>>>() {
               public F<E, Iteratee.IterV<E, Option<E>>> _1() {
                  return new F<E, Iteratee.IterV<E, Option<E>>>() {
                     public Iteratee.IterV<E, Option<E>> f(E e) {
                        return Iteratee.IterV.done(Option.some(e), Iteratee.Input.empty());
                     }
                  };
               }
            };
            final P1<Iteratee.IterV<E, Option<E>>> eof = new P1<Iteratee.IterV<E, Option<E>>>() {
               public Iteratee.IterV<E, Option<E>> _1() {
                  return Iteratee.IterV.done(Option.none(), Iteratee.Input.eof());
               }
            };

            public Iteratee.IterV<E, Option<E>> f(Iteratee.Input<E> s) {
               return (Iteratee.IterV)s.apply(this.empty, this.el, this.eof);
            }
         };
         return cont(step);
      }

      public static final <E> Iteratee.IterV<E, Option<E>> peek() {
         F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>> step = new F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>>() {
            final F<Iteratee.Input<E>, Iteratee.IterV<E, Option<E>>> step = this;
            final P1<Iteratee.IterV<E, Option<E>>> empty = new P1<Iteratee.IterV<E, Option<E>>>() {
               public Iteratee.IterV<E, Option<E>> _1() {
                  return Iteratee.IterV.cont(step);
               }
            };
            final P1<F<E, Iteratee.IterV<E, Option<E>>>> el = new P1<F<E, Iteratee.IterV<E, Option<E>>>>() {
               public F<E, Iteratee.IterV<E, Option<E>>> _1() {
                  return new F<E, Iteratee.IterV<E, Option<E>>>() {
                     public Iteratee.IterV<E, Option<E>> f(E e) {
                        return Iteratee.IterV.done(Option.some(e), Iteratee.Input.el(e));
                     }
                  };
               }
            };
            final P1<Iteratee.IterV<E, Option<E>>> eof = new P1<Iteratee.IterV<E, Option<E>>>() {
               public Iteratee.IterV<E, Option<E>> _1() {
                  return Iteratee.IterV.done(Option.none(), Iteratee.Input.eof());
               }
            };

            public Iteratee.IterV<E, Option<E>> f(Iteratee.Input<E> s) {
               return (Iteratee.IterV)s.apply(this.empty, this.el, this.eof);
            }
         };
         return cont(step);
      }

      public static final <E> Iteratee.IterV<E, List<E>> list() {
         F<List<E>, F<Iteratee.Input<E>, Iteratee.IterV<E, List<E>>>> step = new F<List<E>, F<Iteratee.Input<E>, Iteratee.IterV<E, List<E>>>>() {
            final F<List<E>, F<Iteratee.Input<E>, Iteratee.IterV<E, List<E>>>> step = this;

            public F<Iteratee.Input<E>, Iteratee.IterV<E, List<E>>> f(final List<E> acc) {
               final P1<Iteratee.IterV<E, List<E>>> empty = new P1<Iteratee.IterV<E, List<E>>>() {
                  public Iteratee.IterV<E, List<E>> _1() {
                     return Iteratee.IterV.cont((F)step.f(acc));
                  }
               };
               final P1<F<E, Iteratee.IterV<E, List<E>>>> el = new P1<F<E, Iteratee.IterV<E, List<E>>>>() {
                  public F<E, Iteratee.IterV<E, List<E>>> _1() {
                     return new F<E, Iteratee.IterV<E, List<E>>>() {
                        public Iteratee.IterV<E, List<E>> f(E e) {
                           return Iteratee.IterV.cont((F)step.f(acc.cons(e)));
                        }
                     };
                  }
               };
               final P1<Iteratee.IterV<E, List<E>>> eof = new P1<Iteratee.IterV<E, List<E>>>() {
                  public Iteratee.IterV<E, List<E>> _1() {
                     return Iteratee.IterV.done(acc, Iteratee.Input.eof());
                  }
               };
               return new F<Iteratee.Input<E>, Iteratee.IterV<E, List<E>>>() {
                  public Iteratee.IterV<E, List<E>> f(Iteratee.Input<E> s) {
                     return (Iteratee.IterV)s.apply(empty, el, eof);
                  }
               };
            }
         };
         return cont((F)step.f(List.nil()));
      }
   }

   public abstract static class Input<E> {
      Input() {
      }

      public abstract <Z> Z apply(P1<Z> var1, P1<F<E, Z>> var2, P1<Z> var3);

      public static final <E> Iteratee.Input<E> empty() {
         return new Iteratee.Input<E>() {
            public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
               return empty._1();
            }
         };
      }

      public static final <E> Iteratee.Input<E> eof() {
         return new Iteratee.Input<E>() {
            public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
               return eof._1();
            }
         };
      }

      public static final <E> Iteratee.Input<E> el(final E element) {
         return new Iteratee.Input<E>() {
            public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
               return ((F)el._1()).f(element);
            }
         };
      }
   }
}
