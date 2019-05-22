package fj;

import fj.data.Option;

public final class Function {
   private Function() {
      throw new UnsupportedOperationException();
   }

   public static <A, B> F<F<A, B>, B> apply(final A a) {
      return new F<F<A, B>, B>() {
         public B f(F<A, B> k) {
            return k.f(a);
         }
      };
   }

   public static <A, B, C> F<F<B, C>, F<F<A, B>, F<A, C>>> compose() {
      return new F<F<B, C>, F<F<A, B>, F<A, C>>>() {
         public F<F<A, B>, F<A, C>> f(final F<B, C> f) {
            return new F<F<A, B>, F<A, C>>() {
               public F<A, C> f(F<A, B> g) {
                  return Function.compose(f, g);
               }
            };
         }
      };
   }

   public static <A, B, C> F<A, C> compose(final F<B, C> f, final F<A, B> g) {
      return new F<A, C>() {
         public C f(A a) {
            return f.f(g.f(a));
         }
      };
   }

   public static <A, B, C, D> F<A, F<B, D>> compose2(final F<C, D> f, final F<A, F<B, C>> g) {
      return new F<A, F<B, D>>() {
         public F<B, D> f(final A a) {
            return new F<B, D>() {
               public D f(B b) {
                  return f.f(((F)g.f(a)).f(b));
               }
            };
         }
      };
   }

   public static <A, B, C> F<F<A, B>, F<F<B, C>, F<A, C>>> andThen() {
      return new F<F<A, B>, F<F<B, C>, F<A, C>>>() {
         public F<F<B, C>, F<A, C>> f(final F<A, B> g) {
            return new F<F<B, C>, F<A, C>>() {
               public F<A, C> f(F<B, C> f) {
                  return Function.andThen(g, f);
               }
            };
         }
      };
   }

   public static <A, B, C> F<A, C> andThen(final F<A, B> g, final F<B, C> f) {
      return new F<A, C>() {
         public C f(A a) {
            return f.f(g.f(a));
         }
      };
   }

   public static <A> F<A, A> identity() {
      return new F<A, A>() {
         public A f(A a) {
            return a;
         }
      };
   }

   public static <A, B> F<B, F<A, B>> constant() {
      return new F<B, F<A, B>>() {
         public F<A, B> f(B b) {
            return Function.constant(b);
         }
      };
   }

   public static <A, B> F<A, B> constant(final B b) {
      return new F<A, B>() {
         public B f(A a) {
            return b;
         }
      };
   }

   public static <A, B> F<A, B> vary(final F<? super A, ? extends B> f) {
      return new F<A, B>() {
         public B f(A a) {
            return f.f(a);
         }
      };
   }

   public static <C, A extends C, B, D extends B> F<F<C, D>, F<A, B>> vary() {
      return new F<F<C, D>, F<A, B>>() {
         public F<A, B> f(F<C, D> f) {
            return Function.vary(f);
         }
      };
   }

   public static <A, B, C> F<F<A, F<B, C>>, F<B, F<A, C>>> flip() {
      return new F<F<A, F<B, C>>, F<B, F<A, C>>>() {
         public F<B, F<A, C>> f(F<A, F<B, C>> f) {
            return Function.flip(f);
         }
      };
   }

   public static <A, B, C> F<B, F<A, C>> flip(final F<A, F<B, C>> f) {
      return new F<B, F<A, C>>() {
         public F<A, C> f(final B b) {
            return new F<A, C>() {
               public C f(A a) {
                  return ((F)f.f(a)).f(b);
               }
            };
         }
      };
   }

   public static <A, B, C> F2<B, A, C> flip(final F2<A, B, C> f) {
      return new F2<B, A, C>() {
         public C f(B b, A a) {
            return f.f(a, b);
         }
      };
   }

   public static <A, B, C> F<F2<A, B, C>, F2<B, A, C>> flip2() {
      return new F<F2<A, B, C>, F2<B, A, C>>() {
         public F2<B, A, C> f(F2<A, B, C> f) {
            return Function.flip(f);
         }
      };
   }

   public static <A, B> F<A, Option<B>> nullable(final F<A, B> f) {
      return new F<A, Option<B>>() {
         public Option<B> f(A a) {
            return a == null ? Option.none() : Option.some(f.f(a));
         }
      };
   }

   public static <A, B, C> F<A, F<B, C>> curry(final F2<A, B, C> f) {
      return new F<A, F<B, C>>() {
         public F<B, C> f(final A a) {
            return new F<B, C>() {
               public C f(B b) {
                  return f.f(a, b);
               }
            };
         }
      };
   }

