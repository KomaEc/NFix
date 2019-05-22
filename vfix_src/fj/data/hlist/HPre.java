package fj.data.hlist;

import fj.F;
import fj.Show;

public final class HPre {
   private static final HPre.HTrue hTrue = new HPre.HTrue();
   private static final HPre.HFalse hFalse = new HPre.HFalse();

   private HPre() {
      throw new UnsupportedOperationException();
   }

   public static HPre.HTrue hTrue() {
      return hTrue;
   }

   public static HPre.HFalse hFalse() {
      return hFalse;
   }

   public static final class HAdd<A extends HPre.HNat<A>, B extends HPre.HNat<B>, C extends HPre.HNat<C>> {
      private final C sum;

      private HAdd(C sum) {
         this.sum = sum;
      }

      public C sum() {
         return this.sum;
      }

      public static <N extends HPre.HNat<N>> HPre.HAdd<HPre.HZero, HPre.HSucc<N>, HPre.HSucc<N>> add(HPre.HZero a, HPre.HSucc<N> b) {
         return new HPre.HAdd(b);
      }

      public static <N extends HPre.HNat<N>> HPre.HAdd<HPre.HSucc<N>, HPre.HZero, HPre.HSucc<N>> add(HPre.HSucc<N> a, HPre.HZero b) {
         return new HPre.HAdd(a);
      }

      public static <N extends HPre.HNat<N>, M extends HPre.HNat<M>, R extends HPre.HNat<R>, H extends HPre.HAdd<N, HPre.HSucc<M>, R>> HPre.HAdd<HPre.HSucc<N>, HPre.HSucc<M>, HPre.HSucc<R>> add(HPre.HSucc<N> a, HPre.HSucc<M> b, H h) {
         return new HPre.HAdd(HPre.HNat.hSucc(h.sum()));
      }
   }

   public static final class HEq<X, Y, B extends HPre.HBool> {
      private final B v;

      private HEq(B v) {
         this.v = v;
      }

      public B v() {
         return this.v;
      }

      public static HPre.HEq<HPre.HZero, HPre.HZero, HPre.HTrue> eq(HPre.HZero a, HPre.HZero b) {
         return new HPre.HEq(HPre.hTrue());
      }

      public static <N extends HPre.HNat<N>> HPre.HEq<HPre.HZero, HPre.HSucc<N>, HPre.HFalse> eq(HPre.HZero a, HPre.HSucc<N> b) {
         return new HPre.HEq(HPre.hFalse());
      }

      public static <N extends HPre.HNat<N>> HPre.HEq<HPre.HSucc<N>, HPre.HZero, HPre.HFalse> eq(HPre.HSucc<N> a, HPre.HZero b) {
         return new HPre.HEq(HPre.hFalse());
      }

      public static <N extends HPre.HNat<N>, NN extends HPre.HNat<NN>, B extends HPre.HBool, E extends HPre.HEq<N, NN, B>> HPre.HEq<HPre.HSucc<N>, HPre.HSucc<NN>, B> eq(HPre.HSucc<N> a, HPre.HSucc<NN> b, E e) {
         return new HPre.HEq(e.v());
      }
   }

   public static final class HSucc<N extends HPre.HNat<N>> extends HPre.HNat<HPre.HSucc<N>> {
      private final N pred;

      private HSucc(N n) {
         this.pred = n;
      }

      public Show<HPre.HSucc<N>> show() {
         return Show.showS(new F<HPre.HSucc<N>, String>() {
            public String f(HPre.HSucc<N> s) {
               return "HSucc (" + s.show().showS((Object)s) + ')';
            }
         });
      }

      public Integer toInteger() {
         return 1 + this.pred.toInteger();
      }

      // $FF: synthetic method
      HSucc(HPre.HNat x0, Object x1) {
         this(x0);
      }
   }

   public static final class HZero extends HPre.HNat<HPre.HZero> {
      private HZero() {
      }

      public Show<HPre.HZero> show() {
         return Show.showS(new F<HPre.HZero, String>() {
            public String f(HPre.HZero hZero) {
               return "HZero";
            }
         });
      }

