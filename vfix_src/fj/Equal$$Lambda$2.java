package fj;

import fj.data.Writer;

// $FF: synthetic class
final class Equal$$Lambda$2 implements F {
   private final Equal arg$1;
   private final Equal arg$2;
   private final Writer arg$3;

   private Equal$$Lambda$2(Equal var1, Equal var2, Writer var3) {
      this.arg$1 = var1;
      this.arg$2 = var2;
      this.arg$3 = var3;
   }

   private static F get$Lambda(Equal var0, Equal var1, Writer var2) {
      return new Equal$$Lambda$2(var0, var1, var2);
   }

   public Object f(Object var1) {
      return Equal.access$lambda$1(this.arg$1, this.arg$2, this.arg$3, (Writer)var1);
   }

   public static F lambdaFactory$(Equal var0, Equal var1, Writer var2) {
      return new Equal$$Lambda$2(var0, var1, var2);
   }
}
