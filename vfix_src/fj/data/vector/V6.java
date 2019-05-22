package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P5;
import fj.P6;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;

public final class V6<A> implements Iterable<A> {
   private final V5<A> tail;
   private final P1<A> head;

   private V6(P1<A> head, V5<A> tail) {
      this.head = head;
      this.tail = tail;
   }

   public static <A> V6<A> p(final P6<A, A, A, A, A, A> p) {
      return new V6(new P1<A>() {
         public A _1() {
            return p._1();
         }
      }, V5.p(new P5<A, A, A, A, A>() {
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
      }));
   }

   public static <A> V6<A> cons(P1<A> head, V5<A> tail) {
      return new V6(head, tail);
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

   public V5<A> tail() {
      return this.tail;
   }

   public P1<A> head() {
      return this.head;
   }

   public Iterator<A> iterator() {
      return this.toStream().iterator();
   }

   public P6<A, A, A, A, A, A> p() {
      return new P6<A, A, A, A, A, A>() {
         public A _1() {
            return V6.this._1();
         }

         public A _2() {
            return V6.this._2();
         }

         public A _3() {
            return V6.this._3();
         }

         public A _4() {
            return V6.this._4();
         }

         public A _5() {
            return V6.this._5();
         }

         public A _6() {
            return V6.this._6();
         }
      };
   }

   public NonEmptyList<A> toNonEmptyList() {
      return NonEmptyList.nel(this._1(), this.tail.toNonEmptyList().toList());
   }

   public Stream<A> toStream() {
      return Stream.cons(this.head._1(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return V6.this.tail.toStream();
         }
      });
   }

   public Array<A> toArray() {
      return Array.array(this._1(), this._2(), this._3(), this._4(), this._5(), this._6());
   }

   public <B> V6<B> map(F<A, B> f) {
      return new V6(this.head.map(f), this.tail.map(f));
   }

   public <B> V6<B> apply(V6<F<A, B>> vf) {
      return new V6(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
   }

   public <B, C> V6<C> zipWith(F<A, F<B, C>> f, V6<B> bs) {
      return bs.apply(this.map(f));
   }

   public <B> V6<P2<A, B>> zip(V6<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(__2, bs);
   }

   public V6<V2<A>> vzip(V6<A> bs) {
      F2<A, A, V2<A>> __2 = V.v2();
      return this.zipWith(Function.curry(__2), bs);
   }

   public static <A> F<V6<A>, Stream<A>> toStream_() {
      return new F<V6<A>, Stream<A>>() {
         public Stream<A> f(V6<A> v) {
            return v.toStream();
         }
      };
   }

   public static <A> F<V6<A>, P6<A, A, A, A, A, A>> p_() {
      return new F<V6<A>, P6<A, A, A, A, A, A>>() {
         public P6<A, A, A, A, A, A> f(V6<A> v) {
            return v.p();
         }
      };
   }

   public static <A> F<V6<A>, A> __1() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._1();
         }
      };
   }

   public static <A> F<V6<A>, A> __2() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._2();
         }
      };
   }

   public static <A> F<V6<A>, A> __3() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._3();
         }
      };
   }

   public static <A> F<V6<A>, A> __4() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._4();
         }
      };
   }

   public static <A> F<V6<A>, A> __5() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._5();
         }
      };
   }

   public static <A> F<V6<A>, A> __6() {
      return new F<V6<A>, A>() {
         public A f(V6<A> v) {
            return v._6();
         }
      };
   }
}
