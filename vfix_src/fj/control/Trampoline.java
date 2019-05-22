package fj.control;

import fj.Bottom;
import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.P1;
import fj.data.Either;
import java.util.Iterator;

public abstract class Trampoline<A> {
   protected static <A, B> Trampoline.Codense<B> codense(Trampoline.Normal<A> a, F<A, Trampoline<B>> k) {
      return new Trampoline.Codense(a, k);
   }

   public static <A> F<A, Trampoline<A>> pure() {
      return new F<A, Trampoline<A>>() {
         public Trampoline<A> f(A a) {
            return Trampoline.pure(a);
         }
      };
   }

   public static <A> Trampoline<A> pure(A a) {
      return new Trampoline.Pure(a);
   }

   public static <A> Trampoline<A> suspend(P1<Trampoline<A>> a) {
      return new Trampoline.Suspend(a);
   }

   public static <A> F<P1<Trampoline<A>>, Trampoline<A>> suspend_() {
      return new F<P1<Trampoline<A>>, Trampoline<A>>() {
         public Trampoline<A> f(P1<Trampoline<A>> trampolineP1) {
            return Trampoline.suspend(trampolineP1);
         }
      };
   }

   protected abstract <R> R fold(F<Trampoline.Normal<A>, R> var1, F<Trampoline.Codense<A>, R> var2);

   public abstract <B> Trampoline<B> bind(F<A, Trampoline<B>> var1);

   public final <B> Trampoline<B> map(F<A, B> f) {
      return this.bind(F1Functions.o(pure(), f));
   }

   public static <A, B> F<F<A, Trampoline<B>>, F<Trampoline<A>, Trampoline<B>>> bind_() {
      return new F<F<A, Trampoline<B>>, F<Trampoline<A>, Trampoline<B>>>() {
         public F<Trampoline<A>, Trampoline<B>> f(final F<A, Trampoline<B>> f) {
            return new F<Trampoline<A>, Trampoline<B>>() {
               public Trampoline<B> f(Trampoline<A> a) {
                  return a.bind(f);
               }
            };
         }
      };
   }

   public static <A, B> F<F<A, B>, F<Trampoline<A>, Trampoline<B>>> map_() {
      return new F<F<A, B>, F<Trampoline<A>, Trampoline<B>>>() {
         public F<Trampoline<A>, Trampoline<B>> f(final F<A, B> f) {
            return new F<Trampoline<A>, Trampoline<B>>() {
               public Trampoline<B> f(Trampoline<A> a) {
                  return a.map(f);
               }
            };
         }
      };
   }

   public static <A> F<Trampoline<A>, Either<P1<Trampoline<A>>, A>> resume_() {
      return new F<Trampoline<A>, Either<P1<Trampoline<A>>, A>>() {
         public Either<P1<Trampoline<A>>, A> f(Trampoline<A> aTrampoline) {
            return aTrampoline.resume();
         }
      };
   }

   public abstract Either<P1<Trampoline<A>>, A> resume();

   public A run() {
      Trampoline current = this;

      Iterator var3;
      do {
         Either<P1<Trampoline<A>>, A> x = current.resume();

         P1 t;
         for(var3 = x.left().iterator(); var3.hasNext(); current = (Trampoline)t._1()) {
            t = (P1)var3.next();
         }

         var3 = x.right().iterator();
      } while(!var3.hasNext());

      A a = var3.next();
      return a;
   }

   public final <B> Trampoline<B> apply(Trampoline<F<A, B>> lf) {
      return lf.bind(new F<F<A, B>, Trampoline<B>>() {
         public Trampoline<B> f(F<A, B> f) {
            return Trampoline.this.map(f);
         }
      });
   }

   public final <B, C> Trampoline<C> bind(Trampoline<B> lb, F<A, F<B, C>> f) {
      return lb.apply(this.map(f));
   }

