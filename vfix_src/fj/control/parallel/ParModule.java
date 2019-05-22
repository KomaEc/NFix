package fj.control.parallel;

import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.Monoid;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.Unit;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Zipper;
import fj.function.Effect1;

public final class ParModule {
   private final Strategy<Unit> strategy;

   private ParModule(Strategy<Unit> strategy) {
      this.strategy = strategy;
   }

   public static ParModule parModule(Strategy<Unit> u) {
      return new ParModule(u);
   }

   public <A> Promise<A> promise(P1<A> p) {
      return Promise.promise(this.strategy, p);
   }

   public <A> F<P1<A>, Promise<A>> promise() {
      return new F<P1<A>, Promise<A>>() {
         public Promise<A> f(P1<A> ap1) {
            return ParModule.this.promise(ap1);
         }
      };
   }

   public <A, B> F<A, Promise<B>> promise(F<A, B> f) {
      return F1Functions.promiseK(f, this.strategy);
   }

   public <A, B> F<F<A, B>, F<A, Promise<B>>> promisePure() {
      return new F<F<A, B>, F<A, Promise<B>>>() {
         public F<A, Promise<B>> f(F<A, B> abf) {
            return ParModule.this.promise(abf);
         }
      };
   }

   public <A, B, C> F2<A, B, Promise<C>> promise(F2<A, B, C> f) {
      return P2.untuple(F1Functions.promiseK(F2Functions.tuple(f), this.strategy));
   }

   public <A> Actor<A> effect(Effect1<A> e) {
      return Actor.actor(this.strategy, e);
   }

   public <A> F<Effect1<A>, Actor<A>> effect() {
      return new F<Effect1<A>, Actor<A>>() {
         public Actor<A> f(Effect1<A> effect) {
            return ParModule.this.effect(effect);
         }
      };
   }

   public <A> Actor<A> actor(Effect1<A> e) {
      return Actor.queueActor(this.strategy, e);
   }

   public <A> F<Effect1<A>, Actor<A>> actor() {
      return new F<Effect1<A>, Actor<A>>() {
         public Actor<A> f(Effect1<A> effect) {
            return ParModule.this.actor(effect);
         }
      };
   }

   public <A> Promise<List<A>> sequence(List<Promise<A>> ps) {
      return Promise.sequence(this.strategy, ps);
   }

   public <A> F<List<Promise<A>>, Promise<List<A>>> sequenceList() {
      return new F<List<Promise<A>>, Promise<List<A>>>() {
         public Promise<List<A>> f(List<Promise<A>> list) {
            return ParModule.this.sequence(list);
         }
      };
   }

   public <A> Promise<Stream<A>> sequence(Stream<Promise<A>> ps) {
      return Promise.sequence(this.strategy, ps);
   }

   public <A> F<Stream<Promise<A>>, Promise<Stream<A>>> sequenceStream() {
      return new F<Stream<Promise<A>>, Promise<Stream<A>>>() {
         public Promise<Stream<A>> f(Stream<Promise<A>> stream) {
            return ParModule.this.sequence(stream);
         }
      };
   }

   public <A> Promise<P1<A>> sequence(P1<Promise<A>> p) {
      return Promise.sequence(this.strategy, p);
   }

   public <A, B> Promise<List<B>> mapM(List<A> as, F<A, Promise<B>> f) {
      return this.sequence(as.map(f));
   }

   public <A, B> F<F<A, Promise<B>>, F<List<A>, Promise<List<B>>>> mapList() {
      return Function.curry(new F2<F<A, Promise<B>>, List<A>, Promise<List<B>>>() {
         public Promise<List<B>> f(F<A, Promise<B>> f, List<A> list) {
            return ParModule.this.mapM(list, f);
         }
      });
   }

   public <A, B> Promise<Stream<B>> mapM(Stream<A> as, F<A, Promise<B>> f) {
      return this.sequence(as.map(f));
   }

   public <A, B> F<F<A, Promise<B>>, F<Stream<A>, Promise<Stream<B>>>> mapStream() {
      return Function.curry(new F2<F<A, Promise<B>>, Stream<A>, Promise<Stream<B>>>() {
         public Promise<Stream<B>> f(F<A, Promise<B>> f, Stream<A> stream) {
            return ParModule.this.mapM(stream, f);
         }
      });
   }

   public <A, B> Promise<P1<B>> mapM(P1<A> a, F<A, Promise<B>> f) {
      return this.sequence(a.map(f));
   }

   public <A, B> Promise<List<B>> parMap(List<A> as, F<A, B> f) {
      return this.mapM(as, this.promise(f));
   }

