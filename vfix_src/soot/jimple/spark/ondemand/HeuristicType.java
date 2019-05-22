package soot.jimple.spark.ondemand;

import soot.jimple.spark.internal.TypeManager;

public enum HeuristicType {
   MANUAL,
   INCR,
   EVERY,
   MANUALINCR,
   NOTHING;

   public static FieldCheckHeuristic getHeuristic(HeuristicType type, TypeManager tm, int maxPasses) {
      FieldCheckHeuristic ret = null;
      switch(type) {
      case MANUAL:
         ret = new ManualFieldCheckHeuristic();
         break;
      case INCR:
         ret = new InnerTypesIncrementalHeuristic(tm, maxPasses);
         break;
      case EVERY:
         ret = new EverythingHeuristic();
         break;
      case MANUALINCR:
         ret = new ManualAndInnerHeuristic(tm, maxPasses);
         break;
      case NOTHING:
         ret = new NothingHeuristic();
      }

      return (FieldCheckHeuristic)ret;
   }
}
