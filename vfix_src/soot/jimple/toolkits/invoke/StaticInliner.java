package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Hierarchy;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ExplicitEdgesPred;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.Targets;
import soot.jimple.toolkits.callgraph.TopologicalOrderer;
import soot.options.Options;
import soot.tagkit.Host;

public class StaticInliner extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(StaticInliner.class);
   private final HashMap<SootMethod, Integer> methodToOriginalSize = new HashMap();

   public StaticInliner(Singletons.Global g) {
   }

   public static StaticInliner v() {
      return G.v().soot_jimple_toolkits_invoke_StaticInliner();
   }

   protected void internalTransform(String phaseName, Map options) {
      Filter explicitInvokesFilter = new Filter(new ExplicitEdgesPred());
      if (Options.v().verbose()) {
         logger.debug("[] Inlining methods...");
      }

      boolean enableNullPointerCheckInsertion = PhaseOptions.getBoolean(options, "insert-null-checks");
      boolean enableRedundantCastInsertion = PhaseOptions.getBoolean(options, "insert-redundant-casts");
      String modifierOptions = PhaseOptions.getString(options, "allowed-modifier-changes");
      float expansionFactor = PhaseOptions.getFloat(options, "expansion-factor");
      int maxContainerSize = PhaseOptions.getInt(options, "max-container-size");
      int maxInlineeSize = PhaseOptions.getInt(options, "max-inlinee-size");
      boolean rerunJb = PhaseOptions.getBoolean(options, "rerun-jb");
      new HashMap();
      CallGraph cg = Scene.v().getCallGraph();
      Hierarchy hierarchy = Scene.v().getActiveHierarchy();
      ArrayList<List<Host>> sitesToInline = new ArrayList();
      this.computeAverageMethodSizeAndSaveOriginalSizes();
      TopologicalOrderer orderer = new TopologicalOrderer(cg);
      orderer.go();
      List<SootMethod> l = orderer.order();
      ListIterator it = l.listIterator(l.size());

      while(true) {
         SootMethod container;
         do {
            do {
               do {
                  if (!it.hasPrevious()) {
                     Iterator sitesIt = sitesToInline.iterator();

                     while(sitesIt.hasNext()) {
                        l = (List)sitesIt.next();
                        SootMethod inlinee = (SootMethod)l.get(0);
                        int inlineeSize = ((JimpleBody)((JimpleBody)inlinee.retrieveActiveBody())).getUnits().size();
                        Stmt invokeStmt = (Stmt)l.get(1);
                        SootMethod container = (SootMethod)l.get(2);
                        int containerSize = ((JimpleBody)((JimpleBody)container.retrieveActiveBody())).getUnits().size();
                        if (inlineeSize + containerSize <= maxContainerSize && inlineeSize <= maxInlineeSize && (float)(inlineeSize + containerSize) <= expansionFactor * (float)(Integer)this.methodToOriginalSize.get(container) && InlinerSafetyManager.ensureInlinability(inlinee, invokeStmt, container, modifierOptions)) {
                           SiteInliner.inlineSite(inlinee, invokeStmt, container, options);
                           if (rerunJb) {
                              PackManager.v().getPack("jb").apply(container.getActiveBody());
                           }
                        }
                     }

                     return;
                  }

                  container = (SootMethod)it.previous();
               } while(this.methodToOriginalSize.get(container) == null);
            } while(!container.isConcrete());
         } while(!explicitInvokesFilter.wrap(cg.edgesOutOf((MethodOrMethodContext)container)).hasNext());

         JimpleBody b = (JimpleBody)container.retrieveActiveBody();
         List<Unit> unitList = new ArrayList();
         unitList.addAll(b.getUnits());
         Iterator unitIt = unitList.iterator();

         while(unitIt.hasNext()) {
            Stmt s = (Stmt)unitIt.next();
            if (s.containsInvokeExpr()) {
               Iterator targets = new Targets(explicitInvokesFilter.wrap(cg.edgesOutOf((Unit)s)));
               if (targets.hasNext()) {
                  SootMethod target = (SootMethod)targets.next();
                  if (!targets.hasNext() && target.getDeclaringClass().isApplicationClass() && target.isConcrete() && InlinerSafetyManager.ensureInlinability(target, s, container, modifierOptions)) {
                     List<Host> l = new ArrayList();
                     l.add(target);
                     l.add(s);
                     l.add(container);
                     sitesToInline.add(l);
                  }
               }
            }
         }
      }
   }

   private void computeAverageMethodSizeAndSaveOriginalSizes() {
      long sum = 0L;
      long count = 0L;
      Iterator classesIt = Scene.v().getApplicationClasses().iterator();

      while(classesIt.hasNext()) {
         SootClass c = (SootClass)classesIt.next();
         Iterator methodsIt = c.methodIterator();

         while(methodsIt.hasNext()) {
            SootMethod m = (SootMethod)methodsIt.next();
            if (m.isConcrete()) {
               int size = ((JimpleBody)m.retrieveActiveBody()).getUnits().size();
               sum += (long)size;
               this.methodToOriginalSize.put(m, new Integer(size));
               ++count;
            }
         }
      }

      if (count != 0L) {
         ;
      }
   }
}
