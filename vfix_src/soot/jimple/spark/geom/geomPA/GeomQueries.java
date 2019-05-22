package soot.jimple.spark.geom.geomPA;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import soot.Local;
import soot.PointsToSet;
import soot.SootMethod;
import soot.jimple.spark.geom.dataMgr.ContextsCollector;
import soot.jimple.spark.geom.dataMgr.Obj_full_extractor;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.IntervalContextVar;
import soot.jimple.spark.geom.dataRep.SimpleInterval;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.Edge;

public class GeomQueries {
   protected GeomPointsTo geomPTA = null;
   protected int n_func;
   protected CgEdge[] call_graph;
   protected int[] vis_cg;
   protected int[] rep_cg;
   protected int[] scc_size;
   protected int[] block_num;
   protected long[] max_context_size_block;
   protected int[] top_rank;
   private boolean prop_initialized = false;
   private Queue<Integer> topQ;
   private int[] in_degree;
   private ContextsCollector[] contextsForMethods;

   public GeomQueries(GeomPointsTo geom_pta) {
      this.geomPTA = geom_pta;
      this.n_func = this.geomPTA.n_func;
      this.vis_cg = this.geomPTA.vis_cg;
      this.rep_cg = this.geomPTA.rep_cg;
      this.scc_size = this.geomPTA.scc_size;
      this.block_num = this.geomPTA.block_num;
      this.max_context_size_block = this.geomPTA.max_context_size_block;
      this.call_graph = new CgEdge[this.n_func];
      Arrays.fill(this.call_graph, (Object)null);
      this.in_degree = new int[this.n_func];
      Arrays.fill(this.in_degree, 0);
      CgEdge[] raw_call_graph = this.geomPTA.call_graph;

      for(int i = 0; i < this.n_func; ++i) {
         if (this.vis_cg[i] != 0) {
            CgEdge p = raw_call_graph[i];

            for(int rep = this.rep_cg[i]; p != null; p = p.next) {
               if (!p.scc_edge) {
                  CgEdge q = p.duplicate();
                  q.next = this.call_graph[rep];
                  this.call_graph[rep] = q;
                  int var10002 = this.in_degree[this.rep_cg[q.t]]++;
               }
            }
         }
      }

   }

   private void prepareIntervalPropagations() {
      if (!this.prop_initialized) {
         this.top_rank = new int[this.n_func];
         Arrays.fill(this.top_rank, 0);
         this.topQ = new LinkedList();
         this.topQ.add(0);

         int i;
         while(!this.topQ.isEmpty()) {
            i = (Integer)this.topQ.poll();

            for(CgEdge p = this.call_graph[i]; p != null; p = p.next) {
               int t = p.t;
               int rep_t = this.rep_cg[t];
               int w = this.top_rank[i] + 1;
               if (this.top_rank[rep_t] < w) {
                  this.top_rank[rep_t] = w;
               }

               if (--this.in_degree[rep_t] == 0) {
                  this.topQ.add(rep_t);
               }
            }
         }

         this.contextsForMethods = new ContextsCollector[this.n_func];

         for(i = 0; i < this.n_func; ++i) {
            ContextsCollector cc = new ContextsCollector();
            cc.setBudget(Parameters.qryBudgetSize);
            this.contextsForMethods[i] = cc;
         }

         this.prop_initialized = true;
      }
   }

   protected boolean dfsScanSubgraph(int s, int target) {
      int rep_s = this.rep_cg[s];
      int rep_target = this.rep_cg[target];
      if (rep_s == rep_target) {
         return true;
      } else {
         boolean reachable = false;

         for(CgEdge p = this.call_graph[rep_s]; p != null; p = p.next) {
            int t = p.t;
            int rep_t = this.rep_cg[t];
            if (this.in_degree[rep_t] != 0 || this.top_rank[rep_t] <= this.top_rank[rep_target] && this.dfsScanSubgraph(t, target)) {
               int var10002 = this.in_degree[rep_t]++;
               reachable = true;
            }
         }

         return reachable;
      }
   }

   protected void transferInSCC(int s, int t, long L, long R, ContextsCollector tContexts) {
      if (s == t && this.scc_size[s] == 1) {
         tContexts.insert(L, R);
      } else {
         int n_blocks = this.block_num[t];
         long block_size = this.max_context_size_block[this.rep_cg[s]];
         long offset = (L - 1L) % block_size;
         long ctxtLength = R - L;
         long block_offset = 0L;

         for(int i = 0; i < n_blocks; ++i) {
            long lEnd = 1L + offset + block_offset;
            long rEnd = lEnd + ctxtLength;
            tContexts.insert(lEnd, rEnd);
            block_offset += block_size;
         }

      }
   }

