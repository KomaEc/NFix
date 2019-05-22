package fj.data;

import fj.function.Try1;
import java.io.BufferedReader;

// $FF: synthetic class
final class IOFunctions$$Lambda$21 implements Try1 {
   private static final IOFunctions$$Lambda$21 instance = new IOFunctions$$Lambda$21();

   private IOFunctions$$Lambda$21() {
   }

   public Object f(Object var1) {
      return IOFunctions.access$lambda$20((BufferedReader)var1);
   }

   public static Try1 lambdaFactory$() {
      return instance;
   }
}
