package fj.data;

import fj.P1;

public final class $<A, B> extends P1<B> {
   private final B b;

   private $(B b) {
      this.b = b;
   }

   /** @deprecated */
   @Deprecated
   public static <A, B> $<A, B> _(B b) {
      return constant(b);
   }

   public static <A, B> $<A, B> __(B b) {
      return constant(b);
   }

   public static <A, B> $<A, B> constant(B b) {
      return new $(b);
   }

   public B _1() {
      return this.b;
   }
}
