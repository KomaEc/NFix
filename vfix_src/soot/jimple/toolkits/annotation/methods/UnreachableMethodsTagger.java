package soot.jimple.toolkits.annotation.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;

public class UnreachableMethodsTagger extends SceneTransformer {
   public UnreachableMethodsTagger(Singletons.Global g) {
   }

   public static UnreachableMethodsTagger v() {
      return G.v().soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger();
   }

   protected void internalTransform(String phaseName, Map options) {
      ArrayList<SootMethod> methodList = new ArrayList();
      Iterator getClassesIt = Scene.v().getApplicationClasses().iterator();

      while(getClassesIt.hasNext()) {
         SootClass appClass = (SootClass)getClassesIt.next();
         Iterator getMethodsIt = appClass.getMethods().iterator();

         while(getMethodsIt.hasNext()) {
            SootMethod method = (SootMethod)getMethodsIt.next();
            if (!Scene.v().getReachableMethods().contains(method)) {
               methodList.add(method);
            }
         }
      }

      Iterator unusedIt = methodList.iterator();

      while(unusedIt.hasNext()) {
         SootMethod unusedMethod = (SootMethod)unusedIt.next();
         unusedMethod.addTag(new StringTag("Method " + unusedMethod.getName() + " is not reachable!", "Unreachable Methods"));
         unusedMethod.addTag(new ColorTag(255, 0, 0, true, "Unreachable Methods"));
      }

   }
}
