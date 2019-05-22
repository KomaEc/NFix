package fj.data;

import fj.F;
import fj.F1Functions;

public class Reader<A, B> {
   private F<A, B> function;

   public Reader(F<A, B> f) {
      this.function = f;
   }

   public F<A, B> getFunction() {
      return this.function;
   }

   public static <A, B> Reader<A, B> unit(F<A, B> f) {
      return new Reader(f);
   }

   public static <A, B> Reader<A, B> constant(B b) {
      return unit(Reader$$Lambda$1.lambdaFactory$(b));
   }

   public B f(A a) {
      return this.function.f(a);
   }

   public <C> Reader<A, C> map(F<B, C> f) {
      return unit(F1Functions.andThen(this.function, f));
   }

   public <C> Reader<A, C> andThen(F<B, C> f) {
      return this.map(f);
   }

   public <C> Reader<A, C> flatMap(F<B, Reader<A, C>> f) {
      return unit(Reader$$Lambda$2.lambdaFactory$(this, f));
   }

   public <C> Reader<A, C> bind(F<B, Reader<A, C>> f) {
      return this.flatMap(f);
   }

   // $FF: synthetic method
   private Object lambda$flatMap$155(F var1, Object a) {
      return ((Reader)var1.f(this.function.f(a))).f(a);
   }

   // $FF: synthetic method
   private static Object lambda$constant$154(Object var0, Object a) {
      return var0;
   }

   // $FF: synthetic method
   static Object access$lambda$0(Object var0, Object var1) {
      return lambda$constant$154(var0, var1);
   }

   // $FF: synthetic method
   static Object access$lambda$1(Reader var0, F var1, Object var2) {
      return var0.lambda$flatMap$155(var1, var2);
   }
}
