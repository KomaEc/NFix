package fj;

import fj.data.Array;
import fj.data.Either;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.Validation;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.data.vector.V5;
import fj.data.vector.V6;
import fj.data.vector.V7;
import fj.data.vector.V8;

public final class Hash<A> {
   private final F<A, Integer> f;
   public static final Hash<Boolean> booleanHash = anyHash();
   public static final Hash<Byte> byteHash = anyHash();
   public static final Hash<Character> charHash = anyHash();
   public static final Hash<Double> doubleHash = anyHash();
   public static final Hash<Float> floatHash = anyHash();
   public static final Hash<Integer> intHash = anyHash();
   public static final Hash<Long> longHash = anyHash();
   public static final Hash<Short> shortHash = anyHash();
   public static final Hash<String> stringHash = anyHash();
   public static final Hash<StringBuffer> stringBufferHash = new Hash(new F<StringBuffer, Integer>() {
      public Integer f(StringBuffer sb) {
         int p = true;
         int r = 239;

         for(int i = 0; i < sb.length(); ++i) {
            r = 419 * r + sb.charAt(i);
         }

         return r;
      }
   });
   public static final Hash<StringBuilder> stringBuilderHash = new Hash(new F<StringBuilder, Integer>() {
      public Integer f(StringBuilder sb) {
         int p = true;
         int r = 239;

         for(int i = 0; i < sb.length(); ++i) {
            r = 419 * r + sb.charAt(i);
         }

         return r;
      }
   });

   private Hash(F<A, Integer> f) {
      this.f = f;
   }

   public int hash(A a) {
      return (Integer)this.f.f(a);
   }

   public <B> Hash<B> comap(F<B, A> g) {
      return new Hash(Function.compose(this.f, g));
   }

   public static <A> Hash<A> hash(F<A, Integer> f) {
      return new Hash(f);
   }

   public static <A> Hash<A> anyHash() {
      return new Hash(new F<A, Integer>() {
         public Integer f(A a) {
            return a.hashCode();
         }
      });
   }

   public static <A, B> Hash<Either<A, B>> eitherHash(final Hash<A> ha, final Hash<B> hb) {
      return new Hash(new F<Either<A, B>, Integer>() {
         public Integer f(Either<A, B> e) {
            return e.isLeft() ? ha.hash(e.left().value()) : hb.hash(e.right().value());
         }
      });
   }

   public static <A, B> Hash<Validation<A, B>> validationHash(Hash<A> ha, Hash<B> hb) {
      return eitherHash(ha, hb).comap(Validation.either());
   }

   public static <A> Hash<List<A>> listHash(final Hash<A> ha) {
      return new Hash(new F<List<A>, Integer>() {
         public Integer f(List<A> as) {
            int p = true;
            int r = 239;

            for(List aas = as; !aas.isEmpty(); aas = aas.tail()) {
               r = 419 * r + ha.hash(aas.head());
            }

            return r;
         }
      });
   }

   public static <A> Hash<NonEmptyList<A>> nonEmptyListHash(Hash<A> ha) {
      return listHash(ha).comap(NonEmptyList.toList_());
   }

   public static <A> Hash<Option<A>> optionHash(final Hash<A> ha) {
      return new Hash(new F<Option<A>, Integer>() {
         public Integer f(Option<A> o) {
            return o.isNone() ? 0 : ha.hash(o.some());
         }
      });
   }

   public static <A> Hash<Stream<A>> streamHash(final Hash<A> ha) {
      return new Hash(new F<Stream<A>, Integer>() {
         public Integer f(Stream<A> as) {
            int p = true;
            int r = 239;

            for(Stream aas = as; !aas.isEmpty(); aas = (Stream)aas.tail()._1()) {
               r = 419 * r + ha.hash(aas.head());
            }

            return r;
         }
      });
   }

   public static <A> Hash<Array<A>> arrayHash(final Hash<A> ha) {
      return new Hash(new F<Array<A>, Integer>() {
         public Integer f(Array<A> as) {
            int p = true;
            int r = 239;

            for(int i = 0; i < as.length(); ++i) {
               r = 419 * r + ha.hash(as.get(i));
            }

            return r;
         }
      });
   }

   public static <A> Hash<Tree<A>> treeHash(Hash<A> ha) {
      return streamHash(ha).comap(Tree.flatten_());
   }

   public static <A> Hash<P1<A>> p1Hash(Hash<A> ha) {
      return ha.comap(P1.__1());
   }

