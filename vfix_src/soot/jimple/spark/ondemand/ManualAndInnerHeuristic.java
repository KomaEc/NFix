package soot.jimple.spark.ondemand;

import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.SparkField;

public class ManualAndInnerHeuristic implements FieldCheckHeuristic {
   final ManualFieldCheckHeuristic manual = new ManualFieldCheckHeuristic();
   final InnerTypesIncrementalHeuristic inner;

   public ManualAndInnerHeuristic(TypeManager tm, int maxPasses) {
      this.inner = new InnerTypesIncrementalHeuristic(tm, maxPasses);
   }

   public boolean runNewPass() {
      return this.inner.runNewPass();
   }

   public boolean validateMatchesForField(SparkField field) {
      return this.manual.validateMatchesForField(field) || this.inner.validateMatchesForField(field);
   }

   public boolean validFromBothEnds(SparkField field) {
      return this.inner.validFromBothEnds(field);
   }
}
