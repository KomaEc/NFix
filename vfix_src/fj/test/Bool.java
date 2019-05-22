package fj.test;

import fj.P1;

public final class Bool {
   private final boolean b;
   private static final Bool t = new Bool(true);
   private static final Bool f = new Bool(false);

   private Bool(boolean b) {
      this.b = b;
   }

   public boolean is() {
      return this.b;
   }

   public boolean isNot() {
      return !this.b;
   }

   public Property implies(P1<Property> p) {
      return Property.implies(this.b, p);
   }

   public Property implies(final Property p) {
      return Property.implies(this.b, new P1<Property>() {
         public Property _1() {
            return p;
         }
      });
   }

   public Property implies(Bool c) {
      return this.implies(Property.prop(c.b));
   }

   public Property implies(final boolean c) {
      return Property.implies(this.b, new P1<Property>() {
         public Property _1() {
            return Property.prop(c);
         }
      });
   }

   public static Bool bool(boolean b) {
      return b ? t : f;
   }
}
