package fj.data.fingertrees;

import fj.F;
import fj.Function;
import fj.P2;
import fj.data.List;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;

public final class Deep<V, A> extends FingerTree<V, A> {
   private final V v;
   private final Digit<V, A> prefix;
   private final FingerTree<V, Node<V, A>> middle;
   private final Digit<V, A> suffix;

   Deep(Measured<V, A> m, V v, Digit<V, A> prefix, FingerTree<V, Node<V, A>> middle, Digit<V, A> suffix) {
      super(m);
      this.v = v;
      this.prefix = prefix;
      this.middle = middle;
      this.suffix = suffix;
   }

   public Digit<V, A> prefix() {
      return this.prefix;
   }

   public FingerTree<V, Node<V, A>> middle() {
      return this.middle;
   }

   public Digit<V, A> suffix() {
      return this.suffix;
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return this.prefix.foldRight(aff, this.middle.foldRight(Function.flip(Node.foldRight_(aff)), this.suffix.foldRight(aff, z)));
   }

   public A reduceRight(F<A, F<A, A>> aff) {
      return this.prefix.foldRight(aff, this.middle.foldRight(Function.flip(Node.foldRight_(aff)), this.suffix.reduceRight(aff)));
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return this.suffix.foldLeft(bff, this.middle.foldLeft(Node.foldLeft_(bff), this.prefix.foldLeft(bff, z)));
   }

   public A reduceLeft(F<A, F<A, A>> aff) {
      return this.suffix.foldLeft(aff, this.middle.foldLeft(Node.foldLeft_(aff), this.prefix.reduceLeft(aff)));
   }

