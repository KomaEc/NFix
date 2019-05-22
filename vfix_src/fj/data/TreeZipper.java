package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.F4;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.Show;
import fj.function.Booleans;
import java.util.Iterator;

public final class TreeZipper<A> implements Iterable<TreeZipper<A>> {
   private final Tree<A> tree;
   private final Stream<Tree<A>> lefts;
   private final Stream<Tree<A>> rights;
   private final Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents;

   public Iterator<TreeZipper<A>> iterator() {
      return this.positions().toTree().iterator();
   }

   private TreeZipper(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
      this.tree = tree;
      this.lefts = lefts;
      this.rights = rights;
      this.parents = parents;
   }

   public static <A> TreeZipper<A> treeZipper(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
      return new TreeZipper(tree, lefts, rights, parents);
   }

   public static <A> F<Tree<A>, F<Stream<Tree<A>>, F<Stream<Tree<A>>, F<Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>, TreeZipper<A>>>>> treeZipper() {
      return Function.curry(new F4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>, TreeZipper<A>>() {
         public TreeZipper<A> f(Tree<A> tree, Stream<Tree<A>> lefts, Stream<Tree<A>> rights, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> parents) {
            return TreeZipper.treeZipper(tree, lefts, rights, parents);
         }
      });
   }

   public P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> p() {
      return P.p(this.tree, this.lefts, this.rights, this.parents);
   }

   public static <A> F<TreeZipper<A>, P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>>> p_() {
      return new F<TreeZipper<A>, P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>>>() {
         public P4<Tree<A>, Stream<Tree<A>>, Stream<Tree<A>>, Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>> f(TreeZipper<A> a) {
            return a.p();
         }
      };
   }

   public static <A> Equal<TreeZipper<A>> eq(Equal<A> e) {
      return Equal.p4Equal(Equal.treeEqual(e), Equal.streamEqual(Equal.treeEqual(e)), Equal.streamEqual(Equal.treeEqual(e)), Equal.streamEqual(Equal.p3Equal(Equal.streamEqual(Equal.treeEqual(e)), e, Equal.streamEqual(Equal.treeEqual(e))))).comap(p_());
   }

   public static <A> Show<TreeZipper<A>> show(Show<A> s) {
      return Show.p4Show(Show.treeShow(s), Show.streamShow(Show.treeShow(s)), Show.streamShow(Show.treeShow(s)), Show.streamShow(Show.p3Show(Show.streamShow(Show.treeShow(s)), s, Show.streamShow(Show.treeShow(s))))).comap(p_());
   }

   private static <A> Stream<Tree<A>> combChildren(Stream<Tree<A>> ls, Tree<A> t, Stream<Tree<A>> rs) {
      return (Stream)ls.foldLeft((F)Function.compose(Function.flip(Stream.cons()), P.p1()), Stream.cons(t, P.p(rs)));
   }

   public Option<TreeZipper<A>> parent() {
      if (this.parents.isEmpty()) {
         return Option.none();
      } else {
         P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p = (P3)this.parents.head();
         return Option.some(treeZipper(Tree.node(p._2(), combChildren(this.lefts, this.tree, this.rights)), (Stream)p._1(), (Stream)p._3(), (Stream)this.parents.tail()._1()));
      }
   }

   public TreeZipper<A> root() {
      return (TreeZipper)this.parent().option((Object)this, root_());
   }

   public static <A> F<TreeZipper<A>, TreeZipper<A>> root_() {
      return new F<TreeZipper<A>, TreeZipper<A>>() {
         public TreeZipper<A> f(TreeZipper<A> a) {
            return a.root();
         }
      };
   }

   public Option<TreeZipper<A>> left() {
      return this.lefts.isEmpty() ? Option.none() : Option.some(treeZipper((Tree)this.lefts.head(), (Stream)this.lefts.tail()._1(), this.rights.cons(this.tree), this.parents));
   }

   public Option<TreeZipper<A>> right() {
      return this.rights.isEmpty() ? Option.none() : Option.some(treeZipper((Tree)this.rights.head(), this.lefts.cons(this.tree), (Stream)this.rights.tail()._1(), this.parents));
   }