   protected boolean propagateIntervals(int start, long L, long R, int target) {
      if (!this.dfsScanSubgraph(start, target)) {
         return false;
      } else {
         int rep_start = this.rep_cg[start];
         int rep_target = this.rep_cg[target];
         ContextsCollector targetContexts = this.contextsForMethods[target];
         if (rep_start == rep_target) {
            this.transferInSCC(start, target, L, R, targetContexts);
         } else {
            this.transferInSCC(start, rep_start, L, R, this.contextsForMethods[rep_start]);
            this.topQ.clear();
            this.topQ.add(rep_start);

            while(!this.topQ.isEmpty()) {
               int s = (Integer)this.topQ.poll();
               ContextsCollector sContexts = this.contextsForMethods[s];

               for(CgEdge p = this.call_graph[s]; p != null; p = p.next) {
                  int t = p.t;
                  int rep_t = this.rep_cg[t];
                  if (this.in_degree[rep_t] != 0) {
                     ContextsCollector reptContexts = this.contextsForMethods[rep_t];
                     long block_size = this.max_context_size_block[s];
                     Iterator var18 = sContexts.bars.iterator();

                     while(var18.hasNext()) {
                        SimpleInterval si = (SimpleInterval)var18.next();
                        long in_block_offset = (si.L - 1L) % block_size;
                        long newL = p.map_offset + in_block_offset;
                        long newR = si.R - si.L + newL;
                        if (rep_t == rep_target) {
                           this.transferInSCC(t, target, newL, newR, targetContexts);
                        } else {
                           this.transferInSCC(t, rep_t, newL, newR, reptContexts);
                        }
                     }

                     if (--this.in_degree[rep_t] == 0 && rep_t != rep_target) {
                        this.topQ.add(rep_t);
                     }
                  }
               }

               sContexts.clear();
            }
         }

         return true;
      }
   }

