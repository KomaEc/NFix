package soot.jimple.spark.geom.ptinsE;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.geom.geomPA.IWorklist;
import soot.jimple.spark.geom.geomPA.Parameters;
import soot.jimple.spark.geom.heapinsE.HeapInsIntervalManager;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.P2SetVisitor;

/** @deprecated */
@Deprecated
public class PtInsNode extends IVarAbstraction {
   public Map<PtInsNode, PtInsIntervalManager> flowto;
   public Map<AllocNode, PtInsIntervalManager> pt_objs;
   public Map<AllocNode, PtInsIntervalManager> new_pts;
   public Vector<PlainConstraint> complex_cons = null;

   public PtInsNode(Node thisVar) {
      this.me = thisVar;
   }

   public void deleteAll() {
      this.flowto = null;
      this.pt_objs = null;
      this.new_pts = null;
      this.complex_cons = null;
   }

   public void reconstruct() {
      this.flowto = new HashMap();
      this.pt_objs = new HashMap();
      this.new_pts = new HashMap();
      this.complex_cons = null;
      this.lrf_value = 0;
   }

   public void do_before_propagation() {
      this.do_pts_interval_merge();
      this.do_flow_edge_interval_merge();
      Node wrappedNode = this.getWrappedNode();
      if (wrappedNode instanceof LocalVarNode && ((LocalVarNode)wrappedNode).isThisPtr()) {
         SootMethod func = ((LocalVarNode)wrappedNode).getMethod();
         if (!func.isConstructor()) {
            SootClass defClass = func.getDeclaringClass();
            Hierarchy typeHierarchy = Scene.v().getActiveHierarchy();
            Iterator it = this.new_pts.keySet().iterator();

            while(it.hasNext()) {
               AllocNode obj = (AllocNode)it.next();
               if (obj.getType() instanceof RefType) {
                  SootClass sc = ((RefType)obj.getType()).getSootClass();
                  if (defClass != sc) {
                     try {
                        SootMethod rt_func = typeHierarchy.resolveConcreteDispatch(sc, func);
                        if (rt_func != func) {
                           it.remove();
                           this.pt_objs.put(obj, (PtInsIntervalManager)deadManager);
                        }
                     } catch (RuntimeException var9) {
                     }
                  }
               }
            }
         }
      }

   }

   public void do_after_propagation() {
      Iterator var1 = this.pt_objs.values().iterator();

      while(var1.hasNext()) {
         PtInsIntervalManager pim = (PtInsIntervalManager)var1.next();
         pim.flush();
      }

      this.new_pts = new HashMap();
   }

   public int num_of_diff_objs() {
      if (this.parent != this) {
         return this.getRepresentative().num_of_diff_objs();
      } else {
         return this.pt_objs == null ? -1 : this.pt_objs.size();
      }
   }

   public int num_of_diff_edges() {
      if (this.parent != this) {
         return this.getRepresentative().num_of_diff_objs();
      } else {
         return this.flowto == null ? -1 : this.flowto.size();
      }
   }

   public boolean add_points_to_3(AllocNode obj, long I1, long I2, long L) {
      int code = false;
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L;
      int code;
      if (I1 == 0L) {
         code = I2 == 0L ? -1 : 0;
      } else {
         code = I2 == 0L ? 1 : 2;
      }

      return this.addPointsTo(code, obj);
   }