   public Option<TreeZipper<A>> firstChild() {
      Stream<Tree<A>> ts = (Stream)this.tree.subForest()._1();
      return ts.isEmpty() ? Option.none() : Option.some(treeZipper((Tree)ts.head(), Stream.nil(), (Stream)ts.tail()._1(), this.downParents()));
   }

   public Option<TreeZipper<A>> lastChild() {
      Stream<Tree<A>> ts = ((Stream)this.tree.subForest()._1()).reverse();
      return ts.isEmpty() ? Option.none() : Option.some(treeZipper((Tree)ts.head(), (Stream)ts.tail()._1(), Stream.nil(), this.downParents()));
   }

   public Option<TreeZipper<A>> getChild(int n) {
      Option<TreeZipper<A>> r = Option.none();

      P2 lr;
      for(Iterator var3 = splitChildren(Stream.nil(), (Stream)this.tree.subForest()._1(), n).iterator(); var3.hasNext(); r = Option.some(treeZipper((Tree)((Stream)lr._1()).head(), (Stream)((Stream)lr._1()).tail()._1(), (Stream)lr._2(), this.downParents()))) {
         lr = (P2)var3.next();
      }

      return r;
   }

   public Option<TreeZipper<A>> findChild(final F<Tree<A>, Boolean> p) {
      Option<TreeZipper<A>> r = Option.none();
      F2<Stream<Tree<A>>, Stream<Tree<A>>, Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>>> split = new F2<Stream<Tree<A>>, Stream<Tree<A>>, Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>>>() {
         public Option<P3<Stream<Tree<A>>, Tree<A>, Stream<Tree<A>>>> f(Stream<Tree<A>> acc, Stream<Tree<A>> xs) {
            return xs.isNotEmpty() ? ((Boolean)p.f(xs.head()) ? Option.some(P.p(acc, xs.head(), xs.tail()._1())) : this.f(acc.cons(xs.head()), (Stream)xs.tail()._1())) : Option.none();
         }
      };
      Stream<Tree<A>> subforest = (Stream)this.tree.subForest()._1();
      P3 ltr;
      if (subforest.isNotEmpty()) {
         for(Iterator var5 = ((Option)split.f(Stream.nil(), subforest)).iterator(); var5.hasNext(); r = Option.some(treeZipper((Tree)ltr._2(), (Stream)ltr._1(), (Stream)ltr._3(), this.downParents()))) {
            ltr = (P3)var5.next();
         }
      }

      return r;
   }

   private Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> downParents() {
      return this.parents.cons(P.p(this.lefts, this.tree.root(), this.rights));
   }

   private static <A> Option<P2<Stream<A>, Stream<A>>> splitChildren(Stream<A> acc, Stream<A> xs, int n) {
      return n == 0 ? Option.some(P.p(acc, xs)) : (xs.isNotEmpty() ? splitChildren(acc.cons(xs.head()), (Stream)xs.tail()._1(), n - 1) : Option.none());
   }

   private static <A> Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>> lp3nil() {
      return Stream.nil();
   }

   public static <A> TreeZipper<A> fromTree(Tree<A> t) {
      return treeZipper(t, Stream.nil(), Stream.nil(), lp3nil());
   }

   public static <A> Option<TreeZipper<A>> fromForest(Stream<Tree<A>> ts) {
      return ts.isNotEmpty() ? Option.some(treeZipper((Tree)ts.head(), Stream.nil(), (Stream)ts.tail()._1(), lp3nil())) : Option.none();
   }

   public Tree<A> toTree() {
      return this.root().tree;
   }

   public Stream<Tree<A>> toForest() {
      TreeZipper<A> r = this.root();
      return combChildren(r.lefts, r.tree, r.rights);
   }

   public Tree<A> focus() {
      return this.tree;
   }

   public Stream<Tree<A>> lefts() {
      return this.lefts;
   }

   public Stream<Tree<A>> rights() {
      return this.rights;
   }

   public boolean isRoot() {
      return this.parents.isEmpty();
   }

   public boolean isFirst() {
      return this.lefts.isEmpty();
   }

   public boolean isLast() {
      return this.rights.isEmpty();
   }

   public boolean isLeaf() {
      return ((Stream)this.tree.subForest()._1()).isEmpty();
   }

   public boolean isChild() {
      return !this.isRoot();
   }

