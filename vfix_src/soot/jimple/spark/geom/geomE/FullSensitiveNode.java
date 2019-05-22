package soot.jimple.spark.geom.geomE;

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
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.P2SetVisitor;

public class FullSensitiveNode extends IVarAbstraction {
   public Map<FullSensitiveNode, GeometricManager> flowto;
   public Map<AllocNode, GeometricManager> pt_objs;
   public Map<AllocNode, GeometricManager> new_pts;
   public Vector<PlainConstraint> complex_cons;
   public static String[] symbols = new String[]{"/", "[]"};

   public FullSensitiveNode(Node thisVar) {
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

   public void keepPointsToOnly() {
      this.flowto = null;
      this.new_pts = null;
      this.complex_cons = null;
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
                           this.pt_objs.put(obj, (GeometricManager)deadManager);
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
      if (this.new_pts.size() > 0) {
         Iterator var1 = this.new_pts.values().iterator();

         while(var1.hasNext()) {
            GeometricManager gm = (GeometricManager)var1.next();
            gm.flush();
         }
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
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L;
      return this.addPointsTo(0, obj);
   }

   public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L1;
      pres.L_prime = L2;
      return this.addPointsTo(1, obj);
   }

   public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L;
      return this.addFlowsTo(0, qv);
   }

   public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
      pres.I1 = I1;
      pres.I2 = I2;
      pres.L = L1;
      pres.L_prime = L2;
      return this.addFlowsTo(1, qv);
   }

   public void put_complex_constraint(PlainConstraint cons) {
      if (this.complex_cons == null) {
         this.complex_cons = new Vector();
      }

      this.complex_cons.add(cons);
   }

   public void drop_duplicates() {
      Iterator var1 = this.pt_objs.values().iterator();

      while(var1.hasNext()) {
         GeometricManager gm = (GeometricManager)var1.next();
         gm.removeUselessSegments();
      }

   }

   public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
      if (this.pt_objs.size() != 0) {
         int i;
         AllocNode obj;
         SegmentNode pts;
         SegmentNode[] entry_pts;
         FullSensitiveNode qn;
         Iterator var16;
         Entry entry;
         Iterator var18;
         if (this.complex_cons != null) {
            var16 = this.new_pts.entrySet().iterator();

            label216:
            while(true) {
               label214:
               while(true) {
                  if (!var16.hasNext()) {
                     break label216;
                  }

                  entry = (Entry)var16.next();
                  obj = (AllocNode)entry.getKey();
                  entry_pts = ((GeometricManager)entry.getValue()).getFigures();
                  var18 = this.complex_cons.iterator();

                  while(true) {
                     FullSensitiveNode objn;
                     PlainConstraint pcons;
                     do {
                        if (!var18.hasNext()) {
                           continue label214;
                        }

                        pcons = (PlainConstraint)var18.next();
                        objn = (FullSensitiveNode)ptAnalyzer.findInstanceField(obj, pcons.f);
                        if (objn == null) {
                           this.pt_objs.put(obj, (GeometricManager)deadManager);
                           entry.setValue((GeometricManager)deadManager);
                           continue label214;
                        }
                     } while(!objn.willUpdate);

                     qn = (FullSensitiveNode)pcons.otherSide;

                     for(i = 0; i < 2; ++i) {
                        for(pts = entry_pts[i]; pts != null && pts.is_new; pts = pts.next) {
                           switch(pcons.type) {
                           case 2:
                              if (instantiateLoadConstraint(objn, qn, pts, pcons.code << 8 | i)) {
                                 worklist.push(objn);
                              }
                              break;
                           case 3:
                              if (instantiateStoreConstraint(qn, objn, pts, pcons.code << 8 | i)) {
                                 worklist.push(qn);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (this.flowto.size() != 0) {
            var16 = this.flowto.entrySet().iterator();

            while(var16.hasNext()) {
               entry = (Entry)var16.next();
               boolean added = false;
               qn = (FullSensitiveNode)entry.getKey();
               GeometricManager gm1 = (GeometricManager)entry.getValue();
               SegmentNode[] entry_pe = gm1.getFigures();
               int j;
               SegmentNode pe;
               GeometricManager gm2;
               Entry entry2;
               if (gm1.isThereUnprocessedFigures()) {
                  var18 = this.pt_objs.entrySet().iterator();

                  label143:
                  while(true) {
                     do {
                        do {
                           if (!var18.hasNext()) {
                              gm1.flush();
                              break label143;
                           }

                           entry2 = (Entry)var18.next();
                           obj = (AllocNode)entry2.getKey();
                           gm2 = (GeometricManager)entry2.getValue();
                        } while(gm2 == deadManager);
                     } while(!ptAnalyzer.castNeverFails(obj.getType(), qn.getType()));

                     entry_pts = gm2.getFigures();
                     boolean hasNewPointsTo = gm2.isThereUnprocessedFigures();

                     for(j = 0; j < 2; ++j) {
                        for(pe = entry_pe[j]; pe != null && (pe.is_new || hasNewPointsTo); pe = pe.next) {
                           for(i = 0; i < 2; ++i) {
                              for(pts = entry_pts[i]; pts != null && (pts.is_new || pe.is_new); pts = pts.next) {
                                 if (reasonAndPropagate(qn, obj, pts, pe, i << 8 | j)) {
                                    added = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               } else {
                  var18 = this.new_pts.entrySet().iterator();

                  label182:
                  while(true) {
                     do {
                        do {
                           if (!var18.hasNext()) {
                              break label182;
                           }

                           entry2 = (Entry)var18.next();
                           obj = (AllocNode)entry2.getKey();
                           gm2 = (GeometricManager)entry2.getValue();
                        } while(gm2 == deadManager);
                     } while(!ptAnalyzer.castNeverFails(obj.getType(), qn.getType()));

                     entry_pts = gm2.getFigures();

                     for(i = 0; i < 2; ++i) {
                        for(pts = entry_pts[i]; pts != null && pts.is_new; pts = pts.next) {
                           for(j = 0; j < 2; ++j) {
                              for(pe = entry_pe[j]; pe != null; pe = pe.next) {
                                 if (reasonAndPropagate(qn, obj, pts, pe, i << 8 | j)) {
                                    added = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (added) {
                  worklist.push(qn);
               }
            }

         }
      }
   }

   public boolean isDeadObject(AllocNode obj) {
      return this.pt_objs.get(obj) == deadManager;
   }

   public int count_pts_intervals(AllocNode obj) {
      int ret = 0;
      SegmentNode[] int_entry = this.find_points_to(obj);

      for(int j = 0; j < 2; ++j) {
         for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
            ++ret;
         }
      }

      return ret;
   }

   public int count_flow_intervals(IVarAbstraction qv) {
      int ret = 0;
      SegmentNode[] int_entry = this.find_flowto((FullSensitiveNode)qv);

      for(int j = 0; j < 2; ++j) {
         for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
            ++ret;
         }
      }

      return ret;
   }

   public boolean heap_sensitive_intersection(IVarAbstraction qv) {
      FullSensitiveNode qn = (FullSensitiveNode)qv;
      boolean localToSameMethod = this.enclosingMethod() == qv.enclosingMethod();
      Iterator it = this.pt_objs.keySet().iterator();

      while(true) {
         SegmentNode[] qt;
         AllocNode an;
         do {
            do {
               do {
                  if (!it.hasNext()) {
                     return false;
                  }

                  an = (AllocNode)it.next();
               } while(an instanceof ClassConstantNode);
            } while(an instanceof StringConstantNode);

            qt = qn.find_points_to(an);
         } while(qt == null);

         SegmentNode[] pt = this.find_points_to(an);

         for(int i = 0; i < 2; ++i) {
            for(SegmentNode p = pt[i]; p != null; p = p.next) {
               for(int j = 0; j < 2; ++j) {
                  for(SegmentNode q = qt[j]; q != null; q = q.next) {
                     if (localToSameMethod) {
                        if (p.intersect(q)) {
                           return true;
                        }
                     } else if (p.projYIntersect(q)) {
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

      while(it.hasNext()) {
         AllocNode obj = (AllocNode)it.next();
         SegmentNode[] int_entry = this.find_points_to(obj);

         for(int j = 0; j < 2; ++j) {
            for(SegmentNode p = int_entry[j]; p != null; p = p.next) {
               outPrintStream.print("(" + obj.toString() + ", " + p.I1 + ", " + p.I2 + ", " + p.L + ", ");
               if (p instanceof RectangleNode) {
                  outPrintStream.print(((RectangleNode)p).L_prime + ", ");
               }

               outPrintStream.println(symbols[j] + ")");
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
               FullSensitiveNode.this.pt_objs.put((AllocNode)n, (GeometricManager)FullSensitiveNode.stubManager);
            }

         }
      });
      this.new_pts = null;
   }

   public boolean pointer_interval_points_to(long l, long r, AllocNode obj) {
      SegmentNode[] int_entry = this.find_points_to(obj);

      for(int i = 0; i < 2; ++i) {
         for(SegmentNode p = int_entry[i]; p != null; p = p.next) {
            long R = p.I1 + p.L;
            if (l <= p.I1 && p.I1 < r || p.I1 <= l && l < R) {
               return true;
            }
         }
      }

      return false;
   }

   public void remove_points_to(AllocNode obj) {
      this.pt_objs.remove(obj);
   }

   public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
      if (this.parent != this) {
         this.getRepresentative().get_all_context_sensitive_objects(l, r, visitor);
      } else {
         GeomPointsTo geomPTA = (GeomPointsTo)Scene.v().getPointsToAnalysis();
         Iterator var7 = this.pt_objs.entrySet().iterator();

         while(true) {
            Entry entry;
            AllocNode obj;
            int sm_int;
            do {
               if (!var7.hasNext()) {
                  return;
               }

               entry = (Entry)var7.next();
               obj = (AllocNode)entry.getKey();
               SootMethod sm = obj.getMethod();
               sm_int = geomPTA.getIDFromSootMethod(sm);
            } while(sm_int == -1);

            GeometricManager gm = (GeometricManager)entry.getValue();
            SegmentNode[] int_entry = gm.getFigures();

            for(int i = 0; i < 2; ++i) {
               for(SegmentNode p = int_entry[i]; p != null; p = p.next) {
                  long L = p.I1;
                  long R = L + p.L;
                  long objL = -1L;
                  long objR = -1L;
                  long d;
                  if (l <= L && L < r) {
                     if (i == 0) {
                        d = r - L;
                        if (R < r) {
                           d = p.L;
                        }

                        objL = p.I2;
                        objR = objL + d;
                     } else {
                        objL = p.I2;
                        objR = p.I2 + ((RectangleNode)p).L_prime;
                     }
                  } else if (L <= l && l < R) {
                     if (i == 0) {
                        d = R - l;
                        if (R > r) {
                           d = r - l;
                        }

                        objL = p.I2 + l - L;
                        objR = objL + d;
                     } else {
                        objL = p.I2;
                        objR = p.I2 + ((RectangleNode)p).L_prime;
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

   public int count_new_pts_intervals() {
      int ans = 0;
      Iterator var2 = this.new_pts.values().iterator();

      while(var2.hasNext()) {
         GeometricManager gm = (GeometricManager)var2.next();
         SegmentNode[] int_entry = gm.getFigures();

         for(int i = 0; i < 2; ++i) {
            for(SegmentNode p = int_entry[i]; p != null && p.is_new; p = p.next) {
               ++ans;
            }
         }
      }

      return ans;
   }

   private boolean addPointsTo(int code, AllocNode obj) {
      GeometricManager gm = (GeometricManager)this.pt_objs.get(obj);
      if (gm == null) {
         gm = new GeometricManager();
         this.pt_objs.put(obj, gm);
      } else if (gm == deadManager) {
         return false;
      }

      SegmentNode p = gm.addNewFigure(code, pres);
      if (p != null) {
         this.new_pts.put(obj, gm);
         return true;
      } else {
         return false;
      }
   }

   private boolean addFlowsTo(int code, IVarAbstraction qv) {
      GeometricManager gm = (GeometricManager)this.flowto.get(qv);
      if (gm == null) {
         gm = new GeometricManager();
         this.flowto.put((FullSensitiveNode)qv, gm);
      }

      return gm.addNewFigure(code, pres) != null;
   }

   private void do_pts_interval_merge() {
      Iterator var1 = this.new_pts.values().iterator();

      while(var1.hasNext()) {
         GeometricManager gm = (GeometricManager)var1.next();
         gm.mergeFigures(Parameters.max_pts_budget);
      }

   }

   private void do_flow_edge_interval_merge() {
      Iterator var1 = this.flowto.values().iterator();

      while(var1.hasNext()) {
         GeometricManager gm = (GeometricManager)var1.next();
         gm.mergeFigures(Parameters.max_cons_budget);
      }

   }

   private SegmentNode[] find_flowto(FullSensitiveNode qv) {
      GeometricManager im = (GeometricManager)this.flowto.get(qv);
      return im == null ? null : im.getFigures();
   }

   private SegmentNode[] find_points_to(AllocNode obj) {
      GeometricManager im = (GeometricManager)this.pt_objs.get(obj);
      return im == null ? null : im.getFigures();
   }

   private static int infer_pts_is_one_to_one(SegmentNode pts, SegmentNode pe, int code) {
      long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
      long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
      if (interI < interJ) {
         switch(code) {
         case 0:
            pres.I1 = interI - pe.I1 + pe.I2;
            pres.I2 = interI - pts.I1 + pts.I2;
            pres.L = interJ - interI;
            return 0;
         case 1:
            pres.I1 = pe.I2;
            pres.I2 = interI - pts.I1 + pts.I2;
            pres.L = ((RectangleNode)pe).L_prime;
            pres.L_prime = interJ - interI;
            return 1;
         }
      }

      return -1;
   }

   private static int infer_pts_is_many_to_many(RectangleNode pts, SegmentNode pe, int code) {
      long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
      long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
      if (interI < interJ) {
         switch(code) {
         case 0:
            pres.I1 = interI - pe.I1 + pe.I2;
            pres.I2 = pts.I2;
            pres.L = interJ - interI;
            pres.L_prime = pts.L_prime;
            break;
         case 1:
            pres.I1 = pe.I2;
            pres.I2 = pts.I2;
            pres.L = ((RectangleNode)pe).L_prime;
            pres.L_prime = pts.L_prime;
         }

         return 1;
      } else {
         return -1;
      }
   }

   private static boolean reasonAndPropagate(FullSensitiveNode qn, AllocNode obj, SegmentNode pts, SegmentNode pe, int code) {
      int ret_type = -1;
      switch(code >> 8) {
      case 0:
         ret_type = infer_pts_is_one_to_one(pts, pe, code & 255);
         break;
      case 1:
         ret_type = infer_pts_is_many_to_many((RectangleNode)pts, pe, code & 255);
      }

      return ret_type != -1 ? qn.addPointsTo(ret_type, obj) : false;
   }

   private static boolean instantiateLoadConstraint(FullSensitiveNode objn, FullSensitiveNode qn, SegmentNode pts, int code) {
      int ret_type = -1;
      if (code >> 8 == 0) {
         pres.I1 = pts.I2;
         pres.I2 = pts.I1;
         switch(code & 255) {
         case 0:
            pres.L = pts.L;
            ret_type = 0;
            break;
         case 1:
            pres.L = ((RectangleNode)pts).L_prime;
            pres.L_prime = pts.L;
            ret_type = 1;
         }
      } else {
         pres.I1 = pts.I2;
         pres.I2 = 1L;
         pres.L_prime = 1L;
         switch(code & 255) {
         case 0:
            pres.L = pts.L;
            ret_type = 1;
            break;
         case 1:
            pres.L = ((RectangleNode)pts).L_prime;
            ret_type = 1;
         }
      }

      return objn.addFlowsTo(ret_type, qn);
   }

   private static boolean instantiateStoreConstraint(FullSensitiveNode qn, FullSensitiveNode objn, SegmentNode pts, int code) {
      int ret_type = -1;
      if (code >> 8 == 0) {
         pres.I1 = pts.I1;
         pres.I2 = pts.I2;
         pres.L = pts.L;
         switch(code & 255) {
         case 0:
            ret_type = 0;
            break;
         case 1:
            pres.L_prime = ((RectangleNode)pts).L_prime;
            ret_type = 1;
         }
      } else {
         pres.I1 = 1L;
         pres.I2 = pts.I2;
         pres.L = 1L;
         switch(code & 255) {
         case 0:
            pres.L_prime = pts.L;
            ret_type = 1;
            break;
         case 1:
            pres.L_prime = ((RectangleNode)pts).L_prime;
            ret_type = 1;
         }
      }

      return qn.addFlowsTo(ret_type, objn);
   }

   static {
      stubManager = new GeometricManager();
      pres = new RectangleNode(1L, 1L, 9223372036854775806L, 9223372036854775806L);
      stubManager.addNewFigure(1, pres);
      deadManager = new GeometricManager();
   }
}