   public static <A, B> Hash<P2<A, B>> p2Hash(final Hash<A> ha, final Hash<B> hb) {
      return new Hash(new F<P2<A, B>, Integer>() {
         public Integer f(P2<A, B> p2) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p2._1());
            rx = 419 * rx + hb.hash(p2._2());
            return rx;
         }
      });
   }

   public static <A, B, C> Hash<P3<A, B, C>> p3Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc) {
      return new Hash(new F<P3<A, B, C>, Integer>() {
         public Integer f(P3<A, B, C> p3) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p3._1());
            rx = 419 * rx + hb.hash(p3._2());
            rx = 419 * rx + hc.hash(p3._3());
            return rx;
         }
      });
   }

   public static <A, B, C, D> Hash<P4<A, B, C, D>> p4Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd) {
      return new Hash(new F<P4<A, B, C, D>, Integer>() {
         public Integer f(P4<A, B, C, D> p4) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p4._1());
            rx = 419 * rx + hb.hash(p4._2());
            rx = 419 * rx + hc.hash(p4._3());
            rx = 419 * rx + hd.hash(p4._4());
            return rx;
         }
      });
   }

   public static <A, B, C, D, E> Hash<P5<A, B, C, D, E>> p5Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he) {
      return new Hash(new F<P5<A, B, C, D, E>, Integer>() {
         public Integer f(P5<A, B, C, D, E> p5) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p5._1());
            rx = 419 * rx + hb.hash(p5._2());
            rx = 419 * rx + hc.hash(p5._3());
            rx = 419 * rx + hd.hash(p5._4());
            rx = 419 * rx + he.hash(p5._5());
            return rx;
         }
      });
   }

   public static <A, B, C, D, E, F$> Hash<P6<A, B, C, D, E, F$>> p6Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf) {
      return new Hash(new F<P6<A, B, C, D, E, F$>, Integer>() {
         public Integer f(P6<A, B, C, D, E, F$> p6) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p6._1());
            rx = 419 * rx + hb.hash(p6._2());
            rx = 419 * rx + hc.hash(p6._3());
            rx = 419 * rx + hd.hash(p6._4());
            rx = 419 * rx + he.hash(p6._5());
            rx = 419 * rx + hf.hash(p6._6());
            return rx;
         }
      });
   }

   public static <A, B, C, D, E, F$, G> Hash<P7<A, B, C, D, E, F$, G>> p7Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf, final Hash<G> hg) {
      return new Hash(new F<P7<A, B, C, D, E, F$, G>, Integer>() {
         public Integer f(P7<A, B, C, D, E, F$, G> p7) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p7._1());
            rx = 419 * rx + hb.hash(p7._2());
            rx = 419 * rx + hc.hash(p7._3());
            rx = 419 * rx + hd.hash(p7._4());
            rx = 419 * rx + he.hash(p7._5());
            rx = 419 * rx + hf.hash(p7._6());
            rx = 419 * rx + hg.hash(p7._7());
            return rx;
         }
      });
   }

   public static <A, B, C, D, E, F$, G, H> Hash<P8<A, B, C, D, E, F$, G, H>> p8Hash(final Hash<A> ha, final Hash<B> hb, final Hash<C> hc, final Hash<D> hd, final Hash<E> he, final Hash<F$> hf, final Hash<G> hg, final Hash<H> hh) {
      return new Hash(new F<P8<A, B, C, D, E, F$, G, H>, Integer>() {
         public Integer f(P8<A, B, C, D, E, F$, G, H> p8) {
            int p = true;
            int r = 239;
            int rx = 419 * r + ha.hash(p8._1());
            rx = 419 * rx + hb.hash(p8._2());
            rx = 419 * rx + hc.hash(p8._3());
            rx = 419 * rx + hd.hash(p8._4());
            rx = 419 * rx + he.hash(p8._5());
            rx = 419 * rx + hf.hash(p8._6());
            rx = 419 * rx + hg.hash(p8._7());
            rx = 419 * rx + hh.hash(p8._8());
            return rx;
         }
      });
   }

   public static <A> Hash<V2<A>> v2Hash(Hash<A> ea) {
      return streamHash(ea).comap(V2.toStream_());
   }

   public static <A> Hash<V3<A>> v3Hash(Hash<A> ea) {
      return streamHash(ea).comap(V3.toStream_());
   }

   public static <A> Hash<V4<A>> v4Hash(Hash<A> ea) {
      return streamHash(ea).comap(V4.toStream_());
   }

   public static <A> Hash<V5<A>> v5Hash(Hash<A> ea) {
      return streamHash(ea).comap(V5.toStream_());
   }

   public static <A> Hash<V6<A>> v6Hash(Hash<A> ea) {
      return streamHash(ea).comap(V6.toStream_());
   }

   public static <A> Hash<V7<A>> v7Hash(Hash<A> ea) {
      return streamHash(ea).comap(V7.toStream_());
   }

   public static <A> Hash<V8<A>> v8Hash(Hash<A> ea) {
      return streamHash(ea).comap(V8.toStream_());
   }
}