   public <A, B> F<F<A, B>, F<List<A>, Promise<List<B>>>> parMapList() {
      return Function.curry(new F2<F<A, B>, List<A>, Promise<List<B>>>() {
         public Promise<List<B>> f(F<A, B> abf, List<A> list) {
            return ParModule.this.parMap(list, abf);
         }
      });
   }

   public <A, B> Promise<NonEmptyList<B>> parMap(NonEmptyList<A> as, F<A, B> f) {
      return this.mapM(as.toList(), this.promise(f)).fmap(new F<List<B>, NonEmptyList<B>>() {
         public NonEmptyList<B> f(List<B> list) {
            return (NonEmptyList)NonEmptyList.fromList(list).some();
         }
      });
   }

   public <A, B> Promise<Stream<B>> parMap(Stream<A> as, F<A, B> f) {
      return this.mapM(as, this.promise(f));
   }

   public <A, B> F<F<A, B>, F<Stream<A>, Promise<Stream<B>>>> parMapStream() {
      return Function.curry(new F2<F<A, B>, Stream<A>, Promise<Stream<B>>>() {
         public Promise<Stream<B>> f(F<A, B> abf, Stream<A> stream) {
            return ParModule.this.parMap(stream, abf);
         }
      });
   }

   public <A, B> Promise<Iterable<B>> parMap(Iterable<A> as, F<A, B> f) {
      return this.parMap(Stream.iterableStream(as), f).fmap(Function.vary(Function.identity()));
   }

   public <A, B> F<F<A, B>, F<Iterable<A>, Promise<Iterable<B>>>> parMapIterable() {
      return Function.curry(new F2<F<A, B>, Iterable<A>, Promise<Iterable<B>>>() {
         public Promise<Iterable<B>> f(F<A, B> abf, Iterable<A> iterable) {
            return ParModule.this.parMap(iterable, abf);
         }
      });
   }

   public <A, B> Promise<Array<B>> parMap(Array<A> as, F<A, B> f) {
      return this.parMap(as.toStream(), f).fmap(new F<Stream<B>, Array<B>>() {
         public Array<B> f(Stream<B> stream) {
            return stream.toArray();
         }
      });
   }

   public <A, B> F<F<A, B>, F<Array<A>, Promise<Array<B>>>> parMapArray() {
      return Function.curry(new F2<F<A, B>, Array<A>, Promise<Array<B>>>() {
         public Promise<Array<B>> f(F<A, B> abf, Array<A> array) {
            return ParModule.this.parMap(array, abf);
         }
      });
   }

   public <A, B> Promise<Zipper<B>> parMap(Zipper<A> za, F<A, B> f) {
      return this.parMap(za.rights(), f).apply(((Promise)this.promise(f).f(za.focus())).apply(this.parMap(za.lefts(), f).fmap(Function.curry(Zipper.zipper()))));
   }

   public <A, B> Promise<Tree<B>> parMap(Tree<A> ta, F<A, B> f) {
      return this.mapM(ta.subForest(), (F)this.mapStream().f(this.parMapTree().f(f))).apply(((Promise)this.promise(f).f(ta.root())).fmap(Tree.node()));
   }

   public <A, B> F<F<A, B>, F<Tree<A>, Promise<Tree<B>>>> parMapTree() {
      return Function.curry(new F2<F<A, B>, Tree<A>, Promise<Tree<B>>>() {
         public Promise<Tree<B>> f(F<A, B> abf, Tree<A> tree) {
            return ParModule.this.parMap(tree, abf);
         }
      });
   }

