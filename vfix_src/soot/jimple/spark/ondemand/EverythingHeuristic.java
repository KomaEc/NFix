package soot.jimple.spark.ondemand;

import soot.jimple.spark.pag.SparkField;

public class EverythingHeuristic implements FieldCheckHeuristic {
   public boolean runNewPass() {
      return false;
   }

   public boolean validateMatchesForField(SparkField field) {
      return true;
   }

   public boolean validFromBothEnds(SparkField field) {
      return false;
   }
}
