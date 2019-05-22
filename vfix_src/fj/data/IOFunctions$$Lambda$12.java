package fj.data;

// $FF: synthetic class
final class IOFunctions$$Lambda$12 implements SafeIO {
   private final IO arg$1;

   private IOFunctions$$Lambda$12(IO var1) {
      this.arg$1 = var1;
   }

   private static SafeIO get$Lambda(IO var0) {
      return new IOFunctions$$Lambda$12(var0);
   }

   public Object run() {
      return IOFunctions.access$lambda$11(this.arg$1);
   }

   public static SafeIO lambdaFactory$(IO var0) {
      return new IOFunctions$$Lambda$12(var0);
   }
}
