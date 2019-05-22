package fj.data;

import fj.function.Effect1;

// $FF: synthetic class
final class Conversions$20$$Lambda$1 implements Effect1 {
   private final StringBuffer arg$1;

   private Conversions$20$$Lambda$1(StringBuffer var1) {
      this.arg$1 = var1;
   }

   private static Effect1 get$Lambda(StringBuffer var0) {
      return new Conversions$20$$Lambda$1(var0);
   }

   public void f(Object var1) {
      null.access$lambda$0(this.arg$1, (Character)var1);
   }

   public static Effect1 lambdaFactory$(StringBuffer var0) {
      return new Conversions$20$$Lambda$1(var0);
   }
}
