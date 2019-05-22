package fj;

import fj.data.Writer;

// $FF: synthetic class
final class Equal$$Lambda$1 implements F {
   private final Equal arg$1;
   private final Equal arg$2;

   private Equal$$Lambda$1(Equal var1, Equal var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(Equal var0, Equal var1) {
      return new Equal$$Lambda$1(var0, var1);
   }

   public Object f(Object var1) {
      return Equal.access$lambda$0(this.arg$1, this.arg$2, (Writer)var1);
   }

   public static F lambdaFactory$(Equal var0, Equal var1) {
      return new Equal$$Lambda$1(var0, var1);
   }
}
