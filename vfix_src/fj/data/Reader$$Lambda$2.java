package fj.data;

import fj.F;

// $FF: synthetic class
final class Reader$$Lambda$2 implements F {
   private final Reader arg$1;
   private final F arg$2;

   private Reader$$Lambda$2(Reader var1, F var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static F get$Lambda(Reader var0, F var1) {
      return new Reader$$Lambda$2(var0, var1);
   }

   public Object f(Object var1) {
      return Reader.access$lambda$1(this.arg$1, this.arg$2, var1);
   }

   public static F lambdaFactory$(Reader var0, F var1) {
      return new Reader$$Lambda$2(var0, var1);
   }
}
