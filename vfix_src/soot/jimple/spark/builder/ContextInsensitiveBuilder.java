package soot.jimple.spark.builder;

import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.internal.SparkNativeHelper;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.solver.OnFlyCallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.jimple.toolkits.pointer.util.NativeMethodDriver;
import soot.options.SparkOptions;
import soot.util.queue.QueueReader;

public class ContextInsensitiveBuilder {
   private static final Logger logger = LoggerFactory.getLogger(ContextInsensitiveBuilder.class);
   protected PAG pag;
   protected CallGraphBuilder cgb;
   protected OnFlyCallGraph ofcg;
   protected ReachableMethods reachables;
   int classes = 0;
   int totalMethods = 0;
   int analyzedMethods = 0;
   int stmts = 0;

   public void preJimplify() {
      boolean change = true;

      while(change) {
         change = false;
         Iterator cIt = (new ArrayList(Scene.v().getClasses())).iterator();

         while(cIt.hasNext()) {
            SootClass c = (SootClass)cIt.next();
            Iterator var4 = c.getMethods().iterator();

            while(var4.hasNext()) {
               SootMethod m = (SootMethod)var4.next();
               if (m.isConcrete() && !m.isNative() && !m.isPhantom() && !m.hasActiveBody()) {
                  change = true;
                  m.retrieveActiveBody();
               }
            }
         }
      }

   }

   public PAG setup(SparkOptions opts) {
      this.pag = (PAG)(opts.geom_pta() ? new GeomPointsTo(opts) : new PAG(opts));
      if (opts.simulate_natives()) {
         this.pag.nativeMethodDriver = new NativeMethodDriver(new SparkNativeHelper(this.pag));
      }

      if (opts.on_fly_cg() && !opts.vta()) {
         this.ofcg = new OnFlyCallGraph(this.pag, opts.apponly());
         this.pag.setOnFlyCallGraph(this.ofcg);
      } else {
         this.cgb = new CallGraphBuilder(DumbPointerAnalysis.v());
      }

      return this.pag;
   }

   public void build() {
      QueueReader callEdges;
      if (this.ofcg != null) {
         callEdges = this.ofcg.callGraph().listener();
         this.ofcg.build();
         this.reachables = this.ofcg.reachableMethods();
         this.reachables.update();
      } else {
         callEdges = this.cgb.getCallGraph().listener();
         this.cgb.build();
         this.reachables = this.cgb.reachables();
      }

      Iterator var2 = Scene.v().getClasses().iterator();

      while(var2.hasNext()) {
         SootClass c = (SootClass)var2.next();
         this.handleClass(c);
      }

      while(true) {
         Edge e;
         do {
            if (!callEdges.hasNext()) {
               if (this.pag.getOpts().verbose()) {
                  logger.debug("Total methods: " + this.totalMethods);
                  logger.debug("Initially reachable methods: " + this.analyzedMethods);
                  logger.debug("Classes with at least one reachable method: " + this.classes);
               }

               return;
            }

            e = (Edge)callEdges.next();
         } while(!e.getTgt().method().getDeclaringClass().isConcrete());

         if (e.tgt().isConcrete() || e.tgt().isNative()) {
            MethodPAG.v(this.pag, e.tgt()).addToPAG((Context)null);
         }

         this.pag.addCallTarget(e);
      }
   }

   protected void handleClass(SootClass c) {
      boolean incedClasses = false;
      if (c.isConcrete()) {
         Iterator var3 = c.getMethods().iterator();

         while(true) {
            SootMethod m;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               m = (SootMethod)var3.next();
            } while(!m.isConcrete() && !m.isNative());

            ++this.totalMethods;
            if (this.reachables.contains(m)) {
               MethodPAG mpag = MethodPAG.v(this.pag, m);
               mpag.build();
               mpag.addToPAG((Context)null);
               ++this.analyzedMethods;
               if (!incedClasses) {
                  incedClasses = true;
                  ++this.classes;
               }
            }
         }
      }
   }
}
