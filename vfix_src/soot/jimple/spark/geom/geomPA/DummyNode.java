package soot.jimple.spark.geom.geomPA;

import java.io.PrintStream;
import java.util.Set;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;

public class DummyNode extends IVarAbstraction {
   public DummyNode(Node thisVarNode) {
      this.me = thisVarNode;
   }

   public void deleteAll() {
   }

   public boolean add_points_to_3(AllocNode obj, long I1, long I2, long L) {
      return false;
   }

   public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
      return false;
   }

   public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
      return false;
   }

   public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
      return false;
   }

   public void put_complex_constraint(PlainConstraint cons) {
   }

   public void reconstruct() {
   }

   public void do_before_propagation() {
   }

   public void do_after_propagation() {
   }

   public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
   }

   public void drop_duplicates() {
   }

   public void remove_points_to(AllocNode obj) {
   }

   public int num_of_diff_objs() {
      return -1;
   }

   public int num_of_diff_edges() {
      return -1;
   }

   public int count_pts_intervals(AllocNode obj) {
      return 0;
   }

   public int count_new_pts_intervals() {
      return 0;
   }

   public int count_flow_intervals(IVarAbstraction qv) {
      return 0;
   }

   public boolean heap_sensitive_intersection(IVarAbstraction qv) {
      return false;
   }

   public boolean pointer_interval_points_to(long l, long r, AllocNode obj) {
      return false;
   }

   public Set<AllocNode> get_all_points_to_objects() {
      return null;
   }

   public void print_context_sensitive_points_to(PrintStream outPrintStream) {
   }

   public void keepPointsToOnly() {
   }

   public void injectPts() {
   }

   public boolean isDeadObject(AllocNode obj) {
      return false;
   }

   public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
   }
}
