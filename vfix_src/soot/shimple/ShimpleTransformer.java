package soot.shimple;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.MethodSource;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class ShimpleTransformer extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ShimpleTransformer.class);

   public ShimpleTransformer(Singletons.Global g) {
   }

   public static ShimpleTransformer v() {
      return G.v().soot_shimple_ShimpleTransformer();
   }

   protected void internalTransform(String phaseName, Map options) {
      if (Options.v().verbose()) {
         logger.debug("Transforming all classes in the Scene to Shimple...");
      }

      Iterator classesIt = Scene.v().getClasses().iterator();

      while(true) {
         SootClass sClass;
         do {
            if (!classesIt.hasNext()) {
               return;
            }

            sClass = (SootClass)classesIt.next();
         } while(sClass.isPhantom());

         Iterator methodsIt = sClass.getMethods().iterator();

         while(methodsIt.hasNext()) {
            SootMethod method = (SootMethod)methodsIt.next();
            if (method.isConcrete()) {
               if (method.hasActiveBody()) {
                  Body body = method.getActiveBody();
                  ShimpleBody sBody = null;
                  if (body instanceof ShimpleBody) {
                     sBody = (ShimpleBody)body;
                     if (!sBody.isSSA()) {
                        sBody.rebuild();
                     }
                  } else {
                     sBody = Shimple.v().newBody(body);
                  }

                  method.setActiveBody(sBody);
               } else {
                  MethodSource ms = new ShimpleMethodSource(method.getSource());
                  method.setSource(ms);
               }
            }
         }
      }
   }
}
