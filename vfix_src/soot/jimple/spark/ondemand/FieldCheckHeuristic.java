package soot.jimple.spark.ondemand;

import soot.jimple.spark.pag.SparkField;

public interface FieldCheckHeuristic {
   boolean runNewPass();

   boolean validateMatchesForField(SparkField var1);

   boolean validFromBothEnds(SparkField var1);
}