   public static <A, B, C> F<Trampoline<A>, F<Trampoline<B>, Trampoline<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<Trampoline<A>, Trampoline<B>, Trampoline<C>>() {
         public Trampoline<C> f(Trampoline<A> as, Trampoline<B> bs) {
            return as.bind(bs, f);
         }
      });
   }

   public <B, C> Trampoline<C> zipWith(Trampoline<B> b, final F2<A, B, C> f) {
      Either<P1<Trampoline<A>>, A> ea = this.resume();
      Either<P1<Trampoline<B>>, B> eb = b.resume();
      Iterator var5 = ea.left().iterator();

      P1 x;
      Iterator var7;
      final Object y;
      do {
         P1 y;
         if (!var5.hasNext()) {
            var5 = ea.right().iterator();

            final Object x;
            do {
               if (!var5.hasNext()) {
                  throw Bottom.error("Match error: Trampoline is neither done nor suspended.");
               }

               x = var5.next();
               var7 = eb.right().iterator();
               if (var7.hasNext()) {
                  y = var7.next();
                  return suspend(new P1<Trampoline<C>>() {
                     public Trampoline<C> _1() {
                        return Trampoline.pure(f.f(x, y));
                     }
                  });
               }

               var7 = eb.left().iterator();
            } while(!var7.hasNext());

            y = (P1)var7.next();
            return suspend(y.map((F)liftM2(F2Functions.curry(f)).f(pure(x))));
         }

         x = (P1)var5.next();
         var7 = eb.left().iterator();
         if (var7.hasNext()) {
            y = (P1)var7.next();
            return suspend(x.bind(y, F2Functions.curry(new F2<Trampoline<A>, Trampoline<B>, Trampoline<C>>() {
               public Trampoline<C> f(final Trampoline<A> ta, final Trampoline<B> tb) {
                  return Trampoline.suspend(new P1<Trampoline<C>>() {
                     public Trampoline<C> _1() {
                        return ta.zipWith(tb, f);
                     }
                  });
               }
            })));
         }

         var7 = eb.right().iterator();
      } while(!var7.hasNext());

      y = var7.next();
      return suspend(x.map(new F<Trampoline<A>, Trampoline<C>>() {
         public Trampoline<C> f(Trampoline<A> ta) {
            return ta.map(F2Functions.f(F2Functions.flip(f), y));
         }
      }));
   }

   private static final class Pure<A> extends Trampoline.Normal<A> {
      private final A value;

      private Pure(A a) {
         super(null);
         this.value = a;
      }

      public <R> R foldNormal(F<A, R> pure, F<P1<Trampoline<A>>, R> k) {
         return pure.f(this.value);
      }

      public <R> R fold(F<Trampoline.Normal<A>, R> n, F<Trampoline.Codense<A>, R> gs) {
         return n.f(this);
      }

      public Either<P1<Trampoline<A>>, A> resume() {
         return Either.right(this.value);
      }

      // $FF: synthetic method
      Pure(Object x0, Object x1) {
         this(x0);
      }
   }

   private static final class Suspend<A> extends Trampoline.Normal<A> {
      private final P1<Trampoline<A>> suspension;

      private Suspend(P1<Trampoline<A>> s) {
         super(null);
         this.suspension = s;
      }

      public <R> R foldNormal(F<A, R> pure, F<P1<Trampoline<A>>, R> k) {
         return k.f(this.suspension);
      }

      public <R> R fold(F<Trampoline.Normal<A>, R> n, F<Trampoline.Codense<A>, R> gs) {
         return n.f(this);
      }

      public Either<P1<Trampoline<A>>, A> resume() {
         return Either.left(this.suspension);
      }

      // $FF: synthetic method
      Suspend(P1 x0, Object x1) {
         this(x0);
      }
   }

   private static final class Codense<A> extends Trampoline<A> {
      private final Trampoline.Normal<Object> sub;
      private final F<Object, Trampoline<A>> cont;

      private Codense(Trampoline.Normal<Object> t, F<Object, Trampoline<A>> k) {
         this.sub = t;
         this.cont = k;
      }

      public <R> R fold(F<Trampoline.Normal<A>, R> n, F<Trampoline.Codense<A>, R> gs) {
         return gs.f(this);
      }

      public <B> Trampoline<B> bind(final F<A, Trampoline<B>> f) {
         return codense(this.sub, new F<Object, Trampoline<B>>() {
            public Trampoline<B> f(final Object o) {
               return Trampoline.suspend(new P1<Trampoline<B>>() {
                  public Trampoline<B> _1() {
                     return ((Trampoline)Codense.this.cont.f(o)).bind(f);
                  }
               });
            }
         });
      }

      public Either<P1<Trampoline<A>>, A> resume() {
         return Either.left(this.sub.resume().either(new F<P1<Trampoline<Object>>, P1<Trampoline<A>>>() {
            public P1<Trampoline<A>> f(P1<Trampoline<Object>> p) {
               return p.map(new F<Trampoline<Object>, Trampoline<A>>() {
                  public Trampoline<A> f(Trampoline<Object> ot) {
                     return (Trampoline)ot.fold(new F<Trampoline.Normal<Object>, Trampoline<A>>() {
                        public Trampoline<A> f(Trampoline.Normal<Object> o) {
                           return (Trampoline)o.foldNormal(new F<Object, Trampoline<A>>() {
                              public Trampoline<A> f(Object o) {
                                 return (Trampoline)Codense.this.cont.f(o);
                              }
                           }, new F<P1<Trampoline<Object>>, Trampoline<A>>() {
                              public Trampoline<A> f(P1<Trampoline<Object>> t) {
                                 return ((Trampoline)t._1()).bind(Codense.this.cont);
                              }
                           });
                        }
                     }, new F<Trampoline.Codense<Object>, Trampoline<A>>() {
                        public Trampoline<A> f(final Trampoline.Codense<Object> c) {
                           return Trampoline.codense(c.sub, new F<Object, Trampoline<A>>() {
                              public Trampoline<A> f(Object o) {
                                 return ((Trampoline)c.cont.f(o)).bind(Codense.this.cont);
                              }
                           });
                        }
                     });
                  }
               });
            }
         }, new F<Object, P1<Trampoline<A>>>() {
            public P1<Trampoline<A>> f(final Object o) {
               return new P1<Trampoline<A>>() {
                  public Trampoline<A> _1() {
                     return (Trampoline)Codense.this.cont.f(o);
                  }
               };
            }
         }));
      }

      // $FF: synthetic method
      Codense(Trampoline.Normal x0, F x1, Object x2) {
         this(x0, x1);
      }
   }

   private abstract static class Normal<A> extends Trampoline<A> {
      private Normal() {
      }

      public abstract <R> R foldNormal(F<A, R> var1, F<P1<Trampoline<A>>, R> var2);

      public <B> Trampoline<B> bind(F<A, Trampoline<B>> f) {
         return codense(this, f);
      }

      // $FF: synthetic method
      Normal(Object x0) {
         this();
      }
   }
}