   public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
      return new Deep(m, this.v, this.prefix.map(abf, m), this.middle.map(Node.liftM(abf, m), m.nodeMeasured()), this.suffix.map(abf, m));
   }

   public V measure() {
      return this.v;
   }

   public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
      return deep.f(this);
   }

   public FingerTree<V, A> cons(final A a) {
      final Measured<V, A> m = this.measured();
      final V measure = m.sum(m.measure(a), this.v);
      final MakeTree<V, A> mk = mkTree(m);
      return (FingerTree)this.prefix.match(new F<One<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(One<V, A> one) {
            return new Deep(m, measure, mk.two(a, one.value()), Deep.this.middle, Deep.this.suffix);
         }
      }, new F<Two<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Two<V, A> two) {
            return new Deep(m, measure, mk.three(a, two.values()._1(), two.values()._2()), Deep.this.middle, Deep.this.suffix);
         }
      }, new F<Three<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Three<V, A> three) {
            return new Deep(m, measure, mk.four(a, three.values()._1(), three.values()._2(), three.values()._3()), Deep.this.middle, Deep.this.suffix);
         }
      }, new F<Four<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Four<V, A> four) {
            return new Deep(m, measure, mk.two(a, four.values()._1()), Deep.this.middle.cons(mk.node3(four.values()._2(), four.values()._3(), four.values()._4())), Deep.this.suffix);
         }
      });
   }

   public FingerTree<V, A> snoc(final A a) {
      final Measured<V, A> m = this.measured();
      final V measure = m.sum(m.measure(a), this.v);
      final MakeTree<V, A> mk = mkTree(m);
      return (FingerTree)this.suffix.match(new F<One<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(One<V, A> one) {
            return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.two(one.value(), a));
         }
      }, new F<Two<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Two<V, A> two) {
            return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.three(two.values()._1(), two.values()._2(), a));
         }
      }, new F<Three<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Three<V, A> three) {
            return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.four(three.values()._1(), three.values()._2(), three.values()._3(), a));
         }
      }, new F<Four<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Four<V, A> four) {
            return new Deep(m, measure, Deep.this.prefix, Deep.this.middle.snoc(mk.node3(four.values()._1(), four.values()._2(), four.values()._3())), mk.two(four.values()._4(), a));
         }
      });
   }

   public FingerTree<V, A> append(final FingerTree<V, A> t) {
      final Measured<V, A> m = this.measured();
      return (FingerTree)t.match(Function.constant(t), new F<Single<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Single<V, A> single) {
            return t.snoc(single.value());
         }
      }, new F<Deep<V, A>, FingerTree<V, A>>() {
         public FingerTree<V, A> f(Deep<V, A> deep) {
            return new Deep(m, m.sum(Deep.this.measure(), deep.measure()), Deep.this.prefix, Deep.addDigits0(m, Deep.this.middle, Deep.this.suffix, deep.prefix, deep.middle), deep.suffix);
         }
      });
   }

   public P2<Integer, A> lookup(F<V, Integer> o, int i) {
      int spr = (Integer)o.f(this.prefix.measure());
      int spm = (Integer)o.f(this.middle.measure());
      if (i < spr) {
         return null;
      } else {
         return i < spm ? null : null;
      }
   }

   private static <V, A> FingerTree<V, Node<V, A>> addDigits0(final Measured<V, A> m, final FingerTree<V, Node<V, A>> m1, Digit<V, A> s1, final Digit<V, A> p2, final FingerTree<V, Node<V, A>> m2) {
      final MakeTree<V, A> mk = mkTree(m);
      return (FingerTree)s1.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(final One<V, A> one1) {
            return (FingerTree)p2.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(One<V, A> one2) {
                  return Deep.append1(m, m1, mk.node2(one1.value(), one2.value()), m2);
               }
            }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Two<V, A> two2) {
                  V2<A> vs = two2.values();
                  return Deep.append1(m, m1, mk.node3(one1.value(), vs._1(), vs._2()), m2);
               }
            }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                  V3<A> vs = three.values();
                  return Deep.append2(m, m1, mk.node2(one1.value(), vs._1()), mk.node2(vs._2(), vs._3()), m2);
               }
            }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                  V4<A> vs = four.values();
                  return Deep.append2(m, m1, mk.node3(one1.value(), vs._1(), vs._2()), mk.node2(vs._3(), vs._4()), m2);
               }
            });
         }
      }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Two<V, A> two1) {
            final V2<A> v1 = two1.values();
            return (FingerTree)p2.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                  return Deep.append1(m, m1, mk.node3(v1._1(), v1._2(), one.value()), m2);
               }
            }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Two<V, A> two2) {
                  V2<A> v2 = two2.values();
                  return Deep.append2(m, m1, mk.node2(v1._1(), v1._2()), mk.node2(v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                  V3<A> v2 = three.values();
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                  V4<A> v2 = four.values();
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Three<V, A> three1) {
            final V3<A> v1 = three1.values();
            return (FingerTree)p2.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                  return Deep.append2(m, m1, mk.node2(v1._1(), v1._2()), mk.node2(v1._3(), one.value()), m2);
               }
            }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Two<V, A> two) {
                  V2<A> v2 = two.values();
                  return Deep.append2(m, m1, mk.node3(v1), mk.node2(v2), m2);
               }
            }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Three<V, A> three2) {
                  return Deep.append2(m, m1, mk.node3(v1), mk.node3(three2.values()), m2);
               }
            }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                  return Deep.append3(m, m1, mk.node3(v1), mk.node2(four.values()._1(), four.values()._2()), mk.node2(four.values()._3(), four.values()._4()), m2);
               }
            });
         }
      }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Four<V, A> four1) {
            final V4<A> v1 = four1.values();
            return (FingerTree)p2.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), one.value()), m2);
               }
            }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Two<V, A> two) {
                  V2<A> v2 = two.values();
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                  V3<A> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Four<V, A> four2) {
                  V4<A> v2 = four2.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, A>> append1(final Measured<V, A> m, final FingerTree<V, Node<V, A>> xs, final Node<V, A> a, final FingerTree<V, Node<V, A>> ys) {
      return (FingerTree)xs.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
            return ys.cons(a);
         }
      }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
            return ys.cons(a).cons(single.value());
         }
      }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep1) {
            return (FingerTree)ys.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                  return xs.snoc(a);
               }
            }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                  return xs.snoc(a).snoc(single.value());
               }
            }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                  Measured<V, Node<V, A>> nm = m.nodeMeasured();
                  return new Deep(nm, m.sum(m.sum(deep1.v, nm.measure(a)), deep2.v), deep1.prefix, Deep.addDigits1(nm, deep1.middle, deep1.suffix, a, deep2.prefix, deep2.middle), deep2.suffix);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits1(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> x, final Node<V, A> n, final Digit<V, Node<V, A>> y, final FingerTree<V, Node<V, Node<V, A>>> m2) {
      final MakeTree<V, Node<V, A>> mk = mkTree(m);
      return (FingerTree)x.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one1) {
            return (FingerTree)y.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                  return Deep.append1(m, m1, mk.node3(one1.value(), n, one2.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append2(m, m1, mk.node2(one1.value(), n), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append2(m, m1, mk.node3(one1.value(), n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append2(m, m1, mk.node3(one1.value(), n, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two1) {
            final V2<Node<V, A>> v1 = two1.values();
            return (FingerTree)y.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node2(v1), mk.node2(n, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node3(three.values()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
            final V3<Node<V, A>> v1 = three.values();
            return (FingerTree)y.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node3(v1), mk.node2(n, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  V2<Node<V, A>> v2 = two.values();
                  return Deep.append2(m, m1, mk.node3(v1), mk.node3(n, v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(v1), mk.node2(n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1), mk.node3(n, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
            final V4<Node<V, A>> v1 = four.values();
            return (FingerTree)y.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), n), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, A>> append2(final Measured<V, A> m, FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final FingerTree<V, Node<V, A>> t2) {
      return (FingerTree)t1.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
            return t2.cons(n2).cons(n1);
         }
      }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
            return t2.cons(n2).cons(n1).cons(single.value());
         }
      }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
            return (FingerTree)t2.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                  return deep.snoc(n1).snoc(n2);
               }
            }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                  return deep.snoc(n1).snoc(n2).snoc(single.value());
               }
            }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                  return new Deep(m.nodeMeasured(), m.sum(m.sum(m.sum(deep.measure(), n1.measure()), n2.measure()), deep2.measure()), deep.prefix, Deep.addDigits2(m.nodeMeasured(), deep.middle, deep.suffix, n1, n2, deep2.prefix, deep2.middle), deep2.suffix);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits2(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
      final MakeTree<V, Node<V, A>> mk = mkTree(m);
      return (FingerTree)suffix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                  return Deep.append2(m, m1, mk.node2(one.value(), n1), mk.node2(n2, one2.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(three.values()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
            final V2<Node<V, A>> v1 = two.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two2) {
                  V2<Node<V, A>> v2 = two2.values();
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
            final V3<Node<V, A>> v1 = three.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node3(v1), mk.node3(n1, n2, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(v1), mk.node2(n1, n2), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                  V3<Node<V, A>> v2 = three2.values();
                  return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
            final V4<Node<V, A>> v1 = four.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), n1), mk.node2(n2, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(three.values()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                  V4<Node<V, A>> v2 = four2.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, A>> append3(Measured<V, A> m, FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final FingerTree<V, Node<V, A>> t2) {
      final Measured<V, Node<V, A>> nm = m.nodeMeasured();
      return (FingerTree)t1.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
            return t2.cons(n3).cons(n2).cons(n1);
         }
      }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
            return t2.cons(n3).cons(n2).cons(n1).cons(single.value());
         }
      }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
            return (FingerTree)t2.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                  return deep.snoc(n1).snoc(n2).snoc(n3);
               }
            }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                  return deep.snoc(n1).snoc(n2).snoc(n3).snoc(single.value());
               }
            }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                  return new Deep(nm, nm.monoid().sumLeft(List.list(deep.v, n1.measure(), n2.measure(), n3.measure(), deep2.v)), deep.prefix, Deep.addDigits3(nm, deep.middle, deep.suffix, n1, n2, n3, deep2.prefix, deep2.middle), deep2.suffix);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits3(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
      final MakeTree<V, Node<V, A>> mk = mkTree(m);
      return (FingerTree)suffix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                  return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, one2.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  V2<Node<V, A>> v2 = two.values();
                  return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
            final V2<Node<V, A>> v1 = two.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, n3), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(final Three<V, Node<V, A>> three) {
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(three.values()), mk.node2(n1, n2), mk.node2(n3, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                  return Deep.append3(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node3(three2.values()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append4(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
            final V4<Node<V, A>> v1 = four.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  V2<Node<V, A>> v2 = two.values();
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                  V4<Node<V, A>> v2 = four2.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, A>> append4(final Measured<V, A> m, final FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Node<V, A> n4, final FingerTree<V, Node<V, A>> t2) {
      final Measured<V, Node<V, A>> nm = m.nodeMeasured();
      return (FingerTree)t1.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
            return t2.cons(n4).cons(n3).cons(n2).cons(n1);
         }
      }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
            return t2.cons(n4).cons(n3).cons(n2).cons(n1).cons(single.value());
         }
      }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
         public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
            return (FingerTree)t2.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                  return t1.snoc(n1).snoc(n2).snoc(n3).snoc(n4);
               }
            }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                  return t1.snoc(n1).snoc(n2).snoc(n3).snoc(n4).snoc(single.value());
               }
            }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() {
               public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                  return new Deep(nm, m.monoid().sumLeft(List.list(deep.v, n1.measure(), n2.measure(), n3.measure(), n4.measure(), deep2.v)), deep.prefix, Deep.addDigits4(nm, deep.middle, deep.suffix, n1, n2, n3, n4, deep2.prefix, deep2.middle), deep2.suffix);
               }
            });
         }
      });
   }

   private static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits4(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Node<V, A> n4, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
      final MakeTree<V, Node<V, A>> mk = mkTree(m);
      return (FingerTree)suffix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                  return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, one2.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, n4), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
            final V2<Node<V, A>> v1 = two.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, n3), mk.node2(n4, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two2) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node2(two2.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node3(three.values()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
            final V3<Node<V, A>> v1 = three.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node2(n4, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  V2<Node<V, A>> v2 = two.values();
                  return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node3(n4, v2._1(), v2._2()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append4(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node2(n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append4(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node3(n4, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
               }
            });
         }
      }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
         public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
            final V4<Node<V, A>> v1 = four.values();
            return (FingerTree)prefix.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                  return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, one.value()), m2);
               }
            }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, n4), mk.node2(two.values()), m2);
               }
            }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                  V3<Node<V, A>> v2 = three.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
               }
            }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() {
               public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                  V4<Node<V, A>> v2 = four.values();
                  return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
               }
            });
         }
      });
   }
}
