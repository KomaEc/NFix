package fj;

import fj.control.parallel.Promise;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Zipper;
import java.util.Iterator;

public class F2Functions {
   public static <A, B, C> F<B, C> f(final F2<A, B, C> f, final A a) {
      return new F<B, C>() {
         public C f(B b) {
            return f.f(a, b);
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

   public static <A, B, C> F2<B, A, C> flip(final F2<A, B, C> f) {
      return new F2<B, A, C>() {
         public C f(B b, A a) {
            return f.f(a, b);
         }
      };
   }

   public static <A, B, C> F<P2<A, B>, C> tuple(final F2<A, B, C> f) {
      return new F<P2<A, B>, C>() {
         public C f(P2<A, B> p) {
            return f.f(p._1(), p._2());
         }
      };
   }

   public static <A, B, C> F2<Array<A>, Array<B>, Array<C>> arrayM(final F2<A, B, C> f) {
      return new F2<Array<A>, Array<B>, Array<C>>() {
         public Array<C> f(Array<A> a, Array<B> b) {
            return a.bind(b, F2Functions.curry(f));
         }
      };
   }

   public static <A, B, C> F2<Promise<A>, Promise<B>, Promise<C>> promiseM(final F2<A, B, C> f) {
      return new F2<Promise<A>, Promise<B>, Promise<C>>() {
         public Promise<C> f(Promise<A> a, Promise<B> b) {
            return a.bind(b, F2Functions.curry(f));
         }
      };
   }

   public static <A, B, C> F2<Iterable<A>, Iterable<B>, IterableW<C>> iterableM(final F2<A, B, C> f) {
      return new F2<Iterable<A>, Iterable<B>, IterableW<C>>() {
         public IterableW<C> f(Iterable<A> a, Iterable<B> b) {
            return (IterableW)((F)IterableW.liftM2(F2Functions.curry(f)).f(a)).f(b);
         }
      };
   }

   public static <A, B, C> F2<List<A>, List<B>, List<C>> listM(final F2<A, B, C> f) {
      return new F2<List<A>, List<B>, List<C>>() {
         public List<C> f(List<A> a, List<B> b) {
            return (List)((F)List.liftM2(F2Functions.curry(f)).f(a)).f(b);
         }
      };
   }

   public static <A, B, C> F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> nelM(final F2<A, B, C> f) {
      return new F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() {
         public NonEmptyList<C> f(NonEmptyList<A> as, NonEmptyList<B> bs) {
            return (NonEmptyList)NonEmptyList.fromList(as.toList().bind(bs.toList(), f)).some();
         }
      };
   }

   public static <A, B, C> F2<Option<A>, Option<B>, Option<C>> optionM(final F2<A, B, C> f) {
      return new F2<Option<A>, Option<B>, Option<C>>() {
         public Option<C> f(Option<A> a, Option<B> b) {
            return (Option)((F)Option.liftM2(F2Functions.curry(f)).f(a)).f(b);
         }
      };
   }

   public static <A, B, C> F2<Set<A>, Set<B>, Set<C>> setM(final F2<A, B, C> f, final Ord<C> o) {
      return new F2<Set<A>, Set<B>, Set<C>>() {
         public Set<C> f(Set<A> as, Set<B> bs) {
            Set<C> cs = Set.empty(o);
            Iterator var4 = as.iterator();

            while(var4.hasNext()) {
               A a = var4.next();

               Object b;
               for(Iterator var6 = bs.iterator(); var6.hasNext(); cs = cs.insert(f.f(a, b))) {
                  b = var6.next();
               }
            }

            return cs;
         }
      };
   }

   public static <A, B, C> F2<Stream<A>, Stream<B>, Stream<C>> streamM(final F2<A, B, C> f) {
      return new F2<Stream<A>, Stream<B>, Stream<C>>() {
         public Stream<C> f(Stream<A> as, Stream<B> bs) {
            return as.bind(bs, f);
         }
      };
   }

   public static <A, B, C> F2<Tree<A>, Tree<B>, Tree<C>> treeM(final F2<A, B, C> f) {
      return new F2<Tree<A>, Tree<B>, Tree<C>>() {
         public Tree<C> f(final Tree<A> as, final Tree<B> bs) {
            return Tree.node(f.f(as.root(), bs.root()), new P1<Stream<Tree<C>>>() {
               public Stream<Tree<C>> _1() {
                  return (Stream)F2Functions.streamM(<VAR_NAMELESS_ENCLOSURE>).f(as.subForest()._1(), bs.subForest()._1());
               }
            });
         }
      };
   }

   public static <A, B, C> F2<Array<A>, Array<B>, Array<C>> zipArrayM(final F2<A, B, C> f) {
      return new F2<Array<A>, Array<B>, Array<C>>() {
         public Array<C> f(Array<A> as, Array<B> bs) {
            return as.zipWith(bs, f);
         }
      };
   }

   public static <A, B, C> F2<Iterable<A>, Iterable<B>, Iterable<C>> zipIterableM(final F2<A, B, C> f) {
      return new F2<Iterable<A>, Iterable<B>, Iterable<C>>() {
         public Iterable<C> f(Iterable<A> as, Iterable<B> bs) {
            return IterableW.wrap(as).zipWith(bs, f);
         }
      };
   }

   public static <A, B, C> F2<List<A>, List<B>, List<C>> zipListM(final F2<A, B, C> f) {
      return new F2<List<A>, List<B>, List<C>>() {
         public List<C> f(List<A> as, List<B> bs) {
            return as.zipWith(bs, f);
         }
      };
   }

   public static <A, B, C> F2<Stream<A>, Stream<B>, Stream<C>> zipStreamM(final F2<A, B, C> f) {
      return new F2<Stream<A>, Stream<B>, Stream<C>>() {
         public Stream<C> f(Stream<A> as, Stream<B> bs) {
            return as.zipWith(bs, f);
         }
      };
   }

   public static <A, B, C> F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> zipNelM(final F2<A, B, C> f) {
      return new F2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() {
         public NonEmptyList<C> f(NonEmptyList<A> as, NonEmptyList<B> bs) {
            return (NonEmptyList)NonEmptyList.fromList(as.toList().zipWith(bs.toList(), f)).some();
         }
      };
   }

   public static <A, B, C> F2<Set<A>, Set<B>, Set<C>> zipSetM(final F2<A, B, C> f, final Ord<C> o) {
      return new F2<Set<A>, Set<B>, Set<C>>() {
         public Set<C> f(Set<A> as, Set<B> bs) {
            return Set.iterableSet(o, as.toStream().zipWith(bs.toStream(), f));
         }
      };
   }

   public static <A, B, C> F2<Tree<A>, Tree<B>, Tree<C>> zipTreeM(final F2<A, B, C> f) {
      return new F2<Tree<A>, Tree<B>, Tree<C>>() {
         public Tree<C> f(final Tree<A> ta, final Tree<B> tb) {
            return Tree.node(f.f(ta.root(), tb.root()), new P1<Stream<Tree<C>>>() {
               public Stream<Tree<C>> _1() {
                  return (Stream)F2Functions.zipStreamM(<VAR_NAMELESS_ENCLOSURE>).f(ta.subForest()._1(), tb.subForest()._1());
               }
            });
         }
      };
   }

   public static <A, B, C> F2<Zipper<A>, Zipper<B>, Zipper<C>> zipZipperM(final F2<A, B, C> f) {
      return new F2<Zipper<A>, Zipper<B>, Zipper<C>>() {
         public Zipper<C> f(Zipper<A> ta, Zipper<B> tb) {
            F2<Stream<A>, Stream<B>, Stream<C>> sf = F2Functions.zipStreamM(f);
            return Zipper.zipper((Stream)sf.f(ta.lefts(), tb.lefts()), f.f(ta.focus(), tb.focus()), (Stream)sf.f(ta.rights(), tb.rights()));
         }
      };
   }

   public static <A, B, C> F2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>> zipTreeZipperM(final F2<A, B, C> f) {
      return new F2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>>() {
         public TreeZipper<C> f(TreeZipper<A> ta, TreeZipper<B> tb) {
            F2<Stream<Tree<A>>, Stream<Tree<B>>, Stream<Tree<C>>> sf = F2Functions.zipStreamM(F2Functions.treeM(f));
            F2<Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>, Stream<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>, Stream<P3<Stream<Tree<C>>, C, Stream<Tree<C>>>>> pf = F2Functions.zipStreamM(new F2<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, P3<Stream<Tree<B>>, B, Stream<Tree<B>>>, P3<Stream<Tree<C>>, C, Stream<Tree<C>>>>() {
               public P3<Stream<Tree<C>>, C, Stream<Tree<C>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> pa, P3<Stream<Tree<B>>, B, Stream<Tree<B>>> pb) {
                  return P.p(F2Functions.zipStreamM(F2Functions.treeM(f)).f(pa._1(), pb._1()), f.f(pa._2(), pb._2()), F2Functions.zipStreamM(F2Functions.treeM(f)).f(pa._3(), pb._3()));
               }
            });
            return TreeZipper.treeZipper((Tree)F2Functions.treeM(f).f(ta.p()._1(), tb.p()._1()), (Stream)sf.f(ta.lefts(), tb.lefts()), (Stream)sf.f(ta.rights(), tb.rights()), (Stream)pf.f(ta.p()._4(), tb.p()._4()));
         }
      };
   }

   public static <A, B, C, Z> F2<Z, B, C> contramapFirst(F2<A, B, C> target, F<Z, A> f) {
      return F2Functions$$Lambda$1.lambdaFactory$(target, f);
   }

   public static <A, B, C, Z> F2<A, Z, C> contramapSecond(F2<A, B, C> target, F<Z, B> f) {
      return F2Functions$$Lambda$2.lambdaFactory$(target, f);
   }

   public static <A, B, C, X, Y> F2<X, Y, C> contramap(F2<A, B, C> target, F<X, A> f, F<Y, B> g) {
      return contramapSecond(contramapFirst(target, f), g);
   }

   public static <A, B, C, Z> F2<A, B, Z> map(F2<A, B, C> target, F<C, Z> f) {
      return F2Functions$$Lambda$3.lambdaFactory$(f, target);
   }

   // $FF: synthetic method
   private static Object lambda$map$50(F var0, F2 var1, Object a, Object b) {
      return var0.f(var1.f(a, b));
   }

   // $FF: synthetic method
   private static Object lambda$contramapSecond$49(F2 var0, F var1, Object a, Object z) {
      return var0.f(a, var1.f(z));
   }

   // $FF: synthetic method
   private static Object lambda$contramapFirst$48(F2 var0, F var1, Object z, Object b) {
      return var0.f(var1.f(z), b);
   }

   // $FF: synthetic method
   static Object access$lambda$0(F2 var0, F var1, Object var2, Object var3) {
      return lambda$contramapFirst$48(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   static Object access$lambda$1(F2 var0, F var1, Object var2, Object var3) {
      return lambda$contramapSecond$49(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   static Object access$lambda$2(F var0, F2 var1, Object var2, Object var3) {
      return lambda$map$50(var0, var1, var2, var3);
   }
}