   public boolean hasChildren() {
      return !this.isLeaf();
   }

   public TreeZipper<A> setTree(Tree<A> t) {
      return treeZipper(t, this.lefts, this.rights, this.parents);
   }

   public TreeZipper<A> modifyTree(F<Tree<A>, Tree<A>> f) {
      return this.setTree((Tree)f.f(this.tree));
   }

   public TreeZipper<A> modifyLabel(F<A, A> f) {
      return this.setLabel(f.f(this.getLabel()));
   }

   public TreeZipper<A> setLabel(final A v) {
      return this.modifyTree(new F<Tree<A>, Tree<A>>() {
         public Tree<A> f(Tree<A> t) {
            return Tree.node(v, t.subForest());
         }
      });
   }

   public A getLabel() {
      return this.tree.root();
   }

   public TreeZipper<A> insertLeft(Tree<A> t) {
      return treeZipper(t, this.lefts, this.rights.cons(this.tree), this.parents);
   }

   public TreeZipper<A> insertRight(Tree<A> t) {
      return treeZipper(t, this.lefts.cons(this.tree), this.rights, this.parents);
   }

   public TreeZipper<A> insertDownFirst(Tree<A> t) {
      return treeZipper(t, Stream.nil(), (Stream)this.tree.subForest()._1(), this.downParents());
   }

   public TreeZipper<A> insertDownLast(Tree<A> t) {
      return treeZipper(t, ((Stream)this.tree.subForest()._1()).reverse(), Stream.nil(), this.downParents());
   }

   public Option<TreeZipper<A>> insertDownAt(int n, Tree<A> t) {
      Option<TreeZipper<A>> r = Option.none();

      P2 lr;
      for(Iterator var4 = splitChildren(Stream.nil(), (Stream)this.tree.subForest()._1(), n).iterator(); var4.hasNext(); r = Option.some(treeZipper(t, (Stream)lr._1(), (Stream)lr._2(), this.downParents()))) {
         lr = (P2)var4.next();
      }

      return r;
   }

   public Option<TreeZipper<A>> delete() {
      Option<TreeZipper<A>> r = Option.none();
      if (this.rights.isNotEmpty()) {
         r = Option.some(treeZipper((Tree)this.rights.head(), this.lefts, (Stream)this.rights.tail()._1(), this.parents));
      } else {
         TreeZipper loc;
         if (this.lefts.isNotEmpty()) {
            r = Option.some(treeZipper((Tree)this.lefts.head(), (Stream)this.lefts.tail()._1(), this.rights, this.parents));
         } else {
            for(Iterator var2 = this.parent().iterator(); var2.hasNext(); r = Option.some(loc.modifyTree(new F<Tree<A>, Tree<A>>() {
               public Tree<A> f(Tree<A> t) {
                  return Tree.node(t.root(), Stream.nil());
               }
            }))) {
               loc = (TreeZipper)var2.next();
            }
         }
      }

      return r;
   }

   public TreeZipper<P2<A, Boolean>> zipWithFocus() {
      F<A, P2<A, Boolean>> f = (F)Function.flip(P.p2()).f(false);
      return this.map(f).modifyLabel(P2.map2_(Booleans.not));
   }

   public <B> TreeZipper<B> map(final F<A, B> f) {
      F<Tree<A>, Tree<B>> g = (F)Tree.fmap_().f(f);
      final F<Stream<Tree<A>>, Stream<Tree<B>>> h = (F)Stream.map_().f(g);
      return treeZipper(this.tree.fmap(f), this.lefts.map(g), this.rights.map(g), this.parents.map(new F<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>, P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>() {
         public P3<Stream<Tree<B>>, B, Stream<Tree<B>>> f(P3<Stream<Tree<A>>, A, Stream<Tree<A>>> p) {
            return p.map1(h).map2(f).map3(h);
         }
      }));
   }

   public static <A> F<Tree<A>, TreeZipper<A>> fromTree() {
      return new F<Tree<A>, TreeZipper<A>>() {
         public TreeZipper<A> f(Tree<A> t) {
            return TreeZipper.fromTree(t);
         }
      };
   }

