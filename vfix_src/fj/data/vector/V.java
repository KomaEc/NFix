package fj.data.vector;

import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.P;
import fj.P1;
import fj.P2;

public final class V {
   private V() {
   }

   public static <A> V2<A> v(A a1, A a2) {
      return V2.p(P.p(a1, a2));
   }

   public static <A> V2<A> v(final P1<A> a1, final P1<A> a2) {
      return V2.p(new P2<A, A>() {
         public A _1() {
            return a1._1();
         }

         public A _2() {
            return a2._1();
         }
      });
   }

   public static <A> F2<A, A, V2<A>> v2() {
      return new F2<A, A, V2<A>>() {
         public V2<A> f(A a, A a1) {
            return V.v(a, a1);
         }
      };
   }

   public static <A> V3<A> v(A a1, A a2, A a3) {
      return V3.p(P.p(a1, a2, a3));
   }

   public static <A> V3<A> v(P1<A> a1, P1<A> a2, P1<A> a3) {
      return V3.cons(a1, v(a2, a3));
   }

   public static <A> F3<A, A, A, V3<A>> v3() {
      return new F3<A, A, A, V3<A>>() {
         public V3<A> f(A a, A a1, A a2) {
            return V.v(a, a1, a2);
         }
      };
   }

   public static <A> V4<A> v(A a1, A a2, A a3, A a4) {
      return V4.p(P.p(a1, a2, a3, a4));
   }

   public static <A> V4<A> v(P1<A> a1, P1<A> a2, P1<A> a3, P1<A> a4) {
      return V4.cons(a1, v(a2, a3, a4));
   }

   public static <A> F4<A, A, A, A, V4<A>> v4() {
      return new F4<A, A, A, A, V4<A>>() {
         public V4<A> f(A a, A a1, A a2, A a3) {
            return V.v(a, a1, a2, a3);
         }
      };
   }

   public static <A> V5<A> v(A a1, A a2, A a3, A a4, A a5) {
      return V5.p(P.p(a1, a2, a3, a4, a5));
   }

   public static <A> V5<A> v(P1<A> a1, P1<A> a2, P1<A> a3, P1<A> a4, P1<A> a5) {
      return V5.cons(a1, v(a2, a3, a4, a5));
   }

   public static <A> F5<A, A, A, A, A, V5<A>> v5() {
      return new F5<A, A, A, A, A, V5<A>>() {
         public V5<A> f(A a, A a1, A a2, A a3, A a4) {
            return V.v(a, a1, a2, a3, a4);
         }
      };
   }
}
