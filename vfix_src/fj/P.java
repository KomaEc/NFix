package fj;

public final class P {
   private P() {
      throw new UnsupportedOperationException();
   }

   public static <A> F<A, P1<A>> p1() {
      return new F<A, P1<A>>() {
         public P1<A> f(A a) {
            return P.p(a);
         }
      };
   }

   public static <A> P1<A> p(final A a) {
      return new P1<A>() {
         public A _1() {
            return a;
         }
      };
   }

   public static <A> P1<A> lazy(P1<A> pa) {
      return pa;
   }

   public static <A, B> P2<A, B> lazy(final P1<A> pa, final P1<B> pb) {
      return new P2<A, B>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }
      };
   }

   public static <A, B, C> P3<A, B, C> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc) {
      return new P3<A, B, C>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }
      };
   }

   public static <A, B, C, D> P4<A, B, C, D> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd) {
      return new P4<A, B, C, D>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }

         public D _4() {
            return pd._1();
         }
      };
   }

   public static <A, B, C, D, E> P5<A, B, C, D, E> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe) {
      return new P5<A, B, C, D, E>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }

         public D _4() {
            return pd._1();
         }

         public E _5() {
            return pe._1();
         }
      };
   }

   public static <A, B, C, D, E, F> P6<A, B, C, D, E, F> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf) {
      return new P6<A, B, C, D, E, F>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }

         public D _4() {
            return pd._1();
         }

         public E _5() {
            return pe._1();
         }

         public F _6() {
            return pf._1();
         }
      };
   }

   public static <A, B, C, D, E, F, G> P7<A, B, C, D, E, F, G> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf, final P1<G> pg) {
      return new P7<A, B, C, D, E, F, G>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }

         public D _4() {
            return pd._1();
         }

         public E _5() {
            return pe._1();
         }

         public F _6() {
            return pf._1();
         }

         public G _7() {
            return pg._1();
         }
      };
   }

   public static <A, B, C, D, E, F, G, H> P8<A, B, C, D, E, F, G, H> lazy(final P1<A> pa, final P1<B> pb, final P1<C> pc, final P1<D> pd, final P1<E> pe, final P1<F> pf, final P1<G> pg, final P1<H> ph) {
      return new P8<A, B, C, D, E, F, G, H>() {
         public A _1() {
            return pa._1();
         }

         public B _2() {
            return pb._1();
         }

         public C _3() {
            return pc._1();
         }

         public D _4() {
            return pd._1();
         }

         public E _5() {
            return pe._1();
         }

         public F _6() {
            return pf._1();
         }

         public G _7() {
            return pg._1();
         }

         public H _8() {
            return ph._1();
         }
      };
   }

   public static <A, B> F<A, F<B, P2<A, B>>> p2() {
      return new F<A, F<B, P2<A, B>>>() {
         public F<B, P2<A, B>> f(final A a) {
            return new F<B, P2<A, B>>() {
               public P2<A, B> f(B b) {
                  return P.p(a, b);
               }
            };
         }
      };
   }

   public static <A, B> P2<A, B> p(final A a, final B b) {
      return new P2<A, B>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }
      };
   }

   public static <A, B, C> F<A, F<B, F<C, P3<A, B, C>>>> p3() {
      return new F<A, F<B, F<C, P3<A, B, C>>>>() {
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
      };
   }

   public static <A, B, C> P3<A, B, C> p(final A a, final B b, final C c) {
      return new P3<A, B, C>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }
      };
   }

   public static <A, B, C, D> F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>> p4() {
      return new F<A, F<B, F<C, F<D, P4<A, B, C, D>>>>>() {
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
      };
   }

   public static <A, B, C, D> P4<A, B, C, D> p(final A a, final B b, final C c, final D d) {
      return new P4<A, B, C, D>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }

         public D _4() {
            return d;
         }
      };
   }

   public static <A, B, C, D, E> F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>> p5() {
      return new F<A, F<B, F<C, F<D, F<E, P5<A, B, C, D, E>>>>>>() {
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
      };
   }

   public static <A, B, C, D, E> P5<A, B, C, D, E> p(final A a, final B b, final C c, final D d, final E e) {
      return new P5<A, B, C, D, E>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }

         public D _4() {
            return d;
         }

         public E _5() {
            return e;
         }
      };
   }

   public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>> p6() {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, P6<A, B, C, D, E, F$>>>>>>>() {
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
      };
   }

   public static <A, B, C, D, E, F$> P6<A, B, C, D, E, F$> p(final A a, final B b, final C c, final D d, final E e, final F$ f) {
      return new P6<A, B, C, D, E, F$>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }

         public D _4() {
            return d;
         }

         public E _5() {
            return e;
         }

         public F$ _6() {
            return f;
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>> p7() {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, P7<A, B, C, D, E, F$, G>>>>>>>>() {
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
      };
   }

   public static <A, B, C, D, E, F$, G> P7<A, B, C, D, E, F$, G> p(final A a, final B b, final C c, final D d, final E e, final F$ f, final G g) {
      return new P7<A, B, C, D, E, F$, G>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }

         public D _4() {
            return d;
         }

         public E _5() {
            return e;
         }

         public F$ _6() {
            return f;
         }

         public G _7() {
            return g;
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>> p8() {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, P8<A, B, C, D, E, F$, G, H>>>>>>>>>() {
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
      };
   }

   public static <A, B, C, D, E, F$, G, H> P8<A, B, C, D, E, F$, G, H> p(final A a, final B b, final C c, final D d, final E e, final F$ f, final G g, final H h) {
      return new P8<A, B, C, D, E, F$, G, H>() {
         public A _1() {
            return a;
         }

         public B _2() {
            return b;
         }

         public C _3() {
            return c;
         }

         public D _4() {
            return d;
         }

         public E _5() {
            return e;
         }

         public F$ _6() {
            return f;
         }

         public G _7() {
            return g;
         }

         public H _8() {
            return h;
         }
      };
   }

   public static <A> P1<A> lazy(final F<Unit, A> f) {
      return new P1<A>() {
         public A _1() {
            return f.f(Unit.unit());
         }
      };
   }

   public static <A, B> P2<A, B> lazy(final F<Unit, A> fa, final F<Unit, B> fb) {
      return new P2<A, B>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }
      };
   }

   public static <A, B, C> P3<A, B, C> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc) {
      return new P3<A, B, C>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }
      };
   }

   public static <A, B, C, D> P4<A, B, C, D> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd) {
      return new P4<A, B, C, D>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }

         public D _4() {
            return fd.f(Unit.unit());
         }
      };
   }

   public static <A, B, C, D, E> P5<A, B, C, D, E> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe) {
      return new P5<A, B, C, D, E>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }

         public D _4() {
            return fd.f(Unit.unit());
         }

         public E _5() {
            return fe.f(Unit.unit());
         }
      };
   }

   public static <A, B, C, D, E, F$> P6<A, B, C, D, E, F$> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff) {
      return new P6<A, B, C, D, E, F$>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }

         public D _4() {
            return fd.f(Unit.unit());
         }

         public E _5() {
            return fe.f(Unit.unit());
         }

         public F$ _6() {
            return ff.f(Unit.unit());
         }
      };
   }

   public static <A, B, C, D, E, F$, G> P7<A, B, C, D, E, F$, G> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff, final F<Unit, G> fg) {
      return new P7<A, B, C, D, E, F$, G>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }

         public D _4() {
            return fd.f(Unit.unit());
         }

         public E _5() {
            return fe.f(Unit.unit());
         }

         public F$ _6() {
            return ff.f(Unit.unit());
         }

         public G _7() {
            return fg.f(Unit.unit());
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> P8<A, B, C, D, E, F$, G, H> lazy(final F<Unit, A> fa, final F<Unit, B> fb, final F<Unit, C> fc, final F<Unit, D> fd, final F<Unit, E> fe, final F<Unit, F$> ff, final F<Unit, G> fg, final F<Unit, H> fh) {
      return new P8<A, B, C, D, E, F$, G, H>() {
         public A _1() {
            return fa.f(Unit.unit());
         }

         public B _2() {
            return fb.f(Unit.unit());
         }

         public C _3() {
            return fc.f(Unit.unit());
         }

         public D _4() {
            return fd.f(Unit.unit());
         }

         public E _5() {
            return fe.f(Unit.unit());
         }

         public F$ _6() {
            return ff.f(Unit.unit());
         }

         public G _7() {
            return fg.f(Unit.unit());
         }

         public H _8() {
            return fh.f(Unit.unit());
         }
      };
   }
}
