package soot.jimple.spark.geom.geomPA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.G;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.geomE.FullSensitiveNodeGenerator;
import soot.jimple.spark.geom.heapinsE.HeapInsNodeGenerator;
import soot.jimple.spark.geom.helper.GeomEvaluator;
import soot.jimple.spark.geom.ptinsE.PtInsNodeGenerator;
import soot.jimple.spark.geom.utils.SootInfo;
import soot.jimple.spark.geom.utils.ZArrayNumberer;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.ContextVarNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.options.SparkOptions;
import soot.toolkits.scalar.Pair;
import soot.util.Numberable;
import soot.util.NumberedString;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public class GeomPointsTo extends PAG {
   private static final Logger logger = LoggerFactory.getLogger(GeomPointsTo.class);
   protected IWorklist worklist = null;
   protected IEncodingBroker nodeGenerator = null;
   protected TypeManager typeManager = null;
   protected OfflineProcessor offlineProcessor = null;
   public Map<Node, IVarAbstraction> consG = null;
   public ZArrayNumberer<IVarAbstraction> pointers = null;
   public ZArrayNumberer<IVarAbstraction> allocations = null;
   public ZArrayNumberer<PlainConstraint> constraints = null;
   public Set<Stmt> thread_run_callsites = null;
   public Set<Stmt> multiCallsites = null;
   public long[] context_size;
   public long[] max_context_size_block;
   public int[] block_num;
   public int max_scc_size;
   public int max_scc_id;
   public int n_func;
   public int n_calls;
   public int n_reach_methods;
   public int n_reach_user_methods;
   public int n_reach_spark_user_methods;
   public int n_init_constraints;
   public String dump_dir = null;
   public PrintStream ps = null;
   protected Map<String, Boolean> validMethods = null;
   protected CgEdge[] call_graph;
   protected Vector<CgEdge> obsoletedEdges = null;
   protected Map<Integer, LinkedList<CgEdge>> rev_call_graph = null;
   protected Deque<Integer> queue_cg = null;
   protected int[] vis_cg;
   protected int[] low_cg;
   protected int[] rep_cg;
   protected int[] indeg_cg;
   protected int[] scc_size;
   protected int pre_cnt;
   protected Map<SootMethod, Integer> func2int = null;
   protected Map<Integer, SootMethod> int2func = null;
   protected Map<Edge, CgEdge> edgeMapping = null;
   private boolean hasTransformed = false;
   private boolean hasExecuted = false;
   private boolean ddPrepared = false;

   public GeomPointsTo(SparkOptions opts) {
      super(opts);
   }

   public String toString() {
      return "Geometric Points-To Analysis";
   }

   private void prepareContainers() {
      this.consG = new HashMap(39341);
      this.pointers = new ZArrayNumberer(25771);
      this.allocations = new ZArrayNumberer();
      this.constraints = new ZArrayNumberer(25771);
      this.thread_run_callsites = new HashSet(251);
      this.multiCallsites = new HashSet(251);
      this.queue_cg = new LinkedList();
      this.func2int = new HashMap(5011);
      this.int2func = new HashMap(5011);
      this.edgeMapping = new HashMap(19763);
      this.consG.clear();
      this.constraints.clear();
      this.func2int.clear();
      this.edgeMapping.clear();
   }

   public void parametrize(double spark_run_time) {
      int solver_encoding = this.opts.geom_encoding();
      if (solver_encoding == 1) {
         this.nodeGenerator = new FullSensitiveNodeGenerator();
      } else if (solver_encoding == 2) {
         this.nodeGenerator = new HeapInsNodeGenerator();
      } else if (solver_encoding == 3) {
         this.nodeGenerator = new PtInsNodeGenerator();
      }

      String encoding_name = this.nodeGenerator.getSignature();
      if (encoding_name == null) {
         throw new RuntimeException("No encoding given for geometric points-to analysis.");
      } else if (this.nodeGenerator == null) {
         throw new RuntimeException("The encoding " + encoding_name + " is unavailable for geometric points-to analysis.");
      } else {
         switch(this.opts.geom_worklist()) {
         case 1:
            this.worklist = new PQ_Worklist();
            break;
         case 2:
            this.worklist = new FIFO_Worklist();
         }

         this.dump_dir = this.opts.geom_dump_verbose();
         File dir = null;
         if (!this.dump_dir.isEmpty()) {
            dir = new File(this.dump_dir);
            if (!dir.exists()) {
               dir.mkdirs();
            }

            File log_file = new File(this.dump_dir, encoding_name + (this.opts.geom_blocking() ? "_blocked" : "_unblocked") + "_frac" + this.opts.geom_frac_base() + "_runs" + this.opts.geom_runs() + "_log.txt");

            try {
               this.ps = new PrintStream(log_file);
               logger.debug("[Geom] Analysis log can be found in: " + log_file.toString());
            } catch (FileNotFoundException var9) {
               String msg = "[Geom] The dump file: " + log_file.toString() + " cannot be created. Abort.";
               logger.debug("" + msg);
               throw new RuntimeException(msg, var9);
            }
         } else {
            this.ps = G.v().out;
         }

         String method_verify_file = this.opts.geom_verify_name();
         if (method_verify_file != null) {
            try {
               FileReader fr = new FileReader(method_verify_file);
               Scanner fin = new Scanner(fr);
               this.validMethods = new HashMap();

               while(fin.hasNextLine()) {
                  this.validMethods.put(fin.nextLine(), Boolean.FALSE);
               }

               fin.close();
               fr.close();
               logger.debug("[Geom] Read in verification file successfully.\n");
            } catch (FileNotFoundException var10) {
               this.validMethods = null;
            } catch (IOException var11) {
               logger.debug((String)var11.getMessage(), (Throwable)var11);
            }
         }

         Parameters.seedPts = this.opts.geom_app_only() ? 15 : Integer.MAX_VALUE;
         double mem = (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
         this.ps.println();
         this.ps.printf("[Spark] Time: %.3f s\n", spark_run_time / 1000.0D);
         this.ps.printf("[Spark] Memory: %.1f MB\n", mem / 1024.0D / 1024.0D);
         this.typeManager = this.getTypeManager();
         Parameters.max_cons_budget = this.opts.geom_frac_base();
         Parameters.max_pts_budget = Parameters.max_cons_budget * 2;
         Parameters.cg_refine_times = this.opts.geom_runs();
         if (Parameters.cg_refine_times < 1) {
            Parameters.cg_refine_times = 1;
         }

         this.prepareContainers();
         this.ps.println("[Geom] Start working on <" + (dir == null ? "NoName" : dir.getName()) + "> with <" + encoding_name + "> encoding.");
      }
   }

   private void preprocess() {
      this.n_func = Scene.v().getReachableMethods().size() + 1;
      this.call_graph = new CgEdge[this.n_func];
      this.n_calls = 0;
      this.n_reach_spark_user_methods = 0;
      int id = 1;
      QueueReader<MethodOrMethodContext> smList = Scene.v().getReachableMethods().listener();

      CgEdge intercall;
      for(CallGraph soot_callgraph = Scene.v().getCallGraph(); smList.hasNext(); ++id) {
         SootMethod func = ((MethodOrMethodContext)smList.next()).method();
         this.func2int.put(func, id);
         this.int2func.put(id, func);
         if (soot_callgraph.isEntryMethod(func) || func.isEntryMethod()) {
            intercall = new CgEdge(0, id, (Edge)null, this.call_graph[0]);
            this.call_graph[0] = intercall;
            ++this.n_calls;
         }

         if (!func.isJavaLibraryMethod()) {
            ++this.n_reach_spark_user_methods;
         }
      }

      QueueReader edgeList = Scene.v().getCallGraph().listener();

      while(true) {
         Edge edge;
         do {
            if (!edgeList.hasNext()) {
               Iterator it = this.getVarNodeNumberer().iterator();

               IVarAbstraction obj;
               while(it.hasNext()) {
                  VarNode vn = (VarNode)it.next();
                  obj = this.makeInternalNode(vn);
                  this.pointers.add((Numberable)obj);
               }

               it = this.getAllocDotFieldNodeNumberer().iterator();

               while(true) {
                  AllocDotField adf;
                  RefType decType;
                  IVarAbstraction p;
                  Type baseType;
                  do {
                     if (!it.hasNext()) {
                        it = this.getAllocNodeNumberer().iterator();

                        while(it.hasNext()) {
                           AllocNode obj = (AllocNode)it.next();
                           obj = this.makeInternalNode(obj);
                           this.allocations.add((Numberable)obj);
                        }

                        it = this.allocSources().iterator();

                        int var13;
                        Node[] succs;
                        while(it.hasNext()) {
                           Object object = it.next();
                           obj = this.makeInternalNode((AllocNode)object);
                           Node[] succs = this.allocLookup((AllocNode)object);
                           succs = succs;
                           int var37 = succs.length;

                           for(var13 = 0; var13 < var37; ++var13) {
                              Node element0 = succs[var13];
                              PlainConstraint cons = new PlainConstraint();
                              IVarAbstraction p = this.makeInternalNode(element0);
                              cons.expr.setPair(obj, p);
                              cons.type = 0;
                              this.constraints.add((Numberable)cons);
                           }
                        }

                        Pair<Node, Node> intercall = new Pair();
                        Iterator var29 = this.simpleSources().iterator();

                        Object object;
                        Node[] succs;
                        int var41;
                        Node element0;
                        PlainConstraint cons;
                        while(var29.hasNext()) {
                           object = var29.next();
                           p = this.makeInternalNode((VarNode)object);
                           succs = this.simpleLookup((VarNode)object);
                           succs = succs;
                           var13 = succs.length;

                           for(var41 = 0; var41 < var13; ++var41) {
                              element0 = succs[var41];
                              cons = new PlainConstraint();
                              IVarAbstraction q = this.makeInternalNode(element0);
                              cons.expr.setPair(p, q);
                              cons.type = 1;
                              intercall.setPair((VarNode)object, element0);
                              cons.interCallEdges = this.lookupEdgesForAssignment(intercall);
                              this.constraints.add((Numberable)cons);
                           }
                        }

                        intercall = null;
                        this.assign2edges.clear();
                        var29 = this.loadSources().iterator();

                        IVarAbstraction q;
                        while(var29.hasNext()) {
                           object = var29.next();
                           FieldRefNode frn = (FieldRefNode)object;
                           IVarAbstraction p = this.makeInternalNode(frn.getBase());
                           succs = this.loadLookup(frn);
                           Node[] var40 = succs;
                           var41 = succs.length;

                           for(int var43 = 0; var43 < var41; ++var43) {
                              Node element0 = var40[var43];
                              PlainConstraint cons = new PlainConstraint();
                              q = this.makeInternalNode(element0);
                              cons.f = frn.getField();
                              cons.expr.setPair(p, q);
                              cons.type = 2;
                              this.constraints.add((Numberable)cons);
                           }
                        }

                        var29 = this.storeSources().iterator();

                        while(var29.hasNext()) {
                           object = var29.next();
                           p = this.makeInternalNode((VarNode)object);
                           succs = this.storeLookup((VarNode)object);
                           succs = succs;
                           var13 = succs.length;

                           for(var41 = 0; var41 < var13; ++var41) {
                              element0 = succs[var41];
                              cons = new PlainConstraint();
                              FieldRefNode frn = (FieldRefNode)element0;
                              q = this.makeInternalNode(frn.getBase());
                              cons.f = frn.getField();
                              cons.expr.setPair(p, q);
                              cons.type = 3;
                              this.constraints.add((Numberable)cons);
                           }
                        }

                        this.n_init_constraints = this.constraints.size();
                        this.low_cg = new int[this.n_func];
                        this.vis_cg = new int[this.n_func];
                        this.rep_cg = new int[this.n_func];
                        this.indeg_cg = new int[this.n_func];
                        this.scc_size = new int[this.n_func];
                        this.block_num = new int[this.n_func];
                        this.context_size = new long[this.n_func];
                        this.max_context_size_block = new long[this.n_func];
                        return;
                     }

                     adf = (AllocDotField)it.next();
                     SparkField field = adf.getField();
                     if (!(field instanceof SootField)) {
                        break;
                     }

                     decType = ((SootField)field).getDeclaringClass().getType();
                     baseType = adf.getBase().getType();
                  } while(!this.castNeverFails(baseType, decType));

                  p = this.makeInternalNode(adf);
                  this.pointers.add((Numberable)p);
               }
            }

            edge = (Edge)edgeList.next();
         } while(edge.isClinit());

         SootMethod src_func = edge.src();
         SootMethod tgt_func = edge.tgt();
         int s = (Integer)this.func2int.get(src_func);
         int t = (Integer)this.func2int.get(tgt_func);
         CgEdge p = new CgEdge(s, t, edge, this.call_graph[s]);
         this.call_graph[s] = p;
         this.edgeMapping.put(edge, p);
         Stmt callsite = edge.srcStmt();
         if (!edge.isThreadRunCall() && !edge.kind().isExecutor() && !edge.kind().isAsyncTask()) {
            if (edge.isInstance() && !edge.isSpecial()) {
               InstanceInvokeExpr expr = (InstanceInvokeExpr)callsite.getInvokeExpr();
               if (expr.getMethodRef().getSignature().contains("<java.lang.Thread: void start()>")) {
                  this.thread_run_callsites.add(callsite);
               } else {
                  p.base_var = this.findLocalVarNode(expr.getBase());
                  if (SootInfo.countCallEdgesForCallsite(callsite, true) > 1 && p.base_var != null) {
                     this.multiCallsites.add(callsite);
                  }
               }
            }
         } else {
            this.thread_run_callsites.add(callsite);
         }

         ++this.n_calls;
      }
   }

   private void mergeLocalVariables() {
      int[] count = new int[this.pointers.size()];
      Iterator cons_it = this.constraints.iterator();

      IVarAbstraction my_lhs;
      IVarAbstraction my_rhs;
      Node lhs;
      PlainConstraint cons;
      while(cons_it.hasNext()) {
         cons = (PlainConstraint)cons_it.next();
         my_lhs = cons.getLHS();
         my_rhs = cons.getRHS();
         switch(cons.type) {
         case 0:
         case 1:
            ++count[my_rhs.id];
            break;
         case 2:
            lhs = my_lhs.getWrappedNode();
            int var10001 = my_rhs.id;
            count[var10001] += lhs.getP2Set().size();
         }
      }

      cons_it = this.constraints.iterator();

      while(cons_it.hasNext()) {
         cons = (PlainConstraint)cons_it.next();
         if (cons.type == 1) {
            my_lhs = cons.getLHS();
            my_rhs = cons.getRHS();
            lhs = my_lhs.getWrappedNode();
            Node rhs = my_rhs.getWrappedNode();
            if (lhs instanceof LocalVarNode && rhs instanceof LocalVarNode) {
               SootMethod sm1 = ((LocalVarNode)lhs).getMethod();
               SootMethod sm2 = ((LocalVarNode)rhs).getMethod();
               if (sm1 == sm2 && count[my_rhs.id] == 1 && lhs.getType() == rhs.getType()) {
                  my_rhs.merge(my_lhs);
                  cons_it.remove();
               }
            }
         }
      }

      cons_it = this.constraints.iterator();

      while(cons_it.hasNext()) {
         cons = (PlainConstraint)cons_it.next();
         my_lhs = cons.getLHS();
         my_rhs = cons.getRHS();
         switch(cons.type) {
         case 0:
            cons.setRHS(my_rhs.getRepresentative());
            break;
         case 1:
         case 2:
         case 3:
            cons.setLHS(my_lhs.getRepresentative());
            cons.setRHS(my_rhs.getRepresentative());
         }
      }

   }

   private void callGraphDFS(int s) {
      this.vis_cg[s] = this.low_cg[s] = this.pre_cnt++;
      this.queue_cg.addLast(s);

      int t;
      for(CgEdge p = this.call_graph[s]; p != null; p = p.next) {
         t = p.t;
         if (this.vis_cg[t] == 0) {
            this.callGraphDFS(t);
         }

         if (this.low_cg[t] < this.low_cg[s]) {
            this.low_cg[s] = this.low_cg[t];
         }
      }

      if (this.low_cg[s] < this.vis_cg[s]) {
         this.scc_size[s] = 1;
      } else {
         this.scc_size[s] = this.queue_cg.size();

         int[] var10000;
         do {
            t = (Integer)this.queue_cg.getLast();
            this.queue_cg.removeLast();
            this.rep_cg[t] = s;
            var10000 = this.low_cg;
            var10000[t] += this.n_func;
         } while(s != t);

         var10000 = this.scc_size;
         var10000[s] -= this.queue_cg.size();
         if (this.scc_size[s] > this.max_scc_size) {
            this.max_scc_size = this.scc_size[s];
            this.max_scc_id = s;
         }

      }
   }

   private void encodeContexts(boolean connectMissedEntries) {
      int n_reachable = 0;
      int n_scc_reachable = 0;
      int n_full = 0;
      long max_contexts = Long.MIN_VALUE;
      Random rGen = new Random();
      this.pre_cnt = 1;
      this.max_scc_size = 1;

      int i;
      for(i = 0; i < this.n_func; ++i) {
         this.vis_cg[i] = 0;
         this.indeg_cg[i] = 0;
         this.max_context_size_block[i] = 0L;
      }

      this.queue_cg.clear();
      this.callGraphDFS(0);
      if (connectMissedEntries) {
         for(i = 1; i < this.n_func; ++i) {
            if (this.vis_cg[i] == 0) {
               this.callGraphDFS(i);
            }
         }
      }

      CgEdge p;
      int var10002;
      for(i = 0; i < this.n_func; ++i) {
         if (this.vis_cg[i] != 0) {
            for(p = this.call_graph[i]; p != null; p = p.next) {
               if (this.rep_cg[i] == this.rep_cg[p.t]) {
                  p.scc_edge = true;
               } else {
                  p.scc_edge = false;
                  var10002 = this.indeg_cg[this.rep_cg[p.t]]++;
               }
            }

            ++n_reachable;
            if (this.rep_cg[i] == i) {
               ++n_scc_reachable;
            }
         }
      }

      if (connectMissedEntries) {
         for(i = 1; i < this.n_func; ++i) {
            int rep_node = this.rep_cg[i];
            if (this.indeg_cg[rep_node] == 0) {
               CgEdge p = new CgEdge(0, i, (Edge)null, this.call_graph[0]);
               this.call_graph[0] = p;
               ++this.n_calls;
            }
         }
      }

      for(i = 0; i < this.n_func; ++i) {
         if (this.vis_cg[i] != 0 && this.rep_cg[i] != i) {
            for(p = this.call_graph[i]; p.next != null; p = p.next) {
            }

            p.next = this.call_graph[this.rep_cg[i]];
            this.call_graph[this.rep_cg[i]] = this.call_graph[i];
         }
      }

      this.max_context_size_block[0] = 1L;
      this.queue_cg.addLast(0);

      int j;
      long[] var10000;
      while(!this.queue_cg.isEmpty()) {
         i = (Integer)this.queue_cg.getFirst();
         this.queue_cg.removeFirst();

         for(p = this.call_graph[i]; p != null; p = p.next) {
            if (!p.scc_edge) {
               j = this.rep_cg[p.t];
               if (9223372036854775806L - this.max_context_size_block[i] < this.max_context_size_block[j]) {
                  long start = rGen.nextLong();
                  if (start < 0L) {
                     start = -start;
                  }

                  if (start > 9223372036854775806L - this.max_context_size_block[i]) {
                     start = 9223372036854775806L - this.max_context_size_block[i];
                     this.max_context_size_block[j] = 9223372036854775806L;
                  } else if (this.max_context_size_block[j] < start + this.max_context_size_block[i]) {
                     this.max_context_size_block[j] = start + this.max_context_size_block[i];
                  }

                  p.map_offset = start + 1L;
               } else {
                  p.map_offset = this.max_context_size_block[j] + 1L;
                  var10000 = this.max_context_size_block;
                  var10000[j] += this.max_context_size_block[i];
               }

               if (--this.indeg_cg[j] == 0) {
                  this.queue_cg.addLast(j);
               }
            } else {
               p.map_offset = 1L;
            }
         }

         if (this.max_context_size_block[i] > max_contexts) {
            max_contexts = this.max_context_size_block[i];
         }
      }

      for(i = this.n_func - 1; i > -1; --i) {
         if (this.vis_cg[i] != 0) {
            if (this.rep_cg[i] != i) {
               this.max_context_size_block[i] = this.max_context_size_block[this.rep_cg[i]];

               for(p = this.call_graph[i]; p.next.s == i; p = p.next) {
               }

               this.call_graph[this.rep_cg[i]] = p.next;
               p.next = null;
            }

            if (this.max_context_size_block[i] == 9223372036854775806L) {
               ++n_full;
            }

            this.context_size[i] = this.max_context_size_block[i];
            this.block_num[i] = 1;
         }
      }

      if (this.getOpts().geom_blocking()) {
         for(i = 0; i < this.n_func; ++i) {
            if (this.vis_cg[i] != 0) {
               for(p = this.call_graph[i]; p != null; p = p.next) {
                  j = p.t;
                  if (j != i && p.scc_edge) {
                     if (this.context_size[j] <= 9223372036854775806L - this.max_context_size_block[i]) {
                        p.map_offset = this.context_size[j] + 1L;
                        var10000 = this.context_size;
                        var10000[j] += this.max_context_size_block[i];
                        var10002 = this.block_num[j]++;
                     } else {
                        int iBlock = 0;
                        if (this.block_num[j] > 1) {
                           iBlock = rGen.nextInt(this.block_num[j] - 1) + 1;
                        }

                        p.map_offset = (long)iBlock * this.max_context_size_block[j] + 1L;
                     }
                  }
               }
            }
         }
      }

      this.ps.printf("Reachable Methods = %d, in which #Condensed Nodes = %d, #Full Context Nodes = %d \n", n_reachable - 1, n_scc_reachable - 1, n_full);
      this.ps.printf("Maximum SCC = %d \n", this.max_scc_size);
      this.ps.printf("The maximum context size = %e\n", (double)max_contexts);
   }

   private void solveConstraints() {
      IWorklist ptaList = this.worklist;

      while(ptaList.has_job()) {
         IVarAbstraction pn = ptaList.next();
         pn.do_before_propagation();
         pn.propagate(this, ptaList);
         pn.do_after_propagation();
      }

   }

   private void getCallTargets(IVarAbstraction pn, SootMethod src, Stmt callsite, ChunkedQueue<SootMethod> targetsQueue) {
      InstanceInvokeExpr iie = (InstanceInvokeExpr)callsite.getInvokeExpr();
      Local receiver = (Local)iie.getBase();
      NumberedString subSig = iie.getMethodRef().getSubSignature();
      Iterator var8 = pn.get_all_points_to_objects().iterator();

      while(var8.hasNext()) {
         AllocNode an = (AllocNode)var8.next();
         Type type = an.getType();
         if (type != null) {
            VirtualCalls.v().resolve(type, receiver.getType(), subSig, src, targetsQueue);
         }
      }

   }

   private int updateCallGraph() {
      int all_virtual_edges = 0;
      int n_obsoleted = 0;
      CallGraph cg = Scene.v().getCallGraph();
      ChunkedQueue<SootMethod> targetsQueue = new ChunkedQueue();
      QueueReader<SootMethod> targets = targetsQueue.reader();
      Set<SootMethod> resolvedMethods = new HashSet();
      Iterator csIt = this.multiCallsites.iterator();

      while(true) {
         while(csIt.hasNext()) {
            Stmt callsite = (Stmt)csIt.next();
            Iterator<Edge> edges = cg.edgesOutOf((Unit)callsite);
            if (!edges.hasNext()) {
               csIt.remove();
            } else {
               Edge anyEdge = (Edge)edges.next();
               CgEdge p = (CgEdge)this.edgeMapping.get(anyEdge);
               SootMethod src = anyEdge.src();
               if (!this.isReachableMethod(src)) {
                  csIt.remove();
               } else if (edges.hasNext()) {
                  IVarAbstraction pn = (IVarAbstraction)this.consG.get(p.base_var);
                  if (pn != null) {
                     pn = pn.getRepresentative();
                     this.getCallTargets(pn, src, callsite, targetsQueue);
                     resolvedMethods.clear();

                     while(targets.hasNext()) {
                        resolvedMethods.add(targets.next());
                     }

                     while(true) {
                        SootMethod tgt = anyEdge.tgt();
                        if (!resolvedMethods.contains(tgt) && !anyEdge.kind().isFake()) {
                           p = (CgEdge)this.edgeMapping.get(anyEdge);
                           p.is_obsoleted = true;
                        }

                        if (!edges.hasNext()) {
                           break;
                        }

                        anyEdge = (Edge)edges.next();
                     }
                  }
               }
            }
         }

         for(int i = 1; i < this.n_func; ++i) {
            CgEdge p = this.call_graph[i];

            CgEdge q;
            CgEdge temp;
            for(q = null; p != null; p = temp) {
               if (this.vis_cg[i] == 0) {
                  p.is_obsoleted = true;
               }

               if (p.base_var != null) {
                  ++all_virtual_edges;
               }

               temp = p.next;
               if (!p.is_obsoleted) {
                  p.next = q;
                  q = p;
               } else {
                  cg.removeEdge(p.sootEdge);
                  ++n_obsoleted;
               }
            }

            this.call_graph[i] = q;
         }

         this.ps.printf("%d of %d virtual call edges are proved to be spurious.\n", n_obsoleted, all_virtual_edges);
         return n_obsoleted;
      }
   }

   private void prepareNextRun() {
      Iterator var1 = this.pointers.iterator();

      while(var1.hasNext()) {
         IVarAbstraction pn = (IVarAbstraction)var1.next();
         if (pn.willUpdate) {
            pn.reconstruct();
         }
      }

      System.gc();
   }

   private void markReachableMethods() {
      int ans = 0;

      int i;
      for(i = 0; i < this.n_func; ++i) {
         this.vis_cg[i] = 0;
      }

      this.queue_cg.clear();
      this.queue_cg.add(0);
      this.vis_cg[0] = 1;

      while(this.queue_cg.size() > 0) {
         i = (Integer)this.queue_cg.removeFirst();

         for(CgEdge p = this.call_graph[i]; p != null; p = p.next) {
            int t = p.t;
            if (this.vis_cg[t] == 0) {
               this.queue_cg.add(t);
               this.vis_cg[t] = 1;
               ++ans;
            }
         }
      }

      this.n_reach_methods = ans;
      ans = 0;

      for(i = 1; i < this.n_func; ++i) {
         SootMethod sm = (SootMethod)this.int2func.get(i);
         if (this.vis_cg[i] == 0) {
            this.func2int.remove(sm);
            this.int2func.remove(i);
         } else if (!sm.isJavaLibraryMethod()) {
            ++ans;
         }
      }

      this.n_reach_user_methods = ans;
   }

   private void buildRevCallGraph() {
      this.rev_call_graph = new HashMap();

      for(int i = 0; i < this.n_func; ++i) {
         for(CgEdge p = this.call_graph[i]; p != null; p = p.next) {
            LinkedList<CgEdge> list = (LinkedList)this.rev_call_graph.get(p.t);
            if (list == null) {
               list = new LinkedList();
               this.rev_call_graph.put(p.t, list);
            }

            list.add(p);
         }
      }

   }

   private void finalizeInternalData() {
      this.markReachableMethods();
      Iterator it = this.allocations.iterator();

      while(it.hasNext()) {
         IVarAbstraction po = (IVarAbstraction)it.next();
         AllocNode obj = (AllocNode)po.getWrappedNode();
         SootMethod sm = obj.getMethod();
         if (sm != null && !this.func2int.containsKey(sm)) {
            it.remove();
         }
      }

      final Vector<AllocNode> removeSet = new Vector();
      Iterator cIt = this.pointers.iterator();

      label94:
      while(cIt.hasNext()) {
         IVarAbstraction pn = (IVarAbstraction)cIt.next();
         Node vn = pn.getWrappedNode();
         SootMethod sm = null;
         if (vn instanceof LocalVarNode) {
            sm = ((LocalVarNode)vn).getMethod();
         } else if (vn instanceof AllocDotField) {
            sm = ((AllocDotField)vn).getBase().getMethod();
         }

         if (sm != null && !this.func2int.containsKey(sm)) {
            pn.deleteAll();
            vn.discardP2Set();
            cIt.remove();
         } else if (pn.getRepresentative() == pn) {
            removeSet.clear();
            Iterator oit;
            AllocNode obj;
            if (!pn.hasPTResult()) {
               PointsToSetInternal pts = vn.getP2Set();
               pts.forall(new P2SetVisitor() {
                  public void visit(Node n) {
                     IVarAbstraction pan = GeomPointsTo.this.findInternalNode(n);
                     if (pan.reachable()) {
                        removeSet.add((AllocNode)n);
                     }

                  }
               });
               pts = vn.makeP2Set();
               oit = removeSet.iterator();

               while(oit.hasNext()) {
                  obj = (AllocNode)oit.next();
                  pts.add(obj);
               }
            } else {
               Set<AllocNode> objSet = pn.get_all_points_to_objects();
               oit = objSet.iterator();

               while(true) {
                  IVarAbstraction po;
                  do {
                     if (!oit.hasNext()) {
                        oit = removeSet.iterator();

                        while(oit.hasNext()) {
                           obj = (AllocNode)oit.next();
                           pn.remove_points_to(obj);
                        }

                        pn.drop_duplicates();
                        continue label94;
                     }

                     obj = (AllocNode)oit.next();
                     po = (IVarAbstraction)this.consG.get(obj);
                  } while(po.reachable() && !pn.isDeadObject(obj));

                  removeSet.add(obj);
               }
            }
         }
      }

      cIt = this.constraints.iterator();

      while(true) {
         IVarAbstraction lhs;
         IVarAbstraction rhs;
         do {
            if (!cIt.hasNext()) {
               this.pointers.reassign();
               this.allocations.reassign();
               this.constraints.reassign();
               return;
            }

            PlainConstraint cons = (PlainConstraint)cIt.next();
            lhs = cons.getLHS();
            rhs = cons.getRHS();
         } while(lhs.reachable() && rhs.reachable() && this.getMethodIDFromPtr(lhs) != -1 && this.getMethodIDFromPtr(rhs) != -1);

         cIt.remove();
      }
   }

   private void releaseUselessResources() {
      this.offlineProcessor.destroy();
      this.offlineProcessor = null;
      IFigureManager.cleanCache();
      System.gc();
   }

   private void finalizeSootData() {
      Scene.v().releaseReachableMethods();
      Scene.v().getReachableMethods();
      if (!this.opts.geom_trans()) {
         Iterator var1 = this.pointers.iterator();

         while(var1.hasNext()) {
            IVarAbstraction pn = (IVarAbstraction)var1.next();
            if (pn == pn.getRepresentative() && pn.hasPTResult()) {
               pn.keepPointsToOnly();
               Node vn = pn.getWrappedNode();
               vn.discardP2Set();
            }
         }
      } else {
         this.transformToCIResult();
      }

   }

   public void transformToCIResult() {
      Iterator var1 = this.pointers.iterator();

      while(true) {
         IVarAbstraction pn;
         do {
            if (!var1.hasNext()) {
               this.hasTransformed = true;
               return;
            }

            pn = (IVarAbstraction)var1.next();
         } while(pn.getRepresentative() != pn);

         Node node = pn.getWrappedNode();
         node.discardP2Set();
         PointsToSetInternal ptSet = node.makeP2Set();
         Iterator var5 = pn.get_all_points_to_objects().iterator();

         while(var5.hasNext()) {
            AllocNode obj = (AllocNode)var5.next();
            ptSet.add(obj);
         }

         pn.deleteAll();
      }
   }

   public void solve() {
      long solve_time = 0L;
      long prepare_time = 0L;
      G.v().out.flush();
      this.preprocess();
      this.mergeLocalVariables();
      this.worklist.initialize(this.pointers.size());
      this.offlineProcessor = new OfflineProcessor(this);
      IFigureManager.cleanCache();
      int evalLevel = this.opts.geom_eval();
      GeomEvaluator ge = new GeomEvaluator(this, this.ps);
      if (evalLevel == 1) {
         ge.profileSparkBasicMetrics();
      }

      Date begin = new Date();
      int rounds = 0;

      Date end;
      for(int n_obs = 1000; rounds < Parameters.cg_refine_times && n_obs > 0; ++rounds) {
         this.ps.println("\n[Geom] Propagation Round " + rounds + " ==> ");
         this.encodeContexts(rounds == 0);
         end = new Date();
         this.offlineProcessor.init();
         this.offlineProcessor.defaultFeedPtsRoutines();
         this.offlineProcessor.runOptimizations();
         Date prepare_end = new Date();
         prepare_time += prepare_end.getTime() - end.getTime();
         if (rounds == 0 && evalLevel <= 1) {
            this.offlineProcessor.releaseSparkMem();
         }

         this.prepareNextRun();
         this.nodeGenerator.initFlowGraph(this);
         this.solveConstraints();
         n_obs = this.updateCallGraph();
         this.finalizeInternalData();
      }

      if (rounds < Parameters.cg_refine_times) {
         this.ps.printf("\nThe points-to information has converged. We stop here.\n");
      }

      end = new Date();
      solve_time += end.getTime() - begin.getTime();
      long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      this.ps.println();
      this.ps.printf("[Geom] Preprocessing time: %.2f s\n", (double)prepare_time / 1000.0D);
      this.ps.printf("[Geom] Total time: %.2f s\n", (double)solve_time / 1000.0D);
      this.ps.printf("[Geom] Memory: %.1f MB\n", (double)mem / 1024.0D / 1024.0D);
      if (evalLevel != 0) {
         ge.profileGeomBasicMetrics(evalLevel > 1);
         if (evalLevel > 1) {
            ge.checkCallGraph();
            ge.checkCastsSafety();
            ge.checkAliasAnalysis();
         }
      }

      this.finalizeSootData();
      this.releaseUselessResources();
      this.hasExecuted = true;
   }

   public void ddSolve(Set<Node> qryNodes) {
      long solve_time = 0L;
      long prepare_time = 0L;
      if (!this.hasExecuted) {
         this.solve();
      }

      if (!this.ddPrepared || this.offlineProcessor == null) {
         this.offlineProcessor = new OfflineProcessor(this);
         IFigureManager.cleanCache();
         this.ddPrepared = true;
         this.ps.println();
         this.ps.println("==> Entering demand-driven mode (experimental).");
      }

      int init_size = qryNodes.size();
      if (init_size == 0) {
         this.ps.println("Please provide at least one pointer.");
      } else {
         Date prepare_begin = new Date();
         this.offlineProcessor.init();
         this.offlineProcessor.addUserDefPts(qryNodes);
         this.offlineProcessor.runOptimizations();
         Date prepare_end = new Date();
         prepare_time += prepare_end.getTime() - prepare_begin.getTime();
         Date begin = new Date();
         this.prepareNextRun();
         this.nodeGenerator.initFlowGraph(this);
         this.solveConstraints();
         Date end = new Date();
         solve_time += end.getTime() - begin.getTime();
         this.ps.println();
         this.ps.printf("[ddGeom] Preprocessing time: %.2f seconds\n", (double)prepare_time / 1000.0D);
         this.ps.printf("[ddGeom] Main propagation time: %.2f seconds\n", (double)solve_time / 1000.0D);
      }
   }

   public void cleanResult() {
      this.consG.clear();
      this.pointers.clear();
      this.allocations.clear();
      this.constraints.clear();
      this.func2int.clear();
      this.int2func.clear();
      this.edgeMapping.clear();
      this.hasTransformed = false;
      this.hasExecuted = false;
      System.gc();
      System.gc();
      System.gc();
      System.gc();
   }

   public void keepOnly(Set<IVarAbstraction> usefulPointers) {
      Set<IVarAbstraction> reps = new HashSet();
      Iterator var3 = usefulPointers.iterator();

      IVarAbstraction pn;
      while(var3.hasNext()) {
         pn = (IVarAbstraction)var3.next();
         reps.add(pn.getRepresentative());
      }

      usefulPointers.addAll(reps);
      reps = null;
      var3 = this.pointers.iterator();

      while(var3.hasNext()) {
         pn = (IVarAbstraction)var3.next();
         if (!usefulPointers.contains(pn)) {
            pn.deleteAll();
         }
      }

      System.gc();
   }

   public int getIDFromSootMethod(SootMethod sm) {
      Integer ans = (Integer)this.func2int.get(sm);
      return ans == null ? -1 : ans;
   }

   public SootMethod getSootMethodFromID(int fid) {
      return (SootMethod)this.int2func.get(fid);
   }

   public boolean isReachableMethod(int fid) {
      return fid == -1 ? false : this.vis_cg[fid] != 0;
   }

   public boolean isReachableMethod(SootMethod sm) {
      int id = this.getIDFromSootMethod(sm);
      return this.isReachableMethod(id);
   }

   public boolean isValidMethod(SootMethod sm) {
      if (this.validMethods != null) {
         String sig = sm.toString();
         if (!this.validMethods.containsKey(sig)) {
            return false;
         }

         this.validMethods.put(sig, Boolean.TRUE);
      }

      return true;
   }

   public void outputNotEvaluatedMethods() {
      if (this.validMethods != null) {
         this.ps.println("\nThe following methods are not evaluated because they are unreachable:");
         Iterator var1 = this.validMethods.entrySet().iterator();

         while(var1.hasNext()) {
            Entry<String, Boolean> entry = (Entry)var1.next();
            if (((Boolean)entry.getValue()).equals(Boolean.FALSE)) {
               this.ps.println((String)entry.getKey());
            }
         }

         this.ps.println();
      }

   }

   public Set<SootMethod> getAllReachableMethods() {
      return this.func2int.keySet();
   }

   public CgEdge getCallEgesOutFrom(int fid) {
      return this.call_graph[fid];
   }

   public LinkedList<CgEdge> getCallEdgesInto(int fid) {
      if (this.rev_call_graph == null) {
         this.buildRevCallGraph();
      }

      return (LinkedList)this.rev_call_graph.get(fid);
   }

   public int getMethodIDFromPtr(IVarAbstraction pn) {
      SootMethod sm = null;
      int ret = 0;
      Node node = pn.getWrappedNode();
      if (node instanceof AllocNode) {
         sm = ((AllocNode)node).getMethod();
      } else if (node instanceof LocalVarNode) {
         sm = ((LocalVarNode)node).getMethod();
      } else if (node instanceof AllocDotField) {
         sm = ((AllocDotField)node).getBase().getMethod();
      }

      if (sm != null && this.func2int.containsKey(sm)) {
         int id = (Integer)this.func2int.get(sm);
         if (this.vis_cg[id] == 0) {
            ret = -1;
         } else {
            ret = id;
         }
      }

      return ret;
   }

   public IVarAbstraction makeInternalNode(Node v) {
      IVarAbstraction ret = (IVarAbstraction)this.consG.get(v);
      if (ret == null) {
         ret = this.nodeGenerator.generateNode(v);
         this.consG.put(v, ret);
      }

      return ret;
   }

   public IVarAbstraction findInternalNode(Node v) {
      return (IVarAbstraction)this.consG.get(v);
   }

   public boolean castNeverFails(Type src, Type dst) {
      return this.typeManager.castNeverFails(src, dst);
   }

   public int getNumberOfPointers() {
      return this.pointers.size();
   }

   public int getNumberOfObjects() {
      return this.allocations.size();
   }

   public int getNumberOfSparkMethods() {
      return this.n_func;
   }

   public int getNumberOfMethods() {
      return this.n_reach_methods;
   }

   public IWorklist getWorklist() {
      return this.worklist;
   }

   public IVarAbstraction findInstanceField(AllocNode obj, SparkField field) {
      AllocDotField af = this.findAllocDotField(obj, field);
      return (IVarAbstraction)this.consG.get(af);
   }

   public IVarAbstraction findAndInsertInstanceField(AllocNode obj, SparkField field) {
      AllocDotField af = this.findAllocDotField(obj, field);
      IVarAbstraction pn = null;
      if (af == null) {
         Type decType = ((SootField)field).getDeclaringClass().getType();
         Type baseType = obj.getType();
         if (this.typeManager.castNeverFails(baseType, decType)) {
            af = this.makeAllocDotField(obj, field);
            pn = this.makeInternalNode(af);
            this.pointers.add((Numberable)pn);
         }
      } else {
         pn = (IVarAbstraction)this.consG.get(af);
      }

      return pn;
   }

   public CgEdge getInternalEdgeFromSootEdge(Edge e) {
      return (CgEdge)this.edgeMapping.get(e);
   }

   public boolean isExceptionPointer(Node v) {
      if (v.getType() instanceof RefType) {
         SootClass sc = ((RefType)v.getType()).getSootClass();
         if (!sc.isInterface() && Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sc, Constants.exeception_type.getSootClass())) {
            return true;
         }
      }

      return false;
   }

   public boolean isValidGeometricNode(Node sparkNode) {
      IVarAbstraction pNode = (IVarAbstraction)this.consG.get(sparkNode);
      return pNode != null && pNode.reachable();
   }

   public boolean hasGeomExecuted() {
      return this.hasExecuted;
   }

   public FileOutputStream createOutputFile(String file_name) throws FileNotFoundException {
      return new FileOutputStream(new File(this.dump_dir, file_name));
   }

   private PointsToSetInternal field_p2set(PointsToSet s, final SparkField f) {
      if (!(s instanceof PointsToSetInternal)) {
         throw new RuntimeException("Base pointers must be stored in *PointsToSetInternal*.");
      } else {
         PointsToSetInternal bases = (PointsToSetInternal)s;
         final PointsToSetInternal ret = this.getSetFactory().newSet(f.getType(), this);
         bases.forall(new P2SetVisitor() {
            public final void visit(Node n) {
               Node nDotF = ((AllocNode)n).dot(f);
               if (nDotF != null) {
                  IVarAbstraction pn = (IVarAbstraction)GeomPointsTo.this.consG.get(nDotF);
                  if (pn == null || GeomPointsTo.this.hasTransformed || nDotF.getP2Set() != EmptyPointsToSet.v()) {
                     ret.addAll(nDotF.getP2Set(), (PointsToSetInternal)null);
                     return;
                  }

                  pn = pn.getRepresentative();
                  Iterator var4 = pn.get_all_points_to_objects().iterator();

                  while(var4.hasNext()) {
                     AllocNode obj = (AllocNode)var4.next();
                     ret.add(obj);
                  }
               }

            }
         });
         return ret;
      }
   }

   public PointsToSet reachingObjects(Local l) {
      if (!this.hasExecuted) {
         return super.reachingObjects(l);
      } else {
         LocalVarNode vn = this.findLocalVarNode(l);
         if (vn == null) {
            return EmptyPointsToSet.v();
         } else {
            IVarAbstraction pn = (IVarAbstraction)this.consG.get(vn);
            if (pn == null) {
               return vn.getP2Set();
            } else if (!this.hasTransformed && vn.getP2Set() == EmptyPointsToSet.v()) {
               pn = pn.getRepresentative();
               PointsToSetInternal ptSet = vn.makeP2Set();
               Iterator var5 = pn.get_all_points_to_objects().iterator();

               while(var5.hasNext()) {
                  AllocNode obj = (AllocNode)var5.next();
                  ptSet.add(obj);
               }

               return ptSet;
            } else {
               return vn.getP2Set();
            }
         }
      }
   }

   public PointsToSet reachingObjects(Context c, Local l) {
      if (!this.hasExecuted) {
         return super.reachingObjects(c, l);
      } else if (!this.hasTransformed && c instanceof Unit) {
         LocalVarNode vn = this.findLocalVarNode(l);
         if (vn == null) {
            return EmptyPointsToSet.v();
         } else {
            IVarAbstraction pn = (IVarAbstraction)this.consG.get(vn);
            if (pn == null) {
               return vn.getP2Set();
            } else {
               pn = pn.getRepresentative();
               SootMethod callee = vn.getMethod();
               Edge e = Scene.v().getCallGraph().findEdge((Unit)c, callee);
               if (e == null) {
                  return vn.getP2Set();
               } else {
                  CgEdge myEdge = this.getInternalEdgeFromSootEdge(e);
                  if (myEdge == null) {
                     return vn.getP2Set();
                  } else {
                     long low = myEdge.map_offset;
                     long high = low + this.max_context_size_block[myEdge.s];
                     ContextVarNode cvn = vn.context(c);
                     PointsToSetInternal ptset;
                     if (cvn != null) {
                        ptset = cvn.getP2Set();
                        if (ptset != EmptyPointsToSet.v()) {
                           return ptset;
                        }
                     } else {
                        cvn = this.makeContextVarNode(vn, c);
                     }

                     ptset = cvn.makeP2Set();
                     Iterator var14 = pn.get_all_points_to_objects().iterator();

                     while(var14.hasNext()) {
                        AllocNode an = (AllocNode)var14.next();
                        if (pn.pointer_interval_points_to(low, high, an)) {
                           ptset.add(an);
                        }
                     }

                     return ptset;
                  }
               }
            }
         }
      } else {
         return this.reachingObjects(l);
      }
   }

   public PointsToSet reachingObjects(SootField f) {
      if (!this.hasExecuted) {
         return super.reachingObjects(f);
      } else if (!f.isStatic()) {
         throw new RuntimeException("The parameter f must be a *static* field.");
      } else {
         VarNode vn = this.findGlobalVarNode(f);
         if (vn == null) {
            return EmptyPointsToSet.v();
         } else {
            IVarAbstraction pn = (IVarAbstraction)this.consG.get(vn);
            if (pn == null) {
               return vn.getP2Set();
            } else if (!this.hasTransformed && vn.getP2Set() == EmptyPointsToSet.v()) {
               pn = pn.getRepresentative();
               PointsToSetInternal ptSet = vn.makeP2Set();
               Iterator var5 = pn.getRepresentative().get_all_points_to_objects().iterator();

               while(var5.hasNext()) {
                  AllocNode obj = (AllocNode)var5.next();
                  ptSet.add(obj);
               }

               return ptSet;
            } else {
               return vn.getP2Set();
            }
         }
      }
   }

   public PointsToSet reachingObjects(PointsToSet s, SootField f) {
      return (PointsToSet)(!this.hasExecuted ? super.reachingObjects(s, f) : this.field_p2set(s, f));
   }

   public PointsToSet reachingObjects(Local l, SootField f) {
      return !this.hasExecuted ? super.reachingObjects(l, f) : this.reachingObjects(this.reachingObjects(l), f);
   }

   public PointsToSet reachingObjects(Context c, Local l, SootField f) {
      return !this.hasExecuted ? super.reachingObjects(c, l, f) : this.reachingObjects(this.reachingObjects(c, l), f);
   }

   public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
      return (PointsToSet)(!this.hasExecuted ? super.reachingObjectsOfArrayElement(s) : this.field_p2set(s, ArrayElement.v()));
   }

   public PointsToSet reachingObjects(AllocNode an, SootField f) {
      AllocDotField adf = an.dot(f);
      IVarAbstraction pn = (IVarAbstraction)this.consG.get(adf);
      if (adf == null) {
         return EmptyPointsToSet.v();
      } else if (pn == null) {
         return adf.getP2Set();
      } else if (!this.hasTransformed && adf.getP2Set() == EmptyPointsToSet.v()) {
         pn = pn.getRepresentative();
         PointsToSetInternal ptSet = adf.makeP2Set();
         Iterator var6 = pn.getRepresentative().get_all_points_to_objects().iterator();

         while(var6.hasNext()) {
            AllocNode obj = (AllocNode)var6.next();
            ptSet.add(obj);
         }

         return ptSet;
      } else {
         return adf.getP2Set();
      }
   }
}
