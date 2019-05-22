package fj.data;

// $FF: synthetic class
final class IOFunctions$$Lambda$19 implements IO {
   private final String arg$1;

   private IOFunctions$$Lambda$19(String var1) {
      this.arg$1 = var1;
   }

   private static IO get$Lambda(String var0) {
      return new IOFunctions$$Lambda$19(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$18(this.arg$1);
   }

   public static IO lambdaFactory$(String var0) {
      return new IOFunctions$$Lambda$19(var0);
   }
}
