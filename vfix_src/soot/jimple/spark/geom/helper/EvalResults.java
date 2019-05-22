package soot.jimple.spark.geom.helper;

import soot.jimple.spark.geom.utils.Histogram;

class EvalResults {
   public int loc = 0;
   public long total_geom_ins_pts = 0L;
   public long total_spark_pts = 0L;
   public double avg_geom_ins_pts = 0.0D;
   public double avg_spark_pts = 0.0D;
   public int max_pts_geom = 0;
   public int max_pts_spark = 0;
   public Histogram pts_size_bar_geom = null;
   public Histogram pts_size_bar_spark = null;
   public int n_callsites = 0;
   public int n_user_callsites = 0;
   public int n_geom_call_edges = 0;
   public int n_geom_user_edges = 0;
   public int n_geom_solved_all = 0;
   public int n_geom_solved_app = 0;
   public Histogram total_call_edges = null;
   public long n_alias_pairs = 0L;
   public long n_hs_alias = 0L;
   public long n_hi_alias = 0L;
   public int total_casts = 0;
   public int geom_solved_casts = 0;
   public int spark_solved_casts = 0;
   public long n_geom_du_pairs = 0L;
   public long n_spark_du_pairs = 0L;
}
