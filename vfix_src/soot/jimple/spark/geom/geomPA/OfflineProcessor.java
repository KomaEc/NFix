package soot.jimple.spark.geom.geomPA;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import soot.SootClass;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.utils.ZArrayNumberer;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;

public class OfflineProcessor {
   private boolean visitedFlag;
   GeomPointsTo geomPTA;
   ZArrayNumberer<IVarAbstraction> int2var;
   ArrayList<OfflineProcessor.off_graph_edge> varGraph;
   int[] pre;
   int[] low;
   int[] count;
   int[] rep;
   int[] repsize;
   Deque<Integer> queue;
   int pre_cnt;
   int n_var;

   public OfflineProcessor(GeomPointsTo pta) {
      this.int2var = pta.pointers;
      int size = this.int2var.size();
      this.varGraph = new ArrayList(size);
      this.queue = new LinkedList();
      this.pre = new int[size];
      this.low = new int[size];
      this.count = new int[size];
      this.rep = new int[size];
      this.repsize = new int[size];
      this.geomPTA = pta;

      for(int i = 0; i < size; ++i) {
         this.varGraph.add((Object)null);
      }

   }

   public void init() {
      this.n_var = this.int2var.size();

      for(int i = 0; i < this.n_var; ++i) {
         this.varGraph.set(i, (Object)null);
         ((IVarAbstraction)this.int2var.get((long)i)).willUpdate = false;
      }

   }

   public void defaultFeedPtsRoutines() {
      switch(Parameters.seedPts) {
      case 15:
         this.setAllUserCodeVariablesUseful();
      default:
         Set<Node> multiBaseptrs = new HashSet();
         Iterator var7 = this.geomPTA.multiCallsites.iterator();

         while(var7.hasNext()) {
            Stmt callsite = (Stmt)var7.next();
            InstanceInvokeExpr iie = (InstanceInvokeExpr)callsite.getInvokeExpr();
            VarNode vn = this.geomPTA.findLocalVarNode(iie.getBase());
            multiBaseptrs.add(vn);
         }

         this.addUserDefPts(multiBaseptrs);
         return;
      case Integer.MAX_VALUE:
         for(int i = 0; i < this.n_var; ++i) {
            IVarAbstraction pn = (IVarAbstraction)this.int2var.get((long)i);
            if (pn != null && pn.getRepresentative() == pn) {
               pn.willUpdate = true;
            }
         }

      }
   }

   public void addUserDefPts(Set<Node> initVars) {
      Iterator var2 = initVars.iterator();

      while(var2.hasNext()) {
         Node vn = (Node)var2.next();
         IVarAbstraction pn = this.geomPTA.findInternalNode(vn);
         if (pn != null) {
            pn = pn.getRepresentative();
            if (pn.reachable()) {
               pn.willUpdate = true;
            }
         }
      }

   }

   public void releaseSparkMem() {
      for(int i = 0; i < this.n_var; ++i) {
         IVarAbstraction pn = (IVarAbstraction)this.int2var.get((long)i);
         if (pn == pn.getRepresentative() && pn.willUpdate) {
            Node vn = pn.getWrappedNode();
            vn.discardP2Set();
         }
      }

      System.gc();
      System.gc();
      System.gc();
      System.gc();
   }

   public void runOptimizations() {
      this.buildDependenceGraph();
      this.distillConstraints();
      this.buildImpactGraph();
      this.computeWeightsForPts();
   }

   public void destroy() {
      this.pre = null;
      this.low = null;
      this.count = null;
      this.rep = null;
      this.repsize = null;
      this.varGraph = null;
      this.queue = null;
   }

