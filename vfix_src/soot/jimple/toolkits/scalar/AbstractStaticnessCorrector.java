package soot.jimple.toolkits.scalar;

import soot.BodyTransformer;
import soot.RefType;
import soot.SootClass;
import soot.Type;

public abstract class AbstractStaticnessCorrector extends BodyTransformer {
   protected boolean isClassLoaded(SootClass sc) {
      return sc.resolvingLevel() >= 2;
   }

   protected boolean isTypeLoaded(Type tp) {
      if (tp instanceof RefType) {
         RefType rt = (RefType)tp;
         if (rt.hasSootClass()) {
            return this.isClassLoaded(rt.getSootClass());
         }
      }

      return false;
   }
}
