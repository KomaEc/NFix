package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.Monoid;
import fj.P2;

public abstract class FingerTree<V, A> {
   private final Measured<V, A> m;

   public abstract <B> B foldRight(F<A, F<B, B>> var1, B var2);

   public <B> B foldRight(F2<A, B, B> f, B z) {
      return this.foldRight(F2Functions.curry(f), z);
   }

   public abstract A reduceRight(F<A, F<A, A>> var1);

   public abstract <B> B foldLeft(F<B, F<A, B>> var1, B var2);

   public <B> B foldLeft(F2<B, A, B> f, B z) {
      return this.foldLeft(F2Functions.curry(f), z);
   }

   public abstract A reduceLeft(F<A, F<A, A>> var1);

   public abstract <B> FingerTree<V, B> map(F<A, B> var1, Measured<V, B> var2);

   public <B> FingerTree<V, A> filter(F<A, Boolean> f) {
      FingerTree<V, A> tree = new Empty(this.m);
      return (FingerTree)this.foldLeft((F2)FingerTree$$Lambda$1.lambdaFactory$(f), tree);
   }

   public abstract V measure();

   public final boolean isEmpty() {
      return this instanceof Empty;
   }

   Measured<V, A> measured() {
      return this.m;
   }

   public abstract <B> B match(F<Empty<V, A>, B> var1, F<Single<V, A>, B> var2, F<Deep<V, A>, B> var3);

   FingerTree(Measured<V, A> m) {
      this.m = m;
   }

   public static <V, A> Measured<V, A> measured(Monoid<V> monoid, F<A, V> measure) {
      return Measured.measured(monoid, measure);
   }

   public static <V, A> MakeTree<V, A> mkTree(Measured<V, A> m) {
      return new MakeTree(m);
   }

   public abstract FingerTree<V, A> cons(A var1);

   public abstract FingerTree<V, A> snoc(A var1);

   public abstract FingerTree<V, A> append(FingerTree<V, A> var1);

   public abstract P2<Integer, A> lookup(F<V, Integer> var1, int var2);

   // $FF: synthetic method
   private static FingerTree lambda$filter$156(F var0, FingerTree acc, Object a) {
      return (Boolean)var0.f(a) ? acc.snoc(a) : acc;
   }

   // $FF: synthetic method
   static FingerTree access$lambda$0(F var0, FingerTree var1, Object var2) {
      return lambda$filter$156(var0, var1, var2);
   }
}
