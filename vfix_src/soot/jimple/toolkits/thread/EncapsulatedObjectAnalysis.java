package soot.jimple.toolkits.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootMethod;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class EncapsulatedObjectAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(EncapsulatedObjectAnalysis.class);
   List cachedClasses = new ArrayList();
   List<SootMethod> objectPureMethods = new ArrayList();
   List<SootMethod> objectPureInitMethods = new ArrayList();

   public boolean isMethodPureOnObject(SootMethod sm) {
      if (!this.cachedClasses.contains(sm.getDeclaringClass()) && sm.isConcrete()) {
         SootMethod initMethod = null;
         Collection methods = sm.getDeclaringClass().getMethods();
         Iterator methodsIt = methods.iterator();
         ArrayList mayBePureMethods = new ArrayList(methods.size());

         while(methodsIt.hasNext()) {
            SootMethod method = (SootMethod)methodsIt.next();
            if (method.isConcrete()) {
               if (method.getSubSignature().startsWith("void <init>")) {
                  initMethod = method;
               }

               Body b = method.retrieveActiveBody();
               EncapsulatedMethodAnalysis ema = new EncapsulatedMethodAnalysis(new ExceptionalUnitGraph(b));
               if (ema.isPure()) {
                  mayBePureMethods.add(method);
               }
            }
         }

         if (mayBePureMethods.size() == methods.size()) {
            this.objectPureMethods.addAll(mayBePureMethods);
         } else if (initMethod != null) {
            this.objectPureMethods.add(initMethod);
         }

         if (initMethod != null) {
            this.objectPureInitMethods.add(initMethod);
         }
      }

      return this.objectPureMethods.contains(sm);
   }

   public boolean isInitMethodPureOnObject(SootMethod sm) {
      if (this.isMethodPureOnObject(sm)) {
         boolean ret = this.objectPureInitMethods.contains(sm);
         return ret;
      } else {
         return false;
      }
   }

   public List<SootMethod> getObjectPureMethodsSoFar() {
      return this.objectPureMethods;
   }
}
