package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Natural;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.hlist.HList;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class Show<A> {
   private final F<A, Stream<Character>> f;
   public static final Show<Boolean> booleanShow = anyShow();
   public static final Show<Byte> byteShow = anyShow();
   public static final Show<Character> charShow = anyShow();
   public static final Show<Double> doubleShow = anyShow();
   public static final Show<Float> floatShow = anyShow();
   public static final Show<Integer> intShow = anyShow();
   public static final Show<BigInteger> bigintShow = anyShow();
   public static final Show<BigDecimal> bigdecimalShow = anyShow();
   public static final Show<Long> longShow = anyShow();
   public static final Show<Short> shortShow = anyShow();
   public static final Show<String> stringShow = anyShow();
   public static final Show<StringBuffer> stringBufferShow = anyShow();
   public static final Show<StringBuilder> stringBuilderShow = anyShow();
   public static final Show<Natural> naturalShow;
   public static final Show<LazyString> lazyStringShow;
   public static final Show<HList.HNil> HListShow;

   private Show(F<A, Stream<Character>> f) {
      this.f = f;
   }

   public <B> Show<B> comap(F<B, A> f) {
      return show(Function.compose(this.f, f));
   }

   public Stream<Character> show(A a) {
      return (Stream)this.f.f(a);
   }

   public List<Character> showl(A a) {
      return this.show(a).toList();
   }

   public String showS(A a) {
      return Stream.asString(this.show(a));
   }

   public F<A, String> showS_() {
      return new F<A, String>() {
         public String f(A a) {
            return Show.this.showS(a);
         }
      };
   }

   public F<A, Stream<Character>> show_() {
      return this.f;
   }

   public Unit println(A a) {
      this.print(a);
      System.out.println();
      return Unit.unit();
   }

   public Unit print(A a) {
      char[] buffer = new char[8192];
      int c = 0;

      for(Stream cs = this.show(a); cs.isNotEmpty(); cs = (Stream)cs.tail()._1()) {
         buffer[c] = (Character)cs.head();
         ++c;
         if (c == 8192) {
            System.out.print(buffer);
            c = 0;
         }
      }

      System.out.print(Array.copyOfRange(buffer, 0, c));
      return Unit.unit();
   }

   public void printlnE(A a) {
      System.err.println(this.showS(a));
   }

   public static <A> Show<A> show(F<A, Stream<Character>> f) {
      return new Show(f);
   }

   public static <A> Show<A> showS(final F<A, String> f) {
      return new Show(new F<A, Stream<Character>>() {
         public Stream<Character> f(A a) {
            return Stream.fromString((String)f.f(a));
         }
      });
   }

   public static <A> Show<A> anyShow() {
      return new Show(new F<A, Stream<Character>>() {
         public Stream<Character> f(A a) {
            return Stream.fromString(a == null ? "null" : a.toString());
         }
      });
   }

   public static <A> Show<Option<A>> optionShow(final Show<A> sa) {
      return new Show(new F<Option<A>, Stream<Character>>() {
         public Stream<Character> f(Option<A> o) {
            return o.isNone() ? Stream.fromString("None") : Stream.fromString("Some(").append((Stream)sa.f.f(o.some())).append(Stream.single(')'));
         }
      });
   }

   public static <A, B> Show<Either<A, B>> eitherShow(final Show<A> sa, final Show<B> sb) {
      return new Show(new F<Either<A, B>, Stream<Character>>() {
         public Stream<Character> f(Either<A, B> e) {
            return e.isLeft() ? Stream.fromString("Left(").append((Stream)sa.f.f(e.left().value())).append(Stream.single(')')) : Stream.fromString("Right(").append((Stream)sb.f.f(e.right().value())).append(Stream.single(')'));
         }
      });
   }

   public static <A, B> Show<Validation<A, B>> validationShow(final Show<A> sa, final Show<B> sb) {
      return new Show(new F<Validation<A, B>, Stream<Character>>() {
         public Stream<Character> f(Validation<A, B> v) {
            return v.isFail() ? Stream.fromString("Fail(").append((Stream)sa.f.f(v.fail())).append(Stream.single(')')) : Stream.fromString("Success(").append((Stream)sb.f.f(v.success())).append(Stream.single(')'));
         }
      });
   }

   public static <A> Show<List<A>> listShow(final Show<A> sa) {
      return new Show(new F<List<A>, Stream<Character>>() {
         public Stream<Character> f(List<A> as) {
            return Show.streamShow(sa).show((Object)as.toStream());
         }
      });
   }

   public static <A> Show<NonEmptyList<A>> nonEmptyListShow(Show<A> sa) {
      return listShow(sa).comap(NonEmptyList.toList_());
   }

   public static <A> Show<Tree<A>> treeShow(final Show<A> sa) {
      return new Show(new F<Tree<A>, Stream<Character>>() {
         public Stream<Character> f(Tree<A> a) {
            Stream<Character> b = ((Stream)sa.f.f(a.root())).append((Stream)Show.p1Show(Show.streamShow(Show.treeShow(sa))).f.f(a.subForest())).snoc((Object)')');
            return Stream.cons('(', P.p(b));
         }
      });
   }

   public static <A> Show<Stream<A>> streamShow(final Show<A> sa) {
      return new Show(new F<Stream<A>, Stream<Character>>() {
         public Stream<Character> f(Stream<A> as) {
            return Stream.join(as.map(sa.show_()).intersperse(Stream.fromString(",")).cons(Stream.fromString("<")).snoc(P.p(Stream.fromString(">"))));
         }
      });
   }

   public static <A> Show<Array<A>> arrayShow(final Show<A> sa) {
      return new Show(new F<Array<A>, Stream<Character>>() {
         public Stream<Character> f(Array<A> as) {
            Stream<Character> b = Stream.nil();

            for(int i = 0; i < as.length(); ++i) {
               b = b.append((Stream)sa.f.f(as.get(i)));
               if (i != as.length() - 1) {
                  b = b.snoc((Object)',');
               }
            }

            b = b.snoc((Object)'}');
            return Stream.cons('{', P.p(b));
         }
      });
   }

   public static <A> Show<Class<A>> classShow() {
      return new Show(new F<Class<A>, Stream<Character>>() {
         public Stream<Character> f(Class<A> c) {
            return Show.anyShow().show((Object)c.clas());
         }
      });
   }

   public static <A> Show<P1<A>> p1Show(final Show<A> sa) {
      return new Show(new F<P1<A>, Stream<Character>>() {
         public Stream<Character> f(P1<A> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)')');
         }
      });
   }

   public static <A, B> Show<P2<A, B>> p2Show(final Show<A> sa, final Show<B> sb) {
      return new Show(new F<P2<A, B>, Stream<Character>>() {
         public Stream<Character> f(P2<A, B> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C> Show<P3<A, B, C>> p3Show(final Show<A> sa, final Show<B> sb, final Show<C> sc) {
      return new Show(new F<P3<A, B, C>, Stream<Character>>() {
         public Stream<Character> f(P3<A, B, C> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C, D> Show<P4<A, B, C, D>> p4Show(final Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd) {
      return new Show(new F<P4<A, B, C, D>, Stream<Character>>() {
         public Stream<Character> f(P4<A, B, C, D> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)',').append(sd.show(p._4())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C, D, E> Show<P5<A, B, C, D, E>> p5Show(final Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se) {
      return new Show(new F<P5<A, B, C, D, E>, Stream<Character>>() {
         public Stream<Character> f(P5<A, B, C, D, E> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)',').append(sd.show(p._4())).snoc((Object)',').append(se.show(p._5())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C, D, E, F$> Show<P6<A, B, C, D, E, F$>> p6Show(final Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf) {
      return new Show(new F<P6<A, B, C, D, E, F$>, Stream<Character>>() {
         public Stream<Character> f(P6<A, B, C, D, E, F$> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)',').append(sd.show(p._4())).snoc((Object)',').append(se.show(p._5())).snoc((Object)',').append(sf.show(p._6())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C, D, E, F$, G> Show<P7<A, B, C, D, E, F$, G>> p7Show(final Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf, final Show<G> sg) {
      return new Show(new F<P7<A, B, C, D, E, F$, G>, Stream<Character>>() {
         public Stream<Character> f(P7<A, B, C, D, E, F$, G> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)',').append(sd.show(p._4())).snoc((Object)',').append(se.show(p._5())).snoc((Object)',').append(sf.show(p._6())).snoc((Object)',').append(sg.show(p._7())).snoc((Object)')');
         }
      });
   }

   public static <A, B, C, D, E, F$, G, H> Show<P8<A, B, C, D, E, F$, G, H>> p8Show(final Show<A> sa, final Show<B> sb, final Show<C> sc, final Show<D> sd, final Show<E> se, final Show<F$> sf, final Show<G> sg, final Show<H> sh) {
      return new Show(new F<P8<A, B, C, D, E, F$, G, H>, Stream<Character>>() {
         public Stream<Character> f(P8<A, B, C, D, E, F$, G, H> p) {
            return Stream.cons('(', P.p(sa.show(p._1()))).snoc((Object)',').append(sb.show(p._2())).snoc((Object)',').append(sc.show(p._3())).snoc((Object)',').append(sd.show(p._4())).snoc((Object)',').append(se.show(p._5())).snoc((Object)',').append(sf.show(p._6())).snoc((Object)',').append(sg.show(p._7())).snoc((Object)',').append(sh.show(p._8())).snoc((Object)')');
         }
      });
   }

   public static <A> Show<V2<A>> v2Show(Show<A> ea) {
      return streamShow(ea).comap(V2.toStream_());
   }

   public static <A> Show<V3<A>> v3Show(Show<A> ea) {
      return streamShow(ea).comap(V3.toStream_());
   }

   public static <A> Show<V4<A>> v4Show(Show<A> ea) {
      return streamShow(ea).comap(V4.toStream_());
   }

   public static <A> Show<V5<A>> v5Show(Show<A> ea) {
      return streamShow(ea).comap(V5.toStream_());
   }

   public static <A> Show<V6<A>> v6Show(Show<A> ea) {
      return streamShow(ea).comap(V6.toStream_());
   }

   public static <A> Show<V7<A>> v7Show(Show<A> ea) {
      return streamShow(ea).comap(V7.toStream_());
   }

   public static <A> Show<V8<A>> v8Show(Show<A> ea) {
      return streamShow(ea).comap(V8.toStream_());
   }

   public static <A> Show<Stream<A>> unlineShow(final Show<A> sa) {
      return new Show(new F<Stream<A>, Stream<Character>>() {
         public Stream<Character> f(Stream<A> as) {
            return Stream.join(as.map(sa.show_()).intersperse(Stream.fromString("\n")));
         }
      });
   }

   public static <E, L extends HList<L>> Show<HList.HCons<E, L>> HListShow(final Show<E> e, final Show<L> l) {
      return show(new F<HList.HCons<E, L>, Stream<Character>>() {
         public Stream<Character> f(HList.HCons<E, L> c) {
            return e.show(c.head()).cons('[').append(l.show((Object)c.tail())).snoc((Object)']');
         }
      });
   }

   static {
      naturalShow = bigintShow.comap(new F<Natural, BigInteger>() {
         public BigInteger f(Natural natural) {
            return natural.bigIntegerValue();
         }
      });
      lazyStringShow = show(new F<LazyString, Stream<Character>>() {
         public Stream<Character> f(LazyString string) {
            return string.toStream();
         }
      });
      HListShow = showS(Function.constant("Nil"));
   }
}
