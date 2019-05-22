package fj.data;

import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.Monoid;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Show;
import java.util.Collection;
import java.util.Iterator;

public final class Tree<A> implements Iterable<A> {
   private final A root;
   private final P1<Stream<Tree<A>>> subForest;

   public Iterator<A> iterator() {
      return this.flatten().iterator();
   }

   private Tree(A root, P1<Stream<Tree<A>>> subForest) {
      this.root = root;
      this.subForest = subForest;
   }

   public static <A> Tree<A> leaf(A root) {
      return node(root, Stream.nil());
   }

   public static <A> Tree<A> node(A root, P1<Stream<Tree<A>>> forest) {
      return new Tree(root, forest);
   }

   public static <A> Tree<A> node(A root, Stream<Tree<A>> forest) {
      return new Tree(root, P.p(forest));
   }

   public static <A> Tree<A> node(A root, List<Tree<A>> forest) {
      return node(root, forest.toStream());
   }

   public static <A> F<A, F<P1<Stream<Tree<A>>>, Tree<A>>> node() {
      return Function.curry(new F2<A, P1<Stream<Tree<A>>>, Tree<A>>() {
         public Tree<A> f(A a, P1<Stream<Tree<A>>> p1) {
            return Tree.node(a, p1);
         }
      });
   }

   public A root() {
      return this.root;
   }

   public P1<Stream<Tree<A>>> subForest() {
      return this.subForest;
   }

   public static <A> F<Tree<A>, A> root_() {
      return new F<Tree<A>, A>() {
         public A f(Tree<A> a) {
            return a.root();
         }
      };
   }

   public static <A> F<Tree<A>, P1<Stream<Tree<A>>>> subForest_() {
      return new F<Tree<A>, P1<Stream<Tree<A>>>>() {
         public P1<Stream<Tree<A>>> f(Tree<A> a) {
            return a.subForest();
         }
      };
   }

   public Stream<A> flatten() {
      F2<Tree<A>, P1<Stream<A>>, Stream<A>> squish = new F2<Tree<A>, P1<Stream<A>>, Stream<A>>() {
         public Stream<A> f(Tree<A> t, P1<Stream<A>> xs) {
            return Stream.cons(t.root(), t.subForest().map((F)((F)Stream.foldRight().f(F2Functions.curry(this))).f(xs._1())));
         }
      };
      return (Stream)squish.f(this, P.p(Stream.nil()));
   }

   public static <A> F<Tree<A>, Stream<A>> flatten_() {
      return new F<Tree<A>, Stream<A>>() {
         public Stream<A> f(Tree<A> t) {
            return t.flatten();
         }
      };
   }

   public Stream<Stream<A>> levels() {
      F<Stream<Tree<A>>, Stream<Tree<A>>> flatSubForests = (F)Stream.bind_().f(Function.compose(P1.__1(), subForest_()));
      F<Stream<Tree<A>>, Stream<A>> roots = (F)Stream.map_().f(root_());
      return Stream.iterateWhile(flatSubForests, Stream.isNotEmpty_(), Stream.single(this)).map(roots);
   }

   public <B> Tree<B> fmap(F<A, B> f) {
      return node(f.f(this.root()), this.subForest().map((F)Stream.map_().f(fmap_().f(f))));
   }

   public static <A, B> F<F<A, B>, F<Tree<A>, Tree<B>>> fmap_() {
      return new F<F<A, B>, F<Tree<A>, Tree<B>>>() {
         public F<Tree<A>, Tree<B>> f(final F<A, B> f) {
            return new F<Tree<A>, Tree<B>>() {
               public Tree<B> f(Tree<A> a) {
                  return a.fmap(f);
               }
            };
         }
      };
   }

   public <B> B foldMap(F<A, B> f, Monoid<B> m) {
      return m.sum(f.f(this.root()), m.sumRight(((Stream)this.subForest()._1()).map(foldMap_(f, m)).toList()));
   }

   public Collection<A> toCollection() {
      return this.flatten().toCollection();
   }

   public static <A, B> F<Tree<A>, B> foldMap_(final F<A, B> f, final Monoid<B> m) {
      return new F<Tree<A>, B>() {
         public B f(Tree<A> t) {
            return t.foldMap(f, m);
         }
      };
   }

   public static <A, B> F<B, Tree<A>> unfoldTree(final F<B, P2<A, P1<Stream<B>>>> f) {
      return new F<B, Tree<A>>() {
         public Tree<A> f(B b) {
            P2<A, P1<Stream<B>>> p = (P2)f.f(b);
            return Tree.node(p._1(), ((P1)p._2()).map((F)Stream.map_().f(Tree.unfoldTree(f))));
         }
      };
   }

   public <B> Tree<B> cobind(final F<Tree<A>, B> f) {
      return (Tree)unfoldTree(new F<Tree<A>, P2<B, P1<Stream<Tree<A>>>>>() {
         public P2<B, P1<Stream<Tree<A>>>> f(Tree<A> t) {
            return P.p(f.f(t), t.subForest());
         }
      }).f(this);
   }

   public Tree<Tree<A>> cojoin() {
      F<Tree<A>, Tree<A>> id = Function.identity();
      return this.cobind(id);
   }

   private static <A> Stream<String> drawSubTrees(Show<A> s, Stream<Tree<A>> ts) {
      return ts.isEmpty() ? Stream.nil() : (((Stream)ts.tail()._1()).isEmpty() ? shift("`- ", "   ", ((Tree)ts.head()).drawTree(s)).cons("|") : shift("+- ", "|  ", ((Tree)ts.head()).drawTree(s)).append(drawSubTrees(s, (Stream)ts.tail()._1())));
   }

   private static Stream<String> shift(String f, String o, Stream<String> s) {
      return Stream.repeat(o).cons(f).zipWith(s, Monoid.stringMonoid.sum());
   }

   private Stream<String> drawTree(Show<A> s) {
      return drawSubTrees(s, (Stream)this.subForest._1()).cons(s.showS(this.root));
   }

   public String draw(Show<A> s) {
      return (String)Monoid.stringMonoid.join(this.drawTree(s), "\n");
   }

   public static <A> Show<Tree<A>> show2D(final Show<A> s) {
      return Show.showS(new F<Tree<A>, String>() {
         public String f(Tree<A> tree) {
            return tree.draw(s);
         }
      });
   }

   public <B, C> Tree<C> zipWith(Tree<B> bs, F2<A, B, C> f) {
      return (Tree)F2Functions.zipTreeM(f).f(this, bs);
   }

   public <B, C> Tree<C> zipWith(Tree<B> bs, F<A, F<B, C>> f) {
      return this.zipWith(bs, Function.uncurryF2(f));
   }

   public static <A, B> Tree<B> bottomUp(Tree<A> t, final F<P2<A, Stream<B>>, B> f) {
      F<Tree<A>, Tree<B>> recursiveCall = new F<Tree<A>, Tree<B>>() {
         public Tree<B> f(Tree<A> a) {
            return Tree.bottomUp(a, f);
         }
      };
      Stream<Tree<B>> tbs = ((Stream)t.subForest()._1()).map(recursiveCall);
      return node(f.f(P.p(t.root(), tbs.map(getRoot()))), tbs);
   }

   private static <A> F<Tree<A>, A> getRoot() {
      return new F<Tree<A>, A>() {
         public A f(Tree<A> a) {
            return a.root();
         }
      };
   }
}