   public static <A> F<TreeZipper<A>, Option<TreeZipper<A>>> left_() {
      return new F<TreeZipper<A>, Option<TreeZipper<A>>>() {
         public Option<TreeZipper<A>> f(TreeZipper<A> z) {
            return z.left();
         }
      };
   }

   public static <A> F<TreeZipper<A>, Option<TreeZipper<A>>> right_() {
      return new F<TreeZipper<A>, Option<TreeZipper<A>>>() {
         public Option<TreeZipper<A>> f(TreeZipper<A> z) {
            return z.right();
         }
      };
   }

   public TreeZipper<TreeZipper<A>> positions() {
      Tree<TreeZipper<A>> t = (Tree)Tree.unfoldTree(dwn()).f(this);
      Stream<Tree<TreeZipper<A>>> l = this.uf(left_());
      Stream<Tree<TreeZipper<A>>> r = this.uf(right_());
      Stream<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>> p = Stream.unfold(new F<Option<TreeZipper<A>>, Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>>>() {
         public Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
            Option<P2<P3<Stream<Tree<TreeZipper<A>>>, TreeZipper<A>, Stream<Tree<TreeZipper<A>>>>, Option<TreeZipper<A>>>> r = Option.none();

            TreeZipper z;
            for(Iterator var3 = o.iterator(); var3.hasNext(); r = Option.some(P.p(P.p(z.uf(TreeZipper.left_()), z, z.uf(TreeZipper.right_())), z.parent()))) {
               z = (TreeZipper)var3.next();
            }

            return r;
         }
      }, this.parent());
      return treeZipper(t, l, r, p);
   }

   private Stream<Tree<TreeZipper<A>>> uf(final F<TreeZipper<A>, Option<TreeZipper<A>>> f) {
      return Stream.unfold(new F<Option<TreeZipper<A>>, Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>>>() {
         public Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
            Option<P2<Tree<TreeZipper<A>>, Option<TreeZipper<A>>>> r = Option.none();

            TreeZipper c;
            for(Iterator var3 = o.iterator(); var3.hasNext(); r = Option.some(P.p(Tree.unfoldTree(TreeZipper.dwn()).f(c), f.f(c)))) {
               c = (TreeZipper)var3.next();
            }

            return r;
         }
      }, f.f(this));
   }

   private static <A> F<TreeZipper<A>, P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>>> dwn() {
      return new F<TreeZipper<A>, P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>>>() {
         public P2<TreeZipper<A>, P1<Stream<TreeZipper<A>>>> f(final TreeZipper<A> tz) {
            return P.p(tz, new P1<Stream<TreeZipper<A>>>() {
               private F<Option<TreeZipper<A>>, Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>>> fwd() {
                  return new F<Option<TreeZipper<A>>, Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>>>() {
                     public Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>> f(Option<TreeZipper<A>> o) {
                        Option<P2<TreeZipper<A>, Option<TreeZipper<A>>>> r = Option.none();

                        TreeZipper c;
                        for(Iterator var3 = o.iterator(); var3.hasNext(); r = Option.some(P.p(c, c.right()))) {
                           c = (TreeZipper)var3.next();
                        }

                        return r;
                     }
                  };
               }

               public Stream<TreeZipper<A>> _1() {
                  return Stream.unfold(this.fwd(), tz.firstChild());
               }
            });
         }
      };
   }

   public <B> TreeZipper<B> cobind(F<TreeZipper<A>, B> f) {
      return this.positions().map(f);
   }

   public static <A> F2<F<Tree<A>, Boolean>, TreeZipper<A>, Option<TreeZipper<A>>> findChild() {
      return new F2<F<Tree<A>, Boolean>, TreeZipper<A>, Option<TreeZipper<A>>>() {
         public Option<TreeZipper<A>> f(F<Tree<A>, Boolean> f, TreeZipper<A> az) {
            return az.findChild(f);
         }
      };
   }

   public <B, C> TreeZipper<C> zipWith(TreeZipper<B> bs, F2<A, B, C> f) {
      return (TreeZipper)F2Functions.zipTreeZipperM(f).f(this, bs);
   }

   public <B, C> TreeZipper<C> zipWith(TreeZipper<B> bs, F<A, F<B, C>> f) {
      return this.zipWith(bs, Function.uncurryF2(f));
   }
}
