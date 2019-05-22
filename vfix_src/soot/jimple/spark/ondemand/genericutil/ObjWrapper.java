package soot.jimple.spark.ondemand.genericutil;

public class ObjWrapper {
   public final Object wrapped;

   public ObjWrapper(Object wrapped) {
      this.wrapped = wrapped;
   }

   public String toString() {
      return "wrapped " + this.wrapped;
   }
}
