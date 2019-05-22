package soot.jimple.spark.geom.geomPA;

import java.io.PrintStream;
import java.util.Set;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.util.Numberable;

public abstract class IVarAbstraction implements Numberable {
   protected static IFigureManager stubManager = null;
   protected static IFigureManager deadManager = null;
   protected static RectangleNode pres = null;
   public Node me;
   public int id = -1;
   public int Qpos = 0;
   public boolean willUpdate = false;
   public int top_value = 1;
   public int lrf_value = 0;
   protected IVarAbstraction parent = this;

   public boolean lessThan(IVarAbstraction other) {
      if (this.lrf_value != other.lrf_value) {
         return this.lrf_value < other.lrf_value;
      } else {
         return this.top_value < other.top_value;
      }
   }

   public IVarAbstraction getRepresentative() {
      return this.parent == this ? this : (this.parent = this.parent.getRepresentative());
   }

   public IVarAbstraction merge(IVarAbstraction other) {
      this.getRepresentative();
      this.parent = other.getRepresentative();
      return this.parent;
   }

   public void setNumber(int number) {
      this.id = number;
   }

   public int getNumber() {
      return this.id;
   }

   public String toString() {
      return this.me != null ? this.me.toString() : super.toString();
   }

   public boolean reachable() {
      return this.id != -1;
   }

   public boolean hasPTResult() {
      return this.num_of_diff_objs() != -1;
   }

   public Node getWrappedNode() {
      return this.me;
   }

   public Type getType() {
      return this.me.getType();
   }

   public boolean isLocalPointer() {
      return this.me instanceof LocalVarNode;
   }

   public SootMethod enclosingMethod() {
      return this.me instanceof LocalVarNode ? ((LocalVarNode)this.me).getMethod() : null;
   }

   public abstract boolean add_points_to_3(AllocNode var1, long var2, long var4, long var6);

   public abstract boolean add_points_to_4(AllocNode var1, long var2, long var4, long var6, long var8);

   public abstract boolean add_simple_constraint_3(IVarAbstraction var1, long var2, long var4, long var6);

   public abstract boolean add_simple_constraint_4(IVarAbstraction var1, long var2, long var4, long var6, long var8);

   public abstract void put_complex_constraint(PlainConstraint var1);

   public abstract void reconstruct();

   public abstract void do_before_propagation();

   public abstract void do_after_propagation();

   public abstract void propagate(GeomPointsTo var1, IWorklist var2);

   public abstract void drop_duplicates();

   public abstract void remove_points_to(AllocNode var1);

   public abstract void deleteAll();

   public abstract void keepPointsToOnly();

   public abstract void injectPts();

   public abstract int num_of_diff_objs();

   public abstract int num_of_diff_edges();

   public abstract int count_pts_intervals(AllocNode var1);

   public abstract int count_new_pts_intervals();

   public abstract int count_flow_intervals(IVarAbstraction var1);

   public abstract boolean heap_sensitive_intersection(IVarAbstraction var1);

   public abstract boolean pointer_interval_points_to(long var1, long var3, AllocNode var5);

   public abstract boolean isDeadObject(AllocNode var1);

   public abstract Set<AllocNode> get_all_points_to_objects();

   public abstract void get_all_context_sensitive_objects(long var1, long var3, PtSensVisitor var5);

   public abstract void print_context_sensitive_points_to(PrintStream var1);
}
