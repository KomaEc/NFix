package soot.jimple.spark.ondemand.genericutil;

public abstract class Predicate<T> {
   public static final Predicate FALSE = new Predicate() {
      public boolean test(Object obj_) {
         return false;
      }
   };
   public static final Predicate TRUE;

   public static <T> Predicate<T> truePred() {
      return TRUE;
   }

   public static <T> Predicate<T> falsePred() {
      return FALSE;
   }

   public abstract boolean test(T var1);

   public Predicate<T> not() {
      return new Predicate<T>() {
         public boolean test(T obj_) {
            return !Predicate.this.test(obj_);
         }
      };
   }

   public Predicate<T> and(final Predicate<T> conjunct_) {
      return new Predicate<T>() {
         public boolean test(T obj_) {
            return Predicate.this.test(obj_) && conjunct_.test(obj_);
         }
      };
   }

   public Predicate<T> or(final Predicate<T> disjunct_) {
      return new Predicate<T>() {
         public boolean test(T obj_) {
            return Predicate.this.test(obj_) || disjunct_.test(obj_);
         }
      };
   }

   static {
      TRUE = FALSE.not();
   }
}
