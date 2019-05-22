package soot.jimple.spark.geom.geomPA;

public class Parameters {
   public static int max_cons_budget = 40;
   public static int max_pts_budget = 80;
   public static int cg_refine_times = 1;
   public static int seedPts = 15;
   public static int qryBudgetSize;

   static {
      qryBudgetSize = max_pts_budget / 2;
   }
}