   public static <A, B, C> F<B, C> curry(F2<A, B, C> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C> F<F<A, F<B, C>>, F2<A, B, C>> uncurryF2() {
      return new F<F<A, F<B, C>>, F2<A, B, C>>() {
         public F2<A, B, C> f(F<A, F<B, C>> f) {
            return Function.uncurryF2(f);
         }
      };
   }

   public static <A, B, C> F2<A, B, C> uncurryF2(final F<A, F<B, C>> f) {
      return new F2<A, B, C>() {
         public C f(A a, B b) {
            return ((F)f.f(a)).f(b);
         }
      };
   }

   public static <A, B, C, D> F<A, F<B, F<C, D>>> curry(final F3<A, B, C, D> f) {
      return new F<A, F<B, F<C, D>>>() {
         public F<B, F<C, D>> f(final A a) {
            return new F<B, F<C, D>>() {
               public F<C, D> f(final B b) {
                  return new F<C, D>() {
                     public D f(C c) {
                        return f.f(a, b, c);
                     }
                  };
               }
            };
         }
      };
   }

   public static <A, B, C, D> F<B, F<C, D>> curry(F3<A, B, C, D> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C, D> F<C, D> curry(F3<A, B, C, D> f, A a, B b) {
      return (F)curry(f, a).f(b);
   }

   public static <A, B, C, D> F<F<A, F<B, F<C, D>>>, F3<A, B, C, D>> uncurryF3() {
      return new F<F<A, F<B, F<C, D>>>, F3<A, B, C, D>>() {
         public F3<A, B, C, D> f(F<A, F<B, F<C, D>>> f) {
            return Function.uncurryF3(f);
         }
      };
   }

   public static <A, B, C, D> F3<A, B, C, D> uncurryF3(final F<A, F<B, F<C, D>>> f) {
      return new F3<A, B, C, D>() {
         public D f(A a, B b, C c) {
            return ((F)((F)f.f(a)).f(b)).f(c);
         }
      };
   }

   public static <A, B, C, D, E> F<A, F<B, F<C, F<D, E>>>> curry(final F4<A, B, C, D, E> f) {
      return new F<A, F<B, F<C, F<D, E>>>>() {
         public F<B, F<C, F<D, E>>> f(final A a) {
            return new F<B, F<C, F<D, E>>>() {
               public F<C, F<D, E>> f(final B b) {
                  return new F<C, F<D, E>>() {
                     public F<D, E> f(final C c) {
                        return new F<D, E>() {
                           public E f(D d) {
                              return f.f(a, b, c, d);
                           }
                        };
                     }
                  };
               }
            };
         }
      };
   }

   public static <A, B, C, D, E> F<B, F<C, F<D, E>>> curry(F4<A, B, C, D, E> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C, D, E> F<C, F<D, E>> curry(F4<A, B, C, D, E> f, A a, B b) {
      return (F)((F)curry(f).f(a)).f(b);
   }

   public static <A, B, C, D, E> F<D, E> curry(F4<A, B, C, D, E> f, A a, B b, C c) {
      return (F)((F)((F)curry(f).f(a)).f(b)).f(c);
   }

   public static <A, B, C, D, E> F<F<A, F<B, F<C, F<D, E>>>>, F4<A, B, C, D, E>> uncurryF4() {
      return new F<F<A, F<B, F<C, F<D, E>>>>, F4<A, B, C, D, E>>() {
         public F4<A, B, C, D, E> f(F<A, F<B, F<C, F<D, E>>>> f) {
            return Function.uncurryF4(f);
         }
      };
   }

   public static <A, B, C, D, E> F4<A, B, C, D, E> uncurryF4(final F<A, F<B, F<C, F<D, E>>>> f) {
      return new F4<A, B, C, D, E>() {
         public E f(A a, B b, C c, D d) {
            return ((F)((F)((F)f.f(a)).f(b)).f(c)).f(d);
         }
      };
   }

   public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F<E, F$>>>>> curry(final F5<A, B, C, D, E, F$> f) {
      return new F<A, F<B, F<C, F<D, F<E, F$>>>>>() {
         public F<B, F<C, F<D, F<E, F$>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F$>>>>() {
               public F<C, F<D, F<E, F$>>> f(final B b) {
                  return new F<C, F<D, F<E, F$>>>() {
                     public F<D, F<E, F$>> f(final C c) {
                        return new F<D, F<E, F$>>() {
                           public F<E, F$> f(final D d) {
                              return new F<E, F$>() {
                                 public F$ f(E e) {
                                    return f.f(a, b, c, d, e);
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

   public static <A, B, C, D, E, F$> F<B, F<C, F<D, F<E, F$>>>> curry(F5<A, B, C, D, E, F$> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C, D, E, F$> F<C, F<D, F<E, F$>>> curry(F5<A, B, C, D, E, F$> f, A a, B b) {
      return (F)((F)curry(f).f(a)).f(b);
   }

   public static <A, B, C, D, E, F$> F<D, F<E, F$>> curry(F5<A, B, C, D, E, F$> f, A a, B b, C c) {
      return (F)((F)((F)curry(f).f(a)).f(b)).f(c);
   }

   public static <A, B, C, D, E, F$> F<E, F$> curry(F5<A, B, C, D, E, F$> f, A a, B b, C c, D d) {
      return (F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d);
   }

   public static <A, B, C, D, E, F$> F<F<A, F<B, F<C, F<D, F<E, F$>>>>>, F5<A, B, C, D, E, F$>> uncurryF5() {
      return new F<F<A, F<B, F<C, F<D, F<E, F$>>>>>, F5<A, B, C, D, E, F$>>() {
         public F5<A, B, C, D, E, F$> f(F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
            return Function.uncurryF5(f);
         }
      };
   }

   public static <A, B, C, D, E, F$> F5<A, B, C, D, E, F$> uncurryF5(final F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
      return new F5<A, B, C, D, E, F$>() {
         public F$ f(A a, B b, C c, D d, E e) {
            return ((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e);
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> curry(final F6<A, B, C, D, E, F$, G> f) {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, G>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, G>>>>>() {
               public F<C, F<D, F<E, F<F$, G>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, G>>>>() {
                     public F<D, F<E, F<F$, G>>> f(final C c) {
                        return new F<D, F<E, F<F$, G>>>() {
                           public F<E, F<F$, G>> f(final D d) {
                              return new F<E, F<F$, G>>() {
                                 public F<F$, G> f(final E e) {
                                    return new F<F$, G>() {
                                       public G f(F$ f$) {
                                          return f.f(a, b, c, d, e, f$);
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

   public static <A, B, C, D, E, F$, G> F<F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>>, F6<A, B, C, D, E, F$, G>> uncurryF6() {
      return new F<F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>>, F6<A, B, C, D, E, F$, G>>() {
         public F6<A, B, C, D, E, F$, G> f(F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
            return Function.uncurryF6(f);
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F6<A, B, C, D, E, F$, G> uncurryF6(final F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
      return new F6<A, B, C, D, E, F$, G>() {
         public G f(A a, B b, C c, D d, E e, F$ f$) {
            return ((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> curry(final F7<A, B, C, D, E, F$, G, H> f) {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, H>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, H>>>>>() {
                     public F<D, F<E, F<F$, F<G, H>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, H>>>>() {
                           public F<E, F<F$, F<G, H>>> f(final D d) {
                              return new F<E, F<F$, F<G, H>>>() {
                                 public F<F$, F<G, H>> f(final E e) {
                                    return new F<F$, F<G, H>>() {
                                       public F<G, H> f(final F$ f$) {
                                          return new F<G, H>() {
                                             public H f(G g) {
                                                return f.f(a, b, c, d, e, f$, g);
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

   public static <A, B, C, D, E, F$, G, H> F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C, D, E, F$, G, H> F<C, F<D, F<E, F<F$, F<G, H>>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b) {
      return (F)((F)curry(f).f(a)).f(b);
   }

   public static <A, B, C, D, E, F$, G, H> F<D, F<E, F<F$, F<G, H>>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c) {
      return (F)((F)((F)curry(f).f(a)).f(b)).f(c);
   }

   public static <A, B, C, D, E, F$, G, H> F<E, F<F$, F<G, H>>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d) {
      return (F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d);
   }

   public static <A, B, C, D, E, F$, G, H> F<F$, F<G, H>> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d, E e) {
      return (F)((F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d)).f(e);
   }

   public static <A, B, C, D, E, F$, G, H> F<G, H> curry(F7<A, B, C, D, E, F$, G, H> f, A a, B b, C c, D d, E e, F$ f$) {
      return (F)((F)((F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
   }

   public static <A, B, C, D, E, F$, G, H> F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>>, F7<A, B, C, D, E, F$, G, H>> uncurryF7() {
      return new F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>>, F7<A, B, C, D, E, F$, G, H>>() {
         public F7<A, B, C, D, E, F$, G, H> f(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
            return Function.uncurryF7(f);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H> F7<A, B, C, D, E, F$, G, H> uncurryF7(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
      return new F7<A, B, C, D, E, F$, G, H>() {
         public H f(A a, B b, C c, D d, E e, F$ f$, G g) {
            return ((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H, I> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> curry(final F8<A, B, C, D, E, F$, G, H, I> f) {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>() {
                     public F<D, F<E, F<F$, F<G, F<H, I>>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, F<H, I>>>>>() {
                           public F<E, F<F$, F<G, F<H, I>>>> f(final D d) {
                              return new F<E, F<F$, F<G, F<H, I>>>>() {
                                 public F<F$, F<G, F<H, I>>> f(final E e) {
                                    return new F<F$, F<G, F<H, I>>>() {
                                       public F<G, F<H, I>> f(final F$ f$) {
                                          return new F<G, F<H, I>>() {
                                             public F<H, I> f(final G g) {
                                                return new F<H, I>() {
                                                   public I f(H h) {
                                                      return f.f(a, b, c, d, e, f$, g, h);
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

   public static <A, B, C, D, E, F$, G, H, I> F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a) {
      return (F)curry(f).f(a);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b) {
      return (F)((F)curry(f).f(a)).f(b);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<D, F<E, F<F$, F<G, F<H, I>>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c) {
      return (F)((F)((F)curry(f).f(a)).f(b)).f(c);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<E, F<F$, F<G, F<H, I>>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d) {
      return (F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<F$, F<G, F<H, I>>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e) {
      return (F)((F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d)).f(e);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<G, F<H, I>> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e, F$ f$) {
      return (F)((F)((F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<H, I> curry(F8<A, B, C, D, E, F$, G, H, I> f, A a, B b, C c, D d, E e, F$ f$, G g) {
      return (F)((F)((F)((F)((F)((F)((F)curry(f).f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g);
   }

   public static <A, B, C, D, E, F$, G, H, I> F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>>, F8<A, B, C, D, E, F$, G, H, I>> uncurryF8() {
      return new F<F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>>, F8<A, B, C, D, E, F$, G, H, I>>() {
         public F8<A, B, C, D, E, F$, G, H, I> f(F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
            return Function.uncurryF8(f);
         }
      };
   }

   public static <A, B, C, D, E, F$, G, H, I> F8<A, B, C, D, E, F$, G, H, I> uncurryF8(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
      return new F8<A, B, C, D, E, F$, G, H, I>() {
         public I f(A a, B b, C c, D d, E e, F$ f$, G g, H h) {
            return ((F)((F)((F)((F)((F)((F)((F)f.f(a)).f(b)).f(c)).f(d)).f(e)).f(f$)).f(g)).f(h);
         }
      };
   }

   public static <A, B, C> F<C, B> bind(final F<C, A> ma, final F<A, F<C, B>> f) {
      return new F<C, B>() {
         public B f(C m) {
            return ((F)f.f(ma.f(m))).f(m);
         }
      };
   }

   public static <A, B, C> F<C, B> apply(F<C, F<A, B>> cab, final F<C, A> ca) {
      return bind(cab, new F<F<A, B>, F<C, B>>() {
         public F<C, B> f(final F<A, B> f) {
            return Function.compose(new F<A, B>() {
               public B f(A a) {
                  return f.f(a);
               }
            }, ca);
         }
      });
   }

   public static <A, B, C, D> F<D, C> bind(F<D, A> ca, F<D, B> cb, F<A, F<B, C>> f) {
      return apply(compose(f, ca), cb);
   }

   public static <A, B, C> F<B, F<B, C>> on(F<A, F<A, C>> a, F<B, A> f) {
      return compose(compose((F)andThen().f(f), a), f);
   }

   public static <A, B, C, D> F<F<D, A>, F<F<D, B>, F<D, C>>> lift(final F<A, F<B, C>> f) {
      return curry(new F2<F<D, A>, F<D, B>, F<D, C>>() {
         public F<D, C> f(F<D, A> ca, F<D, B> cb) {
            return Function.bind(ca, cb, f);
         }
      });
   }

   public static <A, B> F<B, A> join(F<B, F<B, A>> f) {
      return bind(f, identity());
   }

   public static <A, B, C> F<A, C> partialApply2(final F<A, F<B, C>> f, final B b) {
      return new F<A, C>() {
         public C f(A a) {
            return Function.uncurryF2(f).f(a, b);
         }
      };
   }

   public static <A, B, C, D> F<A, F<B, D>> partialApply3(final F<A, F<B, F<C, D>>> f, final C c) {
      return new F<A, F<B, D>>() {
         public F<B, D> f(final A a) {
            return new F<B, D>() {
               public D f(B b) {
                  return Function.uncurryF3(f).f(a, b, c);
               }
            };
         }
      };
   }

   public static <A, B, C, D, E> F<A, F<B, F<C, E>>> partialApply4(final F<A, F<B, F<C, F<D, E>>>> f, final D d) {
      return new F<A, F<B, F<C, E>>>() {
         public F<B, F<C, E>> f(final A a) {
            return new F<B, F<C, E>>() {
               public F<C, E> f(final B b) {
                  return new F<C, E>() {
                     public E f(C c) {
                        return Function.uncurryF4(f).f(a, b, c, d);
                     }
                  };
               }
            };
         }
      };
   }

   public static <A, B, C, D, E, F$> F<A, F<B, F<C, F<D, F$>>>> partialApply5(final F<A, F<B, F<C, F<D, F<E, F$>>>>> f, final E e) {
      return new F<A, F<B, F<C, F<D, F$>>>>() {
         public F<B, F<C, F<D, F$>>> f(final A a) {
            return new F<B, F<C, F<D, F$>>>() {
               public F<C, F<D, F$>> f(final B b) {
                  return new F<C, F<D, F$>>() {
                     public F<D, F$> f(final C c) {
                        return new F<D, F$>() {
                           public F$ f(D d) {
                              return Function.uncurryF5(f).f(a, b, c, d, e);
                           }
                        };
                     }
                  };
               }
            };
         }
      };
   }

   public static <A, B, C, D, E, F$, G> F<A, F<B, F<C, F<D, F<E, G>>>>> partialApply6(final F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f, final F$ f$) {
      return new F<A, F<B, F<C, F<D, F<E, G>>>>>() {
         public F<B, F<C, F<D, F<E, G>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, G>>>>() {
               public F<C, F<D, F<E, G>>> f(final B b) {
                  return new F<C, F<D, F<E, G>>>() {
                     public F<D, F<E, G>> f(final C c) {
                        return new F<D, F<E, G>>() {
                           public F<E, G> f(final D d) {
                              return new F<E, G>() {
                                 public G f(E e) {
                                    return Function.uncurryF6(f).f(a, b, c, d, e, f$);
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

   public static <A, B, C, D, E, F$, G, H> F<A, F<B, F<C, F<D, F<E, F<F$, H>>>>>> partialApply7(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f, final G g) {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, H>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, H>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, H>>>>>() {
               public F<C, F<D, F<E, F<F$, H>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, H>>>>() {
                     public F<D, F<E, F<F$, H>>> f(final C c) {
                        return new F<D, F<E, F<F$, H>>>() {
                           public F<E, F<F$, H>> f(final D d) {
                              return new F<E, F<F$, H>>() {
                                 public F<F$, H> f(final E e) {
                                    return new F<F$, H>() {
                                       public H f(F$ f$) {
                                          return Function.uncurryF7(f).f(a, b, c, d, e, f$, g);
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

   public static <A, B, C, D, E, F$, G, H, I> F<A, F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>>> partialApply8(final F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f, final H h) {
      return new F<A, F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>>>() {
         public F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>> f(final A a) {
            return new F<B, F<C, F<D, F<E, F<F$, F<G, I>>>>>>() {
               public F<C, F<D, F<E, F<F$, F<G, I>>>>> f(final B b) {
                  return new F<C, F<D, F<E, F<F$, F<G, I>>>>>() {
                     public F<D, F<E, F<F$, F<G, I>>>> f(final C c) {
                        return new F<D, F<E, F<F$, F<G, I>>>>() {
                           public F<E, F<F$, F<G, I>>> f(final D d) {
                              return new F<E, F<F$, F<G, I>>>() {
                                 public F<F$, F<G, I>> f(final E e) {
                                    return new F<F$, F<G, I>>() {
                                       public F<G, I> f(final F$ f$) {
                                          return new F<G, I>() {
                                             public I f(G g) {
                                                return Function.uncurryF8(f).f(a, b, c, d, e, f$, g, h);
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
}
