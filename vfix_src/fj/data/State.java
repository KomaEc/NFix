package fj.data;

import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.P;
import fj.P2;
import fj.Unit;

public class State<S, A> {
   private F<S, P2<S, A>> run;

   private State(F<S, P2<S, A>> f) {
      this.run = f;
   }

   public P2<S, A> run(S s) {
      return (P2)this.run.f(s);
   }

   public static <S, A> State<S, A> unit(F<S, P2<S, A>> f) {
      return new State(f);
   }

   public static <S> State<S, S> units(F<S, S> f) {
      return unit(State$$Lambda$1.lambdaFactory$(f));
   }

   public static <S, A> State<S, A> constant(A a) {
      return unit(State$$Lambda$2.lambdaFactory$(a));
   }

   public <B> State<S, B> map(F<A, B> f) {
      return unit(State$$Lambda$3.lambdaFactory$(this, f));
   }

   public static <S> State<S, Unit> modify(F<S, S> f) {
      return init().flatMap(State$$Lambda$4.lambdaFactory$(f));
   }

   public <B> State<S, B> mapState(F<P2<S, A>, P2<S, B>> f) {
      return unit(State$$Lambda$5.lambdaFactory$(this, f));
   }

   public static <S, B, C> State<S, C> flatMap(State<S, B> mb, F<B, State<S, C>> f) {
      return mb.flatMap(f);
   }

   public <B> State<S, B> flatMap(F<A, State<S, B>> f) {
      return unit(State$$Lambda$6.lambdaFactory$(this, f));
   }

   public static <S> State<S, S> init() {
      return unit(State$$Lambda$7.lambdaFactory$());
   }

   public State<S, S> gets() {
      return unit(State$$Lambda$8.lambdaFactory$(this));
   }

   public static <S> State<S, Unit> put(S s) {
      return unit(State$$Lambda$9.lambdaFactory$(s));
   }

   public A eval(S s) {
      return this.run(s)._2();
   }

   public S exec(S s) {
      return this.run(s)._1();
   }

   public State<S, A> withs(F<S, S> f) {
      return unit(F1Functions.andThen(f, this.run));
   }

   public static <S, A> State<S, A> gets(F<S, A> f) {
      return init().map(State$$Lambda$10.lambdaFactory$(f));
   }

   public static <S, A> State<S, List<A>> sequence(List<State<S, A>> list) {
      return (State)list.foldLeft((F2)State$$Lambda$11.lambdaFactory$(), constant(List.nil()));
   }

   public static <S, A, B> State<S, List<B>> traverse(List<A> list, F<A, State<S, B>> f) {
      return (State)list.foldLeft((F2)State$$Lambda$12.lambdaFactory$(f), constant(List.nil()));
   }

   // $FF: synthetic method
   private static State lambda$traverse$152(F var0, State acc, Object a) {
      return acc.flatMap(State$$Lambda$13.lambdaFactory$(var0, a));
   }

   // $FF: synthetic method
   private static State lambda$null$151(F var0, Object var1, List bs) {
      return ((State)var0.f(var1)).map(State$$Lambda$14.lambdaFactory$(bs));
   }

   // $FF: synthetic method
   private static List lambda$null$150(List var0, Object b) {
      return var0.snoc(b);
   }

   // $FF: synthetic method
   private static State lambda$sequence$149(State acc, State ma) {
      return acc.flatMap(State$$Lambda$15.lambdaFactory$(ma));
   }

   // $FF: synthetic method
   private static State lambda$null$148(State var0, List xs) {
      return var0.map(State$$Lambda$16.lambdaFactory$(xs));
   }

   // $FF: synthetic method
   private static List lambda$null$147(List var0, Object x) {
      return var0.snoc(x);
   }

   // $FF: synthetic method
   private static Object lambda$gets$146(F var0, Object s) {
      return var0.f(s);
   }

   // $FF: synthetic method
   private static P2 lambda$put$145(Object var0, Object z) {
      return P.p(var0, Unit.unit());
   }