   public boolean contextsGoBy(Edge sootEdge, Local l, PtSensVisitor visitor) {
      CgEdge ctxt = this.geomPTA.getInternalEdgeFromSootEdge(sootEdge);
      if (ctxt != null && !ctxt.is_obsoleted) {
         LocalVarNode vn = this.geomPTA.findLocalVarNode(l);
         if (vn == null) {
            return false;
         } else {
            IVarAbstraction pn = this.geomPTA.findInternalNode(vn);
            if (pn == null) {
               return false;
            } else {
               pn = pn.getRepresentative();
               if (!pn.hasPTResult()) {
                  return false;
               } else {
                  SootMethod sm = vn.getMethod();
                  int target = this.geomPTA.getIDFromSootMethod(sm);
                  if (target == -1) {
                     return false;
                  } else {
                     long L = ctxt.map_offset;
                     long R = L + this.max_context_size_block[this.rep_cg[ctxt.s]];

                     assert L < R;

                     visitor.prepare();
                     this.prepareIntervalPropagations();
                     if (this.propagateIntervals(ctxt.t, L, R, target)) {
                        ContextsCollector targetContexts = this.contextsForMethods[target];
                        Iterator var14 = targetContexts.bars.iterator();

                        while(var14.hasNext()) {
                           SimpleInterval si = (SimpleInterval)var14.next();

                           assert si.L < si.R;

                           pn.get_all_context_sensitive_objects(si.L, si.R, visitor);
                        }

                        targetContexts.clear();
                     }

                     visitor.finish();
                     return visitor.numOfDiffObjects() != 0;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean contexsByAnyCallEdge(Edge sootEdge, Local l, PtSensVisitor visitor) {
      return this.contextsGoBy(sootEdge, l, visitor);
   }

   public boolean contextsGoBy(Edge sootEdge, Local l, SparkField field, PtSensVisitor visitor) {
      Obj_full_extractor pts_l = new Obj_full_extractor();
      if (!this.contextsGoBy(sootEdge, l, pts_l)) {
         return false;
      } else {
         visitor.prepare();
         Iterator var6 = pts_l.outList.iterator();

         while(var6.hasNext()) {
            IntervalContextVar icv = (IntervalContextVar)var6.next();
            AllocNode obj = (AllocNode)icv.var;
            AllocDotField obj_f = this.geomPTA.findAllocDotField(obj, field);
            if (obj_f != null) {
               IVarAbstraction objField = this.geomPTA.findInternalNode(obj_f);
               if (objField != null) {
                  long L = icv.L;
                  long R = icv.R;

                  assert L < R;

                  objField.get_all_context_sensitive_objects(L, R, visitor);
               }
            }
         }

         pts_l = null;
         visitor.finish();
         return visitor.numOfDiffObjects() != 0;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean contextsByAnyCallEdge(Edge sootEdge, Local l, SparkField field, PtSensVisitor visitor) {
      return this.contextsGoBy(sootEdge, l, visitor);
   }

   public boolean kCFA(Edge[] callEdgeChain, Local l, PtSensVisitor visitor) {
      SootMethod firstMethod = callEdgeChain[0].src();
      int firstMethodID = this.geomPTA.getIDFromSootMethod(firstMethod);
      if (firstMethodID == -1) {
         return false;
      } else {
         LocalVarNode vn = this.geomPTA.findLocalVarNode(l);
         if (vn == null) {
            return false;
         } else {
            IVarAbstraction pn = this.geomPTA.findInternalNode(vn);
            if (pn == null) {
               return false;
            } else {
               pn = pn.getRepresentative();
               if (!pn.hasPTResult()) {
                  return false;
               } else {
                  SootMethod sm = vn.getMethod();
                  if (this.geomPTA.getIDFromSootMethod(sm) == -1) {
                     return false;
                  } else {
                     visitor.prepare();
                     long L = 1L;

                     for(int i = 0; i < callEdgeChain.length; ++i) {
                        Edge sootEdge = callEdgeChain[i];
                        CgEdge ctxt = this.geomPTA.getInternalEdgeFromSootEdge(sootEdge);
                        if (ctxt == null || ctxt.is_obsoleted) {
                           return false;
                        }

                        int caller = this.geomPTA.getIDFromSootMethod(sootEdge.src());
                        long block_size = this.max_context_size_block[this.rep_cg[caller]];
                        long in_block_offset = (L - 1L) % block_size;
                        L = ctxt.map_offset + in_block_offset;
                     }

                     long ctxtLength = this.max_context_size_block[this.rep_cg[firstMethodID]];
                     long R = L + ctxtLength;
                     pn.get_all_context_sensitive_objects(L, R, visitor);
                     visitor.finish();
                     return visitor.numOfDiffObjects() != 0;
                  }
               }
            }
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean contextsByCallChain(Edge[] callEdgeChain, Local l, PtSensVisitor visitor) {
      return this.kCFA(callEdgeChain, l, visitor);
   }

   public boolean kCFA(Edge[] callEdgeChain, Local l, SparkField field, PtSensVisitor visitor) {
      Obj_full_extractor pts_l = new Obj_full_extractor();
      if (!this.kCFA(callEdgeChain, l, pts_l)) {
         return false;
      } else {
         visitor.prepare();
         Iterator var6 = pts_l.outList.iterator();

         while(var6.hasNext()) {
            IntervalContextVar icv = (IntervalContextVar)var6.next();
            AllocNode obj = (AllocNode)icv.var;
            AllocDotField obj_f = this.geomPTA.findAllocDotField(obj, field);
            if (obj_f != null) {
               IVarAbstraction objField = this.geomPTA.findInternalNode(obj_f);
               if (objField != null) {
                  long L = icv.L;
                  long R = icv.R;

                  assert L < R;

                  objField.get_all_context_sensitive_objects(L, R, visitor);
               }
            }
         }

         pts_l = null;
         visitor.finish();
         return visitor.numOfDiffObjects() != 0;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean contextsByCallChain(Edge[] callEdgeChain, Local l, SparkField field, PtSensVisitor visitor) {
      return this.kCFA(callEdgeChain, l, field, visitor);
   }

   public boolean isAliasCI(Local l1, Local l2) {
      PointsToSet pts1 = this.geomPTA.reachingObjects(l1);
      PointsToSet pts2 = this.geomPTA.reachingObjects(l2);
      return pts1.hasNonEmptyIntersection(pts2);
   }

   public boolean isAlias(IVarAbstraction pn1, IVarAbstraction pn2) {
      pn1 = pn1.getRepresentative();
      pn2 = pn2.getRepresentative();
      if (pn1.hasPTResult() && pn2.hasPTResult()) {
         return pn1.heap_sensitive_intersection(pn2);
      } else {
         VarNode vn1 = (VarNode)pn1.getWrappedNode();
         VarNode vn2 = (VarNode)pn2.getWrappedNode();
         return this.isAliasCI((Local)vn1.getVariable(), (Local)vn2.getVariable());
      }
   }

   public boolean isAlias(Local l1, Local l2) {
      LocalVarNode vn1 = this.geomPTA.findLocalVarNode(l1);
      LocalVarNode vn2 = this.geomPTA.findLocalVarNode(l2);
      if (vn1 != null && vn2 != null) {
         IVarAbstraction pn1 = this.geomPTA.findInternalNode(vn1);
         IVarAbstraction pn2 = this.geomPTA.findInternalNode(vn2);
         if (pn1 != null && pn2 != null) {
            pn1 = pn1.getRepresentative();
            pn2 = pn2.getRepresentative();
            return pn1.hasPTResult() && pn2.hasPTResult() ? pn1.heap_sensitive_intersection(pn2) : this.isAliasCI(l1, l2);
         } else {
            return this.isAliasCI(l1, l2);
         }
      } else {
         return false;
      }
   }
}