   protected void buildDependenceGraph() {
      Iterator var1 = this.geomPTA.constraints.iterator();

      while(true) {
         while(true) {
            label45:
            while(var1.hasNext()) {
               PlainConstraint cons = (PlainConstraint)var1.next();
               final IVarAbstraction lhs = cons.getLHS();
               final IVarAbstraction rhs = cons.getRHS();
               final SparkField field = cons.f;
               IVarAbstraction rep;
               Iterator var7;
               AllocNode o;
               IVarAbstraction padf;
               OfflineProcessor.off_graph_edge e;
               switch(cons.type) {
               case 1:
                  this.add_graph_edge(rhs.id, lhs.id);
                  break;
               case 2:
                  rep = lhs.getRepresentative();
                  if (!rep.hasPTResult()) {
                     lhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode)n, field);
                           if (padf != null && padf.reachable()) {
                              OfflineProcessor.off_graph_edge e = OfflineProcessor.this.add_graph_edge(rhs.id, padf.id);
                              e.base_var = lhs;
                           }
                        }
                     });
                     break;
                  } else {
                     var7 = rep.get_all_points_to_objects().iterator();

                     while(true) {
                        if (!var7.hasNext()) {
                           continue label45;
                        }

                        o = (AllocNode)var7.next();
                        padf = this.geomPTA.findInstanceField(o, field);
                        if (padf != null && padf.reachable()) {
                           e = this.add_graph_edge(rhs.id, padf.id);
                           e.base_var = lhs;
                        }
                     }
                  }
               case 3:
                  rep = rhs.getRepresentative();
                  if (!rep.hasPTResult()) {
                     rhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode)n, field);
                           if (padf != null && padf.reachable()) {
                              OfflineProcessor.off_graph_edge e = OfflineProcessor.this.add_graph_edge(padf.id, lhs.id);
                              e.base_var = rhs;
                           }
                        }
                     });
                  } else {
                     var7 = rep.get_all_points_to_objects().iterator();

                     while(var7.hasNext()) {
                        o = (AllocNode)var7.next();
                        padf = this.geomPTA.findInstanceField(o, field);
                        if (padf != null && padf.reachable()) {
                           e = this.add_graph_edge(padf.id, lhs.id);
                           e.base_var = rhs;
                        }
                     }
                  }
               }
            }

            return;
         }
      }
   }

   protected void setAllUserCodeVariablesUseful() {
      for(int i = 0; i < this.n_var; ++i) {
         IVarAbstraction pn = (IVarAbstraction)this.int2var.get((long)i);
         if (pn == pn.getRepresentative()) {
            Node node = pn.getWrappedNode();
            int sm_id = this.geomPTA.getMethodIDFromPtr(pn);
            if (this.geomPTA.isReachableMethod(sm_id) && node instanceof VarNode) {
               boolean defined_in_lib = false;
               if (node instanceof LocalVarNode) {
                  defined_in_lib = ((LocalVarNode)node).getMethod().isJavaLibraryMethod();
               } else if (node instanceof GlobalVarNode) {
                  SootClass sc = ((GlobalVarNode)node).getDeclaringClass();
                  if (sc != null) {
                     defined_in_lib = sc.isJavaLibraryClass();
                  }
               }

               if (!defined_in_lib && !this.geomPTA.isExceptionPointer(node)) {
                  pn.willUpdate = true;
               }
            }
         }
      }

   }

   protected void computeReachablePts() {
      this.queue.clear();

      int i;
      IVarAbstraction pn;
      for(i = 0; i < this.n_var; ++i) {
         pn = (IVarAbstraction)this.int2var.get((long)i);
         if (pn.willUpdate) {
            this.queue.add(i);
         }
      }

      while(!this.queue.isEmpty()) {
         i = (Integer)this.queue.getFirst();
         this.queue.removeFirst();

         for(OfflineProcessor.off_graph_edge p = (OfflineProcessor.off_graph_edge)this.varGraph.get(i); p != null; p = p.next) {
            pn = (IVarAbstraction)this.int2var.get((long)p.t);
            if (!pn.willUpdate) {
               pn.willUpdate = true;
               this.queue.add(p.t);
            }

            pn = p.base_var;
            if (pn != null && !pn.willUpdate) {
               pn.willUpdate = true;
               this.queue.add(pn.id);
            }
         }
      }

   }

   protected void distillConstraints() {
      this.computeReachablePts();

      PlainConstraint cons;
      for(Iterator var2 = this.geomPTA.constraints.iterator(); var2.hasNext(); cons.isActive = this.visitedFlag) {
         cons = (PlainConstraint)var2.next();
         IVarAbstraction pn = cons.getRHS();
         final SparkField field = cons.f;
         this.visitedFlag = false;
         switch(cons.type) {
         case 0:
         case 1:
         case 2:
            this.visitedFlag = pn.willUpdate;
            break;
         case 3:
            pn = pn.getRepresentative();
            if (!pn.hasPTResult()) {
               pn.getWrappedNode().getP2Set().forall(new P2SetVisitor() {
                  public void visit(Node n) {
                     if (!OfflineProcessor.this.visitedFlag) {
                        IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode)n, field);
                        if (padf != null && padf.reachable()) {
                           OfflineProcessor.this.visitedFlag = OfflineProcessor.this.visitedFlag | padf.willUpdate;
                        }
                     }
                  }
               });
            } else {
               Iterator var5 = pn.get_all_points_to_objects().iterator();

               while(var5.hasNext()) {
                  AllocNode o = (AllocNode)var5.next();
                  IVarAbstraction padf = this.geomPTA.findInstanceField(o, field);
                  if (padf != null && padf.reachable()) {
                     this.visitedFlag |= padf.willUpdate;
                     if (this.visitedFlag) {
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   protected void buildImpactGraph() {
      for(int i = 0; i < this.n_var; ++i) {
         this.varGraph.set(i, (Object)null);
      }

      this.queue.clear();
      Iterator var10 = this.geomPTA.constraints.iterator();

      while(true) {
         while(true) {
            label55:
            while(true) {
               PlainConstraint cons;
               do {
                  if (!var10.hasNext()) {
                     return;
                  }

                  cons = (PlainConstraint)var10.next();
               } while(!cons.isActive);

               final IVarAbstraction lhs = cons.getLHS();
               final IVarAbstraction rhs = cons.getRHS();
               final SparkField field = cons.f;
               IVarAbstraction rep;
               Iterator var7;
               AllocNode o;
               IVarAbstraction padf;
               switch(cons.type) {
               case 0:
                  this.queue.add(rhs.id);
                  break;
               case 1:
                  this.add_graph_edge(lhs.id, rhs.id);
                  break;
               case 2:
                  rep = lhs.getRepresentative();
                  if (!rep.hasPTResult()) {
                     lhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode)n, field);
                           if (padf != null && padf.reachable()) {
                              OfflineProcessor.this.add_graph_edge(padf.id, rhs.id);
                           }
                        }
                     });
                     break;
                  } else {
                     var7 = rep.get_all_points_to_objects().iterator();

                     while(true) {
                        if (!var7.hasNext()) {
                           continue label55;
                        }

                        o = (AllocNode)var7.next();
                        padf = this.geomPTA.findInstanceField(o, field);
                        if (padf != null && padf.reachable()) {
                           this.add_graph_edge(padf.id, rhs.id);
                        }
                     }
                  }
               case 3:
                  rep = rhs.getRepresentative();
                  if (!rep.hasPTResult()) {
                     rhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode)n, field);
                           if (padf != null && padf.reachable()) {
                              OfflineProcessor.this.add_graph_edge(lhs.id, padf.id);
                           }
                        }
                     });
                  } else {
                     var7 = rep.get_all_points_to_objects().iterator();

                     while(var7.hasNext()) {
                        o = (AllocNode)var7.next();
                        padf = this.geomPTA.findInstanceField(o, field);
                        if (padf != null && padf.reachable()) {
                           this.add_graph_edge(lhs.id, padf.id);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void computeWeightsForPts() {
      this.pre_cnt = 0;

      int i;
      IVarAbstraction node;
      for(i = 0; i < this.n_var; ++i) {
         this.pre[i] = -1;
         this.count[i] = 0;
         this.rep[i] = i;
         this.repsize[i] = 1;
         node = (IVarAbstraction)this.int2var.get((long)i);
         node.top_value = Integer.MIN_VALUE;
      }

      for(i = 0; i < this.n_var; ++i) {
         if (this.pre[i] == -1) {
            this.tarjan_scc(i);
         }
      }

      int var10002;
      int s;
      int t;
      OfflineProcessor.off_graph_edge p;
      for(i = 0; i < this.n_var; ++i) {
         p = (OfflineProcessor.off_graph_edge)this.varGraph.get(i);

         for(s = this.find_parent(i); p != null; p = p.next) {
            t = this.find_parent(p.t);
            if (t != s) {
               var10002 = this.count[t]++;
            }
         }
      }

      for(i = 0; i < this.n_var; ++i) {
         p = (OfflineProcessor.off_graph_edge)this.varGraph.get(i);
         if (p != null && this.rep[i] != i) {
            for(t = this.find_parent(i); p.next != null; p = p.next) {
            }

            p.next = (OfflineProcessor.off_graph_edge)this.varGraph.get(t);
            this.varGraph.set(t, this.varGraph.get(i));
            this.varGraph.set(i, (Object)null);
         }
      }

      this.queue.clear();

      for(i = 0; i < this.n_var; ++i) {
         if (this.rep[i] == i && this.count[i] == 0) {
            this.queue.addLast(i);
         }
      }

      i = 0;

      while(!this.queue.isEmpty()) {
         s = (Integer)this.queue.getFirst();
         this.queue.removeFirst();
         node = (IVarAbstraction)this.int2var.get((long)s);
         node.top_value = i;
         i += this.repsize[s];

         for(p = (OfflineProcessor.off_graph_edge)this.varGraph.get(s); p != null; p = p.next) {
            t = this.find_parent(p.t);
            if (t != s && --this.count[t] == 0) {
               this.queue.addLast(t);
            }
         }
      }

      for(i = this.n_var - 1; i > -1; --i) {
         if (this.rep[i] != i) {
            node = (IVarAbstraction)this.int2var.get((long)this.find_parent(i));
            IVarAbstraction me = (IVarAbstraction)this.int2var.get((long)i);
            me.top_value = node.top_value + this.repsize[node.id] - 1;
            var10002 = this.repsize[node.id]--;
         }
      }

   }

   private OfflineProcessor.off_graph_edge add_graph_edge(int s, int t) {
      OfflineProcessor.off_graph_edge e = new OfflineProcessor.off_graph_edge();
      e.s = s;
      e.t = t;
      e.next = (OfflineProcessor.off_graph_edge)this.varGraph.get(s);
      this.varGraph.set(s, e);
      return e;
   }

   private void tarjan_scc(int s) {
      this.pre[s] = this.low[s] = this.pre_cnt++;
      this.queue.addLast(s);

      int t;
      for(OfflineProcessor.off_graph_edge p = (OfflineProcessor.off_graph_edge)this.varGraph.get(s); p != null; p = p.next) {
         t = p.t;
         if (this.pre[t] == -1) {
            this.tarjan_scc(t);
         }

         if (this.low[t] < this.low[s]) {
            this.low[s] = this.low[t];
         }
      }

      if (this.low[s] >= this.pre[s]) {
         int w = s;

         do {
            t = (Integer)this.queue.getLast();
            this.queue.removeLast();
            int[] var10000 = this.low;
            var10000[t] += this.n_var;
            w = this.merge_nodes(w, t);
         } while(t != s);

      }
   }

   private int find_parent(int v) {
      return v == this.rep[v] ? v : (this.rep[v] = this.find_parent(this.rep[v]));
   }

   private int merge_nodes(int v1, int v2) {
      v1 = this.find_parent(v1);
      v2 = this.find_parent(v2);
      if (v1 != v2) {
         if (this.repsize[v1] < this.repsize[v2]) {
            int t = v1;
            v1 = v2;
            v2 = t;
         }

         this.rep[v2] = v1;
         int[] var10000 = this.repsize;
         var10000[v1] += this.repsize[v2];
      }

      return v1;
   }

   class off_graph_edge {
      int s;
      int t;
      IVarAbstraction base_var;
      OfflineProcessor.off_graph_edge next;
   }
}
