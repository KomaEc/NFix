package soot.jimple.spark.pag;

import soot.G;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.Type;

public class ArrayElement implements SparkField {
   private int number = 0;

   public ArrayElement(Singletons.Global g) {
   }

   public static ArrayElement v() {
      return G.v().soot_jimple_spark_pag_ArrayElement();
   }

   public ArrayElement() {
      Scene.v().getFieldNumberer().add(this);
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }

   public Type getType() {
      return RefType.v("java.lang.Object");
   }
}