   // $FF: synthetic method
   private P2 lambda$gets$144(Object s) {
      P2<S, A> p = this.run(s);
      S s2 = p._1();
      return P.p(s2, s2);
   }

   // $FF: synthetic method
   private static P2 lambda$init$143(Object s) {
      return P.p(s, s);
   }

   // $FF: synthetic method
   private P2 lambda$flatMap$142(F var1, Object s) {
      P2<S, A> p = this.run(s);
      A a = p._2();
      S s2 = p._1();
      State<S, B> smb = (State)var1.f(a);
      return smb.run(s2);
   }

   // $FF: synthetic method
   private P2 lambda$mapState$141(F var1, Object s) {
      return (P2)var1.f(this.run(s));
   }

   // $FF: synthetic method
   private static State lambda$modify$140(F var0, Object s) {
      return unit(State$$Lambda$17.lambdaFactory$(var0, s));
   }

   // $FF: synthetic method
   private static P2 lambda$null$139(F var0, Object var1, Object s2) {
      return P.p(var0.f(var1), Unit.unit());
   }

   // $FF: synthetic method
   private P2 lambda$map$138(F var1, Object s) {
      P2<S, A> p2 = this.run(s);
      B b = var1.f(p2._2());
      return P.p(p2._1(), b);
   }

   // $FF: synthetic method
   private static P2 lambda$constant$137(Object var0, Object s) {
      return P.p(s, var0);
   }

   // $FF: synthetic method
   private static P2 lambda$units$136(F var0, Object s) {
      S s2 = var0.f(s);
      return P.p(s2, s2);
   }

   // $FF: synthetic method
   static P2 access$lambda$0(F var0, Object var1) {
      return lambda$units$136(var0, var1);
   }

   // $FF: synthetic method
   static P2 access$lambda$1(Object var0, Object var1) {
      return lambda$constant$137(var0, var1);
   }

   // $FF: synthetic method
   static P2 access$lambda$2(State var0, F var1, Object var2) {
      return var0.lambda$map$138(var1, var2);
   }

   // $FF: synthetic method
   static State access$lambda$3(F var0, Object var1) {
      return lambda$modify$140(var0, var1);
   }

   // $FF: synthetic method
   static P2 access$lambda$4(State var0, F var1, Object var2) {
      return var0.lambda$mapState$141(var1, var2);
   }

   // $FF: synthetic method
   static P2 access$lambda$5(State var0, F var1, Object var2) {
      return var0.lambda$flatMap$142(var1, var2);
   }

   // $FF: synthetic method
   static P2 access$lambda$6(Object var0) {
      return lambda$init$143(var0);
   }

   // $FF: synthetic method
   static P2 access$lambda$7(State var0, Object var1) {
      return var0.lambda$gets$144(var1);
   }

   // $FF: synthetic method
   static P2 access$lambda$8(Object var0, Object var1) {
      return lambda$put$145(var0, var1);
   }

   // $FF: synthetic method
   static Object access$lambda$9(F var0, Object var1) {
      return lambda$gets$146(var0, var1);
   }

   // $FF: synthetic method
   static State access$lambda$10(State var0, State var1) {
      return lambda$sequence$149(var0, var1);
   }

   // $FF: synthetic method
   static State access$lambda$11(F var0, State var1, Object var2) {
      return lambda$traverse$152(var0, var1, var2);
   }

   // $FF: synthetic method
   static State access$lambda$12(F var0, Object var1, List var2) {
      return lambda$null$151(var0, var1, var2);
   }

   // $FF: synthetic method
   static List access$lambda$13(List var0, Object var1) {
      return lambda$null$150(var0, var1);
   }

   // $FF: synthetic method
   static State access$lambda$14(State var0, List var1) {
      return lambda$null$148(var0, var1);
   }

   // $FF: synthetic method
   static List access$lambda$15(List var0, Object var1) {
      return lambda$null$147(var0, var1);
   }

   // $FF: synthetic method
   static P2 access$lambda$16(F var0, Object var1, Object var2) {
      return lambda$null$139(var0, var1, var2);
   }
}