      public Integer toInteger() {
         return 0;
      }

      // $FF: synthetic method
      HZero(Object x0) {
         this();
      }
   }

   public abstract static class HNat<A extends HPre.HNat<A>> {
      public abstract Show<A> show();

      public abstract Integer toInteger();

      public static HPre.HZero hZero() {
         return new HPre.HZero();
      }

      public static <N extends HPre.HNat<N>> HPre.HSucc<N> hSucc(N n) {
         return new HPre.HSucc(n);
      }

      public static <N extends HPre.HNat<N>> N hPred(HPre.HSucc<N> n) {
         return n.pred;
      }
   }

   public static final class HCond<T, X, Y, Z> {
      private final Z z;

      private HCond(Z z) {
         this.z = z;
      }

      public Z v() {
         return this.z;
      }

      public static <X, Y> HPre.HCond<HPre.HFalse, X, Y, Y> hCond(HPre.HFalse t, X x, Y y) {
         return new HPre.HCond(y);
      }

      public static <X, Y> HPre.HCond<HPre.HTrue, X, Y, X> hCond(HPre.HTrue t, X x, Y y) {
         return new HPre.HCond(x);
      }
   }

   public static final class HOr<A extends HPre.HBool, B extends HPre.HBool, C extends HPre.HBool> {
      private final C v;

      private HOr(C v) {
         this.v = v;
      }

      public C v() {
         return this.v;
      }

      public static HPre.HOr<HPre.HFalse, HPre.HFalse, HPre.HFalse> hOr(HPre.HFalse a, HPre.HFalse b) {
         return new HPre.HOr(HPre.hFalse());
      }

      public static HPre.HOr<HPre.HTrue, HPre.HFalse, HPre.HTrue> hOr(HPre.HTrue a, HPre.HFalse b) {
         return new HPre.HOr(HPre.hTrue());
      }

      public static HPre.HOr<HPre.HFalse, HPre.HTrue, HPre.HTrue> hOr(HPre.HFalse a, HPre.HTrue b) {
         return new HPre.HOr(HPre.hTrue());
      }

      public static HPre.HOr<HPre.HTrue, HPre.HTrue, HPre.HTrue> hOr(HPre.HTrue a, HPre.HTrue b) {
         return new HPre.HOr(HPre.hTrue());
      }
   }

   public static final class HAnd<A extends HPre.HBool, B extends HPre.HBool, C extends HPre.HBool> {
      private final C v;

      private HAnd(C v) {
         this.v = v;
      }

      public C v() {
         return this.v;
      }

      public static HPre.HAnd<HPre.HFalse, HPre.HFalse, HPre.HFalse> hAnd(HPre.HFalse a, HPre.HFalse b) {
         return new HPre.HAnd(HPre.hFalse());
      }

      public static HPre.HAnd<HPre.HTrue, HPre.HFalse, HPre.HFalse> hAnd(HPre.HTrue a, HPre.HFalse b) {
         return new HPre.HAnd(HPre.hFalse());
      }

      public static HPre.HAnd<HPre.HFalse, HPre.HTrue, HPre.HFalse> hAnd(HPre.HFalse a, HPre.HTrue b) {
         return new HPre.HAnd(HPre.hFalse());
      }

      public static HPre.HAnd<HPre.HTrue, HPre.HTrue, HPre.HTrue> hAnd(HPre.HTrue a, HPre.HTrue b) {
         return new HPre.HAnd(HPre.hTrue());
      }
   }

   public static class HFalse extends HPre.HBool {
      private HFalse() {
         super(null);
      }

      // $FF: synthetic method
      HFalse(Object x0) {
         this();
      }
   }

   public static class HTrue extends HPre.HBool {
      private HTrue() {
         super(null);
      }

      // $FF: synthetic method
      HTrue(Object x0) {
         this();
      }
   }

   public static class HBool {
      private HBool() {
      }

      // $FF: synthetic method
      HBool(Object x0) {
         this();
      }
   }
}
