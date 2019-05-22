package fj.test;

import fj.P2;
import fj.function.Effect1;
import java.util.Hashtable;

// $FF: synthetic class
final class Shrink$19$$Lambda$1 implements Effect1 {
   private final Hashtable arg$1;

   private Shrink$19$$Lambda$1(Hashtable var1) {
      this.arg$1 = var1;
   }

   private static Effect1 get$Lambda(Hashtable var0) {
      return new Shrink$19$$Lambda$1(var0);
   }

   public void f(Object var1) {
      null.access$lambda$0(this.arg$1, (P2)var1);
   }

   public static Effect1 lambdaFactory$(Hashtable var0) {
      return new Shrink$19$$Lambda$1(var0);
   }
}
