package fj;

public final class Bottom {
   private Bottom() {
      throw new UnsupportedOperationException();
   }

   public static Error undefined() {
      return error("undefined");
   }

   public static Error error(String s) {
      throw new Error(s);
   }

   public static <A> P1<A> error_(final String s) {
      return new P1<A>() {
         public A _1() {
            throw new Error(s);
         }
      };
   }

   public static <A, B> F<A, B> errorF(final String s) {
      return new F<A, B>() {
         public B f(A a) {
            throw new Error(s);
         }
      };
   }

   public static <A> Error decons(A a, Show<A> sa) {
      return error("Deconstruction failure on type " + a.getClass() + " with value " + sa.show(a).toString());
   }

   public static <A> Error decons(java.lang.Class<A> c) {
      return error("Deconstruction failure on type " + c);
   }

   public static <T extends Throwable> F<T, String> eToString() {
      return new F<T, String>() {
         public String f(Throwable t) {
            return t.toString();
         }
      };
   }

   public static <T extends Throwable> F<T, String> eMessage() {
      return new F<T, String>() {
         public String f(Throwable t) {
            return t.getMessage();
         }
      };
   }
}
