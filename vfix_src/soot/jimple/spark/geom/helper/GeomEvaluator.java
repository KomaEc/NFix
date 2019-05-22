package soot.jimple.spark.geom.helper;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.Local;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.geom.utils.Histogram;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class GeomEvaluator {
   private static final Logger logger = LoggerFactory.getLogger(GeomEvaluator.class);
   private GeomPointsTo ptsProvider;
   private PrintStream outputer;
   private EvalResults evalRes;
   private boolean solved;

   public GeomEvaluator(GeomPointsTo gpts, PrintStream ps) {
      this.ptsProvider = gpts;
      this.outputer = ps;
      this.evalRes = new EvalResults();
   }

   public void profileSparkBasicMetrics() {
      int n_legal_var = 0;
      int[] limits = new int[]{1, 5, 10, 25, 50, 75, 100};
      this.evalRes.pts_size_bar_spark = new Histogram(limits);
      Iterator var3 = this.ptsProvider.pointers.iterator();

      while(var3.hasNext()) {
         IVarAbstraction pn = (IVarAbstraction)var3.next();
         Node var = pn.getWrappedNode();
         if (!this.ptsProvider.isExceptionPointer(var)) {
            ++n_legal_var;
            int size = var.getP2Set().size();
            this.evalRes.pts_size_bar_spark.addNumber(size);
            EvalResults var10000 = this.evalRes;
            var10000.total_spark_pts += (long)size;
            if (size > this.evalRes.max_pts_spark) {
               this.evalRes.max_pts_spark = size;
            }
         }
      }

      this.evalRes.avg_spark_pts = (double)this.evalRes.total_spark_pts / (double)n_legal_var;
   }

   public void profileGeomBasicMetrics(boolean testSpark) {
      int n_legal_var = 0;
      int n_alloc_dot_fields = 0;
      int[] limits = new int[]{1, 5, 10, 25, 50, 75, 100};
      this.evalRes.pts_size_bar_geom = new Histogram(limits);
      if (testSpark) {
         this.evalRes.total_spark_pts = 0L;
         this.evalRes.max_pts_spark = 0;
         this.evalRes.pts_size_bar_spark = new Histogram(limits);
      }

      Iterator var5 = this.ptsProvider.getAllReachableMethods().iterator();

      EvalResults var10000;
      while(var5.hasNext()) {
         SootMethod sm = (SootMethod)var5.next();
         if (sm.isConcrete()) {
            if (!sm.hasActiveBody()) {
               sm.retrieveActiveBody();
            }

            var10000 = this.evalRes;
            var10000.loc += sm.getActiveBody().getUnits().size();
         }
      }

      var5 = this.ptsProvider.pointers.iterator();

      while(var5.hasNext()) {
         IVarAbstraction pn = (IVarAbstraction)var5.next();
         if (pn.hasPTResult()) {
            pn = pn.getRepresentative();
            Node var = pn.getWrappedNode();
            if (!this.ptsProvider.isExceptionPointer(var)) {
               if (var instanceof AllocDotField) {
                  ++n_alloc_dot_fields;
               }

               ++n_legal_var;
               int size;
               if (testSpark) {
                  size = var.getP2Set().size();
                  this.evalRes.pts_size_bar_spark.addNumber(size);
                  var10000 = this.evalRes;
                  var10000.total_spark_pts += (long)size;
                  if (size > this.evalRes.max_pts_spark) {
                     this.evalRes.max_pts_spark = size;
                  }
               }

               size = pn.num_of_diff_objs();
               this.evalRes.pts_size_bar_geom.addNumber(size);
               var10000 = this.evalRes;
               var10000.total_geom_ins_pts += (long)size;
               if (size > this.evalRes.max_pts_geom) {
                  this.evalRes.max_pts_geom = size;
               }
            }
         }
      }

      this.evalRes.avg_geom_ins_pts = (double)this.evalRes.total_geom_ins_pts / (double)n_legal_var;
      if (testSpark) {
         this.evalRes.avg_spark_pts = (double)this.evalRes.total_spark_pts / (double)n_legal_var;
      }

      this.outputer.println("");
      this.outputer.println("----------Statistical Result of geomPTA <Data Format: geomPTA (SPARK)>----------");
      this.outputer.printf("Lines of code (jimple): %.1fK\n", (double)this.evalRes.loc / 1000.0D);
      this.outputer.printf("Reachable Methods: %d (%d)\n", this.ptsProvider.getNumberOfMethods(), this.ptsProvider.getNumberOfSparkMethods());
      this.outputer.printf("Reachable User Methods: %d (%d)\n", this.ptsProvider.n_reach_user_methods, this.ptsProvider.n_reach_spark_user_methods);
      this.outputer.println("#All Pointers: " + this.ptsProvider.getNumberOfPointers());
      this.outputer.println("#Core Pointers: " + n_legal_var + ", in which #AllocDot Fields: " + n_alloc_dot_fields);
      this.outputer.printf("Total/Average Projected Points-to Tuples [core pointers]: %d (%d) / %.3f (%.3f) \n", this.evalRes.total_geom_ins_pts, this.evalRes.total_spark_pts, this.evalRes.avg_geom_ins_pts, this.evalRes.avg_spark_pts);
      this.outputer.println("The largest points-to set size [core pointers]: " + this.evalRes.max_pts_geom + " (" + this.evalRes.max_pts_spark + ")");
      this.outputer.println();
      this.evalRes.pts_size_bar_geom.printResult(this.outputer, "Points-to Set Sizes Distribution [core pointers]:", this.evalRes.pts_size_bar_spark);
   }

   private void test_1cfa_call_graph(LocalVarNode vn, SootMethod caller, SootMethod callee_signature, Histogram ce_range) {
      IVarAbstraction pn = this.ptsProvider.findInternalNode(vn);
      if (pn != null) {
         pn = pn.getRepresentative();
         Set<SootMethod> tgts = new HashSet();
         Set<AllocNode> set = pn.get_all_points_to_objects();
         LinkedList<CgEdge> list = this.ptsProvider.getCallEdgesInto(this.ptsProvider.getIDFromSootMethod(caller));
         FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
         Iterator it = list.iterator();

         while(it.hasNext()) {
            CgEdge p = (CgEdge)it.next();
            long l = p.map_offset;
            long r = l + this.ptsProvider.max_context_size_block[p.s];
            tgts.clear();
            Iterator var16 = set.iterator();

            while(var16.hasNext()) {
               AllocNode obj = (AllocNode)var16.next();
               if (pn.pointer_interval_points_to(l, r, obj)) {
                  Type t = obj.getType();
                  if (t != null) {
                     if (t instanceof AnySubType) {
                        t = ((AnySubType)t).getBase();
                     } else if (t instanceof ArrayType) {
                        t = RefType.v("java.lang.Object");
                     }

                     try {
                        tgts.add(hierarchy.resolveConcreteDispatch(((RefType)t).getSootClass(), callee_signature));
                     } catch (Exception var20) {
                        logger.debug((String)var20.getMessage(), (Throwable)var20);
                     }
                  }
               }
            }

            tgts.remove((Object)null);
            ce_range.addNumber(tgts.size());
         }

      }
   }

   public void checkCallGraph() {
      int[] limits = new int[]{1, 2, 4, 8};
      this.evalRes.total_call_edges = new Histogram(limits);
      CallGraph cg = Scene.v().getCallGraph();
      Iterator var3 = this.ptsProvider.multiCallsites.iterator();

      while(true) {
         Stmt callsite;
         Iterator edges;
         Edge anyEdge;
         SootMethod src;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     this.ptsProvider.ps.println();
                     this.ptsProvider.ps.println("--------> Virtual Callsites Evaluation <---------");
                     this.ptsProvider.ps.printf("Total virtual callsites (app code): %d (%d)\n", this.evalRes.n_callsites, this.evalRes.n_user_callsites);
                     this.ptsProvider.ps.printf("Total virtual call edges (app code): %d (%d)\n", this.evalRes.n_geom_call_edges, this.evalRes.n_geom_user_edges);
                     this.ptsProvider.ps.printf("Virtual callsites additionally solved by geomPTA compared to SPARK (app code) = %d (%d)\n", this.evalRes.n_geom_solved_all, this.evalRes.n_geom_solved_app);
                     this.evalRes.total_call_edges.printResult(this.ptsProvider.ps, "Testing of unsolved callsites on 1-CFA call graph: ");
                     if (this.ptsProvider.getOpts().verbose()) {
                        this.ptsProvider.outputNotEvaluatedMethods();
                     }

                     return;
                  }

                  callsite = (Stmt)var3.next();
                  edges = cg.edgesOutOf((Unit)callsite);
               } while(!edges.hasNext());

               ++this.evalRes.n_callsites;
               anyEdge = (Edge)edges.next();
               src = anyEdge.src();
            } while(!this.ptsProvider.isReachableMethod(src));
         } while(!this.ptsProvider.isValidMethod(src));

         CgEdge p = this.ptsProvider.getInternalEdgeFromSootEdge(anyEdge);
         LocalVarNode vn = (LocalVarNode)p.base_var;
         int edge_cnt = 1;

         while(edges.hasNext()) {
            ++edge_cnt;
            edges.next();
         }

         EvalResults var10000 = this.evalRes;
         var10000.n_geom_call_edges += edge_cnt;
         if (edge_cnt == 1) {
            ++this.evalRes.n_geom_solved_all;
         }

         if (!src.isJavaLibraryMethod()) {
            InvokeExpr ie = callsite.getInvokeExpr();
            if (edge_cnt == 1) {
               ++this.evalRes.n_geom_solved_app;
               if (this.ptsProvider.getOpts().verbose()) {
                  this.outputer.println();
                  this.outputer.println("<<<<<<<<<   Additional Solved Call   >>>>>>>>>>");
                  this.outputer.println(src.toString());
                  this.outputer.println(ie.toString());
               }
            } else {
               Histogram call_edges = new Histogram(limits);
               this.test_1cfa_call_graph(vn, src, ie.getMethod(), call_edges);
               this.evalRes.total_call_edges.merge(call_edges);
               call_edges = null;
            }

            var10000 = this.evalRes;
            var10000.n_geom_user_edges += edge_cnt;
            ++this.evalRes.n_user_callsites;
         }
      }
   }

   public void checkAliasAnalysis() {
      Set<IVarAbstraction> access_expr = new HashSet();
      ArrayList<IVarAbstraction> al = new ArrayList();
      Value[] values = new Value[2];
      Iterator var4 = this.ptsProvider.getAllReachableMethods().iterator();

      label95:
      while(true) {
         SootMethod sm;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     access_expr.remove((Object)null);
                     al.addAll(access_expr);
                     access_expr = null;
                     Date begin = new Date();
                     int size = al.size();

                     for(int i = 0; i < size; ++i) {
                        IVarAbstraction pn = (IVarAbstraction)al.get(i);
                        VarNode n1 = (VarNode)pn.getWrappedNode();

                        for(int j = i + 1; j < size; ++j) {
                           IVarAbstraction qn = (IVarAbstraction)al.get(j);
                           VarNode n2 = (VarNode)qn.getWrappedNode();
                           if (pn.heap_sensitive_intersection(qn)) {
                              ++this.evalRes.n_hs_alias;
                           }

                           if (n1.getP2Set().hasNonEmptyIntersection(n2.getP2Set())) {
                              ++this.evalRes.n_hi_alias;
                           }
                        }
                     }

                     this.evalRes.n_alias_pairs = (long)(size * (size - 1) / 2);
                     Date end = new Date();
                     this.ptsProvider.ps.println();
                     this.ptsProvider.ps.println("--------> Alias Pairs Evaluation <---------");
                     this.ptsProvider.ps.println("Number of pointer pairs in app code: " + this.evalRes.n_alias_pairs);
                     this.ptsProvider.ps.printf("Heap sensitive alias pairs (by Geom): %d, Percentage = %.3f%%\n", this.evalRes.n_hs_alias, (double)this.evalRes.n_hs_alias / (double)this.evalRes.n_alias_pairs * 100.0D);
                     this.ptsProvider.ps.printf("Heap insensitive alias pairs (by SPARK): %d, Percentage = %.3f%%\n", this.evalRes.n_hi_alias, (double)this.evalRes.n_hi_alias / (double)this.evalRes.n_alias_pairs * 100.0D);
                     this.ptsProvider.ps.printf("Using time: %dms \n", end.getTime() - begin.getTime());
                     this.ptsProvider.ps.println();
                     return;
                  }

                  sm = (SootMethod)var4.next();
               } while(sm.isJavaLibraryMethod());
            } while(!sm.isConcrete());

            if (!sm.hasActiveBody()) {
               sm.retrieveActiveBody();
            }
         } while(!this.ptsProvider.isValidMethod(sm));

         Iterator stmts = sm.getActiveBody().getUnits().iterator();

         while(true) {
            Stmt st;
            do {
               if (!stmts.hasNext()) {
                  continue label95;
               }

               st = (Stmt)stmts.next();
            } while(!(st instanceof AssignStmt));

            AssignStmt a = (AssignStmt)st;
            values[0] = a.getLeftOp();
            values[1] = a.getRightOp();
            Value[] var9 = values;
            int var10 = values.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Value v = var9[var11];
               if (v instanceof InstanceFieldRef) {
                  InstanceFieldRef ifr = (InstanceFieldRef)v;
                  SootField field = ifr.getField();
                  if (field.getType() instanceof RefType) {
                     LocalVarNode vn = this.ptsProvider.findLocalVarNode((Local)ifr.getBase());
                     if (vn != null && !this.ptsProvider.isExceptionPointer(vn)) {
                        IVarAbstraction pn = this.ptsProvider.findInternalNode(vn);
                        if (pn != null) {
                           pn = pn.getRepresentative();
                           if (pn.hasPTResult()) {
                              access_expr.add(pn);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void checkCastsSafety() {
      Iterator var1 = this.ptsProvider.getAllReachableMethods().iterator();

      label92:
      while(true) {
         SootMethod sm;
         do {
            do {
               do {
                  if (!var1.hasNext()) {
                     this.ptsProvider.ps.println();
                     this.ptsProvider.ps.println("-----------> Static Casts Safety Evaluation <------------");
                     this.ptsProvider.ps.println("Total casts (app code): " + this.evalRes.total_casts);
                     this.ptsProvider.ps.println("Safe casts: Geom = " + this.evalRes.geom_solved_casts + ", SPARK = " + this.evalRes.spark_solved_casts);
                     return;
                  }

                  sm = (SootMethod)var1.next();
               } while(sm.isJavaLibraryMethod());
            } while(!sm.isConcrete());

            if (!sm.hasActiveBody()) {
               sm.retrieveActiveBody();
            }
         } while(!this.ptsProvider.isValidMethod(sm));

         Iterator stmts = sm.getActiveBody().getUnits().iterator();

         while(true) {
            Value rhs;
            LocalVarNode node;
            IVarAbstraction pn;
            do {
               do {
                  do {
                     Value lhs;
                     do {
                        do {
                           Stmt st;
                           do {
                              if (!stmts.hasNext()) {
                                 continue label92;
                              }

                              st = (Stmt)stmts.next();
                           } while(!(st instanceof AssignStmt));

                           rhs = ((AssignStmt)st).getRightOp();
                           lhs = ((AssignStmt)st).getLeftOp();
                        } while(!(rhs instanceof CastExpr));
                     } while(!(lhs.getType() instanceof RefLikeType));

                     Value v = ((CastExpr)rhs).getOp();
                     node = this.ptsProvider.findLocalVarNode(v);
                  } while(node == null);

                  pn = this.ptsProvider.findInternalNode(node);
               } while(pn == null);

               pn = pn.getRepresentative();
            } while(!pn.hasPTResult());

            ++this.evalRes.total_casts;
            final Type targetType = (RefLikeType)((CastExpr)rhs).getCastType();
            this.solved = true;
            Set<AllocNode> set = pn.get_all_points_to_objects();
            Iterator var12 = set.iterator();

            while(var12.hasNext()) {
               AllocNode obj = (AllocNode)var12.next();
               this.solved = this.ptsProvider.castNeverFails(obj.getType(), targetType);
               if (!this.solved) {
                  break;
               }
            }

            if (this.solved) {
               ++this.evalRes.geom_solved_casts;
            }

            this.solved = true;
            node.getP2Set().forall(new P2SetVisitor() {
               public void visit(Node arg0) {
                  if (GeomEvaluator.this.solved) {
                     GeomEvaluator.this.solved = GeomEvaluator.this.ptsProvider.castNeverFails(arg0.getType(), targetType);
                  }
               }
            });
            if (this.solved) {
               ++this.evalRes.spark_solved_casts;
            }
         }
      }
   }

   public void estimateHeapDefuseGraph() {
      Map<IVarAbstraction, int[]> defUseCounterForGeom = new HashMap();
      final Map<AllocDotField, int[]> defUseCounterForSpark = new HashMap();
      Date begin = new Date();
      Iterator var4 = this.ptsProvider.getAllReachableMethods().iterator();

      label110:
      while(true) {
         SootMethod sm;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     EvalResults var10000;
                     int[] defUseUnit;
                     for(var4 = defUseCounterForSpark.values().iterator(); var4.hasNext(); var10000.n_spark_du_pairs += (long)defUseUnit[0] * (long)defUseUnit[1]) {
                        defUseUnit = (int[])var4.next();
                        var10000 = this.evalRes;
                     }

                     for(var4 = defUseCounterForGeom.values().iterator(); var4.hasNext(); var10000.n_geom_du_pairs += (long)defUseUnit[0] * (long)defUseUnit[1]) {
                        defUseUnit = (int[])var4.next();
                        var10000 = this.evalRes;
                     }

                     Date end = new Date();
                     this.ptsProvider.ps.println();
                     this.ptsProvider.ps.println("-----------> Heap Def Use Graph Evaluation <------------");
                     this.ptsProvider.ps.println("The edges in the heap def-use graph is (by Geom): " + this.evalRes.n_geom_du_pairs);
                     this.ptsProvider.ps.println("The edges in the heap def-use graph is (by Spark): " + this.evalRes.n_spark_du_pairs);
                     this.ptsProvider.ps.printf("Using time: %dms \n", end.getTime() - begin.getTime());
                     this.ptsProvider.ps.println();
                     return;
                  }

                  sm = (SootMethod)var4.next();
               } while(sm.isJavaLibraryMethod());
            } while(!sm.isConcrete());

            if (!sm.hasActiveBody()) {
               sm.retrieveActiveBody();
            }
         } while(!this.ptsProvider.isValidMethod(sm));

         Iterator stmts = sm.getActiveBody().getUnits().iterator();

         while(true) {
            final Value lValue;
            final SootField field;
            LocalVarNode vn;
            IVarAbstraction pn;
            do {
               do {
                  do {
                     InstanceFieldRef ifr;
                     do {
                        Stmt st;
                        do {
                           if (!stmts.hasNext()) {
                              continue label110;
                           }

                           st = (Stmt)stmts.next();
                        } while(!(st instanceof AssignStmt));

                        AssignStmt a = (AssignStmt)st;
                        lValue = a.getLeftOp();
                        Value rValue = a.getRightOp();
                        ifr = null;
                        if (lValue instanceof InstanceFieldRef) {
                           ifr = (InstanceFieldRef)lValue;
                        } else if (rValue instanceof InstanceFieldRef) {
                           ifr = (InstanceFieldRef)rValue;
                        }
                     } while(ifr == null);

                     field = ifr.getField();
                     vn = this.ptsProvider.findLocalVarNode((Local)ifr.getBase());
                  } while(vn == null);

                  pn = this.ptsProvider.findInternalNode(vn);
               } while(pn == null);

               pn = pn.getRepresentative();
            } while(!pn.hasPTResult());

            vn.getP2Set().forall(new P2SetVisitor() {
               public void visit(Node n) {
                  IVarAbstraction padf = GeomEvaluator.this.ptsProvider.findAndInsertInstanceField((AllocNode)n, field);
                  AllocDotField adf = (AllocDotField)padf.getWrappedNode();
                  int[] defUseUnit = (int[])defUseCounterForSpark.get(adf);
                  if (defUseUnit == null) {
                     defUseUnit = new int[2];
                     defUseCounterForSpark.put(adf, defUseUnit);
                  }

                  int var10002;
                  if (lValue instanceof InstanceFieldRef) {
                     var10002 = defUseUnit[0]++;
                  } else {
                     var10002 = defUseUnit[1]++;
                  }

               }
            });
            Set<AllocNode> objsSet = pn.get_all_points_to_objects();
            Iterator var16 = objsSet.iterator();

            while(var16.hasNext()) {
               AllocNode obj = (AllocNode)var16.next();
               IVarAbstraction padf = this.ptsProvider.findAndInsertInstanceField(obj, field);
               int[] defUseUnit = (int[])defUseCounterForGeom.get(padf);
               if (defUseUnit == null) {
                  defUseUnit = new int[2];
                  defUseCounterForGeom.put(padf, defUseUnit);
               }

               int var10002;
               if (lValue instanceof InstanceFieldRef) {
                  var10002 = defUseUnit[0]++;
               } else {
                  var10002 = defUseUnit[1]++;
               }
            }
         }
      }
   }
}
