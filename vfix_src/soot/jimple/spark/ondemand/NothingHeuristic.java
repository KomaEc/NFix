package soot.jimple.spark.ondemand;

import soot.jimple.spark.pag.SparkField;

public class NothingHeuristic implements FieldCheckHeuristic {
   public boolean runNewPass() {
      return false;
   }

   public boolean validateMatchesForField(SparkField field) {
      return false;
   }

   public boolean validFromBothEnds(SparkField field) {
      return false;
   }
}
