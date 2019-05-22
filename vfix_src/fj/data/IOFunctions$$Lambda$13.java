package fj.data;

// $FF: synthetic class
final class IOFunctions$$Lambda$13 implements IO {
   private final IO arg$1;
   private final IO arg$2;

   private IOFunctions$$Lambda$13(IO var1, IO var2) {
      this.arg$1 = var1;
      this.arg$2 = var2;
   }

   private static IO get$Lambda(IO var0, IO var1) {
      return new IOFunctions$$Lambda$13(var0, var1);
   }

   public Object run() {
      return IOFunctions.access$lambda$12(this.arg$1, this.arg$2);
   }

   public static IO lambdaFactory$(IO var0, IO var1) {
      return new IOFunctions$$Lambda$13(var0, var1);
   }
}