   public <A, B> Promise<TreeZipper<B>> parMap(TreeZipper<A> za, final F<A, B> f) {
      final F<Tree<A>, Tree<B>> tf = (F)Tree.fmap_().f(f);
      P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> p = za.p();
      return this.mapM((Stream)p._4(), new F<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, Promise<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>>() {
         public Promise<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p3) {
            return ParModule.this.parMap((Stream)p3._3(), tf).apply(((Promise)ParModule.this.promise(f).f(p3._2())).apply(ParModule.this.parMap((Stream)p3._1(), tf).fmap(P.p3())));
         }
      }).apply(this.parMap(za.rights(), tf).apply(this.parMap(za.lefts(), tf).apply(this.parMap((Tree)p._1(), f).fmap(TreeZipper.treeZipper()))));
   }

   public <A, B> Promise<List<B>> parFlatMap(List<A> as, F<A, List<B>> f) {
      return this.parFoldMap((Iterable)as, f, Monoid.listMonoid());
   }

   public <A, B> Promise<Stream<B>> parFlatMap(Stream<A> as, F<A, Stream<B>> f) {
      return this.parFoldMap(as, f, Monoid.streamMonoid());
   }

   public <A, B> Promise<Array<B>> parFlatMap(Array<A> as, F<A, Array<B>> f) {
      return this.parMap(as, f).fmap(Array.join());
   }

   public <A, B> Promise<Iterable<B>> parFlatMap(Iterable<A> as, F<A, Iterable<B>> f) {
      return this.parMap(as, f).fmap(IterableW.join()).fmap(Function.vary(Function.identity()));
   }

   public <A, B, C> Promise<List<C>> parZipWith(List<A> as, List<B> bs, F<A, F<B, C>> f) {
      return this.sequence(as.zipWith(bs, this.promise(Function.uncurryF2(f))));
   }

   public <A, B, C> Promise<Stream<C>> parZipWith(Stream<A> as, Stream<B> bs, F<A, F<B, C>> f) {
      return this.sequence(as.zipWith(bs, this.promise(Function.uncurryF2(f))));
   }

   public <A, B, C> Promise<Array<C>> parZipWith(Array<A> as, Array<B> bs, F<A, F<B, C>> f) {
      return this.parZipWith(as.toStream(), bs.toStream(), f).fmap(new F<Stream<C>, Array<C>>() {
         public Array<C> f(Stream<C> stream) {
            return stream.toArray();
         }
      });
   }

   public <A, B, C> Promise<Iterable<C>> parZipWith(Iterable<A> as, Iterable<B> bs, F<A, F<B, C>> f) {
      return this.parZipWith(Stream.iterableStream(as), Stream.iterableStream(bs), f).fmap(Function.vary(Function.identity()));
   }

   public <A, B> Promise<B> parFoldMap(Stream<A> as, F<A, B> map, Monoid<B> reduce) {
      return as.isEmpty() ? this.promise(P.p(reduce.zero())) : (Promise)as.map(this.promise(map)).foldLeft1(Promise.liftM2(reduce.sum()));
   }

   public <A, B> Promise<B> parFoldMap(Stream<A> as, F<A, B> map, final Monoid<B> reduce, final F<Stream<A>, P2<Stream<A>, Stream<A>>> chunking) {
      return this.parMap(Stream.unfold(new F<Stream<A>, Option<P2<Stream<A>, Stream<A>>>>() {
         public Option<P2<Stream<A>, Stream<A>>> f(Stream<A> stream) {
            return stream.isEmpty() ? Option.none() : Option.some(chunking.f(stream));
         }
      }, as), (F)Stream.map_().f(map)).bind(new F<Stream<Stream<B>>, Promise<B>>() {
         public Promise<B> f(Stream<Stream<B>> stream) {
            return ParModule.this.parMap(stream, reduce.sumLeftS()).fmap(reduce.sumLeftS());
         }
      });
   }

   public <A, B> Promise<B> parFoldMap(Iterable<A> as, F<A, B> map, Monoid<B> reduce, final F<Iterable<A>, P2<Iterable<A>, Iterable<A>>> chunking) {
      return this.parFoldMap(Stream.iterableStream(as), map, reduce, new F<Stream<A>, P2<Stream<A>, Stream<A>>>() {
         public P2<Stream<A>, Stream<A>> f(Stream<A> stream) {
            F<Iterable<A>, Stream<A>> is = new F<Iterable<A>, Stream<A>>() {
               public Stream<A> f(Iterable<A> iterable) {
                  return Stream.iterableStream(iterable);
               }
            };
            return ((P2)chunking.f(stream)).map1(is).map2(is);
         }
      });
   }

   public <A, B> Promise<B> parFoldMap(Iterable<A> as, F<A, B> map, Monoid<B> reduce) {
      return this.parFoldMap(Stream.iterableStream(as), map, reduce);
   }

   public <A, B> Promise<Zipper<B>> parExtend(Zipper<A> za, F<Zipper<A>, B> f) {
      return this.parMap(za.positions(), f);
   }

   public <A, B> Promise<Tree<B>> parExtend(Tree<A> ta, F<Tree<A>, B> f) {
      return this.parMap(ta.cojoin(), f);
   }

   public <A, B> Promise<TreeZipper<B>> parExtend(TreeZipper<A> za, F<TreeZipper<A>, B> f) {
      return this.parMap(za.positions(), f);
   }

   public <A, B> Promise<NonEmptyList<B>> parExtend(NonEmptyList<A> as, F<NonEmptyList<A>, B> f) {
      return this.parMap(as.tails(), f);
   }
}
