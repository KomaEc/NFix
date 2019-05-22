package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;

public final class V3<A> implements Iterable<A> {
   private final V2<A> tail;
   private final P1<A> head;

   private V3(P1<A> head, V2<A> tail) {
      this.head = head;
      this.tail = tail;
   }

   public static <A> V3<A> p(final P3<A, A, A> p) {
      return new V3(new P1<A>() {
         public A _1() {
            return p._1();
         }
      }, V2.p(new P2<A, A>() {
         public A _1() {
            return p._2();
         }

         public A _2() {
            return p._3();
         }
      }));
   }

   public static <A> V3<A> cons(P1<A> head, V2<A> tail) {
      return new V3(head, tail);
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

   public V2<A> tail() {
      return this.tail;
   }

   public P1<A> head() {
      return this.head;
   }

   public P3<A, A, A> p() {
      return new P3<A, A, A>() {
         public A _1() {
            return V3.this._1();
         }

         public A _2() {
            return V3.this._2();
         }

         public A _3() {
            return V3.this._3();
         }
      };
   }

   public Array<A> toArray() {
      return Array.array(this._1(), this._2(), this._3());
   }

   public <B> V3<B> apply(V3<F<A, B>> vf) {
      return new V3(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
   }

   public <B, C> V3<C> zipWith(F<A, F<B, C>> f, V3<B> bs) {
      return bs.apply(this.map(f));
   }

   public <B> V3<P2<A, B>> zip(V3<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(__2, bs);
   }

   public V3<V2<A>> vzip(V3<A> bs) {
      F2<A, A, V2<A>> __2 = V.v2();
      return this.zipWith(Function.curry(__2), bs);
   }

   public Iterator<A> iterator() {
      return this.toStream().iterator();
   }

   public NonEmptyList<A> toNonEmptyList() {
      return NonEmptyList.nel(this.head()._1(), this.tail().toNonEmptyList().toList());
   }

   public Stream<A> toStream() {
      return Stream.cons(this.head()._1(), new P1<Stream<A>>() {
         public Stream<A> _1() {
            return V3.this.tail().toStream();
         }
      });
   }

   public <B> V3<B> map(F<A, B> f) {
      return new V3(this.head().map(f), this.tail().map(f));
   }

   public static <A> F<V3<A>, Stream<A>> toStream_() {
      return new F<V3<A>, Stream<A>>() {
         public Stream<A> f(V3<A> v) {
            return v.toStream();
         }
      };
   }

   public static <A> F<V3<A>, P3<A, A, A>> p_() {
      return new F<V3<A>, P3<A, A, A>>() {
         public P3<A, A, A> f(V3<A> v) {
            return v.p();
         }
      };
   }

   public static <A> F<V3<A>, A> __1() {
      return new F<V3<A>, A>() {
         public A f(V3<A> v) {
            return v._1();
         }
      };
   }

   public static <A> F<V3<A>, A> __2() {
      return new F<V3<A>, A>() {
         public A f(V3<A> v) {
            return v._2();
         }
      };
   }

   public static <A> F<V3<A>, A> __3() {
      return new F<V3<A>, A>() {
         public A f(V3<A> v) {
            return v._3();
         }
      };
   }
}
