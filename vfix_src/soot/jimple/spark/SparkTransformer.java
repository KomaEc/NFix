package soot.jimple.spark;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.ReachingTypeDumper;
import soot.jimple.Stmt;
import soot.jimple.spark.builder.ContextInsensitiveBuilder;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.ondemand.DemandCSPointsTo;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.PAG2HTML;
import soot.jimple.spark.pag.PAGDumper;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.solver.EBBCollapser;
import soot.jimple.spark.solver.PropAlias;
import soot.jimple.spark.solver.PropCycle;
import soot.jimple.spark.solver.PropIter;
import soot.jimple.spark.solver.PropMerge;
import soot.jimple.spark.solver.PropWorklist;
import soot.jimple.spark.solver.Propagator;
import soot.jimple.spark.solver.SCCCollapser;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.options.SparkOptions;
import soot.tagkit.Host;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;

public class SparkTransformer extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(SparkTransformer.class);

   public SparkTransformer(Singletons.Global g) {
   }

   public static SparkTransformer v() {
      return G.v().soot_jimple_spark_SparkTransformer();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      SparkOptions opts = new SparkOptions(options);
      String output_dir = SourceLocator.v().getOutputDir();
      ContextInsensitiveBuilder b = new ContextInsensitiveBuilder();
      if (opts.pre_jimplify()) {
         b.preJimplify();
      }

      if (opts.force_gc()) {
         doGC();
      }

      Date startBuild = new Date();
      PAG pag = b.setup(opts);
      b.build();
      Date endBuild = new Date();
      reportTime("Pointer Assignment Graph", startBuild, endBuild);
      if (opts.force_gc()) {
         doGC();
      }

      Date startTM = new Date();
      pag.getTypeManager().makeTypeMask();
      Date endTM = new Date();
      reportTime("Type masks", startTM, endTM);
      if (opts.force_gc()) {
         doGC();
      }

      if (opts.verbose()) {
         logger.debug("VarNodes: " + pag.getVarNodeNumberer().size());
         logger.debug("FieldRefNodes: " + pag.getFieldRefNodeNumberer().size());
         logger.debug("AllocNodes: " + pag.getAllocNodeNumberer().size());
      }

      Date startSimplify = new Date();
      if (opts.simplify_sccs() && !opts.on_fly_cg() || opts.vta()) {
         (new SCCCollapser(pag, opts.ignore_types_for_sccs())).collapse();
      }

      if (opts.simplify_offline() && !opts.on_fly_cg()) {
         (new EBBCollapser(pag)).collapse();
      }

      pag.cleanUpMerges();
      Date endSimplify = new Date();
      reportTime("Pointer Graph simplified", startSimplify, endSimplify);
      if (opts.force_gc()) {
         doGC();
      }

      PAGDumper dumper = null;
      if (opts.dump_pag() || opts.dump_solution()) {
         dumper = new PAGDumper(pag, output_dir);
      }

      if (opts.dump_pag()) {
         dumper.dump();
      }

      Date startProp = new Date();
      this.propagatePAG(opts, pag);
      Date endProp = new Date();
      reportTime("Propagation", startProp, endProp);
      reportTime("Solution found", startSimplify, endProp);
      if (opts.force_gc()) {
         doGC();
      }

      if (!opts.on_fly_cg() || opts.vta()) {
         CallGraphBuilder cgb = new CallGraphBuilder(pag);
         cgb.build();
      }

      if (opts.verbose()) {
         logger.debug("[Spark] Number of reachable methods: " + Scene.v().getReachableMethods().size());
      }

      if (opts.set_mass()) {
         this.findSetMass(pag);
      }

      if (opts.dump_answer()) {
         (new ReachingTypeDumper(pag, output_dir)).dump();
      }

      if (opts.dump_solution()) {
         dumper.dumpPointsToSets();
      }

      if (opts.dump_html()) {
         (new PAG2HTML(pag, output_dir)).dump();
      }

      Scene.v().setPointsToAnalysis(pag);
      if (opts.add_tags()) {
         this.addTags(pag);
      }

      if (opts.geom_pta()) {
         if (!opts.simplify_offline() && !opts.simplify_sccs()) {
            GeomPointsTo geomPTA = (GeomPointsTo)pag;
            geomPTA.parametrize((double)(endProp.getTime() - startSimplify.getTime()));
            geomPTA.solve();
         } else {
            logger.debug("Please turn off the simplify-offline and simplify-sccs to run the geometric points-to analysis");
            logger.debug("Now, we keep the SPARK result for querying.");
         }
      }

      if (opts.cs_demand()) {
         Date startOnDemand = new Date();
         PointsToAnalysis onDemandAnalysis = DemandCSPointsTo.makeWithBudget(opts.traversal(), opts.passes(), opts.lazy_pts());
         Date endOndemand = new Date();
         reportTime("Initialized on-demand refinement-based context-sensitive analysis", startOnDemand, endOndemand);
         Scene.v().setPointsToAnalysis(onDemandAnalysis);
      }

   }

   protected void propagatePAG(SparkOptions opts, PAG pag) {
      Propagator propagator = null;
      switch(opts.propagator()) {
      case 1:
         propagator = new PropIter(pag);
         break;
      case 2:
         propagator = new PropWorklist(pag);
         break;
      case 3:
         propagator = new PropCycle(pag);
         break;
      case 4:
         propagator = new PropMerge(pag);
         break;
      case 5:
         propagator = new PropAlias(pag);
      case 6:
         break;
      default:
         throw new RuntimeException();
      }

      if (propagator != null) {
         ((Propagator)propagator).propagate();
      }

   }

   protected void addTags(PAG pag) {
      final Tag unknown = new StringTag("Untagged Spark node");
      final Map<Node, Tag> nodeToTag = pag.getNodeTags();
      Iterator var4 = Scene.v().getClasses().iterator();

      label81:
      while(var4.hasNext()) {
         SootClass c = (SootClass)var4.next();
         Iterator var6 = c.getMethods().iterator();

         label79:
         while(true) {
            SootMethod m;
            do {
               do {
                  if (!var6.hasNext()) {
                     continue label81;
                  }

                  m = (SootMethod)var6.next();
               } while(!m.isConcrete());
            } while(!m.hasActiveBody());

            Iterator var8 = m.getActiveBody().getUnits().iterator();

            while(true) {
               final Stmt s;
               Object v;
               do {
                  do {
                     if (!var8.hasNext()) {
                        continue label79;
                     }

                     Unit u = (Unit)var8.next();
                     s = (Stmt)u;
                  } while(!(s instanceof DefinitionStmt));

                  Value lhs = ((DefinitionStmt)s).getLeftOp();
                  v = null;
                  if (lhs instanceof Local) {
                     v = pag.findLocalVarNode(lhs);
                  } else if (lhs instanceof FieldRef) {
                     v = pag.findGlobalVarNode(((FieldRef)lhs).getField());
                  }
               } while(v == null);

               PointsToSetInternal p2set = ((VarNode)v).getP2Set();
               p2set.forall(new P2SetVisitor() {
                  public final void visit(Node n) {
                     SparkTransformer.this.addTag(s, n, nodeToTag, unknown);
                  }
               });
               Node[] simpleSources = pag.simpleInvLookup((VarNode)v);
               Node[] var15 = simpleSources;
               int var16 = simpleSources.length;

               int var17;
               Node element;
               for(var17 = 0; var17 < var16; ++var17) {
                  element = var15[var17];
                  this.addTag(s, element, nodeToTag, unknown);
               }

               simpleSources = pag.allocInvLookup((VarNode)v);
               var15 = simpleSources;
               var16 = simpleSources.length;

               for(var17 = 0; var17 < var16; ++var17) {
                  element = var15[var17];
                  this.addTag(s, element, nodeToTag, unknown);
               }

               simpleSources = pag.loadInvLookup((VarNode)v);
               var15 = simpleSources;
               var16 = simpleSources.length;

               for(var17 = 0; var17 < var16; ++var17) {
                  element = var15[var17];
                  this.addTag(s, element, nodeToTag, unknown);
               }
            }
         }
      }

   }

   protected static void reportTime(String desc, Date start, Date end) {
      long time = end.getTime() - start.getTime();
      logger.debug("[Spark] " + desc + " in " + time / 1000L + "." + time / 100L % 10L + " seconds.");
   }

   protected static void doGC() {
      System.gc();
      System.gc();
      System.gc();
      System.gc();
      System.gc();
   }

   protected void addTag(Host h, Node n, Map<Node, Tag> nodeToTag, Tag unknown) {
      if (nodeToTag.containsKey(n)) {
         h.addTag((Tag)nodeToTag.get(n));
      } else {
         h.addTag(unknown);
      }

   }

   protected void findSetMass(PAG pag) {
      int mass = 0;
      int varMass = 0;
      int adfs = 0;
      int scalars = 0;
      Iterator var6 = pag.getVarNodeNumberer().iterator();

      while(var6.hasNext()) {
         VarNode v = (VarNode)var6.next();
         ++scalars;
         PointsToSetInternal set = v.getP2Set();
         if (set != null) {
            mass += set.size();
         }

         if (set != null) {
            varMass += set.size();
         }
      }

      var6 = pag.allocSources().iterator();

      while(var6.hasNext()) {
         AllocNode an = (AllocNode)var6.next();
         Iterator var16 = an.getFields().iterator();

         while(var16.hasNext()) {
            AllocDotField adf = (AllocDotField)var16.next();
            PointsToSetInternal set = adf.getP2Set();
            if (set != null) {
               mass += set.size();
            }

            if (set != null && set.size() > 0) {
               ++adfs;
            }
         }
      }

      logger.debug("Set mass: " + mass);
      logger.debug("Variable mass: " + varMass);
      logger.debug("Scalars: " + scalars);
      logger.debug("adfs: " + adfs);
      int[] deRefCounts = new int[30001];

      int var10002;
      int size;
      for(Iterator var14 = pag.getDereferences().iterator(); var14.hasNext(); var10002 = deRefCounts[size]++) {
         VarNode v = (VarNode)var14.next();
         PointsToSetInternal set = v.getP2Set();
         size = 0;
         if (set != null) {
            size = set.size();
         }
      }

      int total = 0;
      int[] var18 = deRefCounts;
      int var21 = deRefCounts.length;

      for(size = 0; size < var21; ++size) {
         int element = var18[size];
         total += element;
      }

      logger.debug("Dereference counts BEFORE trimming (total = " + total + "):");

      for(int i = 0; i < deRefCounts.length; ++i) {
         if (deRefCounts[i] > 0) {
            logger.debug("" + i + " " + deRefCounts[i] + " " + (double)deRefCounts[i] * 100.0D / (double)total + "%");
         }
      }

   }
}
