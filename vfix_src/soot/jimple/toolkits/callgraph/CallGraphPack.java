package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.EntryPoints;
import soot.HasPhaseOptions;
import soot.PhaseOptions;
import soot.RadioScenePack;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.CGOptions;

public class CallGraphPack extends RadioScenePack {
   public CallGraphPack(String name) {
      super(name);
   }

   protected void internalApply() {
      CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions((HasPhaseOptions)this));
      if (!Scene.v().hasCustomEntryPoints()) {
         if (!options.implicit_entry()) {
            Scene.v().setEntryPoints(EntryPoints.v().application());
         }

         if (options.all_reachable()) {
            List<SootMethod> entryPoints = new ArrayList();
            entryPoints.addAll(EntryPoints.v().all());
            entryPoints.addAll(EntryPoints.v().methodsOfApplicationClasses());
            Scene.v().setEntryPoints(entryPoints);
         }
      }

      super.internalApply();
      ClinitElimTransformer trimmer = new ClinitElimTransformer();
      if (options.trim_clinit()) {
         Iterator var3 = Scene.v().getClasses(3).iterator();

         while(var3.hasNext()) {
            SootClass cl = (SootClass)var3.next();
            Iterator var5 = cl.getMethods().iterator();

            while(var5.hasNext()) {
               SootMethod m = (SootMethod)var5.next();
               if (m.isConcrete() && m.hasActiveBody()) {
                  trimmer.transform(m.getActiveBody());
               }
            }
         }
      }

   }
}
