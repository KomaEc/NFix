package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P7;
import fj.P8;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;

public final class V8<A> implements Iterable<A> {
   private final V7<A> tail;
   private final P1<A> head;

   private V8(P1<A> head, V7<A> tail) {
      this.head = head;
      this.tail = tail;
   }

   public static <A> V8<A> p(final P8<A, A, A, A, A, A, A, A> p) {
      return new V8(new P1<A>() {
         public A _1() {
            return p._1();
         }
      }, V7.p(new P7<A, A, A, A, A, A, A>() {
         public A _1() {
            return p._2();
         }

         public A _2() {
            return p._3();
         }

         public A _3() {
            return p._4();
         }

         public A _4() {
            return p._5();
         }

         public A _5() {
            return p._6();
         }

         public A _6() {
            return p._7();
         }

         public A _7() {
            return p._8();
         }
      }));
   }

   public static <A> V8<A> cons(P1<A> head, V7<A> tail) {
      return new V8(head, tail);
   }

   public A _1() {
      return this.head._1();
   }

   public A _2() {
      return this.tail._1();
   }

   public A _3() {
      return this.tail._2();
   }

   public A _4() {
      return this.tail._3();
   }

   public A _5() {
      return this.tail._4();
   }

   public A _6() {
      return this.tail._5();
   }

   public A _7() {
      return this.tail._6();
   }

   public A _8() {
      return this.tail._7();
   }

   public V7<A> tail() {
      return this.tail;
   }

   public P1<A> head() {
      return this.head;
   }

   public Iterator<A> iterator() {
      return this.toStream().iterator();
   }

   public P8<A, A, A, A, A, A, A, A> p() {
      return new P8<A, A, A, A, A, A, A, A>() {
         public A _1() {
            return V8.this._1();
         }

         public A _2() {
            return V8.this._2();
         }

         public A _3() {
            return V8.this._3();
         }

         public A _4() {
            return V8.this._4();
         }

         public A _5() {
            return V8.this._5();
         }

         public A _6() {
            return V8.this._6();
         }

         public A _7() {
            return V8.this._7();
         }

         public A _8() {
            return V8.this._8();
         }
      };
   }

   public NonEmptyList<A> toNonEmptyList() {
      return NonEmptyList.nel(this._1(), this.tail.toNonEmptyList().toList());
   }

   public Stream<A> toStream() {
      return Stream.cons(this.head._1(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return V8.this.tail.toStream();
         }
      });
   }

   public Array<A> toArray() {
      return Array.array(this._1(), this._2(), this._3(), this._4(), this._5(), this._6(), this._7(), this._8());
   }

   public <B> V8<B> map(F<A, B> f) {
      return new V8(this.head.map(f), this.tail.map(f));
   }

   public <B> V8<B> apply(V8<F<A, B>> vf) {
      return new V8(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
   }

   public <B, C> V8<C> zipWith(F<A, F<B, C>> f, V8<B> bs) {
      return bs.apply(this.map(f));
   }

   public <B> V8<P2<A, B>> zip(V8<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(__2, bs);
   }

   public V8<V2<A>> vzip(V8<A> bs) {
      F2<A, A, V2<A>> __2 = V.v2();
      return this.zipWith(Function.curry(__2), bs);
   }

   public static <A> F<V8<A>, Stream<A>> toStream_() {
      return new F<V8<A>, Stream<A>>() {
         public Stream<A> f(V8<A> v) {
            return v.toStream();
         }
      };
   }

   public static <A> F<V8<A>, P8<A, A, A, A, A, A, A, A>> p_() {
      return new F<V8<A>, P8<A, A, A, A, A, A, A, A>>() {
         public P8<A, A, A, A, A, A, A, A> f(V8<A> v) {
            return v.p();
         }
      };
   }

   public static <A> F<V8<A>, A> __1() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._1();
         }
      };
   }

   public static <A> F<V8<A>, A> __2() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._2();
         }
      };
   }

   public static <A> F<V8<A>, A> __3() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._3();
         }
      };
   }

   public static <A> F<V8<A>, A> __4() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._4();
         }
      };
   }

   public static <A> F<V8<A>, A> __5() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._5();
         }
      };
   }

   public static <A> F<V8<A>, A> __6() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._6();
         }
      };
   }

   public static <A> F<V8<A>, A> __7() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._7();
         }
      };
   }

   public static <A> F<V8<A>, A> __8() {
      return new F<V8<A>, A>() {
         public A f(V8<A> v) {
            return v._8();
         }
      };
   }
}