   public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
      return false;
   }

   public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
      int code = false;
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L;
      int code;
      if (I1 == 0L) {
         code = I2 == 0L ? -1 : 0;
      } else {
         code = I2 == 0L ? 1 : 2;
      }

      return this.addFlowsTo(code, (PtInsNode)qv);
   }

   public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
      return false;
   }

   public void put_complex_constraint(PlainConstraint cons) {
      if (this.complex_cons == null) {
         this.complex_cons = new Vector();
      }

      this.complex_cons.add(cons);
   }

   public void drop_duplicates() {
      Iterator it = this.pt_objs.keySet().iterator();

      while(it.hasNext()) {
         PtInsIntervalManager im = (PtInsIntervalManager)this.pt_objs.get(it.next());
         im.removeUselessSegments();
      }

   }

   public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
      int i;
      AllocNode obj;
      SegmentNode pts;
      SegmentNode[] int_entry1;
      PtInsNode qn;
      Iterator var16;
      Entry entry;
      if (this.complex_cons != null) {
         var16 = this.new_pts.entrySet().iterator();

         label163:
         while(true) {
            label161:
            while(true) {
               if (!var16.hasNext()) {
                  break label163;
               }

               entry = (Entry)var16.next();
               obj = (AllocNode)entry.getKey();
               int_entry1 = ((PtInsIntervalManager)entry.getValue()).getFigures();
               Iterator var18 = this.complex_cons.iterator();

               while(true) {
                  PtInsNode objn;
                  PlainConstraint pcons;
                  do {
                     if (!var18.hasNext()) {
                        continue label161;
                     }

                     pcons = (PlainConstraint)var18.next();
                     objn = (PtInsNode)ptAnalyzer.findAndInsertInstanceField(obj, pcons.f);
                     if (objn == null) {
                        this.pt_objs.put(obj, (PtInsIntervalManager)deadManager);
                        entry.setValue((PtInsIntervalManager)deadManager);
                        continue label161;
                     }
                  } while(!objn.willUpdate);

                  qn = (PtInsNode)pcons.otherSide;

                  for(i = 0; i < HeapInsIntervalManager.Divisions; ++i) {
                     for(pts = int_entry1[i]; pts != null && pts.is_new; pts = pts.next) {
                        switch(pcons.type) {
                        case 2:
                           if (objn.add_simple_constraint_3(qn, pts.I2, pcons.code == 0 ? pts.I1 : 0L, pts.L)) {
                              worklist.push(objn);
                           }
                           break;
                        case 3:
                           if (qn.add_simple_constraint_3(objn, pcons.code == 0 ? pts.I1 : 0L, pts.I2, pts.L)) {
                              worklist.push(qn);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      var16 = this.flowto.entrySet().iterator();

      label132:
      while(var16.hasNext()) {
         entry = (Entry)var16.next();
         boolean added = false;
         qn = (PtInsNode)entry.getKey();
         PtInsIntervalManager pim1 = (PtInsIntervalManager)entry.getValue();
         int_entry1 = pim1.getFigures();
         boolean has_new_edges = pim1.isThereUnprocessedFigures();
         Map<AllocNode, PtInsIntervalManager> objs = has_new_edges ? this.pt_objs : this.new_pts;
         Iterator var22 = objs.entrySet().iterator();

         while(true) {
            PtInsIntervalManager pim2;
            do {
               do {
                  if (!var22.hasNext()) {
                     if (added) {
                        worklist.push(qn);
                     }

                     if (has_new_edges) {
                        pim1.flush();
                     }
                     continue label132;
                  }

                  Entry<AllocNode, PtInsIntervalManager> entry2 = (Entry)var22.next();
                  obj = (AllocNode)entry2.getKey();
                  pim2 = (PtInsIntervalManager)entry2.getValue();
               } while(pim2 == deadManager);
            } while(!ptAnalyzer.castNeverFails(obj.getType(), qn.getWrappedNode().getType()));

            SegmentNode[] int_entry2 = pim2.getFigures();

            for(i = 0; i < 3; ++i) {
               for(pts = int_entry2[i]; pts != null && (has_new_edges || pts.is_new); pts = pts.next) {
                  for(int j = 0; j < 3; ++j) {
                     for(SegmentNode pe = int_entry1[j]; pe != null && (pts.is_new || pe.is_new); pe = pe.next) {
                        if (add_new_points_to_tuple(pts, pe, obj, qn)) {
                           added = true;
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public int count_pts_intervals(AllocNode obj) {
      int ret = 0;
      SegmentNode[] int_entry = this.find_points_to(obj);

      for(int j = 0; j < 3; ++j) {
         for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
            ++ret;
         }
      }

      return ret;
   }

   public int count_flow_intervals(IVarAbstraction qv) {
      int ret = 0;
      SegmentNode[] int_entry = this.find_flowto((PtInsNode)qv);

      for(int j = 0; j < 3; ++j) {
         for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
            ++ret;
         }
      }

      return ret;
   }

   public boolean heap_sensitive_intersection(IVarAbstraction qv) {
      PtInsNode qn = (PtInsNode)qv;
      Iterator it = this.pt_objs.keySet().iterator();

      while(true) {
         SegmentNode[] qt;
         AllocNode an;
         do {
            do {
               if (!it.hasNext()) {
                  return false;
               }

               an = (AllocNode)it.next();
            } while(an instanceof StringConstantNode);

            qt = qn.find_points_to(an);
         } while(qt == null);

         SegmentNode[] pt = this.find_points_to(an);

         for(int i = 0; i < 3; ++i) {
            for(SegmentNode p = pt[i]; p != null; p = p.next) {
               for(int j = 0; j < 3; ++j) {
                  for(SegmentNode q = qt[j]; q != null; q = q.next) {
                     if (quick_intersecting_test(p, q)) {
                        return true;
                     }
                  }
               }
            }
         }
      }
   }

   public Set<AllocNode> get_all_points_to_objects() {
      return this.parent != this ? this.getRepresentative().get_all_points_to_objects() : this.pt_objs.keySet();
   }

   public void print_context_sensitive_points_to(PrintStream outPrintStream) {
      Iterator it = this.pt_objs.keySet().iterator();

      while(true) {
         AllocNode obj;
         SegmentNode[] int_entry;
         do {
            if (!it.hasNext()) {
               return;
            }

            obj = (AllocNode)it.next();
            int_entry = this.find_points_to(obj);
         } while(int_entry == null);

         for(int j = 0; j < 3; ++j) {
            for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
               outPrintStream.println("(" + obj.toString() + ", " + p.I1 + ", " + p.I2 + ", " + p.L + ")");
            }
         }
      }
   }

   public boolean pointer_interval_points_to(long l, long r, AllocNode obj) {
      SegmentNode[] int_entry = this.find_points_to(obj);
      if (int_entry[0] != null) {
         return true;
      } else {
         for(int i = 1; i < HeapInsIntervalManager.Divisions; ++i) {
            for(SegmentNode p = int_entry[i]; p != null; p = p.next) {
               long R = p.I1 + p.L;
               if (l <= p.I1 && p.I1 < r || p.I1 <= l && l < R) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void remove_points_to(AllocNode obj) {
      this.pt_objs.remove(obj);
   }

   public void keepPointsToOnly() {
      this.flowto = null;
      this.new_pts = null;
      this.complex_cons = null;
   }

   public int count_new_pts_intervals() {
      int ans = 0;
      Iterator var2 = this.new_pts.values().iterator();

      while(var2.hasNext()) {
         PtInsIntervalManager im = (PtInsIntervalManager)var2.next();
         SegmentNode[] int_entry = im.getFigures();

         for(int i = 0; i < 3; ++i) {
            for(SegmentNode p = int_entry[i]; p != null && p.is_new; p = p.next) {
               ++ans;
            }
         }
      }

      return ans;
   }

   public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
      if (this.parent != this) {
         this.getRepresentative().get_all_context_sensitive_objects(l, r, visitor);
      } else {
         GeomPointsTo geomPTA = (GeomPointsTo)Scene.v().getPointsToAnalysis();
         Iterator var7 = this.pt_objs.entrySet().iterator();

         while(var7.hasNext()) {
            Entry<AllocNode, PtInsIntervalManager> entry = (Entry)var7.next();
            AllocNode obj = (AllocNode)entry.getKey();
            PtInsIntervalManager im = (PtInsIntervalManager)entry.getValue();
            SegmentNode[] int_entry = im.getFigures();
            SootMethod sm = obj.getMethod();
            int sm_int = 0;
            long n_contexts = 1L;
            if (sm != null) {
               sm_int = geomPTA.getIDFromSootMethod(sm);
               n_contexts = geomPTA.context_size[sm_int];
            }

            for(int i = 0; i < 3; ++i) {
               for(SegmentNode p = int_entry[i]; p != null; p = p.next) {
                  long R = p.I1 + p.L;
                  long objL = -1L;
                  long objR = -1L;
                  if (i == 0) {
                     objL = p.I2;
                     objR = p.I2 + p.L;
                  } else {
                     long d;
                     if (l <= p.I1 && p.I1 < r) {
                        if (i != 1) {
                           d = r - p.I1;
                           if (d > p.L) {
                              d = p.L;
                           }

                           objL = p.I2;
                           objR = objL + d;
                        } else {
                           objL = 1L;
                           objR = 1L + n_contexts;
                        }
                     } else if (p.I1 <= l && l < R) {
                        if (i != 1) {
                           d = R - l;
                           if (R > r) {
                              d = r - l;
                           }

                           objL = p.I2 + l - p.I1;
                           objR = objL + d;
                        } else {
                           objL = 1L;
                           objR = 1L + n_contexts;
                        }
                     }
                  }

                  if (objL != -1L && objR != -1L) {
                     visitor.visit(obj, objL, objR, sm_int);
                  }
               }
            }
         }

      }
   }

   public void injectPts() {
      final GeomPointsTo geomPTA = (GeomPointsTo)Scene.v().getPointsToAnalysis();
      this.pt_objs = new HashMap();
      this.me.getP2Set().forall(new P2SetVisitor() {
         public void visit(Node n) {
            if (geomPTA.isValidGeometricNode(n)) {
               PtInsNode.this.pt_objs.put((AllocNode)n, (PtInsIntervalManager)PtInsNode.stubManager);
            }

         }
      });
      this.new_pts = null;
   }

   public boolean isDeadObject(AllocNode obj) {
      return this.pt_objs.get(obj) == deadManager;
   }

   private SegmentNode[] find_flowto(PtInsNode qv) {
      PtInsIntervalManager im = (PtInsIntervalManager)this.flowto.get(qv);
      return im == null ? null : im.getFigures();
   }

   private SegmentNode[] find_points_to(AllocNode obj) {
      PtInsIntervalManager im = (PtInsIntervalManager)this.pt_objs.get(obj);
      return im == null ? null : im.getFigures();
   }

   private void do_pts_interval_merge() {
      Iterator var1 = this.pt_objs.values().iterator();

      while(var1.hasNext()) {
         PtInsIntervalManager im = (PtInsIntervalManager)var1.next();
         im.mergeFigures(Parameters.max_pts_budget);
      }

   }

   private void do_flow_edge_interval_merge() {
      Iterator var1 = this.flowto.values().iterator();

      while(var1.hasNext()) {
         PtInsIntervalManager im = (PtInsIntervalManager)var1.next();
         im.mergeFigures(Parameters.max_cons_budget);
      }

   }

   private boolean addPointsTo(int code, AllocNode obj) {
      PtInsIntervalManager im = (PtInsIntervalManager)this.pt_objs.get(obj);
      if (im == null) {
         im = new PtInsIntervalManager();
         this.pt_objs.put(obj, im);
      } else if (im == deadManager) {
         return false;
      }

      if (im.addNewFigure(code, pres) != null) {
         this.new_pts.put(obj, im);
         return true;
      } else {
         return false;
      }
   }

   private boolean addFlowsTo(int code, PtInsNode qv) {
      PtInsIntervalManager im = (PtInsIntervalManager)this.flowto.get(qv);
      if (im == null) {
         im = new PtInsIntervalManager();
         this.flowto.put(qv, im);
      }

      return im.addNewFigure(code, pres) != null;
   }

   private static boolean add_new_points_to_tuple(SegmentNode pts, SegmentNode pe, AllocNode obj, PtInsNode qn) {
      int code = false;
      int code;
      if (pts.I1 != 0L && pe.I1 != 0L) {
         long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
         long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
         if (interI >= interJ) {
            return false;
         }

         pres.I1 = pe.I2 == 0L ? 0L : interI - pe.I1 + pe.I2;
         pres.I2 = pts.I2 == 0L ? 0L : interI - pts.I1 + pts.I2;
         pres.L = interJ - interI;
         code = pres.I2 == 0L ? 1 : 2;
      } else {
         pres.I1 = 0L;
         pres.I2 = pts.I2;
         pres.L = pts.L;
         code = pts.I2 == 0L ? -1 : 0;
      }

      return qn.addPointsTo(code, obj);
   }

   private static boolean quick_intersecting_test(SegmentNode p, SegmentNode q) {
      if (p.I2 >= q.I2) {
         return p.I2 < q.I2 + q.L;
      } else {
         return q.I2 < p.I2 + p.L;
      }
   }

   static {
      stubManager = new PtInsIntervalManager();
      pres = new RectangleNode(0L, 0L, 9223372036854775806L, 9223372036854775806L);
      stubManager.addNewFigure(-1, pres);
      deadManager = new PtInsIntervalManager();
   }
}
